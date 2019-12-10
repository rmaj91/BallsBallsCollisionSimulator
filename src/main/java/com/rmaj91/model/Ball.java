package com.rmaj91.model;

import com.rmaj91.App;
import javafx.scene.paint.Color;

public class Ball {

    //=============================================================================================
    // Static Properties
    //=============================================================================================
    private static double E_ABSORBTION = 0.9;
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

    public void moveBall(double milliseconds) {
        getNewCoordinates(milliseconds);
        checkWallsCollisions();
        calculateNewVelocity(milliseconds);
    }


    //=============================================================================================
    // Assessors
    //=============================================================================================


    public void setRadius(double radius) {
        this.radius = radius;
        this.mass = (int) Math.pow(radius, 3);
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

    //=============================================================================================
    // Private Methods
    //=============================================================================================
    private void getNewCoordinates(double milliseconds) {
        this.x += velocity.getX() * milliseconds / 1000;
        this.y += velocity.getY() * milliseconds / 1000;
    }

    private void checkWallsCollisions() {
        if ((x - radius < 0 && velocity.getX() < 0)) {
            velocity.setX(-velocity.getX() * Ball.E_ABSORBTION*Simulation.LEFT_WALL_ABS);
            //x = radius;
        } else if (x + radius > Simulation.getCanvas().getWidth() && velocity.getX() > 0) {
            velocity.setX(-velocity.getX() * Ball.E_ABSORBTION*Simulation.RIGHT_WALL_ABS);
           // x = Simulation.getCanvas().getWidth() - radius;
        }
        if (y - radius < 0 && velocity.getY() < 0) {
            y = radius;
            velocity.setY(-velocity.getY() * Ball.E_ABSORBTION*Simulation.FLOOR_ABS);
        }
    }

    private void calculateNewVelocity(double milliseconds) {
        velocity.setY(velocity.getY() - localEarthAcceleration * milliseconds / 1000);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector getVelocity() {
        return velocity;
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
}
