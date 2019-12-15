package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.scene.paint.Color;

public class Ball {

    //=============================================================================================
    // Static Properties
    //=============================================================================================
//    private static final double ENERGY_ABSORPTION = 1.0;
    public static int EARTH_ACC = 0;
    //=============================================================================================
    // Properties
    //=============================================================================================
    private double px;
    private double py;
    private double vx;
    private double vy;
    private double ax;
    private double ay;
    private double radius;
    private int mass;
    private Color color;

    //=============================================================================================
    // Constructors
    //=============================================================================================
    public Ball(double px, double py, double radius, Color color) {
        this.px = px;
        this.py = py;
        this.radius = radius;
        this.color = color;
        this.mass = (int) Math.pow(radius, 3);
    }

    //=============================================================================================
    // Public Methods
    //=============================================================================================

    public void getNewBallState(double milliseconds, BallRepo ballRepo) {
        getNewBallCoordinates(milliseconds);
        getNewVelocities(milliseconds);
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.mass = (int) Math.pow(radius, 3);
    }


    //=============================================================================================
    // Getters/Setters
    //=============================================================================================

    public double getPx() {
        return px;
    }

    public double getPy() {
        return py;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public double getRadius() {
        return radius;
    }

    public int getMass() {
        return mass;
    }

    public Color getColor() {
        return color;
    }

    public void setPx(double px) {
        this.px = px;
    }

    public void setPy(double py) {
        this.py = py;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }
    //=============================================================================================
    // Private Methods
    //=============================================================================================


    private void getNewBallCoordinates(double milliseconds) {
        px += vx * milliseconds / 1000;
        py += vy * milliseconds / 1000;
    }

    private void getNewVelocities(double milliseconds) {
        vx += ax * milliseconds / 1000;
        vy += (ay - EARTH_ACC) * milliseconds / 1000;
        //todo 2 its just test value, can change later
        if (vx * vx + vy * vy < 5 && py == 0) {
            vx = 0;
            vy = 0;
        }
    }

}
