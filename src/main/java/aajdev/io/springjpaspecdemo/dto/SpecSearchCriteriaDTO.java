package aajdev.io.springjpaspecdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.util.List;

@Builder
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpecSearchCriteriaDTO(
    @JsonProperty("key")
    String key,
    @JsonProperty("join_key")
    String joinKey,
    @JsonProperty("search_operation")
    SearchOperation operation,
    @JsonProperty("value")
    Object value,
    @JsonProperty("is_or_predicate")
    Boolean orPredicate,
    @JsonProperty("group_criteria")
    List<SpecSearchCriteriaDTO> groupCriteria) {
}
