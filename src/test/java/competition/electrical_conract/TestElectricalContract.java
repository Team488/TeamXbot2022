package competition.electrical_conract;

import competition.electrical_contract.CompetitionContract;

public class TestElectricalContract extends CompetitionContract {
    @Override
    public boolean isDriveReady() {
        return true;
    }

    @Override
    public boolean arePneumaticsReady() {
        return true;
    }
}
