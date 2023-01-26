package aajdev.io.springjpaspecdemo.domain;


import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "Product", indexes = {
    @Index(name = "idx_product_id_name", columnList = "id, name")
})
public class Product extends BaseEntity {
  protected Integer id;
  private String name;
  private String description;
  private Warehouse warehouse;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Product product = (Product) o;
    return getId() != null && Objects.equals(getId(), product.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "dateCreated = " + dateCreated + ", " +
        "dateModified = " + dateModified + ", " +
        "id = " + id + ", " +
        "name = " + name + ", " +
        "description = " + description + ", " +
        "warehouse = " + warehouse + ")";
  }
}
