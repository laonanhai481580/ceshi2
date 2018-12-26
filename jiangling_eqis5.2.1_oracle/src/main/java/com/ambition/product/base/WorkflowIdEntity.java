package com.ambition.product.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.norteksoft.wf.engine.client.ExtendField;
import com.norteksoft.wf.engine.client.FormFlowable;
import com.norteksoft.wf.engine.client.WorkflowInfo;

@MappedSuperclass
public class WorkflowIdEntity extends IdEntity implements FormFlowable{
	private static final long serialVersionUID = 6954658971359128065L;
	/**是否按时完成状态:逾期未完成*/
	public static final Integer ONTIMESTATE_OVERDUE=0;
	public static final String QMSTATE_OVERDUE="逾期未完成";
	/**是否按时完成状态:逾期完成*/
	public static final Integer ONTIMESTATE_OVERDUE_COMPLETE=1;
	public static final String QMSTATE_OVERDUE_COMPLETE="逾期完成";
	/**是否按时完成状态:按时完成*/
	public static final Integer ONTIMESTATE_ONTIME_COMPLETE=2;
	public static final String QMSTATE_ONTIME_COMPLETE="按时完成";
	/**是否按时完成状态:进行中*/
	public static final Integer ONTIMESTATE_CONDUCT=3;
	public static final String QMSTATE_CONDUCT="进行中";
	/**是否超期:是*/
	public static final Integer ISOVERDUE_YES = 1;
	/**是否超期:否*/
	public static final Integer ISOVERDUE_NO = 0;
	@Transient
	@Column(length=10)
	private String stage1;//阶段1,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage2;//阶段2,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage3;//阶段3,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage4;//阶段4,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage5;//阶段5,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage6;//阶段6,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage7;//阶段7,不在数据库生成
	@Column(length=10)
	private String stage8;//阶段8,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage9;//阶段9,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage10;//阶段10,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage11;//阶段11,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage12;//阶段12,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage13;//阶段13,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage14;//阶段14,不在数据库生成
	@Transient
	@Column(length=10)
    private String stage15;//阶段15,不在数据库生成
	@Transient
	@Column(length=10)
	private String stage16;//阶段16,不在数据库生成
	@Transient
	private Date taskCreatedTime; // 创建时间,不在数据库生成
	@Transient
	@Column(length=10)
	private String isOverTime;//是否超期
	@Transient
	private String transactorName;//办理人姓名
	@Transient
	private String transactor;  //办理人登录名
	@Transient
	private String duedate;//开始催办时限--->限定天数
	@Transient
	private Integer overDays;//超期天数
	@Column(length=300)
	private String launchState;//// 节点状态
	@Column(length=30)
	private String lastState;//最后的阶段状态
	private Date requiredDate;//要求完成时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date completeDate;//完成日期
	@Transient
	private String overdueStages;//超期的环节(不在数据库存储,仅在前台显示)
	@Column(length=10)
	private Integer ontimeState=ONTIMESTATE_CONDUCT;//是否按时完成状态，0:逾期未完成；1:逾期完成；2:按时完成；3:进行中
	private String changeWorkFlowColor;
	@Embedded
	private ExtendField extendField;
	
	@Embedded
	private WorkflowInfo workflowInfo;
	
	public String getChangeWorkFlowColor() {
		return changeWorkFlowColor;
	}
	public void setChangeWorkFlowColor(String changeWorkFlowColor) {
		this.changeWorkFlowColor = changeWorkFlowColor;
	}
	public String getOverdueStages() {
		return overdueStages;
	}
	public void setOverdueStages(String overdueStages) {
		this.overdueStages = overdueStages;
	}
	public Date getTaskCreatedTime() {
		return taskCreatedTime;
	}
	public void setTaskCreatedTime(Date taskCreatedTime) {
		this.taskCreatedTime = taskCreatedTime;
	}
	public String getIsOverTime() {
		return isOverTime;
	}
	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}
	public String getTransactorName() {
		return transactorName;
	}
	public void setTransactorName(String transactorName) {
		this.transactorName = transactorName;
	}
	public String getStage1() {
		return stage1;
	}
	public void setStage1(String stage1) {
		this.stage1 = stage1;
	}
	public String getStage2() {
		return stage2;
	}
	public void setStage2(String stage2) {
		this.stage2 = stage2;
	}
	public String getStage3() {
		return stage3;
	}
	public void setStage3(String stage3) {
		this.stage3 = stage3;
	}
	public String getStage4() {
		return stage4;
	}
	public void setStage4(String stage4) {
		this.stage4 = stage4;
	}
	public String getStage5() {
		return stage5;
	}
	public void setStage5(String stage5) {
		this.stage5 = stage5;
	}
	public String getStage6() {
		return stage6;
	}
	public void setStage6(String stage6) {
		this.stage6 = stage6;
	}
	public String getStage7() {
		return stage7;
	}
	public void setStage7(String stage7) {
		this.stage7 = stage7;
	}
	public String getStage8() {
		return stage8;
	}
	public void setStage8(String stage8) {
		this.stage8 = stage8;
	}
	public String getStage9() {
		return stage9;
	}
	public void setStage9(String stage9) {
		this.stage9 = stage9;
	}
	public String getStage10() {
		return stage10;
	}
	public void setStage10(String stage10) {
		this.stage10 = stage10;
	}
	public String getStage11() {
		return stage11;
	}
	public void setStage11(String stage11) {
		this.stage11 = stage11;
	}
	public String getStage12() {
		return stage12;
	}
	public void setStage12(String stage12) {
		this.stage12 = stage12;
	}
	public String getStage13() {
		return stage13;
	}
	public void setStage13(String stage13) {
		this.stage13 = stage13;
	}
	public String getStage14() {
		return stage14;
	}
	public void setStage14(String stage14) {
		this.stage14 = stage14;
	}
	public String getStage15() {
        return stage15;
    }
    public void setStage15(String stage15) {
        this.stage15 = stage15;
    }
    
    
    public String getStage16() {
        return stage16;
    }
    public void setStage16(String stage16) {
        this.stage16 = stage16;
    }
    public String getLaunchState() {
		return launchState;
	}
	public void setLaunchState(String launchState) {
		this.launchState = launchState;
		//从流程流转过的节点查询出当前停留的节点
		if(null == launchState || "".equals(launchState)){
			lastState = null;
		}else{
			String[] strs = launchState.split(",");
			for(int i=strs.length-1;i>-1;i--){
				if("".equals(strs[i])||"end".equals(strs[i])){
					continue;
				}
				lastState = strs[i];
				break;
			}
		}
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	public Integer getOverDays() {
		return overDays;
	}
	public void setOverDays(Integer overDays) {
		this.overDays = overDays;
	}
	public String getLastState() {
		return lastState;
	}
	public void setLastState(String lastState) {
		this.lastState = lastState;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public Integer getOntimeState() {
		return ontimeState;
	}
	public void setOntimeState(Integer ontimeState) {
		this.ontimeState = ontimeState;
	}
	public ExtendField getExtendField() {
		return extendField;
	}
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	public WorkflowInfo getWorkflowInfo() {
		return workflowInfo;
	}
	public void setWorkflowInfo(WorkflowInfo workflowInfo) {
		this.workflowInfo = workflowInfo;
	}
	public Date getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}
}
