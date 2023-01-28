package aajdev.io.springjpaspecdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum SearchOperation {
  @JsonProperty("EQUALITY")
  EQUALITY,
  @JsonProperty("NEGATION")
  NEGATION,
  @JsonProperty("GREATER_THAN")
  GREATER_THAN,
  @JsonProperty("LESS_THAN")
  LESS_THAN,
  @JsonProperty("LIKE")
  LIKE,
  @JsonProperty("STARTS_WITH")
  STARTS_WITH,
  @JsonProperty("ENDS_WITH")
  ENDS_WITH,
  @JsonProperty("CONTAINS")
  CONTAINS,
  @JsonProperty("JOIN_EQUALITY")
  JOIN_EQUALITY,
  @JsonProperty("GROUP_CRITERIA")
  GROUP_CRITERIA
}
