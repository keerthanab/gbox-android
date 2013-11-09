/**
 *  Amazon Product Advertising API
 *
 *  API Version: 2009-03-31
 *
 */

package com.gbox.amazon.api;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.w3c.dom.*;

public class Amazon {
	private static final String TAG = new Object(){}.getClass().getEnclosingClass().getName();
	
	/** struct */
	public static class Result {
		public String title, ASIN, smallImage, mediumImage, largeImage;
		@Override
		public String toString() {
			return "Result [title=" + title + ", ASIN=" + ASIN + ", smallImage=" + smallImage
					+ ", mediumImage=" + mediumImage + ", largeImage=" + largeImage + "]";
		}
	}

	private static final String AWS_ACCESS_KEY_ID = "AKIAI3LFH2RQGSG5GCRA";
	private static final String AWS_SECRET_KEY = "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";

	/*
	 * Use one of the following end-points, according to the region you are interested in:
	 * 
	 * US: ecs.amazonaws.com CA: ecs.amazonaws.ca UK: ecs.amazonaws.co.uk DE: ecs.amazonaws.de FR:
	 * ecs.amazonaws.fr JP: ecs.amazonaws.jp
	 */
	private static final String ENDPOINT = "ecs.amazonaws.com";

	/**
	 * 
	 * @param requestUrl
	 * @param max_results
	 * @return results
	 * @throws Exception
	 *             (yep, all kinds)
	 */
	private static ArrayList<Result> fetchResults(String requestUrl, int max_results)
			throws Exception {
		ArrayList<Result> results = new ArrayList<Result>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(requestUrl);
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xPath.evaluate("//*[local-name()='Item']",
				doc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < max_results; i++) {
			Node node = nodes.item(i);
			if (node == null) break;// no more results
			Node importedNode = node.cloneNode(true);// TODO find out why node had to be cloned
			Result result = new Result();
			result.title = xPath.evaluate("//Title", importedNode);// TODO compile expression
			result.ASIN = xPath.evaluate("//ASIN", importedNode);
			result.smallImage = xPath.evaluate("(//SmallImage)/URL", importedNode);
			result.mediumImage = xPath.evaluate("(//MediumImage)/URL", importedNode);
			result.largeImage = xPath.evaluate("(//LargeImage)/URL", importedNode);
			results.add(result);
		}
		return results;
	}

	public static ArrayList<Result> search(String keywords, int max_results) throws IOException{
		SignedRequestsHelper helper;
		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.out.println("Map form example:");
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2009-03-31");
		params.put("Operation", "ItemSearch");

		params.put("Keywords", keywords);
		params.put("SearchIndex", "All");
		params.put("ResponseGroup", "Medium");
		params.put("AssociateTag", "ahgifti-20");

		String requestUrl = helper.sign(params);
		System.out.println("Signed Request is \"" + requestUrl + "\"");

		try {
			return fetchResults(requestUrl, max_results);
		} catch (IOException e){
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
