package prog;

import robocode.JuniorRobot;

public class TestRobot extends JuniorRobot {

    private boolean shouldMove = true;

    @Override
    public void run() {
        super.run();

        while (shouldMove) {
            ahead(200);
            turnRight(90);
            //ahead(200);
        }
    }

    @Override
    public void onScannedRobot() {
        super.onScannedRobot();

        shouldMove = false;

        turnGunTo(scannedAngle+5);
        fire(0.5);
        fire(0.5);

        shouldMove = true;
    }
}
