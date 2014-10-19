package co.mewf.humpty.css;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.webjars.WebJarAssetLocator;

public class CssUrlRewriterTest {

  @Test
  public void should_shorten_font_urls_to_webjar_name() throws Exception {
    CssUrlRewriter rewriter = new CssUrlRewriter(new WebJarAssetLocator());
    
    String rewrittenCss = rewriter.rewrite(IOUtils.toString(getClass().getResourceAsStream("fonts.css")));
    
    assertEquals(IOUtils.toString(getClass().getResourceAsStream("fonts_rewritten.css")), rewrittenCss);
  }
}
