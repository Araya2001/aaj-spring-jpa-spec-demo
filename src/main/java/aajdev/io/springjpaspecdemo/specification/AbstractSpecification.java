package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.domain.BaseEntity;
import aajdev.io.springjpaspecdemo.util.SpecSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;


public abstract class AbstractSpecification<T extends BaseEntity> implements Specification<T> {
  private SpecSearchCriteria criteria;

  public AbstractSpecification(SpecSearchCriteria criteria) {
    this.criteria = criteria;
  }

  static Predicate getPredicate(Root<?> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteria criteria) {
    return switch (criteria.getOperation()) {
      case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
      case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
      case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue().toString());
      case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
      case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
      case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
    };
  }

  public SpecSearchCriteria getCriteria() {
    return criteria;
  }

  public void setCriteria(SpecSearchCriteria criteria) {
    this.criteria = criteria;
  }
}
