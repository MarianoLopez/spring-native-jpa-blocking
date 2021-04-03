package com.z.nativejpablocking.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

//https://stackoverflow.com/questions/52490399/spring-boot-page-deserialization-pageimpl-no-constructor
public class RestResponsePage<T> extends PageImpl<T> {
    private static final long serialVersionUID = 3248189030448292002L;
    private JsonNode sort;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
        this.sort = sort;
    }

    public RestResponsePage() {
        super(new ArrayList<>());
    }

    public boolean isSorted() {
        return sort != null && sort.has("sorted") && sort.get("sorted").asBoolean();
    }
}