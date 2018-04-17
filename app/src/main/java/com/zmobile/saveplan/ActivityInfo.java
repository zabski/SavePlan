package com.zmobile.saveplan;

/*import com.appigniter.android.AppIgniterAdLayout;
import com.google.analytics.tracking.android.EasyTracker;
import com.ironsource.mobilcore.MobileCore;
*/
import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//import com.zmobile.payplan.R;

public class ActivityInfo extends ActivityTemplate implements OnClickListener{
	
	Button bEnterData;	
	
	private InterstitialAd interstitial;	
	AdRequest adRequest;		
	//AdView adView;
	//AdView xml_adView;
	//AdView adView2;
	//AdView xml_adView2;
	MenuHelper menuHelp;
	DialogSymbol dialogSymbol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_info);
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		super.onCreate(savedInstanceState);

		TAG = this.getClass().getSimpleName();
		//xml_adView = (AdView) findViewById(R.id.info_adView);
		//new_adView = (AdView) findViewById(R.id.new_adView);
		adView = (AdView) findViewById(R.id.info_adView2);

		/*
		adView = new AdView(this);
	    adView.setAdSize(AdSize.FULL_BANNER);
	    adView.setAdUnitId("ca-app-pub-4402674240600002/3394793775");
		adView2 = new AdView(this);
	    adView2.setAdSize(AdSize.SMART_BANNER);
	    adView2.setAdUnitId("ca-app-pub-4402674240600002/3394793775");
	    
		AdRequestBuilder AdBuilder = new AdRequestBuilder();	
		adRequest = AdBuilder.build();
		
	    LinearLayout layout = (LinearLayout) findViewById(R.id.infoTopLay);
	    //layout.addView(adView);
	    LinearLayout layout2 = (LinearLayout) findViewById(R.id.infoScroLay);
	    //layout2.addView(adView2);
	    //adView.loadAd(adRequest);
	    //adView2.loadAd(adRequest);
	    //xml_adView.loadAd(adRequest);
	    //xml_adView2.loadAd(adRequest);
		
		bEnterData = (Button)findViewById(R.id.bEnterData);   
		bEnterData.setOnClickListener(this);
		*/		
		
		dialogSymbol = DialogSymbol.getInstance(this);
		//dialogSymbol = new DialogSymbol(this);
		//menuHelp = new MenuHelper(this);
		menuHelp = MenuHelper.getInstance(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		menuHelp.handleOnItemSelected(this, item);
		return true;		
		
    } 


	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//startActivityForResult(new Intent(Intent.ACTION_MAIN).setDataAndType(null, SettingsActivity.MIME_TYPE), 100);
		//Intent intent = new Intent(this, ActivityMenu.class);
		//Intent intent = new Intent(Intent.ACTION_MAIN);
		//Intent intent = new Intent("com.zmobile.foodtest.ActivityMenu");
		//String action = "com.zmobile.foodtest.MAIN";//"android.intent.action.MAIN";
		//Intent intent = new Intent();//"android.intent.action.MAIN");
		//Intent intent = new Intent(ActivityInfo.this, ActivityMenu.class);
		//intent.setAction(action);
		//startActivity(intent);
		finish();
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    //adView.loadAd(adRequest);   
	}

	@Override
	public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}
	
    protected void onDestroy(){
        super.onDestroy();
        //AppIgniterAdLayout.clean();
    }	

	@Override
	public void onRestart() {
	    super.onRestart();
	    //MobileCore.showStickee(this);
	}
    
}
