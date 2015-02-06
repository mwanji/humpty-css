package co.mewf.humpty.css;

import java.net.URI;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.webjars.WebJarAssetLocator;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermURI;
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
    CSSWriterSettings settings = new CSSWriterSettings(ECSSVersion.CSS30);
    settings.setQuoteURLs(true);
    settings.setOptimizedOutput(false);
    CSSWriter writer = new CSSWriter(settings);
    writer.setWriteHeaderText(false);
    writer.setWriteFooterText(false);
    
    styleSheet.getAllFontFaceRules()
              .stream()
              .map(rule -> rule.getAllDeclarationsOfPropertyNameCaseInsensitive("src"))
              .flatMap(List::stream)
              .filter(declaration -> declaration.getProperty().equals("src"))
              .map(declaration -> declaration.getExpression().getAllMembers())
              .flatMap(List::stream)
              .filter(member -> member instanceof CSSExpressionMemberTermURI)
              .map(member -> (CSSExpressionMemberTermURI) member)
              .forEach(member -> {
                String originalName = member.getURIString();
                String name = FilenameUtils.getName(originalName);
                if (name.indexOf('?') > -1) {
                  name = name.substring(0, name.indexOf('?'));
                }
                if (name.indexOf('#') > -1) {
                  name = name.substring(0, name.indexOf('#'));
                }
                String fullPath = locator.getFullPath(name).replaceFirst("META-INF/resources", "");

                member.setURIString(URI.create(fullPath.substring(0, fullPath.lastIndexOf('/') + 1) + originalName).normalize().toString());
              });
    
    styleSheet.getAllStyleRules()
              .stream()
              .map(rule -> {
                List<CSSDeclaration> declarations = rule.getAllDeclarationsOfPropertyNameCaseInsensitive("background");
                declarations.addAll(rule.getAllDeclarationsOfPropertyNameCaseInsensitive("background-image"));
                return declarations;
              })
              .flatMap(List::stream)
              .map(declaration -> declaration.getExpression().getAllMembers())
              .flatMap(List::stream)
              .filter(member -> member instanceof CSSExpressionMemberTermURI)
              .map(member -> (CSSExpressionMemberTermURI) member)
              .forEach(member -> {
                String originalName = member.getURIString();
                String name = FilenameUtils.getName(originalName);
                String fullPath = locator.getFullPath(name).replaceFirst("META-INF/resources", "");
                member.setURIString(URI.create(fullPath.substring(0, fullPath.lastIndexOf('/') + 1) + originalName).normalize().toString());
              });
    
    return writer.getCSSAsString(styleSheet);
  }

}
