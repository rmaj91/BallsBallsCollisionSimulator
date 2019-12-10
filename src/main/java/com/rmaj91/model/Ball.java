package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.scene.paint.Color;

public class Ball {

    //=============================================================================================
    // Static Properties
    //=============================================================================================
    private static double E_ABSORPTION = 0.8;
    public static double globalEarthAcceleration = 500;
    //=============================================================================================
    // Properties
    //=============================================================================================
    private double x;
    private double y;
    private double radius;
    private int mass;
    private Vector velocity;
    private Color color;
    private double localEarthAcceleration = 100;
    private Ball recentlyInContact=this;


    //=============================================================================================
    // Constructors
    //=============================================================================================
    public Ball(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.mass = (int) Math.pow(radius, 3);
        this.velocity = new Vector(0, 0);
        this.localEarthAcceleration = globalEarthAcceleration;
    }

    //=============================================================================================
    // Public Methods
    //=============================================================================================

    public void moveBall(double milliseconds, BallRepo ballRepo) {
        getNewBallState(milliseconds);
        calculateNewVelocity(milliseconds);
        checkWallsCollisions();
        checkBallsCollision(ballRepo);
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.mass = (int) Math.pow(radius, 3);
    }



    //=============================================================================================
    // Assessors
    //=============================================================================================

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static void setGlobalEarthAcceleration(double globalEarthAcceleration) {
        Ball.globalEarthAcceleration = globalEarthAcceleration;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", mass=" + mass +
                ", velocity=" + velocity +
                ", color=" + color +
                '}';
    }


    //=============================================================================================
    // Private Methods
    //=============================================================================================

    // todo beta version of collisions
    private void checkBallsCollision(BallRepo ballRepo) {
        for (int i = 0; i < ballRepo.size(); i++)
            for (int j = i + 1; j < ballRepo.size(); j++) {
                Ball ball1 = ballRepo.get(i);
                Ball ball2 = ballRepo.get(j);
                double x1 = ball1.getX();
                double y1 = ball1.getY();
                double x2 = ball2.getX();
                double y2 = ball2.getY();
                double radius1 = ball1.getRadius();
                double radius2 = ball2.getRadius();
                double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

                if (distance < radius1+radius2 && ! (ball1.recentlyInContact==ball2.recentlyInContact)) {
                    // try to avoid multicontacting between the same balls
                    ball1.recentlyInContact=ball2;
                    ball2.recentlyInContact=ball1;

                    double v2x = Math.sqrt(ball1.mass*Math.pow(ball1.getVelocity().getX(),2)/ball2.mass);
                    double v2y = Math.sqrt(ball1.mass*Math.pow(ball1.getVelocity().getY(),2)/ball2.mass);

                    double v1x = Math.sqrt(ball2.mass*Math.pow(ball2.getVelocity().getX(),2)/ball1.mass);
                    double v1y = Math.sqrt(ball2.mass*Math.pow(ball2.getVelocity().getY(),2)/ball1.mass);
                    //todo beta exchanging velocities not energies
                    Vector velocity = ball1.getVelocity();
                    ball1.setVelocity(ball2.getVelocity());
                    ball2.setVelocity(velocity);
                    //System.out.println("distance: "+distance +"R1: "+radius1+"R2: "+radius2);
                }
            }
    }


    private void getNewBallState(double milliseconds) {
        this.x += velocity.getX() * milliseconds / 1000;
        this.y += velocity.getY() * milliseconds / 1000;
    }

    private void checkWallsCollisions() {
        if (x < radius - 1) {
            leftWallContactHandler();
        } else if (x > Simulation.getCanvas().getWidth() - radius + 1) {
            rightWallContactHandler();
        }
        if (y < radius - 1) {
            floorContactHandler();
        }

    }

    private void floorContactHandler() {
        double newKineticEnY = Simulation.FLOOR_ABS * Ball.E_ABSORPTION * velocity.getY() * velocity.getY() * mass / 2;
        double newVelocityY = Math.sqrt(newKineticEnY * 2 / mass);
        velocity.setY(newVelocityY);
        y = radius;
        recentlyInContact = this;
        System.out.println("floor collision");
    }

    private void rightWallContactHandler() {
        double newKineticEnX = Simulation.RIGHT_WALL_ABS * Ball.E_ABSORPTION * velocity.getX() * velocity.getX() * mass / 2;
        double newVelocityX = -Math.sqrt(newKineticEnX * 2 / mass);
        velocity.setX(newVelocityX);
        x = Simulation.getCanvas().getWidth() - radius;
        recentlyInContact = this;
        System.out.println("right wall collision!");
    }

    private void leftWallContactHandler() {
        double newKineticEnX = Simulation.LEFT_WALL_ABS * Ball.E_ABSORPTION * velocity.getX() * velocity.getX() * mass / 2;
        double newVelocityX = Math.sqrt(newKineticEnX * 2 / mass);
        velocity.setX(newVelocityX);
        x = radius;
        recentlyInContact = this;
        System.out.println("left wall collision!");
    }

    private void calculateNewVelocity(double milliseconds) {
        if(y == radius && velocity.getY() > -50 && velocity.getY() < 50)
            velocity.setY(0);
        else
            velocity.setY(velocity.getY() - localEarthAcceleration * milliseconds / 1000);
    }

}
