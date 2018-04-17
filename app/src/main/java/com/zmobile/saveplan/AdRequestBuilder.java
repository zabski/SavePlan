package com.zmobile.saveplan;

import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;

//import com.zmobile.payplan.R;


public class AdRequestBuilder {
	AdRequest adRequest;
	
	String[] inputWords = {"credit", "saving", "finance", "loans", "money", "cash", "interest", "deposit", "banking", "funds"};
	
	private static AdRequestBuilder instance = null;
	
	public static AdRequestBuilder getInstance(){
	      if(instance == null) {
	    	  instance = new AdRequestBuilder();
	      }		    	  		      
	      return instance;
	}
	
	private AdRequestBuilder(){
		Builder adBuilder = new AdRequest.Builder();
        //adBuilder.setGender(AdRequest.GENDER_FEMALE);                
        
        // ----------------
        Set<String> keywords = new HashSet<String>();
		String key = "key";
		String suffix = "";
		int i = 1;
		for(String keyword : inputWords){
			
			suffix = key + i;
			//MMrequest.put(keyword, keyword);
			i++;
			keywords.add(keyword);
			adBuilder.addKeyword(keyword);
		}

		adBuilder.addTestDevice("D9OKCY134460");
		/*
		adBuilder.addTestDevice("R28F70CAAPY");
		adBuilder.addTestDevice("LGH440n28c3d094");
		*/
		adRequest = adBuilder.build();
	}
	
	AdRequest build(){
		return adRequest;
	}

}
