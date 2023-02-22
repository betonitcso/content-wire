import com.contentwire.model.CampaignManager;
import com.contentwire.service.resource.management.UserManagementService;
import com.contentwire.ui.window.router.exceptions.WrongCredentialsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserManagementServiceTest {
    CampaignManager cm;

    @BeforeEach
    public void init() {
        String pwHash = UserManagementService.genHash("mock_password");
        cm = new CampaignManager("mock_manager_name", pwHash);
    }

    @Test
    public void authenticationTest() {
        Assertions.assertDoesNotThrow(() -> UserManagementService.authenticate(cm, "mock_password"));
    }

    @Test
    public void wrongPasswordAuthenticationTest() {
        Assertions.assertThrows(WrongCredentialsException.class, () -> UserManagementService.authenticate(cm, "wrong_password"));
    }

    @Test
    public void wrongManagerTest() {
        String otherHash = UserManagementService.genHash("other_manager_password");
        CampaignManager otherManager = new CampaignManager("manager_with_ill_intentions", otherHash);
        Assertions.assertThrows(WrongCredentialsException.class, () -> UserManagementService.authenticate(otherManager, "mock_password"));
    }
}
