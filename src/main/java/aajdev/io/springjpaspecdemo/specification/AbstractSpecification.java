package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.domain.BaseEntity;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public abstract class AbstractSpecification<T extends BaseEntity> implements Specification<T> {
  private SpecSearchCriteriaDTO criteria;

  public AbstractSpecification(SpecSearchCriteriaDTO criteria) {
    this.criteria = criteria;
  }

  // AAJ - ESTE ME GUSTA, USA RECORD
  static Predicate getPredicate(Root<?> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteriaDTO criteria) {
    return switch (criteria.operation()) {
      case EQUALITY -> criteriaBuilder.equal(root.get(criteria.key()), criteria.value());
      case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.key()), criteria.value());
      case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.key()), criteria.value().toString());
      case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.key()), criteria.value().toString());
      case LIKE -> criteriaBuilder.like(root.get(criteria.key()), criteria.value().toString());
      case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.key()), criteria.value() + "%");
      case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value());
      case CONTAINS -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value() + "%");
      case JOIN_EQUALITY -> criteriaBuilder.equal(root.join(criteria.joinKey(), JoinType.INNER).get(criteria.key()),  criteria.value());
    };
  }

  public SpecSearchCriteriaDTO getCriteria() {
    return criteria;
  }

  public void setCriteria(SpecSearchCriteriaDTO criteria) {
    this.criteria = criteria;
  }
}
