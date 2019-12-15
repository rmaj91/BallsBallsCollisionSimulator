package com.rmaj91.model;

import com.rmaj91.repository.BallRepo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    //=============================================================================================
    // Static Properties
    //=============================================================================================

    public static final double LEFT_WALL_ABS = 0.9;
    public static final double RIGHT_WALL_ABS = 0.9;
    public static final double FLOOR_ABS = 0.8;
    public static final double SEMI_FRICTION = 0.999;
    public static final double FPS = 100;

    //=============================================================================================
    // Properties/Dependencies
    //=============================================================================================
    private BallRepo ballRepo;
    private boolean simulationFlag;
    public static Canvas canvas;
    private GraphicsContext graphicsContext;
    private Timeline timelineDraw;
    private List<Pair<Ball, Ball>> collisionPairs;
    private int step;


    public Simulation(BallRepo ballRepo, Canvas canvas) {
        simulationFlag = false;
        this.ballRepo = ballRepo;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        collisionPairs = new ArrayList<>();
        timelineDraw = new Timeline(new KeyFrame(Duration.millis(1000 / FPS), e -> {
            for (Ball ball : this.ballRepo.getBalls())
                ball.getNewBallState(1000 / FPS, this.ballRepo);
            checkCollisions();
            handleElasticCollisions(collisionPairs);
            handleWallsCollisions();
            clearView();
            drawAll();
        }));
        timelineDraw.setCycleCount(Timeline.INDEFINITE);
    }

    // temporary, ball looses velocity not energy
    private void handleWallsCollisions() {
        for (Ball ball : ballRepo.getBalls()) {
            if (ball.getPy() < ball.getRadius()) {
                ball.setVy(-ball.getVy() * FLOOR_ABS);
                ball.setPy(ball.getRadius());
            }
            if (ball.getPx() < ball.getRadius()) {
                ball.setVx(-ball.getVx() * LEFT_WALL_ABS);
                ball.setPx(ball.getRadius());
            }
            if (ball.getPx() > canvas.getWidth() - ball.getRadius()) {
                ball.setVx(-ball.getVx() * RIGHT_WALL_ABS);
                ball.setPx(canvas.getWidth() - ball.getRadius());
            }
        }
    }

    private void checkCollisions() {
        for (Ball ball1 : ballRepo.getBalls())
            for (Ball ball2 : ballRepo.getBalls())
                if (ball1 != ball2) {
                    double distanceBetweenBallsPow2 = (ball1.getPx() - ball2.getPx()) * (ball1.getPx() - ball2.getPx())
                            + (ball1.getPy() - ball2.getPy()) * (ball1.getPy() - ball2.getPy());
                    double radiusSumPow2 = (ball1.getRadius() + ball2.getRadius()) * (ball1.getRadius() + ball2.getRadius());
                    if (distanceBetweenBallsPow2 < radiusSumPow2) {
                        handleStaticCollisions(ball1, ball2);
                    }
                }
    }

    private void handleElasticCollisions(List<Pair<Ball, Ball>> collisionPairs) {
        for (Pair<Ball, Ball> pair : collisionPairs) {

            Ball ball1 = pair.getKey();
            Ball ball2 = pair.getValue();
            double distance = Math.sqrt((ball1.getPx() - ball2.getPx()) * (ball1.getPx() - ball2.getPx())
                    + (ball1.getPy() - ball2.getPy()) * (ball1.getPy() - ball2.getPy()));
            // normal
            double nX = (ball2.getPx() - ball1.getPx()) / distance;
            double nY = (ball2.getPy() - ball1.getPy()) / distance;
            // tangent
            double tx = -nY;
            double ty = nX;
            // dot product tangent
            double dpTan1 = ball1.getVx() * tx + ball1.getVy() * ty;
            double dpTan2 = ball2.getVx() * tx + ball2.getVy() * ty;
            // dot product normal
            double dpNorm1 = ball1.getVx() * nX + ball1.getVy() * nY;
            double dpNorm2 = ball2.getVx() * nX + ball2.getVy() * nY;
            // conservation of momentum in 1D
            int massBall1 = ball1.getMass();
            int massBall2 = ball2.getMass();
            double m1 = (dpNorm1 * (massBall1 - massBall2) + 2 * massBall2 * dpNorm2)
                    / (massBall1 + massBall2);
            double m2 = (dpNorm2 * (massBall2 - massBall1) + 2 * massBall1 * dpNorm1)
                    / (massBall1 + massBall2);

            ball1.setVx(tx * dpTan1 + nX * m1);
            ball1.setVy(ty * dpTan1 + nY * m1);
            ball2.setVx(tx * dpTan2 + nX * m2);
            ball2.setVy(ty * dpTan2 + nY * m2);
        }
        collisionPairs.clear();
    }

    private void handleStaticCollisions(Ball ball1, Ball ball2) {
        collisionPairs.add(new Pair<>(ball1, ball2));

        double distance = Math.sqrt((ball1.getPx() - ball2.getPx()) * (ball1.getPx() - ball2.getPx())
                + (ball1.getPy() - ball2.getPy()) * (ball1.getPy() - ball2.getPy()));
        double overlap = 0.5 * (distance - ball1.getRadius() - ball2.getRadius());
        //  ball1
        ball1.setPx(ball1.getPx() - overlap * (ball1.getPx() - ball2.getPx()) / distance);
        ball1.setPy(ball1.getPy() - overlap * (ball1.getPy() - ball2.getPy()) / distance);
        //  ball2
        ball2.setPx(ball2.getPx() + overlap * (ball1.getPx() - ball2.getPx()) / distance);
        ball2.setPy(ball2.getPy() + overlap * (ball1.getPy() - ball2.getPy()) / distance);
    }

    public void start() {
        simulationFlag = true;
        clearView();
        drawAll();
        timelineDraw.play();
    }

    public void stop() {
        timelineDraw.stop();
        simulationFlag = false;
        graphicsContext.setLineWidth(2);
        Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 48);
        graphicsContext.setFont(theFont);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeText("Simulation Paused!", canvas.getWidth() / 2, canvas.getHeight() / 2);
    }

    public void drawAll() {
        for (Ball ball : ballRepo.getBalls())
            draw(ball);
    }

    public void draw(Ball ball) {
        graphicsContext.setFill(ball.getColor());
        double radius = ball.getRadius();
        double yCavasCoordinate = -(ball.getPy() - canvas.getHeight());
        graphicsContext.fillOval(ball.getPx() - radius, yCavasCoordinate - radius, radius * 2, radius * 2);
    }

    public void clearView() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void clearRepo() {
        ballRepo = new BallRepo();
    }

    public boolean isSimulationFlag() {
        return simulationFlag;
    }

    public void add(Ball ball) {
        ballRepo.add(ball);
    }

    public void addAll(BallRepo ballRepo) {
        for (Ball ball : ballRepo.getBalls()) {
            this.ballRepo.add(ball);
        }
    }

    public BallRepo getBallRepo() {
        return ballRepo;
    }

    public static Canvas getCanvas() {
        return canvas;
    }
}
