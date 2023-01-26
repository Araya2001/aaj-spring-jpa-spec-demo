package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface WarehouseService {
  List<Warehouse> findAllBySpec(String search);
}
