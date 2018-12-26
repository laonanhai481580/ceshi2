package com.ambition.aftersales.baseinfo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ambition.aftersales.baseinfo.dao.VlrrWarmingDao;
import com.ambition.aftersales.entity.LarTarget;
import com.ambition.aftersales.entity.VlrrData;
import com.ambition.aftersales.entity.VlrrWarming;
import com.ambition.util.common.ExcelUtil;
import com.ambition.util.exception.AmbFrameException;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.AsyncMailUtils;
import com.norteksoft.product.util.ContextUtils;

/**
 * 
 * 类名:VLRR机种+目标值维护Manager
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * <p>功能说明：</p>
 * @author  LPF
 * @version 1.00 2016年9月21日 发布
 */
@Service
@Transactional
public class VlrrWarmingManager {
	@Autowired
	private VlrrWarmingDao vlrrWarmingDao;	
	public VlrrWarming getVlrrWarming(Long id){
		return vlrrWarmingDao.get(id);
	}
	
	public void deleteVlrrWarming(VlrrWarming vlrrWarming){
		vlrrWarmingDao.delete(vlrrWarming);
	}

	public Page<VlrrWarming> search(Page<VlrrWarming>page){
		return vlrrWarmingDao.search(page);
	}

	public List<VlrrWarming> listAll(){
		return vlrrWarmingDao.getAllVlrrWarming();
	}
		
	public void deleteVlrrWarming(Long id){
		vlrrWarmingDao.delete(id);
	}
	public void deleteVlrrWarming(String ids) {
		String[] deleteIds = ids.split(",");
		for (String id : deleteIds) {
			VlrrWarming  vlrrWarming = vlrrWarmingDao.get(Long.valueOf(id));
			if(vlrrWarming.getId() != null){
				vlrrWarmingDao.delete(vlrrWarming);
			}
		}
	}
	public void saveVlrrWarming(VlrrWarming vlrrWarming){
		vlrrWarmingDao.save(vlrrWarming);
	}

	public VlrrWarming getByModel(String ofilmModel) {
		return vlrrWarmingDao.getByModel(ofilmModel);
	}
	public String saveMailSettings(VlrrData vlrrData,String warnManLogin){
		String message = "";
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String dateStr = sdf.format(myDate);			
		if(StringUtils.isNotEmpty(warnManLogin)){
			//发送邮件
			message = "*"+dateStr+"最新预警信息:"+vlrrData.getBusinessUnitName()+"事业部"+sdf.format(vlrrData.getVlrrDate())+","+vlrrData.getOfilmModel()+"机种VLRR数据不良率超过了目标值!";
			String email = ApiFactory.getAcsService().getUserByLoginName(warnManLogin).getEmail();
			if(StringUtils.isNotEmpty(email)){
				AsyncMailUtils.sendMail(email,"售后VLRR数据异常预警",message);
			}
		}
		return null;
	}
	
	public String importDatas(File file,String businessUnit) throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);			
			Workbook book = WorkbookFactory.create(inputStream);
			int totalSheets = book.getNumberOfSheets();
			Map<String,Cell> cellMap = new HashMap<String, Cell>();
			Map<String,org.apache.poi.ss.util.CellRangeAddress> cellRangeAddressMap = new HashMap<String, org.apache.poi.ss.util.CellRangeAddress>();
			for(int k=0;k<totalSheets;k++){
				Sheet sheet = book.getSheetAt(k);
				Row row = sheet.getRow(0);
				//隐藏的sheet不处理
				if(book.isSheetHidden(k)){
					continue;
				}
				if(row == null){
					continue;
				}
				//缓存每个单元格值的
				cellMap.clear();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while(rowIterator.hasNext()){
					row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						Object value = ExcelUtil.getCellValue(cell);
						if(value != null){
							String key = value.toString().replaceAll("\\n","").replaceAll(" ","").replaceAll("　","");
							if(!cellMap.containsKey(key)){
								cellMap.put(key,cell);
							}
						}
					}
				}
				//缓存所有的合并单元格
				cellRangeAddressMap.clear();
				int sheetMergeCount = sheet.getNumMergedRegions();  
			    for(int i = 0 ; i < sheetMergeCount ; i++ ){  
			        CellRangeAddress ca = sheet.getMergedRegion(i);
			        int firstRow = ca.getFirstRow(),lastRow = ca.getLastRow();
			        int firstColumn = ca.getFirstColumn(),lastColumn = ca.getLastColumn();
			        for(int rowIndex = firstRow;rowIndex<=lastRow;rowIndex++){
			        	for(int columnIndex = firstColumn;columnIndex<=lastColumn;columnIndex++){
			        		String key = rowIndex + "_" + columnIndex;
			        		cellRangeAddressMap.put(key,ca);
			        	}
			        }
			    }
				Iterator<Row> rows = sheet.rowIterator();
				Cell itemItitleCell = cellMap.get("年份");
				if(itemItitleCell==null){
					throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为年份的单元格!&nbsp;&nbsp;</br>");
				}
				Integer itemTitleRowNum = itemItitleCell.getRow().getRowNum();
				while(rows.hasNext()){
					row = rows.next();
					//标题以后再执行
					if(row.getRowNum() <= itemTitleRowNum){
						continue;
					}
					Cell cell = row.getCell(itemItitleCell.getColumnIndex());
					if(cell == null){
						continue;
					}
					Cell tempCell=null;
					 String customerName=null,ofilmModel =null;
				     Integer years=null,yearFen= null;
                     Float[] targets=new Float[12];
				     Float target1=null,target2=null,target3=null,target4=null,target5=null,target6=null,
				     target7=null,target8=null,target9=null,target10=null,target11=null,target12=null;
				     targets[0]=target1;targets[1]=target2;targets[2]=target3;targets[3]=target4;targets[4]=target5;targets[6]=target7;
				     targets[7]=target8;targets[9]=target10;targets[10]=target11;targets[11]=target12;
				   
				     tempCell = cellMap.get("年份");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户名称】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						
						yearFen=(Integer)ExcelUtil.getCellValue(cell);	
					}											 
				    if(yearFen==null||"".equals(yearFen)){
				    	throw new  AmbFrameException("Sheet"+k+"的年份值不能为空");
				    }
					 //客户名称
					tempCell = cellMap.get("客户名称");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户名称】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						customerName=(ExcelUtil.getCellValue(cell).toString()).trim();
					}											 
				    if(customerName==null||"".equals(customerName)){
				    	throw new  AmbFrameException("Sheet"+k+"的客户名称值不能为空");
				    }
	
				    //欧菲机型
					tempCell = cellMap.get("欧菲");
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							ofilmModel=(ExcelUtil.getCellValue(cell).toString()).trim();
						}
					}
					//年度目标值
					tempCell = cellMap.get("年度目标值");
					if(tempCell==null){
						throw new AmbFrameException("SHEET"+k+"资料格式不正确!没有值为【客户名称】的单元格!&nbsp;&nbsp;</br>");
					}					
					cell = row.getCell(tempCell.getColumnIndex());
					if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
						
						years=(Integer)ExcelUtil.getCellValue(cell);	
					}											 
				    if(yearFen==null||"".equals(yearFen)){
				    	throw new  AmbFrameException("Sheet"+k+"的年份值不能为空");
				    }
					//12个月的目标值
					for(int j=0;j<12;j++){
						String [] strArray={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
					tempCell = cellMap.get(strArray[j]);
					if(tempCell!=null){
						cell = row.getCell(tempCell.getColumnIndex());
						if(ExcelUtil.getCellValue(cell)!=null&&!"".equals(ExcelUtil.getCellValue(cell))){
							targets[j]=(Float)ExcelUtil.getCellValue(cell);	
						}
					}
				}
									
					LarTarget larTarget = new LarTarget();
					larTarget.setCreatedTime(new Date());
					larTarget.setCompanyId(ContextUtils.getCompanyId());
					larTarget.setCreator(ContextUtils.getUserName());
					larTarget.setLastModifiedTime(new Date());
					larTarget.setLastModifier(ContextUtils.getUserName());
				    larTarget.setTarget1(target1);
				    larTarget.setTarget2(target2);
				    larTarget.setTarget3(target3);
				    larTarget.setTarget3(target3);
				    larTarget.setTarget4(target4);
				    larTarget.setTarget5(target5);
				    larTarget.setTarget6(target6);
				    larTarget.setTarget7(target7);
				    larTarget.setTarget8(target8);
				    larTarget.setTarget9(target9);
				    larTarget.setTarget10(target10);
				    larTarget.setTarget11(target11);
				    larTarget.setTarget12(target12);
					larTarget.setYears(years);
					larTarget.setCustomer(customerName);
					larTarget.setOfilmModel(ofilmModel);
					larTarget.setBusinessUnitName(businessUnit);
				}
			}
			return sb.toString();
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			file.delete();
		}
	}
}