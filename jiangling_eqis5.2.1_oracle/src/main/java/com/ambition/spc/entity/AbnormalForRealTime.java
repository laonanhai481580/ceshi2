package com.ambition.spc.entity;

import java.io.Serializable;

public class AbnormalForRealTime implements Serializable {

	private static final long serialVersionUID = 1L;
	private int abnormity;
	private String abnormityCN;
	private String happenTime;
	private int startPosition;
	private int endPosition;
	private String title;
	private int upLow = 3;

	public String getAbnormityCN(boolean timeType) {
		if (timeType) {
			return this.happenTime + this.title + this.abnormityCN;
		}
		if (this.endPosition - this.startPosition < 1) {
			if ((this.title != null) && ("移动极差控制图".equals(this.title))) {
				return this.title + "第" + (1 + this.startPosition) + "点"
						+ this.abnormityCN;
			}
			return this.title + "第" + this.startPosition + "点"
					+ this.abnormityCN;
		}
		if ((this.title != null) && ("移动极差控制图".equals(this.title))) {
			return this.title + "第" + (1 + this.startPosition) + "至"
					+ (1 + this.endPosition) + "点" + this.abnormityCN;
		}
		return this.title + "第" + this.startPosition + "至" + this.endPosition
				+ "点" + this.abnormityCN;
	}
	public String getAbnormityCN() {
		return this.abnormityCN;
	}
	public void setAbnormityCN(String abnormityCN) {
		this.abnormityCN = abnormityCN;
	}

	public String getHappenTime() {
		return this.happenTime;
	}

	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}

	public int getAbnormity() {
		return this.abnormity;
	}

	public void setAbnormity(int abnormity) {
		this.abnormity = abnormity;
	}

	public int getEndPosition() {
		return this.endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public int getStartPosition() {
		return this.startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public String getTitle() {
		return this.title;
	}

	public int getUpLow() {
		return this.upLow;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUpLow(int upLow) {
		this.upLow = upLow;
	}
}