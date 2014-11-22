package co.mewf.humpty.css;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.webjars.WebJarAssetLocator;

public class CssUrlRewriterTest {

  private final CssUrlRewriter rewriter = new CssUrlRewriter(new WebJarAssetLocator());
  
  @Test
  public void should_change_font_urls_to_servlet3_resource_path() throws Exception {
    String rewrittenCss = rewriter.rewrite(IOUtils.toString(getClass().getResourceAsStream("fonts.css")));
    
    assertEquals(IOUtils.toString(getClass().getResourceAsStream("fonts_rewritten.css")), rewrittenCss);
  }
  
  @Test
  public void should_change_background_url_to_servlet3_resource_path() throws Exception {
    String rewrittenCss = rewriter.rewrite(IOUtils.toString(getClass().getResourceAsStream("background.css")));
    
    assertEquals(IOUtils.toString(getClass().getResourceAsStream("background_rewritten.css")), rewrittenCss);
  }
  
  @Test
  public void should_change_background_image_url_to_servlet3_resource_path() throws Exception {
    String rewrittenCss = rewriter.rewrite(IOUtils.toString(getClass().getResourceAsStream("background-image.css")));
    
    assertEquals(IOUtils.toString(getClass().getResourceAsStream("background-image_rewritten.css")), rewrittenCss);
  }
}
