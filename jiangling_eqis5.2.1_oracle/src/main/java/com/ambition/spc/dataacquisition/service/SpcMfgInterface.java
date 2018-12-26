package com.ambition.spc.dataacquisition.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.spc.entity.QualityFeature;
import com.ambition.spc.entity.SpcSgSample;
import com.ambition.spc.entity.SpcSgTag;
import com.ambition.spc.entity.SpcSubGroup;
import com.ambition.spc.processdefine.service.QualityFeatureManager;
import com.ibm.icu.text.NumberFormat;
import com.norteksoft.product.util.ContextUtils;

@Service
@Transactional
public class SpcMfgInterface {
	@Autowired
	private QualityFeatureManager qualityFeatureManager;
	@Autowired
	private SpcSubGroupManager spcSubGroupManager;
	
	public void importMfgExcelDatas(String[] featureIds,int maxCheckNum,Map<String,String> layersMap,InputStream myFile) throws Exception{
		
		int layersNums = layersMap.size();//附属信息数量
		int featureNums = featureIds.length;
		Double[][] mfgData = new Double[maxCheckNum][featureNums];
		String[][] mfgLayers = new String[layersNums][featureNums];
		
		try{
			org.apache.poi.ss.usermodel.Workbook book = WorkbookFactory.create(myFile);
			org.apache.poi.ss.usermodel.Sheet sheet = book.getSheetAt(0);
			Row row = sheet.getRow(1);
			if(row == null){
				throw new RuntimeException("列名不能为空!");
			}
			Iterator<Row> rows = sheet.rowIterator();
			rows.next();//标题行
			//----------------封装检验数据--------------
			int i = 0;
			while(rows.hasNext()){
					row = rows.next();
					if(i<maxCheckNum){//封装到检验数据数组
						for(int a=1;a<=featureNums;a++){
							org.apache.poi.ss.usermodel.Cell cell = row.getCell(a);
							if(cell != null){
								mfgData[i][a-1] = cell.getNumericCellValue();
								//String ddd= cell.getStringCellValue();
								System.out.println("--------"+cell.getNumericCellValue());
							}
						}
					}else{//封装到附属信息数组
						for(int a=1;a<=featureNums;a++){
							org.apache.poi.ss.usermodel.Cell cell = row.getCell(a);
							if(cell != null){
								if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
									NumberFormat nf = NumberFormat.getInstance();
									nf.setGroupingUsed(false);//true时的格式：1,234,567,890
									Double cellValue = cell.getNumericCellValue();
									String valueStr = nf.format(cellValue);
									mfgLayers[i-maxCheckNum][a-1] = valueStr;
								}else{
									mfgLayers[i-maxCheckNum][a-1] = cell.getStringCellValue();
								}
								System.out.println(mfgLayers[i-maxCheckNum][a-1]);
							}
						}
					}
					i++;
			}
			//---------------------------------------------------------------------------------
			
			
			for(int x=0;x<featureNums;x++){//按质量特性保存数据至SPC
				Long id = Long.valueOf(featureIds[x]);
				QualityFeature qualityFeature = qualityFeatureManager.getQualityFeature(id);
				if(qualityFeature != null){
					int availGroupNum = qualityFeature.getEffectiveCapacity();//有效样本容量
					//封装有效数据，去除NULL\空串\0.0的数据
					List<Double> dataList = new ArrayList<Double>();
					for(int l=0;l<maxCheckNum;l++){
						if(mfgData[l][x]!=null&&!"".equals(mfgData[l][x])&&mfgData[l][x]!=0.0){
							dataList.add(mfgData[l][x]);
						}
							
					}
					System.out.println("----------------"+dataList.size());
					int groups = dataList.size()/availGroupNum;
					for(int c=0;c<groups;c++){//分子组
						int groupStartNum = c*availGroupNum;
						int groupEndNum = (c+1)*availGroupNum-1;
						
						SpcSubGroup spcSubGroup = new SpcSubGroup();
						spcSubGroup.setCompanyId(ContextUtils.getCompanyId());
						spcSubGroup.setCreatedTime(new Date());
						spcSubGroup.setCreator(ContextUtils.getUserName());
						spcSubGroup.setModifiedTime(new Date());
						spcSubGroup.setModifier(ContextUtils.getUserName());
						spcSubGroup.setQualityFeature(qualityFeature);
						spcSubGroup.setSubGroupSize(availGroupNum);
						if(groupEndNum >= groupStartNum){
							spcSubGroup.setActualSmapleNum(groupEndNum-groupStartNum+1);
						}
						int num = spcSubGroupManager.getNumByFeature(qualityFeature) + 1;
						spcSubGroup.setSubGroupOrderNum(num);
						spcSubGroup.setSpcSgSamples(new ArrayList<SpcSgSample>());
						spcSubGroup.setSpcSgTags(new ArrayList<SpcSgTag>());
						Double max =0.00,min = 0.00;
						int sum = 0;
						for(int y=groupStartNum;y<=groupEndNum;y++){
							max = dataList.get(groupStartNum);
							min = max;
							SpcSgSample sample = new SpcSgSample();
							sample.setSamValue(dataList.get(y));
							sample.setSampleNo("X"+String.valueOf(y%availGroupNum+1));
							sample.setSpcSubGroup(spcSubGroup);
							spcSubGroup.getSpcSgSamples().add(sample);
							
							//计算均值、极差、最大值、最小值
							sum += dataList.get(y);
							if(max < dataList.get(y)){
								max = dataList.get(y);
							}
							if(min > dataList.get(y)){
								min = dataList.get(y);
							}
						}
						spcSubGroup.setMaxValue(max);
						spcSubGroup.setMinValue(min);
						spcSubGroup.setRangeDiff(max-min);
						if(availGroupNum != 0){
							spcSubGroup.setSigma(Double.valueOf(sum/availGroupNum));
						}else{
							spcSubGroup.setSigma(0.00);
						}
						
						//-------------保存附属信息--------
						int layerNum = 0;
						for(String key : layersMap.keySet()){
							String attr=layersMap.get(key).toString();
							System.out.println("====="+attr);
							String method = "set"+Character.toUpperCase(attr.charAt(0))+attr.substring(1);
							Method m=spcSubGroup.getClass().getMethod(method,String.class);
							m.invoke(spcSubGroup, mfgLayers[layerNum][x]);
							layerNum++;
						}
						spcSubGroupManager.setLayers(spcSubGroup,qualityFeature);
						
						spcSubGroupManager.save(spcSubGroup);
						
						
					}
				}
			}
			System.out.println("---------ok--------");
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
}
