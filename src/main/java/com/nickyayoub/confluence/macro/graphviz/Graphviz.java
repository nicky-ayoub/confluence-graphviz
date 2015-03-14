package com.nickyayoub.confluence.macro.graphviz;

/**
 * Created by nicky on 3/8/15.
 */

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graphviz {
    private static String TEMP_DIR = "/var/tmp";
    private static String DOT = "/usr/bin/dot";
    private static String TYPE = "png";

    public static byte[] getGraph(String dot_source) throws IOException {
        File dot;
        byte[] img_stream;

        try {
            dot = writeDotSourceToFile(dot_source);
            if (dot != null) {
                img_stream = get_img_stream(dot);
                if (!dot.delete())
                    System.err.println("Warning: " + dot.getAbsolutePath() + " could not be deleted!");
                return img_stream;
            }
            return null;
        } catch (IOException ioe) {
            throw new IOException(ioe);
        }
    }
    public static String encodeGraph(String dot_source) throws IOException {
        return Base64.encodeBase64String(getGraph(dot_source));
    }

    private static int writeGraphToFile(byte[] img, String file) {
        File to = new File(file);
        return writeGraphToFile(img, to);
    }

    private static int writeGraphToFile(byte[] img, File to) {
        try {
            FileOutputStream fos = new FileOutputStream(to);
            fos.write(img);
            fos.close();
        } catch (IOException ioe) {
            return -1;
        }
        return 1;
    }

    private static byte[] get_img_stream(File dot) throws IOException {
        File img;
        byte[] img_stream = null;

        try {
            img = File.createTempFile("graph_", "." + TYPE, new File(Graphviz.TEMP_DIR));

             List<String> commands = new ArrayList<String>();
                commands.add(DOT);
                commands.add("-T" + TYPE);
                commands.add(dot.getAbsolutePath());
                commands.add("-o");
                commands.add(img.getAbsolutePath());
             ProcessBuilder pb = new ProcessBuilder(commands);
            Process process = pb.start();

            process.waitFor();

            FileInputStream in = new FileInputStream(img.getAbsolutePath());
            img_stream = new byte[in.available()];
            in.read(img_stream);
            // Close it if we need to
            if (in != null) in.close();

            if (!img.delete())
                System.err.println("Warning: " + img.getAbsolutePath() + " could not be deleted!");
        } catch (IOException ioe) {
            System.err.println("Error:    in I/O processing of tempfile in dir " + Graphviz.TEMP_DIR );
            System.err.println("       or in calling external command");
          //  ioe.printStackTrace();
            throw new IOException(ioe);

        } catch (InterruptedException ie) {
            System.err.println("Error: the execution of the external program was interrupted");
            //ie.printStackTrace();
             throw new Error(ie);
        }

        return img_stream;
    }



    private static File writeDotSourceToFile(String data) throws IOException {
        File temp = File.createTempFile("graph_", ".dot.tmp", new File(Graphviz.TEMP_DIR));
        FileUtils.writeStringToFile(temp, data);
        return temp;
    }

}
