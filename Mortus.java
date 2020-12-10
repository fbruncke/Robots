package prog;

import robocode.*;

import static java.awt.Color.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * Our implementation of a RoboCode AdvancedRobot.
 *
 * @author  Denis, Jonas, Morten, Niclas, and Rasmus.
 * @version 2.4
 * @since   09.12.2020
 */
public class Mortus extends AdvancedRobot {
    private final double NORTH = 360;
    private final double EAST = 90;
    private final double SOUTH = 180;
    private final double WEST = 270;

    @Override
    public void run() {
        setBodyColor(gray);
        setGunColor(red);
        setRadarColor(red);
        setBulletColor(red);
        setScanColor(green);

        goToEdge();

        while (true) {
            if (getDistance() <= 50) {
                turnLeft(45);
            } else {
                setAhead(getDistance());
                execute();

                turnGunLeft(90);
            }
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        if (event.getDistance() <= 250) {
            setTurnGunRight(normalRelativeAngleDegrees(event.getBearing() + (getHeading() - getRadarHeading())));
            setFire(Math.min(300 / event.getDistance(), 3));

            execute();
        }
    }

    private void goToEdge() {
        double distanceTop = getBattleFieldHeight() - getY();
        double distanceRight = getBattleFieldWidth() - getX();
        double distanceBottom = Math.abs(0 - getY());
        double distanceLeft = Math.abs(0 - getX());

        double minDistance = Math.min(Math.min(distanceTop, distanceBottom), Math.min(distanceLeft, distanceRight));

        if (minDistance == distanceTop) {
            if (getHeading() >= NORTH / 2.0) {
                turnRight(NORTH - getHeading());
            } else {
                turnLeft(getHeading());
            }
        } else if (minDistance == distanceBottom) {
            if (getHeading() >= SOUTH) {
                turnLeft(getHeading() - SOUTH);
            } else {
                turnRight(SOUTH - getHeading());
            }
        } else if (minDistance == distanceLeft) {
            if (getHeading() >= WEST) {
                turnLeft(getHeading() - WEST);
            } else {
                turnRight(WEST - getHeading());
            }
        } else if (minDistance == distanceRight) {
            if (getHeading() >= EAST) {
                turnLeft(getHeading() - EAST);
            } else {
                turnRight(EAST - getHeading());
            }
        }

        ahead(getDistance());
    }

    private double getDistance() {
        final double OFFSET = 30;

        if (getHeading() == 0) {
            return getBattleFieldHeight() - getY() - OFFSET;
        } else if (getHeading() == EAST) {
            return getBattleFieldWidth() - getX() - OFFSET;
        } else if (getHeading() == SOUTH) {
            return Math.abs(0 - getY()) - OFFSET;
        } else if (getHeading() == WEST) {
            return Math.abs(0 - getX()) - OFFSET;
        } else {
            return  0;
        }
    }
}