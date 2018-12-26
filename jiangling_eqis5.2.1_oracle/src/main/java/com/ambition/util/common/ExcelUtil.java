package com.ambition.util.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Session;

import com.ambition.util.exception.AmbFrameException;
import com.ambition.util.exportexcel.ExcelListDynamicColumnValue;
import com.ambition.util.exportexcel.ExportExcelFormatter;
import com.norteksoft.mms.base.utils.view.DynamicColumnDefinition;
import com.norteksoft.mms.form.entity.ListColumn;
import com.norteksoft.product.util.PropUtils;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**
 * 类名:Excel操作公共类
 * <p>amb</p>
 * <p>厦门安必兴信息科技有限公司</p>
 * @author  赵骏
 * @version 1.00 2013-5-7 发布
 */
public class ExcelUtil {
	/**
	  * 方法名: 根据单元格名获取单元格
	  * <p>功能说明：</p>
	  * @param sheet
	  * @param cellName
	  * @param addCount
	  * @return
	 */
	public static Cell getCell(Sheet sheet,String cellName){
		return getCell(sheet, cellName,0);
	}
	
	/**
	  * 方法名: 根据单元格名获取单元格
	  * <p>功能说明：</p>
	  * @param sheet
	  * @param cellName
	  * @param addCount
	  * @return
	 */
	public static Cell getCell(Sheet sheet,String cellName,int addRowCount){
		StringBuilder colStr = new StringBuilder("");
		for(int i=0;i<cellName.length();i++){
			char c = cellName.charAt(i);
			if(c>=97&&c<123){
				colStr.append(c);
			}
		}
		int colIndex = 0,rowIndex=Integer.valueOf(cellName.substring(colStr.length()))-1 + addRowCount;
		if(colStr.length()>1){
			colIndex = (colStr.charAt(0)-96)*26 + colStr.charAt(1) - 97;
		}else{
			colIndex = colStr.charAt(0) - 97;
		}
		
		Row row = sheet.getRow(rowIndex);
		if(row == null){
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(colIndex);
		if(cell == null){
			cell = row.createCell(colIndex);
		}
		return cell;
	}
	
	/**
	  * 方法名:获取单元格的值 
	  * @param cell
	  * @return
	 */
	public static Object getCellValue(Cell cell){
		if(cell == null){
			return null;
		}else if(HSSFCell.CELL_TYPE_STRING == cell.getCellType()){
			return cell.getStringCellValue();
		}else if(HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()){
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				return cell.getDateCellValue();
			}
			Number number = cell.getNumericCellValue();
			if (cell.getCellStyle() != null) {
				// 处理自定义日期格式：m月d日
               String formatStr = cell.getCellStyle().getDataFormatString();
               if(cell.getCellStyle().getDataFormat() == 58
               	||(formatStr != null && formatStr.indexOf("m\"月\"d\"日\"")>-1)){
	                return org.apache.poi.ss.usermodel.DateUtil  
	                        .getJavaDate(number.doubleValue());
				}
           }
			if(number == null){
				return number;
			}else{
				return new DecimalFormat("0.####").format(number);
			}
		}else if(HSSFCell.CELL_TYPE_BLANK == cell.getCellType()){
			return "";
		}else if(HSSFCell.CELL_TYPE_BOOLEAN == cell.getCellType()){
			return cell.getBooleanCellValue();
		}else if(HSSFCell.CELL_TYPE_FORMULA == cell.getCellType()){
			Number number = cell.getNumericCellValue();
			if(number == null){
				return number;
			}else{
				return new DecimalFormat("0.####").format(number);
			}
		}else{
			return "";
		}
	}
	
	/**
	  * 方法名:获取单元格的值 
	  * @param cell
	  * @return
	 */
	public static Object getCellValue(Sheet sheet,String cellName){
		return getCellValue(getCell(sheet, cellName));
	}
	
	public static CellStyle getCellBorderStyle(Workbook workbook){
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//居中
		
		Font ztFont = workbook.createFont();  
        ztFont.setFontHeightInPoints((short)12);    // 将字体大小设置为18px  
        cellStyle.setFont(ztFont);
		return cellStyle;
	}
	
	/**
	  * 方法名: 获取同一行下一个单元格
	  * <p>功能说明：判断是否有合并单元格</p>
	  * @param sheet
	  * @param cell
	  * @param cellRangeAddressMap
	  * @return
	 */
	public static Cell getNextCell(Sheet sheet,Cell cell,Map<String,CellRangeAddress> cellRangeAddressMap){
		if(cell == null){
			return null;
		}
		int columnIndex = cell.getColumnIndex();
		int nextColumnIndex = columnIndex+1;
		String rangeKey = cell.getRowIndex() + "_" + columnIndex;
		CellRangeAddress ca = cellRangeAddressMap.get(rangeKey);
		if(ca != null){
			nextColumnIndex = ca.getLastColumn() + 1;
		}
		return cell.getRow().getCell(nextColumnIndex);
	}
	
	/**
	  * 方法名: 获取上一个单元格
	  * <p>功能说明：判断是否有合并单元格,如果有,取合并单元格的第一个的值</p>
	  * @param sheet
	  * @param cell
	  * @param cellRangeAddressMap
	  * @return
	 */
	public static Cell getPrevCell(Sheet sheet,Cell cell,Map<String,CellRangeAddress> cellRangeAddressMap){
		if(cell == null){
			return null;
		}
		int columnIndex = cell.getColumnIndex();
		int prevColumnIndex = columnIndex-1;
		String rangeKey = cell.getRowIndex() + "_" + prevColumnIndex;
		CellRangeAddress ca = cellRangeAddressMap.get(rangeKey);
		if(ca != null){
			Row row = sheet.getRow(ca.getFirstRow());
			if(row == null){
				return null;
			}
			return row.getCell(ca.getFirstColumn());
		}else{
			return cell.getRow().getCell(prevColumnIndex);
		}
	}
	
	/**
	  * 方法名: 导出到Excel
	  * <p>功能说明：</p>
	  * @param templateInputStream 导出模板
	  * @param obj 导出对象
	  * @param customFormatterMap 自定义格式化方法
	  * @return
	 * @throws IOException 
	 */
	public static void exportToExcel(InputStream templateInputStream,String exportFileName,Object obj,Map<String,ExportExcelFormatter> customFormatterMap) throws IOException{
		try {
			int existCount = 0;
			Workbook book = WorkbookFactory.create(templateInputStream);
			Sheet sheet = book.getSheetAt(0);
			//统计合并列
			int sheetMergeCount = sheet.getNumMergedRegions(); 
			Map<String,CellRangeAddress> cellRangeAddressMap = new HashMap<String, CellRangeAddress>();
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
			//统计需要填充值的单元格
			Map<String,Cell> defaultCellMap = new HashMap<String, Cell>();
			Map<String,Map<String,Cell>> listCellMap = new HashMap<String,Map<String,Cell>>();
			Map<String,Integer> rowNumMap = new HashMap<String, Integer>();
			Iterator<Row> rowIterator = sheet.rowIterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()){
					Cell cell = cellIterator.next();
					Object value = ExcelUtil.getCellValue(cell);
					if(value != null){
						String val = value.toString().trim();
						if(val.startsWith("${")&&val.endsWith("}")){
							String field = val.substring(2,val.length()-1);
							if(field.indexOf(".")==-1){
								if(defaultCellMap.containsKey(field)){
									field = field + "~" + (++existCount);
								}
								defaultCellMap.put(field,cell);
							}else{
								String strs[] = field.split("\\.");
								if(strs.length>2){
									throw new AmbFrameException("无效的字段["+val+"]!");
								}
								if(!listCellMap.containsKey(strs[0])){
									listCellMap.put(strs[0],new HashMap<String, Cell>());
								}
								if(listCellMap.get(strs[0]).containsKey(strs[1])){
									strs[1] = strs[1] + "~" + (++existCount);
								}
								listCellMap.get(strs[0]).put(strs[1],cell);
								rowNumMap.put(strs[0],cell.getRowIndex());
							}
						}
					}
				}
			}
			
			//先填充集合的值
			for(String fieldName : listCellMap.keySet()){
				Object value = getValue(obj, fieldName, fieldName);
				Map<String,Cell> secCellMap = listCellMap.get(fieldName);
				if(value == null){
					//如果集合的值为空,清空所有的值
					for(String itemField : secCellMap.keySet()){
						secCellMap.get(itemField).setCellValue("");
					}
					continue;
				}
				//如果是集合类型
				if(value instanceof List){
					@SuppressWarnings("unchecked")
					List<Object> list = (List<Object>)value;
					
					//获取最后的列的合并的列
					int firstRowNum = 0,maxColumnIndex = 0;
					Row firstRow = null;
					for(String itemField : secCellMap.keySet()){
						Cell cell = secCellMap.get(itemField);
						firstRowNum = cell.getRowIndex();
						firstRow = cell.getRow();
						maxColumnIndex = firstRow.getLastCellNum();
						break;
					}
					
					Map<Integer,CellRangeAddress> rangeAddressMap = new HashMap<Integer, CellRangeAddress>();
					for(String rangeKey : cellRangeAddressMap.keySet()){
						if(rangeKey.startsWith(rowNumMap.get(fieldName)+"_")){
							CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(rangeKey);
							rangeAddressMap.put(cellRangeAddress.getFirstColumn(),cellRangeAddress);
						}
					}
					Map<String,Cell> cellMap = secCellMap;
					int rowNum = 0;
					for(Object item : list){
						if(rowNum>0){
							int rowIndex = firstRowNum+rowNum;
							sheet.shiftRows(rowIndex, sheet.getLastRowNum(),1,true,false);
							sheet.createRow(rowIndex);
							Row row = sheet.getRow(rowIndex);
							row.setHeightInPoints(firstRow.getHeightInPoints());
							for(int i=0;i<=maxColumnIndex;i++){
								row.createCell(i);
								if(firstRow.getCell(i) != null){
									row.getCell(i).setCellStyle(firstRow.getCell(i).getCellStyle());
								}
							}
							for(int columnIndex : rangeAddressMap.keySet()){
								CellRangeAddress cellRangeAddress = rangeAddressMap.get(columnIndex);
								sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex,cellRangeAddress.getFirstColumn(),cellRangeAddress.getLastColumn()));
							}
							cellMap = new HashMap<String, Cell>();
							for(String itemField : secCellMap.keySet()){
								Cell hisCell = secCellMap.get(itemField);
								Cell copyCell = row.getCell(hisCell.getColumnIndex());
								cellMap.put(itemField,copyCell);
							}
						}
						setExcelValue(item,rowNum, cellMap, fieldName, customFormatterMap);
						rowNum++;
					}
				}else{//当作引用对象来取值
					setExcelValue(value,0,secCellMap, fieldName, customFormatterMap);
				}
			}
			
			//填充基本元素的值
			setExcelValue(obj,0,defaultCellMap,null,customFormatterMap);
			
			String fileName = exportFileName + ".xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			Logger.getLogger(ExcelUtil.class).error("导出失败!",e);
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append("error.xls").append("\"")
					.toString());
			response.getOutputStream().write(("服务器错误:" + e.getMessage()).getBytes("8859_1"));
		}
	}
	/**
	  * 方法名: 多个
	  * <p>功能说明：</p>
	  * @param templateInputStream 导出模板
	  * @param obj 导出对象
	  * @param customFormatterMap 自定义格式化方法
	  * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void exportToExcels(InputStream templateInputStream,String exportFileName,List<Object> objects,Map<String,ExportExcelFormatter> customFormatterMap) throws UnsupportedEncodingException, IOException{
		try{
			Workbook book = WorkbookFactory.create(templateInputStream);
			//赋值前，先进行复制模板
			int sheetCount = 1;
			while (sheetCount != objects.size()) {
				book.cloneSheet(0);
				sheetCount++;
			}
			//复制模板后，再进行赋值
			sheetCount = 0;
			for (Object obj : objects) {
				Sheet sheet =  book.getSheetAt(sheetCount);
				book.setSheetName(sheetCount, exportFileName+(sheetCount+1));
				sheetCount++;
				//统计合并列
				int existCount = 0;
				int sheetMergeCount = sheet.getNumMergedRegions(); 
				Map<String,CellRangeAddress> cellRangeAddressMap = new HashMap<String, CellRangeAddress>();
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
				//统计需要填充值的单元格
				Map<String,Cell> defaultCellMap = new HashMap<String, Cell>();
				Map<String,Map<String,Cell>> listCellMap = new HashMap<String,Map<String,Cell>>();
				Map<String,Integer> rowNumMap = new HashMap<String, Integer>();
				Iterator<Row> rowIterator = sheet.rowIterator();
				while(rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						Object value = ExcelUtil.getCellValue(cell);
						if(value != null){
							String val = value.toString().trim();
							if(val.startsWith("${")&&val.endsWith("}")){
								String field = val.substring(2,val.length()-1);
								if(field.indexOf(".")==-1){
									if(defaultCellMap.containsKey(field)){
										field = field + "~" + (++existCount);
									}
									defaultCellMap.put(field,cell);
								}else{
									String strs[] = field.split("\\.");
									if(strs.length>2){
										throw new AmbFrameException("无效的字段["+val+"]!");
									}
									if(!listCellMap.containsKey(strs[0])){
										listCellMap.put(strs[0],new HashMap<String, Cell>());
									}
									if(listCellMap.get(strs[0]).containsKey(strs[1])){
										strs[1] = strs[1] + "~" + (++existCount);
									}
									listCellMap.get(strs[0]).put(strs[1],cell);
									rowNumMap.put(strs[0],cell.getRowIndex());
								}
							}
						}
					}
				}
				//先填充集合的值
				for(String fieldName : listCellMap.keySet()){
					Object value = getValue(obj, fieldName, fieldName);
					Map<String,Cell> secCellMap = listCellMap.get(fieldName);
					if(value == null){
						//如果集合的值为空,清空所有的值
						for(String itemField : secCellMap.keySet()){
							secCellMap.get(itemField).setCellValue("");
						}
						continue;
					}
					//如果是集合类型
					if(value instanceof List){
						@SuppressWarnings("unchecked")
						List<Object> list = (List<Object>)value;
						if(list.isEmpty()){
							//如果集合的值为空,清空所有的值
							for(String itemField : secCellMap.keySet()){
								secCellMap.get(itemField).setCellValue("");
							}
							continue;
						}
						//获取最后的列的合并的列
						int firstRowNum = 0,maxColumnIndex = 0;
						Row firstRow = null;
						for(String itemField : secCellMap.keySet()){
							Cell cell = secCellMap.get(itemField);
							firstRowNum = cell.getRowIndex();
							firstRow = cell.getRow();
							maxColumnIndex = firstRow.getLastCellNum();
							break;
						}
						
						Map<Integer,CellRangeAddress> rangeAddressMap = new HashMap<Integer, CellRangeAddress>();
						for(String rangeKey : cellRangeAddressMap.keySet()){
							if(rangeKey.startsWith(rowNumMap.get(fieldName)+"_")){
								CellRangeAddress cellRangeAddress = cellRangeAddressMap.get(rangeKey);
								rangeAddressMap.put(cellRangeAddress.getFirstColumn(),cellRangeAddress);
							}
						}
						Map<String,Cell> cellMap = secCellMap;
						int rowNum = 0;
						for(Object item : list){
							if(rowNum>0){
								int rowIndex = firstRowNum+rowNum;
								sheet.shiftRows(rowIndex, sheet.getLastRowNum(),1,true,false);
								sheet.createRow(rowIndex);
								Row row = sheet.getRow(rowIndex);
								row.setHeightInPoints(firstRow.getHeightInPoints());
								for(int i=0;i<=maxColumnIndex;i++){
									row.createCell(i);
									if(firstRow.getCell(i) != null){
										row.getCell(i).setCellStyle(firstRow.getCell(i).getCellStyle());
									}
								}
								for(int columnIndex : rangeAddressMap.keySet()){
									CellRangeAddress cellRangeAddress = rangeAddressMap.get(columnIndex);
									sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex,cellRangeAddress.getFirstColumn(),cellRangeAddress.getLastColumn()));
								}
								cellMap = new HashMap<String, Cell>();
								for(String itemField : secCellMap.keySet()){
									Cell hisCell = secCellMap.get(itemField);
									Cell copyCell = row.getCell(hisCell.getColumnIndex());
									cellMap.put(itemField,copyCell);
								}
							}
							setExcelValue(item,rowNum, cellMap, fieldName, customFormatterMap);
							rowNum++;
						}
					}else{//当作引用对象来取值
						setExcelValue(value,0,secCellMap, fieldName, customFormatterMap);
					}
				}
				//填充基本元素的值
				setExcelValue(obj,0,defaultCellMap,null,customFormatterMap);
			}
			String fileName = exportFileName + ".xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			if(!response.isCommitted())response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder("attachment; filename=\"")).append(fileName).append("\"").toString());
			book.write(response.getOutputStream());
		}catch (Exception e) {
			Logger.getLogger(ExcelUtil.class).error("导出为Excel文件失败!",e);
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder("attachment; filename=\"")).append("error.xls").append("\"").toString());
			response.getOutputStream().write(("服务器错误:" + e.getMessage()).getBytes("8859_1"));
		}
	}
	/**
	  * 方法名: 从对象中获取值
	  * <p>功能说明：</p>
	  * @param obj
	  * @param fieldName
	  * @param fullFieldName
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	private static Object getValue(Object obj,String fieldName,String fullFieldName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(obj instanceof Map){
			return ((Map<?,?>)obj).get(fieldName);
		}else{
			Class<?> type = PropertyUtils.getPropertyType(obj,fieldName);
			if(type == null){
				throw new AmbFrameException("无效的字段名["+fullFieldName+"]!");
			}
			return PropertyUtils.getProperty(obj,fieldName);
		}
	}
	
	/**
	  * 方法名: 设置Excel中的值
	  * <p>功能说明：</p>
	  * @param value
	  * @param rowNum
	  * @param cellMap
	  * @param baseField
	  * @param formatterMap
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws NoSuchMethodException
	 */
	private static void setExcelValue(Object value,int rowNum,Map<String,Cell> cellMap,String baseField,Map<String,ExportExcelFormatter> formatterMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		for(String field : cellMap.keySet()){
			String fieldName = field;
			if(field.indexOf("~")>-1){
				fieldName = field.substring(0,field.indexOf("~"));
			}
			String fullField = fieldName;
			if(baseField != null){
				fullField = baseField + "." + fieldName;
			}
			ExportExcelFormatter formatter = null;
			if(formatterMap != null){
				formatter = formatterMap.get(fullField);
			}
			//默认的序号格式化
			if(formatter==null&&"_NO".equals(fieldName)){
				formatter = rowNumFormatter;
			}
			Cell cell = cellMap.get(field);
			String excelValue = null;
			if(formatter != null){
				excelValue = formatter.format(value,rowNum,fullField,cell);
			}else{
				Object itemValue = getValue(value,fieldName,fullField);
				if(itemValue != null){
					if(itemValue instanceof Date){
						excelValue = DateUtil.formateDateStr((Date)itemValue);
					}else if(itemValue instanceof Float || itemValue instanceof Double){
						excelValue = new DecimalFormat("#.##").format(itemValue);
					}else if(itemValue instanceof Boolean){
						excelValue = (Boolean)itemValue?"是":"否";
					}else{
						excelValue = itemValue.toString();
					}
				}
			}
			cellMap.get(field).setCellValue(excelValue==null?"":excelValue);
		}
	}
	
	//序号格式化
	private final static ExportExcelFormatter rowNumFormatter = new ExportExcelFormatter() {
		public String format(Object value, int rowNum, String fieldName,Cell cell) {
			return rowNum+1+"";
		}
	};
	
	/**
	  * 方法名: 导出集合到Excel,导出格式由系统元数据中的列表管理来设置
	  * <p>功能说明：</p>
	  * @param list 集合
	  * @param fileName 导出文件名
	  * @param listCode 列表配置编码
	  * @return 生成的临时文件名称
	 * @throws IOException 
	 */
	public static String exportListToExcel(List<?> list,String fileName,String listCode,List<DynamicColumnDefinition> dynamicColumnDefinitions,ExcelListDynamicColumnValue excelListDynamicColumnValue,Session session) throws Exception{
		String savePath = PropUtils.getProp("excel.export.file.path");
		File pathFile = new File(savePath);
		if(!pathFile.exists()){
			pathFile.mkdir();
		}
		String saveFileName = UUID.randomUUID().toString();
		File file = new File(savePath + saveFileName);
		exportListToExcel(file, list, listCode, dynamicColumnDefinitions, excelListDynamicColumnValue,session);
		return fileName + ".xls_" + saveFileName;
	}
	/**
	  * 方法名: 导出集合到Excel,导出格式由系统元数据中的列表管理来设置
	  * <p>功能说明：</p>
	  * @param list 集合
	  * @param fileName 导出文件名
	  * @param listCode 列表配置编码
	  * @return 生成的临时文件名称
	 * @throws IOException 
	 */
	public static void exportListToExcel(File exportFile,List<?> list,String listCode,List<DynamicColumnDefinition> dynamicColumnDefinitions,ExcelListDynamicColumnValue excelListDynamicColumnValue,Session session) throws Exception{
		InputStream inputStream = null;
		OutputStream excelOutputStream = null;
		try {
			String hql = "select l from ListColumn l where l.listView.deleted = ? and l.listView.code = ? order by l.displayOrder";
			List<?> listColumns = session.createQuery(hql)
										.setParameter(0,false)
										.setParameter(1,listCode)
										.list();
//			ListView listView = ApiFactory.getMmsService().getListViewByCode(listCode);
			if(listColumns.isEmpty()){
				throw new AmbFrameException("找不到编码为" + listCode + "的列表！");
			}
			//找到模板
			inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("template/report/list-template.xls");
			Workbook book = WorkbookFactory.create(inputStream);
			Sheet sheet = book.getSheetAt(0);
			Row headerRow = sheet.getRow(0);
			if(headerRow==null){
				headerRow = sheet.createRow(0);
			}
			Map<String,Integer> fieldIndexMap = new HashMap<String, Integer>();
			Map<String,Integer> dycolumnIndexMap = new HashMap<String, Integer>();
			Map<String,ListColumn> columnMap = new HashMap<String, ListColumn>();
			int colIndex = 0;
			for(Object objColumn : listColumns){
				ListColumn listColumn = (ListColumn)objColumn;
				if(listColumn.getExportable() == null 
						|| !listColumn.getExportable()){
					continue;
				}
				if(listColumn.getTableColumn()==null){
					continue;
				}
				columnMap.put(listColumn.getTableColumn().getName(), listColumn);
				Cell cell = headerRow.getCell(colIndex);
				if(cell==null){
					cell = headerRow.createCell(colIndex);
				}
				cell.setCellValue(listColumn.getHeaderName());
				fieldIndexMap.put(listColumn.getTableColumn().getName(),colIndex);
				colIndex++;
			}
			//扩展表头
			if(dynamicColumnDefinitions != null){
				for(DynamicColumnDefinition columnDefinition : dynamicColumnDefinitions){
					if(columnDefinition.getVisible()==null || !columnDefinition.getVisible()){
						continue;
					}
					Cell cell = headerRow.getCell(colIndex);
					if(cell==null){
						cell = headerRow.createCell(colIndex);
					}
					cell.setCellValue(columnDefinition.getColName());
					dycolumnIndexMap.put(columnDefinition.getName(),colIndex);
					colIndex++;
				}
			}
			int rowIndex = 0;
			Map<String,String> dynamicValueMap = new HashMap<String, String>();
			for(Object obj : list){
				rowIndex++;
				//自定义格式化字段
				dynamicValueMap.clear();
				if(excelListDynamicColumnValue != null){
					dynamicValueMap = excelListDynamicColumnValue.getDynamicColumnValue(obj, rowIndex, dynamicValueMap);
				}
				
				Row row = sheet.createRow(rowIndex);
				for(String fieldName : fieldIndexMap.keySet()){
					Object val = null;
					if(dynamicValueMap.containsKey(fieldName)){
						val = dynamicValueMap.get(fieldName);
					}
					if(val != null){//
						row.createCell(fieldIndexMap.get(fieldName)).setCellValue(val+"");
					}else{
						Class<?> valType = PropertyUtils.getPropertyType(obj,fieldName);
						if(valType==null){
							continue;
						}
						val = PropertyUtils.getProperty(obj,fieldName);
						if(val==null){
							continue;
						}
						row.createCell(fieldIndexMap.get(fieldName)).setCellValue(formateObj(val,columnMap.get(fieldName)));
					}
				}
				//扩展列
				for(String fieldName : dycolumnIndexMap.keySet()){
					if(dynamicValueMap==null){
						continue;
					}
					String val = dynamicValueMap.get(fieldName);
					if(val==null){
						continue;
					}
					row.createCell(dycolumnIndexMap.get(fieldName)).setCellValue(val);
				}
			}
			excelOutputStream = new FileOutputStream(exportFile);
			book.write(excelOutputStream);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
			if(excelOutputStream != null){
				excelOutputStream.close();
			}
		}
	}
	private static DecimalFormat twoPointDF = new DecimalFormat("0.00");//两位小数的格式化方法
	/**
	  * 方法名: 列格式化方法
	  * <p>功能说明：</p>
	  * @param obj
	  * @param listColumn
	  * @return
	 */
	private static String formateObj(Object obj,ListColumn listColumn){
		if(obj==null){
			return "";
		}else if(obj instanceof Date){
			return DateUtil.formateDateStr((Date)obj);
		}else if(obj instanceof Double){
			return twoPointDF.format(obj);
		}else if(obj instanceof Float){
			return twoPointDF.format(obj);
		}else if(obj instanceof Boolean){
			Boolean b = (Boolean)obj;
			if(b){
				return "是";
			}else{
				return "否";
			}
		}else{
			return obj.toString();
		}
	}
}	
