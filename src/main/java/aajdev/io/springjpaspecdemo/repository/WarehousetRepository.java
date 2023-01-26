package aajdev.io.springjpaspecdemo.repository;

import aajdev.io.springjpaspecdemo.domain.Product;
import aajdev.io.springjpaspecdemo.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehousetRepository extends JpaRepository<Warehouse, Integer>, JpaSpecificationExecutor<Warehouse> {
}
