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
    // Getters/Setters
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
                double dx = ball1.getX() - ball2.getX();
                double dy = ball1.getY() - ball2.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < ball1.radius + ball2.radius) {

                    double overlap = -0.5 * (distance - ball1.radius - ball2.radius);

//                    Vector vector = ball1.getVelocity();
//                    ball1.setVelocity(ball2.getVelocity());
//                    ball2.setVelocity(velocity);

                }
            }
    }


    private void getNewBallState(double milliseconds) {
        this.x += velocity.getX() * milliseconds / 1000;
        this.y += velocity.getY() * milliseconds / 1000;
    }

    private void checkWallsCollisions() {
        if (x < radius) {
            leftWallContactHandler();
        } else if (x > Simulation.getCanvas().getWidth() - radius) {
            rightWallContactHandler();
        }
//        if(y == radius)
//            // semi rolling friction
//            velocity.setX(velocity.getX()*0.999);
        if (y < radius) {
            floorContactHandler();
        }


    }

    private void floorContactHandler() {
        double newKineticEnY = Simulation.FLOOR_ABS * Ball.E_ABSORPTION * velocity.getY() * velocity.getY() * mass / 2;
        double newVelocityY = Math.sqrt(newKineticEnY * 2 / mass);
        velocity.setY(newVelocityY);
//        if(velocity.getY()<50)
//            velocity.setY(0);
        y = radius;
        velocity.setX(velocity.getX()*Simulation.SEMI_FRICTION);
    }

    private void rightWallContactHandler() {
        double newKineticEnX = Simulation.RIGHT_WALL_ABS * Ball.E_ABSORPTION * velocity.getX() * velocity.getX() * mass / 2;
        double newVelocityX = -Math.sqrt(newKineticEnX * 2 / mass);
        velocity.setX(newVelocityX);
        x = Simulation.getCanvas().getWidth() - radius;
    }

    private void leftWallContactHandler() {
        double newKineticEnX = Simulation.LEFT_WALL_ABS * Ball.E_ABSORPTION * velocity.getX() * velocity.getX() * mass / 2;
        double newVelocityX = Math.sqrt(newKineticEnX * 2 / mass);
        velocity.setX(newVelocityX);
        x = radius;
    }

    private void calculateNewVelocity(double milliseconds) {
        if (y == radius && velocity.getY() > -50 && velocity.getY() < 50)
            velocity.setY(0);
        else
            velocity.setY(velocity.getY() - globalEarthAcceleration * milliseconds / 1000);
    }

}
