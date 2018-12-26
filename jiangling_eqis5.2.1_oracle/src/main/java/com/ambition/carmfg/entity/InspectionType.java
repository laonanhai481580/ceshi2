package com.ambition.carmfg.entity;

public enum InspectionType {
	/**检验*/
	INSPECTION("inspectionType.inspection"),
	/**检查*/
	CHECK("inspectionType.check");
	String code;
	private InspectionType(String code) {
		this.code=code;
	}
	public String getCode() {
		return code;
	}
}
