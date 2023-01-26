package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProductSpecification extends AbstractSpecification<Product> {
  public ProductSpecification(SpecSearchCriteriaDTO criteria) {
    super(criteria);
  }

  @Override
  public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return getPredicate(root, criteriaBuilder, super.getCriteria());
  }
}
