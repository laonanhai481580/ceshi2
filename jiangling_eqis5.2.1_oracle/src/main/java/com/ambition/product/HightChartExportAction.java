package com.ambition.product;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.ambition.iqc.entity.IncomingInspectionActionsReport;
import com.norteksoft.product.web.struts2.CrudActionSupport;
import com.norteksoft.product.web.struts2.Struts2Utils;

/**    
 * 检验报告ACTION
 * @authorBy wlongfeng
 *
 */

@Namespace("/common/hight-chart")
@ParentPackage("default")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "", type = "redirectAction") })
public class HightChartExportAction extends CrudActionSupport<IncomingInspectionActionsReport> {
	private static final long serialVersionUID = 1L;

	/**
	 * 导出报表及数据
	 */
	@Action("chart-export")
	public String chartExport() throws Exception {
		String svg = Struts2Utils.getRequest().getParameter("svg");
		String filename = Struts2Utils.getRequest().getParameter("filename");
		String data=Struts2Utils.getRequest().getParameter("data");
		String label=Struts2Utils.getRequest().getParameter("label");
		String width=Struts2Utils.getRequest().getParameter("width");
		String height=Struts2Utils.getRequest().getParameter("height");
		filename = filename==null?"chart":filename;
		ServletOutputStream out = Struts2Utils.getResponse().getOutputStream();
		if (null != svg) {
		    svg = svg.replaceAll(":rect", "rect");
		    Transcoder t = new JPEGTranscoder();
	    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			TranscoderInput input = new TranscoderInput(new StringReader(svg));
			TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
			t.transcode(input, output);
			InputStream inputStream = getClass().getResourceAsStream("/template/report/chart.xls");
			Workbook workbook = WorkbookFactory.create(inputStream);
			inputStream.close();
			Sheet sheet = workbook.getSheetAt(0);
			Drawing d= sheet.createDrawingPatriarch();
			int begin=0;
			int end=0;
			if(!width.equals("")){
				begin=Integer.parseInt(width)/70;
			}
			if(!height.equals("")){
				end=Integer.parseInt(height)/25;
			}
			HSSFClientAnchor anchor=null;
			if(begin!=0&&end!=0){
				anchor= new HSSFClientAnchor(0,0,512,255,(short) 1,1,(short)begin,end);
			}else{
				anchor= new HSSFClientAnchor(0,0,512,255,(short) 1,1,(short)15,20);
			}
			d.createPicture(anchor, workbook.addPicture(byteArrayOutputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			String[] labels=label.split(",");
			//设置样式
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short)10);
			style.setFont(font);
			style.setWrapText(true);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setBorderTop((short) 1);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight((short) 1);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderBottom((short) 1);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft((short) 1);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			//创建单元格
			Row row = sheet.createRow(22);
			for(int i=0;i<labels.length;i++){
				if(i==0){
					row.createCell(i).setCellStyle(style);
				}else{
				row.createCell(i).setCellStyle(style);
				}
			}
			for(int i=0;i<labels.length;i++){
				if(labels[i].equals("rn")){
					
				}else{
					sheet.getRow(22).getCell(i).setCellValue(labels[i]);
				}
			}
			String[] datas=data.split("\\|");
			for(int i=0;i<datas.length;i++){
				Row row1 = sheet.createRow(23+i);
				for(int j=0;j<labels.length;j++){
					if(j==0){
					row1.createCell(j).setCellStyle(style);
					}else{
					row1.createCell(j).setCellStyle(style);
				}
				}
			}
			int k=23;
			for(int i=0;i<datas.length;i++){
				String[] datasStr=datas[i].split(",");
				for(int j=0;j<datasStr.length;j++){
					sheet.getRow(k).getCell(j).setCellValue(datasStr[j]);
				}
					k=k+1;
			}
			byteArrayOutputStream.close();
			//标题
			String fileName = "报表.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			workbook.write(response.getOutputStream());
		}
		out.flush();
		out.close();
		return null;
	}
	
	
	/**
	 * 导出多张报表及数据
	 */
	@Action("chart-exports")
	public String chartExports() throws Exception {
		//报表数
		int chartSize = Struts2Utils.getParameter("chartSize")!=null&&!Struts2Utils.getParameter("chartSize").equals("")?Integer.parseInt(Struts2Utils.getParameter("chartSize")):0;
		
		String filename = Struts2Utils.getRequest().getParameter("filename");
		String data=Struts2Utils.getRequest().getParameter("data");
		String label=Struts2Utils.getRequest().getParameter("label");

		filename = filename==null?"chart":filename;
		ServletOutputStream out = Struts2Utils.getResponse().getOutputStream();

		if (chartSize > 0) {
			
			InputStream inputStream = getClass().getResourceAsStream("/template/report/chart.xls");
			Workbook workbook = WorkbookFactory.create(inputStream);
			inputStream.close();
			Sheet sheet = workbook.getSheetAt(0);
			Drawing d= sheet.createDrawingPatriarch();

			for(int i=0;i<chartSize;i++){
				Transcoder t = new JPEGTranscoder();
				String svg = Struts2Utils.getRequest().getParameter("svg"+i);
//				String width=Struts2Utils.getRequest().getParameter("width"+i);
//				String height=Struts2Utils.getRequest().getParameter("height"+i);
				svg = svg.replaceAll(":rect", "rect");
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				TranscoderInput input = new TranscoderInput(new StringReader(svg));
				TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
				t.transcode(input, output);
				
				HSSFClientAnchor anchor=null;
				if(i==0){
					anchor= new HSSFClientAnchor(0,0,512,255,(short) 1,1,(short)8,13);
				}else if(i==1){
					anchor= new HSSFClientAnchor(0,0,512,255,(short) 10,1,(short)17,13);
				}else if(i%2==0){
					anchor= new HSSFClientAnchor(0,0,512,255,(short) 1,i*8,(short)8,i*8+12);
				}else{
					anchor= new HSSFClientAnchor(0,0,512,255,(short) 10,(i-1)*8,(short)17,(i-1)*8+12);
				}
				d.createPicture(anchor, workbook.addPicture(byteArrayOutputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
				byteArrayOutputStream.close();
			}


			
			String[] labels=label.split(",");
			//设置样式
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short)10);
			style.setFont(font);
			style.setWrapText(true);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setBorderTop((short) 1);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight((short) 1);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderBottom((short) 1);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft((short) 1);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			//创建单元格
			Row row = sheet.createRow(22);
			for(int i=0;i<labels.length;i++){
				if(i==0){
					row.createCell(i).setCellStyle(style);
				}else{
				row.createCell(i).setCellStyle(style);
				}
			}
			for(int i=0;i<labels.length;i++){
				if(labels[i].equals("rn")){
					
				}else{
					sheet.getRow(22).getCell(i).setCellValue(labels[i]);
				}
			}
			String[] datas=data.split("\\|");
			for(int i=0;i<datas.length;i++){
				Row row1 = sheet.createRow(23+i);
				for(int j=0;j<labels.length;j++){
					if(j==0){
					row1.createCell(j).setCellStyle(style);
					}else{
					row1.createCell(j).setCellStyle(style);
				}
				}
			}
			int k=23;
			for(int i=0;i<datas.length;i++){
				String[] datasStr=datas[i].split(",");
				for(int j=0;j<datasStr.length;j++){
					sheet.getRow(k).getCell(j).setCellValue(datasStr[j]);
				}
					k=k+1;
			}
			
			//标题
			String fileName = "报表.xls";
			byte byname[] = fileName.getBytes("gbk");
			fileName = new String(byname, "8859_1");
			HttpServletResponse response = Struts2Utils.getResponse();
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", (new StringBuilder(
					"attachment; filename=\"")).append(fileName).append("\"")
					.toString());
			workbook.write(response.getOutputStream());
		}
		out.flush();
		out.close();
		return null;
	}
	
	
	
	/**
	 * 导出报表及数据
	 */
	@SuppressWarnings("deprecation")
	@Action("chart-image-export")
	public String chartImageExport() throws Exception {
		String svg = Struts2Utils.getRequest().getParameter("svg");
		String filename = Struts2Utils.getRequest().getParameter("filename");
		filename = filename==null?"chart":filename;
		String dir = Struts2Utils.getRequest().getRealPath("/")+"\\images\\goal";
		File uploadFile = new File(dir);
		//判断指定路径dir是否存在，不存在则创建路径
		if (!uploadFile.exists() || uploadFile == null) {
			uploadFile.mkdirs();
		}
		ServletOutputStream out = Struts2Utils.getResponse().getOutputStream();
		if (null != svg) {
		    svg = svg.replaceAll(":rect", "rect");
		    Transcoder t = new JPEGTranscoder();
	    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			TranscoderInput input = new TranscoderInput(new StringReader(svg));
			TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
			t.transcode(input, output);
		}
		out.flush();
		out.close();
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	public IncomingInspectionActionsReport getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
