package com.voiceiq.iamservice.search.controller;

import com.voiceiq.iamservice.common.response.ApiResponse;
import com.voiceiq.iamservice.search.dto.*;
import com.voiceiq.iamservice.search.service.SearchService;
import com.voiceiq.iamservice.security.CustomAuthenticationDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Advanced search across sessions, transcripts, and insights")
@SecurityRequirement(name = "Bearer Authentication")
public class SearchController {
    
    private final SearchService searchService;
    
    @Operation(summary = "Global search across all user-accessible content")
    @GetMapping
    public ResponseEntity<ApiResponse<GlobalSearchResponse>> globalSearch(
            @RequestParam String q,
            @RequestParam(required = false) String type, // sessions, transcripts, insights
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) UUID teamId,
            @RequestParam(defaultValue = "20") Integer limit,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        
        GlobalSearchRequest searchRequest = GlobalSearchRequest.builder()
                .query(q)
                .searchType(type)
                .organizationId(organizationId)
                .teamId(teamId)
                .userId(details.getUserId())
                .limit(limit)
                .build();
        
        GlobalSearchResponse response = searchService.globalSearch(searchRequest);
        
        ApiResponse<GlobalSearchResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Search sessions with advanced filters")
    @PostMapping("/sessions")
    public ResponseEntity<ApiResponse<Page<SessionSearchResult>>> searchSessions(
            @Valid @RequestBody SessionSearchRequest request,
            Pageable pageable,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        request.setUserId(details.getUserId());
        
        Page<SessionSearchResult> response = searchService.searchSessions(request, pageable);
        
        ApiResponse<Page<SessionSearchResult>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Search within transcript text")
    @GetMapping("/transcripts")
    public ResponseEntity<ApiResponse<Page<TranscriptSearchResult>>> searchTranscripts(
            @RequestParam String q,
            @RequestParam(required = false) UUID sessionId,
            @RequestParam(required = false) UUID speakerId,
            @RequestParam(required = false) String sentiment,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            Pageable pageable,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        
        TranscriptSearchRequest searchRequest = TranscriptSearchRequest.builder()
                .query(q)
                .sessionId(sessionId)
                .speakerId(speakerId)
                .sentiment(sentiment)
                .startTimeMs(startTime)
                .endTimeMs(endTime)
                .userId(details.getUserId())
                .build();
        
        Page<TranscriptSearchResult> response = searchService.searchTranscripts(searchRequest, pageable);
        
        ApiResponse<Page<TranscriptSearchResult>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Search insights and flags")
    @GetMapping("/insights")
    public ResponseEntity<ApiResponse<Page<InsightSearchResult>>> searchInsights(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String flagType,
            @RequestParam(required = false) Integer minSeverity,
            @RequestParam(required = false) Double minScore,
            @RequestParam(required = false) UUID organizationId,
            Pageable pageable,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        
        InsightSearchRequest searchRequest = InsightSearchRequest.builder()
                .query(q)
                .flagType(flagType)
                .minSeverity(minSeverity)
                .minScore(minScore)
                .organizationId(organizationId)
                .userId(details.getUserId())
                .build();
        
        Page<InsightSearchResult> response = searchService.searchInsights(searchRequest, pageable);
        
        ApiResponse<Page<InsightSearchResult>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get search suggestions and autocomplete")
    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<SearchSuggestionsResponse>> getSearchSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") Integer limit,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        
        SearchSuggestionsResponse response = searchService.getSearchSuggestions(
            q, details.getUserId(), limit);
        
        ApiResponse<SearchSuggestionsResponse> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Save search query for quick access")
    @PostMapping("/saved-searches")
    public ResponseEntity<ApiResponse<SavedSearchResponse>> saveSearch(
            @Valid @RequestBody SaveSearchRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        request.setUserId(details.getUserId());
        
        SavedSearchResponse response = searchService.saveSearch(request);
        
        ApiResponse<SavedSearchResponse> apiResponse = ApiResponse.success(
            response, "Search saved successfully");
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @Operation(summary = "Get user's saved searches")
    @GetMapping("/saved-searches")
    public ResponseEntity<ApiResponse<Page<SavedSearchResponse>>> getSavedSearches(
            Pageable pageable,
            Authentication authentication,
            HttpServletRequest httpRequest) {
        
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        
        Page<SavedSearchResponse> response = searchService.getSavedSearches(
            details.getUserId(), pageable);
        
        ApiResponse<Page<SavedSearchResponse>> apiResponse = ApiResponse.success(response);
        apiResponse.setTraceId((String) httpRequest.getAttribute("traceId"));
        
        return ResponseEntity.ok(apiResponse);
    }
}