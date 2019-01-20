package com.prudent.crawlerApi.bo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class CrawlerUtil3 {
	static final String URL="http://www.prudential.co.uk"; 
	//static final String URL_LITERAL="cis.udel.edu"; 
	static final String URL_LITERAL="prudential.co.uk"; 
	static ConcurrentHashMap<String,Set> map=new ConcurrentHashMap<String,Set>();
	static ExecutorService es = Executors.newFixedThreadPool(100);
	static  int count=0; 
	static  int executorCount=0; 
	static List<Future<Integer>> listFuture=new ArrayList<Future<Integer>>();
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
 
		File dir = new File(".");
		String loc = dir.getCanonicalPath() + File.separator + "record.txt";
		FileWriter fstream = new FileWriter(loc, true);
		BufferedWriter out = new BufferedWriter(fstream);
		out.newLine();
		out.close();
 
		//processPage("http://cis.udel.edu");
		processPage(URL);
		File file = new File(loc);
 
		if (file.delete()) {
 
		}
		System.out.println("hm Size: "+map.size());
		System.out.println("Final Map:=> "+map);
	}
 
	// givn a String, and a File
	// return if the String is contained in the File
	public static boolean checkExist(String s, File fin) throws IOException {
 
		FileInputStream fis = new FileInputStream(fin);
		// //Construct the BufferedReader object
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
		String aLine = null;
		while ((aLine = in.readLine()) != null) {
			// //Process each line
			if (aLine.trim().contains(s)) {
				//System.out.println("contains " + s);
				in.close();
				fis.close();
				return true;
			}
		}
 
		// do not forget to close the buffer reader
		in.close();
		fis.close();
 
		return false;
	}
	public static int processContent(String URL) {
		if (URL.contains(".pdf") || URL.contains("@") 
				|| URL.contains("adfad") || URL.contains(":80")
				|| URL.contains("fdafd") || URL.contains(".jpg")
				|| URL.contains(".pdf") || URL.contains(".jpg"))
			return 1;
		if (URL.contains(URL_LITERAL) && !URL.endsWith("/")) {
			 
		} else if(URL.contains(URL_LITERAL) && URL.endsWith("/")){
			URL = URL.substring(0, URL.length()-1);
		}else{
			// url of other site -> do nothing
			return 1;
		}
		if( (map.get(URL.trim())!=null)) {
			return 1;
		}
		return 0;
		
	}
	public static int processPage(String URL) throws IOException, InterruptedException, ExecutionException {
 
		File dir = new File(".");
		String loc = dir.getCanonicalPath() + File.separator + "record.txt";
 
		// invalid link
		if (URL.contains(".pdf") || URL.contains("@") 
				|| URL.contains("adfad") || URL.contains(":80")
				|| URL.contains("fdafd") || URL.contains(".jpg")
				|| URL.contains(".pdf") || URL.contains(".jpg"))
			return 1;
 
		// process the url first
		if (URL.contains(URL_LITERAL) && !URL.endsWith("/")) {
 
		} else if(URL.contains(URL_LITERAL) && URL.endsWith("/")){
			URL = URL.substring(0, URL.length()-1);
		}else{
			// url of other site -> do nothing
			return 1;
		}
		if( (map.get(URL.trim())!=null)) {
			return 1;
		}
		File file = new File(loc);
 
		// check existance
		boolean e = checkExist(URL, file);
		if (!e) {
			System.out.println((count++)+"------ :  " + URL);
				
			// insert to file
			FileWriter fstream = new FileWriter(loc, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(URL);
			out.newLine();
			out.close();
 
			Document doc = null;
			try {
				doc = Jsoup.connect(URL).get();
			} catch (IOException e1) {
				e1.printStackTrace();
				return 1;
			}
 
			/*if (doc.text().contains("PhD")) {
				//System.out.println(URL);
			}*/
			//rahul code starts
			Elements img = doc.select("img");
			Set<String> set= new HashSet<String>();
			for (Element link : img) {
				set.add(CrawlerUtil3.URL+link.attr("src"));
			}
			System.out.println("-img-->"+set.toString());
			//rahul code ends 
			map.put(URL.trim(),set);
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				String localhref=link.attr("abs:href");
				if(executorCount==0) {
					Future<Integer> submitTask = es.submit(new Callable<Integer>() {

						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							int processPage = processPage(localhref);
							return processPage;
						}
					});
					
					listFuture.add(submitTask);
						
				}
				
			}
			System.out.println("List size of the taks=> "+listFuture);
			executorCount++;
			for(Future<Integer> future:listFuture) {
				if(future.get()==1) {
					System.out.println("task completed -> "+future.toString());
				}
			}
			
			
		} else {
			// do nothing
			return 1;
		}
		return 1;
	}
}
