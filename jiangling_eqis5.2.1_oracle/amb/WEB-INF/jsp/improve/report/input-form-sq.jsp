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
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">8D Corrective and Preventive Action Report (8D纠正及预防措施报告）</caption>
	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">发生日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="happenDate" name="happenDate" value="<s:date name='happenDate' format="yyyy-MM-dd"/>" />
				<%-- <s:date name='happenDate' format="yyyy-MM-dd"/> --%>
			</td>
			<td style="width:160px;text-align:center;">8D分析引导者</td>
			<td style="width:200px;">
				${sponsor}
				<input type="hidden"  name="sponsor" id="sponsor" value="${sponsor}"></input>
			</td>
			<td style="width:160px;text-align:center;">产品型号</td>
			<td style="width:200px;">
				<input  name="productModel" id="productModel" value="${productModel}"></input>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">产品阶段</td>
			<td style="width:200px;">
				<s:select list="productPhases"
					listKey="value" 
					listValue="name" 
					name="productPhase" 
					id="productPhase" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">发生区</td>
			<td style="width:200px;">
				<input  name="hanppenArea" id="hanppenArea" value="${hanppenArea}"></input>
			</td>
			<td style="width:160px;text-align:center;">机种</td>
			<td style="width:200px;">
				<input  name="model" id="model" value="${model}"></input>
				<input type="hidden" name="businessUnitCode" id="businessUnitCode" value="282"></input>
			</td>				

		</tr>
		<tr>
			<td style="width:160px;text-align:center;">客户名称</td>
			<td style="width:200px;">
				<input  name="customerName" id="customerName" value="${customerName}"></input>
			</td>		
			<td style="width:160px;text-align:center;">问题归属</td>
			<td style="width:200px;">
				<s:select list="problemBelongs"
					listKey="value" 
					listValue="name" 
					name="problemBelong" 
					id="problemBelong" 
					cssStyle="width:140px;"
					onchange="sourceChange();"
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">问题来源</td>
			<td style="width:200px;">
				<select name="problemSource" id="problemSource" >
					<option value=""></option>
				    <option ty="1" value="客户端IQC">客户端IQC</option>
					<option ty="1" value="客户端上线">客户端上线</option>
					<option ty="1" value="客户端OQC">客户端OQC</option>
					<option ty="1" value="客户端市场退机">客户端市场退机</option>
					<option ty="1" value="客户端RA实验">客户端RA实验</option>
					
					<option ty="2" value="厂内设计评审">厂内设计评审</option>
					<option ty="2" value="厂内IQC异常">厂内IQC异常</option>					
					<option ty="2" value="厂内制程异常">厂内制程异常</option>
					<option ty="2" value="厂内RA验证异常">厂内RA验证异常</option>
				</select>
			</td>			
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">lot no.</td>
			<td style="width:200px;">
				<input  name="lotNo" id="lotNo" value="${lotNo}"></input>
			</td>		
			<td style="width:160px;text-align:center;">问题类型</td>
			<td style="width:200px;">
				<s:select list="problemTypes"
					listKey="value" 
					listValue="name" 
					name="problemType" 
					id="problemType" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
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
			<td style="width:160px;text-align:center;">不良率</td>
			<td style="width:200px;">
				<input  name="unqualifiedRate" id="unqualifiedRate" value="${unqualifiedRate}"></input>
			</td>	
			<td style="width:160px;text-align:center;">生产事业群</td>
			<td style="width:200px;">
				<s:select list="productionEnterpriseGroups"
					listKey="value" 
					listValue="name" 
					name="productionEnterpriseGroup" 
					id="productionEnterpriseGroup" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">制程区段</td>
			<td style="width:200px;">
				<s:select list="processSections" 
					  theme="simple"
					  listKey="value" 
					  listValue="name" 
					  labelSeparator=""
					  id="processSection"
					  name="processSection"
					  emptyOption="false"
					  cssClass="">
				</s:select> 			
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">问题点</td>
			<td style="width:200px;">
				<input type="text"  name="problemPoint" id="problemPoint"  value="${problemPoint}" onclick="problemPointClick(this);"></input>
			</td>
			<td style="width:200px;" colspan="4">
				<textarea rows="2"   id="remark1" name="remark1"  >${remark1}</textarea>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">D1  Problem/Defect Description（问题/缺陷确认描述）</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="problemDescrible" name="problemDescrible"  >${problemDescrible}</textarea>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">D2  Form the Team（建立团队）</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">负责人</td>
			<td style="width:200px;">
				<input type="text" id="dutyManD2" isTemp="true" isUser="true" hiddenInputId="dutyManD2Login" style="float: left;"  name="dutyManD2" value="${dutyManD2}" />
				<input type="hidden" id="dutyManD2Login" name="dutyManD2Login" value="${dutyManD2Login}" />
			</td>
<%-- 			<td style="width:160px;text-align:center;">部门</td>
			<td style="width:200px;">
				<input type="text" style="float: left;" isDept="true" name="deprtmentD2" id="deprtmentD2"   value="${deprtmentD2}"></input>
			</td> --%>
			<td style="width:160px;text-align:center;">负责人主管</td>
			<td style="width:200px;">
				<input type="text" id="dutyManD2Manager" isTemp="true" isUser="true" hiddenInputId="dutyManD2ManagerLogin" style="float: left;"  name="dutyManD2Manager" value="${dutyManD2Manager}" />
				<input type="hidden" id="dutyManD2ManagerLogin" name="dutyManD2ManagerLogin" value="${dutyManD2ManagerLogin}" />
			</td>			
			<td style="width:160px;text-align:center;">预计完成时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="planDateD2" name="planDateD2" value="<s:date name='planDateD2' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td colspan="6" style="padding: 0px; border-bottom: 0px;">
			<table class="form-table-border-left" style="border: 0px; width: 100%;">
				<thead>
					<tr>
						<td style="width: 5%; text-align: center; border-top: 0px; border-left: 0px;">操作</td>
						<td width="30%" style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">部门</td>
						<td width="30%" style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">责任人</td>
						<td width="30%" style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">备注</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="_improveReportTeams" id="item" var="item">
						<tr class="improveReportTeams">
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>
							<td style="text-align: center;"><input type="text" style="float: left;margin-left: 35%;" name="deprtment"  isDept="true" value="${deprtment}" /></td>
							<td style="text-align: center;">
							<input type="text" id="dutyMan" name="dutyMan" isTemp="true"  isUser="true" style="float: left;margin-left: 35%;" value="${dutyMan}" />
							</td>
							<td style="text-align: center;"><input type="text" name="remark" value="${remark}" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">D3  Containment Action(s)（应急措施）</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">客户端库存</td>
			<td style="width:200px;">
				<input  name="clientStore" id="clientStore" value="${clientStore}" class="{digits:true}"></input>
			</td>
			<td style="width:160px;text-align:center;">在途数量</td>
			<td style="width:200px;">
				<input  name="onOrder" id="onOrder" value="${onOrder}" class="{digits:true}"></input>
			</td>
			<td style="width:160px;text-align:center;">内部成品仓库存数量</td>
			<td style="width:200px;">
				<input  name="innerStore" id="innerStore" value="${innerStore}" class="{digits:true}"></input>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">内部在线数据</td>
			<td style="width:200px;">
				<input  name="innerOnOrder" id="innerOnOrder" value="${innerOnOrder}" class="{digits:true}"></input>
			</td>
			<td style="width:160px;text-align:center;">RMA仓库存数量</td>
			<td style="width:200px;">
				<input  name="rmaStore" id="rmaStore" value="${rmaStore}" class="{digits:true}"></input>
			</td>
			<td style="width:160px;text-align:center;">原材料仓</td>
			<td style="width:200px;">
				<input  name="materialStore" id="materialStore" value="${materialStore}" class="{digits:true}"></input>
			</td>
		</tr>
		<tr>
			<td style="width:200px;text-align:center;" >应急措施</td>
			<td style="width:200px;" colspan="5">
				<textarea rows="5"   id="emergencyMeasures" name="emergencyMeasures"  >${emergencyMeasures}</textarea>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">D4  Identify Root Cause of the Problem（识别问题根因）</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">负责人</td>
			<td style="width:200px;">
				<input type="text" id="dutyManD4" isTemp="true" isUser="true" hiddenInputId="dutyManD4Login" style="float: left;"  name="dutyManD4" value="${dutyManD4}" />
				<input type="hidden" id="dutyManD4Login" name="dutyManD4Login" value="${dutyManD4Login}" />
			</td>
			<td style="width:160px;text-align:center;">部门</td>
			<td style="width:200px;">
				<input style="float: left;" isDept="true" name="deprtmentD4" id="deprtmentD4" value="${deprtmentD4}"></input>
			</td>
			<td style="width:160px;text-align:center;">预计完成时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="planDateD4" name="planDateD4" value="<s:date name='planDateD4' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">方法</td>
			<td style="width:200px;">
				<s:checkboxlist
					theme="simple" list="methods" listKey="value" listValue="name"
					name="method" value="#request.methodList">
				</s:checkboxlist>
			</td>
			<td style="width:160px;text-align:center;">原因分类</td>
			<td style="width:200px;">
				<s:select list="reasons"
					listKey="value" 
					listValue="value" 
					name="reason" 
					id="reason" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">责任部门</td>
			<td style="width:200px;">
				<s:select list="departments"
					listKey="value" 
					listValue="value" 
					name="dutyDept" 
					id="dutyDept" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="9"   id="remarkD4" name="remarkD4"  >${remarkD4}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachmentD4" name="attachmentD4" value="${attachmentD4}"/>
			</td>
		</tr>				
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6" >D5  Formulate Corrective Actions（制定纠正措施）</td>
		</tr>			
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remarkD5" name="remarkD5"  >${remarkD5}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachmentD5" name="attachmentD5" value="${attachmentD5}"/>
			</td>
		</tr>	
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6" >D6  Verify Corrective Actions（验证纠正措施）</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">负责人</td>
			<td style="width:200px;">
				<input type="text" id="dutyManD6" isTemp="true" isUser="true" hiddenInputId="dutyManD6Login" style="float: left;"  name="dutyManD6" value="${dutyManD6}" />
				<input type="hidden" id="dutyManD6Login" name="dutyManD6Login" value="${dutyManD6Login}" />
			</td>
			<td style="width:160px;text-align:center;">部门</td>
			<td style="width:200px;">
				<input style="float: left;" isDept="true"  name="deprtmentD6" id="deprtmentD6" value="${deprtmentD6}"></input>
			</td>
			<td style="width:160px;text-align:center;">预计完成时间</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="planDateD6" name="planDateD6" value="<s:date name='planDateD6' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remarkD6" name="remarkD6"  >${remarkD6}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachmentD6" name="attachmentD6" value="${attachmentD6}"/>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6" >D7  Preventive Action(s)（预防措施）</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remarkD7" name="remarkD7"  >${remarkD7}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachmentD7" name="attachmentD7" value="${attachmentD7}"/>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6" >D8  效果追踪</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">确认日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="confirmDate" name="confirmDate" value="<s:date name='confirmDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">关闭状态</td>
			<td style="width:200px;">
				<s:select list="closeStates"
					listKey="value" 
					listValue="value" 
					name="closeState" 
					id="closeState" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">关闭确认人</td>
			<td style="width:200px;">
<%-- 				<input type="text" id="closeMan" isTemp="true" isUser="true" hiddenInputId="closeManLogin" style="float: left;"  name="closeMan" value="${closeMan}" /> --%>
<%-- 				<input type="hidden" id="closeManLogin" name="closeManLogin" value="${closeManLogin}" /> --%>
			
				 <input style="float:left;width:100px" name="closeMan" id="closeMan" value="${closeMan}" />
	           <input style="float:left;" type='hidden' name="closeManLogin" id="closeManLogin" value="${closeManLogin}" />
	           <a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1(closeMan,'closeManLogin');"><span class="ui-icon ui-icon-search"  ></span></a>
	           <a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('closeMan','closeManLogin')" href="javascript:void(0);" title="<s:text name='清空'/>">
	           <span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
			</td>
		</tr>	
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remarkD8" name="remarkD8"  title="客诉问题需要连续追踪三批出货客户端上线未反馈问题，制程问题需要连续三批制造生产未发现问题">${remarkD8}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="3">
				<input type="hidden"  isFile="true" id="attachmentD8" name="attachmentD8" value="${attachmentD8}"/>
			</td>
			<td style="width:160px;text-align:center;">结案日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="closeDate" name="closeDate" value="<s:date name='closeDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
	</table>