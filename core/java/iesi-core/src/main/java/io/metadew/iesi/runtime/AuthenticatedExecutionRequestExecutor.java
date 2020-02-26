package io.metadew.iesi.runtime;

import io.metadew.iesi.framework.configuration.FrameworkSettingConfiguration;
import io.metadew.iesi.framework.execution.FrameworkControl;
import io.metadew.iesi.framework.execution.IESIMessage;
import io.metadew.iesi.guard.configuration.UserAccessConfiguration;
import io.metadew.iesi.guard.definition.UserAccess;
import io.metadew.iesi.metadata.configuration.execution.ExecutionRequestConfiguration;
import io.metadew.iesi.metadata.definition.execution.AuthenticatedExecutionRequest;
import io.metadew.iesi.metadata.definition.execution.ExecutionRequestStatus;
import io.metadew.iesi.metadata.definition.execution.script.ScriptExecutionRequest;
import io.metadew.iesi.runtime.script.ScriptExecutorService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthenticatedExecutionRequestExecutor implements ExecutionRequestExecutor<AuthenticatedExecutionRequest> {

    private final UserAccessConfiguration userAccessConfiguration;
    private final Boolean authenticationEnabled;

    private static AuthenticatedExecutionRequestExecutor INSTANCE;

    public synchronized static AuthenticatedExecutionRequestExecutor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthenticatedExecutionRequestExecutor();
        }
        return INSTANCE;
    }

    private AuthenticatedExecutionRequestExecutor() {
        this.userAccessConfiguration = new UserAccessConfiguration();
        this.authenticationEnabled = FrameworkSettingConfiguration.getInstance().getSettingPath("guard.authenticate")
                .map(settingPath -> FrameworkControl.getInstance().getProperty(settingPath)
                        .orElseThrow(() -> new RuntimeException("no value set for guard.authenticate")).equalsIgnoreCase("y"))
                .orElse(false);
    }

    @Override
    public Class<AuthenticatedExecutionRequest> appliesTo() {
        return AuthenticatedExecutionRequest.class;
    }

    @Override
    public void execute(AuthenticatedExecutionRequest executionRequest) {
        if (authenticationEnabled) {
            checkUserAccess(executionRequest);
        } else {
            log.info("authentication.disabled:access automatically granted");
        }
        executionRequest.setExecutionRequestStatus(ExecutionRequestStatus.ACCEPTED);
        ExecutionRequestConfiguration.getInstance().update(executionRequest);

        for (ScriptExecutionRequest scriptExecutionRequest : executionRequest.getScriptExecutionRequests()) {
            ScriptExecutorService.getInstance().execute(scriptExecutionRequest);
        }

    }

    private void checkUserAccess(AuthenticatedExecutionRequest executionRequest) {
        UserAccess userAccess = userAccessConfiguration.doUserLogin(executionRequest.getUser(), executionRequest.getPassword());

        if (userAccess.isException()) {
            log.info(new IESIMessage("guard.user.exception=" + userAccess.getExceptionMessage()));
            log.info(new IESIMessage("guard.user.denied"));
            executionRequest.setExecutionRequestStatus(ExecutionRequestStatus.DECLINED);
            ExecutionRequestConfiguration.getInstance().update(executionRequest);
            throw new RuntimeException("guard.user.denied");
        }
    }
}
