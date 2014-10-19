package co.mewf.humpty.css;

import java.net.URI;

import org.apache.commons.io.FilenameUtils;
import org.webjars.WebJarAssetLocator;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSFontFaceRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

public class CssUrlRewriter {
  
  private final WebJarAssetLocator locator;
  
  public CssUrlRewriter(WebJarAssetLocator locator) {
    this.locator = locator;
  }

  public String rewrite(String css) {
    CascadingStyleSheet styleSheet = CSSReader.readFromString(css, ECSSVersion.CSS30);
    CascadingStyleSheet rewrittenStyleSheet = new CascadingStyleSheet();
    CSSWriterSettings settings = new CSSWriterSettings(ECSSVersion.CSS30);
    settings.setQuoteURLs(true);
    CSSWriter writer = new CSSWriter(settings);
    writer.setWriteHeaderText(false);
    writer.setWriteFooterText(false);
    
    styleSheet.getAllRules().stream().forEach(rule -> {
      if (rule instanceof CSSFontFaceRule) {
        CSSFontFaceRule rewrittenRule = new CSSFontFaceRule();
        ((CSSFontFaceRule) rule).getAllDeclarations().stream().forEach(d -> {
          if (d.getProperty().equals("font-family")) {
            rewrittenRule.addDeclaration(d);
          } else if (d.getProperty().equals("src")) {
            d.getExpression().getAllMembers().stream().filter(m -> m instanceof CSSExpressionMemberTermURI).map(m -> (CSSExpressionMemberTermURI) m).forEach(m -> {
              String originalName = m.getURIString();
              String name = FilenameUtils.getName(originalName);
              if (name.indexOf('?') > -1) {
                name = name.substring(0, name.indexOf('?'));
              }
              if (name.indexOf('#') > -1) {
                name = name.substring(0, name.indexOf('#'));
              }
              String fullPath = locator.getFullPath(name).replaceFirst("META-INF/resources", "");
              
              m.setURIString(URI.create(fullPath.substring(0, fullPath.lastIndexOf('/') + 1) + originalName).normalize().toString());
            });
          }
        });
      }
      rewrittenStyleSheet.addRule(rule);
    });
    
    return writer.getCSSAsString(rewrittenStyleSheet);
  }

}
