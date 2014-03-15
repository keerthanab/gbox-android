package com.gbox.home;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class FBLoginActivity extends FragmentActivity {

	private FBMainFragment fbMainFragment;
	private static final String TAG = "FBMainFragmentActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			Log.d(TAG, "Adding FB fragment on initial activity setup");
			fbMainFragment = new FBMainFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, fbMainFragment).commit();
		} else {
			Log.d(TAG, "Setting FB fragment from restored state info");
			fbMainFragment = (FBMainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}

}

/**This is using UserSettingFragment. TODO: Check is this approach is needed **/ 

/*public class FBLoginActivity extends FragmentActivity {
	
	private FBMainFragment mainFragment;
	private static final String TAG = "FBMainFragment";
	
	 private UserSettingsFragment userSettingsFragment;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.fb_login_activity);

	        FragmentManager fragmentManager = getSupportFragmentManager();
	        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
	        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
	            @Override
	            public void call(Session session, SessionState state, Exception exception) {
	                Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
	            }
	        });
	    }

	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
	        super.onActivityResult(requestCode, resultCode, data);
	    } 
	
}*/

