package com.zmobile.saveplan;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.*;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.zmobile.payplan.ActivityDeposit.SymbolListner;
//import com.zmobile.payplan.R;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;


public class ActivitySaving extends ActivityTemplate implements OnClickListener, OnItemSelectedListener,
							OnFocusChangeListener, Updatable {
	
	Button bOblicz;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editWplata;
	EditText editKwota;
	EditText editOkres;
	//EditText editKapital;
	EditText editProcent;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	TextView textKwota;
	TextView textZysk;	
	TextView tvPercent;
	TextView tvTotalDepo;
	TextView tvSymbol1;
	TextView tvSymbol2;
	TextView tvSymbol3;
	Spinner ddownKapital;
	Spinner ddownOkres;
	LinearLayout layResult;
	DialogSymbol dialogSymbol;
	AlertDialog dialog;
	//double kapitalizacja;
	double compoundsPerYear = 1;
	double numMonthPeriod;
	double procentNetto;
	double okres;
	double timeUnitInYears = 1;
	public String listArray[] = { "Example1", "Example2", "Example3", "Example4", "Example5"};
	String mSymbol = " ";
	Spinner ddSymbol;
	//SymbolListner symbolListner = new SymbolListner();
	Listner periodListner = new Listner();
	//private InterstitialAd interstitial;
	//AdRequest adRequest;
	//AdView adView;
	//com.google.android.gms.ads.AdView adView;
	//private com.facebook.ads.AdView faceAdView;
	SharedPreferences settings;
	MenuHelper menuHelp;
	int ads;
	//HandlerPurchase handlerIAP;
	
	class Listner implements OnItemSelectedListener  {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			switch(pos){
			case 0:
				numMonthPeriod = 1;
				timeUnitInYears = 1.0/12.0;
				break;
			case 1:
				numMonthPeriod = 12;
				timeUnitInYears = 1;
				break;		
			}	
			Calculate();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	  @Override
	  public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		  /*
		  if (ads==0 || handlerIAP.mHasRemovedAds) {
			  adView.setVisibility(View.GONE);
			  faceAdView.setVisibility(View.GONE);
		  }else {
			  //adView.loadAd(adRequest);
			  faceAdView.loadAd();
		  }*/
	    //editKwota.requestFocus();
	    focusedView = editKwota;
	    //Update();
		mSymbol = settings.getString("symbol", "$");
		tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);
		//tvSymbol3.setText(mSymbol);
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_saving_calc);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		fb_adView_end = (FrameLayout) findViewById(fb_adView_end_id);
		fb_adView_front = (FrameLayout) findViewById(fb_adView_front_id);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		adView = (com.google.android.gms.ads.AdView) findViewById(R.id.save_adView);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		TAG = this.getClass().getSimpleName();

		//fb_adView_front.setVisibility(View.GONE);
		//fb_adView_end.setVisibility(View.VISIBLE);

		//nativeAdContainer = fb_adView_end;

		// Facebook Audeince Network
		/*
		RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
		String facebook_banner_id = getString(R.string.facebook_banner_id);
		faceAdView = new AdView(this, facebook_banner_id, AdSize.BANNER_320_50);
		adViewContainer.addView(faceAdView);*/
		//faceAdView.loadAd();
		// ------------------- */
		/*
		//AdRequestBuilder AdBuilder = new AdRequestBuilder();
		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();
		ads = getResources().getInteger(R.integer.ads);
		//handlerIAP = HandlerPurchase.getInstance(this);
		*/
        //interstitial = new InterstitialAd(this);
        //interstitial.setAdUnitId("ca-app-pub-4402674240600002/1319286972");
		bOblicz = (Button) findViewById(R.id.bOblicz);
		editWplata = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		editOkres = (EditText) findViewById(R.id.editPeriod);
		//editKapital = (EditText) findViewById(R.id.editKapital);
		editProcent = (EditText) findViewById(R.id.editProcent);
		textKwota = (TextView) findViewById(R.id.textDeposit);
		textZysk = (TextView) findViewById(R.id.tvProfit);
		tvPercent = (TextView) findViewById(R.id.tvPercent);
		tvTotalDepo = (TextView) findViewById(R.id.tvTotalDepo);
		layResult = (LinearLayout) findViewById(R.id.layResult);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		//tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);
		/*
		editKwota.addTextChangedListener(new NumberTextWatcher(editKwota));
		editKwota.addTextChangedListener(new NumberTextWatcher(editKwota));
		editOkres.addTextChangedListener(new NumberTextWatcher(editOkres));
		editProcent.addTextChangedListener(new NumberTextWatcher(editProcent));*/
		bOblicz.setOnClickListener(this);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		editList.add(editWplata);
		editList.add(editKwota);
		editList.add(editOkres);
		editList.add(editProcent);
		
		for (EditText view : editList){
			view.setOnFocusChangeListener(this);
			if (view!=editProcent && view!=editOkres)
				view.addTextChangedListener(new NumberTextWatcher(view));
		}		
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		//ddlistOkres.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray));
		
		ddownKapital = (Spinner) findViewById(R.id.ddownKapital);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterKapital = ArrayAdapter.createFromResource(this,
		        R.array.kapitalizacja, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterKapital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownKapital.setAdapter(adapterKapital);
		
		ddownOkres = (Spinner) findViewById(R.id.ddownOkres);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterOkres = ArrayAdapter.createFromResource(this,
		        R.array.okres, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterOkres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownOkres.setAdapter(adapterOkres);
		
		ddownKapital.setOnItemSelectedListener(this);
		ddownKapital.setSelection(2);
		ddownOkres.setOnItemSelectedListener(periodListner);
		ddownOkres.setSelection(1);
		
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
		menuHelp = MenuHelper.getInstance(this);
		//menuHelp = new MenuHelper(this);

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
			editWplata.setText("");
			editOkres.setText("");
			//editKwota.setSelected(true);
			editKwota.requestFocus();
			//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);	        
	        if (imm != null) {
	            imm.showSoftInput(editKwota,0);
	        }
	        layResult.setVisibility(View.GONE);
		}	
		if (arg0.getId() == R.id.bOblicz){
			
			Calculate();
		}
		
	}
	
	private void Calculate(){
		String kwotaStr = editKwota.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		String procentStr = editProcent.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		String okresStr = editOkres.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		String wplataStr = editWplata.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		
		String msg = getString(R.string.fill_in);
		
		if (kwotaStr.equals("") || procentStr.equals("") ||
				okresStr.equals("") || wplataStr.equals("")){
			//Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			toastMsg(msg,6);
			return;
		}
		layResult.setVisibility(View.VISIBLE);
		double wplata = Double.parseDouble(wplataStr);
		double kwota = Double.parseDouble(kwotaStr);
		double procent = Double.parseDouble(procentStr) / 100;
		double okres = Double.parseDouble(okresStr);
		/*
		double q = 1 + procent / 12;
		double qn = Math.pow(q, okres);
		double rata = kwota * qn * (1 - q) / (1 - qn);
		double totalAmount = rata * okres;
		double podatek = 0.19;
		double suma = 0;
		double kapital = 0;
		*/			
		double principal = kwota; 
		
		double paymentPerYear = wplata*12;
		double paymentPerCompundPeriod = paymentPerYear/compoundsPerYear;
		
		
		/*
		for (int m = 1; m < okres*numMonthPeriod; m++){
			procentNetto = (Math.pow(1+(procent/(kapitalizacja*100)*(1-podatek/100)),kapitalizacja*okres)-1);
			suma = suma + wplata + suma*procentNetto;
			kapital = kapital + wplata;
		}
		
		double zysk = suma - kapital;
		*/
		
		double numberOfYears = okres*timeUnitInYears;
		double i = procent/compoundsPerYear;
		double n = numberOfYears*compoundsPerYear;
		double pmt = paymentPerCompundPeriod;
					
		
		double futureValue = principal * Math.pow(1+i,n);
		
		futureValue+=pmt/i*(Math.pow(1+i, n)-1);
		
		double zysk = futureValue - principal - n*pmt;
		
		double months = okres*numMonthPeriod;
		double totalDeposit = principal+wplata*months;
		double percent = 100*zysk/totalDeposit;
		
		String currency = getString(R.string.currency);
		String pre_currency = getString(R.string.pre_currency);
		if (mSymbol == null) mSymbol = " ";

		textKwota.setText(formatNumber(futureValue)+" "+mSymbol);
		textZysk.setText(formatNumber(zysk)+" "+mSymbol);
		tvPercent.setText(formatNumber(percent)+" %");
		tvTotalDepo.setText(formatNumber(totalDeposit)+" "+mSymbol);

		/*
		textKwota.setText(pre_currency+String.format("%.2f ", futureValue)+mSymbol);
		textZysk.setText(pre_currency+String.format("%.2f ", zysk)+mSymbol);
		tvPercent.setText(pre_currency+String.format("%.2f", percent)+" %");
		tvTotalDepo.setText(pre_currency+String.format("%.2f ", totalDeposit)+mSymbol);
		*/
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		/*
		switch (view.getId()) {
		case R.id.ddownKapital:*/
			switch(pos){
			case 0:
				compoundsPerYear = 365;
				break;
			case 1:
				compoundsPerYear = 52;
				break;
			case 2:
				compoundsPerYear = 12;
				break;
			case 3:
				compoundsPerYear = 4;
				break;
			case 4:
				compoundsPerYear = 2;
				break;				
			case 5:
				compoundsPerYear = 1;
				break;				
			}				
			/*
			break;
		case R.id.ddownOkres:
			switch(pos){
			case 1:
				numMonthPeriod = 1;
				timeUnitInYears = 1/12;
				break;
			case 2:
				numMonthPeriod = 12;
				timeUnitInYears = 1;
				break;		
			}				
			break;
		}*/
		Calculate();			
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
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
		tvSymbol2.setText(mSymbol);		
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
		//adView.pause();
		super.onPause();		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		//adView.resume();
		//if (ads<=1) faceAdView.setVisibility(View.GONE);
	}	
	  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//removeAdView();    
		//adView.destroy();
		menuHelp.dispose();
		super.onDestroy();		              
	}
	/*
	public void setAdVisible(boolean show){
		if (show) {
			adView.setVisibility(View.VISIBLE);
			faceAdView.setVisibility(View.VISIBLE);
		}else {
			adView.setVisibility(View.GONE);
			faceAdView.setVisibility(View.GONE);
		}
	}*/
	/*
	class SymbolListner implements OnItemSelectedListener  {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int pos, long arg3) {
			
			mSymbol = ddSymbol.getItemAtPosition(pos).toString();
			tvSymbol1.setText(mSymbol);
			tvSymbol2.setText(mSymbol);
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}		
	}	
	*/

}
