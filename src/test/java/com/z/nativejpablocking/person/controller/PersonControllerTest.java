package com.z.nativejpablocking.person.controller;

import com.z.nativejpablocking.utils.MockHttpUtils;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collection;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
            webTestClient
                    .perform(buildGetPersonResponsePageableRequest(pageRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pageable.pageSize", is(pageRequest.getPageSize())))
                    .andExpect(jsonPath("$.pageable.pageNumber", is(pageRequest.getPageNumber())))
                    .andExpect(jsonPath("$.content[*].id", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].firstName", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].lastName", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].enabled", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].countryISOCode", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].cityName", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.sort.sorted", is(pageRequest.getSort().isSorted())));
        }

        @ParameterizedTest
        @MethodSource("com.z.nativejpablocking.person.controller.PersonControllerTest#provideBadPageRequest")
        @DisplayName("Get Person ErrorHandling")
        void findAllErrorHandling(PageRequest pageRequest) throws Exception {
            webTestClient
                    .perform(buildGetPersonResponsePageableRequest(pageRequest))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", notNullValue()))
                    .andExpect(jsonPath("$.payload", notNullValue()));
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
}