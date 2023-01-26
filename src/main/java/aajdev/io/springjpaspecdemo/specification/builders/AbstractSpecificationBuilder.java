package aajdev.io.springjpaspecdemo.specification.builders;

import aajdev.io.springjpaspecdemo.specification.AbstractSpecification;
import aajdev.io.springjpaspecdemo.util.SearchOperation;
import aajdev.io.springjpaspecdemo.util.SpecSearchCriteria;

import java.util.List;

public abstract class AbstractSpecificationBuilder {
  protected final void loadParams(List<SpecSearchCriteria> params, final String orPredicate, final String key,
                                  final String operation,
                                  final Object value,
                                  final String prefix,
                                  final String suffix) {
    SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
    if (op != null) {
      if (op == SearchOperation.EQUALITY) {
        final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
        final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
        if (startWithAsterisk && endWithAsterisk) {
          op = SearchOperation.CONTAINS;
        } else if (startWithAsterisk) {
          op = SearchOperation.ENDS_WITH;
        } else if (endWithAsterisk) {
          op = SearchOperation.STARTS_WITH;
        }
      }
      params.add(new SpecSearchCriteria(orPredicate, key, op, value));
    }
  }

  protected final <T extends AbstractSpecification<?>> void loadParams(List<SpecSearchCriteria> params, T spec) {
    params.add(spec.getCriteria());
  }

  protected final void loadParams(List<SpecSearchCriteria> params, SpecSearchCriteria criteria) {
    params.add(criteria);
  }
}
