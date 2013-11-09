package com.gbox.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapWorker extends AsyncTask<Void, Void, Bitmap> {
	private static final String TAG = new Object(){}.getClass().getEnclosingClass().getName();
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	 private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
	 int reqHeight) {
	
	 // First decode with inJustDecodeBounds=true to check dimensions
	 final BitmapFactory.Options options = new BitmapFactory.Options();
	 options.inJustDecodeBounds = true;
	 BitmapFactory.decodeResource(res, resId, options);
	
	 // Calculate inSampleSize
	 options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	
	 // Decode bitmap with inSampleSize set
	 options.inJustDecodeBounds = false;
	 return BitmapFactory.decodeResource(res, resId, options);
	 }

	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) throws MalformedURLException, IOException {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new java.net.URL(pathName).openStream(),null,options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(new java.net.URL(pathName).openStream(),null,options);
	}

	private final WeakReference<ImageView> imageViewReference;

	private String url;

	public BitmapWorker(ImageView imageView, String url) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.url = url;
	}

	// Decode image in background.
	@Override
	protected Bitmap doInBackground(Void... params) {
		try {
			return decodeSampledBitmapFromFile(url, 200, 200);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			if (imageView != null) imageView.setImageBitmap(bitmap);
			else Log.e(TAG, "ImageView is null");
		}
	}
}