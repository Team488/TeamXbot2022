package competition.injection.arm;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ArmComponentHolder {
    
    public final ArmComponent leftArm;

    public final ArmComponent rightArm;

    @Inject
    public ArmComponentHolder(@LeftArm ArmComponent left, @RightArm ArmComponent right) {
        this.leftArm = left;
        this.rightArm = right;
    }

}
