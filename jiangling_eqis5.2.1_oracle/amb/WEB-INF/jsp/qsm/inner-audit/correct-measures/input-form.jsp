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
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">不符合与纠正措施报告</caption>
	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
			<input type="hidden" id="userName" name="userName" type="text" value="${userName}" />
			<input type="hidden" id="loginName" name="loginName" type="text" value="${loginName}" />
		</th>
	</tr> 
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">事业部</td>
			<td style="width:200px;">
				<s:select list="businessUnits"
					listKey="value" 
					listValue="name" 
					name="businessUnitCode" 
					id="businessUnitCode" 
					cssStyle="width:180px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">受审核部门</td>
			<td style="width:200px;">
				<input type="text"   id="auditDept" isDept=true name="auditDept" multiple=true value="${auditDept}" style="float: left;"/> 
			</td>
			<td style="width:160px;text-align:center;">部门负责人</td>
			<td style="width:200px;">
				<input type="text" id="departmentLeader" isTemp="true" isUser="true"  multiple=true  hiddenInputId="departmentLeaderLogin" style="float: left;"  name="departmentLeader" value="${departmentLeader}" />
				<input type="hidden" id="departmentLeaderLogin" name="departmentLeaderLogin" value="${departmentLeaderLogin}" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">发起人</td>
			<td style="width:200px;">
				${setMan}
			</td>
			<td style="width:160px;text-align:center;">审核日期</td>
			<td style="width:200px;">
			<input  type="text" isDate="true" id="auditDate" name="auditDate" value="<s:date name='auditDate' format="yyyy-MM-dd"/>" />
				<%-- <s:date name='auditDate' format="yyyy-MM-dd"/> --%>
			</td>
			<td style="width:160px;text-align:center;">审核类型</td>
			<td style="width:200px;">
				<s:select list="auditTypes"
					listKey="value" 
					listValue="name" 
					name="auditType" 
					id="auditType" 
					cssStyle="width:160px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">问题描述</td>
		</tr>	
		<tr>
			<td style="width:200px;text-align: center;">
				不符合事实陈述
			</td>
			<td style="width:200px;" colspan="5">
				<textarea rows="2"   id="remark1" name="remark1"  >${remark1}</textarea>
			</td>
		</tr>	
		<tr>
<%-- 			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment1" name="attachment1" value="${attachment1}"/>
			</td> --%>
			<td style="text-align: center;">
				<input type="hidden" name="attachment1" id="attachment1" value='${attachment1}'></input>
				<input type="hidden" name="uploadMan1" id="uploadMan1" value='${uploadMan1}'></input>
				<button  class='btn' type="button" onclick="uploadFiles('showattachment1','attachment1');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			</td>
			<td colspan="5" id="showattachment1">
			</td>			
		</tr>
		<tr >
			<td style="width:160px;text-align:center;" >不符合标准条款</td>
			<td style="width:200px;" colspan="5" >
				<table class="form-table-border-left" style="border: 0px; width: 100%;">
				<tbody>
				<tr><td>操作</td><td>条款名称</td><td>体系名称</td><tr>
					<s:iterator value="_correctMeasuresItems" id="item" var="item">
						<tr class="correctMeasuresItems">
							<td style="text-align:center;width: 5%">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>
							<td style="text-align: left;"><input type="text" style="width: 40%;"  name="clauseName"  value="${clauseName}" onkeyup="searchSet(this);" /></td>
							<td style="text-align: left;"><input type="text" style="width: 40%;"  name="systemName"  value="${systemName}" onkeyup="searchSet(this);" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">不符合类型</td>
			<td style="width:200px;" >
				<s:select list="inconformityTypes"
					listKey="value" 
					listValue="name" 
					name="inconformityType" 
					id="inconformityType" 
					cssStyle="width:160px;"
					onchange=""
					theme="simple">
				</s:select>				
			</td>
			<td style="width:160px;text-align:center;">审核</td>
			<td style="width:200px;">
				<input type="text" id="auditMan1" isTemp="true" isUser="true" hiddenInputId="auditMan1Login" style="float: left;"  name="auditMan1" value="${auditMan1}" />
				<input type="hidden" id="auditMan1Login" name="auditMan1Login" value="${auditMan1Login}" />
			</td>
			<td style="width:160px;text-align:center;">日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="auditMan1Date" name="auditMan1Date" value="<s:date name='auditMan1Date' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">原因分析</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">改善担当</td>
			<td style="width:200px;" colspan="5">
				<input type="text" id="auditMan2" isTemp="true" isUser="true" multiple=true hiddenInputId="auditMan2Login" style="float: left;"  name="auditMan2" value="${auditMan2}" />
				<input type="hidden" id="auditMan2Login" name="auditMan2Login" value="${auditMan2Login}" />
			</td>
		</tr>
		<s:iterator value="_signReasonItems" id="item" var="item">
			<tr class="signReasonItems"  zbtr1="true">
				<td   style="width:160px;text-align:center;">${item.signMan}办理
				<input  type="hidden"   name="signMan" value="${signMan}" />
				<input  type="hidden"   name="signManLogin" value="${signManLogin}" />
				</td>
				<td   style="width:200px;" colspan="5">
				<table class="form-table-border-left" style="border: 0px; width: 100%;">
					<tbody>
						<tr >
							<td  colspan="4" style="text-align: left;"><textarea rows="2" item="true" login="${signManLogin}" name="signContent"  >${signContent}</textarea></td>						
						</tr>
						<tr >
							<td  colspan="2" style="text-align: left;"><input type="hidden" item="true" login="${signManLogin}"  isFile="true"  name="attachment" value="${attachment}"/></td>	
							<td  style="width:160px;text-align:center;">办理时间</td>
							<td  style="width:200px;">
								<input  type="text" isDate="true" login="${signManLogin}" item="true" name="signDate" value="<s:date name='signDate' format="yyyy-MM-dd"/>" />
							</td>												
						</tr>
					
					</tbody>
				</table>					
				</td>
			</tr>		
		</s:iterator>				
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">纠正措施</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">改善担当</td>
			<td style="width:200px;">
				<input type="text" id="auditMan3" isTemp="true" isUser="true" multiple=true  hiddenInputId="auditMan3Login"   style="float: left;"  name="auditMan3" value="${auditMan3}" />
				<input type="hidden" id="auditMan3Login" name="auditMan3Login" value="${auditMan3Login}" />
			</td>
			<td style="width:160px;text-align:center;">审核员</td>
			<td style="width:200px;">
				<input type="text" id="departmentLeader3" isTemp="true" isUser="true" hiddenInputId="departmentLeader3Login" style="float: left;"  name="departmentLeader3" value="${departmentLeader3}" />
				<input type="hidden" id="departmentLeader3Login" name="departmentLeader3Login" value="${departmentLeader3Login}" />
			</td>				
			<td style="width:160px;text-align:center;">预计完成时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="departmentLeader3Date" name="departmentLeader3Date" value="<s:date name='departmentLeader3Date' format="yyyy-MM-dd"/>" />
			</td>					
		</tr>		
		<s:iterator value="_signMeasureItems" id="item" var="item">
			<tr class="signMeasureItems"  zbtr1="true">
				<td   style="width:160px;text-align:center;">${item.signMan}办理
				<input  type="hidden"   name="signMan" value="${signMan}" />
				<input  type="hidden"   name="signManLogin" value="${signManLogin}" />
				</td>
				<td   style="width:200px;" colspan="5">
				<table class="form-table-border-left" style="border: 0px; width: 100%;">
					<tbody>
						<tr >
							<td  colspan="4" style="text-align: left;"><textarea rows="2" item="true" login="${signManLogin}" name="signContent"  >${signContent}</textarea></td>						
						</tr>
						<tr >
							<td  colspan="2" style="text-align: left;"><input type="hidden" item="true" login="${signManLogin}"  isFile="true"  name="attachment" value="${attachment}"/></td>	
							<td  style="width:160px;text-align:center;">办理时间</td>
							<td  style="width:200px;">
								<input  type="text" isDate="true" login="${signManLogin}" item="true" name="signDate" value="<s:date name='signDate' format="yyyy-MM-dd"/>" />
							</td>												
						</tr>
					
					</tbody>
				</table>					
				</td>
			</tr>		
		</s:iterator>			
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">纠正措施完成情况</td>
		</tr>		
		<tr>
			<td style="width:160px;text-align:center;">改善担当</td>
			<td style="width:200px;">
				<input type="text" id="auditMan4" isTemp="true" isUser="true"  multiple=true  hiddenInputId="auditMan4Login" style="float: left;"  name="auditMan4" value="${auditMan4}" />
				<input type="hidden" id="auditMan4Login" name="auditMan4Login" value="${auditMan4Login}" />
			</td>
			<td style="width:160px;text-align:center;">负责人确认</td>
			<td style="width:200px;">
				<input type="text" id="departmentLeader4" isTemp="true" isUser="true" hiddenInputId="departmentLeader4Login" style="float: left;"  name="departmentLeader4" value="${departmentLeader4}" />
				<input type="hidden" id="departmentLeader4Login" name="departmentLeader4Login" value="${departmentLeader4Login}" />
			</td>
			<td style="width:160px;text-align:center;">日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="departmentLeader4Date" name="departmentLeader4Date" value="<s:date name='departmentLeader4Date' format="yyyy-MM-dd"/>" />
			</td>				
		</tr>	
		<s:iterator value="_signCompleteItems" id="item" var="item">
			<tr class="signCompleteItems"  zbtr1="true">
				<td   style="width:160px;text-align:center;">${item.signMan}办理
				<input  type="hidden"   name="signMan" value="${signMan}" />
				<input  type="hidden"   name="signManLogin" value="${signManLogin}" />
				</td>
				<td   style="width:200px;" colspan="5">
				<table class="form-table-border-left" style="border: 0px; width: 100%;">
					<tbody>
						<tr >
							<td  colspan="4" style="text-align: left;"><textarea rows="2" item="true" login="${signManLogin}" name="signContent"  >${signContent}</textarea></td>						
						</tr>
						<tr >
							<td  colspan="2" style="text-align: left;"><input type="hidden" item="true" login="${signManLogin}"  isFile="true"  name="attachment" value="${attachment}"/></td>	
							<td  style="width:160px;text-align:center;">办理时间</td>
							<td  style="width:200px;">
								<input  type="text" isDate="true" login="${signManLogin}" item="true" name="signDate" value="<s:date name='signDate' format="yyyy-MM-dd"/>" />
							</td>												
						</tr>
					
					</tbody>
				</table>					
				</td>
			</tr>		
		</s:iterator>			
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">措施验证</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark5" name="remark5"  >${remark5}</textarea>
			</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment5" name="attachment5" value="${attachment5}"/>
			</td>
		</tr>			
		<tr>
			<td style="width:160px;text-align:center;"></td>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center;">审核员</td>
			<td style="width:200px;">
				<input type="text" id="auditMan5" isTemp="true" isUser="true" hiddenInputId="auditMan5Login" style="float: left;"  name="auditMan5" value="${auditMan5}" />
				<input type="hidden" id="auditMan5Login" name="auditMan5Login" value="${auditMan5Login}" />
			</td>
			<td style="width:160px;text-align:center;">日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="auditMan5Date" name="auditMan5Date" value="<s:date name='auditMan5Date' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		
		
		
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">问题关闭</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="2"   id="remark6" name="remark6"  >${remark6}</textarea>
			</td>
		</tr>	
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment6" name="attachment6" value="${attachment6}"/>
			</td>
		</tr>			
		<tr>
			<td style="width:160px;text-align:center;"></td>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center;">组长确认</td>
			<td style="width:200px;">
				<input type="text" id="auditMan6" isTemp="true" isUser="true" hiddenInputId="auditMan6Login" style="float: left;"  name="auditMan6" value="${auditMan6}" />
				<input type="hidden" id="auditMan6Login" name="auditMan6Login" value="${auditMan6Login}" />
			</td>
			<td style="width:160px;text-align:center;">日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="auditMan6Date" name="auditMan6Date" value="<s:date name='auditMan6Date' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
	</table>