package com.zmobile.saveplan;

//import com.example.tutorial1.R;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.android.gms.analytics.HitBuilders;
//import com.zmobile.payplan.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Service;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;
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

//import com.suredigit.inappfeedback.FeedbackDialog;
import com.zmobile.util.SoftKeyboard;

public class ActivityDeposit extends ActivityTemplate implements OnClickListener, OnItemSelectedListener, OnFocusChangeListener,
	Updatable {
	
	Button bOblicz;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editTarget;
	EditText editKwota;
	EditText editOkres;
	//EditText editKapital;
	EditText editProcent;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	TextView textDeposit;
	TextView textZysk;	
	TextView tvDepPeriod;
	TextView tvSymbol1;
	TextView tvSymbol2;
	Spinner ddownKapital;
	Spinner ddownOkres;
	DialogSymbol dialogSymbol;// = new DialogSymbol(this);
	AlertDialog dialog;
	
	LinearLayout layResult;
	
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
	SharedPreferences settings;
	//private FeedbackDialog feedBack;
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
		
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	    //feedBack.dismiss();
	  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_deposit_calc);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdview);
		fb_adView_end = (FrameLayout) findViewById(fb_adView_end_id);
		fb_adView_front = (FrameLayout) findViewById(fb_adView_front_id);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		adView = (AdView) findViewById(R.id.deposit_adView);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		TAG = this.getClass().getSimpleName();

		//fb_adView_front.setVisibility(View.GONE);
		//fb_adView_end.setVisibility(View.VISIBLE);

		//nativeAdContainer = fb_adView_end;

		/*
		String name = getLocalClassName();
		Log.i(name);
		mTracker.setScreenName(name);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		*/

		//AdRequestBuilder AdBuilder = new AdRequestBuilder();
		/*
		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();
		ads = getResources().getInteger(R.integer.ads);*/
		/*
		handlerIAP = HandlerPurchase.getInstance(this);
		if (ads==0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);*/
		layResult = (LinearLayout) findViewById(R.id.layResult);
		bOblicz = (Button) findViewById(R.id.bOblicz);
		editTarget = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		editOkres = (EditText) findViewById(R.id.editPeriod);
		//editKapital = (EditText) findViewById(R.id.editKapital);
		editProcent = (EditText) findViewById(R.id.editPercent);
		textDeposit = (TextView) findViewById(R.id.textDeposit);
		tvDepPeriod = (TextView) findViewById(R.id.tvDepositPeriod);
		textZysk = (TextView) findViewById(R.id.textMonths);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		/*
		editTarget.addTextChangedListener(new NumberTextWatcher(editTarget));
		editKwota.addTextChangedListener(new NumberTextWatcher(editKwota));
		editOkres.addTextChangedListener(new NumberTextWatcher(editOkres));
		editProcent.addTextChangedListener(new NumberTextWatcher(editProcent));
		*/
		bOblicz.setOnClickListener(this);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		editList.add(editOkres);
		editList.add(editKwota);
		editList.add(editTarget);
		editList.add(editProcent);

		for (EditText view : editList){
			view.setOnFocusChangeListener(this);
			if (view!=editProcent && view!=editOkres)
				view.addTextChangedListener(new NumberTextWatcher(view));
		}

		RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.deposit_calc); // You must use your root layout
		InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

		SoftKeyboard softKeyboard;
		softKeyboard = new SoftKeyboard(mainLayout, im);
		softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
		{

			@Override
			public void onSoftKeyboardHide()
			{
				// Code here
				Toast.makeText(ActivityDeposit.this, "keyboard hidden", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSoftKeyboardShow()
			{
				// Code here
				Toast.makeText(ActivityDeposit.this, "keyboard visible", Toast.LENGTH_SHORT).show();
			}
		});

		for (EditText view : editList){
			   view.setOnFocusChangeListener(this);
		}		
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		//ddlistOkres.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray));
		
		// spinner capital
		ddownKapital = (Spinner) findViewById(R.id.ddownKapital);
		// Create an ArrayAdapter using the string array and a default spinner layout
		//ArrayAdapter<CharSequence> adapterKapital = ArrayAdapter.createFromResource(this,
		        //R.array.kapitalizacja, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> adapterKapital = ArrayAdapter.createFromResource(this,
		        R.array.kapitalizacja, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterKapital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		//adapterKapital.setDropDownViewResource(R.layout.spinner);
		// Apply the adapter to the spinner
		ddownKapital.setAdapter(adapterKapital);
		ddownKapital.setOnItemSelectedListener(this);
		ddownKapital.setSelection(2);
		
		// spinner period
		ddownOkres = (Spinner) findViewById(R.id.ddownOkres);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterOkres = ArrayAdapter.createFromResource(this,
		        R.array.okres, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterOkres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownOkres.setAdapter(adapterOkres);		
		ddownOkres.setOnItemSelectedListener(periodListner);
		ddownOkres.setSelection(1);			
				
		dialogSymbol = DialogSymbol.getInstance(this);
		//dialogSymbol = new DialogSymbol(this);
		
		//feedBack = new FeedbackDialog(this, "AF-07B6F5F50562-7E");
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
			editTarget.setText("");
			editOkres.setText("");
			//editKwota.setSelected(true);
			editKwota.requestFocus();
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
		String targetStr = editTarget.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
		
		String msg = getResources().getString(R.string.fill_in);
		
		if (kwotaStr.equals("") || procentStr.equals("") ||
				okresStr.equals("") || targetStr.equals("")){
			//Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			toastMsg(msg,6);
			return;
		}
		layResult.setVisibility(View.VISIBLE);
		double target = Double.parseDouble(targetStr);
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
		
		//double paymentPerYear = wplata*12;
		//double paymentPerCompundPeriod = paymentPerYear/compoundsPerYear;
		
		
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
		//double pmt = paymentPerCompundPeriod;
							
		//double futureValue = principal * Math.pow(1+i,n);		
		//futureValue+=pmt/i*(Math.pow(1+i, n)-1);		
		//double zysk = futureValue - principal - n*pmt;
		
		double FV = target;
		double PV = principal;
		
		double deposit = (FV*i-PV*i*Math.pow(1+i, n))/(Math.pow(1+i, n)-1);
				
		String currency = getString(R.string.currency);
		String pre_currency = getString(R.string.pre_currency);			
		
		if (mSymbol == null) mSymbol = " ";
		
		//textDeposit.setText(pre_currency+String.format("%.2f ", numberAsString)+mSymbol);
		textDeposit.setText(formatNumber(deposit)+" "+mSymbol);
		
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
		
		menuHelp.handleOnItemSelected(this, item);
		return true;

    }
	
	@Override
	public void Update(){
		mSymbol = dialogSymbol.getSymbol();
		tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);		
		
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("symbol", mSymbol);
		// Commit the edits!
		editor.commit();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);


		// Checks whether a hardware keyboard is available
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
		}
	}

	SoftKeyboard.SoftKeyboardChanged onKeyboardChanged = new SoftKeyboard.SoftKeyboardChanged()
	{

		@Override
		public void onSoftKeyboardHide()
		{
			// Code here
			Toast.makeText(ActivityDeposit.this, "keyboard hidden", Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onSoftKeyboardShow()
		{
			// Code here
			Toast.makeText(ActivityDeposit.this, "keyboard visible", Toast.LENGTH_SHORT).show();
		}
	};

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
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}		
	}
	*/

}
