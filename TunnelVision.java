/**
 * trash code by jesper
 */
package prog;

import robocode.*;
import java.awt.*;
import java.util.ArrayList;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class TunnelVision extends AdvancedRobot {
    ArrayList<ScannedRobotEvent> enemies = new ArrayList<>();
    boolean targetExist = false;
    ScannedRobotEvent closest = null;

    @Override
    public void run() {
        setMaxVelocity(Rules.MAX_VELOCITY);
        setMaxTurnRate(Rules.MAX_TURN_RATE);
        setRadarColor(Color.blue);

        int others = getOthers();

        while(true) {
            if(others != getOthers()){
                others = getOthers();
                enemies.clear();
                targetExist = false;
                closest = null;
            }
            scanEnemies();
        }
    }

    public ScannedRobotEvent getClosestEnemy(){
        for (ScannedRobotEvent e: enemies) {
            if(closest == null){
                closest = e;
                continue;
            }
            if(e.getDistance() < closest.getDistance()){
                closest = e;
            }
        }
        return closest;
    }

    public void scanEnemies(){
        turnRadarLeft(360);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        if(targetExist && event.getName().equals(closest.getName())){
            double absoluteBearing = getHeading() + event.getBearing(); //stolen code from TrackFire
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());  //stolen code TrackFire

            turnRight(bearingFromGun);

            if(event.getDistance() < 90){
                setMaxVelocity(0);
            }
            else if(event.getDistance() < 150){
                setMaxVelocity(event.getVelocity() - 4);
            }
            else{
                setMaxVelocity(Rules.MAX_VELOCITY);
            }

            if(Math.abs(bearingFromGun) <= 1.5){
                if(getGunHeat() == 0){
                    fire(Rules.MAX_BULLET_POWER);
                }
            }
        }

        if(!targetExist){
            for (ScannedRobotEvent e: enemies) {
                String enemyName = e.getName();
                if(enemyName.equals(event.getName())){
                    return;
                }
            }

            if(!event.isSentryRobot()){
                enemies.add(event);
            }

            if(enemies.size() == getOthers()){
                closest = getClosestEnemy();
                targetExist = true;
                setAhead(40000);
            }
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        setAhead(40000);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        setAhead(40000);
    }
}
