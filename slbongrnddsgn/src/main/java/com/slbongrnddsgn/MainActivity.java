package com.slbongrnddsgn;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;

public class MainActivity extends Activity {

    //design input variables

    private WheelLoadFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set defaults
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preference, false);
        setContentView(R.layout.activity_main);

        //avoid overlapping fragment
        if (savedInstanceState != null) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        mFragment = new WheelLoadFragment();
        ft.add(R.id.fragmentContainer, mFragment);
        ft.commit();



        /*in app biling*/

        bindService(new
                Intent("com.android.vending.billing.InAppBillingService.BIND"),
                mServiceConn, Context.BIND_AUTO_CREATE);

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
                text.setText("This app will calculate the point load capacity of a slab on ground" +
                        "in accordance with Concrete Society Report TR34 - Concrete " +
                        "industrial ground floors, 3rd edition");

                // ImageView image = (ImageView) dialog.findViewById(R.id.image);
                // image.setImageResource(R.drawable.icon);
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    /*following code realates to IAB*/

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };


}
