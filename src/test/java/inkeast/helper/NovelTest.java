
package inkeast.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import wuxiaworld.helper.WuxiaWorld;
import wuxiaworld.helper.WuxiaworldHelperApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WuxiaworldHelperApplication.class)
public class NovelTest {

	@Autowired
	private WuxiaWorld wuxiaWorld;

	@Test
	public void testNovel() throws Exception {
		wuxiaWorld.update("www.wuxiaworld.com", "ancient-strengthening-technique");
	}
}
