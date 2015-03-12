package ut.com.nickyayoub.confluence.macro.graphviz;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.importexport.resource.WritableDownloadResourceManager;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

/**
 * Testing {@link com.nickyayoub.confluence.macro.graphviz.GeevizMacro}
 */
@RunWith(MockitoJUnitRunner.class)
public class GeevizMacroTest extends TestCase {
    private final ConversionContext mockConversionContext = mock(ConversionContext.class);
    @Mock
    private WritableDownloadResourceManager downloadResourceManager;

    @Test
    public void testGeeviz() throws Exception {
           assertTrue("oK", true);
     }

}
