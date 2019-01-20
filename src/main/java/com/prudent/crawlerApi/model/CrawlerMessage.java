package com.prudent.crawlerApi.model;

import java.util.Set;

public class CrawlerMessage {

	private String link;
	private Set<String> listOfImagesLink;
	
	public CrawlerMessage(String link, Set<String> listOfImagesLink) {
		super();
		this.link = link;
		this.listOfImagesLink = listOfImagesLink;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Set<String> getListOfImagesLink() {
		return listOfImagesLink;
	}
	public void setListOfImagesLink(Set<String> listOfImagesLink) {
		this.listOfImagesLink = listOfImagesLink;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrawlerMessage other = (CrawlerMessage) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}
	
	
	
}
