package com.voiceiq.iamservice.search.service;

import com.voiceiq.iamservice.search.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SearchService {
    
    /**
     * Global search across all content types
     */
    GlobalSearchResponse globalSearch(GlobalSearchRequest request);
    
    /**
     * Advanced session search with filters
     */
    Page<SessionSearchResult> searchSessions(SessionSearchRequest request, Pageable pageable);
    
    /**
     * Search within transcript text
     */
    Page<TranscriptSearchResult> searchTranscripts(TranscriptSearchRequest request, Pageable pageable);
    
    /**
     * Search insights and flags
     */
    Page<InsightSearchResult> searchInsights(InsightSearchRequest request, Pageable pageable);
    
    /**
     * Get search suggestions and autocomplete
     */
    SearchSuggestionsResponse getSearchSuggestions(String query, UUID userId, Integer limit);
    
    /**
     * Save search query for quick access
     */
    SavedSearchResponse saveSearch(SaveSearchRequest request);
    
    /**
     * Get user's saved searches
     */
    Page<SavedSearchResponse> getSavedSearches(UUID userId, Pageable pageable);
}