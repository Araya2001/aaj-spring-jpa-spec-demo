package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class AbstractService<T> {
  protected abstract Specification<T> resolveSpec(List<SpecSearchCriteriaDTO> searchParameters);
}
