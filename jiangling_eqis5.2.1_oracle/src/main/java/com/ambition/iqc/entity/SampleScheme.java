package com.ambition.iqc.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ambition.iqc.samplestandard.dao.SampleSchemeDao;
import com.ambition.product.base.IdEntity;
import com.norteksoft.bs.options.entity.Option;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.wf.engine.client.ExtendField;

/**    
 * 抽样方案
 * @authorBy YUKE
 *
 */
@Entity
@Table(name="IQC_SAMPLE_SCHEME")
public class SampleScheme extends IdEntity{
	private static final long serialVersionUID = 1L;
	private String code;//样本量字码
	private Integer amount;//样本量
	private String type = ORDINARY_TYPE;//检验类型
	public static final String EXEMPTION_TYPE="免检";
	public static final String ORDINARY_TYPE="正常";
	public static final String TIGHTEN_TYPE="加严";
	public static final String RELAX_TYPE="放宽";
	
	private String countType = COUNT_TYPE;//计值类型
	public static final String MEASURE_TYPE="计量";
	public static final String COUNT_TYPE="计数";
	private String baseType = SampleCodeLetter.GB_TYPE;//抽样标准类型
	
	@OneToMany(mappedBy="sampleScheme",cascade=javax.persistence.CascadeType.ALL)
    @Cascade(value=CascadeType.DELETE_ORPHAN)
    private List<AcceptanceQualityLimit> acceptanceQualityLimits;//接收质量限AQL
	
	@Embedded
	private ExtendField extendField;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getAmount() {
		return amount;
	}
	
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCountType() {
		return countType;
	}
	
	public void setCountType(String countType) {
		this.countType = countType;
	}
	
	public String getBaseType() {
		return baseType;
	}
	
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
	
	public List<AcceptanceQualityLimit> getAcceptanceQualityLimits() {
		return acceptanceQualityLimits;
	}
	
	public void setAcceptanceQualityLimits(
			List<AcceptanceQualityLimit> acceptanceQualityLimits) {
		this.acceptanceQualityLimits = acceptanceQualityLimits;
	}
	
	public ExtendField getExtendField() {
		return extendField;
	}
	
	public void setExtendField(ExtendField extendField) {
		this.extendField = extendField;
	}
	
	public static String[] getMitAQLs(){
		return new String[]{"validateLevel7|VII","validateLevel6|VI","validateLevel5|V","validateLevel4|IV","validateLevel3|III","validateLevel2|II","validateLevel1|I"};
	}
	
	public static String[] getMit1051AQLs(){
		return getMit1051AQLs(ContextUtils.getCompanyId());
	}
	
	public static String[] getMit1051AQLs(Long companyId){
		String hql = "from Option o where o.companyId=? and o.optionGroup.code=? order by o.optionIndex";
		SampleSchemeDao sampleSchemeDao = (SampleSchemeDao) ContextUtils.getBean("sampleSchemeDao");
		List<Option> options = sampleSchemeDao.find(hql,companyId,"iqc_acceptance_quality_limit");
		String[] strs = new String[options.size()];
		for(int i=0;i<strs.length;i++){
			strs[i] = options.get(i).getValue();
		}
		return strs;
	}
	
	public String toString(){
		return "物料抽样标准维护：检验抽样方案    ID"+this.getId()+"，类型"+this.type+"，样本量字码"+this.code+"，样本量"+this.amount;
	}
}
