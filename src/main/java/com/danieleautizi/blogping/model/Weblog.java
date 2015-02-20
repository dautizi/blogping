package com.danieleautizi.blogping.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name="weblog")
@XmlAccessorType(XmlAccessType.NONE)
public class Weblog {

    private long 	id;
    private Date	insertDate;
    private boolean	isValid;
    
    private String 	name;
    
    private String 	url;
    
    private String 	rssUrl;
    
    private Integer when;
    
    
    @XmlTransient
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	
	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getWhen() {
		return when;
	}
	
	@XmlAttribute
	public void setWhen(Integer when) {
		this.when = when;
	}
	
	public Date getInsertDate() {
		return insertDate;
	}
	
	@XmlTransient
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	
	@XmlTransient
	public boolean isValid() {
		return isValid;
	}
	
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	@Override
	public String toString() {
		return "Weblog [id=" + id + ", insertDate=" + insertDate + ", isValid="
				+ isValid + ", name=" + name + ", url=" + url + ", when="
				+ when + "]";
	}

}
