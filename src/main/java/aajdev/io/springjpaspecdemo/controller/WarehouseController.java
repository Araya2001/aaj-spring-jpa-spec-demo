package aajdev.io.springjpaspecdemo.controller;

import aajdev.io.springjpaspecdemo.domain.Warehouse;
import aajdev.io.springjpaspecdemo.dto.SpecSearchCriteriaDTO;
import aajdev.io.springjpaspecdemo.service.WarehouseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@ControllerAdvice
@RequestMapping("/v1/warehouse")
public class WarehouseController {
  private final WarehouseService warehouseService;

  @Autowired
  public WarehouseController(WarehouseService warehouseService) {
    this.warehouseService = warehouseService;
  }

  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<Warehouse> postSearchProductsWithCriteriaSpec(HttpServletRequest httpServletRequest, @RequestBody List<SpecSearchCriteriaDTO> search) {
    log.warn("NEW REQUEST - HTTP GET request on resource mapping \"v1/warehouse/search\" - IP: " + httpServletRequest.getHeader("X-FORWARDED-FOR"));
    return warehouseService.findAllBySpecs(search);
  }
}