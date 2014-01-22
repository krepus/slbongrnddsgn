package com.slbongrnddsgn;

import static java.lang.Math.PI;
import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static com.slbongrnddsgn.MyDouble.Unit.*;
import static com.slbongrnddsgn.MathLib.Linterp;

/**
 * Created by josua.arnigo on 7/01/14.
 */
public class SlabOnGround {

    MyDouble mcover; //concrete cover
    Double mGammaC, mGammaS; //partial safety factors for conc and steel
    MyDouble mHf; //slab thickness
    MyDouble mKs; //subgrade modulus
    MyDouble mPu; //Ultimate load
    MyDouble mR;  //equivalent radius
    MyDouble mSload; //wheel load spacing
    Integer mReoLoc; //
    MyDouble mBarDia, mScc; //bar dia and spacing
    MyDouble mFcm; //mean cylinder compressive strength
    MyDouble mFck, mFyk;
    MyDouble mFctm, mFctk005; //axial tensile,


    //derived qttys
    MyDouble phiMn_hogging; //hogging moment capacity
    MyDouble phiMn_sagging;
    MyDouble mLr; //radius of relative stiffness
    MyDouble mFctk_flex;
    MyDouble mEcm;  //modulus of elasticity of concrete
    Double mPhi, mPoisson;  //strenght factor

    //point load capacities
    MyDouble phiPn_Interior_single, phiPn_Interior_dual, phiPn_edge_single, getPhiPn_edge_dual;


    public SlabOnGround(
            MyDouble tcover,
            MyDouble tHf,
            MyDouble tKs,
            MyDouble tPu,
            MyDouble tR,
            MyDouble tSload,
            Integer tReoLoc,
            MyDouble tBarDia,
            MyDouble tScc,
            MyDouble tFc,
            MyDouble tFy,
            double tgamma_c, //partial safety factor, conc
            double tgamma_s
    ) {

        mcover = new MyDouble(75.d, mm);
        mHf = tHf;
        mKs = tKs;
        mPu = tPu;
        mR = tR;
        mSload = tSload;
        mReoLoc = tReoLoc;
        mBarDia = tBarDia;
        mScc = tScc;
        mFck = tFc;
        mFyk = tFy;
        mGammaC = tgamma_c;
        mGammaS = tgamma_s;

        mPhi = 0.6d; //strength reduction factor
        mPoisson = 0.2d; //Poisons ratio


        //secant modulus of elasticity
        mFcm = new MyDouble(mFck.v() + 8.d, MPa); //mean cylinder compressive strength
        mEcm = new MyDouble(22000.d * pow(mFcm.v() / 10.d, 0.3d), MPa);

        //mean axial tensile strength of plain concrete, tr34
        mFctm = new MyDouble(0.3d * pow(mFck.v(), 2.d / 3.d), MPa);
        //5% fractile, charac axial tensile strength
        mFctk005 = new MyDouble(0.7d * mFctm.v(), MPa);


        //concrete flexural strength
        double minFctkflex = min(2.d * mFctk005.v(), mFctk005.v() * (1.d + sqrt(200.d / mHf.v())));
        mFctk_flex = new MyDouble(minFctkflex, MPa);

        //radius of relative stiffness
        double Lr = pow(mEcm.v() * pow(mHf.v(), 3.d) / (12.d * (1.d - mPoisson) * mKs.v()), 0.25d);
        mLr = new MyDouble(Lr, MPa_per_mm);

        //hogging moment cap
        double gamma_c = 1.d / 0.6;
        double dphiMn = (mFctk_flex.v() - 1.5d) / mGammaC * pow(mHf.v(), 2.d) / 6.d;
        phiMn_hogging = new MyDouble(dphiMn, N); //N-mm/mm

        /* sagging moment capacity, if bottom reo is provided
        0 - nominal temp bars only
        1 - top and bottom
         */
        phiMn_sagging = fphiMp(mFck, mFyk, mBarDia, mHf, mScc, mcover);

        //flag abort design if slab is over reinforced at the bottom
        boolean overreinforced = 0.d > phiMn_sagging.v();

        if (tReoLoc > 0 || (!overreinforced)) { //top and bottom
            {

            }
        } else { //top only


        }


    }

    /**
     * @param fc,   concrete
     * @param fy,   steel reo yield
     * @param db,   bar size
     * @param hf,   slab thickness
     * @param b,    bar spacing
     * @param cover
     * @return moment capacity per unit length or -1.0 for design error
     */

    private MyDouble fphiMp(
            MyDouble fc,
            MyDouble fy,
            MyDouble db,
            MyDouble hf,
            MyDouble b, //spacing of bars
            MyDouble cover

    ) {


        //ultimate design stress of concrete
        double eta = 1.d; //fck<=50MPa
        double alfa_cc = 0.85d;
        double gamma_c = 1.5d; // concrete  safety factor
        double fcd = eta * alfa_cc * fc.v() / gamma_c;

        //limiting strains,
        double kuo = 0.45d; //assume maximum limit for EC2>> x = 0.45d, no redistribution
        double ec = 0.0035d;
        double et = (ec - ec * kuo) / kuo; // based on Kuo = 0.45
        double Es = 200000.d;
        double gamma_s = 1.15d; //safety factor for steel

        //tensile stress of rebars, assume all bars at d distance
        double fs = min(fy.v(), et * Es);

        //get depth of compression
        double d = hf.v() - cover.v() - 1.5d * db.v();
        double lamda = 0.8d; //for fck<=50MPa
        double c = kuo * d;
        double a = lamda * c;

        //get moment caapacity given reo
        double As = PI / 4.d * pow(db.v(), 2.d);
        double es = ec / c * (d - c);
        double Fsd = min(fy.v(), es * Es) * As / gamma_s; //factored steel forces
        double phiMn = Fsd * (d - 0.5d * a) / b.v(); //moemtn per unit length

        //check depth of compression block
        double a_final = Fsd / (fcd * b.v());

        //verify design reult if valid
        if (a > a_final) {
            return new MyDouble(phiMn / 1000.d, kN);
        } else {
            return new MyDouble(-1, kN);

        }

    }


    MyDouble getPhiPn_Interior_single(MyDouble Mn,
                                      MyDouble Mp,
                                      MyDouble Lr,
                                      MyDouble a) {

        double P00 = 2.d * PI * (Mn.v() + Mp.v());
        double P02 = 4.d * PI * (Mn.v() + Mp.v()) / (1.d + a.v() / (3.d * Lr.v()));

        double[] vx = {0.d, 0.2d};
        double[] vy = {P00, P02};

        if (a.v() / Lr.v() > 0.2d) {
            return new MyDouble(P02, N);
        } else {
            return new MyDouble(Linterp(vx, vy, a.v() / Lr.v()), N);

        }


    }

    MyDouble getPhiPn_Interior_dual(MyDouble Mn,
                                    MyDouble Mp,
                                    MyDouble Lr,
                                    MyDouble a,
                                    MyDouble x) {

        double P00 = (2.d * PI + 1.8d * x.v() / Lr.v()) * (Mn.v() + Mp.v());
        double P02 = (Mn.v() + Mp.v()) * (4.d * PI / (1.d - a.v() / (3.d * Lr.v())) + 1.8d * x.v()
                / (Lr.v() - a.v() / 2.d));

        double[] vx = {0.d, 0.2d};
        double[] vy = {P00, P02};

        if (a.v() / Lr.v() > 0.2d) {
            return new MyDouble(P02, N);
        } else {
            return new MyDouble(Linterp(vx, vy, a.v() / Lr.v()), N);

        }

    }


    MyDouble getPhiPn_edge_single(MyDouble Mn,
                                  MyDouble Mp,
                                  MyDouble Lr,
                                  MyDouble a,
                                  MyDouble x) {

        double P00 = (Mn.v() + Mp.v()) * PI / 2.d + 2.d * Mn.v();
        double P02 = ((Mn.v() + Mp.v()) * PI + 4.d * Mn.v()) / (1.d + 2.d * a.v() / (3.d * Lr.v()));

        double[] vx = {0.d, 0.2d};
        double[] vy = {P00, P02};

        if (a.v() / Lr.v() > 0.2d) {
            return new MyDouble(P02, N);
        } else {
            return new MyDouble(Linterp(vx, vy, a.v() / Lr.v()), N);

        }

    }


    MyDouble getPhiPn_edge_dual(MyDouble Lr,
                                MyDouble a,
                                MyDouble x) {

        double dMn = (mFctk_flex.v() - 1.5d) / mGammaC * pow(mHf.v(), 2.d) / 6.d;
        MyDouble phiMn = new MyDouble(dMn, N);

        MyDouble phiMp = fphiMp(mFck, mFyk, mBarDia, mHf, mScc, mcover);


        MyDouble phiPn_wheel = getPhiPn_Interior_single(phiMn, phiMp, Lr, a);
        MyDouble phiPn_axle = getPhiPn_Interior_dual(phiMn, phiMp, Lr, a, x);

        double f_dual = phiPn_axle.v() / phiPn_wheel.v();

        MyDouble phiPn_edge_single = getPhiPn_edge_single(phiMn, phiMp, Lr, a, x);

        return new MyDouble(f_dual * phiPn_edge_single.v(), N);
        //dfsldnf


    }

}
