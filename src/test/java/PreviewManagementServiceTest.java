import com.contentwire.service.resource.management.PreviewManagementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PreviewManagementServiceTest {

    private String HTMLMessage;

    @BeforeEach
    public void init() throws IOException {
        HTMLMessage = "this the HTML body of a fascinating E-Mail.";
        PreviewManagementService.previewHTML(HTMLMessage);
    }


    @Test
    public void htmlTextFileCreatedTest() {
        File previewFile = new File("src/main/resources/generated/preview.html");
        Assertions.assertTrue(previewFile.exists());
    }

    @Test
    public void htmlTextFileContentTest() throws IOException {
        File previewFile = new File("src/main/resources/generated/preview.html");
        Assertions.assertEquals(new String(Files.readAllBytes(previewFile.toPath())), HTMLMessage);
    }

    @Test
    public void messageBodyChangeTest() throws IOException {
        String newMessageBody = "<h1>this is not the same message body as before.</h1>";
        PreviewManagementService.previewHTML(newMessageBody);
        File previewFile = new File("src/main/resources/generated/preview.html");
        Assertions.assertNotEquals(new String(Files.readAllBytes(previewFile.toPath())), HTMLMessage);
    }
}
