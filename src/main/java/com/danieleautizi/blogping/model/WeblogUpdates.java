package com.danieleautizi.blogping.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name="weblogUpdates")
@XmlAccessorType(XmlAccessType.NONE)
public class WeblogUpdates {
	
	private String 	version;
	
	private String	updated;
	
	private Integer	count = 0;
	
	private List<Weblog> weblogs;
	
	private Date	updatedDatetime;
	
	
	public void addWeblog(Weblog weblog) {
		if (weblog != null) {
			// ADD NEW WEBLOG
			getWeblogs().add(weblog);
			// INCREMENT COUNTER
			count++;
		}	
	}
	
	public String getVersion() {
		return version;
	}

	@XmlAttribute
	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdated() {
		return updated;
	}

	@XmlAttribute
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public Integer getCount() {
		return count;
	}

	@XmlAttribute
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@XmlElement(name="weblog")
	public List<Weblog> getWeblogs() {
		return weblogs;
	}

	public void setWeblogs(List<Weblog> weblogs) {
		this.weblogs = weblogs;
	}

	public Date getUpdatedDatetime() {
		return updatedDatetime;
	}
	
	@XmlTransient
	public void setUpdatedDatetime(Date updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}

	@Override
	public String toString() {
		return "WeblogUpdates [version=" + version + ", updated=" + updated
				+ ", count=" + count + ", weblogs=" + weblogs + "]";
	}
}
