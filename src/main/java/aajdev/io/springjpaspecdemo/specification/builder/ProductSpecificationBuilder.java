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
    AtomicReference<Specification<Product>> specificationAtomicReference = new AtomicReference<>(new ProductSpecification(params.get(0)));
    AtomicReference<Specification<Product>> groupSpecificationAtomicReference = new AtomicReference<>();
    if (!params.isEmpty()) {
      params.forEach(criteria -> {
        log.info("PRODUCT - CRITERIA: " + criteria);
        if (criteria.operation() != SearchOperation.GROUP_CRITERIA) {
          specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new ProductSpecification(criteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new ProductSpecification(criteria)));
        } else if (!criteria.groupCriteria().isEmpty()) {
          groupSpecificationAtomicReference.set(new ProductSpecification(criteria.groupCriteria().get(0)));
          criteria.groupCriteria().forEach(groupCriteria -> groupSpecificationAtomicReference
              .set(groupCriteria.orPredicate() ?
                  Specification.where(groupSpecificationAtomicReference.get()).or(new ProductSpecification(groupCriteria)) :
                  Specification.where(groupSpecificationAtomicReference.get()).and(new ProductSpecification(groupCriteria))));
          specificationAtomicReference.set(criteria.orPredicate() ?
              Specification.where(specificationAtomicReference.get()).or(groupSpecificationAtomicReference.get()) :
              Specification.where(specificationAtomicReference.get()).and(groupSpecificationAtomicReference.get()));
        }
      });
      return specificationAtomicReference.get();
    }
    return null;
  }
}
