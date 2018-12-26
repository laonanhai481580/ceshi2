package com.ambition.product.base;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Attachment extends IdEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private String path;
	private Long size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}


}
