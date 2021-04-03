package com.z.nativejpablocking.person.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.z.nativejpablocking.person.dto.CreatePersonRequest;
import com.z.nativejpablocking.person.dto.PersonResponse;
import com.z.nativejpablocking.person.dto.UpdatePersonRequest;
import com.z.nativejpablocking.utils.MockHttpUtils;
import com.z.nativejpablocking.utils.RestResponsePage;
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
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PersonControllerTest extends BaseIntegrationTest {
    private static final String PERSON_BASE_URL = "/api/v1/person";

    @DisplayName("GET Person")
    @Nested
    class GetPerson {
        @ParameterizedTest
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#providePageRequest")
        @DisplayName("Should retrieve Page<PersonResponse> from PageableRequest")
        void findAll(PageRequest pageRequest, Map<String, String> queryParams) throws Exception {
            var result = mockMvc
                    .perform(buildGetPersonResponsePageableRequest(pageRequest, queryParams))
                    .andExpect(status().isOk())
                    .andReturn();

            RestResponsePage<PersonResponse> response = mapPage(result, new TypeReference<>() {
            });
            assertThat(response, notNullValue());
            assertThat(response.getSize(), is(pageRequest.getPageSize()));
            assertThat(response.getNumber(), is(pageRequest.getPageNumber()));
            assertThat(response.isSorted(), is(pageRequest.getSort().isSorted()));
            assertThat(response.getContent(), notNullValue());
            response.forEach(PersonControllerTest.this::commonPersonResponseAssertions);
        }

        @ParameterizedTest
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideBadPageRequest")
        @DisplayName("Get Person ErrorHandling")
        void findAllErrorHandling(PageRequest pageRequest, Map<String, String> queryParams) throws Exception {
            mockMvc
                    .perform(buildGetPersonResponsePageableRequest(pageRequest, queryParams))
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

        private MockHttpServletRequestBuilder buildGetPersonResponsePageableRequest(PageRequest pageRequest, Map<String, String> queryParams) {
            var requestBuilder = MockHttpUtils.get(PERSON_BASE_URL)
                    .param("size", String.valueOf(pageRequest.getPageSize()))
                    .param("page", String.valueOf(pageRequest.getPageNumber()));

            if (queryParams != null && !queryParams.isEmpty()) {
                queryParams.forEach(requestBuilder::param);
            }

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

        @ParameterizedTest
        @DisplayName("Save Person with invalid request body should return bad request")
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideInvalidCreatePersonRequest")
        void saveShouldReturnBadRequest(CreatePersonRequest createPersonRequest) throws Exception {
            mockMvc
                    .perform(MockHttpUtils.post(PERSON_BASE_URL, asByteArray(createPersonRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("MethodArgumentNotValidException")));
        }
    }

    @DisplayName("PUT Person")
    @Nested
    class UpdatePerson {
        @ParameterizedTest
        @DisplayName("Update existing person")
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideUpdatePersonRequest")
        void update(UpdatePersonRequest updatePersonRequest) throws Exception {
            var now = LocalDateTime.now();
            var sseMvcResult = getSseMvcResult();
            var result = mockMvc
                    .perform(MockHttpUtils.put(PERSON_BASE_URL, asByteArray(updatePersonRequest)))
                    .andExpect(status().isAccepted())
                    .andReturn();

            var personResponse = map(result, PersonResponse.class);
            commonPersonResponseAssertions(personResponse);
            assertThat(personResponse.getLastName(), is(updatePersonRequest.getLastName()));
            assertThat(personResponse.getFirstName(), is(updatePersonRequest.getFirstName()));
            assertThat(personResponse.getCountryISOCode(), is(updatePersonRequest.getCountryISOCode()));
            assertThat(personResponse.getCityName(), is(updatePersonRequest.getCityName()));
            assertThat(personResponse.getJobs(), is(updatePersonRequest.getJobs()));
            assertThat(personResponse.getCreatedDate(), lessThanOrEqualTo(now));
            assertThat(personResponse.getLastModifiedDate(), greaterThanOrEqualTo(now));

            personStreamAssertions(
                    asyncDispatch(sseMvcResult),
                    result.getResponse().getContentAsString(),
                    "UpdatePersonEvent");
        }


        @ParameterizedTest
        @DisplayName("Update existing person")
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideInvalidUpdatePersonRequest")
        void updateWithInvalidRequest(UpdatePersonRequest updatePersonRequest) throws Exception {
            mockMvc
                    .perform(MockHttpUtils.put(PERSON_BASE_URL, asByteArray(updatePersonRequest)))
                    .andExpect(status().isBadRequest());
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
                Arguments.of(PageRequest.of(0, 20), emptyMap()),
                Arguments.of(PageRequest.of(0, 5), emptyMap()),
                Arguments.of(PageRequest.of(1, 5), emptyMap()),
                Arguments.of(PageRequest.of(0, 20, Sort.by(desc("firstName"))), emptyMap()),
                Arguments.of(PageRequest.of(0, 100, Sort.by(desc("lastName"), asc("city.id.name"))), emptyMap()),
                Arguments.of(PageRequest.of(0, 100, Sort.by(desc("lastName"), asc("city.id.name"))), Map.of("lastName", "test")),
                Arguments.of(PageRequest.of(0, 100, Sort.by(desc("lastName"), asc("city.id.name"))), Map.of("lastName", "no-one"))
        );
    }

    private static Stream<Arguments> provideBadPageRequest() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 20, Sort.by(desc("unknownField"))), emptyMap())
        );
    }

    private static Stream<Arguments> provideUpdatePersonRequest() {
        return IntStream.range(1, 11).mapToObj(i -> Arguments.of(createUpdatePersonRequest(i)));
    }


    private static Stream<Arguments> provideInvalidUpdatePersonRequest() {
        var mockString = "unknown entity";
        return Stream.of(
                Arguments.of(UpdatePersonRequest.builder().id(null).build()),
                Arguments.of(UpdatePersonRequest.builder().id(0L).build()),
                Arguments.of(UpdatePersonRequest
                        .builder()
                        .id(99999L)
                        .firstName(mockString)
                        .lastName(mockString)
                        .cityName(mockString)
                        .countryISOCode(mockString)
                        .build()),
                Arguments.of(UpdatePersonRequest.builder().id(1L).firstName("").build()),
                Arguments.of(UpdatePersonRequest.builder().id(1L).lastName("").build()),
                Arguments.of(UpdatePersonRequest.builder().id(1L).cityName("").build()),
                Arguments.of(UpdatePersonRequest.builder().id(1L).countryISOCode("").build())
        );
    }

    private static UpdatePersonRequest createUpdatePersonRequest(int id) {
        var cities = citiesByCountry.get(DEFAULT_COUNTRY);

        return UpdatePersonRequest.builder()
                .id((long) id)
                .firstName("TestFistName_" + id)
                .lastName("TesLastName_" + id)
                .countryISOCode(DEFAULT_COUNTRY)
                .cityName(cities.get(randomNumberBetween(0, cities.size())))
                .jobs(Collections.emptySet())
                .build();
    }

    private static Stream<Arguments> provideCreatePersonRequest() {
        return IntStream.range(1, 11).mapToObj(i -> Arguments.of(randomCreatePersonRequest()));
    }

    private static Stream<Arguments> provideInvalidCreatePersonRequest() {
        return Stream.of(
                Arguments.of(CreatePersonRequest.builder().build()),
                Arguments.of(CreatePersonRequest.builder().firstName("").build()),
                Arguments.of(CreatePersonRequest.builder().lastName("").build()),
                Arguments.of(CreatePersonRequest.builder().lastName("lastName").build()),
                Arguments.of(CreatePersonRequest.builder().firstName("firstName").lastName("lastName").build()),
                Arguments.of(CreatePersonRequest.builder().cityName("cityName").firstName("firstName").lastName("lastName").build()),
                Arguments.of(CreatePersonRequest.builder().countryISOCode("").cityName("cityName").firstName("firstName").lastName("lastName").build())
        );
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