package competition.injection.arm;

public class ArmInstance {
    private final String label;

    public ArmInstance(String label)
    {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}