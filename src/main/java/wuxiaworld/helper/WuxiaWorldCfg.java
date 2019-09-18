package wuxiaworld.helper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class WuxiaWorldCfg {

	private String all_url = //
			"https://%s/api/novels/search";
	private String post_json = "{\"count\":100}";
	private String cfg_path = "/web/github.io/inkeast.com/%s.json";

	public void config(String domain) throws Exception {

		URL u = new URL(String.format(all_url, domain));
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.addRequestProperty("User-agent", "Mozilla/4.0");
		conn.addRequestProperty("content-type", "application/json");
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		out.print(post_json);
		out.flush();
		out.close();

		InputStream in = conn.getInputStream();
		OutputStream o = new FileOutputStream(String.format(cfg_path, domain), false);
		IOUtils.copy(in, o);
	}
}
