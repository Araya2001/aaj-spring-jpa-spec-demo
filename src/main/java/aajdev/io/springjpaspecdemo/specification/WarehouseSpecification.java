package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.util.SpecSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class WarehouseSpecification extends AbstractSpecification<Warehouse> {
  public WarehouseSpecification(SpecSearchCriteria criteria) {
    super(criteria);
  }

  @Override
  public Predicate toPredicate(Root<Warehouse> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return getPredicate(root, criteriaBuilder, super.getCriteria());
  }
}
