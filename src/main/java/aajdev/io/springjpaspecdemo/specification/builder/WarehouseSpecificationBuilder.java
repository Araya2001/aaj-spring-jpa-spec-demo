package aajdev.io.springjpaspecdemo.specification.builder;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.specification.WarehouseSpecification;
import aajdev.io.springjpaspecdemo.specification.util.SpecSearchCriteria;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class WarehouseSpecificationBuilder extends AbstractSpecificationBuilder {
  private final List<SpecSearchCriteria> params;

  public WarehouseSpecificationBuilder() {
    params = new ArrayList<>();
  }

  public final WarehouseSpecificationBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
    loadParams(params, null, key, operation, value, prefix, suffix);
    return this;
  }

  public final WarehouseSpecificationBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
    loadParams(params, orPredicate, key, operation, value, prefix, suffix);
    return this;
  }

  public Specification<Warehouse> build() {
    if (!params.isEmpty()) {
      AtomicReference<Specification<Warehouse>> specificationAtomicReference = new AtomicReference<>(new WarehouseSpecification(params.get(0)));
      params.forEach(criteria -> {
        log.info("WAREHOUSE - CRITERIA: " + criteria);
        specificationAtomicReference
            .set(criteria.isOrPredicate() ?
                Specification.where(specificationAtomicReference.get()).or(new WarehouseSpecification(criteria)) :
                Specification.where(specificationAtomicReference.get()).and(new WarehouseSpecification(criteria)));
      });
      return specificationAtomicReference.get();
    }
    return null;
  }
}
