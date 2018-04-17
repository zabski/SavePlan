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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.*;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.zmobile.payplan.ActivityDeposit.SymbolListner;
//import com.zmobile.payplan.R;


public class ActivityPeriod extends ActivityTemplate implements OnClickListener, OnItemSelectedListener,
							OnFocusChangeListener, Updatable {
	
	Button bOblicz;
	Button bClear;
	Button bDel;
	EditText editWplata;
	EditText editKwota;
	EditText editFuture;
	//EditText editKapital;
	EditText editProcent;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent}; 
	TextView tvYears;
	TextView tvMonths;
	TextView tvDays;	
	TextView tvSymbol1;
	TextView tvSymbol2;
	TextView tvSymbol3;
	Spinner ddownKapital;
	Spinner ddownOkres;
	View focusedView;
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
	private InterstitialAd interstitial;	
	AdRequest adRequest;		
	//AdView adView;
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
		  if (ads==0 || handlerIAP.mHasRemovedAds)
			  adView.setVisibility(View.GONE);
		  else
			  adView.loadAd(adRequest);
			  */

	    //editKwota.requestFocus();
	    focusedView = editKwota;
	    //Update();
		mSymbol = settings.getString("symbol", "$");
		tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);
		tvSymbol3.setText(mSymbol);
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_period_calc);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		fb_adView_end = (FrameLayout) findViewById(fb_adView_end_id);
		fb_adView_front = (FrameLayout) findViewById(fb_adView_front_id);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		adView = (AdView) findViewById(R.id.period_adView);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();

		/*
		fb_adView_end.setVisibility(View.VISIBLE);

		nativeAdContainer = fb_adView_end;


		//AdRequestBuilder AdBuilder = new AdRequestBuilder();
		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();
		ads = getResources().getInteger(R.integer.ads);*/
		/*
		handlerIAP = HandlerPurchase.getInstance(this);
		if (ads==0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);
			*/
		layResult = (LinearLayout) findViewById(R.id.layResult);
		bOblicz = (Button) findViewById(R.id.bOblicz);
		bClear = (Button) findViewById(R.id.bClear);
		editWplata = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		editFuture = (EditText) findViewById(R.id.editPeriod);
		//editKapital = (EditText) findViewById(R.id.editKapital);
		editProcent = (EditText) findViewById(R.id.editProcent);
		tvYears = (TextView) findViewById(R.id.textDeposit);
		tvMonths = (TextView) findViewById(R.id.textMonths);
		tvDays = (TextView) findViewById(R.id.tvDays);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);

		bOblicz.setOnClickListener(this);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		
		editList.add(editWplata);
		editList.add(editKwota);
		editList.add(editFuture);
		editList.add(editProcent);
		
		for (EditText view : editList){
			view.setOnFocusChangeListener(this);
			if (view!=editProcent)
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
		ddownKapital.setOnItemSelectedListener(this);
		ddownKapital.setSelection(2);
		
		/*
		 * ddownOkres = (Spinner) findViewById(R.id.ddownOkres);
		 
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterOkres = ArrayAdapter.createFromResource(this,
		        R.array.okres, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterOkres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownOkres.setAdapter(adapterOkres);
				ddownOkres.setOnItemSelectedListener(periodListner);
		ddownOkres.setSelection(1);
		*/		
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
			editFuture.setText("");
			editWplata.setText("");
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
		String kwotaStr = extractValue(editKwota);//.getText().toString().replaceAll("\\s", "");
		String procentStr = extractValue(editProcent);//.getText().toString().replaceAll("\\s", "");
		String futureStr = extractValue(editFuture);//.getText().toString().replaceAll("\\s", "");
		String wplataStr = extractValue(editWplata);//.getText().toString().replaceAll("\\s", "");
		
		String msg = getResources().getString(R.string.fill_in);
		
		if (kwotaStr.equals("") || procentStr.equals("") ||
				futureStr.equals("") || wplataStr.equals("")){
			//Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			toastMsg(msg,6);
			return;
		}
		layResult.setVisibility(View.VISIBLE);
		double wplata = Double.parseDouble(wplataStr);
		double kwota = Double.parseDouble(kwotaStr);
		double procent = Double.parseDouble(procentStr) / 100;
		double future = Double.parseDouble(futureStr);
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
		
		//double futureValue = principal * Math.pow(1+i,n);
		double periods;
		if (i == 0)
			periods = (future - principal)/pmt;
		else				
			periods = Math.log((pmt + future*i)/(pmt + principal*i))/Math.log(1+i);
		double compoundPerMonth = compoundsPerYear/12;
		double compoundPerDay = compoundsPerYear/365;
		double months = periods/compoundPerMonth;
		double days = periods/compoundPerMonth;
		
		int years = (int)(months/12);
		int monthsLeft = (int)Math.floor(months % 12);
		
		double monthRest = months - years*12- monthsLeft;
		int daysLeft = (int)Math.round(monthRest*30.5);
		
		//futureValue += pmt/i*(Math.pow(1+i, n)-1);		
		//double zysk = futureValue - principal - n*pmt;
		
		String currency = getString(R.string.currency);
		String pre_currency = getString(R.string.pre_currency);
		
		tvYears.setText(Integer.toString(years));
		tvMonths.setText(Integer.toString(monthsLeft));
		tvDays.setText(Integer.toString(daysLeft));
		
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
		tvSymbol3.setText(mSymbol);		
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
	}	
	  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//removeAdView();    
		//adView.destroy();
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
			tvSymbol2.setText(mSymbol);
			tvSymbol3.setText(mSymbol);
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}		
	}	
	*/
}
