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

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
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
            var pageSize = pageRequest.getPageSize();
            var pageNumber = pageRequest.getPageNumber();
            var expectedTotalElements = personDAO.count();
            var expectedTotalPages = (int) Math.ceil(expectedTotalElements / (double) pageSize);
            var sort = pageRequest
                    .getSort()
                    .stream()
                    .map(o -> String.format("sort=%s,%s", o.getProperty(), o.getDirection().name()))
                    .collect(Collectors.joining("&"));

            webTestClient
                    .perform(MockHttpUtils.get(String.format("%s?size=%d&page=%d&%s", PERSON_BASE_URL, pageSize, pageNumber, sort)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(Math.toIntExact(expectedTotalElements))))
                    .andExpect(jsonPath("$.totalPages", is(expectedTotalPages)))
                    .andExpect(jsonPath("$.totalPages", is(expectedTotalPages)))
                    .andExpect(jsonPath("$.pageable.pageSize", is(pageSize)))
                    .andExpect(jsonPath("$.pageable.pageNumber", is(pageNumber)))
                    .andExpect(jsonPath("$.content[*].id", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].firstName", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].lastName", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].enabled", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].countryISOCode", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.content[*].cityName", COLLECTION_NOT_EMPTY))
                    .andExpect(jsonPath("$.sort.sorted", is(pageRequest.getSort().isSorted())));

        }
    }

    private static Stream<Arguments> providePageRequest() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 20)),
                Arguments.of(PageRequest.of(0, 5)),
                Arguments.of(PageRequest.of(1, 5)),
                Arguments.of(PageRequest.of(0, 20, Sort.by(Sort.Order.desc("firstName"))))
        );
    }
}