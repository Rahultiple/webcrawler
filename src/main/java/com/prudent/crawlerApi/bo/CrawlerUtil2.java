package com.prudent.crawlerApi.bo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerUtil2 {
	private final static Logger logger = Logger.getLogger(CrawlerUtil2.class);
	private static String URL1="http://www.prudential.co.uk"; 
	//static final String URL_LITERAL="cis.udel.edu"; 
	private static  String URL_LITERAL="prudential.co.uk"; 
	static  int count=0; 
	static ConcurrentHashMap<String,Set<String>> map=new ConcurrentHashMap<String,Set<String>>();
	static ExecutorService es = Executors.newFixedThreadPool(20);
	public static void main(String[] args) throws IOException,InterruptedException {
 
		//PropertyConfigurator.configure("C:\\Users\\RT1050\\eclipse-workspace\\Api-Web-Crawler\\src\\main\\resources\\log4j.properties");
		PropertyConfigurator.configure("log4j.properties");
		
		//processPage("http://cis.udel.edu");
		//processPage(URL1);
		startThreadProcess(URL1);
			
		System.out.println("HashMap:: "+map);
		System.out.println("HashMap Size:: "+map.size());
	}
	
	public static Map<String,Set<String>> callInterface(String url,String urliteral) throws InterruptedException {
		URL1=url;
		URL_LITERAL=urliteral;
		startThreadProcess(URL1);
		return map;
	}
	private static void startThreadProcess(String URL ) throws InterruptedException{
		
		if (URL.contains(".pdf") || URL.contains("@") 
				|| URL.contains("adfad") || URL.contains(":80")
				|| URL.contains(".PDF") || URL.contains(".jpg")
				|| URL.contains(".pdf") || URL.contains(".png")
				|| URL.contains("#") ||  URL.contains(".xls") || URL.contains(".ppt") 
				|| URL.contains(".xlsx"))
			return;
 
		// process the url first
		if (URL.contains(URL_LITERAL) && !URL.endsWith("/")) {
			//return;
		} else if(URL.contains(URL_LITERAL) && URL.endsWith("/")){
			URL = URL.substring(0, URL.length()-1);
		}else{
			// url of other site -> do nothing
			return;
		}
		if (map.get(URL)!=null) {
			return;
		}
		
			System.out.println((count++)+"------ :  " + URL);
				
			
			Document doc = null;
			try {
				
				doc = Jsoup.connect(URL).timeout(5000).get();
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
						
			Elements questions = doc.select("a[href]");
			int sizeofLink=questions.size();
			int constLoopEnd=10;
			int loops= Math.floorDiv(sizeofLink, constLoopEnd) ;
			if( Math.floorMod(sizeofLink, constLoopEnd) !=0) {
				 loops++;
			}
			final ArrayList<Integer> listOFInteger=new ArrayList<Integer>();
			int listStrtVal=0;
			for(int size=0;size<loops;size++) {
				 listOFInteger.add(listStrtVal);
				 listStrtVal=listStrtVal+constLoopEnd+1;
			}
			System.out.println("Loops : "+listOFInteger);
			
			final Iterator<Integer> iterator = listOFInteger.iterator();
			 
			logger.info("Inilial link size is -> "+sizeofLink);
			for(int threadLoop=0;threadLoop<loops;threadLoop++) {
				 iterator.hasNext();
				 final int finalinitialLoopStart=iterator.next();
				 int temp=finalinitialLoopStart+constLoopEnd;
				 if((finalinitialLoopStart+constLoopEnd)>sizeofLink) {
					 temp=sizeofLink;
			}
			final int initialLoopEnd=temp;;
				 es.execute(new Runnable() {
					
					@Override
					public void run() {
						Thread.currentThread().setName(finalinitialLoopStart+"-"+initialLoopEnd);
						// TODO Auto-generated method stub
						for(int i=finalinitialLoopStart;i<initialLoopEnd;i++) {
							Element links = questions.get(i);
							String innerURL=links.attr("abs:href");
							 processPage(innerURL);
						
						}
						//es.shutdown();
						System.out.println(Thread.currentThread().getName()+ ":: End Iteration-->startIteration=>"+finalinitialLoopStart+" endIteration=>"+initialLoopEnd);
						logger.info(Thread.currentThread().getName()+":: End Iteration-->startIteration=>"+finalinitialLoopStart+" endIteration=>"+initialLoopEnd);
						
						//Thread.currentThread().stop();
						
					}
				});
				 System.out.println("-->startIteration=>"+finalinitialLoopStart+" endIteration=>"+initialLoopEnd);
				
			}
			 es.shutdown();
			 if( es.awaitTermination(300, TimeUnit.SECONDS)) {
					logger.info("before loop termination size of Map=>"+map.size());
					
					System.out.println("HashMap Size:: "+map.size());
			}
			
			return;
	}
 
	private static void processPage(String URL){
 		try {
		// invalid link
		if ( 	(URL==null) || URL.trim().length()==0
				|| URL.contains(".pdf") || URL.contains("@") 
				|| URL.contains("adfad") || URL.contains(":80")
				|| URL.contains(".PDF") || URL.contains(".jpg")
				|| URL.contains(".pdf") || URL.contains(".png")|| URL.contains(".zip") 
				|| URL.contains("#") ||  URL.contains(".xls") || URL.contains(".ppt") 
				|| URL.contains(".xlsx"))
			return;
 
		// process the url first
		if (URL.contains(URL_LITERAL) && !URL.endsWith("/")) {
			//return;
		} else if(URL.contains(URL_LITERAL) && URL.endsWith("/")){
			URL = URL.substring(0, URL.length()-1);
		}else{
			// url of other site -> do nothing
			return;
		}
		if (map.get(URL)!=null) {
			return;
		}
		
			System.out.println((count++)+"------ :  " + URL);
			
			 map.put(URL.trim(),new HashSet<String>());		
			
			Document doc = null;
			try {
				
				doc = Jsoup.connect(URL).timeout(5000).get();
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
 
			/*if (doc.text().contains("PhD")) {
				//System.out.println(URL);
			}*/
			//rahul code starts
			Elements img = doc.select("img");
			Set<String> set= map.get(URL.trim());
			for (Element link : img) {
				set.add(URL+link.attr("src"));
			}
			System.out.println("-img-->"+set.toString());
			
			//rahul code ends 
			
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				URL=link.attr("abs:href");
				processPage(URL);
			}
		return;
 		}catch(Exception e) {
 			e.printStackTrace();
 			return;
 		}
			
		}
	


}
