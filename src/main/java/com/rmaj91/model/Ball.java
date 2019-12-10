package com.rmaj91.model;

import com.rmaj91.App;
import javafx.scene.paint.Color;

public class Ball {

    //=============================================================================================
    // Static Properties
    //=============================================================================================
    private static double E_ABSORBTION_X = 0.85;
    private static double E_ABSORBTION_Y = 0.85;
    private static double globalEarthAcceleration = 100;
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
        double velocityX = velocity.getX();
        double velocityY = velocity.getY();

        getNewCoordinates(milliseconds, velocityX, velocityY);
        checkWallsCollisions(velocityX, velocityY);
        calculateNewVelocity(milliseconds, velocityY);
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
    private void getNewCoordinates(double milliseconds, double velocityX, double velocityY) {
        this.x += velocityX * milliseconds / 1000;
        this.y += velocityY * milliseconds / 1000;
    }

    private void checkWallsCollisions(double velocityX, double velocityY) {
        if (x < 0 || x > App.C_WIDTH)
            velocity.setX(-velocityX * E_ABSORBTION_X);
        if (y < 0)
            velocity.setY(-velocityY * E_ABSORBTION_Y);
    }

    private void calculateNewVelocity(double milliseconds, double velocityY) {
        velocity.setY(velocityY - localEarthAcceleration * milliseconds / 1000);
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
