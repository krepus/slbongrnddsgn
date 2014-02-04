package com.slbongrnddsgn;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import util.IabHelper;
import util.IabResult;
import util.Inventory;

/**
 * Created by josua.arnigo on 3/02/14.
 */
public class InAppBillingFragment extends Fragment {

    /*in app biling*/
    boolean mHasLicense = false;
    //String SKU_PREMIUM = "";
    IabHelper mIABHelper;
    String TAG;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //avoid overlapping fragment
        if (savedInstanceState != null) {
            return;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        if (mIABHelper != null) mIABHelper.dispose();
        mIABHelper = null;
    }


    public boolean isLicensed() {


        if (mHasLocalLicense()) {

            return true;
        } else

        {

         /*check google play store in app biling*/
            String base64EncodedPublicKey;

            // compute your public key and store it in base64EncodedPublicKey
            base64EncodedPublicKey = new String(getString(R.string.DONKEY) + getString(R.string.HORSE) + getString(R.string.CARABAO)
                    + getString(R.string.GOAT));

            mIABHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

            //perform service binding
            TAG = "IABLog";
            mIABHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    } else {

                        // Hooray, IAB is fully set up!
                        Log.d(TAG, "In-app Billing OK: " + result);
                    }

                }
            });

            //querry google play for license
            IabHelper.QueryInventoryFinishedListener mGotInventoryListener
                    = new IabHelper.QueryInventoryFinishedListener() {
                public void onQueryInventoryFinished(IabResult result,
                                                     Inventory inventory) {

                    if (result.isFailure()) {
                        // handle error here
                        Log.d(TAG, "SKU_premium querry error: " + result);
                    } else {
                        // does the user have the premium upgrade?
                        mHasLicense = inventory.hasPurchase(getString(R.string.SKU_LICENSE));
                        // update UI accordingly
                    }
                }
            };
            mIABHelper.queryInventoryAsync(mGotInventoryListener);


            return mHasLicense;
        }


    }

    public boolean mHasLocalLicense() {
        mHasLicense = false;
        return mHasLicense;
    }


}
