package org.meli.service;

import javax.enterprise.context.ApplicationScoped;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

@ApplicationScoped
public class LocationFoundService {
    //Esta funcion usará la libreria lemmingapex implementando Trilateration para usar las formulas matematicas correspondientes y ubicar
    //la posicion triangulada por circunferencias del punto que esta emitiendo los mensajes a los satelites.

    //1)recibe las posiciones de todos los satelites (SatelliteWrapper.getPositions()) = positions

    //2)estas posiciones se cargan en UploadPositions en el CommunicationServiceImpl, luego de recibir la llamada POST 

    //3)y un array de distancias de los satelites (son las que vienen en el post) (SatelliteWrapper.getDistances()) = distances={100,115.5,142.7}

    //Solucion Escalable
    public double[] getLocation(double[][] positions, double [] distances) { 

        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
        NonLinearLeastSquaresSolver nSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());
        double[] points = nSolver.solve().getPoint().toArray();

        for (int i = 0; i < points.length; i++) {
            points[i]= Math.round(points[i]*100d/100d);
        }

        return  points;
    }

    //solucion no escalable
    //ver el archivo readme seccion: "Analisis Matematico" , esto es lo que pasa internamente en la libreria
    public double[] trackPosition(double[][] positions, double[] distances){
        
        double x1,x2,x3,y1,y2,y3,r1,r2,r3,x,y;

        x1 = positions[0][0];
        x2 = positions[1][0];
        x3 = positions[2][0];

        y1 = positions[0][1];
        y2 = positions[1][1];
        y3 = positions[2][1];

        r1 = distances[0];
        r2 = distances[1];
        r3 = distances[2];
        double A,B,C,D,E,F;

        //primera formula
        A= 2*x2 - 2*x1;
        B= 2*y2 - 2*y1;
        C= Math.pow(r1, 2) - Math.pow(r2, 2) - Math.pow(x1, 2) + Math.pow(x2, 2) - Math.pow(y1, 2) + Math.pow(y2, 2);
        
        //segunda formula 
        D= 2*x3 - 2*x2;
        E= 2*y3 - 2*y2;
        F= Math.pow(r2, 2) - Math.pow(r3, 2) - Math.pow(x2, 2) + Math.pow(x3, 2) - Math.pow(y2, 2) + Math.pow(y3, 2);

        //despeje de puntos x y 
        x= (C*E - F*B) / (E*A - B*D);
        y= (C*D - A*F) / (B*D - A*E);

        double[] points = {x,y};
        return points;

    }
}
