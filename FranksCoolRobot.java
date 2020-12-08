package prog;

import robocode.JuniorRobot;
import robocode.Robot;

public class FranksCoolRobot extends JuniorRobot {

    @Override
    public void run() {
        super.run();

        while (true) {
            ahead(200);
            turnLeft(90);
            //ahead(200);
        }
    }

    @Override
    public void onScannedRobot() {
        super.onScannedRobot();
        turnTo(scannedAngle);
        fire(1);
    }
}
