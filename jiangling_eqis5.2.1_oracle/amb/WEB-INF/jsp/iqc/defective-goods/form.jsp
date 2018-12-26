<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/taglibs.jsp"%>
<input type="hidden" name="id" id="id" value="${id}"/>
<input type="hidden" name="status" id="status" value="${status}"/>
<input type="hidden" name="params.saveType" value="input"/>
<input type="hidden" name="inspectionId" id="inspectionId" value="${inspectionId}"/>
<div style="height: 30px;text-align: center"><h2>不合格品评审申请表</h2></div>
<div style="height: 25px;width:100%;text-align: left;padding-left: 4px;">不合格品单号:${unqualityNo}</div>
<table class="form-table-border-left" id="rejectTable" style="width:100%;margin: auto;">
	<tr>
		<td style="width:12%;"><span style="color:red">*</span>供应商</td>
		<td style="width:20%">
			<input style="float:left;" name="dutySupplier" id="dutySupplier" value="${dutySupplier}" class="{required:true,messages:{required:'必填'}}"/>
			<input type="hidden" name="dutySupplierCode" id="dutySupplierCode" value="${dutySupplierCode}"/>
			<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick();" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
		</td>
		<td style="width:14%;"><span style="color:red">*</span>不良物料代码</td>
		<td style="width:20%">
			<input type="text" style="float:left;" name="materialCode" id="materialCode" value="${materialCode}" class="{required:true,messages:{required:'必填'}}"/>
			<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectComponent();" href="javascript:void(0);" title="选择物料"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
		</td>
		<td style="width:14%;"><span style="color:red">*</span>不良物料名称</td>
		<td style="width:20%">
			<input type="text" style="float:left;" name="materialName" id="materialName" value="${materialName}" class="{required:true,messages:{required:'必填'}}"/>
			<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectComponent();" href="javascript:void(0);" title="选择物料"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
		</td>
	</tr>
	<tr>
		<td>车型</td>
		<td>
			<s:select list="modelSpecifications"
					  listKey="value" 
					  listValue="value"
					  theme="simple"
					  name="modelSpecification">
			</s:select>
		</td>
		<td>批次号</td>
		<td>
			<input type="text" name="batchNo" id="batchNo" value="${batchNo}"/>
		</td>
		<td><span style="color:red">*</span>不合格品数量</td>
		<td>
			<input type="text" id="unqualifiedAmount" name="unqualifiedAmount" value="${unqualifiedAmount}" class="{number:true,required:true,min:0,messages:{required:'必填'}}"/>
		</td>
	</tr>
	<tr>
		<td>不合格来源</td>
		<td>
			<s:select list="unqualifiedSourceOptions" 
					  listKey="value" 
					  listValue="name"
					  theme="simple"
					  id="unqualifiedSource"
					  name="unqualifiedSource">
			</s:select>
		</td>
		<td><span style="color:red">*</span>重要度</td>
		<td>
			<s:select list="importanceOptions" 
					  listKey="value" 
					  listValue="value"
					  theme="simple"
					  cssClass="{required:true,messages:{required:'必填'}}"
					  id="importance"
					  name="importance">
			</s:select>
		</td>
		<td>不合格品所在地</td>
		<td>
			<input type="text" id="unqualifiedRegion" name="unqualifiedRegion" value="${unqualifiedRegion}"/>
		</td>
	</tr>
	<tr>
		<td>检验报告编号</td>
		<td colspan="5">
			<input type="text" name="unqualifiedSourceNo" id="unqualifiedSourceNo" value="${unqualifiedSourceNo}"/>
		</td>
	</tr>
	<tr>
		<td rowspan="2"><span style="color:red">*</span>不良描述</td>
		<td colspan="5">
			<textarea rows="5" style="width: 99.5%" name="unqualifiedDescription" id="unqualifiedDescription" class="{required:true,messages:{required:'必填'}}">${unqualifiedDescription}</textarea>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
			<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
			<button class='btn' type="button" onclick="uploadFiles('showAttachmentFiles','attachmentFiles');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			<span id="showAttachmentFiles"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:center" rowspan="2">申<br/>请<br/>原<br/>因<br/>及<br/>处<br/>理<br/>意<br/>见</td>
		<td	colspan="5">
			<textarea rows="8" style="width:99.5%" id="analyseReason" name="analyseReason">${analyseReason}</textarea>
		</td>
	</tr>
	<tr>
		<td colspan="5" style="text-align:right">
			提出部门&nbsp;&nbsp;<input type="text" name="analyseDept" id='analyseDept' value="${analyseDept }" onclick="selectObj('选择部门','analyseDept','DEPARTMENT_TREE')" style="width:90px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
			申请人&nbsp;<input type="text" name="analyseUser" id="analyseUser" value="${analyseUser}" onclick="selectObj('选择人员','analyseUser','MAN_DEPARTMENT_TREE')" style="width:90px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
			<span style="color:red">*</span>申请人部门经理&nbsp;<input type="text" name="analyseDeptPrincipal" id="analyseDeptPrincipal" value="${analyseDeptPrincipal}" onclick="selectPerson1(this)" style="width:90px;" class="{required:true,messages:{required:'必填'}}"/>
			<input type="hidden" name="analyseLoginName" id="analyseLoginName" value="${analyseLoginName}" />
			审核日期:<input style="width:110px;" type="text" value="<s:date name="analyseDeptDate" format="yyyy-MM-dd HH:mm"/>" disabled="disabled"/>
		</td>
	</tr>
	<tr>
		<td style="text-align:center" rowspan="4">
			不<br/>合<br/>格<br/>品<br/>评<br/>审
		</td>
		<td valign="top" rowspan="3" colspan="2">
			评审结论:</br>
			<textarea style="height:160px;" name="verifyAnalyseResult" id="verifyAnalyseResult">${verifyAnalyseResult}</textarea>
		</td>
		<td valign="top" rowspan="3" >
			<s:checkboxlist list="disposeMethodOptions"
							listKey="name"
							listValue="value"
							value="%{strss}"
							theme="css_xhtml"
							name="disposeMethod"
			></s:checkboxlist>
		</td>
		<td colspan="2" id="cbTd">
			<input type="checkbox" name="reviewMode" value="0" onclick="chooseOne(this);" <s:if test="reviewMode==0">checked="checked"</s:if>/>质量工程师评审</br>
			<input type="checkbox" name="reviewMode" value="1" onclick="chooseOne(this);" <s:if test="reviewMode==1">checked="checked"</s:if>/>不合格品评审小组评审
		</td>
	</tr>
	<tr>
		<td colspan="2">
			评审人员:
			<input type="text" style="width:60%;" onclick="selectPerson2(this)" name="reviewMen" id="reviewMen" value="${reviewMen }" readonly="readonly"/><button class='btn' type="button" onclick="$('#reviewMen').val('');$('#reviewMenLoginNames').val('');"><span><span><b class="btn-icons btn-icons-delete"></b>清空</span></span></button>
			<input type="hidden" name="reviewMenLoginNames" id="reviewMenLoginNames" value="${reviewMenLoginNames }"/>
		</td>
		
	</tr>
	<tr>
		<td  colspan="2">
		<button class='btn' type="button" onclick="endInstance();"><span><span><b class="btn-icons btn-icons-delete"></b>结束流程</span></span></button>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<input type="hidden" name="hisAttachmentFiles1" value='${attachmentFiles1}'></input>
			<input type="hidden" name="attachmentFiles1" id="attachmentFiles1" value='${attachmentFiles1}'></input>
			<button class='btn' type="button" onclick="uploadFiles('showAttachmentFiles1','attachmentFiles1');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
			<span id="showAttachmentFiles1"></span>
		</td>
	</tr>
	<tr>
		<td>质量经理处理</td>
		<td colspan="5">
			<div id="cbTd1" class="a" style="float:left;">
				<input class="aaa" type="checkbox" id="verifyState0" name="verifyState" value="0" onclick="chooseOne1(this);" <s:if test="verifyState==0">checked="checked"</s:if>/><label for="verifyState0">同意</label>
				<input class="aaa" type="checkbox" id="verifyState1" name="verifyState" value="1" onclick="chooseOne1(this);" <s:if test="verifyState==1">checked="checked"</s:if>/><label for="verifyState1">不同意</label>
				<input class="aaa" type="checkbox" id="verifyState2" name="verifyState" value="2" onclick="chooseOne1(this);" <s:if test="verifyState==2">checked="checked"</s:if>/><label for="verifyState2">提交质量部长</label>
			</div>
			<div style="margin-top:2px;float:right;">
				质量经理
				<input type="text" name="verifyUser" id="verifyUser" value="${verifyUser}" onclick="customSelectUser('选择经理','verifyUser','verifyUserLoginName')" style="width:90px;"/>
				<input type="hidden" name="verifyUserLoginName" id="verifyUserLoginName" value="${verifyUserLoginName}" />
				日期&nbsp;<input type="text" name="verifyDate" id="verifyDate" value="<s:date name='verifyDate' format='yyyy-MM-dd HH:mm'/>" style="width:120px;"/>
			</div>
		</td>
	</tr>
	<tr>
		<td>质量部长意见</td>
		<td colspan="5">
			<textarea rows="5" style="width:99.5%" id="qualitySignResult" name="qualitySignResult">${qualitySignResult}</textarea>
			<div id="cbTd2" class="a" >
			    <input class="aa" type="checkbox" id="qualityState0" name="qualityState" value="0" onclick="chooseOne2(this);" <s:if test="qualityState==0">checked="checked"</s:if>/><label for="qualityState0">同意</label>
				<input class="aa" type="checkbox" id="qualityState1" name="qualityState" value="1" onclick="chooseOne2(this);" <s:if test="qualityState==1">checked="checked"</s:if>/><label for="qualityState1">不同意</label>
				<input class="aa" type="checkbox" id="qualityState2" name="qualityState" value="2" onclick="chooseOne2(this);" <s:if test="qualityState==2">checked="checked"</s:if>/><label for="qualityState2">提交副总</label>
			</div>
			<div style="width:100%;margin-right:20px;margin-top:2px;text-align:right;">
				质量部长<input type="text" name="minister" id="minister" value="${minister}"/>&nbsp;&nbsp;&nbsp;&nbsp;
				日期&nbsp;<input type="text" name="qualitySignDate" id="qualitySignDate" value="<s:date name='qualitySignDate' format='yyyy-MM-dd'/>" style="width:120px;"/>
			</div>
		</td>
	</tr>
	<tr>
		<td>质量副总意见</td>
		<td colspan="5">
			<textarea rows="5" style="width:99.5%" id="directorOpinion"  name="directorOpinion">${directorOpinion}</textarea>
			<div style="width:100%;margin-right:20px;margin-top:2px;text-align:right;">
				质量副总<input type="text" name="president" id="president" value="${president}"/>&nbsp;&nbsp;&nbsp;&nbsp;
				日期&nbsp;<input type="text" name="directorOpininDate" id="directorOpininDate" value="<s:date name='directorOpininDate' format='yyyy-MM-dd'/>" style="width:120px;" />
			</div>
		</td>
	</tr>
	<tr>
		<td>分发</td>
		<td colspan="5">
		<div id="cbTd4">
			<input type="checkbox" name="distribute" value="0" onclick="chooseOne4(this);" <c:if test="${distribute=='0'}">checked="checked"</c:if>/>开发中心
			<input type="checkbox" name="distribute" value="1" <c:if test="${distribute=='1'}">checked="checked"</c:if>/>制造部
			<input type="checkbox" name="distribute" value="2" <c:if test="${distribute=='2'}">checked="checked"</c:if>/>物流部
			<input type="checkbox" name="distribute" value="3" <c:if test="${distribute=='3'}">checked="checked"</c:if>/>总装厂
			<input type="checkbox" name="distribute" value="4" <c:if test="${distribute=='4'}">checked="checked"</c:if>/>采购中心
			<input type="checkbox" name="distribute" value="5" <c:if test="${distribute=='5'}">checked="checked"</c:if>/>其他专业厂
			<input type="checkbox" name="distribute" value="6" <c:if test="${distribute=='6'}">checked="checked"</c:if>/>质量管理部
			<input type="checkbox" name="distribute" value="7" <c:if test="${distribute=='7'}">checked="checked"</c:if>/>其他
		</div>
		</td>
	</tr>					
</table> 