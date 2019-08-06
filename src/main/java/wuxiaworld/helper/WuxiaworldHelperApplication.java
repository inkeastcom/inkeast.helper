package wuxiaworld.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class WuxiaworldHelperApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(WuxiaworldHelperApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WuxiaworldHelperApplication.class, args);
	}

	@Autowired
	private WuxiaWorldCfg cfg;

	@Autowired
	private WuxiaWorld wuxiaWorld;

	@Override
	public void run(String... args) throws Exception {
		if (args == null || args.length == 0) {
			log.error("no args");
		} else {
			if ("config".equals(args[0])) {
				cfg.config();
			} else if (StringUtils.hasText(args[0])) {
				if (args.length >= 2 || "force".equals(args[1])) {
					wuxiaWorld.update(args[0], true);
				}
				wuxiaWorld.update(args[0]);
			}
		}
	}
}
