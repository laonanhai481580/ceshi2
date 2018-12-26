package com.ambition.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.ambition.carmfg.baseinfo.service.InspectionPointManager;
import com.ambition.carmfg.entity.InspectionPoint;
import com.ambition.carmfg.entity.InspectionPointTypeEnum;
import com.ambition.carmfg.entity.InspectionType;
import com.norteksoft.acs.base.web.struts2.Struts2Utils;
import com.norteksoft.product.api.entity.Option;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.opensymphony.xwork2.ActionContext;

public class BaseAction<E> extends CrudActionSupport<E>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private InspectionPointManager inspectionPointManager;
	private String inspectionType = InspectionType.CHECK.name();//数据类型
	private List<Option> listOption;
	private List<InspectionPoint> listInspectionPoint;
	private Map<String,String> workshopTypeMap = new HashMap<String, String>();
	private Long inspectionPointId;
	
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	public List<Option> getListOption() {
		return listOption;
	}
	public void setListOption(List<Option> listOption) {
		this.listOption = listOption;
	}
	public List<InspectionPoint> getListInspectionPoint() {
		return listInspectionPoint;
	}
	public void setListInspectionPoint(List<InspectionPoint> listInspectionPoint) {
		this.listInspectionPoint = listInspectionPoint;
	}
	
	public Map<String, String> getWorkshopTypeMap() {
		return workshopTypeMap;
	}
	public void setWorkshopTypeMap(Map<String, String> workshopTypeMap) {
		this.workshopTypeMap = workshopTypeMap;
	}
	public void renderMenu(){
		//获取当前所在车间
//		String curWorhshop = Struts2Utils.getParameter("workshop");
//		listOption = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_workshop");
		List<Option> options = new ArrayList<Option>();
		//塞选有权限的车间
//		List<Object> workshops = inspectionPointManager.getInspectionPointWorkshops(ContextUtils.getUserId());
//		for(Object obj : workshops){
//			Object[] objs = (Object[])obj;
//			if(objs[0]==null||objs[1]==null){
//				continue;
//			}
//			String workShop = objs[0].toString();
//			Integer orderal = ((InspectionPointTypeEnum)objs[1]).ordinal();
//			if(workshopTypeMap.containsKey(workShop)){
//				workshopTypeMap.put(workShop,workshopTypeMap.get(workShop) + orderal + ",");
//			}else{
//				workshopTypeMap.put(workShop, "," + orderal + ",");
//			}
//		}
//		for(Option option : listOption){
//			if(workshopTypeMap.containsKey(option.getValue())){
//				options.add(option);
//			}
//		}
		listOption = options;
		
		listInspectionPoint = inspectionPointManager.getInspectionPointByInspector(ContextUtils.getUserId());
		//获取当前车间的所有检查数据采集点
		List<InspectionPoint> firstInspectionPointList =new ArrayList<InspectionPoint>();
		List<InspectionPoint> patrolInspectionPointList =new ArrayList<InspectionPoint>();
		List<InspectionPoint> completeInspectionPointList =new ArrayList<InspectionPoint>();
		List<InspectionPoint> storageInspectionPointList =new ArrayList<InspectionPoint>();
		List<InspectionPoint> deliverInspectionPointList =new ArrayList<InspectionPoint>();
		List<InspectionPoint> productCheckPointList =new ArrayList<InspectionPoint>();
		List<InspectionPoint> productDaliyPointList =new ArrayList<InspectionPoint>();
		for(InspectionPoint point : listInspectionPoint){
			if(point.getListType()==null){
				continue;
			}
			if(InspectionPointTypeEnum.COMPLETEINSPECTION.getCode().equals(point.getListType().getCode())){
				completeInspectionPointList.add(point);
			}else if(InspectionPointTypeEnum.DELIVERINSPECTION.getCode().equals(point.getListType().getCode())){
				deliverInspectionPointList.add(point);
			}else if(InspectionPointTypeEnum.FIRSTINSPECTION.getCode().equals(point.getListType().getCode())){
				firstInspectionPointList.add(point);
			}else if(InspectionPointTypeEnum.PATROLINSPECTION.getCode().equals(point.getListType().getCode())){
				patrolInspectionPointList.add(point);
			}else if(InspectionPointTypeEnum.PRODUCTCHECK.getCode().equals(point.getListType().getCode())){
				productCheckPointList.add(point);
			}else if(InspectionPointTypeEnum.PRODUCTDALIYREPORT.getCode().equals(point.getListType().getCode())){
				productDaliyPointList.add(point);
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(point.getListType().getCode())){
				storageInspectionPointList.add(point);
			}
		}
		//完工检验集合
		Struts2Utils.getRequest().setAttribute("completeInspectionPointList",completeInspectionPointList);
		//出货检验集合(制程抽检)
		Struts2Utils.getRequest().setAttribute("deliverInspectionPointList",deliverInspectionPointList);
		//首检采集点集合
		Struts2Utils.getRequest().setAttribute("firstInspectionPointList",firstInspectionPointList);
		//巡检采集点集合
		Struts2Utils.getRequest().setAttribute("patrolInspectionPointList",patrolInspectionPointList);
		//生产检查采集点集合
		Struts2Utils.getRequest().setAttribute("productCheckPointList",productCheckPointList);
		//生产日报采集点集合
		Struts2Utils.getRequest().setAttribute("productDaliyPointList",productDaliyPointList);
		//入库检验采集点集合
		Struts2Utils.getRequest().setAttribute("storageInspectionPointList",storageInspectionPointList);
		this.setListOption(listOption);
		this.setListInspectionPoint(listInspectionPoint);
		
		//生成菜单
		List<Map<String,Object>> inputPoints = new ArrayList<Map<String,Object>>();
		Map<String,Boolean> hasExist = new HashMap<String,Boolean>();
		for(InspectionPoint point : listInspectionPoint){
			if(InspectionPointTypeEnum.FIRSTINSPECTION.getCode().equals(point.getListType().getCode())){
				String id = "firstInspectionRecord";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",1);
					map.put("thirdUrl","/inspection/first-inspection/list.htm");
					map.put("name","首检数据采集");
					inputPoints.add(map);
				}
			}else if(InspectionPointTypeEnum.PATROLINSPECTION.getCode().equals(point.getListType().getCode())){
				String id = "patrolTemporary";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",2);
					map.put("thirdUrl","/patrol-inspection/temporary-list.htm");
					map.put("name","巡检报告");
					inputPoints.add(map);
				}
			}else if(InspectionPointTypeEnum.COMPLETEINSPECTION.getCode().equals(point.getListType().getCode())){
				String id = "storageInspectionRecord";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",3);
					map.put("thirdUrl","/inspection/storage-inspection/list.htm");
					map.put("name","末检检验采集");
					inputPoints.add(map);
				}
			}else if(InspectionPointTypeEnum.STORAGEINSPECTION.getCode().equals(point.getListType().getCode())){
				String id = "storageInspectionRecord";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",6);
					map.put("thirdUrl","/inspection/storage-inspection/list.htm");
					map.put("name","末检检验报告");
					inputPoints.add(map);
				}
			}else if(InspectionPointTypeEnum.DELIVERINSPECTION.getCode().equals(point.getListType().getCode())){
				String id = "deliverInspectionRecord";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",7);
					map.put("thirdUrl","/inspection/deliver-inspection/list.htm");
					map.put("name","制程抽检报告");
					inputPoints.add(map);
				}
			}else if(InspectionPointTypeEnum.PRODUCTCHECK.getCode().equals(point.getListType().getCode())){
				String id = "checkRecord";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",8);
					map.put("thirdUrl","/inspection/inprocess-inspection/list.htm");
					map.put("name","生产检查记录");
					inputPoints.add(map);
				}
			}else if(InspectionPointTypeEnum.PRODUCTDALIYREPORT.getCode().equals(point.getListType().getCode())){
				String id = "daliyPoduceReport";
				if(!hasExist.containsKey(id)){
					hasExist.put(id,true);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id",id);
					map.put("index",4);
					map.put("thirdUrl","/inspection/daliy-report/list.htm");
					map.put("name","生产日报表");
					inputPoints.add(map);
				}
			}
		}
		//按照显示顺序排序
		for(int i=0;i<inputPoints.size();i++){
			for(int j=i+1;j<inputPoints.size();j++){
				int index1 = (Integer)inputPoints.get(i).get("index");
				int index2 = (Integer)inputPoints.get(j).get("index");
				if(index1>index2){
					Map<String,Object> tempMap = inputPoints.get(i);
					inputPoints.set(i,inputPoints.get(j));
					inputPoints.set(j,tempMap);
				}
			}
		}
		ActionContext.getContext().put("inputPoints", inputPoints);
	}
	@Override
	public E getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 创建返回消息
	 * @param error
	 * @param message
	 * @return
	 */
	public void createErrorMessage(String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("error",true);
		map.put("message",message);
		renderText(JSONObject.fromObject(map).toString());
	}
	
}
