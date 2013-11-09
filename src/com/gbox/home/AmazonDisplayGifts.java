package com.gbox.home;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.*;

import com.gbox.amazon.api.Amazon;
import com.gbox.android.R;
import com.gbox.util.BitmapWorker;


public class AmazonDisplayGifts extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	};

	@Override
	protected void onResume() {
		super.onResume();
		new Thread() {
			@Override
			public void run() {
				LinearLayout list1 = (LinearLayout)AmazonDisplayGifts.this.findViewById(R.id.list1);
				LinearLayout list2 = (LinearLayout)AmazonDisplayGifts.this.findViewById(R.id.list2);
				try {
					ArrayList<Amazon.Result> results = Amazon.search("Laptop", 8);
					for (int i=0;i<results.size();i++){
						System.out.println(results.get(i));
						new BitmapWorker((ImageView)list1.getChildAt(i+1),results.get(i).largeImage).execute();
					}
					results = Amazon.search("Bismuth crystal", 8);
					for (int i=0;i<results.size();i++){
						System.out.println(results.get(i));
						new BitmapWorker((ImageView)list2.getChildAt(i+1),results.get(i).largeImage).execute();
					}
				} catch (final IOException e){
					AmazonDisplayGifts.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
