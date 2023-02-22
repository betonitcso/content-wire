package com.contentwire.service.resource.management;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A Singleton responsible for creating HTML Documents for E-Mail previews.
 */

public class PreviewManagementService {

    /**
     * Generates an HTML Document from given text, then displays it in the default browser.
     * @param html text from E-Mail
     * @throws IOException the file operations couldn't be executed.
     */
    public static void previewHTML(String html) throws IOException {
        File src = new File("src/main/resources/generated/preview.html");
        Files.write(src.toPath(), html.getBytes());
        Desktop.getDesktop().browse(src.toURI());
    }
}
