package io.metadew.iesi.test.launch;

import java.util.List;

public final class Launcher {

	
	public static void execute(String launcher, List<LaunchArgument> inputArgs) {
		
		int inputArgsArraySize = 0;
		for (LaunchArgument launchArgument : inputArgs) {
			if (launchArgument.isKeyvalue()) {
				inputArgsArraySize = inputArgsArraySize + 2;
			} else {
				inputArgsArraySize++;
			}
		}
		
		String inputArgsArray[] = new String[inputArgsArraySize];
		int k = 0;
		int i = 0;
		while (i < inputArgsArraySize) {
			LaunchArgument launchArgument = inputArgs.get(k);
			if (launchArgument.isKeyvalue()) {
				inputArgsArray[i] = launchArgument.getKey();
				inputArgsArray[i+1] = launchArgument.getValue();
				i = i + 2;
			} else {
				inputArgsArray[i] = launchArgument.getKey();
				i++;
			}
			k++;
		}

		switch (launcher) {
		case "metadata":
			io.metadew.iesi.launch.MetadataLauncher.main(inputArgsArray);
			break;
		case "script":
			io.metadew.iesi.launch.ScriptLauncher.main(inputArgsArray);
			break;
		default:
			break;
		}

	}
	
}