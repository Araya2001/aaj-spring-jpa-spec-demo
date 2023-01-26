package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;

import java.util.List;

public interface ProductService {
  List<Product> findAllBySpecs(List<SpecSearchCriteriaDTO> search);
}
