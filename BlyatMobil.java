package prog;

import robocode.Robocode;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

    public class BlyatMobil extends AdvancedRobot {
        static Hashtable enemies = new Hashtable();
        static microEnemy target;
        static Point2D.Double nextDestination;
        static Point2D.Double lastPosition;
        static Point2D.Double myPos;
        static double myEnergy;

        //- run -------------------------------------------------------------------------------------------------------------------------------------
        public void run() {
            setColors(Color.white, Color.red, Color.blue);
            setAdjustGunForRobotTurn(true);
            setAdjustRadarForGunTurn(true);

            setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

            nextDestination = lastPosition = myPos = new Point2D.Double(getX(), getY());
            target = new microEnemy();

            do {

                myPos = new Point2D.Double(getX(), getY());
                myEnergy = getEnergy();

                if (target.live && getTime() > 9) {
                    doMovementAndGun();
                }

                execute();

            } while (true);
        }

        //- stuff -----------------------------------------------------------------------------------------------------------------------------------
        public void doMovementAndGun() {

            double distanceToTarget = myPos.distance(target.pos);

            if (getGunTurnRemaining() == 0 && myEnergy > 1) {
                setFire(Math.min(Math.min(myEnergy / 6d, 1300d / distanceToTarget), target.energy / 3d));
            }

            setTurnGunRightRadians(Utils.normalRelativeAngle(calcAngle(target.pos, myPos) - getGunHeadingRadians()));

            //**** move *****************//
            double distanceToNextDestination = myPos.distance(nextDestination);


            if (distanceToNextDestination < 15) {

                double addLast = 1 - Math.rint(Math.pow(Math.random(), getOthers()));

                Rectangle2D.Double battleField = new Rectangle2D.Double(30, 30, getBattleFieldWidth() - 60, getBattleFieldHeight() - 60);
                Point2D.Double testPoint;
                int i = 0;

                do {

                    testPoint = calcPoint(myPos, Math.min(distanceToTarget * 0.8, 100 + 200 * Math.random()), 2 * Math.PI * Math.random());
                    if (battleField.contains(testPoint) && evaluate(testPoint, addLast) < evaluate(nextDestination, addLast)) {
                        nextDestination = testPoint;
                    }
                } while (i++ < 200);

                lastPosition = myPos;

            } else {


                double angle = calcAngle(nextDestination, myPos) - getHeadingRadians();
                double direction = 1;

                if (Math.cos(angle) < 0) {
                    angle += Math.PI;
                    direction = -1;
                }

                setAhead(distanceToNextDestination * direction);
                setTurnRightRadians(angle = Utils.normalRelativeAngle(angle));
                setMaxVelocity(Math.abs(angle) > 1 ? 0 : 8d);

            }
        }

        //- eval position ---------------------------------------------------------------------------------------------------------------------------
        public static double evaluate(Point2D.Double p, double addLast) {
            double eval = addLast * 0.08 / p.distanceSq(lastPosition);

            Enumeration _enum = enemies.elements();
            while (_enum.hasMoreElements()) {
                microEnemy en = (microEnemy) _enum.nextElement();
                if (en.live) {
                    eval += Math.min(en.energy / myEnergy, 2) *
                            (1 + Math.abs(Math.cos(calcAngle(myPos, p) - calcAngle(en.pos, p)))) / p.distanceSq(en.pos);
                }
            }
            return eval;
        }

        //- scan event ------------------------------------------------------------------------------------------------------------------------------
        public void onScannedRobot(ScannedRobotEvent e) {
            microEnemy en = (microEnemy) enemies.get(e.getName());

            if (en == null) {
                en = new microEnemy();
                enemies.put(e.getName(), en);
            }

            en.energy = e.getEnergy();
            en.live = true;
            en.pos = calcPoint(myPos, e.getDistance(), getHeadingRadians() + e.getBearingRadians());

            if (!target.live || e.getDistance() < myPos.distance(target.pos)) {
                target = en;
            }

            if (getOthers() == 1) setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
        }

        //- minor events ----------------------------------------------------------------------------------------------------------------------------
        public void onRobotDeath(RobotDeathEvent e) {
            ((microEnemy) enemies.get(e.getName())).live = false;
        }

        //- math ------------------------------------------------------------------------------------------------------------------------------------
        private static Point2D.Double calcPoint(Point2D.Double p, double dist, double ang) {
            return new Point2D.Double(p.x + dist * Math.sin(ang), p.y + dist * Math.cos(ang));
        }

        private static double calcAngle(Point2D.Double p2, Point2D.Double p1) {
            return Math.atan2(p2.x - p1.x, p2.y - p1.y);
        }

        //- microEnemy ------------------------------------------------------------------------------------------------------------------------------
        public class microEnemy {
            public Point2D.Double pos;
            public double energy;
            public boolean live;
        }

        }
    }