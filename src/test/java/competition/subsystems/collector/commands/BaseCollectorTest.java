package competition.subsystems.collector.commands;

import org.junit.Ignore;

import competition.BaseCompetitionTest;
import competition.operator_interface.OperatorInterface;
import competition.subsystems.collector.CollectorSubsystem;

@Ignore
public class BaseCollectorTest extends BaseCompetitionTest {
    protected CollectorSubsystem collectorSubsystem;
    protected OperatorInterface oi;

    @Override
    public void setUp() {
        super.setUp();

        collectorSubsystem = getInjectorComponent().collectorSubsystem();
    }
}
