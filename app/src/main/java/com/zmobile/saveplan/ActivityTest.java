package com.zmobile.saveplan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

//import static com.z_mobile.bmi.R.id.adView;

/**
 * Created by lukasz on 04.08.2017.
 */

public class ActivityTest extends Activity {

    InterstitialAd interstitial;
    AdRequest adRequest;

    public boolean showNativeAds = false;
    public boolean showExpressAds = true;
    public boolean showFaceAds = true;
    public boolean showAmazonAds = true;

    public boolean showFaceNative = true;
    public boolean showFaceBanner = true;
    public boolean showAdmobBanner = true;
    public boolean showAdmobExpress = true;
    public boolean showAmazonBanner = true;

    public AdView adView;
    //public com.amazon.device.ads.AdLayout amazonAd;
    public NativeExpressAdView adViewExpress;
    public FrameLayout nativeAdContainer;
    public FrameLayout fbBannerContainer;

    public int faceNativeLayoutId = R.layout.ad_unit;
    int admobNativeLayoutId = 3;

    //AdsGeneralHandler adsHandle;

    @Override
    public void onStop() {
        super.onStop();
        // The rest of your onStop() code.
        //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        //adView.pause();
        super.onPause();

    }

    public void onBackPressed() {

        if (interstitial.isLoaded()) {
            interstitial.show();
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        adView = (AdView) findViewById(R.id.adView);

        AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
        adRequest = AdBuilder.build();
        adView.setAdListener(getAdMobBannerListener);
        adView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.admob_inter_id));
        interstitial.setAdListener(getAdMobInterListener);

        //adsHandle = new AdsGeneralHandler(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // The rest of your onStart() code.
        interstitial.loadAd(adRequest);
        //adsHandle.loadAds();
        //adsHandle.showAds();

    }

    com.google.android.gms.ads.AdListener getAdMobBannerListener = new com.google.android.gms.ads.AdListener(){


        @Override
        public void onAdFailedToLoad(int errorCode){
            toastMsg("AdMob banner load error: "+errorCode, 3);


        }

        @Override
        public void onAdLoaded(){
            toastMsg("AdMob banner loaded", 3);
        }

    };

    com.google.android.gms.ads.AdListener getAdMobInterListener = new com.google.android.gms.ads.AdListener(){

        @Override
        public void onAdFailedToLoad(int errorCode){
            toastMsg("AdMob inter load error: "+errorCode, 3);
        }

        @Override
        public void onAdLoaded(){
            toastMsg("AdMob inter loaded", 3);
        }


    };

    public void toastMsg(String msg, int level){

        //String msg = context.getResources().getString(stringId);

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
