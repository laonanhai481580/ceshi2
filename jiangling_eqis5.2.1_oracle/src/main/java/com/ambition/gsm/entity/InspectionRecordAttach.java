package com.ambition.gsm.entity;


import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.norteksoft.product.orm.IdEntity;

/**
 * 类名: InspectionRecordAttach 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：检查记录附件</p>
 * @author  刘承斌
 * @version 1.00 2014-5-21 下午2:43:52  发布
 */
@Entity
@Table(name="GSM_RECORD_ATTACH")
public class InspectionRecordAttach extends IdEntity{
	private static final long serialVersionUID = 1L;
	private Blob attach;//附件
	private String attachname;//附件名字
	
	@ManyToOne
	@JoinColumn(name="FK_INSPECTION_RECORD_ID")
	private GsmInspectionRecord gsmIspectionRecord;//检定计划

	public Blob getAttach() {
		return attach;
	}

	public void setAttach(Blob attach) {
		this.attach = attach;
	}

	public String getAttachname() {
		return attachname;
	}

	public void setAttachname(String attachname) {
		this.attachname = attachname;
	}

	public GsmInspectionRecord getGsmIspectionRecord() {
		return gsmIspectionRecord;
	}

	public void setGsmIspectionRecord(GsmInspectionRecord gsmIspectionRecord) {
		this.gsmIspectionRecord = gsmIspectionRecord;
	}
}
