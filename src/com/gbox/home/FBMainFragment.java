package com.gbox.home;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.Request;
import com.facebook.LoggingBehavior;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.gbox.android.R;

public class FBMainFragment extends Fragment {
	private static final String TAG = "FBMainFragment";
	private UiLifecycleHelper uiHelper;
	
	private TextView userInfoTextView;
    private LoginButton buttonLoginLogout;
    private ImageView imgView;
    
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state, final Exception exception) {
			onSessionStateChange(session, state, exception);
			// updateView(); //TODO: Do we need to update? If not where does this happen?
		}
	};
    
	
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main, container, false);
		//imgView = (ImageView) view.findViewById(R.id.imgname);
		//imgView.setImageResource(R.drawable.letscarty);
		userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
		buttonLoginLogout = (LoginButton) view.findViewById(R.id.authButton);
		buttonLoginLogout.setFragment(this);
		buttonLoginLogout.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes"));
	
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(getActivity(), null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(getActivity());
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }
		
		return view;
	}
	
   
	@SuppressWarnings("deprecation") //TODO: what is the alternative?
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
		    userInfoTextView.setVisibility(View.VISIBLE);	    
		    // Request user data and show the results
		    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
		        @Override
		        public void onCompleted(GraphUser user, Response response) {
		            if (user != null) {
		                // Display the parsed user info
		                userInfoTextView.setText(buildUserInfoDisplay(user));
		            }
		        }
		    });
	    
		   //Intent intent = new Intent(getActivity(), AmazonDisplayGifts.class);
	       //this.startActivity(intent);
		    
		} else if (state.isClosed()) {
			Log.i(TAG, "User Logged out..."); //TODO: where am I clearing the session state and it's token? 
			userInfoTextView.setVisibility(View.INVISIBLE);
		}
	} 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}		
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "I am here");
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	} 
	
	
	private String buildUserInfoDisplay(GraphUser user) {
	    StringBuilder userInfo = new StringBuilder("");

	    // Example: typed access (name)
	    // - no special permissions required
	    userInfo.append(String.format("Name: %s\n\n", 
	        user.getName()));

	    // Example: typed access (birthday)
	    // - requires user_birthday permission
	    userInfo.append(String.format("Birthday: %s\n\n", 
	        user.getBirthday()));

	    // Example: partially typed access, to location field,
	    // name key (location)
	    // - requires user_location permission
	    //userInfo.append(String.format("Location: %s\n\n", 
	    //    user.getLocation().getProperty("user_location")));

	    // Example: access via property name (locale)
	    // - no special permissions required
	    userInfo.append(String.format("Locale: %s\n\n", 
	        user.getProperty("locale")));

	    // Example: access via key for array (languages) 
	    // - requires user_likes permission
	    /*JSONArray languages = (JSONArray)user.getProperty("languages");
	    if (languages.length() > 0) {
	        ArrayList<String> languageNames = new ArrayList<String> ();
	        for (int i=0; i < languages.length(); i++) {
	            JSONObject language = languages.optJSONObject(i);
	            // Add the language name to a list. Use JSON
	            // methods to get access to the name field. 
	            languageNames.add(language.optString("name"));
	        }           
	        userInfo.append(String.format("Languages: %s\n\n", 
	        languageNames.toString()));
	    }*/

	    return userInfo.toString();
	}
	
	
}
