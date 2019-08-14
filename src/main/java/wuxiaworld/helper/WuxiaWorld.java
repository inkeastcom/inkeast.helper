package wuxiaworld.helper;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class WuxiaWorld {

	private static Log log = LogFactory.getLog(WuxiaWorld.class);

	@Value("${posts.source.path}")
	private String posts_source_path;

	@Value("${cfg.path}")
	private String cfg_path;

	@Autowired
	private WuxiaworldCrawler wc;

	public void update(String path) throws Exception {
		update(path, false);
	}

	public void update(String path,
			// 是否覆盖旧版本
			boolean recover) throws Exception {

		String json = IOUtils.toString(new FileInputStream(cfg_path));
		Novels novels = new Gson().fromJson(json, Novels.class);

		Novel novel = null;
		for (Novel n : novels.getItems()) {
			if (n.getSlug().equals(path)) {
				novel = n;
				break;
			}
		}

		if (novel == null) {
			log.error("none: " + path);
			System.exit(1);
		}

		String name = novel.getName();
		String time = novel.getTimeCreated();
		if (time.indexOf(".") >= 0) {
			time = time.substring(0, time.indexOf("."));
		}
		long millis = Long.parseLong(time);
		if (millis < 0) {
			millis = System.currentTimeMillis();
		} else {
			millis *= 1000;
		}
		int chapterCount = novel.getChapterCount();
		String categories = //
				(novel.getGenres() == null || novel.getGenres().length == 0) ? //
						"Novel" : "[[\"" + String.join("\"],[\"", novel.getGenres()) + "\"]]";
		String tags = //
				(novel.getTags() == null || novel.getTags().length == 0) ? //
						name : "[[\"" + String.join("\"],[\"", novel.getTags()) + "\"]]";

		String keywords = String.format("%s English version,novel", name);
		String description = novel.getSynopsis().replace("\"", "'").replace(":", "");
		String index_url = "https://www.wuxiaworld.com/novel/" + path;
		String date_index = getDate(millis, chapterCount + 100);
		wc.crawl_index(index_url, name, path, date_index, categories, tags, keywords, description);
		log.warn("index finish");

		Document _doc = Jsoup.parse(new File(posts_source_path + path + "/" + path + ".html"), "UTF-8");
		Elements es = _doc.select("a");
		int i = 0;
		for (Element e : es) {
			String href = e.attr("href");
			if (href.startsWith("/novel")) {
				i++;
				String date = getDate(millis, i);
				int s = href.lastIndexOf("/");
				String filename = href.substring(s + 1);

				String url = "https://www.wuxiaworld.com" + href;

				String mulu = posts_source_path + path + "/" + path + "/";
				String filepath = mulu + filename + ".html";
				if (recover || !new File(filepath).exists()) {
					wc.crawl(url, name, path, filename, date, categories, tags, keywords, 3);
					log.warn(href + " finish");
				}
			}
		}
	}

	private static String getDate(long millis, int delay) {
		Calendar c_index = Calendar.getInstance();
		c_index.setTimeInMillis(millis);
		c_index.add(Calendar.MINUTE, 12 * delay);
		c_index.add(Calendar.SECOND, 23 * delay);
		SimpleDateFormat sdf_index = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf_index.format(c_index.getTime());
	}

}
