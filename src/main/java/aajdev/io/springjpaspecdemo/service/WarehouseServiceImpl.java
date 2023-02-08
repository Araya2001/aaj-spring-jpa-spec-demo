package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import aajdev.io.springjpaspecdemo.repository.WarehousetRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@SuppressWarnings("Duplicates")
public class WarehouseServiceImpl extends AbstractService<Warehouse> implements WarehouseService {
  private final WarehousetRepository warehousetRepository;

  @Autowired
  public WarehouseServiceImpl(WarehousetRepository warehousetRepository) {
    this.warehousetRepository = warehousetRepository;
  }

  @Override
  public List<Warehouse> findAllBySpecs(List<SpecSearchCriteriaDTO> search) {
    try {
      return warehousetRepository.findAll(resolveSpec(search));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }
}
