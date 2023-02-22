import com.contentwire.service.email.management.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionServiceTest {

    @BeforeEach
    public void init() {
        System.setProperty("mail.smtp.host", "");
        System.setProperty("mail.smtp.port", "");
        new SessionService("smtp.gmail.com", 465);
    }

    @Test
    public void sessionHostTest() {
        Assertions.assertEquals("smtp.gmail.com", System.getProperty("mail.smtp.host"));
    }

    @Test
    public void sessionPortTest() {
        Assertions.assertEquals("465", System.getProperty("mail.smtp.port"));
    }
}
