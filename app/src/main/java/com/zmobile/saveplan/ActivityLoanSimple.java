package com.zmobile.saveplan;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.analytics.tracking.android.EasyTracker;
import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.*;
//import com.zmobile.payplan.ActivityDeposit.SymbolListner;
//import com.zmobile.payplan.R;


public class ActivityLoanSimple extends ActivityTemplate implements OnClickListener, OnFocusChangeListener,
						Updatable {
	
	Button button1;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editKwota;
	EditText editProcent;
	EditText editOkres;	
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	String mSymbol = " ";
	Spinner ddSymbol;
	//SymbolListner symbolListner = new SymbolListner();
	TextView textView5;
	TextView textView8;
	TextView tvCostPercent;
	TextView tvSymbol1;
	TextView tvSymbol2;
	TextView tvSymbol3;
	LinearLayout layResult;
	DialogSymbol dialogSymbol;
	AlertDialog dialog;
	//private InterstitialAd interstitial;
	//AdRequest adRequest;
	//AdView adView;
	SharedPreferences settings;
	MenuHelper menuHelp;
	//int ads;
	//HandlerPurchase handlerIAP;
	
	//View MainView = (View) findViewById(R.layout.activity_main);
    int clBackground = Color.rgb(155, 155, 0);
    int clInputField = Color.rgb(255, 255, 255);
    
    public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		/*
		if (ads==0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);
		else
			adView.loadAd(adRequest);*/
	    //editKwota.requestFocus();
	    focusedView = editKwota;
	    //Update();
		mSymbol = settings.getString("symbol", "$");
		tvSymbol1.setText(mSymbol);
		//tvSymbol2.setText(mSymbol);
		//tvSymbol3.setText(mSymbol);
	}    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_loan_calc);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		fb_adView_end = (FrameLayout) findViewById(fb_adView_end_id);
		fb_adView_front = (FrameLayout) findViewById(fb_adView_front_id);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		adView = (AdView) findViewById(R.id.loan_adView);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		TAG = this.getClass().getSimpleName();
		/*
		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_front);

		//fb_adView_front.setVisibility(View.GONE);
		fb_adView_end.setVisibility(View.VISIBLE);

		nativeAdContainer = fb_adView_end;


		//AdRequestBuilder AdBuilder = new AdRequestBuilder();
		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();*/

		/*
		handlerIAP = HandlerPurchase.getInstance(this);
		if (ads==0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);
		*/
		layResult = (LinearLayout) findViewById(R.id.layResult);
		button1 = (Button) findViewById(R.id.button1);
		
		editKwota = (EditText) findViewById(R.id.editText1);
		editProcent = (EditText) findViewById(R.id.editText2);
		editOkres = (EditText) findViewById(R.id.editText3);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView8 = (TextView) findViewById(R.id.textView8);
		tvCostPercent = (TextView) findViewById(R.id.textView10);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		//tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		//tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		editList.add(editOkres);
		editList.add(editKwota);		
		editList.add(editProcent);
		
		for (EditText view : editList){
			view.setOnFocusChangeListener(this);
			if (view!=editProcent && view!=editOkres)
				view.addTextChangedListener(new NumberTextWatcher(view));
		}		
		
		/*
		editText1.setBackgroundColor(clInputField);
		editText2.setBackgroundColor(clInputField);
		editText3.setBackgroundColor(clInputField);		
		MainView.setBackgroundColor(clBackground);
		*/
		button1.setOnClickListener(this);
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		
		/*
		// spinner symbol
		ddSymbol = (Spinner) findViewById(R.id.ddSymbol);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterSymbol = ArrayAdapter.createFromResource(this,
		        R.array.symbols, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterSymbol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddSymbol.setAdapter(adapterSymbol);		
		ddSymbol.setOnItemSelectedListener(symbolListner);
		ddSymbol.setSelection(1);		
		*/
		dialogSymbol = DialogSymbol.getInstance(this);
		//dialogSymbol = new DialogSymbol(this);
		//menuHelp = new MenuHelper(this);
		menuHelp = MenuHelper.getInstance(this);

		//createAndLoadNativeAd();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (arg0.getId() == R.id.bDel){
			((EditText)focusedView).setText("");
	        if (imm != null) {
	            imm.showSoftInput(focusedView,0);
	        }
	        layResult.setVisibility(View.GONE);
		}		
		if (arg0.getId() == R.id.bClear){
			editKwota.setText("");
			editProcent.setText("");			
			editOkres.setText("");
			//editKwota.setSelected(true);
			editKwota.requestFocus();
	        if (imm != null) {
	            imm.showSoftInput(editKwota,0);
	        }	
	        layResult.setVisibility(View.GONE);
		}	
		
		if (arg0.getId() == R.id.button1){
			String kwotaStr = editKwota.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
			String procentStr = editProcent.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
			String okresStr = editOkres.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
			
			String msg = getResources().getString(R.string.fill_in);
			
			if (kwotaStr.equals("") || procentStr.equals("") ||
					okresStr.equals("") ){
				//Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
				toastMsg(msg,6);
				return;
			}
			layResult.setVisibility(View.VISIBLE);
			double kwota = Double.parseDouble(kwotaStr);
			double procent = Double.parseDouble(procentStr) / 100;
			double okres = Double.parseDouble(okresStr);
			double q = 1 + procent / 12;
			double qn = Math.pow(q, okres);
			double rata = kwota * qn * (1 - q) / (1 - qn);
			double totalAmount = rata * okres - kwota;
			double cost_percent = 100*totalAmount/kwota;
			
			String currency = getString(R.string.currency);
			String pre_currency = getString(R.string.pre_currency);
			if (mSymbol == null) mSymbol = " ";

			textView5.setText(formatNumber(rata)+" "+mSymbol);
			textView8.setText(formatNumber(totalAmount)+" "+mSymbol);
			tvCostPercent.setText(formatNumber(cost_percent)+" %");

			/*
			textView5.setText(pre_currency+String.format("%.2f ", rata)+mSymbol);
			textView8.setText(pre_currency+String.format("%.2f ", totalAmount)+mSymbol);
			tvCostPercent.setText(String.format("%.2f", cost_percent)+" %");
			*/
			
		}
	}
	
	@Override
    public void onFocusChange(View v, boolean hasFocus){
          if(hasFocus){
          focusedView = v;
        } else {
            focusedView  = null;
        }
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
	
	@Override
	public void Update(){
		mSymbol = dialogSymbol.getSymbol();
		tvSymbol1.setText(mSymbol);
		//tvSymbol2.setText(mSymbol);
		//tvSymbol3.setText(mSymbol);		
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("symbol", mSymbol);
		// Commit the edits!
		editor.commit();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		adView.pause();
		super.onPause();		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		adView.resume();
	}	
	  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//removeAdView();    
		adView.destroy();
		menuHelp.dispose();
		super.onDestroy();		              
	}

	public void setAdVisible(boolean show){
		if (show)
			adView.setVisibility(View.VISIBLE);
		else
			adView.setVisibility(View.GONE);
	}
	/*
	class SymbolListner implements OnItemSelectedListener  {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int pos, long arg3) {
			
			mSymbol = ddSymbol.getItemAtPosition(pos).toString();
			tvSymbol1.setText(mSymbol);
			//tvSymbol2.setText(mSymbol);
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}		
	}	
	*/
}