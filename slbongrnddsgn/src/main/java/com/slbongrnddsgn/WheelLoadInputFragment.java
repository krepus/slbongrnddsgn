package com.slbongrnddsgn;

/**
 * Created with IntelliJ IDEA.
 * User: j0sua3
 * Date: 30/06/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static com.slbongrnddsgn.MyDouble.Unit;

public class WheelLoadInputFragment extends Fragment {

    private View view;
    private String[] strarrBarlist;
    Bundle mSavedInstanceState = null;


/*
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        try {
            saveDesigInputData = (SaveDesigInputData) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
*/

    /**
     * @param inflater
     * @param container          is the root view, UNO
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSavedInstanceState = savedInstanceState;
        view = inflater.inflate(R.layout.wheel_load_main, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDesignInput();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mTabHost = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateInputUI();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean barlistchanged = Boolean.parseBoolean(sharedPref.getString(
                getString(R.string.BARLIST_RESET), "false"));
        if (barlistchanged) {
            updateSpinner();
        } else {
            updateSpinner();
            //restore spinner selection
            Spinner DBspinner = (Spinner) view.findViewById(R.id.DB_spinner);
            DBspinner.setSelection(
                    Integer.parseInt(sharedPref.getString(getString(R.string.MIN_TENSION_BAR_DIA_ITEM_SELECTION), "0")));


            Spinner relocspinner = (Spinner) view.findViewById(R.id.reo_loc_spinner);
            relocspinner.setSelection(
                    Integer.parseInt(sharedPref.getString(getString(R.string.REOLOC_ITEM_SELECTION), "0")));

        }

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    /*

    // Container Activity must implement this interface to pass data
    public interface SaveDesigInputData {
        public void SaveDesigInput(Bundle bundle);

        //data is actually saved in this fragments onPause method
    }


    */

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

        v = (TextView) view.findViewById(R.id.Pu_in);
        ed.putString(getString(R.string.POINT_LOAD), v.getText().toString());


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

    private void updateInputUI() {
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
            v.setText(getString(R.string.hf_str) + ", " + Unit.mm.toString());

            v = (TextView) view.findViewById(R.id.ks_textview_ID);
            v.setText(getString(R.string.ks_str) + ", " + Unit.MPa_per_mm);

            v = (TextView) view.findViewById(R.id.Pu_textview_ID);
            v.setText(getString(R.string.Pu_str) + ", " + Unit.kN.toString());


            v = (TextView) view.findViewById(R.id.radius_textview_ID);
            v.setText(getString(R.string.radius_str) + ", " + Unit.mm.toString());

            v = (TextView) view.findViewById(R.id.wheel_spacing_textview_ID);
            v.setText(getString(R.string.wheel_spacing_str) + ", " + Unit.mm.toString());

            v = (TextView) view.findViewById(R.id.DB_textview_ID);
            v.setText(getString(R.string.DB_str) + ", " + DIA + "(" + Unit.mm.toString() + ")");

            v = (TextView) view.findViewById(R.id.DB_spacing_textview_ID);
            v.setText(getString(R.string.DB_spacing_str) + ", " + Unit.mm.toString());

        } else {
            TextView v = (TextView) view.findViewById(R.id.hf_textview_ID);
            v.setText(getString(R.string.hf_str) + ", " + Unit.in.toString());

            v = (TextView) view.findViewById(R.id.ks_textview_ID);
            v.setText(getString(R.string.ks_str) + ", " + Unit.psi_per_in);

            v = (TextView) view.findViewById(R.id.Pu_textview_ID);
            v.setText(getString(R.string.Pu_str) + ", " + Unit.kip.toString());


            v = (TextView) view.findViewById(R.id.radius_textview_ID);
            v.setText(getString(R.string.radius_str) + ", " + Unit.in.toString());

            v = (TextView) view.findViewById(R.id.wheel_spacing_textview_ID);
            v.setText(getString(R.string.wheel_spacing_str) + ", " + Unit.ft.toString());

            v = (TextView) view.findViewById(R.id.DB_textview_ID);
            v.setText(getString(R.string.DB_str) + ", #");

            v = (TextView) view.findViewById(R.id.DB_spacing_textview_ID);
            v.setText(getString(R.string.DB_spacing_str) + ", " + Unit.in.toString());
        }


        //edittexts
        EditText v = (EditText) view.findViewById(R.id.hf_in);
        v.setText(sharedPref.getString(getString(R.string.SLAB_THICKNESS), "150"));


        v = (EditText) view.findViewById(R.id.ks_in);
        v.setText(sharedPref.getString(getString(R.string.SUBGRADE), "0.025"));

        v = (EditText) view.findViewById(R.id.Pu_in);
        v.setText(sharedPref.getString(getString(R.string.POINT_LOAD), "300"));

        v = (EditText) view.findViewById(R.id.radius_in);
        v.setText(sharedPref.getString(getString(R.string.EQUIV_RADIUS), "300"));

        v = (EditText) view.findViewById(R.id.wheel_spacing_in);
        v.setText(sharedPref.getString(getString(R.string.WHEEL_SPACING), "2500"));

        v = (EditText) view.findViewById(R.id.DB_spacing_textview_ID);
        v.setText(sharedPref.getString(getString(R.string.DB_spacing_str), "200"));

        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putString(getString(R.string.UNIT_CHANGED), "false");
        ed.commit();

    }

    private void updateSpinner() {
        //read preferences
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

        Spinner DBspinner = (Spinner) view.findViewById(R.id.DB_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, strarrBarlist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DBspinner.setAdapter(arrayAdapter);
        Spinner reolocspinner = (Spinner) view.findViewById(R.id.reo_loc_spinner);
        ArrayAdapter<String> reolocarrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, R.array.reo_loc_entries);
        reolocspinner.setAdapter(reolocarrayAdapter);

        //
        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putString(getString(R.string.BARLIST_RESET), "false");
        ed.commit();
    }


}
