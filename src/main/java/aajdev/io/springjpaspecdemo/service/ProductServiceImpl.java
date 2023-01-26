package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.repository.ProductRepository;
import aajdev.io.springjpaspecdemo.specification.builder.ProductSpecificationBuilder;
import aajdev.io.springjpaspecdemo.specification.util.SearchOperation;
import com.google.common.base.Joiner;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  public List<Product> findAllBySpec(String search) {
    return productRepository.findAll(resolveSpec(search));
  }

  @Override
  protected Specification<Product> resolveSpec(String searchParameters) {
    ProductSpecificationBuilder builder = new ProductSpecificationBuilder();
    String operationSetExper = Joiner.on("|")
        .join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile("(\\p{Punct}?)(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
    Matcher matcher = pattern.matcher(searchParameters + ",");
    while (matcher.find()) {
      builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(4), matcher.group(6));
    }
    log.warn(builder);
    return builder.build();
  }
}
