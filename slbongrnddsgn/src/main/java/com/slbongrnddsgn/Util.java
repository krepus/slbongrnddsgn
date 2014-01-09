package com.slbongrnddsgn;

/**
 * Created by IntelliJ IDEA.
 * User: j0sua3
 * Date: 11/19/11
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public Util() {
    }

    public static String round2string(double Rval, int Rpl) {
        double p = (double) Math.pow(10, Rpl);
        Rval = Rval * p;
        double tmp = Math.round(Rval);
        Double res = tmp / p;
        return res.toString();
    }

    public static int mod(int x, int y) {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }


}
