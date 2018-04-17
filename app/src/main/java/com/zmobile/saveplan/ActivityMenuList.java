package com.zmobile.saveplan;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
//import com.zmobile.payplan.R;

public class ActivityMenuList extends ListActivity implements AdListener {

	protected static final String TAG = ActivityMenuList.class.getSimpleName();
	static final Uri APP_URI = Uri.parse("android-app://com.example.android.recipes/http/recipe-app.com/recipes");
	static final Uri WEB_URL = Uri.parse("http://recipe-app.com/recipes/");
	public static String fbNativeId;
	private GoogleApiClient mClient;

	String classes[] = {"ActivitySaving", "ActivityPeriod", "ActivityDeposit", "ActivityIntrest", "ActivityLoanSimple", "ActivityLoanCalc", "ActivityInfo"};
	
	String menuImages[] = {"saving_round_164", "deposit_round_164", "pig_round_164", "procent_shade_164", "gold_icon2", "loan_cost_256", "info_128"};
	int menuColors[] = {R.color.orange300, R.color.green400, R.color.teal300, R.color.blue300, R.color.purple200, R.color.indigo200};
	ArrayList<ListItem> menuItems;

	//Facebook native ads
	//private TextView nativeAdStatus;
	private FrameLayout nativeAdContainer;
	/*
	private Button showNativeAdButton;
	private Button showNativeAdListButton;
	private Button showNativeAdHScrollButton;
	private Button showNativeAdTemplateButton;
	*/
	private LinearLayout fb_adView;
	private NativeAd nativeAd;
	private AdChoicesView adChoicesView;

	int ads, fb_ads;
	double adShowBias = 0.4;
	static boolean adShown = false;
	AdView adView;
	private InterstitialAd interstitial;
	AdRequest adRequest;
	HandlerPurchase handlerIAP;

	SharedPreferences settings;// = getApplicationContext().getSharedPreferences("Settings", 0);
	Boolean remarket;
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String activityName = classes[position];
		super.onListItemClick(l, v, position, id);
		Class ourClass = null;
		try {
			String packageName = getString(R.string.pack);
			ourClass = Class.forName(packageName+"."+activityName);
			Intent ourIntent = new Intent(ActivityMenuList.this, ourClass);
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		//mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
		ads = getResources().getInteger(R.integer.ads);
		fb_ads = getResources().getInteger(R.integer.fb_ads);
		handlerIAP = HandlerPurchase.getInstance(this);
		fbNativeId = getResources().getString(R.string.facebook_native_id);

		String menuTitles[] = {getString(R.string.SavingCalc), getString(R.string.PeriodCalc),
				getString(R.string.DepositCalc),getString(R.string.IntrestCalc),
				getString(R.string.LoanCalc), getString(R.string.LoanCalcPro), getString(R.string.info) };
		menuItems = new ArrayList<ListItem>();
		
		for (int i=0;i<6;i++){
			int col = getResources().getColor(menuColors[i]);
			menuItems.add(i, new ListItem(menuTitles[i],"","@drawable/"+menuImages[i],col));
			//[i].title = menuTitles[i];
			//menuItems[i].img = "@drawable/ic_launcher";
		}
		
		//full screen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//setListAdapter(new ArrayAdapter<String>(ActivityMenu.this, android.R.layout.simple_list_item_1, menuTitles));

		ListAdapter adapter = new ImageArrayAdapter(ActivityMenuList.this, android.R.layout.simple_list_item_1, menuItems);

		setListAdapter(adapter);
				//new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems));

		// Facebook native ads

		createAndLoadNativeAd();

		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		remarket = settings.getBoolean("remarket", false);
		if (!remarket)
			RemarketHttpCall.get();
	}

	protected void createAndLoadNativeAd() {

		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);

		LayoutInflater inflater = LayoutInflater.from(this);
		fb_adView = (LinearLayout) inflater.inflate(R.layout.ad_unit_no_img, nativeAdContainer, false);
		nativeAdContainer.addView(fb_adView);

		//nativeAdStatus = (TextView) findViewById(R.id.native_ad_status);
		//showNativeAdButton = (Button) findViewById(R.id.load_native_ad_button);
		/*
		showNativeAdListButton = (Button) findViewById(R.id.load_native_ad_list_button);
		showNativeAdHScrollButton = (Button) findViewById(R.id.load_native_ad_hscroll);
		showNativeAdTemplateButton = (Button) findViewById(R.id.load_native_ad_template_button);
		*/
		nativeAd = new NativeAd(ActivityMenuList.this, fbNativeId);

		// Set a listener to get notified when the ad was loaded.
		nativeAd.setAdListener(ActivityMenuList.this);

		// When testing on a device, add its hashed ID to force test ads.
		// The hash ID is printed to log cat when running on a device and loading an ad.
		// AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

		// Initiate a request to load an ad.
		nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);


	}

	@Override
	public void onError(Ad ad, AdError error) {
		//nativeAdStatus.setText("Ad failed to load: " + error.getErrorMessage());
		if (nativeAdContainer!=null) nativeAdContainer.setVisibility(View.GONE);
	}

	@Override
	public void onAdClicked(Ad ad) {
		Toast.makeText(this, "Ad Clicked", Toast.LENGTH_SHORT).show();
	}

	public void onLoggingImpression(Ad ad) { }

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//ThemeUtils.onActivityCreateSetTheme(this);
		//setContentView(R.layout.main);
		if (ads > 0 && !handlerIAP.mHasRemovedAds) {
			if (ads > 1 && adView != null) {
				adView.loadAd(adRequest);
				adView.resume();
			}
			if (interstitial != null)
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

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		//EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		int sta = RemarketHttpCall.status;
		if (!remarket && sta==200) {
			remarket = true;
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("remarket",true);
			editor.commit();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void setAdVisible(boolean show){
		if (show) {
			adView.setVisibility(View.VISIBLE);
			nativeAdContainer.setVisibility(View.VISIBLE);
		}else {
			adView.setVisibility(View.GONE);
			nativeAdContainer.setVisibility(View.GONE);
		}
	}

	public void Update(){

	}
}
