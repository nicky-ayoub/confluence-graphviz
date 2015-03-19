package com.nickyayoub.confluence.macro.graphviz;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.importexport.resource.DownloadResourceWriter;
import com.atlassian.confluence.importexport.resource.WritableDownloadResourceManager;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class GeevizMacro implements Macro {

    private static final Logger log = LoggerFactory.getLogger(GeevizMacro.class);
    private final WritableDownloadResourceManager downloadResourceManager;

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    public GeevizMacro(WritableDownloadResourceManager downloadResourceManager) {

        this.downloadResourceManager = downloadResourceManager;
    }

    @Override
    public String execute(Map<String, String> parameters, String bodyContent,
                          ConversionContext context) throws MacroExecutionException {


        parameters = toLowerCase(parameters);

        boolean showSource = new Boolean((String) parameters.get("showsource"));
        StringBuilder builder = new StringBuilder();
        bodyContent = bodyContent.replaceFirst("[\\p{Zs}\\s]*$", ""); // Unicode non-breaking space \p{Zs}
        bodyContent = bodyContent.replaceFirst("^[\\p{Zs}\\s]*", "");
//        byte[] buf = bodyContent.getBytes();
//        log.error(String.format("body : %sEmpty",  bodyContent.isEmpty() ? "" : "Not " ));
//        log.error(String.format("body : -->%s<--", bodyContent));
//        log.error( Hex.encodeHexString(buf));

        if (bodyContent.isEmpty()) {  // Checking for empty was not good enough. The Unicode NBSP was never stripped.

            builder.append("<div class=\"confluence-information-macro confluence-information-macro-warning\">\n");
            builder.append("<span class=\"aui-icon aui-icon-small aui-iconfont-error confluence-information-macro-icon\"></span>\n");
            builder.append("<div class=\"confluence-information-macro-body\">");
            builder.append("The Macro Body is empty. There is no graph!");
            builder.append("</div>");
            builder.append("</div>");
        } else {
            String gv = bodyContent;
            DownloadResourceWriter downloadResourceWriter = downloadResourceManager.getResourceWriter(
                    StringUtils.defaultString(AuthenticatedUserThreadLocal.getUsername()),
                    "graph", String.format("%s%s", '.', "png")
            );
            OutputStream outputStream = null;
            try {
                outputStream = downloadResourceWriter.getStreamForWriting();
                byte[] image = Graphviz.getGraph(gv);
                outputStream.write(image);

                int width = 100; //getIntegerParameter(parameters, "width", DEFAULT_WIDTH, 0);
                //if (width < 0)
                //    width = DEFAULT_WIDTH;
                int height = 100; //getIntegerParameter(parameters, "height", DEFAULT_HEIGHT, 0);

                builder.append(
                        String.format(
                                //"\n<img src=\"%s\" width=\"%d\" height=\"%d\">\n",
                                "\n<img src=\"%s\">\n",
                                downloadResourceWriter.getResourcePath()
                        )
                );
                if(showSource) {
                    builder.append(String.format("\n<pre>\n%s\n</pre>\n", gv));
                }
            } catch (IOException e) {
                builder.append("<div class=\"aui-message aui-message-error\">\n");
                builder.append("<p class=\"title\"><strong>The GraphViz image generation failed...</strong></p>\n");
                builder.append("<p>");
                builder.append(e.getMessage());
                builder.append("</p>\n");
                builder.append("</div><!-- .aui-message -->\n");
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
        }
        return builder.toString();

    }

    @Override
    public BodyType getBodyType() {
        return BodyType.PLAIN_TEXT;
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }

    // force integer parameter to default if less than lower bound
    int getIntegerParameter(Map parameters, String param, int def, int lowerBound) throws MacroExecutionException {
        int result = getIntegerParameter(parameters, param, new Integer(def)).intValue();
        if (result < lowerBound) {
            result = def;
        }
        return result;
    }

//	private int getIntegerParameter(Map parameters, String param, int def) throws MacroExecutionException {
//      return getIntegerParameter(parameters, param, new Integer(def)).intValue();
//  }

    Integer getIntegerParameter(Map parameters, String param, Integer def) throws MacroExecutionException {
        Integer result = def;
        if (!StringUtils.isEmpty((String) parameters.get(param))) {
            try {
                result = new Integer((String) parameters.get(param));
            } catch (NumberFormatException exception) {
                throw new MacroExecutionException("Invalid " + param + " parameter.  It must be an integer.");
            }
        }
        return result;
    }

    Map<String, String> toLowerCase(Map<String, String> params) {
        Map<String, String> paramsWithLowerCasedKeys = new HashMap<String, String>(params.size());
        for (Map.Entry<String, String> paramValue : params.entrySet())
            paramsWithLowerCasedKeys.put(StringUtils.lowerCase(paramValue.getKey()), paramValue.getValue());

        return paramsWithLowerCasedKeys;
    }

}
