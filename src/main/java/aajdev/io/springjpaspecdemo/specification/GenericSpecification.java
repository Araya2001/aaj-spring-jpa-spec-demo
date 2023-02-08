package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import jakarta.persistence.criteria.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

@Log4j2
public class GenericSpecification<T> implements Specification<T> {
  private SpecSearchCriteriaDTO criteria;

  // AAJ - ESTE ME GUSTA, USA RECORD
  static Predicate getPredicate(Root<?> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteriaDTO criteria) {
    try {
      return switch (criteria.operation()) {
        case EQUALITY -> criteriaBuilder.equal(root.get(criteria.key()), criteria.value());
        case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.key()), criteria.value());
        case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.key()), criteria.value().toString());
        case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.key()), criteria.value().toString());
        case LIKE -> criteriaBuilder.like(root.get(criteria.key()), criteria.value().toString());
        case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.key()), criteria.value() + "%");
        case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value());
        case CONTAINS -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value() + "%");
        case JOIN_EQUALITY -> criteriaBuilder.equal(root.join(criteria.joinKey(), JoinType.INNER).get(criteria.key()), criteria.value());
        case GROUP_CRITERIA -> null;
      };
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public SpecSearchCriteriaDTO getCriteria() {
    return criteria;
  }

  public Specification<T> getSpecification(SpecSearchCriteriaDTO criteria) {
    this.criteria = criteria;
    return this;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return getPredicate(root, criteriaBuilder, this.getCriteria());
  }
}
