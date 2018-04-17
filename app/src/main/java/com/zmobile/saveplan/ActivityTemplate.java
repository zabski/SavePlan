package com.zmobile.saveplan;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
//import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.zmobile.ads.AdsGeneralHandler;
import com.zmobile.ads.AdviewsContainer;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by lukasz on 2015-06-16.
 */
public class ActivityTemplate extends Activity implements Updatable  {

    public String TAG;// = ActivityTemplate.class.getSimpleName();
    public String classTAG = ActivityTemplate.class.getSimpleName();
    private static String ADMOB_APP_ID;// = "ca-app-pub-4402674240600002~4147934173";

    ActivityTemplate act;

    static final long millisPerDay = 24 * 60 * 60 * 1000;
    Tracker mTracker;

    MenuHelper menuHelp;
    SharedPreferences sharedData;
    String filename = "SharedData";


    //------------------------ showing ads -----------------------------

    int ads, fb_ads, log;

    String fbNativeId;

    //admobNativeId = getString(R.string.admob_native_main_id);

    double adShowBias = 0.5;
    static boolean adShown = false;
    AdviewsContainer adsContain;

    //private static InterstitialAd interstitial;
    //private static com.facebook.ads.InterstitialAd faceInterstitial;
    com.google.android.gms.ads.InterstitialAd interstitial;
    com.facebook.ads.InterstitialAd faceInterstitial;

    AdsAmazonHandle adsAmazon;
    String interAdId;
    static String admobNativeId;
    static String faceInterId;
    static int faceShown = 0;
    static int admobShown = 0;
    static int interShown = 0;
    //public static InterstitialAd fullAd;
    AdRequest adRequest;
    HandlerPurchase handlerIAP;
    //Tracker mTracker;

    //Facebook native ads

    private LinearLayout fb_adView;
    private NativeAd nativeAd;
    private AdChoicesView adChoicesView;
    //String adNativeId = ActivityMenu.fbNativeId;
    int fb_adView_end_id = R.id.native_ad_container_end;
    int fb_adView_front_id = R.id.native_ad_container_front;
    public FrameLayout fb_adView_end, fb_adView_front;

    FacebookNativeAds fbNativeAds;
    AdmobNativeAds admobNativeAds;

    NumberTextWatcher numbetWatcher;
    //Tracker mTracker;

    public boolean showNativeAds = false;
    public boolean showExpressAds = true;
    public boolean showFaceAds = true;
    public boolean showAmazonAds = true;

    public boolean showFaceNative = true;
    public boolean showFaceBanner = true;
    public boolean showAdmobBanner = true;
    public boolean showAdmobExpress = true;
    public boolean showAmazonBanner = true;

    public com.google.android.gms.ads.AdView adView;
    public com.amazon.device.ads.AdLayout amazonAd;
    public NativeExpressAdView adViewExpress;
    public FrameLayout nativeAdContainer;
    public FrameLayout fbBannerContainer;
    public FrameLayout admobNativeContainer;

    AdsGeneralHandler adsHandle;

    public int faceNativeLayoutId = R.layout.ad_unit;
    int admobNativeLayoutId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the shared Tracker instance.
        //AnalyticsApplication application = (AnalyticsApplication) getApplication();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion>=14) {
            if (TAG == null || (!TAG.equals("ActivityMenu"))) {
                ActionBar actionBar = getActionBar();
                if (actionBar!=null) {
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setIcon(R.drawable.prev_2);
                }
            }
        }
        mTracker = getDefaultTracker(this);
        act = this;
        fbNativeId = getResources().getString(R.string.facebook_native_id);
        menuHelp = MenuHelper.getInstance(this);
                //new MenuHelper(this);
        sharedData = getSharedPreferences(filename, 0);
        act = this;
        if (TAG==null || (!TAG.equals("ActivityMenu2"))) {
            ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setIcon(R.drawable.prev_2);
        }
        AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
        adRequest = AdBuilder.build();
        ADMOB_APP_ID = getString(R.string.admob_app_id);
        MobileAds.initialize(this, ADMOB_APP_ID);
        //String interAd = getString(R.string.inter_ad);
        interAdId = getString(R.string.admob_inter_id3);
        //admobNativeId = getString(R.string.admob_native_main_id);
        faceInterId = getString(R.string.facebook_inter_id);
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(interAdId);
        interstitial.setAdListener(getAdMobInterListener);

        ads = getResources().getInteger(R.integer.ads);
        fb_ads = getResources().getInteger(R.integer.fb_ads);
        log = getResources().getInteger(R.integer.log);
        //adShown = false;
        //handlerIAP = HandlerPurchase.getInstance(this);
        int theme = sharedData.getInt("theme", 0);

        //ThemeUtils.setTheme(theme);
        //ThemeUtils.onActivityCreateSetTheme(this);

        //if (showFaceAds)
        if (faceNativeLayoutId == 0)
            faceNativeLayoutId = R.layout.ad_unit;

        if (admobNativeLayoutId == 0)
            admobNativeLayoutId = R.layout.ad_app_install;

        /*
        loadFacebookFullAd();

        fbNativeAds = new FacebookNativeAds();
        //if (adView!=null) adView.setVisibility(View.GONE);

        admobNativeAds = new AdmobNativeAds(this);

        //amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
        //if (amazonAd != null)
        adsAmazon = new AdsAmazonHandle(this, amazonAd);
        */

        adsContain = new AdviewsContainer();
        adsContain.adView = adView;
        adsContain.admobNativeContainer = admobNativeContainer;
        adsContain.adViewExpress = adViewExpress;
        adsContain.fbBannerContainer  = fbBannerContainer;
        adsContain.amazonAd = amazonAd;
        adsContain.nativeAdContainer = nativeAdContainer;

        adsContain.showAdmobBanner = showAdmobBanner;
        adsContain.showAdmobExpress = showAdmobExpress;
        adsContain.showAmazonBanner = showAmazonBanner;
        adsContain.showFaceBanner = showFaceBanner;
        adsContain.showFaceNative = showFaceNative;


        adsHandle = new AdsGeneralHandler(this, adsContain);
    }

    @Override
    public void onStart() {
        super.onStart();
        // The rest of your onStart() code.
        //ThemeUtils.onActivityCreateSetThemeId(this);
        adShown = false;
        //loadAds();
        adsHandle.loadAds();
        adsHandle.showAds();
        /*
        try {
            String id = interstitial.getAdUnitId();
            if (id == null) {
                interstitial.setAdUnitId(interAdId);
            }
            if (!interstitial.isLoaded())
                interstitial.loadAd(adRequest);

            //ActivityMenu.fbFullAd.loadAd();
            if (showFaceAds)
                if (!faceInterstitial.isAdLoaded())
                    faceInterstitial.loadAd();

        }catch (Exception e){
            toastMsg("Start error: "+e.getMessage(),2);
        }*/
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //ThemeUtils.onActivityCreateSetTheme(this);
        //setContentView(R.layout.main);
        if (mTracker!=null) {
            mTracker.setScreenName(TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
        //admobNativeAds.refreshAd(nativeAdContainer,true,true);
        /*
        if (showFaceAds)
            showFacebookNativeAd(faceNativeLayoutId);
        if (showNativeAds)
            showAdmobNativeAd();
        if (showExpressAds)
            showAdMobExpressNativeAd();
            */
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (adView!=null) adView.pause();
        super.onPause();
        //showInterstitial();
        //ThemeUtils.saveTheme(instance);
        try {
            //showInterstitial(adShowBias);
            //showInterstitial2();
            adsHandle.showInterstitial4();
        }catch (Exception e){
            toastMsg("Pause error: "+e.getMessage(),3);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //removeAdView();
        if (adView!=null) adView.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        //showInterstitial();
        super.onBackPressed();
    }


    // ------------------------------- showing ads ------------------------------------------------

    public void loadAdsOld(){
        /*
        if (handlerIAP!=null)
            if (handlerIAP.mHasRemovedAds)
                return;
        if (adView==null)
            adView = (AdView) findViewById(R.id.adView);
            */
        if (ads > 0 ) { //&& !handlerIAP.mHasRemovedAds) {
            if (ads > 1 && adView!=null) {
                setAdsVisible(adView);
                adView.loadAd(adRequest);
                adView.resume();
            }
            if (interstitial!=null)
                if (!interstitial.isLoaded() && !interstitial.isLoading())
                    interstitial.loadAd(adRequest);
        }
        if (adView != null) {
            if (ads < 1) { // || handlerIAP.mHasRemovedAds) {
                adView.setVisibility(View.GONE);
            }else{
                adView.setVisibility(View.VISIBLE);
            }
        }
        if (fb_ads == 0 && ads == 0) { //|| handlerIAP.mHasRemovedAds) {
            if (nativeAdContainer != null) nativeAdContainer.setVisibility(View.GONE);
        }
        if (showAmazonAds){
            adsAmazon.loadAd();
        }
    }

    protected void showInterstitial(double showBias) {
        //if (interstitial.isReady()) {
        try {
            double rand = Math.random();
            /*
            if (handlerIAP!=null)
                if (handlerIAP.mHasRemovedAds)
                    return;
                    */
            if (ads == 0 || rand > showBias) // || adShown)
                return;
            if (interShown % 2 == 1) {
                // google
                if (interstitial != null) {
                    boolean loaded = interstitial.isLoaded();
                    if (loaded) { // && adShown == false) {
                        interstitial.show();
                        interstitial.loadAd(adRequest);
                        adShown = true;
                        admobShown++;
                        //toastMsg("Admob shown: "+admobShown+" face: "+faceShown, 2);
                    } else if (!interstitial.isLoading())
                        interstitial.loadAd(adRequest);
                }
            } else {
                // facebook
                //if (ActivityMenu.fbFullAd != null) {
                if (faceInterstitial != null) {
                    //boolean loaded = ActivityMenu.fbFullAd.isAdLoaded();
                    boolean loaded = faceInterstitial.isAdLoaded();
                    //faceInterstitial.show();
                    if (loaded) {
                        //ActivityMenu.fbFullAd.show();
                        faceInterstitial.show();
                        faceInterstitial.loadAd();
                        adShown = true;
                        faceShown++;
                        //toastMsg("Face shown: "+faceShown+" admob "+admobShown, 2);
                    } else {
                        //ActivityMenu.fbFullAd.loadAd();
                        faceInterstitial.loadAd();
                    }
                }
            }
            toastMsg("Admob shown: "+admobShown+" face: "+faceShown, 2);
            interShown++;
        }catch (Exception e){
            Log.d("showInterstitial() ", e.toString());
        }

		/*else if (MobileCore.isOfferwallReady())
			MobileCore.showOfferWall(this, null);*/
    }

    protected void showInterstitial2(){
        double rand = Math.random();
        /*
        if (handlerIAP!=null)
            if (handlerIAP.mHasRemovedAds)
                return;*/
        if (ads == 0 || rand > adShowBias) // || adShown)
            return;
        boolean admobLoaded = interstitial.isLoaded();
        boolean faceLoaded = faceInterstitial.isAdLoaded();
        interShown++;
        if (interShown % 3 == 1) {
            if (admobLoaded) {
                interstitial.show();
                admobShown++;
                //interstitial.loadAd(adRequest);
                //toastMsg("Admob shown: "+admobShown+" face: "+faceShown, 3);
            }
            interstitial.loadAd(adRequest);
            //ActivityMenu.fbFullAd.show();
        }else if (interShown % 3 == 2){
            if (faceLoaded) {
                faceInterstitial.show();
                faceShown++;
                //toastMsg("Face shown: "+faceShown+" admob "+admobShown, 3);
            } else
                faceInterstitial.loadAd();
        }else{

            if (adsAmazon.isInterstitalLoaded())
                adsAmazon.showInterstitial();
        }
        toastMsg("Admob shown: "+admobShown+" face: "+faceShown, 2);
    }

    public void setAdVisible(boolean show){
        if (adView != null) {
            if (show) {
                adView.setVisibility(View.VISIBLE);
                nativeAdContainer.setVisibility(View.VISIBLE);
            } else {
                adView.setVisibility(View.GONE);
                nativeAdContainer.setVisibility(View.GONE);
            }
        }
    }

        /*

    private void loadAds(){

        if (ads > 0 && !handlerIAP.mHasRemovedAds) {
            if (ads > 1 && adView!=null) {
                adView.loadAd(adRequest);
                adView.resume();
            }
            if (interstitial!=null)
                if (!interstitial.isLoaded() && !interstitial.isLoading())
                    interstitial.loadAd(adRequest);
        }
        if (ads < 2 || handlerIAP.mHasRemovedAds) {
            if (adView != null) adView.setVisibility(View.GONE);
        }
        if (fb_ads == 0 || handlerIAP.mHasRemovedAds) {
            if (nativeAdContainer != null) nativeAdContainer.setVisibility(View.GONE);
        }
    }

    private void showInterstitial() {
        //if (interstitial.isReady()) {
        double rand = Math.random();
        if (interstitial!=null && !handlerIAP.mHasRemovedAds) {
            boolean loaded = interstitial.isLoaded();
            if (loaded && rand < adShowBias && ads > 0) { //&& adShown == false) {
                interstitial.show();
                interstitial.loadAd(adRequest);
                adShown = true;
            }
        }

    }

    */


    /*



    @Override
    public void onError(Ad ad, AdError error) {
        //nativeAdStatus.setText("Ad failed to load: " + error.getErrorMessage());
        if (nativeAdContainer!=null) nativeAdContainer.setVisibility(View.GONE);
        if (adView!=null && !handlerIAP.mHasRemovedAds){
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(adRequest);
        }
    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(this, "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (nativeAd == null || nativeAd != ad) {
            // Race condition, load() called again before last ad was displayed
            if (nativeAdContainer!=null) nativeAdContainer.setVisibility(View.VISIBLE);
            return;
        }

        // Unregister last ad
        nativeAd.unregisterView();

        //nativeAdStatus.setText("");

        // Using the AdChoicesView is optional, but your native ad unit should
        // be clearly delineated from the rest of your app content. See
        // https://developers.facebook.com/docs/audience-network/guidelines/native-ads#native
        // for details. We recommend using the AdChoicesView.
        if (adChoicesView == null) {
            adChoicesView = new AdChoicesView(this, nativeAd, true);
            fb_adView.addView(adChoicesView, 0);
        }

        inflateAd(nativeAd, fb_adView, this);

        // Registering a touch listener to log which ad component receives the touch event.
        // We always return false from onTouch so that we don't swallow the touch event (which
        // would prevent click events from reaching the NativeAd control).
        // The touch listener could be used to do animations.
        nativeAd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.native_ad_call_to_action:
                            Log.d(TAG, "Call to action button clicked");
                            break;
                        case R.id.native_ad_media:
                            Log.d(TAG, "Main image clicked");
                            break;
                        default:
                            Log.d(TAG, "Other ad component clicked");
                    }
                }
                return false;
            }
        });
    }

    public static void inflateAd(NativeAd nativeAd, View adView, Context context) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext =
                (TextView) adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(View.VISIBLE);
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Downloading and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        int bannerWidth = adCoverImage.getWidth();
        int bannerHeight = adCoverImage.getHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int mediaWidth = adView.getWidth() > 0 ? adView.getWidth() : metrics.widthPixels;
        nativeAdMedia.setLayoutParams(new LinearLayout.LayoutParams(
                mediaWidth,
                Math.min(
                        (int) (((double) mediaWidth / (double) bannerWidth) * bannerHeight),
                        metrics.heightPixels / 3)));
        nativeAdMedia.setNativeAd(nativeAd);

        // Wire up the View with the native ad, the whole nativeAdContainer will be clickable.
        nativeAd.registerViewForInteraction(adView);

        // You can replace the above call with the following call to specify the clickable areas.
        // nativeAd.registerViewForInteraction(adView,
        //     Arrays.asList(nativeAdCallToAction, nativeAdMedia));
    }

    */


    // ---------------------------- facebook inter ------------------------------------

    private void loadFacebookFullAd() {
        String fbFullAdId = getString(R.string.facebook_inter_id);
        faceInterstitial = new com.facebook.ads.InterstitialAd(this, fbFullAdId);
        faceInterstitial.setAdListener(getFacebookFullAdListener);
        faceInterstitial.loadAd();
    }

    InterstitialAdListener getFacebookFullAdListener = new InterstitialAdListener() {
        public void onInterstitialDisplayed(Ad ad) { }

        public void onInterstitialDismissed(Ad ad) {
            //ActivityMenu.fbFullAd.loadAd();
            faceInterstitial.loadAd();
        }

        public void onError(Ad ad, AdError adError) {
            toastMsg("Face inter error: "+adError.getErrorMessage(), 3);
        }

        public void onAdLoaded(Ad ad) {
            toastMsg("Face inter loaded", 3);
        }

        public void onLoggingImpression(Ad ad) { }

        public void onAdClicked(Ad ad) {}
    };

    // ---------------------------- facebook native ------------------------------------

    private void showFacebookNativeAd(int layoutId){

        if (nativeAdContainer==null)
            nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
        if (nativeAdContainer==null)
            return;

        LinearLayout ll = new LinearLayout(this);
        //LinearLayout ll = nativeAdContainer;
        //LinearLayout ll2 = ll;

        fbNativeAds = new FacebookNativeAds();
        //fbNativeAds.createAndLoadNativeAd(this, ll, R.layout.ad_unit_grid);
        fbNativeAds.createAndLoadNativeAd(this, ll, layoutId, fbNativeId);

        //v = vi.inflate(R.layout.ad_container, null);
        //ll2 = (LinearLayout) ll.getChildAt(0);
        //View adview = (View) ll2.getChildAt(1);
        //ViewGroup.LayoutParams lp = v.getLayoutParams();
        //AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ll2.getLayoutParams());
        //ll2.setLayoutParams(lp);
        //ListView lv = new ListView(c);
        //lv.addView(v,0);
        //View v2 = lv.getChildAt(0);
        //AbsListView.
        //LinearLayout.
        //ViewGroup.LayoutParams lp = convertView.getLayoutParams();
        //adview.setLayoutParams(lp);
        //((ViewGroup)ll2.getParent()).removeAllViews();

        if (ll.getChildCount()<1) {
            showAdmobNativeAd();
        }else {
            setAdsVisible(nativeAdContainer);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(ll);
        }

    }

    public void onFbNativeAdError(){
        showAdmobNativeAd();
    /*
            nativeAdContainer.setVisibility(View.GONE);
        if (adView != null) {
            if (ads > 3 && !handlerIAP.mHasRemovedAds) {
                adView.setVisibility(View.VISIBLE);
                adView.loadAd(adRequest);
            }else{
                adView.setVisibility(View.GONE);
            }
        }*/

    }

    public void onFbNativeAdLoaded(){

        if (adView != null)
            adView.setVisibility(View.GONE);
        if (nativeAdContainer!=null)
            nativeAdContainer.setVisibility(View.VISIBLE);

    }
/*

    protected void createAndLoadNativeAd() {

        LayoutInflater inflater = LayoutInflater.from(this);
        fb_adView = (LinearLayout) inflater.inflate(R.layout.ad_unit, nativeAdContainer, false);
        nativeAdContainer.addView(fb_adView);

        //nativeAdStatus = (TextView) findViewById(R.id.native_ad_status);
        //showNativeAdButton = (Button) findViewById(R.id.load_native_ad_button);

        nativeAd = new NativeAd(ActivityTemplate.this, adNativeId);

        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(ActivityTemplate.this);

        // When testing on a device, add its hashed ID to force test ads.
        // The hash ID is printed to log cat when running on a device and loading an ad.
        // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

        // Initiate a request to load an ad.
        nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);


    }
	
	*/

    // ---------------------------- Ad Mob Native --------------------------------------------------

    public void showAdmobNativeAd(){
        //LinearLayout ll = new LinearLayout(this);
        FrameLayout fl = new FrameLayout(this);
        //LinearLayout ll = nativeAdContainer;
        admobNativeAds = new AdmobNativeAds((Activity) this);
        admobNativeAds.refreshAd(fl, true, true, admobNativeLayoutId);
        //LinearLayout l;
        //ll2 = ll;
        ViewGroup parent = ((ViewGroup)fl.getParent());
        if (parent!=null)
            parent.removeAllViews();
        /*
        if (ll.getChildCount()<1) {
            loadAds();
        }else{
        */
        if (nativeAdContainer==null)
            nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
        if (nativeAdContainer!=null){
            setAdsVisible(nativeAdContainer);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(fl);
        }
    }

    // ---------------------------- Ad Mob Native Express ------------------------------------------

    public void showAdMobExpressNativeAd(){
        //if (nativeAdContainer!=null)
        //nativeAdContainer.setVisibility(View.GONE);
        if (adViewExpress!=null){
            if (!showExpressAds){
                adViewExpress.setVisibility(View.GONE);
                return;
            }
            //adViewExpress.setVisibility(View.VISIBLE);

            adViewExpress.setAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    //loadError = true;
                    if (log>1) Toast.makeText(act, "Express Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                    //((ActivityTemplate)act).showAdMobExpressNativeAd();
                    //((ActivityTemplate)act).loadAds();
                }

                public void onAdLoaded(){
                    //loadError = true;//false;
                    if (log>1) Toast.makeText(act, "Express native loaded!", Toast.LENGTH_SHORT).show();
                    setAdsVisible(adViewExpress);
                    //ActivityTemplate ctxTemp = ((ActivityTemplate) ctx);
                    //ctxTemp.nativeAdContainer.setVisibility(View.VISIBLE);
                }
            });
            if (adViewExpress.getAdSize() == null)
                adViewExpress.setAdSize(AdSize.MEDIUM_RECTANGLE);
            if (adViewExpress.getAdUnitId() == null)
                adViewExpress.setAdUnitId(getString(R.string.admob_express_small_id));
            adViewExpress.loadAd(new AdRequest.Builder().build());
        }
    }
    // ------------------------------ AdMob inter -----------------------------

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

    public void setAdsVisible(View viewToShow){
        if (adView!=null)
            //adView.setVisibility(View.GONE);
            adView.setVisibility(View.VISIBLE);
        if (nativeAdContainer!=null)
            //nativeAdContainer.setVisibility(View.GONE);
            nativeAdContainer.setVisibility(View.VISIBLE);
        if (adViewExpress!=null)
            //adViewExpress.setVisibility(View.GONE);
            adViewExpress.setVisibility(View.VISIBLE);
        if (fbBannerContainer!=null)
            //nativeAdContainer.setVisibility(View.GONE);
            fbBannerContainer.setVisibility(View.VISIBLE);
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
    }

    public void setAdsVisible(boolean show){
        if (adView!=null)
            if (!show) adView.setVisibility(View.GONE);
            else adView.setVisibility(View.VISIBLE);
        if (nativeAdContainer!=null)
            if (!show) nativeAdContainer.setVisibility(View.GONE);
            else nativeAdContainer.setVisibility(View.VISIBLE);
        if (adViewExpress!=null)
            if (!show) adViewExpress.setVisibility(View.GONE);
            else adViewExpress.setVisibility(View.VISIBLE);
        if (fbBannerContainer!=null)
            if (!show) nativeAdContainer.setVisibility(View.GONE);
            else fbBannerContainer.setVisibility(View.VISIBLE);
        /*
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
            */
    }

    protected void setUpAdLayouts(){
        amazonAd = (AdLayout) findViewById(R.id.amazonAdView);
        nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
        adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
        //adView = (com.google.android.gms.ads.AdView) findViewById(R.id.disc_adView);
        adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
        fbBannerContainer = (FrameLayout) findViewById(R.id.fb_banner_ad);
    }

    public void onAdLoadError(){
        adsHandle.showAds();
    }

    // ---------------------- general stuff, not related to showing ads --------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the activity_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        menuHelp.handleOnItemSelected(this, item);
        return true;

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    // This is for Google Analytics
    synchronized public Tracker getDefaultTracker(Activity activity) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(activity);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            String ga_tracker = getString(R.string.ga_trackingId);
            mTracker = analytics.newTracker(ga_tracker);
            //R.xml.global_tracker);
            //"UA-45019697-11");
            //R.xml.global_tracker);
        }
        return mTracker;
    }
    public void toastMsg(int stringId, int level){

        String msg = getResources().getString(stringId);
        if (level<log)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, msg);
    }

    public void toastMsg(String str, int level){
        if (level<log)
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        Log.d(TAG, str);
    }

    private void sendEmail(String msg){
        //nutrInfo.sendEmail(this, msg);
    }

    public void Update(){

    }

        /*
    public void updateTheme(Activity activity) {
        // TODO Auto-generated method stub
        int themeId = menuHelp.dialogSymbol.getThemeId();
        int selectionId = menuHelp.dialogSymbol.getSelectionId();
        //getApplication().setTheme(themeId);
        //ThemeUtils.changeToTheme(this, selectionId);
        ThemeUtils.changeToTheme(activity, selectionId);
    }*/

    String extractValue(EditText et){
        return et.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
    }

    String formatNumber(double number){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formated = numberFormat.format(number);
        return formated;
    }

}
