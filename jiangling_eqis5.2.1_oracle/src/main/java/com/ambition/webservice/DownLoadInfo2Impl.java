package com.ambition.webservice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.bom.dao.ProductBomDao;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgManufactureMessage;
import com.ambition.carmfg.entity.MfgSupplierMessage;
import com.ambition.carmfg.madeinspection.dao.MadeInspectionDao;
import com.ambition.carmfg.madeinspection.service.MadeInspectionManager;
import com.ambition.util.common.WebserviceHttpClientUtil;
import com.ambition.util.erp.entity.Result;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.util.ContextUtils;
  
/**
 * 类名:HelloWorldImpl.java
 * 中文类名:
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * <p>
 * @author 作者 :wuxuming
 * @version 1.0 创建时间：2015-9-9 下午5:27:23
 * </p>
 */
@Service
@WebService(endpointInterface="com.ambition.webservice.DownLoadInfo2",serviceName="DownLoadInfo2")
public class DownLoadInfo2Impl implements DownLoadInfo2 { 
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MadeInspectionDao madeInspectionDao;
	@SuppressWarnings("unused")
	@Override
	public String Download_Info(String text) {
		MadeInspectionManager madeInspectionManager = (MadeInspectionManager) ContextUtils.getBean("madeInspectionManager");
		FormCodeGenerated formCodeGenerated = (FormCodeGenerated) ContextUtils.getBean("formCodeGenerated");
				Map<String,Object> resultMap=null;
				Result result=new Result();
				String returnStr="";
				String interfaceName="";
				String idStr="";
				try{
					resultMap=WebserviceHttpClientUtil.converToMapForErp(text);
					if(resultMap.get("objectId")!=null&&resultMap.get("dataList")!=null){
						String objectId=resultMap.get("objectId").toString();//接口名称
						List<Map<String,String>> dataListMaps=(List<Map<String, String>>) resultMap.get("dataList");
						if("QISIPQCBASEDATE".equals(objectId)){
							//保存IPQC接口相关的数据
							MfgCheckInspectionReport report=new MfgCheckInspectionReport();
							report.setCompanyId(ContextUtils.getCompanyId());
							report.setCreatedTime(new Date());
							report.setCreator("system");
							report.setCreatorName("system");
							report.setMfgSupplierMessages(new ArrayList<MfgSupplierMessage>());
							report.setManufactureMessages(new ArrayList<MfgManufactureMessage>());
							report.setInspectionNo(formCodeGenerated.generateMFGode());
							for(Map<String,String> map:dataListMaps){
								String fieldRootName=map.get("fieldRootName").toString();//接口名称
								if("DataSetIPQC_02_01".equals(fieldRootName)){//保存表头信息
									saveIPQCForm(report,map);
								}else if("DataSetIPQC_02_02".equals(fieldRootName)){//保存IPQC生产部分信息
									MfgManufactureMessage mF= new MfgManufactureMessage();
									mF.setCreatedTime(new Date());
									mF.setMfgCheckInspectionReport(report);
									mF.setCreator("system");
									mF.setCreatorName("system");
									saveMfgManufactureMessageToIpqc(report,mF, map);
								}else if("DataSetIPQC_02_03".equals(fieldRootName)){//保存IPQC物料信息
									MfgSupplierMessage msm=new MfgSupplierMessage();
									msm.setCreatedTime(new Date());
									msm.setMfgCheckInspectionReport(report);
									msm.setCreator("system");
									msm.setCreatorName("system");
									saveMfgSupplierMessageToIpqc(report,msm, map);
								}
							}
//							session.save(report);
							madeInspectionManager.saveMfgCheckInspectionReport(report);
							returnStr=report.getId().toString();
							result.setMsg("ok");
							result.setStatus("0");
							result.setIdStr(report.getId().toString());
						}
					}else{
						throw new AmbFrameException("没有找到相对应的接口，请检查接口名称是否一致");
					}
				}catch(Exception e){
//					logUtilDao.debugLog("保存ERP更新数据失败", e.getMessage());
					result.setStatus("1");
					result.setMsg("ERROR:"+e.getMessage());
					log.error("ERP推送QIS数据失败:"+e.getMessage());
//					returnStr="<STD_OUT><Service><ServiceId>"+interfaceName+"</ServiceId><Status>1</Status><Error>"+e.getMessage()+"</Error></Service></STD_OUT>";
				}
				return returnStr;
	}
	
	/**
	  * 方法名: 保存IPQC表头信息
	  * <p>功能说明：</p>
	  * @return
	 */
	private void saveIPQCForm(MfgCheckInspectionReport report,Map<String,String> map){
		String fProductCode=map.get("fProductCode");//产品编码
		String fProductName=map.get("fProductName");//产品名称
		String fAmount=map.get("fAmount");//批次数
		String fMachineNo=map.get("fMachineNo");//机种
		report.setCheckBomCode(fProductCode);
		report.setCheckBomName(fProductName);
		if(fAmount==null){
			report.setStockAmount(null);
		}else{
			report.setStockAmount(Integer.valueOf(fAmount));
		}
		report.setMachineNo(fMachineNo);
	}
	
	/**
	  * 方法名: 保存生产信息
	  * <p>功能说明：</p>
	  * @return
	 */
	private void saveMfgManufactureMessageToIpqc(MfgCheckInspectionReport report,MfgManufactureMessage mf,Map<String,String> map) throws Exception{
		String fQcCode=map.get("fQcCode");//流程卡号
		String marchineNo=map.get("marchineNo");//机台
		String fOperator=map.get("fQcCode");//作业员
		String fManufactureTime=map.get("fManufactureTime");//生产日期
		String fWorkOrderNo=map.get("fWorkOrderNo");//工单
		String fSection=map.get("fSection");//工站
		mf.setQcCode(fQcCode);
		mf.setMarchineNo(marchineNo);
		mf.setOperator(fOperator);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(fManufactureTime==null){
			mf.setManufactureTime(null);
		}else{
			mf.setManufactureTime(sdf.parse(fManufactureTime));
		}
		mf.setWorkOrderNo(fWorkOrderNo);
		mf.setSection(fSection);
		report.getManufactureMessages().add(mf);
	}
	
	/**
		  * 方法名: 保存供应商信息
		  * <p>功能说明：</p>
		  * @return
		 */
	private void saveMfgSupplierMessageToIpqc(MfgCheckInspectionReport report,MfgSupplierMessage msm,Map<String,String> map){
		String fSbCode=map.get("fSbCode");//供应商编码
		String fSbBomCode=map.get("fSbBomCode");//物料编号
		String fSbomBatchNo=map.get("fSbomBatchNo");//物料批次号
		String fSbatchNum=map.get("fSbatchNum");//批次库存数
		String fRemark=map.get("fRemark");//备注
		msm.setSupplierCode(fSbCode);
		msm.setSupplierBomCode(fSbBomCode);
		msm.setSupplierBomBatchNo(fSbomBatchNo);
		msm.setSupplierBomBatchNo(fSbatchNum);
		msm.setSupplierRemark(fRemark);
		report.getMfgSupplierMessages().add(msm);
	}
	
	public static void main(String [] args){
		 File file = new File("G:\\QIS项目需求\\欧菲光\\IPQC.xml");
		 String tempStr="<";
		 try {
			FileInputStream  is= new FileInputStream(file);
			InputStreamReader ir= new InputStreamReader(is);
			BufferedReader reader=new BufferedReader(ir);
			 byte b[] = new byte[1024];   
	        while((is.read())!=-1){    //当没有读取完时，继续读取   
	        	tempStr+=reader.readLine();
	        } 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 DownLoadInfo2Impl impl=new DownLoadInfo2Impl();
//		 impl.Download_Info(tempStr);
	}
}  