package com.z.nativejpablocking.person.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.utils.MockHttpUtils;
import com.z.nativejpablocking.utils.RestResponsePage;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PersonControllerTest extends BaseIntegrationTest {
    private static final String PERSON_BASE_URL = "/api/v1/person";
    private static final Matcher<Collection<?>> COLLECTION_NOT_EMPTY = not(empty());

    @DisplayName("GET Person")
    @Nested
    class GetPerson {
        @ParameterizedTest
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#providePageRequest")
        @DisplayName("Should retrieve Page<PersonResponse> from PageableRequest")
        void findAll(PageRequest pageRequest) throws Exception {
            var result = mockMvc
                    .perform(buildGetPersonResponsePageableRequest(pageRequest))
                    .andExpect(status().isOk())
                    .andReturn();

            RestResponsePage<PersonResponse> response = mapPage(result, new TypeReference<>() {
            });
            assertThat(response, notNullValue());
            assertThat(response.getSize(), is(pageRequest.getPageSize()));
            assertThat(response.getNumber(), is(pageRequest.getPageNumber()));
            assertThat(response.isSorted(), is(pageRequest.getSort().isSorted()));
            assertThat(response.getContent(), COLLECTION_NOT_EMPTY);
            response.forEach(PersonControllerTest.this::commonPersonResponseAssertions);
        }

        @ParameterizedTest
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideBadPageRequest")
        @DisplayName("Get Person ErrorHandling")
        void findAllErrorHandling(PageRequest pageRequest) throws Exception {
            mockMvc
                    .perform(buildGetPersonResponsePageableRequest(pageRequest))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", notNullValue()))
                    .andExpect(jsonPath("$.payload", notNullValue()));
        }

        @ParameterizedTest
        @DisplayName("Get Person by ID")
        @ValueSource(longs = {1L, 2L, 3L})
        void findById(Long userId) throws Exception {
            var result = mockMvc
                    .perform(MockHttpUtils.get(PERSON_BASE_URL + "/" + userId))
                    .andExpect(status().isOk())
                    .andReturn();

            var personResponse = map(result, PersonResponse.class);
            commonPersonResponseAssertions(personResponse);
            assertThat(personResponse.getId(), is(userId));
        }

        @ParameterizedTest
        @DisplayName("Get Person with UnExists ID")
        @ValueSource(longs = {-999L})
        void findByIdWithUnExistsId(Long userId) throws Exception {
            mockMvc
                    .perform(MockHttpUtils.get(PERSON_BASE_URL + "/" + userId))
                    .andExpect(status().isNoContent());
        }

        private MockHttpServletRequestBuilder buildGetPersonResponsePageableRequest(PageRequest pageRequest) {
            var requestBuilder = MockHttpUtils.get(PERSON_BASE_URL)
                    .param("size", String.valueOf(pageRequest.getPageSize()))
                    .param("page", String.valueOf(pageRequest.getPageNumber()));
            pageRequest
                    .getSort()
                    .stream()
                    .forEach(o -> requestBuilder.param("sort", o.getProperty() + "," + o.getDirection().name()));

            return requestBuilder;
        }
    }

    @DisplayName("POST Person")
    @Nested
    class CreatePerson {
        @ParameterizedTest
        @DisplayName("Save Person")
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideCreatePersonRequest")
        void save(CreatePersonRequest createPersonRequest) throws Exception {
            var now = LocalDateTime.now();
            var sseMvcResult = getSseMvcResult();
            var result = mockMvc
                    .perform(MockHttpUtils.post(PERSON_BASE_URL, asByteArray(createPersonRequest)))
                    .andExpect(status().isCreated())
                    .andReturn();

            var personResponse = map(result, PersonResponse.class);
            commonPersonResponseAssertions(personResponse);
            assertThat(personResponse.getEnabled(), is(true));
            assertThat(personResponse.getLastName(), is(createPersonRequest.getLastName()));
            assertThat(personResponse.getFirstName(), is(createPersonRequest.getFirstName()));
            assertThat(personResponse.getCountryISOCode(), is(createPersonRequest.getCountryISOCode()));
            assertThat(personResponse.getCityName(), is(createPersonRequest.getCityName()));
            assertThat(personResponse.getJobs(), is(createPersonRequest.getJobs()));
            assertThat(personResponse.getCreatedDate(), greaterThanOrEqualTo(now));
            assertThat(personResponse.getLastModifiedDate(), greaterThanOrEqualTo(now));

            personStreamAssertions(
                    asyncDispatch(sseMvcResult),
                    result.getResponse().getContentAsString(),
                    "CreatePersonEvent");
        }
    }

    private void personStreamAssertions(ResultActions streamResult, String body, String eventName) throws Exception {
        streamResult
                .andExpect(content().string(containsString(body)))
                .andExpect(content().string(containsString("event:" + eventName)));
    }

    private MvcResult getSseMvcResult() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(PERSON_BASE_URL + "/stream")
                .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(request().asyncStarted())
                .andReturn();
    }

    private void commonPersonResponseAssertions(PersonResponse personResponse) {
        assertThat(personResponse, notNullValue());
        assertThat(personResponse.getId(), notNullValue());
        assertThat(personResponse.getFirstName(), not(emptyOrNullString()));
        assertThat(personResponse.getLastName(), not(emptyOrNullString()));
        assertThat(personResponse.getCityName(), not(emptyOrNullString()));
        assertThat(personResponse.getCountryISOCode(), not(emptyOrNullString()));
        assertThat(personResponse.getEnabled(), notNullValue());
        assertThat(personResponse.getLastModifiedDate(), notNullValue());
        assertThat(personResponse.getCreatedDate(), notNullValue());
    }

    private static Stream<Arguments> providePageRequest() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 20)),
                Arguments.of(PageRequest.of(0, 5)),
                Arguments.of(PageRequest.of(1, 5)),
                Arguments.of(PageRequest.of(0, 20, Sort.by(desc("firstName")))),
                Arguments.of(PageRequest.of(0, 100, Sort.by(desc("lastName"), asc("city.id.name"))))
        );
    }

    private static Stream<Arguments> provideBadPageRequest() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 20, Sort.by(desc("unknownField"))))
        );
    }

    private static Stream<Arguments> provideCreatePersonRequest() {
        return IntStream.range(1, 11).mapToObj(i -> Arguments.of(randomCreatePersonRequest()));
    }

    private static CreatePersonRequest randomCreatePersonRequest() {
        var id = UUID.randomUUID().toString();
        var cities = citiesByCountry.get(DEFAULT_COUNTRY);
        return CreatePersonRequest.builder()
                .firstName("TestFistName_" + id)
                .lastName("TesLastName_" + id)
                .countryISOCode(DEFAULT_COUNTRY)
                .cityName(cities.get(randomNumberBetween(0, cities.size())))
                .jobs(Collections.emptySet())
                .build();
    }
}