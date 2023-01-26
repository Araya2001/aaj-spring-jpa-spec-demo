package aajdev.io.springjpaspecdemo.repository;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
  List<Product> findAllByWarehouse(Warehouse warehouse);
}
