<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<h3 style="text-align: center;font-size: 25px;font-weight: bold;">工程变更申请单(ECR)</h3>
<table  class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
	<caption style="text-align: right;font-weight: bold;">ECR单号:${ecrReportNo}</caption>
	<tr lanuch="stage1">
		<td>事业群</td>
		<td>
		    <s:select list="businessUnits"
				listKey="value" 
				listValue="name" 
				name="businessUnitName" 
				id="businessUnitName" 
				cssStyle="width:140px;"
				onchange=""
				theme="simple">
			</s:select> 
		</td>
		<td>生效日期</td>
		<td>
			<input id="operationTime" name="operationTime" readonly="readonly" value='<s:date name="operationTime" format="yyyy-MM-dd"/>' />
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>分发部门</td>
		<td colspan="3">
			 <s:iterator value="ecm_ecr_dev" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="distributeDev" value="${option.name}" <s:if test="%{distributeDev.indexOf(#option.value)>=0}">checked="checked" </s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>机种编号</td>
		<td>
			<input id="machineNo" name="machineNo" value="${machineNo}" />
		</td>
		<td>客户编号</td>
		<td>
			<input id="coustomerNo" name="coustomerNo" value="${coustomerNo}"/>
			<input type="hidden"  id="coustomerName" name="coustomerName" value="${coustomerName}"/>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>变更原因</td>
		<td colspan="3">
			<s:iterator value="ecm_ecr_tyep" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="causeType" value="${option.name}" <s:if test="%{causeType.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td colspan="4">
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
				
				<s:iterator value="_ecrReportDetails" id="item" var="item">
				<tr  class="ecrReportDetails">
					<td style="width: 10%;">
						<div style="margin:0 auto;width: 42px;">
			      			<a class="small-button-bg" lanuch="stage1" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加项目"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" lanuch="stage1" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除项目"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
						</div>
					</td>
					<td  style="width:10%;text-align: center;">
						<input id="beforeCode" fieldName="beforeCode" name="beforeCode" onclick="itemNumberClick(this);" value="${beforeCode}"/>
					</td>
					<td  style="width: 20%;text-align: center;">
						<input id="beforeName" fieldName="beforeName" name="beforeName" value="${beforeName}"/>
					</td>
					<td  style="width:10%;text-align: center;">
						<input id="afterCode" fieldName="afterCode" name="afterCode" value="${afterCode}"/>
					</td>
					<td  style="width:20%;text-align: center;">
						<input id="afterName" fieldName="afterName" name="afterName" value="${afterName}"/>
					</td>
					<td style="width: 30%;text-align: center;">
						<textarea rows="2" cols="2" name="describe" fieldName="describe">${describe}</textarea>
					</td>
				</tr>
				<%a++; %>
				</s:iterator>
<%-- 				<input type="hidden" id="fir" name="fir" value="<%=a %>"/> --%>
			</table>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>变更数据</td>
		<td colspan="3">
			<s:iterator value="ecm_ecr_datas" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="ecrDatas" value="${option.name}" <s:if test="%{ecrDatas.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	 <tr lanuch="stage1">
		<td>变更时机</td>
		<td colspan="3">
			变更时机：自<input id="marketSignChange" name="marketSignChange" readonly="readonly" value='<s:date name="marketSignChange" format="yyyy-MM-dd HH:mm"/>'/>切换
			自定单号：<input id="marketSignNo" name="marketSignNo" value="${marketSignNo}"/>开始切换
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>库存处理方式</td>
		<td  colspan="3">
			<s:iterator value="ecm_ecr_inventory" var="option">
				<input label="true" type="checkbox" id="${option.id}" name="inventoryMode" value="${option.name}" <s:if test="%{inventoryMode.indexOf(#option.value)>=0}">checked="checked"</s:if> onchange="checkboxChange(this);"/>
				<label for="${option.id}" id="${option.id}_label">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>是否需要通知客户</td>
		<td>
			<s:iterator value="ecm_yes_or_no" var="option">
				<input type="radio" id="${option.id}" name="isNoticeCou" value="${option.name}" <s:if test="%{isNoticeCou.indexOf(#option.value)>=0}">checked="checked"</s:if>/>
				<label for="${option.id}" >${option.name}</label>
			</s:iterator>
		</td>
		<td>库存数量</td>
		<td>
			<input id="inventoryNum" name="inventoryNum" value="${inventoryNum}"/>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>附件</td>
		<td colspan="3">
			<input type="hidden" id="ecrFile" isFile="true" name="ecrFile" value="${ecrFile}"/>
		</td>
	</tr>
	<tr lanuch="stage1">
		<td>处置</td>
		<td colspan="3">
			是否需要试产：
			<s:iterator value="ecm_yes_or_no" var="optionPro">
				<input type="radio" id="optionPro_${option.id}" name="isNeedPro" value="${option.name}" <s:if test="%{isNeedPro.indexOf(#option.value)>=0}">checked="checked"</s:if>/>
				<label for="optionPro_${option.id}">${optionPro.name}</label>
			</s:iterator>
			是否需要RA：
			<s:iterator value="ecm_yes_or_no" var="option">
				<input type="radio" id="isNeedRa_${option.id}" name="isNeedRa" value="${option.name}" <s:if test="%{isNeedRa.indexOf(#option.value)>=0}">checked="checked"</s:if>/>
				<label for="isNeedRa_${option.id}">${option.name}</label>
			</s:iterator>
		</td>
	</tr>
	<tr>
		<td style="text-align: center;font-weight: bold;" colspan="4">选择每个环节办理人</td>
	</tr>
	<tr lanuch="stage1">
		<td colspan="4">
			<table style="width:100%;border: 0px;">
				<tr>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">主管审核:</label>
						<input type="hidden" id="chargeLoginMan" name=chargeLoginMan value="${chargeLoginMan}"/>
						<input id="chargeMan" style="width: 100px;" name="chargeMan" isLogin="true" value="${chargeMan}" readonly="readonly"/>
					</td>
					<td style="width: 16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">ME部门:</label>
						<input type="hidden" id="meLoginMan" name="meLoginMan" value="${meLoginMan}"/>
						<input id="meMan" name="meMan" style="width: 100px;" value="${meMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="width: 16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">PM部门: </label>
						<input type="hidden"  id="pmLoginMan" name="pmLoginMan" value="${pmLoginMan}"/>
						<input id="pmMan" name="pmMan" style="width: 100px;"  value="${pmMan}" isLogin="true" readonly="readonly" />
					</td>
					<td style="width:16%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">EE部门:</label>
						<input type="hidden" id="eeLoginMan"  name="eeLoginMan" value="${eeLoginMan}"/>
						<input id="eeMan" name="eeMan" style="width: 100px;" value="${eeMan}" isLogin="true" readonly="readonly"/>
					</td>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">工程:</label>
						<input type="hidden" id="projectLoginMan" name="projectLoginMan" value="${projectLoginMan}"/>
						<input id="projectMan" name="projectMan" style="width: 100px;" isLogin="true" value="${projectMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">品保:</label>
						<input type="hidden" id="qualityLoginMan" name="qualityLoginMan" value="${qualityLoginMan}"/>
						<input id="qualityMan" style="width: 100px;" name="qualityMan" isLogin="true" value="${qualityMan}" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td style="width:17%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">PMC部门:</label>
						<input type="hidden" id="pmcLoginMan" name="pmcLoginMan" value="${pmcLoginMan}"/>
						<input id="pmcMan" style="width: 100px;" name="pmcMan" isLogin="true" value="${pmcMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">市场部:</label>
						<input type="hidden" id="marketLoginMan" name="marketLoginMan" value="${marketLoginMan}"/>
						<input id="marketMan" name="marketMan" style="width: 100px;" isLogin="true"  value="${marketMan}" readonly="readonly"/>
					</td>
					<td style="width:18%;border: 0px;">
						<label style="width: 80px;display: block;float: left;">生产部:</label>
						<input type="hidden" id="produceLoginMan" name="produceLoginMan" value="${produceLoginMan}"/>
						<input id="produceMan" style="width:100px;" name="produceMan" isLogin="true" value="${produceMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">采购部:</label>
						<input type="hidden" id="purchaseLoginMan" name="purchaseLoginMan" value="${purchaseLoginMan}"/>
						<input id="purchaseMan" style="width: 100px;" name="purchaseMan" isLogin="true" value="${purchaseMan}" readonly="readonly"/>
					</td>
					<td style="border: 0px;">
						<label style="width: 80px;display: block;float: left;">事业群:</label>
						<input type="hidden" id="businessGroupLogin" name="businessGroupLogin" value="${businessGroupLogin}"/>
						<input id="businessGroup" style="width: 100px;" name="businessGroup" isLogin="true" value="${businessGroup}" readonly="readonly"/>
					</td>
					<td style="border: 0px;"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="4">
			申请人:${proposer}&nbsp;&nbsp;&nbsp;
			申请时间:<s:date name="proposerTime" format="yyyy-MM-dd HH:mm"/>&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	<tr lanuch="stage2">
		<td style="text-align: center;font-weight: bold;">主管审核</td>
		<td colspan="3">
			<textarea rows="3" cols="3" name="personLiableText" style="height: 80%;">${personLiableText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${personLiableMan}
			办理时间:<s:date name="personLiableTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage3">
		<td style="text-align: center;font-weight: bold;">ME</td>
		<td colspan="3">
			<textarea rows="3" cols="3" name="meSignText"  style="height: 80%;">${meSignText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${meSignMan}
			办理时间:<s:date name="meSignTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage4">
		<td style="text-align: center;font-weight: bold;">PM</td>
		<td colspan="3">
			<textarea rows="3" cols="3" name="pmSignText" style="height: 80%;">${pmSignText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${pmSignMan}
			办理时间:<s:date name="pmSignTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage5">
		<td style="text-align: center;font-weight: bold;">EE</td>
		<td colspan="3">
			<textarea rows="3" cols="3" name="eeSignText" style="height: 80%;">${eeSignText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${eeSignMan}
			办理时间:<s:date name="eeSignTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage6">
		<td style="text-align: center;font-weight: bold;">工程</td>
		<td colspan="3">
			<textarea rows="3" cols="3" name="projectSignText" style="height: 80%;">${projectSignText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${projectSignMan}
			办理时间:<s:date name="projectSignTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage7">
		<td style="text-align: center;font-weight: bold;">品保</td>
		<td colspan="3">
			<textarea rows="3" cols="3" name="qualitySignText" style="height: 80%;">${qualitySignText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${qualitySignMan}
			办理时间:<s:date name="qualitySignTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage8">
		<td style="text-align: center;font-weight: bold;">PMC</td>
		<td colspan="3">
			<textarea rows="" cols="" name="pmcSignText" style="height: 80%;">${pmcSignText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${pmcSignMan}
			办理时间:<s:date name="pmcSignTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage9">
		<td style="text-align: center;font-weight: bold;">市场、业务部</td>
		<td colspan="3">
			<textarea rows="" cols="" name="marketText" style="height: 80%;">${marketText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${marketTMan}
			办理时间:<s:date name="marketTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage10">
		<td style="text-align: center;font-weight: bold;">生产</td>
		<td colspan="3">
			<textarea rows="" cols="" name="produceText" style="height: 80%;">${produceText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${produceAuditMan}
			办理时间:<s:date name="produceAuditTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage11">
		<td style="text-align: center;font-weight: bold;">采购</td>
		<td colspan="3">
			<textarea rows="" cols="" name="purchaseText" style="height: 80%;">${purchaseText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${purchaseAuditMan}
			办理时间:<s:date name="purchaseAuditTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
	<tr lanuch="stage12">
		<td style="text-align: center;font-weight: bold;">事业群负责人</td>
		<td colspan="3">
			<textarea rows="" cols="" name="businessGroupText" style="height: 80%;">${businessGroupText}</textarea>
			<br/>
			<label style="float:right">
			办理人:${businessGroupMan}
			办理时间:<s:date name="businessGroupTime" format="yyyy-MM-dd HH:mm"/>
			</label>
		</td>
	</tr>
</table>
