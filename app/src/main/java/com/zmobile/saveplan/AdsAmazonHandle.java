package com.zmobile.saveplan;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.InterstitialAd;

/**
 * Created by lukasz on 13.06.2017.
 */

public class AdsAmazonHandle {

    static final String TAG = AdsAmazonHandle.class.getSimpleName();

    private com.amazon.device.ads.AdLayout amazonAd;
    private InterstitialAd interstitialAd;
    private boolean adLoaded = false;
    private boolean interstitalLoaded = false;
    Context context;
    boolean showToast = false;

    public boolean isAdLoaded() {
        return adLoaded;
    }

    public boolean isInterstitalLoaded() {
        return interstitalLoaded;
    }

    /**
     * This class is for an event listener that tracks ad lifecycle events.
     * It extends DefaultAdListener, so you can override only the methods that you need.
     */

    public AdsAmazonHandle(Context context, AdLayout adLayout) {

        this.context = context;
        AdRegistration.setAppKey("8d4fd9066c834c3898e32c6cf7e8ad42");

        amazonAd = adLayout;
        //SampleAdListener amListener = new SampleAdListener(amazonAd);
        AdListener amListener = new AdListener(amazonAd);
        amazonAd.setListener(amListener);
        //adListener);
        // Create the interstitial.
        this.interstitialAd = new InterstitialAd(context);

        // Set the listener to use the callbacks below.
        this.interstitialAd.setListener(new MyCustomAdListener());

        // Load the interstitial.
        this.interstitialAd.loadAd();
    }

    public void loadAd() {
        if (amazonAd != null) amazonAd.loadAd();
        if (interstitialAd != null) interstitialAd.loadAd();
    }

    public void showAd() {
        if (amazonAd != null) {
            if (isAdLoaded())
                amazonAd.showAd();
        }
    }

    public void showInterstitial() {

        if (interstitialAd != null) interstitialAd.showAd();
    }

    public void destroy(){
        amazonAd.destroy();
    }

    class AdListener extends DefaultAdListener {

        AdLayout adView;

        public AdListener(com.amazon.device.ads.AdLayout adView){
            this.adView = adView;
        }
        /**
         * This event is called once an ad loads successfully.
         */
        @Override
        public void onAdLoaded(final Ad ad, final AdProperties adProperties) {

            adLoaded = true;
            amazonAd.showAd();
            if (adView!=null) adView.showAd();
            Log.i(TAG, adProperties.getAdType().toString() + " ad loaded successfully.");
            /*
            //Once a banner ad has been loaded, it can be shown
            if (!SimpleAdActivity.this.autoShowCheckBox.isChecked()) {
                SimpleAdActivity.this.showAdButton.setEnabled(true);
                SimpleAdActivity.this.loadAdButton.setEnabled(false);
            }*/
        }

        /**
         * This event is called if an ad fails to load.
         */
        @Override
        public void onAdFailedToLoad(final Ad ad, final AdError error) {
            Log.w(TAG, "Amazon Ad failed to load. Code: " + error.getCode() + ", Message: " + error.getMessage());
            //SimpleAdActivity.this.loadAdButton.setEnabled(true);
            if (showToast)
                Toast.makeText(context, "Amazon Ad Failed "+error.getMessage(), Toast.LENGTH_SHORT).show();
            //SimpleAdActivity.this.loadAdButton.setEnabled(true);

        }

        /**
         * This event is called after a rich media ad expands.
         */
        @Override
        public void onAdExpanded(final Ad ad) {
            Log.i(TAG, "Ad expanded.");
            // You may want to pause your activity here.
        }

        /**
         * This event is called after a rich media ad has collapsed from an expanded state.
         */
        @Override
        public void onAdCollapsed(final Ad ad) {
            Log.i(TAG, "Ad collapsed.");
            // Resume your activity here, if it was paused in onAdExpanded.
        }
    }

    class MyCustomAdListener extends DefaultAdListener
    {
        @Override
        public void onAdLoaded(Ad ad, AdProperties adProperties)
        {
            interstitalLoaded = true;
            if (ad == interstitialAd)
            {
                // Show the interstitial ad to the app's user.
                // Note: While this implementation results in the ad being shown
                // immediately after it has finished loading, for smoothest user
                // experience you will generally want the ad already loaded
                // before it’s time to show it. You can thus instead set a flag
                // here to indicate the ad is ready to show and then wait until
                // the best time to display the ad before calling showAd().
                interstitialAd.showAd();
            }
        }

        @Override
        public void onAdFailedToLoad(Ad ad, AdError error)
        {
            // Call backup ad network.
            Log.w(TAG, "Amazon interstitial failed to load. Code: " + error.getCode() + ", Message: " + error.getMessage());
            if (showToast)
                Toast.makeText(context, "Amazon Inter Failed "+error.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdDismissed(Ad ad)
        {
            // Start the level once the interstitial has disappeared.
            //startNextLevel();
        }
    }
}
