package co.mewf.humpty.css;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

import javax.inject.Inject;

import org.webjars.WebJarAssetLocator;

import co.mewf.humpty.config.Configuration;
import co.mewf.humpty.config.PreProcessorContext;
import co.mewf.humpty.spi.processors.AssetProcessor;

public class CssAssetProcessor implements AssetProcessor {
  
  private CssUrlRewriter urlRewriter;
  private Optional<Boolean> minify;
  
  @Override
  public String getName() {
    return "css";
  }

  @Override
  public boolean accepts(String assetName) {
    return assetName.endsWith(".css");
  }

  @Override
  public String processAsset(String assetName, String asset, PreProcessorContext context) {
    if (urlRewriter != null) {
      asset = urlRewriter.rewrite(asset);
    }
    
    if (!assetName.endsWith(".min.css") && minify.orElse(context.getMode() == Configuration.Mode.PRODUCTION)) {
      try {
        StringWriter writer = new StringWriter();
        new CssCompressor(new StringReader(asset)).compress(writer, -1);
        asset = writer.toString();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
    return asset;
  }
  
  @Inject
  public void configure(WebJarAssetLocator locator, Configuration.Options options) {
    urlRewriter = options.get("rewrite", true) ? new CssUrlRewriter(locator) : null;
    minify = Optional.<Boolean>ofNullable(options.get("minify", null));
  }
}
