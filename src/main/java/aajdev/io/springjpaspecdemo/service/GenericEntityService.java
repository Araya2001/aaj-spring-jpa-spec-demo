package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;

import java.util.List;

public interface GenericEntityService<T> {
  List<T> findAllBySpecs(List<SpecSearchCriteriaDTO> search);
}
