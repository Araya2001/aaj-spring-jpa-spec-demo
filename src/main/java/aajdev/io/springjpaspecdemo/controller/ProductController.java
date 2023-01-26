package aajdev.io.springjpaspecdemo.controller;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@ControllerAdvice
@RequestMapping("/v1/product")
public class ProductController {
  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<Product> getProduct(HttpServletRequest httpServletRequest, @RequestParam(name = "search") String search) {
    log.warn("NEW REQUEST - HTTP GET request on resource mapping \"v1/product\" - IP: " + httpServletRequest.getHeader("X-FORWARDED-FOR"));
    return productService.findAllBySpec(search);
  }
}
