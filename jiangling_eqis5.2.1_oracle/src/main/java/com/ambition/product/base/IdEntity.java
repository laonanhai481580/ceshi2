package com.ambition.product.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
@SuppressWarnings("serial")
@MappedSuperclass
public class IdEntity extends com.norteksoft.product.orm.IdEntity implements Serializable{
	@Id
	@GenericGenerator(name = "IdGenerator", strategy = "native")
	@GeneratedValue(generator = "IdGenerator")
	private Long   id;
	private String businessUnitCode;//事业部编码 
	private String businessUnitName;//事业部名称 
	private Integer year;//年
	private Integer monthOfYear;//月
	private Integer yearAndMonth;//统计年和月
	private Integer DateOfMonthOfYear;//日
	private Integer yearAndMonthAndDate;//统计年和月和日
	private Integer quarterOfYear;//季
	private Integer yearAndQuarter;//统计年和季
	private Integer weekOfYear;//周
	private Integer yearAndWeek;//统计年和周
	private String lastModifier;
	private String hiddenState="N";//隐藏状态
	private String taskProgress;//任务进度
	private String factoryClassify;//厂区分类 用于厂区数据区分
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Object clone(){
		ByteArrayOutputStream bout = null;
		ObjectOutputStream out = null;
		ByteArrayInputStream bin = null;
		try 
		{
			bout  =   new  ByteArrayOutputStream();
		    out  =   new  ObjectOutputStream(bout);
		    out.writeObject( this );
		    bin  =   new  ByteArrayInputStream(bout
		    		.toByteArray());
		    ObjectInputStream in  =   new  ObjectInputStream(bin);
		    return in.readObject();
		} catch (IOException e) {
			throw new RuntimeException("对象克隆失败!",e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("对象克隆失败!",e);
		}finally{
			if(bout != null){
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bin != null){
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}  
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(Integer monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public Integer getDateOfMonthOfYear() {
		return DateOfMonthOfYear;
	}

	public void setDateOfMonthOfYear(Integer dateOfMonthOfYear) {
		DateOfMonthOfYear = dateOfMonthOfYear;
	}

	public Integer getYearAndMonthAndDate() {
		return yearAndMonthAndDate;
	}

	public void setYearAndMonthAndDate(Integer yearAndMonthAndDate) {
		this.yearAndMonthAndDate = yearAndMonthAndDate;
	}

	public Integer getQuarterOfYear() {
		return quarterOfYear;
	}

	public void setQuarterOfYear(Integer quarterOfYear) {
		this.quarterOfYear = quarterOfYear;
	}

	public Integer getYearAndQuarter() {
		return yearAndQuarter;
	}

	public void setYearAndQuarter(Integer yearAndQuarter) {
		this.yearAndQuarter = yearAndQuarter;
	}

	public Integer getWeekOfYear() {
		return weekOfYear;
	}

	public void setWeekOfYear(Integer weekOfYear) {
		
		this.weekOfYear = weekOfYear;
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

	public String getBusinessUnitCode() {
		return businessUnitCode;
	}

	public void setBusinessUnitCode(String businessUnitCode) {
		this.businessUnitCode = businessUnitCode;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getHiddenState() {
		return hiddenState;
	}

	public void setHiddenState(String hiddenState) {
		this.hiddenState = hiddenState;
	}

	public String getTaskProgress() {
		return taskProgress;
	}

	public void setTaskProgress(String taskProgress) {
		this.taskProgress = taskProgress;
	}

	public String getFactoryClassify() {
		return factoryClassify;
	}

	public void setFactoryClassify(String factoryClassify) {
		this.factoryClassify = factoryClassify; 
	}

}
