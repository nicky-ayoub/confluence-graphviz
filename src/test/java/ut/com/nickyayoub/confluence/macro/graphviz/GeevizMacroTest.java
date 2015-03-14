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

    @Test
    public void testStringTrim() throws Exception {
        String s = "       A    ";
        s = s.trim();
        assertTrue("Trimmed", s.equals("A"));
    }
        @Test
    public void testStringFront() throws Exception {
        String s = "          ";
        s = s.replaceFirst("^\\s*", "");
        assertTrue("compress empty", s.isEmpty());
    }
        @Test
    public void testStringBack() throws Exception {
        String s = "          ";
        s = s.replaceFirst("\\s*$", "");
        assertTrue("compress empty", s.isEmpty());
    }
            @Test
    public void testSpaceFront() throws Exception {
        String s = " ";
        s = s.replaceFirst("^\\s*", "");
        assertTrue("compress space front", s.isEmpty());
    }
        @Test
    public void testSpaceBack() throws Exception {
        String s = " ";
        s = s.replaceFirst("\\s*$", "");
        assertTrue("compress space back", s.isEmpty());
    }
    public void testUnicodeBack() throws Exception {
        String s = "\uC2A0";
        s = s.replaceFirst("[\\p{Zs}\\s]*$", "");
        assertTrue("compress U space back", s.isEmpty());
    }
    public void testUnicodeFront() throws Exception {
        String s = "\uC2A0";
        s = s.replaceFirst("^[\\p{Zs}\\s]*", "");
        assertTrue("compress sI pace back", s.isEmpty());
    }
}
