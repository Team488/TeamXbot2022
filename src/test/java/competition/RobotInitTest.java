package competition;

import org.junit.Test;

public class RobotInitTest extends BaseCompetitionTest {
    @Test
    public void testDefaultSystem() {
        this.getInjectorComponent().subsystemDefaultCommandMap();
        this.getInjectorComponent().operatorCommandMap();
    }
}