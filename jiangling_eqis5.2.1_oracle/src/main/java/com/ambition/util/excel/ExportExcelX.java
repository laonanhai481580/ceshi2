package com.ambition.util.excel;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA. User: SUKE Date: 12-8-21 Time: 上午7:15 To change
 * this template use File | Settings | File Templates.
 */
public class ExportExcelX<T> { // 舍去的列 //第几列开始
	public void exportExcel(Collection<T> dataset, OutputStream out,Integer dataseLen, Integer startField,String backColorFlag) {
		exportExcel("导出EXCEL文档", null, dataset, out, "yyyy-MM-dd", dataseLen,	startField,backColorFlag);
	}

	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out, Integer dataseLen, Integer startField ,String backColorFlag) {
		exportExcel("导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd",	dataseLen, startField,backColorFlag);
	}
	
	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out, Integer dataseLen, Integer startField ,String backColorFlag,
			String figureNumber,String scheme,String Num,String materialName,String productModel,String fristCarType,java.sql.Blob  pic,String filePath) throws IOException {
		exportExcelJob("导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd",	dataseLen, startField,backColorFlag,
					figureNumber,scheme,Num,materialName,productModel,fristCarType,pic,filePath);
	}
	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out, String pattern, Integer dataseLen,Integer startField,String backColorFlag) {
		exportExcel("导出EXCEL文档", headers, dataset, out, pattern, dataseLen,startField,backColorFlag);
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @param backColorFlag
	 * 		"1":设置不同行不同背景颜色;"0":不设置           
	 */

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public void exportExcel(String title, String[] headers,
			Collection<T> dataset, OutputStream out, String pattern,
			Integer dataseLen, Integer startField,String backColorFlag) {
		// 声明一个工作薄
		//XSSFWorkbook workbook = new XSSFWorkbook(); //xxxx
		//换成SXSSFWorkbook,解决导出时消耗内存的遐想
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 生成一个表格
		//XSSFSheet sheet = workbook.createSheet(title);/xxxxx
		Sheet sheet = workbook.createSheet(title);
		
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		
		//1. 生成一个样式,标题行
		//XSSFCellStyle style = workbook.createCellStyle();///xxxx
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		//style.setFillForegroundColor(new XSSFColor(new java.awt.Color(191, 239, 255)));/xxxxx
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		//1.1 生成一个字体,标题栏使用
		//XSSFFont font = workbook.createFont();//xxxx
		Font font = workbook.createFont();
		//font.setColor(new XSSFColor(new java.awt.Color(139, 139, 139)));//xxxx
		font.setColor(HSSFColor.VIOLET.index);
		
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		//1.2 把字体应用到当前的样式
		style.setFont(font);
		
		// 2.生成并设置另一个样式,数据行
		//XSSFCellStyle style2 = workbook.createCellStyle();//xxxx
		CellStyle style2 = workbook.createCellStyle();
		//style2.setFillForegroundColor(new XSSFColor(new java.awt.Color(240, 240, 240)));/xxxx
		style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
		//style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setAlignment(XSSFCellStyle.ALIGN_LEFT);//居左
		style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		//XSSFFont font2 = workbook.createFont();//xxxx
		Font font2 = workbook.createFont();
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		//XSSFDrawing patriarch = sheet.createDrawingPatriarch();//xxxx
		Drawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//XSSFClientAnchor clientAnchor = new XSSFClientAnchor();//xxxx
		ClientAnchor clientAnchor = new XSSFClientAnchor();
		//XSSFComment comment = patriarch.createCellComment(clientAnchor);//xxxx
		Comment comment = patriarch.createCellComment(clientAnchor);
		// 设置注释内容
		comment.setString(new XSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("tata");

		// 产生表格标题行
		//XSSFRow row = sheet.createRow(0);
	
		
		Row row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			//XSSFCell cell = row.createCell(i);//xxxx
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		////设定冻结表头
		sheet.createFreezePane(0, 1, 0, 1);
		
		//字体颜色设置,不能放在循环内,否则:java.lang.IllegalArgumentException: Maximum number of fonts was exceeded
		//XSSFCellStyle style3 = workbook.createCellStyle();//xxxx
		CellStyle style3 = workbook.createCellStyle();
		//style3.setFillForegroundColor(new XSSFColor(new java.awt.Color(240, 240, 240)));//xxxx
		style3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		//XSSFFont font3 = workbook.createFont();//xxxx
		Font font3 = workbook.createFont();
		//font3.setColor(new XSSFColor(new java.awt.Color(255, 255, 255)));//xxxx 
		font3.setColor(HSSFColor.BLUE.index);
		
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		//级别码，用于设置数据行背景颜色
		String subCode="1";
		
		while (it.hasNext()) {
			// 0行是标题,index++ 是数据开始行
			index++; 
			//logger.debug("index = " +index);
			
			row = sheet.createRow(index);
			T t = (T) it.next();
			
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			//dataseLen:对象集合不取的后边字段;startField:前面开始的字段pojo对象的开始字段数
			for (int i = 0; i < fields.length - dataseLen - startField; i++) {
				// 根据i创建excel行数
				
				//XSSFCell cell = row.createCell(i); //xxxx
				Cell cell = row.createCell(i);
				//设置数据单元格样式
				cell.setCellStyle(style2);
				//设置cell格式为文本格式
				cell.setCellType(XSSFCell.CELL_TYPE_STRING); 
				// Field field = fields[i];
				// startField=2 舍去:fields[0]=serialVersionUID,fields[1]=pid
				Field field = fields[i + startField]; 
				String fieldName = field.getName();
				// System.out.println("fieldName = " +fieldName );
				String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					// if (value instanceof Integer) {
					// int intValue = (Integer) value;
					// cell.setCellValue(intValue);
					// } else if (value instanceof Float) {
					// float fValue = (Float) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(fValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Double) {
					// double dValue = (Double) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(dValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Long) {
					// long longValue = (Long) value;
					// cell.setCellValue(longValue);
					// }
					//布尔型
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}

					//日起型	
					} else if(value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					//字节型	
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(
								bsValue, XSSFWorkbook.PICTURE_TYPE_JPEG));
					//其他:字符,数字
					} else {
						// 其它数据类型都当作字符串简单处理	// 如果是空就不能toString
						if (value != null) {
							textValue = value.toString();
							
							//logger.debug("value.toString() = " + value.toString());
						} else {
							textValue = "";
						}
					}

					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						//根据不同的级别码设置不同的背景颜色,加上导出速度比较慢
//						if(i == 0){
//							subCode = textValue;
//							//logger.debug("subCode = " +subCode);
//						}
//						if("1".equals(backColorFlag)){
//							int color3= Integer.valueOf(subCode)*30%255;
//							XSSFCellStyle styleTemp = workbook.createCellStyle();
//							styleTemp.setFillForegroundColor(new XSSFColor(new java.awt.Color(color3, color3, 255)));
//							styleTemp.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//							styleTemp.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//							styleTemp.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//							styleTemp.setBorderRight(XSSFCellStyle.BORDER_THIN);
//							styleTemp.setBorderTop(XSSFCellStyle.BORDER_THIN);
//							//style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//							styleTemp.setAlignment(XSSFCellStyle.ALIGN_LEFT);//居左
//							styleTemp.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//							cell.setCellStyle(styleTemp);
//						}
//						
						//没有数字型处理,都是字符串型.
						
//						if(i != 0 && i != 1 && i != 2 && i != 3){
//							Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
//							Matcher matcher = p.matcher(textValue);
//							if (matcher.matches()) {
//								// 是数字当作double处理
//								cell.setCellValue(Double.parseDouble(textValue));
//							}else{
//								XSSFRichTextString richString = new XSSFRichTextString(	textValue);
//								richString.applyFont(font3);
//								cell.setCellValue(richString);
//							}
//						}else{
//							XSSFRichTextString richString = new XSSFRichTextString(textValue);
//							richString.applyFont(font3);
//							cell.setCellValue(richString);
//						}
						
						XSSFRichTextString richString = new XSSFRichTextString(textValue);
						richString.applyFont(font3);
						cell.setCellValue(richString);
					}

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
				
			}//一个对象各自段循环结束
			
		}//it.hasNext() ，List集合循环结束
		
		try {
			out.flush();
			workbook.write(out);
		} catch (IOException e) {
			
			//e.printStackTrace();
			System.out.println("用户取消了保存或打开,您的主机中的软件中止了一个已建立的连接");
		}finally{
			dataset.clear();
			System.gc();
		}
	}
	
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @param backColorFlag
	 * 		"1":设置不同行不同背景颜色;"0":不设置           
	 * @throws IOException 
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void exportExcelJob(String title, String[] headers,
			Collection<T> dataset, OutputStream out, String pattern,
			Integer dataseLen, Integer startField,String backColorFlag,
			String figureNumber,String scheme,String Num,String materialName,
			String fristCarType,String productType,java.sql.Blob picValue,String filePath ) throws IOException {
		// 声明一个工作薄
		//XSSFWorkbook workbook = new XSSFWorkbook(); //xxxx
		//换成SXSSFWorkbook,解决导出时消耗内存的遐想
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
		//设置自动换行样式，粗体背景颜色
		CellStyle cellStyle=workbook.createCellStyle();   
		cellStyle.setWrapText(true);   
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);//居中
		Font font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);//把字体应用到当前的样式
		
		//设置自动换行样式，
		CellStyle cellStyleWrap=workbook.createCellStyle();   
		cellStyleWrap.setWrapText(true);  
		cellStyleWrap.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyleWrap.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyleWrap.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyleWrap.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyleWrap.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 生成一个表格
		//XSSFSheet sheet = workbook.createSheet(title);/xxxxx
		Sheet sheet = workbook.createSheet(title);
		
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(18);
		sheet.setColumnWidth(0, 2500); //第1列的列宽
		sheet.setColumnWidth(1, 4700); //第2列的列宽
		sheet.setColumnWidth(2, 2500); //第3列的列宽
		sheet.setColumnWidth(3, 3100); //第4列的列宽
		sheet.setColumnWidth(4, 2500); //第5列的列宽
		sheet.setColumnWidth(5, 4900); //第6列的列宽
		sheet.setColumnWidth(6, 2500); //第7列的列宽
	
		//1. 生成一个样式,标题行
		//XSSFCellStyle style = workbook.createCellStyle();///xxxx
		CellStyle style1 = workbook.createCellStyle();
		style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style1.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style1.setBorderTop(XSSFCellStyle.BORDER_THIN);
		//1.1 生成一个字体,标题栏使用
		//XSSFFont font = workbook.createFont();//xxxx
		Font font1 = workbook.createFont();
		font1.setColor(HSSFColor.BLACK.index);
		font1.setFontHeightInPoints((short) 14);
		font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		//1.2 把字体应用到当前的样式
		style1.setFont(font1);
		
		// 2.生成并设置另一个样式,数据行
		//XSSFCellStyle style2 = workbook.createCellStyle();//xxxx
		CellStyle style2 = workbook.createCellStyle();
		//style2.setFillForegroundColor(new XSSFColor(new java.awt.Color(240, 240, 240)));/xxxx
		//style2.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		//style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style2.setAlignment(XSSFCellStyle.ALIGN_LEFT);//居左
		//style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);//居中
		style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		//XSSFFont font2 = workbook.createFont();//xxxx
		Font font2 = workbook.createFont();
		font2.setColor(HSSFColor.BLACK.index);
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
				
		//1. 生成一个样式,标题行
		//XSSFCellStyle style = workbook.createCellStyle();///xxxx
		CellStyle style3 = workbook.createCellStyle();
		style3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style3.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style3.setBorderTop(XSSFCellStyle.BORDER_THIN);
		//1.1 生成一个字体,标题栏使用
		Font font3 = workbook.createFont();
		font3.setColor(HSSFColor.BLACK.index);
		font3.setFontHeightInPoints((short) 12);
		font3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		//1.2 把字体应用到当前的样式
		style3.setFont(font3);

		// 产生表格标题行-----------------------------------
		//XSSFRow row = sheet.createRow(0);
		Row row0 = sheet.createRow(0);
		//合并单元格
		CellRangeAddress range1 = new CellRangeAddress(0, 0, 0, 6); //合并单元格
		sheet.addMergedRegion(range1);
		Cell cell6 = row0.createCell(6);
		Cell cell0 = row0.createCell(0);
		XSSFRichTextString text0 = new XSSFRichTextString("配套件检验规程");
		cell6.setCellValue(text0);
		cell6.setCellStyle(style1);
		cell0.setCellValue(text0);
		cell0.setCellStyle(style1);
		
		//excel第一行-------------------------------------
		Row row1 = sheet.createRow(1);
		Cell cell10 = row1.createCell(0);
		cell10.setCellStyle(style2);
		XSSFRichTextString text10 = new XSSFRichTextString("零件图号");
		cell10.setCellValue(text10);
		
		Cell cell11 = row1.createCell(1);
		cell11.setCellStyle(style2);
		Cell cell112 = row1.createCell(2);
		cell112.setCellStyle(style2);
		XSSFRichTextString text11 = new XSSFRichTextString(figureNumber);
		cell11.setCellValue(text11);
		CellRangeAddress range11 = new CellRangeAddress(1, 1, 1, 2); //合并单元格
		sheet.addMergedRegion(range11);
		
		Cell cell12 = row1.createCell(3);
		cell12.setCellStyle(style2);
		XSSFRichTextString text12 = new XSSFRichTextString("抽样判定方案");
		cell12.setCellValue(text12);
		
		Cell cell13 = row1.createCell(4);
		cell13.setCellStyle(style2);
		XSSFRichTextString text13 = new XSSFRichTextString(scheme);
		cell13.setCellValue(text13);
		
		Cell cell14 = row1.createCell(5);
		cell14.setCellStyle(style2);
		XSSFRichTextString text14 = new XSSFRichTextString("检验标准编号");
		cell14.setCellValue(text14);
		
		Cell cell15 = row1.createCell(6);
		cell15.setCellStyle(style2);
		XSSFRichTextString text15 = new XSSFRichTextString(Num);
		cell15.setCellValue(text15);
		//excel第二行--------------------------------
		Row row2 = sheet.createRow(2);
		Cell cell20 = row2.createCell(0);
		cell20.setCellStyle(style2);
		XSSFRichTextString text20 = new XSSFRichTextString("零件名称");
		cell20.setCellValue(text20);
		
		
		Cell cell21 = row2.createCell(1);
		Cell cell212 = row2.createCell(2);
		cell21.setCellStyle(style2);
		cell212.setCellStyle(style2);
		XSSFRichTextString text21 = new XSSFRichTextString(materialName);
		cell21.setCellValue(text21);
		CellRangeAddress range21 = new CellRangeAddress(2, 2, 1, 2); //合并单元格
		sheet.addMergedRegion(range21);
		
		Cell cell22 = row2.createCell(3);
		cell22.setCellStyle(style2);
		XSSFRichTextString text22 = new XSSFRichTextString("首用车型");
		cell22.setCellValue(text22);
		
		Cell cell23 = row2.createCell(4);
		cell23.setCellStyle(style2);
		XSSFRichTextString text23 = new XSSFRichTextString(fristCarType);
		cell23.setCellValue(text23);
		
		Cell cell24 = row2.createCell(5);
		cell24.setCellStyle(style2);
		XSSFRichTextString text24 = new XSSFRichTextString("零件特性");
		cell24.setCellValue(text24);
		
		Cell cell25 = row2.createCell(6);
		cell25.setCellStyle(style2);
		XSSFRichTextString text25 = new XSSFRichTextString(productType);
		cell25.setCellValue(text25);
		
		//excel第3行--------------------------------
		Row row3 = sheet.createRow(3);
		Cell cell30 = row3.createCell(0);
		Cell cell306 = row3.createCell(6);
		cell30.setCellStyle(style3);
		XSSFRichTextString text30 = new XSSFRichTextString("零件简图/视图");
		cell30.setCellValue(text30);
		cell306.setCellStyle(style3);
		cell306.setCellValue(text30);
		//合并单元格
		CellRangeAddress range2 = new CellRangeAddress(3, 3, 0, 6); //合并单元格
		sheet.addMergedRegion(range2);
		
		//test pic--------------
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		BufferedImage bufferImg;
		try {
			bufferImg = ImageIO.read(new File(filePath));
			ImageIO.write(bufferImg, "PNG", outStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Row row4 = sheet.createRow(4);
		row4.setHeightInPoints(200);
		// 设置图片所在列宽度为80px,注意这里单位的一个换算
		//sheet.setColumnWidth(1, (short) (35.7 * 80));
		// sheet.autoSizeColumn(i);
		byte[] bsPicValue =  null;
		if(picValue !=null){
			bsPicValue =  blobToBytes(picValue);
		}else{
			bsPicValue =outStream.toByteArray();
		}
		// 声明一个画图的顶级管理器
		//XSSFDrawing patriarch = sheet.createDrawingPatriarch();//xxxx
		Drawing patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		//XSSFClientAnchor clientAnchor = new XSSFClientAnchor();//xxxx
//		ClientAnchor clientAnchor = new XSSFClientAnchor();
		
		//XSSFComment comment = patriarch.createCellComment(clientAnchor);//xxxx
//		Comment comment = patriarch.createCellComment(clientAnchor);
		// 设置注释内容
//		comment.setString(new XSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//		comment.setAuthor("ambition");	
		
		XSSFClientAnchor anchorPic = new XSSFClientAnchor(5, 5, 5, 5,
				(short) 0, 4, (short) 7, 25);
		/*XSSFClientAnchor anchorPic = new XSSFClientAnchor(0, 0,
				1023, 255, (short) 6, 1, (short) 6, 1);*/
		anchorPic.setAnchorType(2);
		patriarch.createPicture(anchorPic, workbook.addPicture(
				bsPicValue, XSSFWorkbook.PICTURE_TYPE_JPEG));
				
		Row row = sheet.createRow(25);
		for (int i = 0; i < headers.length; i++) {
			//XSSFCell cell = row.createCell(i);//xxxx
			Cell cell = row.createCell(i);
			cell.setCellStyle(cellStyle);//样式设定为自动换行 
			//cell.setCellStyle(cellStyleBJ);//灰色背景
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		////设定冻结表头
		sheet.createFreezePane(0, 1, 0, 1);
		
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 25;
		
		while (it.hasNext()) {
			// 0行是标题,index++ 是数据开始行
			index++; 
			//logger.debug("index = " +index);
			
			row = sheet.createRow(index);
			T t = (T) it.next();
			
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			//dataseLen:对象集合不取的后边字段;startField:前面开始的字段pojo对象的开始字段数
			for (int i = 0; i < fields.length - dataseLen - startField; i++) {
				// 根据i创建excel行数
				
				//XSSFCell cell = row.createCell(i); //xxxx
				Cell cell = row.createCell(i);
				//设置数据单元格样式
				cell.setCellStyle(style2);
				//样式设定为自动换行 
				cell.setCellStyle(cellStyleWrap);
				//设置cell格式为文本格式
				cell.setCellType(XSSFCell.CELL_TYPE_STRING); 
				// Field field = fields[i];
				// startField=2 舍去:fields[0]=serialVersionUID,fields[1]=pid
				Field field = fields[i + startField]; 
				String fieldName = field.getName();
				// System.out.println("fieldName = " +fieldName );
				String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					// if (value instanceof Integer) {
					// int intValue = (Integer) value;
					// cell.setCellValue(intValue);
					// } else if (value instanceof Float) {
					// float fValue = (Float) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(fValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Double) {
					// double dValue = (Double) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(dValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Long) {
					// long longValue = (Long) value;
					// cell.setCellValue(longValue);
					// }
					//布尔型
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}

					//日起型	
					} else if(value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					//字节型	
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(
								bsValue, XSSFWorkbook.PICTURE_TYPE_JPEG));
					//其他:字符,数字
					} else {
						// 其它数据类型都当作字符串简单处理	// 如果是空就不能toString
						if (value != null) {
							textValue = value.toString();
							//System.out.println("textValue="+textValue);
							//logger.debug("value.toString() = " + value.toString());
						} else {
							textValue = "";
						}
					}

					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						//根据不同的级别码设置不同的背景颜色,加上导出速度比较慢
//						if(i == 0){
//							subCode = textValue;
//							//logger.debug("subCode = " +subCode);
//						}
//						if("1".equals(backColorFlag)){
//							int color3= Integer.valueOf(subCode)*30%255;
//							XSSFCellStyle styleTemp = workbook.createCellStyle();
//							styleTemp.setFillForegroundColor(new XSSFColor(new java.awt.Color(color3, color3, 255)));
//							styleTemp.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//							styleTemp.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//							styleTemp.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//							styleTemp.setBorderRight(XSSFCellStyle.BORDER_THIN);
//							styleTemp.setBorderTop(XSSFCellStyle.BORDER_THIN);
//							//style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//							styleTemp.setAlignment(XSSFCellStyle.ALIGN_LEFT);//居左
//							styleTemp.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//							cell.setCellStyle(styleTemp);
//						}
//						
						//没有数字型处理,都是字符串型.
						
//						if(i != 0 && i != 1 && i != 2 && i != 3){
//							Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
//							Matcher matcher = p.matcher(textValue);
//							if (matcher.matches()) {
//								// 是数字当作double处理
//								cell.setCellValue(Double.parseDouble(textValue));
//							}else{
//								XSSFRichTextString richString = new XSSFRichTextString(	textValue);
//								richString.applyFont(font3);
//								cell.setCellValue(richString);
//							}
//						}else{
//							XSSFRichTextString richString = new XSSFRichTextString(textValue);
//							richString.applyFont(font3);
//							cell.setCellValue(richString);
//						}
						
						XSSFRichTextString richString = new XSSFRichTextString(textValue);
						richString.applyFont(font3);
						cell.setCellValue(richString);
					}

				} catch (SecurityException e) {
					
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					
					e.printStackTrace();
				} finally {
					// 清理资源
				}
				
			}//一个对象各自段循环结束
			
		}//it.hasNext() ，List集合循环结束
		
		String[] ends0 = {"","","","","","",""};
		Row rowend0 = sheet.createRow(++index);
		for (int i = 0; i < ends0.length; i++) {
			Cell cell = rowend0.createCell(i);
			cell.setCellStyle(style2);//样式设定为自动换行 
			XSSFRichTextString text = new XSSFRichTextString(ends0[i]);
			cell.setCellValue(text);
		}
		
		String[] ends = {"标记","更改文件号","处数","签名","日期","编制（日期）",""};
		Row rowend = sheet.createRow(++index);
		for (int i = 0; i < ends.length; i++) {
			Cell cell = rowend.createCell(i);
			cell.setCellStyle(style2);//样式设定为自动换行 
			XSSFRichTextString text = new XSSFRichTextString(ends[i]);
			cell.setCellValue(text);
		}
		
		
		String[] ends1 = {"","","","","","审核（日期）",""};
		Row rowend1 = sheet.createRow(++index);
		for (int i = 0; i < ends1.length; i++) {
			Cell cell = rowend1.createCell(i);
			cell.setCellStyle(style2);//样式设定为自动换行 
			XSSFRichTextString text = new XSSFRichTextString(ends1[i]);
			cell.setCellValue(text);
		}
		
		
		String[] ends2 = {"","","","","","批准（日期）",""};
		Row rowend2 = sheet.createRow(++index);
		for (int i = 0; i < ends2.length; i++) {
			Cell cell = rowend2.createCell(i);
			cell.setCellStyle(style2);//样式设定为自动换行 
			XSSFRichTextString text = new XSSFRichTextString(ends2[i]);
			cell.setCellValue(text);
		}
		
		try {
			out.flush();
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("用户取消了保存或打开,您的主机中的软件中止了一个已建立的连接");
		}finally{
			dataset.clear();
			System.gc();
		}
	}


	public static void main(String[] args) {

		// 测试学生
		ExportExcelX<EntityStudent> ex = new ExportExcelX<EntityStudent>();
		String[] headers = { "学号", "姓名", "年龄", "性别", "出生日期" };
		List<EntityStudent> dataset = new ArrayList<EntityStudent>();
		dataset.add(new EntityStudent(10000001, "张三", 20, true, new Date()));
		dataset.add(new EntityStudent(20000002, "李四", 24, false, new Date()));
		dataset.add(new EntityStudent(30000003, "王五", 22, true, new Date()));
		// 测试图书
		ExportExcelX<EntityBook> ex2 = new ExportExcelX<EntityBook>();
		String[] headers2 = { "图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN",
				"图书出版社", "封面图片" };
		List<EntityBook> dataset2 = new ArrayList<EntityBook>();
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream("E:\\idms\\book.jpg"));
			byte[] buf = new byte[bis.available()];
			while ((bis.read(buf)) != -1) {
				//
			}
			dataset2.add(new EntityBook(1, "jsp", "leno", 300.33f, "1234567",
					"清华出版社", buf));
			dataset2.add(new EntityBook(2, "java编程思想", "brucl", 300.33f,
					"1234567", "阳光出版社", buf));
			dataset2.add(new EntityBook(3, "DOM艺术", "lenotang", 300.33f,
					"1234567", "清华出版社", buf));
			dataset2.add(new EntityBook(4, "c++经典", "leno", 400.33f, "1234567",
					"清华出版社", buf));
			dataset2.add(new EntityBook(5, "c#入门", "leno", 300.33f, "1234567",
					"汤春秀出版社", buf));

			OutputStream out = new FileOutputStream("E:\\a.xls");
			OutputStream out2 = new FileOutputStream("E:\\b.xls");
			ex.exportExcel(headers, dataset, out, 0, 0,"0");
			ex2.exportExcel(headers2, dataset2, out2, 0, 0,"0");
			out.close();
			JOptionPane.showMessageDialog(null, "导出成功!");
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] blobToBytes(java.sql.Blob blob) {
		 
		  BufferedInputStream is = null;
		  try {
		    is = new BufferedInputStream(blob.getBinaryStream());
		    byte[] bytes = new byte[(int) blob.length()];
		    int len = bytes.length;
		    int offset = 0;
		    int read = 0;
		    while (offset < len && (read = is.read(bytes, offset, len -offset)) >= 0) {
		      offset += read;
		    }
		    return bytes;
		  } catch (Exception e) {
		    return null;
		  } finally {
		    try {
		      is.close();
		      is = null;
		    } catch (IOException e) {
		      return null;
		    }
		  }
		}
}
