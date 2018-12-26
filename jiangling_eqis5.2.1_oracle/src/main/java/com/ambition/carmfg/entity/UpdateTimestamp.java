package com.ambition.carmfg.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ambition.product.base.IdEntity;

/**
 * 类名:erp更新时间戳
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-27 发布
 */
@Entity
@Table(name = "AMB_UPDATE_TIMESTAMP")
public class UpdateTimestamp extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String tableName;//表名
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;//最后更新时间
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
