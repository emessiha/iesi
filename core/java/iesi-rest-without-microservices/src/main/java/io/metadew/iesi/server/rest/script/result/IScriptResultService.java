package io.metadew.iesi.server.rest.script.result;

import io.metadew.iesi.metadata.definition.script.result.ScriptResult;

import java.util.List;
import java.util.Optional;

public interface IScriptResultService {

    List<ScriptResult> getAll();

    Optional<List<ScriptResult>> getByRunId(String runId);

    Optional<ScriptResult> getByRunIdAndProcessId(String runId, Long processId);

}
