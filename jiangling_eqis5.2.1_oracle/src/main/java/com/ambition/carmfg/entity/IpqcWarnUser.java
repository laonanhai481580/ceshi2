package com.ambition.carmfg.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;
/**
 * 类名: IpqcWarnUser 
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：IPQC稽核预警人员表</p>
 * @author  刘承斌
 * @version 1.00  2014-10-20 下午11:27:38  发布
 */
@Entity
@Table(name="MFG_IPQC_WARN_USER")
public class IpqcWarnUser extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String name;//用户名
	private String userId;//用户ID
	
	@ManyToOne
	@JoinColumn(name="FK_IPQC_AUDIT_ID")
	private IpqcAudit ipqcAudit;//IPQC稽核

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public IpqcAudit getIpqcAudit() {
		return ipqcAudit;
	}

	public void setIpqcAudit(IpqcAudit ipqcAudit) {
		this.ipqcAudit = ipqcAudit;
	}
	
	
}
