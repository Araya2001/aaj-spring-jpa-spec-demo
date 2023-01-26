package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import aajdev.io.springjpaspecdemo.repository.ProductRepository;
import aajdev.io.springjpaspecdemo.specification.builder.ProductSpecificationBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@SuppressWarnings("Duplicates")
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {
  private final ProductRepository productRepository;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> findAllBySpecs(List<SpecSearchCriteriaDTO> search) {
    return productRepository.findAll(resolveSpec(search));
  }

  @Override
  protected Specification<Product> resolveSpec(List<SpecSearchCriteriaDTO> searchParameters) {
    ProductSpecificationBuilder builder = new ProductSpecificationBuilder(searchParameters);
    return builder.build();
  }
}
