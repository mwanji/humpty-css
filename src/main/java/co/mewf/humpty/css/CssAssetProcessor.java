package co.mewf.humpty.css;

import javax.inject.Inject;

import org.webjars.WebJarAssetLocator;

import co.mewf.humpty.config.PreProcessorContext;
import co.mewf.humpty.spi.processors.AssetProcessor;

public class CssAssetProcessor implements AssetProcessor {
  
  private CssUrlRewriter urlRewriter;
  
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
    return urlRewriter.rewrite(asset);
  }
  
  @Inject
  public void configure(WebJarAssetLocator locator) {
    urlRewriter = new CssUrlRewriter(locator);
  }

}
