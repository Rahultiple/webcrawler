package com.prudent.crawlerApi.model;

import java.util.List;
import java.util.Set;

public class CrawlerData {

	private Integer size;
	private String errorMessage;
	private List<CrawlerMessage> linkInformation;
	
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public List<CrawlerMessage> getLinkInformation() {
		return linkInformation;
	}
	public void setLinkInformation(List<CrawlerMessage> linkInformation) {
		this.linkInformation = linkInformation;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
}
