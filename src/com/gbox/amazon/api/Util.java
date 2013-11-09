package com.gbox.amazon.api;

import java.io.IOException;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

public final class Util {
	static BasicCookieStore store = new BasicCookieStore();
	static DefaultHttpClient httpclient = new DefaultHttpClient();
	static {
		httpclient.setCookieStore(store);
	}

	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String get(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpget);
			String ret = convertStreamToString(response.getEntity().getContent());
			return ret;

		} catch (ClientProtocolException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e2) {
			throw new RuntimeException(e2);
		}
	}

	public static HttpResponse post(String url, List<NameValuePair> nameValuePairs,
			Header... headers) {
		// Create Post Header
		HttpPost httppost = new HttpPost(url);
		for (Header h : headers)
			httppost.addHeader(h);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("response:");
			System.out.println(convertStreamToString(response.getEntity().getContent()));
			return response;

		} catch (ClientProtocolException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e2) {
			throw new RuntimeException(e2);
		}
	}

	public static HttpResponse post(String url, String text, Header... headers) {
		HttpPost httppost = new HttpPost(url);

		try {
			httppost.setEntity(new StringEntity(text));
			for (Header h : headers)
				httppost.addHeader(h);
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			System.out.println(convertStreamToString(response.getEntity().getContent()));
			return response;

		} catch (ClientProtocolException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e2) {
			throw new RuntimeException(e2);
		}
	}
}
