package com.slbongrnddsgn;

/**
 * Created with IntelliJ IDEA.
 * User: j0sua3
 * Date: 17/04/13
 * Time: 11:14 PM
 * To change this template use File | Settings | File Templates.
 */


import static java.lang.Math.*;


public class MyDouble {


    private Double mValue;
    private Unit mUnit;
    private static final double tolerance = 1e-6;


    public MyDouble() {
        mValue = 0.d;
        mUnit = Unit.mm;
    }

    public MyDouble(double value, Unit unit) {
        mValue = value; //set teh value according to users chosen unit
        mUnit = unit; //set unit string according to chosen unit
    }


    /**
     * return the double value of the value field converted to base unit
     * length units --> mm
     * Force units --> N
     * Moment units --> N*mm
     * Stress untis --> MPa
     * subgrade modulus --> N/mm^3
     */
    public double v() {

        switch (mUnit) {
            case in:
                return mValue * 25.4d;
            case m:
                return mValue * 1000.d;
            case ft:
                return mValue * 304.8d;
            case kN:
                return mValue * 1000.d;

            case lbf:
                return mValue * 4.4482216152605d;
            case kip:
                return mValue * 4448.2216152605d;
            case kipft:
                return mValue * 1355817.9483314003d;
            case kNm:
                return mValue * 1000.d * 1000.d;
            case ksi:
                return mValue * 6.894757293168361d;
            //area
            case in2:
                return mValue * 25.4d * 25.4d;

            //modulus
            case psi_per_in:
                return mValue * 0.00027144713752631d;

            default:
                return mValue;
        }

    }

    /**
     * @return a converted double value specified by Unit unit
     */
    public MyDouble toUnit(Unit unit) {

        if (mUnit != unit) {
            switch (unit) {

                case m:
                    return new MyDouble(v() / 1e3, Unit.m);
                case kN:
                    //source kip
                    return new MyDouble(v() / 1e3, Unit.kN);
                case kNm:
                    return new MyDouble(v() * 1e6, Unit.kNm);
                case in:
                    return new MyDouble(v() / 25.4d, Unit.in);
                case ft:
                    return new MyDouble(v() / 304.8d, Unit.ft);
                case lbf:
                    return new MyDouble(v() * 0.2248089430997105d, Unit.ft);
                case kip:
                    return new MyDouble(v() * 0.00022480894309971d, Unit.kip);
                case kipft:
                    return new MyDouble(v() * 0.00000073756214928d, Unit.kipft);
                case ksi:
                    return new MyDouble(v() * 0.14503773773020923d, Unit.ksi);
                case psi:
                    return new MyDouble(v() * 145.03773773020922d, Unit.psi);

                //area
                case in2:
                    return new MyDouble(v() / 25.4d / 25.4d, Unit.in2);

                //modulus
                case psi_per_in:
                    return new MyDouble(v() / 3683.958538347314d, Unit.MPa_per_mm);

                default:
                    return new MyDouble(mValue, mUnit);
            }
        } else {
            return new MyDouble(mValue, mUnit);
        }
    }


    public double dblValueInUnit(Unit unit) {

        if (mUnit != unit) {
            switch (unit) {
                case mm:
                    //source unit is in

                    return v();
                case m:
                    return v() / 1000.d;
                case kN:
                    return v() / 1000.d;
                case kNm:
                    return v() / 1e6;
                case in:
                    return v() / 25.4d;
                case ft:
                    return v() / (12.d * 25.4d);
                case lbf:
                    return v() / 4448.d;
                case kip:
                    return v() / 4448.d;
                case kipft:
                    //source kN-m
                    return v() / (4448.d * 12.d * 25.4d);
                case ksi:
                    //source MPa
                    return v() * pow(25.4d, 2) / 4448.d;
                case psi:
                    //source MPa
                    return v() * pow(25.4d, 2) / 4.448d;

                //area
                case in2:
                    return v() / pow(25.4d, 2);

                //modulus
                case psi_per_in:
                    return v() / 3683.958538347314d;

                default:
                    return v();
            }
        } else {
            return v();
        }
    }


    public boolean isEqual(MyDouble myDouble) {

        if (Math.abs(myDouble.v() - v()) < tolerance) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * set the value and units per users chosen unit
     */

    public void sv(double value, Unit unit) {
        mValue = value; //set the value according to users chosen unit
        mUnit = unit; //set unit string according to chosen unit
    }


    public String toString() {

        return Util.round2string(mValue, 2) + String.valueOf(mUnit);
    }

    public static enum Unit {
        mm, mm2, in, in2, m, ft, N, kN,
        lbf, kip, kipft, kNm, MPa, MPa_per_mm, ksi, psi, psi_per_in
    }
}
