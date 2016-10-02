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
import java.util.Hashtable;
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
			//ArrayList<String> list1 = diff.FileToArray(configFile1);
			//ArrayList<String> list2 = diff.FileToArray(configFile2);
			Hashtable<String, String> hashList1 = diff.ToHashTable(configFile1);
			Hashtable<String, String> hashList2 = diff.ToHashTable(configFile2);
			
			Hashtable<String, String> sourceList = new Hashtable<String, String>(hashList1);
			Hashtable<String, String> destinationList = new Hashtable<String, String>(hashList2);

			for(String key: hashList2.keySet())
			{
				sourceList.remove(key);	
			}
			System.out.println(sourceList.size() + " Rows In " + configFile1 + " and not in " + configFile2 + " Saved into File1-File2");
			
			for (String temp : sourceList.values()) {
				configFile1MconfigFile2.println(temp);
			}

			configFile1MconfigFile2.close();

			
			for(String key: hashList1.keySet())
			{
				destinationList.remove(key);	
			}
			System.out.println(destinationList.size() + " Rows In " + configFile2 + " and not in " + configFile1 + " Saved into File2-File1");
			
			for (String temp : destinationList.values()) {
				configFile2MconfigFile1.println(temp);
			}
			configFile2MconfigFile1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Hashtable<String, String> ToHashTable(String configFile) throws FileNotFoundException, IOException {
		File f = new File(configFile);
		Hashtable<String, String> retArray = new Hashtable<String, String>();
		if (!f.exists() || f.isDirectory()) {
			System.out.println("Cannot open:" + configFile);
			return null;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] command = line.split(":",-1);
				if (command.length != 5) {
					System.out.println("Line should contain 5 fields: " + line);
					continue;
				}
				String setId = command[0];
				String albumName = command[1];
				String photoId = command[2];
				String photoName = command[3];
				photoName=photoName.replaceAll(".jpg", ".JPG");
				String dateTaken = command[4];
				if(dateTaken.length() == 0) // Default date
					dateTaken = "20000101000000";
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				try{
				Date dateTakenInDate = df.parse(dateTaken);
				}
				catch (ParseException p)
				{
//					2013-07-15T165905-0700
					DateFormat sonydf = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss");
					Date dateTakenInDate = null;
					try{
							dateTakenInDate =sonydf.parse(dateTaken);
							dateTaken = df.format(dateTakenInDate);
					}
					catch(ParseException pp2)
					{
						System.out.println("Unable to parse date: " + line);
						dateTaken = "20000101000000";
					}
				
				}
				retArray.put(photoName + ":" + dateTaken, line);
			}
		}
		return retArray;
	}

}
