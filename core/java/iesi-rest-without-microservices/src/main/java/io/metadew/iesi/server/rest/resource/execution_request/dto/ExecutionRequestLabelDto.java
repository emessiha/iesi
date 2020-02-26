package io.metadew.iesi.server.rest.resource.execution_request.dto;

import io.metadew.iesi.metadata.definition.execution.ExecutionRequest;
import io.metadew.iesi.metadata.definition.execution.ExecutionRequestLabel;
import io.metadew.iesi.metadata.definition.execution.key.ExecutionRequestKey;
import io.metadew.iesi.metadata.definition.execution.key.ExecutionRequestLabelKey;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

@Data
public class ExecutionRequestLabelDto {

    private final String name;
    private final String value;

    public ExecutionRequestLabel convertToEntity(ExecutionRequestKey executionRequestKey) {
        return new ExecutionRequestLabel(new ExecutionRequestLabelKey(DigestUtils.sha256Hex(executionRequestKey.getId()+name)), executionRequestKey, name, value);
    }
}
