package prog;


import robocode.Bullet;
import robocode.BulletMissedEvent;
import robocode.Robot;
import robocode.JuniorRobot;

import java.awt.*;
import java.util.Random;

public class skittleBot extends JuniorRobot {

    private boolean shouldMove = true;
    Random random = new Random();

    @Override
    public void run() {
        while (shouldMove) {
            turnGunRight(10);
            setColors(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
        }
    }

    @Override
    public void onScannedRobot() {
        shouldMove = false;
        turnGunTo(scannedAngle);
        fire(50);
        setColors(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    public void onHitByBullet() {
        shouldMove = true;
        turnGunTo(hitByBulletAngle);
        setColors(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
    }
}




