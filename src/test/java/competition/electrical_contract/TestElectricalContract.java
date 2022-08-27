package competition.electrical_contract;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestElectricalContract extends CompetitionContract {

    private boolean canCodersReady = true;

    @Inject
    public TestElectricalContract() {}

    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public boolean areCanCodersReady() {
        return canCodersReady;
    }

    @Override
    public boolean arePneumaticsReady() {
        return true;
    }

    @Override
    public boolean isShooterReady() {
        return true;
    }

    public void setCanCodersReady(boolean areReady) {
        canCodersReady = areReady;
    }

    @Override
    public boolean isClimberReady() {
        return true;
    }
}
