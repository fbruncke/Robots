package prog;

import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.awt.*;

public class RasmusHBot extends Robot {

    int dist= 10;
    boolean found = false;

    public RasmusHBot(){
    }

    @java.lang.Override
    public void run() {
        //find robot
        this.turnRight(90);
        while (true) {
            this.turnGunRight(dist);
            this.ahead(dist);
            this.setBodyColor(Color.black);
        }
        //shoot at robot
        //dodge bullets
    }

    public void onScannedRobot(ScannedRobotEvent e){
        if (e.getDistance()<50 && e.getEnergy()<50) {
            this.fireBullet(3);
        }
        else if (e.getEnergy()>80){
            this.fireBullet(2);
        }
        else {
            this.fireBullet(1);
        }
        dist *= -1;
        this.scan();
    }
}