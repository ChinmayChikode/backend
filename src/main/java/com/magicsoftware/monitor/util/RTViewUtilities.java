package com.magicsoftware.monitor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RTViewUtilities {

	private static BufferedReader fileReader;	
	
	public static String readPropertyValueFromIFSINIFile(String filename, String groupname, String property) {

		String propertyValue = "";

		try {

			File file = new File(filename);

			if (file.exists()) {

				fileReader = new BufferedReader(new FileReader(filename));
				String inputIterator;

				while ((inputIterator = fileReader.readLine()) != null) {
					if (inputIterator.length() > 0) {
						String[] keyValuePair = inputIterator.split("=");
						if (keyValuePair.length == 2 && keyValuePair[0].trim().contains(("[" + groupname + "]" + property))) {
							propertyValue = keyValuePair[1].trim();
							break;
						}
					}
				}

				fileReader.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return propertyValue;
	}
}
