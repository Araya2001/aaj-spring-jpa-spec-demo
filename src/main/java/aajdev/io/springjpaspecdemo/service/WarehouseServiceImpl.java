package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.repository.WarehousetRepository;
import aajdev.io.springjpaspecdemo.specification.builders.WarehouseSpecificationBuilder;
import aajdev.io.springjpaspecdemo.util.SearchOperation;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@SuppressWarnings("Duplicates")
public class WarehouseServiceImpl extends AbstractService<Warehouse> implements WarehouseService {
  private final WarehousetRepository warehousetRepository;

  @Autowired
  public WarehouseServiceImpl(WarehousetRepository warehousetRepository) {
    this.warehousetRepository = warehousetRepository;
  }

  @Override
  public List<Warehouse> findAllBySpec(String search) {
    return warehousetRepository.findAll(resolveSpec(search));
  }

  @Override
  protected Specification<Warehouse> resolveSpec(String searchParameters) {
    WarehouseSpecificationBuilder builder = new WarehouseSpecificationBuilder();
    String operationSetExper = Joiner.on("|")
        .join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile("(\\p{Punct}?)(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
    Matcher matcher = pattern.matcher(searchParameters + ",");
    while (matcher.find()) {
      builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(4), matcher.group(6));
    }
    return builder.build();
  }
}
