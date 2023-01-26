package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.util.SpecSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;


public class ProductSpecification extends AbstractSpecification<Product>{
  public ProductSpecification(SpecSearchCriteria criteria) {
    super(criteria);
  }

  @Override
  public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return getPredicate(root, criteriaBuilder, super.getCriteria());
  }
}
