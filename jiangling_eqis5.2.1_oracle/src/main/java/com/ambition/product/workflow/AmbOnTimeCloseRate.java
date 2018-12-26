package com.ambition.product.workflow;

import com.ambition.product.base.WorkflowIdEntity;

/**
 * 类名:按时完成率统计模型
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2015-4-20 发布
 */
public class AmbOnTimeCloseRate extends WorkflowIdEntity{
	private static final long serialVersionUID = 1L;
	private String name;//分组名称（责任供应商）
	private Integer allForms;//单据数量（改进任务总数）
	private Integer notClosed;//未关闭数量
	private Integer yesClosed;//已关闭数量
	
	private Float closedRate;//关闭率
	private Integer overTimeClosed;//延期项数量
	private Integer onTimeClosed;//按时关闭数量
	private Float onTimeClosedRate;//按时关闭率
	
	/**
	 * @return the notClosed
	 */
	public Integer getNotClosed() {
		return notClosed;
	}
	/**
	 * @param notClosed the notClosed to set
	 */
	public void setNotClosed(Integer notClosed) {
		this.notClosed = notClosed;
	}
	/**
	 * @return the yesClosed
	 */
	public Integer getYesClosed() {
		return yesClosed;
	}
	/**
	 * @param yesClosed the yesClosed to set
	 */
	public void setYesClosed(Integer yesClosed) {
		this.yesClosed = yesClosed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAllForms() {
		return allForms;
	}
	public void setAllForms(Integer allForms) {
		this.allForms = allForms;
	}
	public Integer getOnTimeClosed() {
		return onTimeClosed;
	}
	public void setOnTimeClosed(Integer onTimeClosed) {
		this.onTimeClosed = onTimeClosed;
	}
	public Integer getOverTimeClosed() {
		return overTimeClosed;
	}
	public void setOverTimeClosed(Integer overTimeClosed) {
		this.overTimeClosed = overTimeClosed;
	}
	public Float getClosedRate() {
		return closedRate;
	}
	public void setClosedRate(Float closedRate) {
		this.closedRate = closedRate;
	}
	public Float getOnTimeClosedRate() {
		return onTimeClosedRate;
	}
	public void setOnTimeClosedRate(Float onTimeClosedRate) {
		this.onTimeClosedRate = onTimeClosedRate;
	}
}
