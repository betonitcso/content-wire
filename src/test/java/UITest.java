import com.contentwire.ui.display.dashboard.DashboardUI;
import com.contentwire.ui.display.dashboard.components.pages.orchestrator.pages.Dashboard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UITest {

    @Test
    public void DashboardUIPageTest() {
        DashboardUI window = new DashboardUI();
        window.setPage(new Dashboard("Dashboard", window));
        Assertions.assertEquals(Dashboard.class, window.getCurrentPage().getClass());
    }
}
