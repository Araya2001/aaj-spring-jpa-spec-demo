package aajdev.io.springjpaspecdemo.service;

import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractService<T> {
  protected abstract Specification<T> resolveSpec(String searchParameters);
}
