package io.metadew.iesi.framework.execution;

import io.metadew.iesi.common.properties.PropertiesTools;
import io.metadew.iesi.connection.tools.FileTools;
import io.metadew.iesi.connection.tools.FolderTools;
import io.metadew.iesi.framework.configuration.framework.FrameworkConfiguration;
import io.metadew.iesi.framework.definition.FrameworkRunIdentifier;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.ThreadContext;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

public class FrameworkRuntime {

	private String runCacheFolderName;
	private String localHostChallenge;
	private String localHostChallengeFileName;
	private String runSpoolFolderName;
	private String processIdFileName;
	private String frameworkRunId;

	private static FrameworkRuntime INSTANCE;

	public synchronized static FrameworkRuntime getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FrameworkRuntime();
		}
		return INSTANCE;
	}

	private FrameworkRuntime() {}

	public void init() {
		init(new FrameworkRunIdentifier());
	}

	public void init(FrameworkRunIdentifier frameworkRunIdentifier) {
		init(frameworkRunIdentifier.getId());
	}

	public void init(String runId) {
		this.frameworkRunId = runId;
		ThreadContext.put("fwk.runid", runId);
		this.runCacheFolderName = FrameworkConfiguration.getInstance().getMandatoryFrameworkFolder("run.cache").getAbsolutePath()
				+ File.separator + this.frameworkRunId;
		FolderTools.createFolder(runCacheFolderName);

		this.runSpoolFolderName = this.runCacheFolderName + File.separator + "spool";
		FolderTools.createFolder(runSpoolFolderName);

		this.localHostChallenge = UUID.randomUUID().toString();
		this.localHostChallengeFileName = FilenameUtils.normalize(runCacheFolderName + File.separator + this.localHostChallenge  + ".fwk");
		FileTools.appendToFile(localHostChallengeFileName, "", "localhost.challenge=" + this.localHostChallenge);

		// Initialize process id
		this.processIdFileName = FilenameUtils.normalize(runCacheFolderName + File.separator  + "processId.fwk");
		Properties processIdProperties = new Properties();
		processIdProperties.put("processId", "-1");
		PropertiesTools.setProperties(processIdFileName, processIdProperties);
	}


//	public FrameworkRuntime(FrameworkConfiguration frameworkConfiguration, FrameworkRunIdentifier frameworkRunIdentifier) {
//		this.setFrameworkConfiguration(frameworkConfiguration);
//
//		// Create run id
//		if (frameworkRunIdentifier == null) {
//			this.setFrameworkRunId(UUID.randomUUID().toString());
//		} else {
//			this.setFrameworkRunId(frameworkRunIdentifier.getScriptId());
//		}
//
//		// Create run cache folder
//		this.setRunCacheFolderName(
//				this.getFrameworkConfiguration().getFolderConfiguration().getFolderAbsolutePath("run.cache")
//						+ File.separator + this.getFrameworkRunId());
//		FolderTools.createFolder(this.getRunCacheFolderName());
//
//		// Create spool folder
//		this.setRunSpoolFolderName(
//				this.getRunCacheFolderName() + File.separator + "spool");
//		FolderTools.createFolder(this.getRunSpoolFolderName());
//
//		// Create localhost challenge
//		this.setLocalHostChallenge(UUID.randomUUID().toString());
//		this.setLocalHostChallengeFileName(FilenameUtils.normalize(this.getRunCacheFolderName() + File.separator + this.getLocalHostChallenge()  + ".fwk"));
//		FileTools.appendToFile(this.getLocalHostChallengeFileName(), "", "localhost.challenge=" + this.getLocalHostChallenge());
//
//		// Initialize process id
//		this.setProcessIdFileName(FilenameUtils.normalize(this.getRunCacheFolderName() + File.separator  + "processId.fwk"));
//		Properties processIdProperties = new Properties();
//		processIdProperties.put("processId", "-1");
//		PropertiesTools.setProperties(this.getProcessIdFileName(), processIdProperties);
//	}


	public void terminate() {
		//FolderTools.deleteFolder(this.getRunCacheFolderName(), true);
	}

	public String getFrameworkRunId() {
		return frameworkRunId;
	}

	public String getLocalHostChallengeFileName() {
		return localHostChallengeFileName;
	}

}

