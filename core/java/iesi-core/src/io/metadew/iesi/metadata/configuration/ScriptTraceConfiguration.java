package io.metadew.iesi.metadata.configuration;

import io.metadew.iesi.connection.tools.SQLTools;
import io.metadew.iesi.framework.execution.FrameworkExecution;
import io.metadew.iesi.metadata.definition.Action;
import io.metadew.iesi.metadata.definition.Script;
import io.metadew.iesi.metadata.definition.ScriptParameter;
import io.metadew.iesi.metadata.tools.IdentifierTools;
import io.metadew.iesi.script.execution.ScriptExecution;

public class ScriptTraceConfiguration {

	private Script script;
	private FrameworkExecution frameworkExecution;

	// Constructors
	public ScriptTraceConfiguration(FrameworkExecution frameworkExecution) {
		this.setFrameworkExecution(frameworkExecution);
	}

	public ScriptTraceConfiguration(Script script, FrameworkExecution frameworkExecution) {
		this.setScript(script);
		this.setFrameworkExecution(frameworkExecution);
	}

	// Create insert statement
	public String getInsertStatement(ScriptExecution scriptExecution) {
		String runId = scriptExecution.getExecutionControl().getRunId();
		long processId = scriptExecution.getExecutionControl().getProcessId();
		long parentProcessId = scriptExecution.getParentScriptExecution().getProcessId();
		String scriptId =IdentifierTools.getScriptIdentifier(this.getScript().getName());
		
		String sql = "";
		
		sql += "INSERT INTO " + this.getFrameworkExecution().getMetadataControl().getTraceMetadataRepository()
				.getTableNameByLabel("ScriptDesignTraces");
		sql += " (RUN_ID, PRC_ID, PARENT_PRC_ID, SCRIPT_ID, SCRIPT_TYP_NM, SCRIPT_NM, SCRIPT_DSC) ";
		sql += "VALUES ";
		sql += "(";
		sql += SQLTools.GetStringForSQL(runId);
		sql += ",";
		sql += SQLTools.GetStringForSQL(processId);
		sql += ",";
		sql += SQLTools.GetStringForSQL(parentProcessId);
		sql += ",";
		sql += SQLTools.GetStringForSQL(scriptId);
		sql += ",";
		sql += SQLTools.GetStringForSQL(this.getScript().getType());
		sql += ",";
		sql += SQLTools.GetStringForSQL(this.getScript().getName());
		sql += ",";
		sql += SQLTools.GetStringForSQL(this.getScript().getDescription());
		sql += ")";
		sql += ";";

		// add ScriptVersion
		String sqlVersion = this.getVersionInsertStatements(runId, processId, scriptId);
		if (!sqlVersion.equalsIgnoreCase("")) {
			sql += "\n";
			sql += sqlVersion;
		}

		// add Actions
		String sqlActions = this.getActionInsertStatements(runId, processId, scriptId);
		if (!sqlActions.equalsIgnoreCase("")) {
			sql += "\n";
			sql += sqlActions;
		}

		// add Parameters
		String sqlParameters = this.getParameterInsertStatements(runId, processId, scriptId);
		if (!sqlParameters.equalsIgnoreCase("")) {
			sql += "\n";
			sql += sqlParameters;
		}

		return sql;
	}

	private String getVersionInsertStatements(String runId, long processId, String scriptId) {
		String result = "";

		if (this.getScript().getVersion() == null)
			return result;

		ScriptVersionTraceConfiguration scriptVersionTraceConfiguration = new ScriptVersionTraceConfiguration(
				this.getScript().getVersion(), this.getFrameworkExecution());
		result += scriptVersionTraceConfiguration.getInsertStatement(runId, processId, scriptId);

		return result;
	}

	private String getActionInsertStatements(String runId, long processId, String scriptId) {
		String result = "";
		int counter = 0;

		if (this.getScript().getActions() == null)
			return result;

		for (Action action : this.getScript().getActions()) {
			counter++;
			ActionTraceConfiguration actionTraceConfiguration = new ActionTraceConfiguration(action, this.getFrameworkExecution());
			if (!result.equalsIgnoreCase(""))
				result += "\n";
			result += actionTraceConfiguration.getInsertStatement(runId, processId, scriptId,
					this.getScript().getVersion().getNumber(), counter);
		}

		return result;
	}

	private String getParameterInsertStatements(String runId, long processId, String scriptId) {
		String result = "";

		if (this.getScript().getParameters() == null)
			return result;

		for (ScriptParameter scriptParameter : this.getScript().getParameters()) {
			ScriptParameterTraceConfiguration scriptParameterTraceConfiguration = new ScriptParameterTraceConfiguration(
					this.getScript().getVersion(), scriptParameter, this.getFrameworkExecution());
			if (!result.equalsIgnoreCase(""))
				result += "\n";
			result += scriptParameterTraceConfiguration.getInsertStatement(runId, processId, scriptId);
		}

		return result;
	}

	// Getters and Setters
	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public FrameworkExecution getFrameworkExecution() {
		return frameworkExecution;
	}

	public void setFrameworkExecution(FrameworkExecution frameworkExecution) {
		this.frameworkExecution = frameworkExecution;
	}

}