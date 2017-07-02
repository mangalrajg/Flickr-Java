package com.mangalraj.Local;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.flickr4java.flickr.photosets.Photoset;

public class CreatePictureList {
	public static String baseDir=null;
	public void printPhotoList(ArrayList<File> fileList) throws Exception {
		DateFormat outputDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		DateFormat inputDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		PrintWriter dumpText = new PrintWriter("PictureList.Local.txt", "UTF-8");
		PrintWriter dumpError = new PrintWriter("PictureList.Local.err", "UTF-8");

		Iterator<File> sets = fileList.iterator();
		while (sets.hasNext()) {
			File set = (File) sets.next();
			try {
				Metadata metadata = ImageMetadataReader.readMetadata(set);
				ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
				String date = directory.getDescription(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				Date d=inputDateFormat.parse(date);
				String line="albumid" + ":" + set.getParent().substring(baseDir.length()+1) + ":" + "photoid" + ":" + set.getName() + ":"
						+ outputDateFormat.format(d);
				dumpText.println(line);
				System.out.println(line);
			} catch (Exception ex) {
				dumpError.println(set.getParent().substring(baseDir.length())+":"+set.getName());
			}
		}
		dumpText.close();
		dumpError.close();

	}

	private String makeSafeFilename(String input) {
		byte[] fname = input.getBytes();
		byte[] bad = new byte[] { '\\', '/', '"' };
		byte replace = '_';
		for (int i = 0; i < fname.length; i++) {
			for (byte element : bad) {
				if (fname[i] == element) {
					fname[i] = replace;
				}
			}
		}
		return new String(fname);
	}

	public static void main(String[] args) throws Exception {
		baseDir = args[0];
		ArrayList<File> files = new ArrayList<File>();
		listf(baseDir, files);
		CreatePictureList bf = new CreatePictureList();
		bf.printPhotoList(files);
	}

	public static void listf(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath(), files);
			}
		}
	}

}
