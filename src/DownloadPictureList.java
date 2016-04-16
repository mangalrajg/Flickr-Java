import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Extras;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DownloadPictureList {
	private final Flickr flickr;
	private static int MAX_COUNT = 500;
	private String nsid;



	public DownloadPictureList() throws FlickrException, IOException, SAXException {
		AuthManager am = new AuthManager();
		flickr = am.getFlickr();
		nsid = am.getNsId();
	}


	public void getPhotoList() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		PrintWriter dumpText = new PrintWriter("dumpText.txt", "UTF-8"); 
		PrintWriter dumpJSON = new PrintWriter("dumpJSON.txt", "UTF-8"); 


		PhotosetsInterface pi = flickr.getPhotosetsInterface();
		Set<String> extras = new HashSet<String>();
		extras.add(Extras.DATE_TAKEN);
		Iterator<Photoset> sets = pi.getList(this.nsid).getPhotosets().iterator();
		JSONObject myFlickrData = new JSONObject();
		myFlickrData.put("nsid", this.nsid);
		JSONArray albumList = new JSONArray();
		myFlickrData.put("albums", albumList);
		while (sets.hasNext()) 
		{
			Photoset set = (Photoset) sets.next();
			JSONObject jSONAlbum = new JSONObject();
			jSONAlbum.put("name", set.getTitle());
			jSONAlbum.put("id", set.getId());
			albumList.put(jSONAlbum);
			JSONArray photoList = new JSONArray();

			int count = MAX_COUNT;
			int page = 1;
			while (count == MAX_COUNT) {
				PhotoList<Photo> photos = pi.getPhotos(set.getId(), extras,
						Flickr.PRIVACY_LEVEL_NO_FILTER, MAX_COUNT, page);
				count = photos.size();
				page++;
				Iterator<Photo> i = photos.iterator();
				while (i.hasNext()) {
					Photo p = ((Photo) i.next());
					dumpText.println(set.getId() + ":" + set.getTitle() + ":"
							+ p.getId() + ":" + p.getTitle() + ":"
							+ df.format(p.getDateTaken()) );
					JSONObject jSONPhoto = new JSONObject();
					jSONPhoto.put("name", p.getTitle());
					jSONPhoto.put("id", p.getId());
					jSONPhoto.put("dateteken",df.format(p.getDateTaken()));
					photoList.put(jSONPhoto);
				}
				jSONAlbum.put("photos", photoList);
			}
		}
		dumpText.close();

		dumpJSON.println(myFlickrData.toString(2));
		dumpJSON.close();
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
		DownloadPictureList bf = new DownloadPictureList();
		bf.getPhotoList();
	}
}