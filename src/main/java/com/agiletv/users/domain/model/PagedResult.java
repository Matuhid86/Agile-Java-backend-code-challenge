package com.agiletv.users.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@lombok.Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResult<T> {
    @JsonProperty("content")
    private List<T> content;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("total_elements")
    private Long totalElements;
    @JsonProperty("total_pages")
    private Integer totalPages;
}