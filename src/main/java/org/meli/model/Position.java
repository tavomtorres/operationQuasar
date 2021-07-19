package org.meli.model;

public class Position {

    //puntos x y de la posicion
    private double x;
    private double y;

    public Position(){
        
    }
    public Position(double[] points){
        this.x = points[0];
        this.y = points[1];
    }

    public double getY(){
        return y;
    }

    public double getX(){
        return x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setX(double x){
        this.x = x;
    }

    @Override
    public String toString(){
        return  x+","+ y;
    }
    
}
