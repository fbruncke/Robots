package prog;

import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import java.awt.*;

public class AnkleBiter extends AdvancedRobot {

    @java.lang.Override
    public void run() {
        setRadarColor(Color.red);
        setGunColor(Color.white);
        setBodyColor(Color.red);
        setScanColor(Color.green);
        setBulletColor(Color.yellow);


        while(true) {
            turnGunRightRadians(Double.POSITIVE_INFINITY);
            scan();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        setTurnGunRight(1.0 * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getGunHeading()));
        setTurnRight(2.0 * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getHeading()));
        fire(9001);
        setAhead(20000);
    }


}
