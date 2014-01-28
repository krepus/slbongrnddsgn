package com.slbongrnddsgn;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

    //design input variables

    private WheelLoadFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set defaults
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preference, false);

        setContentView(R.layout.activity_main);


        /**
         Context mCtxt = this;
         FragmentManager fragmentManager = getSupportFragmentManager();
         mTabHost = new android.support.v4.app.FragmentTabHost(mCtxt);
         mTabHost.setup(mCtxt, fragmentManager, R.id.fragment_container);

         // mTabHost.addTab(mTabHost.newTabSpec("design brief").setIndicator("design brief"),
         //         DesignBriefFragment.class, null);
         mTabHost.addTab(mTabHost.newTabSpec("design input").setIndicator("design input"),
         WheelLoadFragment.class, null);
         // mTabHost.addTab(mTabHost.newTabSpec("design out").setIndicator("design out"),
         //      DesignOutFragment.class, null);

         //return mTabHost;

         setContentView(mTabHost);

         */

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        mFragment = new WheelLoadFragment();
        ft.add(R.id.fragmentContainer, mFragment);
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        //temp
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.preferences_id:
                // Display the fragment as the menu_main content.

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.About:

                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.about_dialog);
                dialog.setTitle("About the app");
                TextView text = (TextView) dialog.findViewById(R.id.about_text);
                text.setText("This app will check the design thickness of a slab with specified design parameters");

                // ImageView image = (ImageView) dialog.findViewById(R.id.image);
                // image.setImageResource(R.drawable.icon);
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
