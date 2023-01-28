package aajdev.io.springjpaspecdemo.specification.builder;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.dto.SearchOperation;
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
    AtomicReference<Specification<Warehouse>> specificationAtomicReference = new AtomicReference<>(new WarehouseSpecification(params.get(0)));
    AtomicReference<Specification<Warehouse>> groupSpecificationAtomicReference = new AtomicReference<>();
    if (!params.isEmpty()) {
      params.forEach(criteria -> {
        log.info("WAREHOUSE - CRITERIA: " + criteria);
        if (criteria.operation() != SearchOperation.GROUP_CRITERIA) {
          specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new WarehouseSpecification(criteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new WarehouseSpecification(criteria)));
        } else if (!criteria.groupCriteria().isEmpty()) {
          groupSpecificationAtomicReference.set(new WarehouseSpecification(criteria.groupCriteria().get(0)));
          criteria.groupCriteria().forEach(groupCriteria -> groupSpecificationAtomicReference
              .set(groupCriteria.orPredicate() ?
                  Specification.where(groupSpecificationAtomicReference.get()).or(new WarehouseSpecification(groupCriteria)) :
                  Specification.where(groupSpecificationAtomicReference.get()).and(new WarehouseSpecification(groupCriteria))));
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
