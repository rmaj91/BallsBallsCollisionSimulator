package com.rmaj91.repository;

import com.rmaj91.model.Ball;

import java.util.HashSet;


public class BallRepo {
    HashSet<Ball> balls;

    public BallRepo() {
        balls = new  HashSet<>();
    }

    public void add(Ball ball){
        balls.add(ball);
    }


    public HashSet<Ball> getBalls() {
        return balls;
    }


    public void clear(){
        balls.clear();
    }

    public boolean isEmpty() {
        return balls.isEmpty();
    }

    public void addAll(BallRepo ballRepo) {
        balls.addAll(ballRepo.getBalls());
    }
}
