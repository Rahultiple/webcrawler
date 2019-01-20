package com.prudent.crawlerApi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prudent.crawlerApi.bo.CrawlerUtil2;
import com.prudent.crawlerApi.model.CrawlerData;
import com.prudent.crawlerApi.model.CrawlerMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value="Crawler-API", description = "API has the parameter is optional so that this api will use url as 'https://www.prudential.co.uk' and literal as 'prudential.co.uk' from the resource property.</br><b>click on list operation</b><br><b> Please wait for the 2 minutes to get the results once click to tryout button </b>")
@RestController
public class CrawlerController {
	
	@Value("${start.url}")
	private String url;
	
	@Value("${url.literals}")
	private String urlLiterals;
	
	
	@GetMapping(value = "/getCrawler")
	public CrawlerData getCrawlerMap(@ApiParam(value = "Its optional param  , otherwise put it as 'https://www.prudential.co.uk' ",required=false) @RequestParam(value="url",required=false) String url,
		@ApiParam(value = "Its optional param  , otherwise put it as 'prudential.co.uk' ",required=false) @RequestParam(value="url_literals",required = false) String urlLiterals) {
		CrawlerData data=new CrawlerData();
		try {
			if((url!=null) && (urlLiterals !=null)) {
				this.url=url;
				this.urlLiterals=urlLiterals;
			}
			
			Map<String, Set<String>> callInterfaceMap = CrawlerUtil2.callInterface(this.url,this.urlLiterals);
			List<CrawlerMessage> processedToList = processLinkMap(callInterfaceMap);
			data.setLinkInformation(processedToList);
			data.setSize(callInterfaceMap.size());
			if(callInterfaceMap.size()==0) {
				data.setErrorMessage("Error Occured , Reson: check this site  "+this.url+" is running or not");
			}
			return data;
		}catch (Exception e) {
			// TODO: handle exception
			data.setErrorMessage("Error Occured , Reson:  "+e.getMessage());
			return data;
		}
		
		
	}
	private List<CrawlerMessage> processLinkMap(Map<String, Set<String>> map){
		List<CrawlerMessage> list= new ArrayList<CrawlerMessage>();
		for(String key:map.keySet()) {
			list.add(new CrawlerMessage(key, map.get(key)));
		}
		return list;
	}
	
}

