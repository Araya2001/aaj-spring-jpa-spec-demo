# aaj-spring-jpa-spec-demo

Hecho por Alejandro Araya Jiménez

## Introducción
El servicio es relativamente simple, no hay complejidades de lógica de negocio, etc. Únicamente cumple con traer datos, teniendo esto en cuenta, la información del esquema se 
genera con un archivo puesto en `src/resources/main` llamado `data.sql`. Este archivo cuenta con el ingreso de datos, cada vez que se crea el esquema en la DB, para no tener 
que poner datos cada vez que se inicie el proyecto. Este proyecto corre con PostgreSQL, en un futuro puedo considerar implementar uno con MySQL.

El Proyecto también cuenta con varios detalles novedosos de Spring Framework 6, está en Java 17 y hace uso de expresiones de switch, records, etc... El principal motivo de este 
servicio 
es que se puedan realizar consultas dinámicas por medio de REST sin necesidad de tener que estar creando endpoints o métodos de repositorio cada vez que se quiere consultar 
sobre un campo de la entidad, con este servicio se demuestra que es posible realizarlo con un único endpoint y repositorio de una respectiva entidad, por ejemplo:

### Controlador
```java
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

  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
  public List<Product> postSearchProductsWithCriteriaSpec(HttpServletRequest httpServletRequest, @RequestBody List<SpecSearchCriteriaDTO> search) {
    log.warn("NEW REQUEST - HTTP GET request on resource mapping \"v1/product\" - IP: " + httpServletRequest.getHeader("X-FORWARDED-FOR"));
    return productService.findAllBySpecs(search);
  }
}
```
### Interfaz Servicio
```java
public interface ProductService {
  List<Product> findAllBySpecs(List<SpecSearchCriteriaDTO> search);
}
```
### Implementación de Interfaz
```java
@Service
@Log4j2
@SuppressWarnings("Duplicates")
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {
  private final ProductRepository productRepository;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public List<Product> findAllBySpecs(List<SpecSearchCriteriaDTO> search) {
    return productRepository.findAll(resolveSpec(search));
  }

  @Override
  protected Specification<Product> resolveSpec(List<SpecSearchCriteriaDTO> searchParameters) {
    ProductSpecificationBuilder builder = new ProductSpecificationBuilder(searchParameters);
    return builder.build();
  }
}
```
### Repositorio
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
  List<Product> findAllByWarehouse(Warehouse warehouse);
}
```

Con estos elementos y unos cuantos más que se mostraran más adelante, se puede obtener como resultado, un endpoint que permita las búsquedas dinámicas por cualquier campo de la 
entidad, contando uniones `INNER JOIN` y agrupaciones de expresiones booleanas. 