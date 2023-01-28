package aajdev.io.springjpaspecdemo.specification.builder;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import aajdev.io.springjpaspecdemo.specification.WarehouseSpecification;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class WarehouseSpecificationBuilder {
  private final List<SpecSearchCriteriaDTO> params;

  public WarehouseSpecificationBuilder(List<SpecSearchCriteriaDTO> params) {
    this.params = params;
  }

  public Specification<Warehouse> build() {
    if (!params.isEmpty()) {
      AtomicReference<Specification<Warehouse>> specificationAtomicReference = new AtomicReference<>(new WarehouseSpecification(params.get(0)));
      params.forEach(criteria -> {
        log.info("WAREHOUSE - CRITERIA: " + criteria);
        if (criteria.groupCriteria() != null) {
          criteria.groupCriteria().forEach(groupCriteria -> specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new WarehouseSpecification(groupCriteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new WarehouseSpecification(groupCriteria))));
        } else {
          specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new WarehouseSpecification(criteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new WarehouseSpecification(criteria)));
        }
      });
      return specificationAtomicReference.get();
    }
    return null;
  }
}
