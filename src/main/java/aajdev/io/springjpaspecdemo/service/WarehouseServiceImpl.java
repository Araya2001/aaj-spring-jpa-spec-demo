package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import aajdev.io.springjpaspecdemo.repository.WarehousetRepository;
import aajdev.io.springjpaspecdemo.specification.builder.WarehouseSpecificationBuilder;
import aajdev.io.springjpaspecdemo.dto.SearchOperation;
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
  public List<Warehouse> findAllBySpecs(List<SpecSearchCriteriaDTO> search) {
    return warehousetRepository.findAll(resolveSpec(search));
  }

  @Override
  protected Specification<Warehouse> resolveSpec(List<SpecSearchCriteriaDTO> searchParameters) {
    WarehouseSpecificationBuilder builder = new WarehouseSpecificationBuilder(searchParameters);
    return builder.build();
  }
}
