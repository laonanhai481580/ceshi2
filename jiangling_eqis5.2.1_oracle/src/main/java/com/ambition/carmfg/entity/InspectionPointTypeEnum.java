package com.ambition.carmfg.entity;

public enum InspectionPointTypeEnum {
	/**首检*/
	FIRSTINSPECTION("inspectionPointTypeEnum.firstinspection"),
	/**巡检*/
	PATROLINSPECTION("inspectionPointTypeEnum.patrolinspection"),
	/**末检检验*/
	COMPLETEINSPECTION("inspectionPointTypeEnum.completeinspection"),
	/**入库检验*/
	STORAGEINSPECTION("inspectionPointTypeEnum.storageinspection"),
	/**出货检验*/
	DELIVERINSPECTION("inspectionPointTypeEnum.deliverinspection"),
	/**生产检查*/
	PRODUCTCHECK("inspectionPointTypeEnum.productcheck"),
	/**生产日报表*/
	PRODUCTDALIYREPORT("inspectionPointTypeEnum.productdaliyreport");
	
	String code;
	private InspectionPointTypeEnum(String code) {
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	
}
