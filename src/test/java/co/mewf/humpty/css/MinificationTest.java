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

public class MinificationTest {

  private final Pipeline pipeline = new HumptyBootstrap(Configuration.load("MinificationTest/humpty.toml")).createPipeline();
  private final Path resourcesDir = Paths.get("src", "test", "resources", "MinificationTest");
  
  @Test
  public void should_minify_in_production_mode() throws Exception {
    String asset = pipeline.process("bundle.css").getAsset();
    
    assertEquals(read("humpty-css.min.css"), asset);
  }
  
  @Test
  public void should_not_minify_in_development_mode() throws Exception {
    String asset = pipeline.process("bundle.css/humpty-css.css").getAsset();
    
    assertEquals(read("humpty-css.css").trim(), asset.trim());
  }
  
  @Test
  public void should_not_minify_preminified_asset() throws Exception {
    String asset = pipeline.process("preminified.css").getAsset();
    
    assertEquals(read("bootstrap.min.css").trim(), asset.trim());
  }
  
  private String read(String file) throws IOException {
    return new String(Files.readAllBytes(resourcesDir.resolve(file)));
  }
}
