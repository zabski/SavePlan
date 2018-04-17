package com.zmobile.saveplan;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

//import com.google.analytics.tracking.android.EasyTracker;

import android.app.Service;
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
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.android.gms.ads.*;
import com.zmobile.util.SoftKeyboard;
//import com.zmobile.payplan.ActivityDeposit.SymbolListner;
//import com.zmobile.payplan.R;


public class ActivityIntrest extends ActivityTemplate implements OnClickListener,
					OnItemSelectedListener, OnFocusChangeListener, Updatable {
	
	Button bOblicz;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editTarget;
	EditText editKwota;
	EditText editOkres;
	//EditText editKapital;
	EditText editDeposit;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	TextView tvPercent;
	TextView textZysk;
	TextView tvGain;
	TextView tvTotalDepo;
	TextView tvSymbol1;
	TextView tvSymbol2;
	TextView tvSymbol3;
	Spinner ddownKapital;
	Spinner ddownOkres;
	LinearLayout layResult;
	//double kapitalizacja;
	double compoundsPerYear = 1;
	double numMonthPeriod;
	double procentNetto;
	double okres;
	double procent;
	double timeUnitInYears = 1;
	public String listArray[] = { "Example1", "Example2", "Example3", "Example4", "Example5"};
	DialogSymbol dialogSymbol;
	AlertDialog dialog;
	String mSymbol = " ";
	Spinner ddSymbol;
	//SymbolListner symbolListner = new SymbolListner();
	Listner periodListner = new Listner();
	private InterstitialAd interstitial;	
	AdRequest adRequest;		
	//AdView adView;
	SharedPreferences settings;
	MenuHelper menuHelper;
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
			menuHelper.dispose();
			super.onDestroy();		              
		}	  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_intrest_calc);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		fb_adView_end = (FrameLayout) findViewById(fb_adView_end_id);
		fb_adView_front = (FrameLayout) findViewById(fb_adView_front_id);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		adView = (AdView) findViewById(R.id.intr_adView);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		/*
		//Amazon ads
		AdRegistration.setAppKey("74f59c4ba04047aebfce945771db42ef");

		// Programmatically create the AmazonAdLayout

		this.amazonAd = new AdLayout(this);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout);
		// Set the correct width and height of the ad
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.addView(this.amazonAd,rlp);

		// If you declared AdLayout in your xml you would instead
		// replace the 3 lines above with the following line:
		//   this.adView = (AdLayout) findViewById(R.id.adview);

		AdTargetingOptions adOptions = new AdTargetingOptions();

		// Optional: Set ad targeting options here.
		this.amazonAd.loadAd(adOptions); // Retrieves an ad on background thread
		//// ------- Amazon ads
		*/

		TAG = this.getClass().getSimpleName();

		/*
		//fb_adView_front.setVisibility(View.GONE);
		fb_adView_end.setVisibility(View.VISIBLE);

		nativeAdContainer = fb_adView_end;

		//AdRequestBuilder AdBuilder = new AdRequestBuilder();
		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();
		ads = getResources().getInteger(R.integer.ads);*/
		/*
		handlerIAP = HandlerPurchase.getInstance(this);
		if (ads==0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);*/

		bOblicz = (Button) findViewById(R.id.bOblicz);
		editTarget = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		editOkres = (EditText) findViewById(R.id.editPeriod);
		//editKapital = (EditText) findViewById(R.id.editKapital);
		editDeposit = (EditText) findViewById(R.id.editDeposit);
		tvPercent = (TextView) findViewById(R.id.tvPercent);
		tvGain = (TextView) findViewById(R.id.tvGain);
		tvTotalDepo = (TextView) findViewById(R.id.tvTotalDepo);
		layResult = (LinearLayout) findViewById(R.id.layResult);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);
		/*
		editTarget.addTextChangedListener(new NumberTextWatcher(editTarget));
		editKwota.addTextChangedListener(new NumberTextWatcher(editKwota));
		editOkres.addTextChangedListener(new NumberTextWatcher(editOkres));
		editDeposit.addTextChangedListener(new NumberTextWatcher(editDeposit));
		*/
		//textZysk = (TextView) findViewById(R.id.textMonths);
		bOblicz.setOnClickListener(this);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		editList.add(editKwota);
		editList.add(editDeposit);
		editList.add(editOkres);
		editList.add(editTarget);


		for (EditText view : editList){
			view.setOnFocusChangeListener(this);
			view.addTextChangedListener(new NumberTextWatcher(view));
		}

		RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.mainLayout); // You must use your root layout
		InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

		SoftKeyboard softKeyboard;
		softKeyboard = new SoftKeyboard(mainLayout, im);
		softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

			@Override
			public void onSoftKeyboardHide() {
				// Code here
				//Toast.makeText(ActivityIntrest.this, "keyboard hidden", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSoftKeyboardShow() {
				// Code here
				//Toast.makeText(ActivityIntrest.this, "keyboard visible", Toast.LENGTH_SHORT).show();
			}
		});
		
		for (EditText view : editList){
			if (view!=editOkres)
			   view.setOnFocusChangeListener(this);
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
		menuHelper = MenuHelper.getInstance(this);
		//menuHelper = new MenuHelper(this);

		// Facebook native ads

		//createAndLoadNativeAd();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (arg0.getId() == R.id.bDel && focusedView != null){
			((EditText)focusedView).setText("");
	        if (imm != null) {
	            imm.showSoftInput(focusedView,0);
	        }
	        layResult.setVisibility(View.GONE);
		}		
		if (arg0.getId() == R.id.bClear){
			editKwota.setText("");
			editDeposit.setText("");
			editTarget.setText("");
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
		String depositStr = editDeposit.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		String okresStr = editOkres.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		String targetStr = editTarget.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		
		String msg = getResources().getString(R.string.fill_in);
		
		if (kwotaStr.equals("") || depositStr.equals("") ||
				okresStr.equals("") || targetStr.equals("")){
			//Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			toastMsg(msg,6);
			return;
		}
		layResult.setVisibility(View.VISIBLE);
		double target = Double.parseDouble(targetStr);
		double kwota = Double.parseDouble(kwotaStr);
		double deposit = Double.parseDouble(depositStr);
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
		
		double paymentPerYear = deposit*12;
		double paymentPerCompundPeriod = paymentPerYear/compoundsPerYear;			
		
		double months = okres*numMonthPeriod;
		double totalDeposit = principal+deposit*months;
		double needGain = target - totalDeposit;
		/*
		for (int m = 1; m < okres*numMonthPeriod; m++){
			procentNetto = (Math.pow(1+(procent/(kapitalizacja*100)*(1-podatek/100)),kapitalizacja*okres)-1);
			suma = suma + wplata + suma*procentNetto;
			kapital = kapital + wplata;
		}			
		double zysk = suma - kapital;
		*/
		
		double numberOfYears = okres*timeUnitInYears;			
		double n = numberOfYears*compoundsPerYear;
		double i;
		double pmt = paymentPerCompundPeriod;
								
		//double futureValue = principal * Math.pow(1+i,n);			
		//futureValue+=pmt/i*(Math.pow(1+i, n)-1);			
		//double zysk = futureValue - principal - n*pmt;
		
		double FV = target;
		double PV = principal;
		
		//double deposit = (FV*i-PV*i*Math.pow(1+i, n))/(Math.pow(1+i, n)-1);
		
		double futureValue = principal + pmt*n;
		double bottomLimit = 0;
		double topLimit = 0;
		double prevProcent = 0.0;
		double nextProcent = 0.0;
		procent = 10.0;
		
		if (futureValue < target){
		
			while (Math.abs(futureValue - target) > 1){		
			
				i = procent/(compoundsPerYear*100);
				
				//double pmt = paymentPerCompundPeriod;
							
				
				futureValue = principal * Math.pow(1+i,n);
				
				if (i > 0)
					futureValue += pmt*(Math.pow(1+i, n)-1)/i;
				
				//prevProcent = procent; 
				if (futureValue < target){
					bottomLimit = procent;
					if (topLimit == 0)
						nextProcent = procent*2;
					else
						nextProcent = (bottomLimit+topLimit)/2;					
				}else if (futureValue > target){					
					topLimit = procent; 					
					nextProcent = (bottomLimit+topLimit)/2;
					
				}
				prevProcent = procent;
				procent = nextProcent;					
				//procent += 0.1;
			
			}
		}else{
			procent = 0.0;
		}
		
		double zysk = futureValue - principal - n*pmt;
							
		String currency = getString(R.string.currency);
		String pre_currency = getString(R.string.pre_currency);
		
		if (mSymbol == null) mSymbol = " ";

		tvPercent.setText(formatNumber(procent)+" %");
		tvGain.setText(formatNumber(needGain)+" "+mSymbol);
		tvTotalDepo.setText(formatNumber(totalDeposit)+mSymbol);
		/*
		tvPercent.setText(String.format("%.2f", procent)+" %");
		tvGain.setText(pre_currency+String.format("%.2f ", needGain)+mSymbol);
		tvTotalDepo.setText(pre_currency+String.format("%.2f ", totalDeposit)+mSymbol);
		*/
		//textZysk.setText(pre_currency+String.format("%.2f", zysk)+currency);
		
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
		
		menuHelper.handleOnItemSelected(this, item);
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
			
			//mSymbol = ddSymbol.getItemAtPosition(pos).toString();
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
