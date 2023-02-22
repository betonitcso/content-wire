import com.contentwire.service.repository.service.ConnectionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ConnectionServiceTest {

    @Test
    public void invalidConnectionURITest() {
        Assertions.assertThrows(SQLException.class, () -> ConnectionService.setConnectionURI("NotAValidConnectionURI"));
    }
}
