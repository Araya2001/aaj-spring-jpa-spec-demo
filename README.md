# aaj-spring-jpa-spec-demo

Hecho por Alejandro Araya Jiménez

## Introducción

El servicio es relativamente simple, no hay complejidades de lógica de negocio, etc.
Únicamente cumple con traer datos, teniendo esto en cuenta, la información del esquema se
genera con un archivo puesto en `src/main/resources` llamado `data.sql`.
Este archivo cuenta con el ingreso de datos, cada vez que se crea el esquema en la DB, para no tener
que poner datos cada vez que se inicie el proyecto.
Este proyecto corre con Postgres, en un futuro puedo considerar implementar uno con MySQL.

El Proyecto también cuenta con varios detalles novedosos de Spring Framework 6, está en Java 17 y hace uso de expresiones de switch, records, etc...
El principal motivo de este
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

### Configuración

Es importante notar que existe un archivo llamado `application.yml` en `src/main/resources`.
Este viene siendo el reemplazo de application.properties como usualmente se ve en
aplicaciones y/o servicios de Spring.
El tener el archivo como `YAML` permite brindar una estructura mucho más organizada que las propiedades comunes y corrientes.

El sistema también cuenta con una conexión a una DB de Postgres, una recomendación es que instalen docker y corran el motor de base de datos en contenedor.
Existe un proyecto actualmente que cuenta con la creación de este contenedor, pueden revisarlo por si les funciona para no instalar directamente en la máquina la DB.

* https://github.com/Araya2001/aaj-spring-security-db-demo-env

## Especificación

Se puede hacer uso de una interfaz que se llama Especificación para la construcción de `Criterias` con los cuales se puede realizar consultas a la base de datos sin necesidad
de tener que definir métodos en los repositorios o consultas manuales con `EntintyManager`.
Tomando como caso la entidad `Product` que ya ha sido referenciada en los ejemplos
mostrados anteriormente, se requieren múltiples elementos para hacer que esto funcione, los cuales serían los siguientes:

### Especificación Abstracta

```java

public abstract class AbstractSpecification<T extends BaseEntity> implements Specification<T> {
  private SpecSearchCriteriaDTO criteria;

  public AbstractSpecification(SpecSearchCriteriaDTO criteria) {
    this.criteria = criteria;
  }

  // AAJ - ESTE ME GUSTA, USA RECORD
  static Predicate getPredicate(Root<?> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteriaDTO criteria) {
    return switch (criteria.operation()) {
      case EQUALITY -> criteriaBuilder.equal(root.get(criteria.key()), criteria.value());
      case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.key()), criteria.value());
      case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.key()), criteria.value().toString());
      case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.key()), criteria.value().toString());
      case LIKE -> criteriaBuilder.like(root.get(criteria.key()), criteria.value().toString());
      case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.key()), criteria.value() + "%");
      case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value());
      case CONTAINS -> criteriaBuilder.like(root.get(criteria.key()), "%" + criteria.value() + "%");
      case JOIN_EQUALITY -> criteriaBuilder.equal(root.join(criteria.joinKey(), JoinType.INNER).get(criteria.key()), criteria.value());
      case GROUP_CRITERIA -> null;
    };
  }

  public SpecSearchCriteriaDTO getCriteria() {
    return criteria;
  }

  public void setCriteria(SpecSearchCriteriaDTO criteria) {
    this.criteria = criteria;
  }
}
```

### Especificación concreta

Con la especificación abstracta y genérica nos aseguramos de que al realizar la construcción del Criteria, no tengamos que reescribir todo una y otra vez, implementando la
interfaz, de forma genérica, de `Specification<?>`.
Una vez se aplica herencia sobre una clase concreta, este va a poder hacer uso de los métodos de construcción de `Criteria` y quedará con la minima cantidad de código.

```java

public class ProductSpecification extends AbstractSpecification<Product> {
  public ProductSpecification(SpecSearchCriteriaDTO criteria) {
    super(criteria);
  }

  @Override
  public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return getPredicate(root, criteriaBuilder, super.getCriteria());
  }
}
```

> El método `toPredicate` se implementa gracias a la interfaz `Specification<?>`

### Constructor de la Especificación

Para Generar especificaciones que contengan los `Criteria` es necesario generar una capa de abstracción adicional para poder acceder a estos recursos de forma simple y reducida.

```java

@Log4j2
public class ProductSpecificationBuilder {
  private final List<SpecSearchCriteriaDTO> params;

  public ProductSpecificationBuilder(List<SpecSearchCriteriaDTO> params) {
    this.params = params;
  }

  public Specification<Product> build() {
    AtomicReference<Specification<Product>> specificationAtomicReference = new AtomicReference<>(new ProductSpecification(params.get(0)));
    AtomicReference<Specification<Product>> groupSpecificationAtomicReference = new AtomicReference<>();
    if (!params.isEmpty()) {
      params.forEach(criteria -> {
        log.info("PRODUCT - CRITERIA: " + criteria);
        if (criteria.operation() != SearchOperation.GROUP_CRITERIA) {
          specificationAtomicReference
              .set(criteria.orPredicate() ?
                  Specification.where(specificationAtomicReference.get()).or(new ProductSpecification(criteria)) :
                  Specification.where(specificationAtomicReference.get()).and(new ProductSpecification(criteria)));
        } else if (!criteria.groupCriteria().isEmpty()) {
          groupSpecificationAtomicReference.set(new ProductSpecification(criteria.groupCriteria().get(0)));
          criteria.groupCriteria().forEach(groupCriteria -> groupSpecificationAtomicReference
              .set(groupCriteria.orPredicate() ?
                  Specification.where(groupSpecificationAtomicReference.get()).or(new ProductSpecification(groupCriteria)) :
                  Specification.where(groupSpecificationAtomicReference.get()).and(new ProductSpecification(groupCriteria))));
          specificationAtomicReference.set(criteria.orPredicate() ?
              Specification.where(specificationAtomicReference.get()).or(groupSpecificationAtomicReference.get()) :
              Specification.where(specificationAtomicReference.get()).and(groupSpecificationAtomicReference.get()));
        }
      });
      return specificationAtomicReference.get();
    }
    return null;
  }
}
```

> Es visible qué existe una cantidad de código más grande que contiene lógica de ciertos operadores.
> Esta clase es responsable de generar las especificaciones con una lista de parámetros, esta lista es de tipo `SpecSearchCriteriaDTO`

### DTO Serializable

El DTO serializable que se encuentra referenciado en la sección anterior, tiene varios elementos para ser de asistencia en varios de los procesos de la construcción de
`Criteria`, se hace uso de esto para conocer los parámetros que tiene la búsqueda para formular la consulta

```java

@Builder
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpecSearchCriteriaDTO(
    @JsonProperty("key")
    String key,
    @JsonProperty("join_key")
    String joinKey,
    @JsonProperty("search_operation")
    SearchOperation operation,
    @JsonProperty("value")
    Object value,
    @JsonProperty("is_or_predicate")
    Boolean orPredicate,
    @JsonProperty("group_criteria")
    List<SpecSearchCriteriaDTO> groupCriteria) {
}
```

> Este record cuenta con un atributo de sí mismo para el agrupamiento de expresiones booleanas

Como se puede ver en el record anterior, existen elementos cuya propiedad Serializable con `Jackson`, para transformarlo a un `JSON` y/o ser recibido por medio de un `JSON`,
estos atributos tienen el siguiente significado:

* `key`: La columna por la que se desea buscar.
* `join_key`: Si existe una unión, es necesario especificar el nombre de la entidad por la cual se estará buscando. Únicamente es válida si la operación es `JOIN_EQUALITY`.
* `search_operation`: Es la operación a realizar con la columna y el valor por el cual se deseará encontrar un resultado, Se serializa como un `String`.
* `value`: El valor que se desea encontrar respectivo a su operación y su llave.
* `is_or_predicate`: Bandera para conocer si se debe agregar un OR antes de la expresión en caso de que exista más de una expresión.
* `group_criteria`: Lista de objetos que hace referencia a sí mismo, preferiblemente para anidar condiciones o agruparlas para formar una única expresión booleana, únicamente
  puede ser usado sí la operación es `GROUP_CRITERIA`.

### Enumeración `SearchOperation`

Este se puede serializar y ya es utilizado en múltiples puntos del código, pensé que ponerlo acá fue la mejor idea porque primero leen toda la estructura y luego van conectando
una pieza con la otra, finalizando con este elemento para que se pueda memorizar fácilmente la función que cumple la enumeración en todo aspecto de la especificación.

```java

@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum SearchOperation {
  @JsonProperty("EQUALITY")
  EQUALITY,
  @JsonProperty("NEGATION")
  NEGATION,
  @JsonProperty("GREATER_THAN")
  GREATER_THAN,
  @JsonProperty("LESS_THAN")
  LESS_THAN,
  @JsonProperty("LIKE")
  LIKE,
  @JsonProperty("STARTS_WITH")
  STARTS_WITH,
  @JsonProperty("ENDS_WITH")
  ENDS_WITH,
  @JsonProperty("CONTAINS")
  CONTAINS,
  @JsonProperty("JOIN_EQUALITY")
  JOIN_EQUALITY,
  @JsonProperty("GROUP_CRITERIA")
  GROUP_CRITERIA
}
```

> Estos son los valores que se usan en `operation` dentro del `JSON` de solicitud hacia el endpoint para la consulta de datos

### Solicitud

Un ejemplo del `JSON` que se debe enviar al endpoint es el siguiente:

```json
[
  {
    "key": "id",
    "join_key": "warehouse",
    "search_operation": "JOIN_EQUALITY",
    "value": 3,
    "is_or_predicate": false
  },
  {
    "search_operation": "GROUP_CRITERIA",
    "group_criteria": [
      {
        "key": "id",
        "search_operation": "EQUALITY",
        "value": 1,
        "is_or_predicate": false
      },
      {
        "key": "id",
        "search_operation": "EQUALITY",
        "value": 2,
        "is_or_predicate": true
      }
    ],
    "is_or_predicate": true
  }
]
```

> No todos los campos del objeto deben de ser puestos, si no existen en el objeto, no serializa y se considera como `null`

## Conclusión

Es una forma bastante distintiva de formular Consultas al motor de base de datos por medio de una API, esto facilita el trabajo cuando se tiene una arquitectura distribuida,
debido a que no es requerido estar modificando el servicio que encapsule las entidades de las Bases de datos, gracias a que la delegación de las consultas se le entrega al
usuario de la API y no al desarrollador de la API.
El desarrollador no se encuentra librado de tener que realizar modificaciones en la base de código, pero, minimiza la cantidad de veces que esta se debe de modificar solo para
acceder a datos, especialmente cuando existen columnas nuevas o la necesidad de buscar por una columna o unión en la cual antes no era de interés 
