package com.mangalraj.Flicker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

public class ModifyPhotos {
	private final Flickr flickr;
	private String nsid;
	Photosets photoSets;
	PhotosInterface photoInt;
	PhotosetsInterface photosetInt;

	public ModifyPhotos() throws FlickrException, IOException, SAXException {
		AuthManager am = new AuthManager();
		flickr = am.getFlickr();
		nsid = am.getNsId();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String configFile = args[0];
		ModifyPhotos mp;
		try {
			mp = new ModifyPhotos();
			mp.Start(configFile);
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void Start(String configFile) throws FileNotFoundException,
			IOException, FlickrException, ParseException {
		File f = new File(configFile);
		if (!f.exists() || f.isDirectory()) {
			System.out.println("Cannot open:" + configFile);
			return;
		}
		photoInt = flickr.getPhotosInterface();
		photosetInt = flickr.getPhotosetsInterface();
		photoSets = photosetInt.getList(this.nsid);
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println("Processing:" + line);
				String[] command = line.split(":");
				if (command.length != 5) {
					System.out.println("Line should contain 5 fields: " + line);
					return;
				}
				String setId = command[0];
				String albumName = command[1];
				String photoId = command[2];
				String photoName = command[3];
				String dateTaken = command[4];
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				Date dateTakenInDate = df.parse(dateTaken);
				
				Photo p = photoInt.getPhoto(photoId);
				Photoset ps = photosetInt.getInfo(setId);
				
				UpdateAlbum(albumName, ps, p);				
				UpdatePhoto(photoName,dateTakenInDate, p);
				
			}
		}
	}

	private void UpdatePhoto(String photoName, Date dateTaken, Photo p) throws FlickrException {

		if (!p.getTitle().equals(photoName))
			photoInt.setMeta(p.getId(), photoName, p.getDescription());
		if(!p.getDateTaken().equals(dateTaken))
			photoInt.setDates(p.getId(), null, dateTaken, null);
	}

	private void UpdateAlbum(String newAlbumName, Photoset ps, Photo p)
			throws FlickrException {
		if (ps == null) {
			// Ideally i should throw an error
			ps = photosetInt.create(newAlbumName, "", p.getId());
			photoSets = photosetInt.getList(this.nsid);
		} else if (!ps.getTitle().equals(newAlbumName)) {
			// Looks like the photo has to be moved
			photosetInt.removePhoto(ps.getId(), p.getId());
			ps = null;
			Iterator<Photoset> sets = photoSets.getPhotosets().iterator();
			// Find the destination Album
			while (sets.hasNext()) {
				Photoset set = (Photoset) sets.next();
				if (set.getTitle().equals(newAlbumName)) {
					// Yepee we found it
					ps = set;
					break;
				}
			}
			if (ps == null) {
				// Create new album with requested name
				ps = photosetInt.create(newAlbumName, "", p.getId());
				photoSets = photosetInt.getList(this.nsid);
			} else {
				photosetInt.addPhoto(ps.getId(), p.getId());
			}
		}
		else
		{
			String currId=ps.getId();
			photosetInt.removePhoto(ps.getId(), p.getId());
			ps = null;
			Iterator<Photoset> sets = photoSets.getPhotosets().iterator();
			// Find the destination Album
			while (sets.hasNext()) {
				Photoset set = (Photoset) sets.next();
				if (set.getTitle().equals(newAlbumName)) {
					// Yepee we found it
					ps = set;
					break;
				}
			}
			if (ps == null) {
				// Create new album with requested name
				ps = photosetInt.create(newAlbumName, "", p.getId());
				photoSets = photosetInt.getList(this.nsid);
			} else {
				photosetInt.addPhoto(ps.getId(), p.getId());
				System.out.println("Moving Set from: "+currId+" to " +ps.getId());
			}
		}
	}
}
