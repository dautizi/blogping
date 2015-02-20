package com.danieleautizi.blogping.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name="WeblogResponse")
@XmlAccessorType(XmlAccessType.NONE)
public class WeblogResponse {
	
	private String  success;
	
	private Integer	status = 200;
	
	private String 	action;
	
	private String 	message;
	
	private String 	code;
	
	private boolean isBlacklisted = false;
	
	
	public String getAction() {
		return action;
	}

	@XmlElement
	public void setAction(String action) {
		this.action = action;
	}

	public String getSuccess() {
		return success;
	}
	
	@XmlAttribute
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public Integer getStatus() {
		return status;
	}

	@XmlElement
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	@XmlElement
	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	@XmlElement
	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlTransient
	public boolean isBlacklisted() {
		return isBlacklisted;
	}

	public void setBlacklisted(boolean isBlacklisted) {
		this.isBlacklisted = isBlacklisted;
	}

	@Override
	public String toString() {
		return "WeblogResponse [success=" + success + ", status=" + status
				+ ", action=" + action + ", message=" + message + ", code="
				+ code + ", isBlacklisted=" + isBlacklisted + "]";
	}

}
