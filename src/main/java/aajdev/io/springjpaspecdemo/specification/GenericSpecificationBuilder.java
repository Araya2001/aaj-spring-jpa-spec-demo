package aajdev.io.springjpaspecdemo.specification;

import aajdev.io.springjpaspecdemo.dto.SearchOperation;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class GenericSpecificationBuilder<T> {
  public Specification<T> buildFromParams(List<SpecSearchCriteriaDTO> params) {
    try {
      AtomicReference<Specification<T>> specificationAtomicReference = new AtomicReference<>(new GenericSpecification<T>().getSpecification(params.get(0)));
      AtomicReference<Specification<T>> groupSpecificationAtomicReference = new AtomicReference<>();
      if (!params.isEmpty()) {
        params.forEach(criteria -> {
          if (criteria.operation() != SearchOperation.GROUP_CRITERIA) {
            specificationAtomicReference
                .set(criteria.orPredicate() ?
                    Specification.where(specificationAtomicReference.get()).or(new GenericSpecification<T>().getSpecification(criteria)) :
                    Specification.where(specificationAtomicReference.get()).and(new GenericSpecification<T>().getSpecification(criteria)));
          } else if (!criteria.groupCriteria().isEmpty()) {
            groupSpecificationAtomicReference.set(new GenericSpecification<T>().getSpecification(criteria.groupCriteria().get(0)));
            criteria.groupCriteria().forEach(groupCriteria -> groupSpecificationAtomicReference
                .set(groupCriteria.orPredicate() ?
                    Specification.where(groupSpecificationAtomicReference.get()).or(new GenericSpecification<T>().getSpecification(groupCriteria)) :
                    Specification.where(groupSpecificationAtomicReference.get()).and((new GenericSpecification<T>().getSpecification(groupCriteria)))));
            specificationAtomicReference.set(criteria.orPredicate() ?
                Specification.where(specificationAtomicReference.get()).or(groupSpecificationAtomicReference.get()) :
                Specification.where(specificationAtomicReference.get()).and(groupSpecificationAtomicReference.get()));
          }
        });
        return specificationAtomicReference.get();
      }
      return null;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}

