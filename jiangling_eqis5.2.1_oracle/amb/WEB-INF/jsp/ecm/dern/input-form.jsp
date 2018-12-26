<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<h3 style="text-align: center;font-size: 25px;font-weight: bold;">设计变更申请/通知单（DCR/N）</h3>
<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
	<caption style="text-align: right;font-weight: bold;">DCR/N&nbsp;NO: ${dcrnNo}</caption>
	<tr lanuch="stage1">
		<td>分发部门</td>
		<td style="width: 15%;" colspan="5">
			 <s:iterator value="ecm_dev" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="distributeDev" value="${option.name}" <s:if test="%{distributeDev.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>变更类型</td>
		<td colspan="2">
			<s:iterator value="ecm_dcrn_type" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="dcrnType" value="${option.name}" <s:if test="%{dcrnType.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
		<td>签核时效</td>
		<td colspan="2">
			<s:iterator value="ecm_dcrn_peroid" var="option">
				<input type="radio" id="${option.id}" name="nuclearSignPeroid" value="${option.name}" <s:if test="%{nuclearSignPeroid.indexOf(#option.value)>=0}">checked="checked"</s:if> />
				<label for="${option.id}" >${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td style="width:10%;">机种编号</td>
		<td style="width:25%;">
			<input id="machineNo" name="machineNo"  value="${machineNo}"/>
		</td>
		<td style="width:10%;">客户编号</td>
		<td style="width:25%;">
			<input id="coustomerNo" name="coustomerNo" value="${coustomerNo}"/>
			<input type="hidden"  id="coustomerName" name="coustomerName" value="${coustomerName}"/>
		</td>
		<td style="width:15%;">生效日期</td>
		<td style="width:15%;">
			<input id="operationTime" name="operationTime" readonly="readonly" value='<s:date name="operationTime" format="yyyy-MM-dd HH:mm"/>'/>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td rowspan="2">变更原因</td>
		<td colspan="5">
			原因说明:<br/>
			<textarea rows="3" cols="3" name="causeDiscript" style="height: 80%;">${causeDiscript}</textarea>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td colspan="5">
			<s:iterator value="ecm_dcrn_cause" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="causeType" value="${option.name}" <s:if test="%{causeType.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>变更内容</td>
		<td colspan="5">
			<s:iterator value="ecm_ecm_context" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="alterationContent" value="${option.name}" <s:if test="%{alterationContent.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>物料变更</td>
		<td colspan="5">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td rowspan="2" style="text-align: center;">操作</td>
					<td colspan="2" style="text-align: center;">变更前</td>
					<td colspan="2" style="text-align: center;">变更后</td>
					<td  rowspan="2" style="text-align: center;">处理方式</td>
				</tr>
				<tr>
					<td style="text-align: center;">物料编码</td>
					<td style="text-align: center;">物料名称</td>
					<td style="text-align: center;">物料编码</td>
					<td style="text-align: center;">物料名称</td>
				</tr>
				<% 
					int a=0;
				StringBuffer flagStr = new StringBuffer("");
				%>
				
				<s:iterator value="dcrnReportDetails">
				<%
					flagStr.append(",flag" + a);
				%>
				<tr tableName="dcrnReportDetails" name="dcrnReportDetails" class="dcrnReportDetails1">
					<td style="width: 10%;">
						<div style="margin:0 auto;width: 42px;">
			      			<a lanuch="stage1" class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加项目"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a lanuch="stage1" class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除项目"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						</div>
					</td>
					<td  style="width:10%;text-align: center;">
						<input id="flag<%=a%>_beforeCode" fieldName="beforeCode" onclick="itemNumberClick(this);" name="flag<%=a%>_beforeCode" value="${beforeCode}"/>
					</td>
					<td  style="width: 20%;text-align: center;">
						<input id="flag<%=a%>_beforeName" fieldName="beforeName" name="flag<%=a%>_beforeName" value="${beforeName}"/>
					</td>
					<td  style="width:10%;text-align: center;">
						<input id="flag<%=a%>_afterCode" fieldName="afterCode" name="flag<%=a%>_afterCode" value="${afterCode}"/>
					</td>
					<td  style="width:20%;text-align: center;">
						<input id="flag<%=a%>_afterName" fieldName="afterName" name="flag<%=a%>_afterName" value="${afterName}"/>
					</td>
					<td style="width: 30%;text-align: center;">
						<textarea rows="2" cols="2" name="flag<%=a%>_describe" fieldName="describe">${describe}</textarea>
					</td>
				</tr>
				<%a++; %>
				</s:iterator>
				<input type="hidden"  id="fir" name="fir" value="<%=a %>"/>
				<input type="hidden" id="flagIds" name="flagIds" value="<%=flagStr%>"/>
			</table>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>工艺流程变更</td>
		<td colspan="5">
			<textarea rows="2" cols="2" name="technologyAlteration">${technologyAlteration}</textarea>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>软件变更</td>
		<td colspan="5">
			<textarea rows="2" cols="2" name="softAlteration">${softAlteration}</textarea>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>其他</td>
		<td colspan="5">
			<textarea rows="2" cols="2" name="otherAlteration">${otherAlteration}</textarea>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td rowspan="5">需要提交资料</td>
		<td>
			否需要RA测试：
			<s:iterator value="ecm_yes_no" var="option">
				<input type="radio" id="isNeedRa${option.id}" name="isNeedRa" value="${option.name}" <s:if test="%{#option.value.equals(isNeedRa)}">checked="checked"</s:if>/>
				<label for="isNeedRa${option.id}">${option.name}</label>
			</s:iterator>	
		</td>
		<td>实验室</td>
		<td>
			<input id="raLaboratory" name="raLaboratory" value="${raLaboratory}"/>
			<input type="hidden" id="raFileAttach" name="raFileAttach" value="${raFileAttach}"></input>
			<button uploadBtn=true class='btn' type="button" onclick='uploadFiles("showRaFileAttach","raFileAttach");'><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			<span id="showRaFileAttach">${raFileAttach}</span>
		</td>
		<td>测试报告编号</td>
		<td>
			<input id="raReportNo" name="raReportNo" value="${raReportNo}"/>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>
			是否需要XRF测试：
			<s:iterator value="ecm_yes_no" var="option">
				<input type="radio" id="isNeedXrf${option.id}" name="isNeedXrf" value="${option.name}" <s:if test="%{#option.value.equals(isNeedXrf)}">checked="checked"</s:if>/>
				<label for="isNeedXrf${option.id}">${option.name}</label>
			</s:iterator>		
		</td>
		<td>实验室</td>
		<td>
			<input id="xrfLaboratory" name="xrfLaboratory" value="${xrfLaboratory}"/>
<%-- 			<input type="hidden" name="xrfFileAttach" id="xrfFileAttach" isFile="true" uploadBtnText="上传附件"  value="${xrfFileAttach}"/> --%>
			<input type="hidden" id="xrfFileAttach" name="xrfFileAttach" value='${xrfFileAttach}'></input>
			<button  class='btn' type="button" onclick='uploadFiles("showXrfFileAttach","xrfFileAttach");'><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			<span id="showXrfFileAttach"></span>
		</td>
		<td>测试报告编号</td>
		<td>
			<input id="xrfReportNo" name="xrfReportNo" value="${xrfReportNo}"/>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>试做报告：
			<s:iterator value="ecm_yes_no" var="option">
				<input type="radio" id="isNeedtryReport${option.id}" name="isNeedtryReport" value="${option.name}" <s:if test="%{#option.value.equals(isNeedtryReport)}">checked="checked"</s:if>/>
				<label for="isNeedtryReport${option.id}">${option.name}</label>
			</s:iterator>
		</td>
		<td>附件</td>
		<td>
<%-- 			<input type="hidden" name="tryFileAttach" id="tryFileAttach" isFile="true" uploadBtnText="上传附件"  value="${tryFileAttach}"/> --%>
			<input type="hidden" id="tryFileAttach" name="tryFileAttach" value='${tryFileAttach}'></input>
			<button  class='btn' type="button" onclick='uploadFiles("showTryFileAttach","tryFileAttach");'><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			<span id="showTryFileAttach"></span>
		</td>
		<td>客户凭证</td>
		<td>
			<s:iterator value="ecm_yes_no" var="option">
				<input type="radio" id="isCoustomer${option.id}" name="isCoustomer" value="${option.name}" <s:if test="%{#option.value.equals(isCoustomer)}">checked="checked"</s:if>/>
				<label for="isCoustomer${option.id}">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>
			第三方有害物质检测报告：
			<s:iterator value="ecm_yes_no" var="option">
				<input type="radio" id="isThridHarmful${option.id}" name="isThridHarmful" value="${option.name}" <s:if test="%{#option.value.equals(isThridHarmful)}">checked="checked"</s:if>/>
				<label for="isThridHarmful${option.id}">${option.name}</label>
			</s:iterator>
		</td>
		<td>附件</td>
		<td>
			<input type="hidden" id="harmfulFileAttach" name="harmfulFileAttach" value='${harmfulFileAttach}'></input>
			<button  class='btn' type="button" onclick='uploadFiles("showHarmfulFileAttach","harmfulFileAttach");'><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			<span id="showHarmfulFileAttach"></span>
		</td>
		<td  colspan="2">
			<textarea rows="2" cols="2" name="harmfulText">${harmfulText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>是否通知客户：
			<s:iterator value="ecm_yes_no" var="option">
				<input type="radio" id="${option.id}" name="isNoticeCou" value="${option.name}" <s:if test="%{#option.value.equals(isNoticeCou)}">checked="checked"</s:if>/>
				<label for="${option.id}">${option.name}</label>
			</s:iterator>
		</td>
		<td>通知客户说明：</td>
		<td colspan="3">
			<textarea rows="2" cols="2" name="noticeText">${noticeText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>提出人</td>
		<td>${proposedMan}</td>
		<td>提出部门</td>
		<td>${proposeDev}</td>
		<td>提出日期</td>
		<td><s:date name="proposeTime" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr lanuch="stage2">
		<td rowspan="2">部门负责人确认</td>
		<td colspan="5">
			<textarea rows="" cols="" name="personLiableText">${personLiableText}</textarea>
		</td>
	</tr>
	<tr  lanuch="stage1">
		<td>确认人:
			<input type="hidden" id="personLiableManLogin" name="personLiableManLogin" value="${personLiableManLogin}"/>
			<input id="personLiableMan" name="personLiableMan" value="${personLiableMan}" readonly="readonly" onclick='selectObj("选择人","personLiableManLogin","personLiableMan","loginName")'/>
		</td>
		<td>确认时间</td>
		<td>
			<input id="personLiableTime" name="personLiableTime" readonly="readonly" value='<s:date name="personLiableTime" format="yyyy-MM-dd HH:mm"/>'/>
			<%-- <s:date id="personLiableTime" name="personLiableTime" format="yyyy-MM-dd HH:mm"/> --%>
		</td>
		<td></td>
		<td>
		</td>
	</tr>
	<tr>
		<td colspan="5">会签部门</td>
	</tr>
	<tr  lanuch="stage1">
		<td style="text-align: center;font-weight: bold;">各部门会签人</td>
		<td colspan="5">
			<input type="hidden" id="jointlySignStrs" name="jointlySignStrs" value="${jointlySignStrs}"/>
			<table style="width:100%;border: 0px;">
				<tr>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">PM部门: </label>
						<input type="hidden" login="true"  id="pmLoginMan" name="pmLoginMan" value="${pmLoginMan}"/>
						<input id="pmMan" name="pmMan" value="${pmMan}" isLogin="true" readonly="readonly" />
					</td>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">EE部门:</label>
						<input type="hidden" login="true" id="eeLoginMan"  name="eeLoginMan" value="${eeLoginMan}"/>
						<input id="eeMan" name="eeMan" value="${eeMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">ME:</label>
						<input type="hidden" login="true" id="meLoginMan" name="meLoginMan" value="${meLoginMan}"/>
						<input id="meMan" name="meMan" value="${meMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">材料部门:</label>
						<input type="hidden" login="true" id="materialLoginMan" name="materialLoginMan" value="${materialLoginMan}"/>
						<input id="materialMan" name="materialMan" isLogin="true" value="${materialMan}" readonly="readonly"/>
					</td>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">工程:</label>
						<input type="hidden" login="true" id="projectLoginMan" name="projectLoginMan" value="${projectLoginMan}"/>
						<input id="projectMan" name="projectMan" isLogin="true" value="${projectMan}" readonly="readonly"/>
					</td>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">PMC部门:</label>
						<input type="hidden" id="pmcLoginMan" login="true" name="pmcLoginMan" value="${pmcLoginMan}"/>
						<input id="pmcMan" name="pmcMan" isLogin="true" value="${pmcMan}" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">调达课:</label>
						<input type="hidden" id="seasoningLoginMan" login="true" name="seasoningLoginMan" value="${seasoningLoginMan}"/>
						<input id="seasoningMan" name="seasoningMan" isLogin="true" value="${seasoningMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">采购部:</label>
						<input type="hidden" id="purchaseLoginMan" login="true" name="purchaseLoginMan" value="${purchaseLoginMan}"/>
						<input id="purchaseMan" name="purchaseMan" isLogin="true" value="${purchaseMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">生产部:</label>
						<input type="hidden" id="produceLoginMan" login="true" name="produceLoginMan" value="${produceLoginMan}"/>
						<input id="produceMan" name="produceMan" isLogin="true" value="${produceMan}" readonly="readonly"/>
					</td>

					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">治具部:</label>
						<input type="hidden" id="jigLoginMan"login="true" name="jigLoginMan" value="${jigLoginMan}"/>
						<input id="jigMan" name="jigMan" value="${jigMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">设备部:</label>
						<input type="hidden" id="equipmentLoginMan" login="true" name="equipmentLoginMan" value="${equipmentLoginMan}"/>
						<input id="equipmentMan" name="equipmentMan" isLogin="true" value="${equipmentMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">IE课:</label>
						<input type="hidden" id="ieLoginMan" login="true" name="ieLoginMan" value="${ieLoginMan}"/>
						<input id="ieMan" name="ieMan" value="${ieMan}" isLogin="true" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">市场部:</label>
						<input type="hidden" id="marketLoginMan" login="true" name="marketLoginMan" value="${marketLoginMan}"/>
						<input id="marketMan" name="marketMan" isLogin="true" value="${marketMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">QS:</label>
						<input type="hidden" id="qsLoginMan" login="true" name="qsLoginMan" value="${qsLoginMan}"/>
						<input id="qsMan" name="qsMan" value="${qsMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">仓库:</label>
						<input type="hidden" id="warehouseLoginMan" login="true" name="warehouseLoginMan" value="${warehouseLoginMan}"/>
						<input id="warehouseMan" name="warehouseMan" isLogin="true" value="${warehouseMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">资材部:</label>
						<input type="hidden" id="materialsLoginMan" login="true" name="materialsLoginMan" value="${materialsLoginMan}"/>
						<input id="materialsMan" name="materialsMan" isLogin="true" value="${materialsMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">品保:</label>
						<input type="hidden" id="qualityLoginMan" login="true" name="qualityLoginMan" value="${qualityLoginMan}"/>
						<input id="qualityMan" name="qualityMan" isLogin="true" value="${qualityMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">文控:</label>
						<input type="hidden" id="docControlLoginMan" login="true"  name="docControlLoginMan" value="${docControlLoginMan}"/>
						<input id="docControlMan" name="docControlMan" isLogin="true" value="${docControlMan}" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">关务:</label>
						<input type="hidden" id="guanwuLoginMan" login="true" name="guanwuLoginMan" value="${guanwuLoginMan}"/>
						<input id="guanwuMan" name="guanwuMan" isLogin="true" value="${guanwuMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">个数:</label>
						<input type="hidden" id="geshuLoginMan" login="true" name="geshuLoginMan" value="${geshuLoginMan}"/>
						<input id="geshuMan" name="geshuMan" value="${geshuMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">其他:</label>
						<input type="hidden" id="otherLoginMan" login="true" name="otherLoginMan" value="${otherLoginMan}"/>
						<input id="otherMan" name="otherMan" isLogin="true" value="${otherMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
					</td>
					<td style="border: 0px;">
					</td>
					<td style="border: 0px;">
					</td>
				</tr>				
			</table>
		</td>
	</tr>
	<tr>
		<td style="text-align: center;font-weight: bold;">执行效果确认人</td>
		<td colspan="5">
			<table style="width:100%;border: 0px;">
				<tr>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">调达：</label>
						<input type="hidden"   id="seasoningAuditManLogin" name="seasoningAuditManLogin" value="${seasoningAuditManLogin}"/>
						<input id="seasoningAuditMan" name="seasoningAuditMan" value="${seasoningAuditMan}"  readonly="readonly" onclick='selectObj("选择人","seasoningAuditManLogin","seasoningAuditMan","loginName")'/>
					</td>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">采购:</label>
						<input type="hidden"  id="purchaseAuditManLogin"  name="purchaseAuditManLogin" value="${purchaseAuditManLogin}"/>
						<input id="purchaseAuditMan" name="purchaseAuditMan" value="${purchaseAuditMan}"  readonly="readonly" onclick='selectObj("选择人","purchaseAuditManLogin","purchaseAuditMan","loginName")'/>
					</td>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">生产:</label>
						<input type="hidden"  id="produceAuditManLogin" name="produceAuditManLogin" value="${produceAuditManLogin}"/>
						<input id="produceAuditMan" name="produceAuditMan" value="${produceAuditMan}"  readonly="readonly" onclick='selectObj("选择人","produceAuditManLogin","produceAuditMan","loginName")'/>
					</td>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">效果追踪:</label>
						<input type="hidden"  id="trackManLogin" name="trackManLogin" value="${trackManLogin}"/>
						<input id="trackMan" name="trackMan" value="${trackMan}" readonly="readonly" onclick='selectObj("选择人","trackManLogin","trackMan","loginName")'/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6" style="text-align: center;font-weight: bold;">会签单位</td>
	</tr>
	<tr  lanuch="stage3" isEdit="PM" edit="PM">
		<td>PM</td>
		<td colspan="2">
			<textarea rows="3" cols="3" name="pmSignText">${pmSignText}</textarea>
		</td>
		<td colspan="3">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td colspan="2" style="text-align: center;">变更前</td>
					<td rowspan="2" style="text-align: center;">处理方式</td>
				</tr>
				<tr>
					<td style="text-align: center;">物料编码</td>
					<td style="text-align: center;">物料名称</td>
				</tr>
				<% int b=0 ;%>
				<s:iterator value="dcrnReportDetails">	
				<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
					<td>${beforeCode}</td>
					<td>${beforeName}</td>
					<td>
						<input id="handle" name="flag<%=b%>_handle" value="${handle}"/>
					</td>
				</tr>
				<%b++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="EE"  edit="EE">
		<td>EE</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="eeSignText">${eeSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="ME" edit="ME">
		<td>ME</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="meSignText">${meSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="材料组" edit="material">
		<td>材料组</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="materialSignText">${materialSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="工程组" edit="project">
		<td>工程组</td>
		<td colspan="2">
			<textarea rows="" cols="" name="projectSignText">${projectSignText}</textarea>
		</td>
		<td colspan="3">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td colspan="2" style="text-align: center;">变更前</td>
					<td rowspan="2" style="text-align: center;">处理方式</td>
				</tr>
				<tr>
					<td  style="text-align: center;">物料编码</td>
					<td  style="text-align: center;">物料名称</td>
				</tr>
				<%int c=0; %>
				<s:iterator value="dcrnReportDetails">
					<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
						<td>${beforeCode}</td>
						<td>${beforeName}</td>
						<td>
							<input id="produceHandle" name="flag<%=b%>_produceHandle" value="${produceHandle}"/>
						</td>
					</tr>
				<%c++; %>					
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="开发" edit="PMC">
		<td>PMC部</td>
		<td colspan="5">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td style="width:10%;text-align: center;">物料号</td>
					<td style="width:10%;text-align: center;">物料名称</td>
					<td style="width:10%;text-align: center;">物料库存量</td>
					<td style="width:10%;text-align: center;">成品库存量</td>
					<td style="width:10%;text-align: center;">在制原材料量</td>
					<td style="width:10%;text-align: center;">在制半成品量</td>
					<td style="width:10%;text-align: center;">在制成品量</td>
					<td style="width:30%;text-align: center;">备注</td>
				</tr>
				<%int d=0; %>
				<s:iterator value="dcrnReportDetails">
					<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
						<td>${beforeCode}</td>
						<td>${beforeName}</td>
						<td>
							<input id="bomAmount" name="flag<%=d%>_bomAmount" value="${bomAmount}"/>
						</td>
						<td>
							<input id="productAmount" name="flag<%=d%>_productAmount" value="${productAmount}"/>
						</td>
						<td>
							<input id="rawAmount" name="flag<%=d%>_rawAmount" value="${rawAmount}"/>
						</td>
						<td>
							<input id="preAmount" name="flag<%=d%>_preAmount" value="${preAmount}"/>
						</td>
						<td>
							<input id="finishAmount" name="flag<%=d%>_finishAmount" value="${finishAmount}"/>
						</td>
						<td>
							<textarea rows="3" cols="3" name="flag<%=d%>_pmcRemark">${pmcRemark}</textarea>
						</td>
					</tr>
					<% d++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="调达" edit="seasoning">
		<td>调达课</td>
		<td colspan="5">
			<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td style="width:10%;text-align: center;">物料号</td>
					<td style="width:10%;text-align: center;">物料名称</td>
					<td style="width:10%;text-align: center;">待交量</td>
					<td style="width:10%;text-align: center;">供应商在制量</td>
					<td style="width:10%;text-align: center;">供应商库存量</td>
					<td style="width:10%;text-align: center;">其他</td>
					<td style="width:10%;text-align: center;">是/否取消</td>
					<td style="width:30%;text-align: center;">备注</td>
				</tr>
				<%int e=0; %>
				<s:iterator value="dcrnReportDetails">
					<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
						<td>${beforeCode}</td>
						<td>${beforeName}</td>
						<td>
						<input type="hidden" id="beforeCode" fieldName="beforeCode" name="flag<%=a%>_beforeCode" value="${beforeCode}"/>
						<input type="hidden" id="beforeName" fieldName="beforeName" name="flag<%=a%>_beforeName" value="${beforeName}"/>
						<input type="hidden" id="afterCode" fieldName="afterCode" name="flag<%=a%>_afterCode" value="${afterCode}"/>
						<input type="hidden" id="afterName" fieldName="afterName" name="flag<%=a%>_afterName" value="${afterName}"/>
						<input  type="hidden" id="describe" name="flag<%=a%>_describe" fieldName="describe" value="${describe}"/>
						
							<input id="waitAmount" name="flag<%=e%>_waitAmount" value="${waitAmount}"/>
						</td>
						<td>
							<input id="supplierAmount" name="flag<%=e%>_supplierAmount" value="${supplierAmount}"/>
						</td>
						<td>
							<input id="supStockAmount" name="flag<%=e%>_supStockAmount" value="${supStockAmount}"/>
						</td>
						<td>
							<input id="supOtherAmount" name="flag<%=e%>_supOtherAmount" value="${supOtherAmount}"/>
						</td>
						<td>
							<input id="isSupCanle" name="flag<%=e%>_isSupCanle" value="${isSupCanle}"/>
						</td>
						<td>
							<textarea rows="3" cols="3" name="flag<%=e%>_supRemark">${supRemark}</textarea>
						</td>
					</tr>
					<%e++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="采购" edit="purchase">
		<td>采购部</td>
		<td colspan="5">
			<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td style="text-align: center;width: 10%;">物料号</td>
					<td style="text-align: center;width: 10%;">物料名称</td>
					<td style="text-align: center;width: 10%;">待交量</td>
					<td style="text-align: center;width: 10%;">供应商在制量</td>
					<td style="text-align: center;width: 10%;">供应商库存量</td>
					<td style="text-align: center;width: 10%;">其他</td>
					<td style="text-align: center;width: 10%;">是/否取消</td>
					<td style="text-align: center;width: 30%;">备注</td>
				</tr>
				<%int f=0; %>
				<s:iterator value="dcrnReportDetails">
				<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
					<td>${beforeCode}</td>
					<td>${beforeName}</td>
					<td>
						<input id="purWaitAmount" name="flag<%=f%>_purWaitAmount" value="${purWaitAmount}"/>
					</td>
					<td>
						<input id="purSupAmount" name="flag<%=f%>_purSupAmount" value="${purSupAmount}"/>
					</td>
					<td>
						<input id="purStockAmount" name="flag<%=f%>_purStockAmount" value="${purStockAmount}"/>
					</td>
					<td>
						<input id="purOtherAmount" name="flag<%=f%>_purOtherAmount" value="${purOtherAmount}"/>
					</td>
					<td>
						<input id="isPurCanle" name="flag<%=f%>_isPurCanle" value="${isPurCanle}"/>
					</td>
					<td>
						<textarea rows="2" cols="2" name="flag<%=f%>_purRemark">${purRemark}</textarea>
					</td>
				</tr>
				<%f++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="生产" edit="produce">
		<td>生产部</td>
		<td colspan="5">
			<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td style="width:10%;text-align: center;">物料号</td>
					<td style="width:10%;text-align: center;">物料名称</td>
					<td style="width:10%;text-align: center;">在制品处理</td>
					<td style="width:70%;text-align: center;">备注</td>
				</tr>
				<%int g=0; %>
				<s:iterator value="dcrnReportDetails">
				<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
					<td>${beforeCode}</td>
					<td>${beforeName}</td>
					<td>
						<%-- <s:select list="mfg_result"
							listKey="value" 
							listValue="name" 
							name="mfgResult" 
							id="mfgResult" 
							cssStyle="width:140px;"
							onchange=""
							theme="simple">
						</s:select> --%>
						<input id="mfgResult" name="flag<%=g%>_mfgResult" value="${mfgResult}"/>
					</td>
					<td>
						<textarea rows="2" cols="2" name="flag<%=g%>_mfgRemark">${mfgRemark}</textarea>
					</td>
				</tr>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="治具" edit="jig">
		<td>治具部</td>
		<td colspan="5">
			<input type="radio" id="choiceJig" name="choiceJig" value="治具改良"/>治具改良
			<input type="radio" id="choiceJig" name="choiceJig" value="新制治具"/>新制治具
			<input type="radio" id="choiceJig" name="choiceJig" value="旧治具的处理方式"/>旧治具的处理方式
			<input id="oldJigDoc" name="oldJigDoc" value="${oldJigDoc}"/>
			<input type="radio" id="choiceJig" name="choiceJig" value="其他"/>其他
			<input id="otherJigDoc" name="otherJigDoc" value="${otherJigDoc}"/>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="设备" edit="equipment">
		<td>设备部</td>
		<td colspan="5">
			<input type="radio" id="choiceEquipment" name="choiceEquipment" value="设备改良"/>设备改良
			<input type="radio" id="choiceEquipment" name="choiceEquipment" value="新制设备"/>新制设备
			<input type="radio" id="choiceEquipment" name="choiceEquipment" value="旧设备的处理方式"/>旧设备的处理方式
			<input id="oldEquipmentDoc" name="oldEquipmentDoc" value="${oldEquipmentDoc}"/>
			<input type="radio" id="choiceEquipment" name="choiceEquipment" value="其他"/>其他
			<input id="otherEquipmentDoc" name="otherEquipmentDoc" value="${otherEquipmentDoc}"/>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="IE" edit="IE">
		<td>IE课</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="ieSignText">${ieSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="市场" edit="market">
		<td>市场/业务部</td>
		<td colspan="2">变更时机：自<input id="marketSignChange" name="marketSignChange" value='<s:date name="marketSignChange" format="yyyy-MM-dd HH:mm"/>'/>切换</td>
		<td colspan="3">自定单号：<input id="marketSignNo" name="marketSignNo" value="${marketSignNo}"/>开始切换</td>
	</tr>
	<tr lanuch="stage3" isEdit="QS" edit="QS">
		<td>QS</td>
		<td colspan="5">
			HSF符合性：
				<input  type="radio" name="qsSignText" id="qsSignText-OK" value="OK"/>OK
				<input  type="radio" name="qsSignText" id="qsSignText-OK" value="NG"/>NG
				<input  type="radio" name="qsSignText" id="qsSignText-OK" value="其他"/>其他
				<input id="qsOtherDoc" name="qsOtherDoc" value="${qsOtherDoc}"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				附件:
<%-- 				<input type="hidden" isFile="true" uploadBtnText="上传附件"  id="qsSignFile" name="qsSignFile" value="${qsSignFile}"/> --%>
				<input type="hidden" id="qsSignFile" name="qsSignFile" value="${qsSignFile}"></input>
				<button  class='btn' type="button" onclick='uploadFiles("showQsSignFile","qsSignFile");'><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
				<span id="showQsSignFile">${qsSignFile}</span>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="仓库" edit="warehouse">
		<td>仓库</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="warehouseSignText" id="warehouseSignText">${warehouseSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="资材" edit="materials">
		<td>资材部</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="materialsSignText" id="materialsSignText">${materialsSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="品保" edit="quality">
		<td>品保部</td>
		<td colspan="5">
			<textarea rows="3" cols="3" id="qualitySignText" name="qualitySignText">${qualitySignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="文控" edit="docControl">
		<td>文控部</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="docControlSignText">${docControlSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="关务" edit="guanwu">
		<td>关务</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="guanwuSignText">${guanwuSignText}</textarea>
		</td>
	</tr>
	<tr lanuch="stage3" isEdit="个数" edit="geshu">
		<td>个数</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="geshuSignText">${geshuSignText}</textarea>
		</td>
	</tr>		
	<tr lanuch="stage3" isEdit="其他" edit="other">
		<td>其他部门</td>
		<td colspan="5">
			<textarea rows="3" cols="3" name="otherSignText">${otherSignText}</textarea>
		</td>
	</tr>	
	<tr><td colspan="6">材料执行状况确认</td></tr>
	<tr lanuch="stage4">
		<td>调达课</td>
		<td colspan="5">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td>物料号</td>
					<td>物料名称</td>
					<td>待交量处置结果</td>
					<td>供应商在制量处置结果</td>
					<td>供应商库存量处置结果</td>
					<td>其他处置结果</td>
				</tr>
				<%int h=0; %>
				<s:iterator value="dcrnReportDetails">				
				<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
					<td>${beforeCode}</td>
					<td>${beforeName}</td>
					<td>
						<input id="waitResult" name="flag<%=h%>_waitResult" value="${waitResult}"/>
					</td>
					<td>
						<input id="supplierResult" name="flag<%=h%>_supplierResult" value="${supplierResult}"/>
					</td>
					<td>
						<input id="supStockResult" name="flag<%=h%>_supStockResult" value="${supStockResult}"/>
					</td>
					<td>
						<input id="supOtherResult" name="flag<%=h%>_supOtherResult" value="${supOtherResult}"/>
					</td>
				</tr>
				<%h++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage5">
		<td>采购部</td>
		<td colspan="5">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td style="width:10%;text-align: center;">物料号</td>
					<td style="width:20%;text-align: center;">物料名称</td>
					<td style="width:10%;text-align: center;">待交量处置结果</td>
					<td style="width:20%;text-align: center;">供应商在制量处置结果</td>
					<td style="width:10%;text-align: center;">供应商库存量处置结果</td>
					<td style="width:20%;text-align: center;">其他处置结果</td>
				</tr>
				<%int i=0; %>
				<s:iterator value="dcrnReportDetails">				
				<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
					<td>${beforeCode}</td>
					<td>${beforeName}</td>
					<td>
						<input id="purWaitResult" name="flag<%=i%>_purWaitResult" value="${purWaitResult}"/>
					</td>
					<td>
						<input id="purSupResult" name="flag<%=i%>_purSupResult" value="${purSupResult}"/>
					</td>
					<td>
						<input id="purStockResult" name="flag<%=i%>_purStockResult" value="${purStockResult}"/>
					</td>
					<td>
						<input id="purOtherResult" name="flag<%=i%>_purOtherResult" value="${purOtherResult}"/>
					</td>
				</tr>
				<%i++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage6">
		<td>生产部</td>
		<td colspan="5">
			<table   class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
				<tr>
					<td style="width:10%;text-align: center;">物料号</td>
					<td style="width:10%;text-align: center;">物料名称</td>
					<td style="width:10%;text-align: center;">在制品处置结果</td>
					<td style="width:70%;text-align: center;">备注</td>
				</tr>
				<%int j=0; %>
				<s:iterator value="dcrnReportDetails">				
				<tr tableName="dcrnReportDetails" class="dcrnReportDetails">
					<td>${beforeCode}</td>
					<td>${beforeName}</td>
					<td>
						<input id="lastMfgResult" name="flag<%=j%>_lastMfgResult" value="${lastMfgResult}"/>
					</td>
					<td>
						<textarea rows="3" cols="3" name="flag<%=j%>_lastMfgRemark">${lastMfgRemark}</textarea>
					</td>
				</tr>
				<%j++; %>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr lanuch="stage7">
		<td>变更效果追踪</td>
		<td>
			<s:iterator value="track_results" var="option">
				<input type="checkbox" id="${option.id}" name="trackResult" value="${option.name}" <s:if test="%{#option.value.equals(trackResult)}">checked="checked"</s:if>/>
				<label for="${option.id}">${option.name}</label>
			</s:iterator>
		</td>
		<td colspan="4">
			<textarea rows="3" cols="3" name="trackText">${trackText}</textarea>
		</td>
	</tr>
</table>
