package br.com.editoraglobo.crawler.util;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebCrawler {

	public static Queue<String> queue = new LinkedList<>();
	public static Set<String> marked = new HashSet<>();

	public static JSONObject crawl(String root) throws Exception {
		queue.add(root);
		JSONObject object = new JSONObject();

		while (!queue.isEmpty()) {
			String crawledUrl = queue.poll();

			URL url = null;
			InputStream is = null;

			url = new URL(crawledUrl);
			is = url.openStream();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			
			if (doc != null) {
				doc.getDocumentElement().normalize();
				
				object.put("feed", getChilds(doc.getElementsByTagName("item")));
				
			}
		}

		return object;
	}

	public static JSONArray getChilds(NodeList nodeList) {
		JSONArray array = new JSONArray();
		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				
				Node node = nodeList.item(i);
				if(node != null) {
					node.normalize();
				}
				
				try {
					if(node.hasChildNodes()) {
						JSONObject json = new JSONObject();
						JSONArray arr = getChilds(node.getChildNodes());
						if(arr != null) {
							json.put(node.getNodeName(), arr );
							array.put(json);
						}
					}else if( "#text".equals( node.getNodeName() ) || "#cdata-section".equals( node.getNodeName() ) ){
						if( node.getNodeValue().length() >= 6 ) {
							array.put(node.getNodeValue());
						}
					}else {
						JSONObject json = new JSONObject();
						json.put(node.getNodeName(), node.getNodeValue());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		if(array.length() == 0) {
			return null;
		}
		return array;
	}
}
