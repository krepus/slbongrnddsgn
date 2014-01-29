package com.slbongrnddsgn;

/**
 * Created by josua.arnigo on 9/01/14.
 */
public class MathLib {
    public MathLib() {
    }


    public static double Linterp(
            double[] vx,
            double[] vy,
            double x
    ) {
        return x * (vy[1] - vy[0]) / (vx[1] - vx[0]) + vy[0];


    }
}
