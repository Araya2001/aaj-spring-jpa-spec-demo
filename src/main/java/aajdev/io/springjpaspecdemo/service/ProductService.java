package aajdev.io.springjpaspecdemo.service;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.domain.Warehouse;

import java.util.List;

public interface ProductService {
  List<Product> findAllBySpec(String search);
}
