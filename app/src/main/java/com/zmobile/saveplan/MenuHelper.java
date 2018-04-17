package com.zmobile.saveplan;

//import com.suredigit.inappfeedback.FeedbackDialog;
//import com.suredigit.inappfeedback.FeedbackSettings;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

//import com.zmobile.payplan.R;

class MenuHelper {

	Activity ctx;
	DialogSymbol dialogSymbol;
	//private FeedbackDialog feedBack;
	
	private static MenuHelper instance = null;
	
	public static MenuHelper getInstance(Activity ctx){
	  if(instance == null) {
		  instance = new MenuHelper(ctx);
	  }
		instance.ctx = ctx;
	    return instance;
	}
		
	private MenuHelper(Activity ctx) {
		this.ctx = ctx;
		// TODO Auto-generated constructor stub	
	
		//dialogSymbol = new DialogSymbol(ctx);
		dialogSymbol = DialogSymbol.getInstance(ctx);
		/*
		FeedbackSettings feedbackSettings = new FeedbackSettings();
		String feedbackPrompt = ctx.getString(R.string.feedback_prompt);
		String opinion = ctx.getString(R.string.opinion);
		feedbackSettings.setText(feedbackPrompt);
		feedbackSettings.setIdeaLabel(opinion);
		
		feedBack = new FeedbackDialog((Activity) ctx, "AF-07B6F5F50562-7E", feedbackSettings);
		*/
	}
	

	   public boolean handleOnItemSelected(Activity ctx, MenuItem item) {
	          // do something..
		   Intent i;
	        switch (item.getItemId()) {
			case android.R.id.home:
				this.ctx.finish();
				return true;
	        case R.id.action_save:
	            i = new Intent(ctx, ActivitySaving.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_period:
	            i = new Intent(ctx, ActivityPeriod.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_deposit:
	            i = new Intent(ctx, ActivityDeposit.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_interest:
	            i = new Intent(ctx, ActivityIntrest.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_loan:
	            i = new Intent(ctx, ActivityLoanSimple.class);
	            ctx.startActivity(i);
	            return true;               
	        case R.id.action_currency:
	          //i = new Intent(this, AddDrinkActivity.class);
	          //startActivity(i);
	          //dialogSymbol.show(this);
	        
	          dialogSymbol.setAct((Updatable)ctx);
	          //dialog = dialogSymbol.getDialog();
	          //dialog.show();
	          //mSymbol = dialogSymbol.getSymbol();
	          //UpdateSymbols();
	          return true;
	        case R.id.action_rate_app:
	        	Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
	        	Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
	        	try {
	        		ctx.startActivity(goToMarket);
	        	} catch (ActivityNotFoundException e) {
	        		ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.getPackageName())));
	        	}
	        	return true;
	        case R.id.action_feedback:
	        	//feedBack.show();
				final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");

				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[]{"zmobapp@gmail.com"});

				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ctx.getString(R.string.app_name2));

				String prompt = ctx.getString(R.string.feedback_prompt)+"\n\n";
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,prompt);

				ctx.startActivity(Intent.createChooser(
						emailIntent, prompt));
	        	return true;
			case R.id.action_remove_ads:

				HandlerPurchase.getInstance(ctx).onRemoveAdsButtonClicked(new View(ctx));
				return true;
	        	/*
	        case R.id.action_translate:
	        	Intent intent = new Intent(ctx, TransCommuActivity.class);
	        	intent.putExtra(TransCommuActivity.APPLICATION_CODE_EXTRA, "wLUOuFfXyn");
	        	ctx.startActivity(intent);	          	          
	        case R.id.action_info:
	          i = new Intent(this, ActivityInfo.class);
	          startActivity(i);
	          return true;
	          */
	        default:
	          //return super.onOptionsItemSelected(item);
	          return false;
	        } 
	   }

		void dispose(){
			instance = null;
			if (dialogSymbol!=null) {
				dialogSymbol.dispose();
				dialogSymbol.dismiss();
			}
		}

	}