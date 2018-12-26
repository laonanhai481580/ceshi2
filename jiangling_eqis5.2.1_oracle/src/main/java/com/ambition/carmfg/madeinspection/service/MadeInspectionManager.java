package com.ambition.carmfg.madeinspection.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.carmfg.baseinfo.service.FormCodeGenerated;
import com.ambition.carmfg.checkinspection.service.MfgCheckInspectionReportManager;
import com.ambition.carmfg.checkinspection.web.MfgCheckInspectionReportAction;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.MfgCheckInspectionReport;
import com.ambition.carmfg.entity.MfgCheckItem;
import com.ambition.carmfg.entity.MfgManufactureMessage;
import com.ambition.carmfg.entity.MfgPlantParameterItem;
import com.ambition.carmfg.entity.MfgSupplierMessage;
import com.ambition.carmfg.madeinspection.dao.MadeInspectionDao;
import com.ambition.carmfg.madeinspection.dao.MfgManufactureMessageDao;
import com.ambition.carmfg.madeinspection.dao.MfgSupplierMessageDao;
import com.ambition.iqc.entity.MfgToMes;
import com.ambition.spc.dataacquisition.dao.SpcSgSampleDao;
import com.ambition.spc.dataacquisition.dao.SpcSubGroupDao;
import com.ambition.spc.entity.FeatureLayer;
import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.importutil.service.SpcImportManager;
import com.ambition.spc.processdefine.dao.QualityFeatureDao;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ambition.supplier.utils.DateUtil;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.ambition.webservice.QisToBackService;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.api.entity.Opinion;
import com.norteksoft.product.api.entity.WorkflowTask;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;
import com.norteksoft.product.web.wf.WorkflowManagerSupport;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.wf.engine.client.EndInstanceInterface;
import com.norteksoft.wf.engine.client.FormFlowableDeleteInterface;
import com.norteksoft.wf.engine.client.RetrieveTaskInterface;

@Service
@Transactional
public class MadeInspectionManager extends WorkflowManagerSupport<MfgCheckInspectionReport> implements FormFlowableDeleteInterface,RetrieveTaskInterface,
EndInstanceInterface{
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MadeInspectionDao madeInspectionDao;
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private FormCodeGenerated formCodeGenerated;
	@Autowired
	private MfgManufactureMessageDao mfgManufactureMessageDao;
	@Autowired
	private MfgSupplierMessageDao mfgSupplierMessageDao;
	@Autowired
	private QualityFeatureDao qualityFeatureDao;
	@Autowired
	private SpcSgSampleDao spcSgSampleDao;
	@Autowired
	private SpcSubGroupDao spcSubGroupDao;
	@Autowired
	private SpcImportManager spcImportManager;
	@Autowired
	private MfgCheckInspectionReportManager mfgCheckInspectionReportManager;
	/**
	  * 方法名: 查询物料信息
	  * <p>功能说明：</p>
	  * @return
	 */
	@Transactional(readOnly=true)
	public Page<MfgSupplierMessage> searchMfgSupplierMessagePage(Page<MfgSupplierMessage> page){
		return mfgSupplierMessageDao.searchMfgSupplierMessage(page);
	}
	
	@Transactional(readOnly=true)
	public Page<MfgManufactureMessage> searchMfgManufactureMessagePage(Page<MfgManufactureMessage> page){
		return mfgManufactureMessageDao.searchMfgManufactureMessage(page);
	}
	
	public static final String MFG_CODE="mfg-made-workflow-id";
	
	public MfgCheckInspectionReport getMfgCheckInspectionReport(Long id){
		return madeInspectionDao.get(id);
	}
	
	public void saveMfgCheckInspectionReport(MfgCheckInspectionReport mfgCheckInspectionReport){
		madeInspectionDao.save(mfgCheckInspectionReport);
	}
	
	@Override
	public void endInstanceExecute(Long arg0) {
		
	}

	@Override
	public void retrieveTaskExecute(Long arg0, Long arg1) {
		
	}

	@Override
	public void deleteFormFlowable(Long id) {
		madeInspectionDao.delete(id);
	}

	@Override
	protected MfgCheckInspectionReport getEntity(Long id) {
		return madeInspectionDao.get(id);
	}

	@Override
	protected void saveEntity(MfgCheckInspectionReport mfgCheckInspectionReport) {
		madeInspectionDao.save(mfgCheckInspectionReport);
	}
	 public String deleteMfgCheckInspectionReport(String deleteIds){
	        String [] deleteId=deleteIds.split(",");
	        String message = "";
	        Integer deleteNum = 0;
	        Integer dontDeleteNum = 0;
	        StringBuilder sb = new StringBuilder("");
	        Session	session = qualityFeatureDao.getSessionFactory().openSession();
	        for(String id:deleteId){
	        	MfgCheckInspectionReport apply = madeInspectionDao.get(Long.valueOf(id));
	        	if(ContextUtils.getUserName().equals(apply.getCreator())){
	        		deleteNum++;
	        		List<MfgCheckItem> checkItems=apply.getCheckItems();
	        		for (MfgCheckItem checkItem : checkItems) {
	        			String featureId=checkItem.getFeatureId();
	        			QualityFeature qualityFeature=null;
	        			if(featureId!=null&&!featureId.equals("")){
	        				qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(checkItem.getFeatureId()));
	        			}	        			
	        			String spcSampleIds = checkItem.getSpcSampleIds();
	        			if(spcSampleIds!=null&&qualityFeature!=null&&StringUtils.isNotEmpty(qualityFeature.getTargetTableName())){
			        		String[] spcArr = spcSampleIds.split(",");
			        		String delSql = "delete from " + qualityFeature.getTargetTableName() + " where id = ?";
			        		for(String spcSampId:spcArr){
			        			if(StringUtils.isNotEmpty(spcSampId)&&!" ".equals(spcSampId)){
			        				session
									.createSQLQuery(delSql)
									.setParameter(0,spcSampId)
									.executeUpdate();
			        			}
			        		}			                
			        	}
					}
	        		//删除旧版spc数据
        			/*String spcSampleIds = apply.getSpcSampleIds();
		        	if(spcSampleIds!=null){
		        		String[] spcArr = spcSampleIds.split(",");
		        		Long spcSubGroupId = null;
		        		for(String spcSampId:spcArr){
		        			if(StringUtils.isNotEmpty(spcSampId)&&!" ".equals(spcSampId)){
		        				SpcSgSample spcs = spcSgSampleDao.get(Long.valueOf(spcSampId));
		        				if(spcs!=null){
		        					spcSubGroupId = spcs.getSpcSubGroup().getId();
		        					if(spcSubGroupId!=null){
		    		                	spcSubGroupDao.delete(spcSubGroupId);
		    		                }
		        				}
		        			}
		        		}		                
		        	}*/
		        	if(apply.getWorkflowInfo() != null){
		        		ApiFactory.getInstanceService().deleteInstance(apply);
		        	}else{
		        		madeInspectionDao.delete(apply);
		        		sb.append(apply.getInspectionNo() + ",");
		        	}
	        	}else{
	        		dontDeleteNum++;
	        	}
	        }
	        session.close();
	        message =  sb.toString()+"~"+deleteNum+"条已删除，"+dontDeleteNum+"条无权限删除";
	        return message;
	    }
	/**
	  * 方法名: 提交发起流程
	  * <p>功能说明：</p>
	  * @return
	 */
	public boolean submitProcess(MfgCheckInspectionReport report,List<JSONObject> checkItemArray,InspectionPointTypeEnum typeEnum,
			JSONArray messagesArray,JSONArray manufactureArray,List<JSONObject> parameterItems) throws Exception{
		saveMadeInspection(report,checkItemArray,typeEnum,messagesArray,manufactureArray,parameterItems);
		boolean isSubmit=true;
		for(MfgCheckItem item:report.getCheckItems()){
			if(item.getItemStatus().equals("未领取")&&"欧菲科技-神奇工场".equals(ContextUtils.getCompanyName())){
				isSubmit=false;
				break;
			}
		}
		if(isSubmit){
			Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(MFG_CODE).get(0).getId();
			//System.out.println(processId);
			ApiFactory.getInstanceService().submitInstance(processId,report);
			report.setReportState(MfgCheckInspectionReport.STATE_AUDIT);
			this.saveEntity(report);
		}
		if(isSubmit){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	  * 方法名: 提交发起流程
	  * <p>功能说明：</p>
	  * @return
	 */
	public boolean submitProcess(MfgCheckInspectionReport report) throws Exception{
		boolean isSubmit=true;
		for(MfgCheckItem item:report.getCheckItems()){
			if(item.getItemStatus().equals("未领取")&&"欧菲科技-神奇工场".equals(ContextUtils.getCompanyName())){
				isSubmit=false;
				break;
			}
		}
		if(isSubmit){
			Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(MFG_CODE).get(0).getId();
			//System.out.println(processId);
			ApiFactory.getInstanceService().submitInstance(processId,report);
			report.setReportState(MfgCheckInspectionReport.STATE_AUDIT);
			this.saveEntity(report);
		}
		if(isSubmit){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	  * 方法名:流程办理
	  * <p>功能说明：</p>
	  * @return
	 * @throws Exception 
	 */
	public void completeTaskCode(MfgCheckInspectionReport report,Long taskId,TaskProcessingResult taskTransact) throws Exception{
		madeInspectionDao.save(report);
		List<JSONObject> checkItemArray = MfgCheckInspectionReportAction.getRequestCheckItems();
		ApiFactory.getTaskService().completeWorkflowTask(taskId,taskTransact);
		String processResult = "";
        if("REFUSE".equals(taskTransact.name())){
        	processResult = "不同意";
        	report.setReportState(MfgCheckInspectionReport.STATE_DEFAULT);
        }else  if("SUBMIT".equals(taskTransact.name())){
        	processResult = "提交";
        	report.setReportState(MfgCheckInspectionReport.STATE_AUDIT);
        }else if("APPROVE".equals(taskTransact.name())){
        	report.setReportState(MfgCheckInspectionReport.STATE_COMPLETE);
        	processResult = "同意";
        	String allHisPatrolSpcSampleIds = report.getSpcSampleIds()==null?"":report.getSpcSampleIds();
    		String allPatrolSpcSampleIds = ",";
    		Map<String,QualityFeature> featureMap = new HashMap<String, QualityFeature>();
    		String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
//    		boolean isOver=true;
    		if(checkItemArray != null){
    			report.getCheckItems().clear();
    			for(JSONObject json : checkItemArray){
    				MfgCheckItem checkItem = new MfgCheckItem();
    				checkItem.setCompanyId(ContextUtils.getCompanyId());
    				checkItem.setCreatedTime(new Date());
    				checkItem.setCreator(ContextUtils.getUserName());
    				checkItem.setLastModifiedTime(new Date());
    				checkItem.setLastModifier(ContextUtils.getUserName());
    				//以下用于SPC推送数据
    				String featureId = null,countType = null;
    				String values = "";
    				List<Double> valueList=new ArrayList<Double>();
    				for(Object key : json.keySet()){
    					String value = json.getString(key.toString());
    					setProperty(checkItem, key.toString(),value);
    					if("featureId".equals(key.toString())){
    						featureId = value;
    					}
    					if(!"results".equals(key.toString())&& key.toString().indexOf("result")>=0){
    						if(value != null && !value.equals("")){
    							values += value + ",";
    							valueList.add(Double.valueOf(value));
    						}
    					}
    					if("countType".equals(key.toString())){
    						countType = value;
    						if(!countType.contains("计量")){
    							values = "";
    						}
    					}					
    				}
//    				if(checkItem.getItemStatus().equals("已领取")){
//    					checkItem.setInspector(ContextUtils.getUserName());
//    				}else{
//    					isOver=false;
//    				}
    				checkItem.setCheckBomCode(report.getCheckBomCode());
    				checkItem.setCheckBomName(report.getCheckBomName());
    				checkItem.setWorkProcedure(report.getWorkProcedure());
    				checkItem.setInspectionDate(report.getInspectionDate());
    				checkItem.setMfgCheckInspectionReport(report);
    				report.getCheckItems().add(checkItem);
    				
    				String spcSampleIds = "";
    				//处理spc数据
    				if("计量".equals(countType) && StringUtils.isNotEmpty(featureId)){
    					List<String> hisIds=new ArrayList<String>();
    					if(valueList.size()>0){
    						if(checkItem.getSpcSampleIds()!=null&&!"".equals(checkItem.getSpcSampleIds())){
    							String hisSampleIds = checkItem.getSpcSampleIds();
        						String [] deleteId=hisSampleIds.split(",");
        				        for(String id:deleteId){
        				        	if(!id.equals("")&&id!=null){
        				        		hisIds.add(id);
        				        	}   				        	
        				        }
    						}   
    						//采集层别信息
    						QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(Long.valueOf(featureId));
    						Map<String,String> layerMap=new HashMap<String, String>();
    						for(FeatureLayer layer : qualityFeature.getFeatureLayers()){
    							if(layer.getDetailName().equals("机台")&&StringUtils.isNotEmpty(checkItem.getEquipmentNo())){
    								layerMap.put(layer.getDetailCode(), checkItem.getEquipmentNo());
    							}else if(layer.getDetailName().equals("班组")&&StringUtils.isNotEmpty(report.getWorkGroupType())){
    								layerMap.put(layer.getDetailCode(), report.getWorkGroupType());
    							}else if(layer.getDetailName().equals("检验员")&&StringUtils.isNotEmpty(report.getInspector())){
    								layerMap.put(layer.getDetailCode(), report.getInspector());
    							}else if(layer.getDetailName().equals("检验")){
    								String inspectionType="";
    								if(InspectionPointTypeEnum.FIRSTINSPECTION.equals(report.getInspectionPointType())){
    									inspectionType = "首检";
    								}else if(InspectionPointTypeEnum.PATROLINSPECTION.equals(report.getInspectionPointType())){
    									inspectionType = "巡检";
    								}else if(InspectionPointTypeEnum.COMPLETEINSPECTION.equals(report.getInspectionPointType())){
    									inspectionType = "末检";
    								}
    								layerMap.put(layer.getDetailCode(), inspectionType);
    							}
    						} 
    				        //List<String> list=spcImportManager.backImportValues(Long.valueOf(featureId), hisIds, valueList, null);
    						List<String> list=spcImportManager.backImportValues(Long.valueOf(featureId),report.getInspectionDate(),null,null,hisIds,valueList, null);
        					for (String string : list) {
        						spcSampleIds+=string+",";
    						}
    					}
    				}
    				checkItem.setSpcSampleIds(spcSampleIds);
    			}
    		}
        }else{
        	processResult = "";
        }
		String opinion = Struts2Utils.getParameter("opinion");
		if(StringUtils.isNotEmpty(opinion)){
			WorkflowTask task = ApiFactory.getTaskService().getTask(taskId);
			//保存记录
			Opinion opinionParameter = new Opinion();
			opinionParameter.setCustomField(processResult);
	        opinionParameter.setOpinion(opinion);
	        opinionParameter.setTransactor(ContextUtils.getLoginName());
	        opinionParameter.setTransactorName(ContextUtils.getUserName());
	        opinionParameter.setTaskName(task.getName());
	        opinionParameter.setTaskId(taskId);
	        opinionParameter.setAddOpinionDate(new Date());
	        ApiFactory.getOpinionService().saveOpinion(opinionParameter);
		}
	}
	
	 /**
	  * 方法名: 保存检验记录
	  * <p>功能说明：</p>
	  * @return
	 */
	public void saveMadeInspection(MfgCheckInspectionReport incomingInspectionActionsReport,List<JSONObject> checkItemArray,
			InspectionPointTypeEnum typeEnum,JSONArray messagesArray,JSONArray manufactureArray,List<JSONObject> parameterItems) throws Exception{
		if(!(MfgCheckInspectionReport.STATE_DEFAULT.equals(incomingInspectionActionsReport.getReportState())
			||MfgCheckInspectionReport.STATE_RECHECK.equals(incomingInspectionActionsReport.getReportState()))){
			throw new AmbFrameException("只能保存状态为【"+MfgCheckInspectionReport.STATE_DEFAULT+"】" +
					"或【"+MfgCheckInspectionReport.STATE_RECHECK+"】的检验报告!");
		}
		if(incomingInspectionActionsReport.getId() == null){
			incomingInspectionActionsReport.setCheckItems(new ArrayList<MfgCheckItem>());
			incomingInspectionActionsReport.setMfgPlantParameterItems(new ArrayList<MfgPlantParameterItem>());
			incomingInspectionActionsReport.setInspectionNo(formCodeGenerated.generateMFGode());
			incomingInspectionActionsReport.setMfgSupplierMessages(new ArrayList<MfgSupplierMessage>());
		}else{
			incomingInspectionActionsReport.setLastModifiedTime(new Date());
			incomingInspectionActionsReport.setLastModifier(ContextUtils.getUserName());
		}
		//setMfgSupplierMessage(incomingInspectionActionsReport,messagesArray,manufactureArray);
		if(parameterItems!=null){
			incomingInspectionActionsReport.getMfgPlantParameterItems().clear();
			for(JSONObject json : parameterItems){
				MfgPlantParameterItem mpi = new MfgPlantParameterItem();
				mpi.setCompanyId(ContextUtils.getCompanyId());
				mpi.setCreatedTime(new Date());
				mpi.setCreator(ContextUtils.getUserName());
				mpi.setLastModifiedTime(new Date());
				mpi.setLastModifier(ContextUtils.getUserName());
				String values = "";
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(mpi, key.toString(),value);
					if(!"results".equals(key.toString())&& key.toString().indexOf("result")>=0){
						if(value != null && !value.equals("")){
							values += value + ",";
						}
					}
				}
				mpi.setMfgCheckInspectionReport(incomingInspectionActionsReport);
				incomingInspectionActionsReport.getMfgPlantParameterItems().add(mpi);
			}
		}
		String allHisPatrolSpcSampleIds = incomingInspectionActionsReport.getSpcSampleIds()==null?"":incomingInspectionActionsReport.getSpcSampleIds();
		String allPatrolSpcSampleIds = ",";
		Map<String,QualityFeature> featureMap = new HashMap<String, QualityFeature>();
		String inspectionPointType=Struts2Utils.getParameter("inspectionPointType");
//		boolean isOver=true;
		if(checkItemArray != null){
			incomingInspectionActionsReport.getCheckItems().clear();
			for(JSONObject json : checkItemArray){
				MfgCheckItem checkItem = new MfgCheckItem();
				checkItem.setCompanyId(ContextUtils.getCompanyId());
				checkItem.setCreatedTime(new Date());
				checkItem.setCreator(ContextUtils.getUserName());
				checkItem.setLastModifiedTime(new Date());
				checkItem.setLastModifier(ContextUtils.getUserName());
				//以下用于SPC推送数据
				String featureId = null,countType = null;
				String values = "";
				for(Object key : json.keySet()){
					String value = json.getString(key.toString());
					setProperty(checkItem, key.toString(),value);
					if("featureId".equals(key.toString())){
						featureId = value;
					}
					if(!"results".equals(key.toString())&& key.toString().indexOf("result")>=0){
						if(value != null && !value.equals("")){
							values += value + ",";
						}
					}
					if("countType".equals(key.toString())){
						countType = value;
						if(!countType.contains("计量")){
							values = "";
						}
					}					
				}
//				if(checkItem.getItemStatus().equals("已领取")){
//					checkItem.setInspector(ContextUtils.getUserName());
//				}else{
//					isOver=false;
//				}
				checkItem.setCheckBomCode(incomingInspectionActionsReport.getCheckBomCode());
//				checkItem.setResults(values);
				checkItem.setCheckBomName(incomingInspectionActionsReport.getCheckBomName());
				checkItem.setWorkProcedure(incomingInspectionActionsReport.getWorkProcedure());
				checkItem.setInspectionDate(incomingInspectionActionsReport.getInspectionDate());
				checkItem.setMfgCheckInspectionReport(incomingInspectionActionsReport);
				incomingInspectionActionsReport.getCheckItems().add(checkItem);
				//处理spc数据
				/*String spcSampleIds = "";
				if("FIRSTINSPECTION".equals(inspectionPointType)){
					if("计量".equals(countType) && StringUtils.isNotEmpty(featureId)){
						//spcSubGroupManager.saveGroupFromMfg(featureId,datas);
						if(StringUtils.isNotEmpty(values)){
							if(!featureMap.containsKey(checkItem.getFeatureId())){
								List<QualityFeature> features = qualityFeatureDao.find("from QualityFeature q where q.id = ?",Long.valueOf(checkItem.getFeatureId()));
				    			if(features.isEmpty()){
				    				featureMap.put(checkItem.getFeatureId(),null);
				    			}else{
				    				featureMap.put(checkItem.getFeatureId(),features.get(0));
				    			}
							}
							QualityFeature feature = featureMap.get(checkItem.getFeatureId());
							if(feature != null){
								String hisSampleIds = checkItem.getSpcSampleIds();
								if(StringUtils.isNotEmpty(hisSampleIds)){
									allHisPatrolSpcSampleIds = allHisPatrolSpcSampleIds.replaceAll("," + hisSampleIds,",");
								}
//								spcSampleIds = mfgCheckInspectionReportManager.insertSpcByResults(feature,checkItem.getCreatedTime(),hisSampleIds,values,incomingInspectionActionsReport.getInspectionPointType());
							}
							allPatrolSpcSampleIds += spcSampleIds;
						}
					
					}
					checkItem.setSpcSampleIds(spcSampleIds);
				}*/
			}
		}
		//删除不存在采集数据的子组(全局调用，涉及报告的修改，存在删除问题)
		/*if("FIRSTINSPECTION".equals(inspectionPointType)){
			String[] ids = allHisPatrolSpcSampleIds.split(",");
			for(String id : ids){
				id = id.trim();
				if(StringUtils.isNotEmpty(id)){
					List<SpcSgSample> list = spcSgSampleDao.find("from SpcSgSample s where s.id = ?",Long.valueOf(id));
					if(!list.isEmpty()){
						SpcSgSample sample = list.get(0);
						spcSubGroupDao.delete(sample.getSpcSubGroup());
					}
				}
			}
			//设置最新的采集数据ID
			incomingInspectionActionsReport.setSpcSampleIds(allPatrolSpcSampleIds);
		}*/
		
		int qualifiedAmount = incomingInspectionActionsReport.getQualifiedAmount()==null?0:incomingInspectionActionsReport.getQualifiedAmount();
		int inspectionAmount = incomingInspectionActionsReport.getInspectionAmount()==null?0:incomingInspectionActionsReport.getInspectionAmount();
		if(inspectionAmount==0){
			incomingInspectionActionsReport.setQualifiedRate(1f);
		}else{
			incomingInspectionActionsReport.setQualifiedRate(qualifiedAmount/(inspectionAmount*1.0f));
		}
//		if(isOver){
//			incomingInspectionActionsReport.setReportState(MfgCheckInspectionReport.STATE_AUDIT);
//			Long processId = ApiFactory.getDefinitionService().getWorkflowDefinitionsByCode(MFG_CODE).get(0).getId();
//			ApiFactory.getInstanceService().submitInstance(processId,incomingInspectionActionsReport);
//		}
//		if("true".equals(Struts2Utils.getParameter("isSubmit"))){
//			if("OK".equals(incomingInspectionActionsReport.getInspectionConclusion())){
//				incomingInspectionActionsReport.setReportState(MfgCheckInspectionReport.STATE_COMPLETE);
//			}else{
//				incomingInspectionActionsReport.setReportState(MfgCheckInspectionReport.STATE_AUDIT);
//			}
//		}
		madeInspectionDao.save(incomingInspectionActionsReport);
		if(incomingInspectionActionsReport.getInspectionDate()==null){
			throw new AmbFrameException("检验日期为空,请重新提交");
		}
	}
	
	/**
	  * 方法名:获取设备参数检验项 
	  * <p>功能说明：</p>
	  * @return
	 */
	public Map<String,List<MfgCheckItem>> getPlantParameterItems(String machineNo,String workProcedure){
		return null;
	}
	
	/**
		  * 方法名: 
		  * <p>功能说明：保存供应商物料信息</p>
		  * @return
		 */
	private void setMfgSupplierMessage(MfgCheckInspectionReport report,JSONArray messagesArray,JSONArray manufactureArray){
		if(messagesArray==null){
            return ;
        }
		report.getMfgSupplierMessages().clear();
		int len = messagesArray.size();
		MfgSupplierMessage sm=null;
        for(int i=0;i<len;i++){
            JSONObject json=messagesArray.getJSONObject(i);
            sm = new MfgSupplierMessage();
            sm.setCompanyId(ContextUtils.getCompanyId());
            sm.setCreatedTime(new Date());
            try {
                for(Object key : json.keySet()){
                    String value = json.getString(key.toString());
                    setProperty(sm,key.toString(),value);
                }
                sm.setMfgCheckInspectionReport(report);
                report.getMfgSupplierMessages().add(sm);
            } catch (Exception e) {
                throw new AmbFrameException("保存供应商物料信息!",e);
            }
        }
        if(manufactureArray==null){
            return ;
        }
        report.getManufactureMessages().clear();
		int lenM = manufactureArray.size();
		MfgManufactureMessage mm=null;
        for(int i=0;i<lenM;i++){
            JSONObject json=manufactureArray.getJSONObject(i);
            mm = new MfgManufactureMessage();
            mm.setCompanyId(ContextUtils.getCompanyId());
            mm.setCreatedTime(new Date());
            try {
                for(Object key : json.keySet()){
                    String value = json.getString(key.toString());
                    setProperty(mm,key.toString(),value);
                }
                mm.setMfgCheckInspectionReport(report);
                report.getManufactureMessages().add(mm);
            } catch (Exception e) {
                throw new AmbFrameException("保存生产物料信息!",e);
            }
        }
	}
	
	private void setProperty(Object obj, String property, Object value) throws Exception {
		String fieldName = property,customType = null;
		if(property.indexOf("_")>0){
			String[] strs = property.split("_");
			fieldName = strs[0];
			customType = strs[1];
		}
		Class<?> type = PropertyUtils.getPropertyType(obj, fieldName);
		if (type != null) {
			if (value == null || StringUtils.isEmpty(value.toString())) {
				PropertyUtils.setProperty(obj, fieldName, null);
			} else {
				if("timestamp".equals(customType)){
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value.toString()));
				}else if (String.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName, value.toString());
				} else if (Integer.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Integer.valueOf(value.toString()));
				} else if (Double.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Double.valueOf(value.toString()));
				} else if (Float.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Float.valueOf(value.toString()));
				} else if (Boolean.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,Boolean.valueOf(value.toString()));
				} else if (Date.class.getName().equals(type.getName())) {
					PropertyUtils.setProperty(obj, fieldName,DateUtil.parseDateTime(value+""));
				} else {
					PropertyUtils.setProperty(obj, fieldName, value);
				}
			}
		}
	}
	 
    /**
     * 得到所有意见集合
     */
    public List<Opinion> getOpinions(MfgCheckInspectionReport mfgCheckInspectionReport) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        return ApiFactory.getOpinionService().getOpinions(mfgCheckInspectionReport);
    }
    /**
     * 流程任务ID获取类
     * @param taskId
     * @return
     */
    public MfgCheckInspectionReport  getMfgCheckInspectionReportByTaskId(Long taskId){
        if(taskId == null) return null;
        MfgCheckInspectionReport mfgCheckInspectionReport=null;
        Long id =ApiFactory.getFormService().getFormFlowableIdByTask(taskId);
        mfgCheckInspectionReport=getEntity(id);
        return mfgCheckInspectionReport;
    }

	public String mfgToBarch(QisToBackService qbs, MfgToMes mes) throws Exception {
		// TODO Auto-generated method stub
		return qbs.mfgToBarch(mes);
	}
	 /**
     * 方法名:导出Excel文件 
     * <p>功能说明：</p>
     * @param incomingInspectionActionsReport
     * @throws IOException
    */
   public void exportReport(MfgCheckInspectionReport s) throws IOException{
       InputStream inputStream = null;
       try {
    	   MfgCheckInspectionReport report = s;                                                
           inputStream = this.getClass().getClassLoader().getResourceAsStream("template/report/mfg-inspection-report.xlsx");
           Map<String,ExportExcelFormatter> formatterMap = new HashMap<String, ExportExcelFormatter>();
           //单号格式化
           formatterMap.put("code",new ExportExcelFormatter() {
			@Override
			public String format(Object value, int rowNum, String fieldName,
					Cell cell) {
				 MfgCheckInspectionReport inspectionReport = (MfgCheckInspectionReport)value;
                 return "编号:" + inspectionReport.getInspectionNo();
			}
           });
        
           String exportFileName = "市场问题改进表单";
           ExcelUtil.exportToExcel(inputStream, exportFileName, report, formatterMap);
       }catch (Exception e) {
           log.error("导出失败!",e);
       } finally{
           if(inputStream != null){
               inputStream.close();
           }
       }
   }

	public String getReportIdBySpcSmapleId(String spcSampleId) {
		// TODO Auto-generated method stub
		String reportId = "";
		String sql = "select FK_MFG_REPORT_ID from mfg_check_item t where t.spc_sample_ids like ? ";
		@SuppressWarnings("unchecked")
		List<Object> objs = madeInspectionDao.getSession().createSQLQuery(sql).setParameter(0,"%"+spcSampleId+"%").list();
		if(objs.size()!=0){
			reportId = objs.get(0).toString();
		}
		return reportId;
		
	}
}
