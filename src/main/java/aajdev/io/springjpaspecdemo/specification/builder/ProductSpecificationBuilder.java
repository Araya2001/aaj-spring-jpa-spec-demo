package aajdev.io.springjpaspecdemo.specification.builder;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.dto.SearchOperation;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import aajdev.io.springjpaspecdemo.specification.ProductSpecification;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class ProductSpecificationBuilder {
  private final List<SpecSearchCriteriaDTO> params;

  public ProductSpecificationBuilder(List<SpecSearchCriteriaDTO> params) {
    this.params = params;
  }

  public Specification<Product> build() {
    if (!params.isEmpty()) {
      AtomicReference<Specification<Product>> specificationAtomicReference = new AtomicReference<>(new ProductSpecification(params.get(0)));
      params.forEach(criteria -> {
        log.info("PRODUCT - CRITERIA: " + criteria);
        if (criteria.groupCriteria() != null && criteria.operation() == SearchOperation.GROUP_CRITERIA) {
          criteria.groupCriteria().forEach(groupCriteria -> specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new ProductSpecification(groupCriteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new ProductSpecification(groupCriteria))));
        } else {
          specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new ProductSpecification(criteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new ProductSpecification(criteria)));
        }
      });
      return specificationAtomicReference.get();
    }
    return null;
  }
}
