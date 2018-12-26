package com.ambition.spc.entity;
 
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.norteksoft.product.orm.IdEntity;

/**
 * 类名:Klipple产品采集记录
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  赵骏
 * @version 1.00 2013-7-3 发布
 */
@Entity
@Table(name = "SPC_KLIPPEL_CHECK_RECORD")
public class KlippelCheckRecord extends IdEntity {
	private static final long serialVersionUID = 8572882099055995900L;
	private String productNo;//产品型号
	private String machineNo;//设备编号 
	@Temporal(TemporalType.DATE)
	private Date detectDate;//采集日期 
	private Integer year;//年
	private Integer yearAndMonth;//月
	private Integer yearAndWeek;//周
	private Integer yearAndQuarter;//季度
	private Integer amount=0;//数量
	private Boolean isPass = Boolean.FALSE;//是否通过
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public Date getDetectDate() {
		return detectDate;
	}
	public void setDetectDate(Date detectDate) {
		this.detectDate = detectDate;
		if(detectDate != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(detectDate);
			this.year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			if(month < 10){
				this.yearAndMonth = Integer.valueOf(this.year + "0" + month);
			}else{
				this.yearAndMonth = Integer.valueOf(this.year + "" + month);
			}
			int week = calendar.get(Calendar.WEEK_OF_YEAR);
			if(week < 10){
				this.yearAndWeek = Integer.valueOf(this.year + "0" + week);
			}else{
				this.yearAndWeek = Integer.valueOf(this.year + "" + week);
			}
			if(month >=1 && month <=3){
				this.yearAndQuarter = Integer.valueOf(this.year + "1");
			}else if(month >=4 && month <=6){
				this.yearAndQuarter = Integer.valueOf(this.year + "2");
			}else if(month >=7 && month <=9){
				this.yearAndQuarter = Integer.valueOf(this.year + "3");
			}else{
				this.yearAndQuarter = Integer.valueOf(this.year + "4");
			}
		}else{
			this.year = null;
			this.yearAndMonth = null;
			this.yearAndQuarter = null;
			this.yearAndWeek = null;
		}
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Boolean getIsPass() {
		return isPass;
	}
	public void setIsPass(Boolean isPass) {
		this.isPass = isPass;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getYearAndMonth() {
		return yearAndMonth;
	}
	public void setYearAndMonth(Integer yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}
	public Integer getYearAndWeek() {
		return yearAndWeek;
	}
	public void setYearAndWeek(Integer yearAndWeek) {
		this.yearAndWeek = yearAndWeek;
	}
	public Integer getYearAndQuarter() {
		return yearAndQuarter;
	}
	public void setYearAndQuarter(Integer yearAndQuarter) {
		this.yearAndQuarter = yearAndQuarter;
	}
}
