package co.mewf.humpty.css;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import co.mewf.humpty.Pipeline;
import co.mewf.humpty.config.Configuration;
import co.mewf.humpty.config.HumptyBootstrap;

public class CssUrlRewriterTest {

  private final Path resourcesDir = Paths.get("src", "test", "resources", "CssUrlRewriterTest");
  private final Pipeline pipeline = new HumptyBootstrap(Configuration.load("CssUrlRewriterTest/humpty.toml")).createPipeline();
  
  @Test
  public void should_change_font_urls_to_servlet3_resource_path() throws Exception {
    String asset = pipeline.process("bundle.css/fonts.css").getAsset();
    
    assertEquals(read("fonts_rewritten.css").trim(), asset.trim());
  }
  
  @Test
  public void should_change_background_url_to_servlet3_resource_path() throws Exception {
    String asset = pipeline.process("bundle.css/background.css").getAsset();
    
    assertEquals(read("background_rewritten.css").trim(), asset.trim());
  }
  
  @Test
  public void should_change_background_image_url_to_servlet3_resource_path() throws Exception {
    String asset = pipeline.process("bundle.css/background-image.css").getAsset();
    
    assertEquals(read("background-image_rewritten.css").trim(), asset.trim());
  }
  
  private String read(String file) throws IOException {
    return new String(Files.readAllBytes(resourcesDir.resolve(file)));
  }
}
