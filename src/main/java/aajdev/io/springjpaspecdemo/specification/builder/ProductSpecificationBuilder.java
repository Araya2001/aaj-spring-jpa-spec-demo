package aajdev.io.springjpaspecdemo.specification.builder;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.specification.ProductSpecification;
import aajdev.io.springjpaspecdemo.specification.util.SpecSearchCriteria;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class ProductSpecificationBuilder extends AbstractSpecificationBuilder {
  private final List<SpecSearchCriteria> params;

  public ProductSpecificationBuilder() {
    params = new ArrayList<>();
  }

  public final ProductSpecificationBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
    loadParams(params, null, key, operation, value, prefix, suffix);
    return this;
  }

  public final ProductSpecificationBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
    loadParams(params, orPredicate, key, operation, value, prefix, suffix);
    return this;
  }

  public Specification<Product> build() {
    if (!params.isEmpty()) {
      AtomicReference<Specification<Product>> specificationAtomicReference = new AtomicReference<>(new ProductSpecification(params.get(0)));
      params.forEach(criteria -> {
        log.info("PRODUCT - CRITERIA: " + criteria);
        specificationAtomicReference
            .set(criteria.isOrPredicate() ?
                Specification.where(specificationAtomicReference.get()).or(new ProductSpecification(criteria)) :
                Specification.where(specificationAtomicReference.get()).and(new ProductSpecification(criteria)));
      });
      return specificationAtomicReference.get();
    }
    return null;
  }
}
