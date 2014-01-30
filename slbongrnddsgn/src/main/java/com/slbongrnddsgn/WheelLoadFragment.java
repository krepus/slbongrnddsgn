package com.slbongrnddsgn;

/**
 * Created with IntelliJ IDEA.
 * User: j0sua3
 * Date: 30/06/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.layout.simple_dropdown_item_1line;
import static android.R.layout.simple_spinner_dropdown_item;
import static android.R.layout.simple_spinner_item;
import static com.slbongrnddsgn.MyDouble.Unit.*;

public class WheelLoadFragment extends Fragment implements View.OnClickListener {

    public View view;
    private String[] strarrBarlist;
    Bundle mSavedInstanceState = null;

    //global input var
    MyDouble[] mBarlist;
    MyDouble mHf, mKs, mPu, ma, mX, mScc, mDB;
    int mReoLoc;
    String mUnit, mReport;

    //global var for pref variables
    MyDouble mcover;
    MyDouble mFc;
    MyDouble mFy;
    double mgamma_c; //partial safety factor, conc
    double mgamma_s;

    SlabOnGround sog;
    // get a label for our log entries
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Log.i(TAG, "onAttach");
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            // Restore state here
        }
        // Log.i(TAG, "onCreate");

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Log.i(TAG, "onActivityCreated");
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //mSavedInstanceState = savedInstanceState;
        view = inflater.inflate(R.layout.wheel_load_main, container, false);

        Button b = (Button) view.findViewById(R.id.button_des);
        b.setOnClickListener(this);

        //return v;
        // Log.i(TAG, "onCreateView");
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        saveDesignInput();
        //Log.i(TAG, "onPause");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mTabHost = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Log.i(TAG, "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
        //updateSpinner();


        // Log.i(TAG, "onResume");


    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.i(TAG, "onStart");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_des:


                TextView reportstr = (TextView) view.findViewById(R.id.report);
                reportstr.setText(getDesignReport());

                break;
        }
    }

    private void saveDesignInput() {


        //save data in sharedpref
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor ed = sp.edit();

        Spinner reolocspinner = (Spinner) view.findViewById(R.id.reo_loc_spinner);
        Spinner DBspinner = (Spinner) view.findViewById(R.id.DB_spinner);

        ed.putString(getString(R.string.MIN_TENSION_BAR_DIA_ITEM_SELECTION),
                String.valueOf(DBspinner.getSelectedItemPosition()));

        ed.putString(getString(R.string.REOLOC_ITEM_SELECTION),
                String.valueOf(reolocspinner.getSelectedItemPosition()));

        TextView v = (TextView) view.findViewById(R.id.hf_in);
        ed.putString(getString(R.string.SLAB_THICKNESS), v.getText().toString());

        v = (TextView) view.findViewById(R.id.ks_in);
        ed.putString(getString(R.string.SUBGRADE), v.getText().toString());

       /* v = (TextView) view.findViewById(R.id.Pu_in);
        ed.putString(getString(R.string.POINT_LOAD), v.getText().toString());*/


        v = (TextView) view.findViewById(R.id.radius_in);
        ed.putString(getString(R.string.EQUIV_RADIUS), v.getText().toString());

        v = (TextView) view.findViewById(R.id.wheel_spacing_in);
        ed.putString(getString(R.string.WHEEL_SPACING), v.getText().toString());

        v = (TextView) view.findViewById(R.id.DB_spacing_in);
        ed.putString(getString(R.string.BAR_SPACING), v.getText().toString());


        ed.commit();


        //pass the data to activity
        //saveDesigInputData.SaveDesigInput(bundle);
        //then return the bundled data
        //return bundle;
    }

    private void updateUI() {
        /**
         * populate spinner_minDB
         */
        //read preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //update unit of prompt string
        String SIUnit = getString(R.string.SI);
        String unit = sharedPref.getString(getString(R.string.UNIT), SIUnit);
        String DIA = "\u00D8";


        if (unit.equals(SIUnit)) {
            TextView v = (TextView) view.findViewById(R.id.hf_textview_ID);
            v.setText(getString(R.string.hf_str) + ", " + mm.toString());

            v = (TextView) view.findViewById(R.id.ks_textview_ID);
            v.setText(getString(R.string.ks_str) + ", " + kPa_per_mm);

            /*v = (TextView) view.findViewById(R.id.Pu_textview_ID);
            v.setText(getString(R.string.Pu_str) + ", " + kN.toString());
*/

            v = (TextView) view.findViewById(R.id.radius_textview_ID);
            v.setText(getString(R.string.radius_str) + ", " + mm.toString());

            v = (TextView) view.findViewById(R.id.wheel_spacing_textview_ID);
            v.setText(getString(R.string.wheel_spacing_str) + ", " + mm.toString());

            v = (TextView) view.findViewById(R.id.DB_textview_ID);
            v.setText(getString(R.string.DB_str) + ", " + DIA + "(" + mm.toString() + ")");

            v = (TextView) view.findViewById(R.id.DB_spacing_textview_ID);
            v.setText(getString(R.string.DB_spacing_str) + ", " + mm.toString());

        } else {
            TextView v = (TextView) view.findViewById(R.id.hf_textview_ID);
            v.setText(getString(R.string.hf_str) + ", " + in.toString());

            v = (TextView) view.findViewById(R.id.ks_textview_ID);
            v.setText(getString(R.string.ks_str) + ", " + ksf_per_in);

            /*v = (TextView) view.findViewById(R.id.Pu_textview_ID);
            v.setText(getString(R.string.Pu_str) + ", " + kip.toString());*/


            v = (TextView) view.findViewById(R.id.radius_textview_ID);
            v.setText(getString(R.string.radius_str) + ", " + in.toString());

            v = (TextView) view.findViewById(R.id.wheel_spacing_textview_ID);
            v.setText(getString(R.string.wheel_spacing_str) + ", " + ft.toString());

            v = (TextView) view.findViewById(R.id.DB_textview_ID);
            v.setText(getString(R.string.DB_str) + ", #");

            v = (TextView) view.findViewById(R.id.DB_spacing_textview_ID);
            v.setText(getString(R.string.DB_spacing_str) + ", " + in.toString());
        }


        //edittexts
        EditText v = (EditText) view.findViewById(R.id.hf_in);
        v.setText(sharedPref.getString(getString(R.string.SLAB_THICKNESS), "150"));


        v = (EditText) view.findViewById(R.id.ks_in);
        v.setText(sharedPref.getString(getString(R.string.SUBGRADE), "25"));

       /* v = (EditText) view.findViewById(R.id.Pu_in);
        v.setText(sharedPref.getString(getString(R.string.POINT_LOAD), "300"));*/

        v = (EditText) view.findViewById(R.id.radius_in);
        v.setText(sharedPref.getString(getString(R.string.EQUIV_RADIUS), "300"));

        v = (EditText) view.findViewById(R.id.wheel_spacing_in);
        v.setText(sharedPref.getString(getString(R.string.WHEEL_SPACING), "2500"));

        v = (EditText) view.findViewById(R.id.DB_spacing_in);
        v.setText(sharedPref.getString(getString(R.string.BAR_SPACING), "200"));


        //update spinnners
        updateSpinner();

    }

    private void updateSpinner() {
        //set up spinner selections, re-read bar selection, might have changed
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String barlist_input_str = sharedPref.getString(getString(R.string.BARLIST), "");

        if (barlist_input_str != null) {
            String[] strings = barlist_input_str.split(" ");
            strarrBarlist = new String[strings.length];
            strarrBarlist = strings;


        } else {
            strarrBarlist = new String[1];
            strarrBarlist[0] = "menu|options";
        }

        String[] reolocentries = new String[2];
        reolocentries[0] = getString(R.string.topbarstr1);
        reolocentries[1] = getString(R.string.topbarstr2);

        Spinner DBspinner = (Spinner) view.findViewById(R.id.DB_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), simple_spinner_item, strarrBarlist);
        Spinner reolocspinner = (Spinner) view.findViewById(R.id.reo_loc_spinner);
        ArrayAdapter<String> reolocarrayAdapter = new ArrayAdapter<String>(getActivity(), simple_spinner_item, reolocentries);

        arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
        reolocarrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
        DBspinner.setAdapter(arrayAdapter);
        reolocspinner.setAdapter(reolocarrayAdapter);

        //restore reoloc spinner selection from previous, selection is fixed
        reolocspinner.setSelection(
                Integer.parseInt(sharedPref.getString(getString(R.string.REOLOC_ITEM_SELECTION), "0")));

        // reset to zero if bar list preference has changed has changed
        int previousDBselectedItem = Integer.parseInt(sharedPref.getString(getString(R.string.MIN_TENSION_BAR_DIA_ITEM_SELECTION), "0"));
        if (strarrBarlist.length < previousDBselectedItem + 1) {
            DBspinner.setSelection(0);
        } else {
            DBspinner.setSelection(previousDBselectedItem);

        }


    }


    /**
     * @return true if all input is a number, otherwise
     * @throws NumberFormatException and return false
     */
    private Boolean getDesignInput() throws NumberFormatException {

        //save design input first
        saveDesignInput();

        //then retrieve input
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());


        mgamma_c = 1.d / Double.parseDouble(sp.getString(getString(R.string.PHI_C), "0.6d"));
        mgamma_s = 1.d / Double.parseDouble(sp.getString(getString(R.string.PHI_S), "0.9d"));


        //get reo location
        Spinner reolocspinner = (Spinner) view.findViewById(R.id.reo_loc_spinner);
        mReoLoc = reolocspinner.getSelectedItemPosition();


        //get bar diameter
        Spinner DBspinner = (Spinner) view.findViewById(R.id.DB_spinner);


        //throw numberformatexception if entry is not a number
        try {

            //get unit
            mUnit = sp.getString(getString(R.string.UNIT), getString(R.string.SI));


            //for SI unit
            if (mUnit.equals(getString(R.string.SI))) {
                mHf = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.SLAB_THICKNESS), "")), mm);
                mKs = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.SUBGRADE), "")), kPa_per_mm);/*
                mPu = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.POINT_LOAD), "")), kN);*/
                ma = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.EQUIV_RADIUS), "")), mm);
                mX = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.WHEEL_SPACING), "")), mm);
                //reoloc
                double dDB = Double.parseDouble(DBspinner.getSelectedItem().toString());
                mDB = new MyDouble(dDB, mm);
                mScc = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.BAR_SPACING), "")), mm);

                //pref vars
                mcover = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.COVER), "75.d")), mm);
                mFc = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.FC), "32.d")), MPa);
                mFy = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.FYMAIN), "414.d")), MPa);


            } else {
                mHf = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.SLAB_THICKNESS), "")), in);
                mKs = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.SUBGRADE), "")), ksf_per_in);/*
                mPu = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.POINT_LOAD), "")), kip);*/
                ma = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.EQUIV_RADIUS), "")), in);
                mX = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.WHEEL_SPACING), "")), in);
                //get DB
                double dDB = Double.parseDouble(DBspinner.getSelectedItem().toString()) / 8.d; //in inches
                mDB = new MyDouble(dDB, in);
                mScc = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.BAR_SPACING), "")), in);

                //pref vars
                mcover = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.COVER), "3.d")), in);
                mFc = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.FC), "32.d")), ksi);
                mFy = new MyDouble(Double.parseDouble(sp.getString(getString(R.string.FYMAIN), "414.d")), ksi);

            }

            return true;

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Check input for invalid entries..", Toast.LENGTH_LONG).show();
            return false;
        }


        //pass the data to activity
        //saveDesigInputData.SaveDesigInput(bundle);
        //then return the bundled data
        //return bundle;
    }

    public String getDesignReport() {

        String report = "sample";

        if (getDesignInput()) {

            sog = new SlabOnGround(mcover,
                    mHf,
                    mKs,/*
                    mPu,*/
                    ma,
                    mX,
                    mReoLoc,
                    mDB,
                    mScc,
                    mFc,
                    mFy,
                    mgamma_c,
                    mgamma_s,
                    mUnit);


            //input part of the report
            String inputrep;
            String[] reolocstr = new String[2];
            reolocstr[0] = getString(R.string.topbarstr1);
            reolocstr[1] = getString(R.string.topbarstr2);


            //print report

            if (mUnit.equals("SI")) {

                inputrep = "DESIGN INPUT \r\n" +
                        "Slab thickness = " + mHf.toString() + "\r\n" +
                        "Subgrade reaction = " + mKs.toString() + "\r\n" +
                        "Radius of relative stiffness = " + sog.mLr.toString() + "\r\n" +
                        "Equivalent radius of loaded area = " + ma.toString() + "\r\n" +
                        "Load spacing for dual point loads = " + mX.toString() + "\r\n" +
                        "Reinforcement location = " + reolocstr[mReoLoc] + "\r\n" +
                        "Bar diameter = " + mDB.toString() + "\r\n" +
                        "Bar spacing = " + mScc.toString() + "\r\n";

                mReport = "SLAB CAPACITY (see notes below): \r\n\r\n" +
                        "Interior location, single point = " + sog.phiPn_Interior_single.toUnit(kN).toString() + "\r\n" +
                        "Interior location, dual point = " + sog.phiPn_Interior_dual.toUnit(kN).toString() + " @ " + sog.mSload.toString() + "\r\n" +
                        "Edge location, single point = " + sog.phiPn_edge_single.toUnit(kN).toString() + "\r\n" +
                        "Edge location, dual point = " + sog.phiPn_edge_dual.toUnit(kN).toString() + " @ " + sog.mSload.toString() + "\r\n" +
                        "\r\n";

            } else {
                //get bar diameter
                Spinner DBspinner = (Spinner) view.findViewById(R.id.DB_spinner);

                inputrep = "DESIGN INPUT \r\n" +
                        "Slab thickness = " + mHf.toUnit(in).toString() + "\r\n" +
                        "Subgrade reaction = " + mKs.toUnit(ksf_per_in).toString() + "\r\n" +
                        "Radius of relative stiffness = " + sog.mLr.toUnit(in).toString() + "\r\n" +
                        "Equivalent radius of loaded area = " + ma.toUnit(in).toString() + "\r\n" +
                        "Load spacing for dual point loads = " + mX.toUnit(in).toString() + "\r\n" +
                        "Reinforcement location = " + reolocstr[mReoLoc] + "\r\n" +
                        "Bar number = " + DBspinner.getSelectedItem().toString() + "\r\n" +
                        "Bar spacing = " + mScc.toUnit(in).toString() + "\r\n";
                mReport = "SLAB CAPACITY (see notes below): \r\n\r\n" +
                        "Interior location, single point = " + sog.phiPn_Interior_single.toUnit(kip).toString() + "\r\n" +
                        "Interior location, dual point = " + sog.phiPn_Interior_dual.toUnit(kip).toString() + " @ " + sog.mSload.toUnit(in).toString() + "\r\n" +
                        "Edge location, single point = " + sog.phiPn_edge_single.toUnit(kip).toString() + "\r\n" +
                        "Edge location, dual point = " + sog.phiPn_edge_dual.toUnit(kip).toString() + " @ " + sog.mSload.toUnit(in).toString() + "\r\n" +
                        "\r\n";
            }


            return mReport + inputrep + sog.mNotes;
            //return "test only";


        } else {
            report = "Check design input";
        }
        return report;
    }


}
