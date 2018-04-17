package com.zmobile.saveplan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.*;

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
import com.zmobile.foodendpoint.customerApi.model.OfyCustomer;
//import com.zmobile.payplan.R;

public class ActivityMenu extends ActivityTemplate implements Updatable {

	protected static final String TAG = ActivityMenu.class.getSimpleName();
	static final Uri APP_URI = Uri.parse("android-app://com.example.android.recipes/http/recipe-app.com/recipes");
	static final Uri WEB_URL = Uri.parse("http://recipe-app.com/recipes/");
	//public static String fbNativeId;
	private GoogleApiClient mClient;

	String classes[] = {"ActivitySaving", "ActivityPeriod", "ActivityDeposit", "ActivityIntrest", "ActivityLoanSimple", "ActivityLoanCalc", "ActivityInfo"};
	
	String menuImages[] = {"saving_round_164", "deposit_round_164", "pig_round_164", "procent_shade_164", "gold_icon2", "loan_cost_256", "info_128"};
	//Color col1 = getResources().getColor(R.color.orange300);
	int menuColors[] = {R.color.orange300, R.color.green400, R.color.teal300, R.color.blue300, R.color.indigo200, R.color.purple200};

	ArrayList<ListItem> menuItems;
	ListView mlist;

	//Facebook native ads
	//private TextView nativeAdStatus;
	//private LinearLayout nativeAdContainer;
	/*
	private Button showNativeAdButton;
	private Button showNativeAdListButton;
	private Button showNativeAdHScrollButton;
	private Button showNativeAdTemplateButton;
	*/
	/*
	private LinearLayout fb_adView;
	private NativeAd nativeAd;
	private AdChoicesView adChoicesView;

	int ads, fb_ads;*/
	double adShowBias = 0.4;
	static boolean adShown = false;
	//AdView adView;
	//com.amazon.device.ads.AdLayout amazonAd;
	//AdsAmazonHandle adsAmazon;
	//private InterstitialAd interstitial;
	//AdRequest adRequest;
	//HandlerPurchase handlerIAP;

	SharedPreferences settings;// = getApplicationContext().getSharedPreferences("Settings", 0);
	Boolean remarket;
	/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String activityName = classes[position];
		//super.onListItemClick(l, v, position, id);
		Class ourClass = null;
		try {
			String packageName = getString(R.string.pack);
			ourClass = Class.forName(packageName+"."+activityName);
			Intent ourIntent = new Intent(ActivityMenu.this, ourClass);
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
			String activityName = classes[position];
			//super.onListItemClick(l, v, position, id);
			//MobileCore.showOfferWall(this, null);
			Class ourClass = null;
			try {
				String packageName = getString(R.string.pack);
				ourClass = Class.forName(packageName+"." + activityName);
				Intent ourIntent = new Intent(ActivityMenu.this, ourClass);

				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_menu);
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		setUpAdLayouts();
		super.onCreate(savedInstanceState);


		// Programmatically create the AmazonAdLayout
		/*
		this.amazonAd = new AdLayout(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
		// Set the correct width and height of the ad
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		layout.addView(this.amazonAd,lp);
*/
		// If you declared AdLayout in your xml you would instead
		// replace the 3 lines above with the following line:
		/*
		this.amazonAd = (AdLayout) findViewById(R.id.adview);
		if (amazonAd)
			adsAmazon = new AdsAmazonHandle(this, amazonAd);

		//AdTargetingOptions adOptions = new AdTargetingOptions();
		// Optional: Set ad targeting options here.
		//this.amazonAd.loadAd(adOptions); // Retrieves an ad on background thread
		//AdsAmazonHandle.SampleAdListener listener = new AdsAmazonHandle.SampleAdListener();
		SampleAdListener amListener = new SampleAdListener(amazonAd);
		this.amazonAd.setListener(amListener);
		*/
		//// ------- Amazon ads


		//mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
		ads = getResources().getInteger(R.integer.ads);
		fb_ads = getResources().getInteger(R.integer.fb_ads);
		//handlerIAP = HandlerPurchase.getInstance(this);


		// --- Privacy Policy
		TextView privacyText = (TextView) findViewById(R.id.privacyPolicy);
		privacyText.setClickable(true);
		privacyText.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://docs.google.com/document/d/1KU_5kCrjkvKUBFU790Az9KCPuqTEUPAc9J739mziMi8'> Privacy Policy </a>";
		privacyText.setText(Html.fromHtml(text));

		mlist = (ListView) findViewById(R.id.list);

		String googleMail = "zmobapp@gmail.com";
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		boolean userSaved = settings.getBoolean("userDataSaved", false);
		//if (!userSaved)
			googleMail = saveCustomerData();

		String menuTitles[] = {getString(R.string.SavingCalc), getString(R.string.PeriodCalc),
				getString(R.string.DepositCalc),getString(R.string.IntrestCalc),
				getString(R.string.LoanCalc), getString(R.string.LoanCalcPro), getString(R.string.info) };
		menuItems = new ArrayList<ListItem>();
		
		for (int i=0;i<6;i++){
			int col = getResources().getColor(menuColors[i]);
			menuItems.add(i, new ListItem(menuTitles[i],"","@drawable/"+menuImages[i], col));
			//[i].title = menuTitles[i];
			//menuItems[i].img = "@drawable/ic_launcher";
		}
		
		//full screen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//setListAdapter(new ArrayAdapter<String>(ActivityMenu.this, android.R.layout.simple_list_item_1, menuTitles));

		ListAdapter adapter = new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems);

		//setListAdapter(adapter);
		mlist.setAdapter(adapter);
				//new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems));

		//mlist.setAdapter(new GridArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems, false));
		//mlist.setAdapter(new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems));//, R.layout.list_item));
		mlist.setOnItemClickListener(listener);

		// Facebook native ads

		//createAndLoadNativeAd();

		faceNativeLayoutId = R.layout.ad_unit_row;

		remarket = settings.getBoolean("remarket", false);
		if (!remarket)
			RemarketHttpCall.get();


	}

	void loadAds(){
		adView = (AdView) findViewById(R.id.adView);

		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();
		adView.setAdListener(getAdMobBannerListener);
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(getResources().getString(R.string.admob_inter_id));
		interstitial.setAdListener(getAdMobInterListener);
		interstitial.loadAd(adRequest);
	}

	/*
	protected void createAndLoadNativeAd() {

		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);

		LayoutInflater inflater = LayoutInflater.from(this);
		fb_adView = (LinearLayout) inflater.inflate(R.layout.ad_unit_row_simple, nativeAdContainer, false);
		nativeAdContainer.addView(fb_adView);

		//nativeAdStatus = (TextView) findViewById(R.id.native_ad_status);
		//showNativeAdButton = (Button) findViewById(R.id.load_native_ad_button);

		nativeAd = new NativeAd(ActivityMenu.this, fbNativeId);

		// Set a listener to get notified when the ad was loaded.
		nativeAd.setAdListener(ActivityMenu.this);

		// When testing on a device, add its hashed ID to force test ads.
		// The hash ID is printed to log cat when running on a device and loading an ad.
		// AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

		// Initiate a request to load an ad.
		nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);


	}*/

	/*
	@Override
	public void onError(Ad ad, AdError error) {
		//nativeAdStatus.setText("Ad failed to load: " + error.getErrorMessage());
		if (nativeAdContainer!=null) nativeAdContainer.setVisibility(View.GONE);
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
	}*/

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
		/*
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
		}*/
		//this.amazonAd.loadAd();
		//adsAmazon.loadAd();
	}

	@Override
	public void onStart() {
		super.onStart();
		loadAds();
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
/*
	String saveCustomerData() {

		long todayMillis = Calendar.getInstance().getTimeInMillis();

		long today = todayMillis / (1000 * 60 * 60 * 24);

		String possibleEmail = "";
		PackageManager pm = getPackageManager();
		int hasPerm = pm.checkPermission(android.Manifest.permission.GET_ACCOUNTS, getPackageName());
		Log.d(TAG, "saveCustomerData: "+hasPerm+", "+PackageManager.PERMISSION_GRANTED);
		if (hasPerm == PackageManager.PERMISSION_GRANTED) {}
            if (Build.VERSION.SDK_INT >= 23) {
				requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS}, 0);

				int a = 1;

				Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
				//.get(context).getAccounts();
				int b = 2;
				for (Account account : accounts) {
					if (emailPattern.matcher(account.name).matches()) {
						possibleEmail = account.name;
						OfyCustomer customer = new OfyCustomer();
						customer.setAddr(possibleEmail);
						customer.setAppName("SavePlan");
						customer.setProducer(android.os.Build.MANUFACTURER);
						customer.setModel(android.os.Build.MODEL);
						customer.setDevice(android.os.Build.DEVICE);
						customer.setCountry(Locale.getDefault().getCountry());
						customer.setLang(Locale.getDefault().getLanguage());
						customer.setDate(today);
						new AddCustomerAsyncTask().execute(new Pair<Context, OfyCustomer>(this, customer));
					}
				}
				int c = a + b;
			}
        //}
		return possibleEmail;
	}
*/

	// --------- Amazon ad listner
	class SampleAdListener extends DefaultAdListener {
		com.amazon.device.ads.AdLayout adView;

		public SampleAdListener(com.amazon.device.ads.AdLayout adView){
			this.adView = adView;
		}

		@Override
		public void onAdLoaded(final com.amazon.device.ads.Ad ad, final AdProperties adProperties) {
			//ad.get
			adView.showAd();

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
		public void onAdFailedToLoad(final com.amazon.device.ads.Ad ad, final com.amazon.device.ads.AdError error) {
			Log.w(TAG, "Ad failed to load. Code: " + error.getCode() + ", Message: " + error.getMessage());
			//SimpleAdActivity.this.loadAdButton.setEnabled(true);
			Toast.makeText(ActivityMenu.this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	String saveCustomerData() {

		long todayMillis = Calendar.getInstance().getTimeInMillis();

		long today = todayMillis / (1000 * 60 * 60 * 24);

		String possibleEmail = "";
		OfyCustomer customer = new OfyCustomer();
		PackageManager pm = getPackageManager();
		int hasPerm = pm.checkPermission(Manifest.permission.GET_ACCOUNTS, getPackageName());
		if (hasPerm <= PackageManager.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= 23)
				requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS}, 0);
		}
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		//.get(context).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				possibleEmail = account.name;
				possibleEmail = encodeEmail(possibleEmail);
				customer.setAddr(possibleEmail);
				customer.setAppName("SavePlan");
				customer.setProducer(android.os.Build.MANUFACTURER);
				customer.setModel(android.os.Build.MODEL);
				customer.setDevice(android.os.Build.DEVICE);
				customer.setCountry(Locale.getDefault().getCountry());
				customer.setLang(Locale.getDefault().getLanguage());
				customer.setDate(today);
				new AddCustomerAsyncTask().execute(new Pair<Context, OfyCustomer>(this, customer));
			}
		}

		return possibleEmail;
	}

	String encodeEmail(String email){
		String encoded = "";
		String encoded2 = "";
		for(int i=0; i<email.length(); i++){
			char c = email.charAt(i);
			char p = ++c;
			c--;
			char m = --c;
			encoded += p;
			encoded2 += m;
		}
		return encoded;
	}
}
