package com.ambition.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ambition.product.base.IdEntity;

/**
 * 类名:供应商业绩评价数据来源
 * <p>amb</p>
 * <p>安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-4-19 发布
 */
@Entity
@Table(name = "SUPPLIER_EVALUATE_DATA_SOURCE")
public class EvaluateDataSource extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String code;//编码
	private String name;//名称
	@Column(length=3000)
	private String executeCode;//执行的代码
    private String description;//执行逻辑的描述,包括修改的事项

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecuteCode() {
		return executeCode;
	}

	public void setExecuteCode(String executeCode) {
		this.executeCode = executeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString(){
		return "供应商质量管理：自动评分数据来源  ID"+this.getId()+",编码:"+this.code+",名称:"+this.name;
	}
}
