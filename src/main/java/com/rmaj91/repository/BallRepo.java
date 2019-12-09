package com.rmaj91.repository;

import com.rmaj91.model.Ball;

import java.util.LinkedList;
import java.util.List;

public class BallRepo {
    List<Ball> balls;

    public BallRepo() {
        balls = new LinkedList<>();
    }

    public void add(Ball ball){
        balls.add(ball);
    }


    public List<Ball> getBalls() {
        return balls;
    }


    public void clear(){
        balls.clear();
    }

    public boolean isEmpty() {
        return balls.isEmpty();
    }
}
