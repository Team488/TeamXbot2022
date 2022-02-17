package competition.electrical_conract;

import com.google.inject.Singleton;

import competition.electrical_contract.CompetitionContract;

@Singleton
public class TestElectricalContract extends CompetitionContract {

    private boolean canCodersReady = true;

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
