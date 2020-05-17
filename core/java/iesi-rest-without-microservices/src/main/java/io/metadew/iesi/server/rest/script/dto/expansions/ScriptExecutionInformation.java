package io.metadew.iesi.server.rest.script.dto.expansions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ScriptExecutionInformation {

    private Long total;
    @JsonProperty("mostRecent")
    private ScriptExecutionDto scriptExecutionDto;

}