package com.voiceiq.iamservice.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchSuggestionsResponse {
    private String query;
    private List<String> suggestions;
    private List<String> recentSearches;
    private List<String> popularSearches;
}