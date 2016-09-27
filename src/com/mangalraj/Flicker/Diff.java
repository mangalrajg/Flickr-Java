package com.mangalraj.Flicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Diff {
	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Usage: command file1 file2");
			return;
		}

		String configFile1 = args[0];
		String configFile2 = args[1];
		Diff diff;
		try {
			diff = new Diff();
			PrintWriter configFile1MconfigFile2 = new PrintWriter("File1-File2", "UTF-8");
			PrintWriter configFile2MconfigFile1 = new PrintWriter("File2-File1", "UTF-8");
			ArrayList<String> list1 = diff.FileToArray(configFile1);
			ArrayList<String> list2 = diff.FileToArray(configFile2);

			List<String> sourceList = new ArrayList<String>(list1);
			List<String> destinationList = new ArrayList<String>(list2);

			sourceList.removeAll(list2);
			System.out.println(sourceList.size() + " Rows In " + configFile1 + " and not in " + configFile2 + " Saved into File1-File2");
			for (String temp : sourceList) {
				configFile1MconfigFile2.println(temp);
			}
			configFile1MconfigFile2.close();

			
			destinationList.removeAll(list1);
			System.out.println(destinationList.size() + " Rows In " + configFile2 + " and not in " + configFile1 + " Saved into File2-File1");
			for (String temp : destinationList) {
				configFile2MconfigFile1.println(temp);
			}
			configFile2MconfigFile1.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<String> FileToArray(String configFile) throws FileNotFoundException, IOException, ParseException {
		File f = new File(configFile);
		ArrayList<String> retArray = new ArrayList<String>();
		if (!f.exists() || f.isDirectory()) {
			System.out.println("Cannot open:" + configFile);
			return null;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] command = line.split(":");
				if (command.length != 5) {
					System.out.println("Line should contain 5 fields: " + line);
					continue;
				}
				String setId = command[0];
				String albumName = command[1];
				String photoId = command[2];
				String photoName = command[3];
				String dateTaken = command[4];
				retArray.add(photoName + ":" + dateTaken);
			}
		}
		return retArray;
	}
}
