<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">品质异常联络单</caption>
	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">异常日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="exceptionDate" name="exceptionDate" value="<s:date name='exceptionDate' format="yyyy-MM-dd"/>" />					
				<%-- <s:date name='exceptionDate' format="yyyy-MM-dd"/> --%>
			</td>		
			<td style="width:160px;text-align:center;">产品型号</td>
			<td style="width:200px;">
				<input  name="productModel" id="productModel" value="${productModel}"></input>
			</td>
			<td style="width:160px;text-align:center;">问题严重度</td>
			<td style="width:200px;">
				<s:select list="problemDegrees"
					listKey="value" 
					listValue="name" 
					name="problemDegree" 
					id="problemDegree" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>				
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">流程卡号</td>
			<td style="width:200px;">
				<input  name="processCard" id="processCard" value="${processCard}"></input>
			</td>
			<td style="width:160px;text-align:center;">异常项目</td>
			<td style="width:200px;">
				<input  name="exceptionItem" id="exceptionItem" value="${exceptionItem}"></input>
			</td>
			<td style="width:160px;text-align:center;">不良数/Lot数量</td>
			<td style="width:200px;">
				<input  name="lotNum" id="lotNum" value="${lotNum}"></input>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">异常类型</td>
			<td style="width:200px;">
				<s:select list="exceptionTypes"
					listKey="value" 
					listValue="name" 
					name="exceptionType" 
					id="exceptionType" 
					cssStyle="width:140px;"
					emptyOption="true"
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">异常归属</td>
			<td style="width:200px;">
				<s:select list="exceptionBelongs"
					listKey="value" 
					listValue="name" 
					name="exceptionBelong" 
					id="exceptionBelong" 
					cssStyle="width:140px;"
					emptyOption="true"
					theme="simple">
				</s:select>			
			</td>
			<td style="width:160px;text-align:center;"></td>
			<td style="width:200px;">
			</td>
		</tr>		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">异常描述</td>
		</tr>		
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="exceptionDescrible" name="exceptionDescrible"  >${exceptionDescrible}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">提报人</td>
			<td style="width:200px;">
				${presentMan}
			</td>			
			<td style="width:160px;text-align:center;">流出单位确认</td>
			<td style="width:200px;">
				<input type="text" id="presentDeptMan" isTemp="true" isUser="true" hiddenInputId="presentDeptManLogin" style="float: left;"  name="presentDeptMan" value="${presentDeptMan}" />
				<input type="hidden" id="presentDeptManLogin" name="presentDeptManLogin" value="${presentDeptManLogin}" />
			</td>
			<td></td>
			<td></td>		
		</tr>		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">异常确认现象描述</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="exceptionDescribleConfirm" name="exceptionDescribleConfirm"  >${exceptionDescribleConfirm}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">工程确认</td>
			<td style="width:200px;">
				<input type="text" id="engineerMan" isTemp="true" isUser="true" hiddenInputId="engineerManLogin" style="float: left;"  name="engineerMan" value="${engineerMan}" />
				<input type="hidden" id="engineerManLogin" name="engineerManLogin" value="${engineerManLogin}" />
			</td>		
			<td style="width:160px;text-align:center;">品保确认</td>
			<td style="width:200px;">
				<input type="text" id="qualityMan" isTemp="true" isUser="true" hiddenInputId="qualityManLogin" style="float: left;"  name="qualityMan" value="${qualityMan}" />
				<input type="hidden" id="qualityManLogin" name="qualityManLogin" value="${qualityManLogin}" />
			</td>
			<td></td>
			<td></td>		
		</tr>			
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">紧急围堵措施</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="emergencyMeasures" name="emergencyMeasures"  >${emergencyMeasures}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">责任单位</td>
			<td style="width:200px;">
				<input type="text" id="dutyDept1" isTemp="true" isDept="true"  style="float: left;"  name="dutyDept1" value="${dutyDept1}" />
			</td>		
			<td style="width:160px;text-align:center;">作成</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan1" isTemp="true" isUser="true" hiddenInputId="dutyMan1Login" style="float: left;"  name="dutyMan1" value="${dutyMan1}" />
				<input type="hidden" id="dutyMan1Login" name="dutyMan1Login" value="${dutyMan1Login}" />
			</td>
			<td></td>
			<td></td>		
		</tr>		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">原因分析</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="reasonAnalysis" name="reasonAnalysis"  >${reasonAnalysis}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">责任单位</td>
			<td style="width:200px;">
				<input type="text" id="dutyDept2" isTemp="true" isDept="true"  style="float: left;"  name="dutyDept2" value="${dutyDept2}" />
			</td>		
			<td style="width:160px;text-align:center;">确认</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan2" isTemp="true" isUser="true" hiddenInputId="dutyMan2Login" style="float: left;"  name="dutyMan2" value="${dutyMan2}" />
				<input type="hidden" id="dutyMan2Login" name="dutyMan2Login" value="${dutyMan2Login}" />
			</td>
			<td style="width:160px;text-align:center;">作成</td>
			<td style="width:200px;">
				<input type="text" id="approvalMan2" isTemp="true" isUser="true" hiddenInputId="approvalMan2Login" style="float: left;"  name="approvalMan2" value="${approvalMan2}" />
				<input type="hidden" id="approvalMan2Login" name="approvalMan2Login" value="${approvalMan2Login}" />
			</td>		
		</tr>	
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">改善措施</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="improvementMeasures" name="improvementMeasures"  >${improvementMeasures}</textarea>
			</td>
		</tr>					
		<tr>
			<td style="width:160px;text-align:center;">负责人</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan3" isTemp="true" isUser="true" hiddenInputId="dutyMan3Login" style="float: left;"  name="dutyMan3" value="${dutyMan3}" />
				<input type="hidden" id="dutyMan3Login" name="dutyMan3Login" value="${dutyMan3Login}" />
			</td>			
			<td style="width:160px;text-align:center;">预计完成日</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="planDate" name="planDate" value="<s:date name='planDate' format="yyyy-MM-dd"/>" />
			</td>
			<td></td>
			<td></td>			
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">QE确认</td>
			<td style="width:200px;">
				<input type="text" id="qcMan" isTemp="true" isUser="true" hiddenInputId="qcManLogin" style="float: left;"  name="qcMan" value="${qcMan}" />
				<input type="hidden" id="qcManLogin" name="qcManLogin" value="${qcManLogin}" />
			</td>			
			<td style="width:160px;text-align:center;">实际完成日</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="actualDate" name="actualDate" value="<s:date name='actualDate' format="yyyy-MM-dd"/>" />
			</td>
			<td></td>
			<td></td>			
		</tr>		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">效果确认</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="effectConfirm" name="effectConfirm"  >${effectConfirm}</textarea>
			</td>
		</tr>			
		<tr>
			<td style="width:160px;text-align:center;">确认</td>
			<td style="width:200px;">
				<input type="text" id="dutyMan4" isTemp="true" isUser="true" hiddenInputId="dutyMan4Login" style="float: left;"  name="dutyMan4" value="${dutyMan4}" />
				<input type="hidden" id="dutyMan4Login" name="dutyMan4Login" value="${dutyMan4Login}" />
			</td>			
			<td style="width:160px;text-align:center;">作成</td>
			<td style="width:200px;">
				<input type="text" id="approvalMan4" isTemp="true" isUser="true" hiddenInputId="approvalMan4Login" style="float: left;"  name="approvalMan4" value="${approvalMan4}" />
				<input type="hidden" id="approvalMan4Login" name="approvalMan4Login" value="${approvalMan4Login}" />
			</td>
			<td></td>
			<td></td>			
		</tr>
	</table>