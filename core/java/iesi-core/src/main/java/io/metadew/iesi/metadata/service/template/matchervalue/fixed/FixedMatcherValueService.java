package io.metadew.iesi.metadata.service.template.matchervalue.fixed;

import io.metadew.iesi.datatypes.DataType;
import io.metadew.iesi.datatypes.DataTypeHandler;
import io.metadew.iesi.metadata.definition.template.matcher.value.MatcherFixedValue;
import io.metadew.iesi.metadata.service.template.matchervalue.IMatcherValueService;
import io.metadew.iesi.script.execution.ExecutionRuntime;

public class FixedMatcherValueService implements IMatcherValueService<MatcherFixedValue> {

    private static FixedMatcherValueService INSTANCE;

    public synchronized static FixedMatcherValueService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FixedMatcherValueService();
        }
        return INSTANCE;
    }

    @Override
    public boolean matches(MatcherFixedValue matcherValue, DataType dataType, ExecutionRuntime executionRuntime) {
        return DataTypeHandler.getInstance().equals(dataType,
                DataTypeHandler.getInstance().resolve(matcherValue.getValue(), executionRuntime),
                executionRuntime);
    }

    @Override
    public Class<MatcherFixedValue> appliesToClass() {
        return MatcherFixedValue.class;
    }
}
