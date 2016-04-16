import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

import java.util.Scanner;

/**
 * Demonstrates the authentication-process.
 * <p>
 * 
 * If you registered API keys, you find them with the shared secret at your <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API
 * keys</a>
 * 
 * @author mago
 * @version $Id: AuthExample.java,v 1.6 2009/08/25 19:37:45 x-mago Exp $
 */
public class AuthManager {
	private Flickr flickr;
	private AuthStore authStore;
	private String nsid;

	private void authorize() throws IOException, SAXException, FlickrException {
		AuthInterface authInterface = flickr.getAuthInterface();
		Token accessToken = authInterface.getRequestToken();

		String url = authInterface.getAuthorizationUrl(accessToken,
				Permission.WRITE);
		System.out.println("Follow this URL to authorise yourself on Flickr");
		System.out.println(url);
		System.out.println("Paste in the token it gives you:");
		System.out.print(">>");

		String tokenKey = new Scanner(System.in).nextLine();

		Token requestToken = authInterface.getAccessToken(accessToken,
				new Verifier(tokenKey));

		Auth auth = authInterface.checkToken(requestToken);
		RequestContext.getRequestContext().setAuth(auth);
		this.authStore.store(auth);

		System.out
				.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");
	}
	public Flickr getFlickr()
	{
		return flickr;
	}
	public String getNsId()
	{
		return nsid;
	}
	
	public AuthManager() throws FlickrException, IOException, SAXException
	{
		String apiKey = "6c5f9affbbd5f9f96054300504900e92";
		String sharedSecret = "c9fbcf157653bcd9";
		RequestContext rc = RequestContext.getRequestContext();

		flickr = new Flickr(apiKey, sharedSecret, new REST());
		File authsDir = new File(
				System.getProperty("user.home") + File.separatorChar
						+ ".flickrAuth");
		if (authsDir != null) {
			this.authStore = new FileAuthStore(authsDir);
		}
		if (this.authStore != null) {
			Auth auth = null;
			Auth[] auths = this.authStore.retrieveAll();
			if (auths.length > 0)
				auth = auths[0];

			if (auth == null) {
				this.authorize();
			} else {
				rc.setAuth(auth);
			}
			this.nsid = auth.getUser().getId();
		}



	}

}