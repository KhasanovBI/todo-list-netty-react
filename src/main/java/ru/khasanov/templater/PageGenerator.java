package ru.khasanov.templater;

/**
 * Created by bulat on 19.01.17.
 */

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PageGenerator {
    private static final String HTML_DIR = "public_html";
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_23);

    static {
        try {
            CONFIGURATION.setTemplateLoader(new FileTemplateLoader(new File(HTML_DIR)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {

            Template template = CONFIGURATION.getTemplate(filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}
