<%@page import="com.ambition.util.common.CommonUtil1"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	//生产线
    List<Option> productionLines = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_production_line");
    ActionContext.getContext().put("productionLines", productionLines);
    //执行单位
    List<Option> analyseDepartments = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_analyse_department");
    ActionContext.getContext().put("analyseDepartments", analyseDepartments);
   
    // 质量部处理意见
    List<Option> qualityOpinions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_quality_opinion");
    ActionContext.getContext().put("qualityOpinions", qualityOpinions);

    //执行单位处理意见
    List<Option> analyseDepartmentOptions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_quality_opinion");
    ActionContext.getContext().put("analyseDepartmentOptions", analyseDepartmentOptions);
    
    // 缺陷类别
    List<Option> defectLevelOptions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_defect_level");
    ActionContext.getContext().put("defectLevelOptions", defectLevelOptions);

    // 是否同意让步接收
    List<Option> agreeConcessionOptions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_agree_concession");
    ActionContext.getContext().put("agreeConcessionOptions", agreeConcessionOptions);

    // 是否需要8d单号
    List<Option> need8dOptions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_need_8d");
    ActionContext.getContext().put("need8dOptions", need8dOptions);

    // 不合格对象
    List<Option> defectTypes = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_defect_type");
    ActionContext.getContext().put("defectTypes", defectTypes);

    // 所属产业
//     ActionContext.getContext().put("industryTypeOptions", CommonUtil.getCurrentBusinessUnits());

    // 工序
    List<Option> workProcedures = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_work_procedure");
    ActionContext.getContext().put("workProcedureOptions", workProcedures);

    // 产品阶段
    List<Option> productPhases = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_product_phase");
    ActionContext.getContext().put("productPhaseOptions", productPhases);

    // 处理方式
    List<Option> disposeMethods = ApiFactory.getSettingService().getOptionsByGroupCode("market_processingMode");
    ActionContext.getContext().put("disposeMethodOptions", disposeMethods);

    // 不合格类别
    List<Option> unqualifiedTypes = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_unqualified_type");
    ActionContext.getContext().put("unqualifiedTypeOptions", unqualifiedTypes);

    // 不合格来源
    List<Option> unqualifiedSourceOptions = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_unqualified_source");
    ActionContext.getContext().put("unqualifiedSourceOptions", unqualifiedSourceOptions);
%>
<style>
<!--
.deptTab{border:1px solid #1b7aa5;padding-left:8px;padding-right:8px;border-bottom:0px;}
.deptSel{background:rgb(59,177, 234);padding: 0.2em 0.5em 0.2em 1.3em;
 	font-weight:bold;padding-left:3px;font-size: 14px;}
-->
</style>
<div style="height: 30px;text-align: center"><h2>不合格品处理单</h2></div>
<div style="height: 25px;width:99%;text-align: right;">
	<span>No.<span style="padding: 1px 11px 1px 1px;">${formNo}</span></span><input type="hidden" name="formNo" style="padding:0 4px;" value="${formNo}"/>
</div>
<table class="form-table-border-left" id="rejectTable"	style="width:100%;margin: auto;">
	<tr>
		<td style="text-align:center;" colspan="6">
			<s:checkboxlist name="defectType" value="defectType" theme="simple" list="defectTypes" listKey="value" listValue="name"/>
		</td>
	</tr>
	<tr>
		<td style="width:14%;"><font color="red">*</font>送检单位</td>
		<td style="width:20%">
			<input type="hidden" name="deptType" value="${deptType}" id="deptType"/>
			<div style='padding-left:4px;'>
				<a class="deptTab <s:if test="deptType!='外部'"> deptSel</s:if>" href="javascript:void(0);" onclick="deptTypeSelect(this);">内部</a>
				<a class="deptTab <s:if test="deptType=='外部'"> deptSel</s:if>" style="margin-left:-9px;" href="javascript:void(0);" onclick="deptTypeSelect(this);">外部</a>
			</div>
			<div>
				<input type="text" name="inspectionDept" id='inspectionDept' value="${inspectionDept}" onclick="selectInspectionDept();" style="width:90%;max-width:148px;margin-top:0px;float:left;" class="{required:true,messages:{required:'必填'}}"/>
				<a id="selectDept" class="small-button-bg" style="margin-bottom:-6px;margin-left:4px;" onclick="selectInspectionDept();" href="javascript:void(0);" title="选择产品"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
			</div>
		</td>
		<td style="width:13%;">生产线</td>
		<td style="width:20%">
			<s:select list="productionLines" 
					  listKey="value" 
					  listValue="name"
					  theme="simple"
					  cssStyle="width:90px;"
					  emptyOption=""
					  headerKey=""
					  headerValue="请选择"
					  name="productionLine"></s:select>
		</td>
		<td style="width:13%;"><font color="red">*</font>统计归属</td>
		<td style="width:20%">
			<s:select list="industryTypeOptions" 
					  listKey="businessUnitCode" 
					  listValue="businessUnitName"
					  theme="simple"
					  cssStyle="{required:true};width:90px;"
					  name="industryType"></s:select>
		</td>
	</tr>
	<tr>
		<td style="width:13%;"><font color="red">*</font>不合格来源</td>
		<td style="width:20%">
			<s:select list="unqualifiedSourceOptions" 
					  listKey="value" 
					  listValue="name"
					  theme="simple"
					  cssStyle="{required:true};width:90px;"
					  name="unqualifiedSource"></s:select>
		</td>
		<td style="width:13%;"><font color="red">*</font>检验单号</td>
		<td style="width:20%">
			<input style="width:90%;max-width:148px;" type="text" name="qualityTestingReportNo" value="${qualityTestingReportNo}" class="{required:true,messages:{required:'必填'}}"/>
		</td>
		<td style="width:13%;">缺陷分类</td>
		<td style="width:20%">
			<s:select list="defectLevelOptions" 
					  listKey="value" 
					  listValue="name"
					  theme="simple"
					  cssStyle="{required:true};width:90px;"
					  name="defectLevel"></s:select>
		</td>
	</tr>
	<tr>
		<td style="width:13%;">质检员</td>
		<td colspan="5">
			<input style="width:120px;" readonly="readonly" onclick="selectObj('请选择质检员', this, 'MAN_DEPARTMENT_TREE','loginName','checkMan');" type="text" id="checkMan" name="checkMan" value="${checkMan}"></input>
		</td>
	</tr>
	<tr><td style="font-weight: bold;text-align:center;" colspan="6">不合格品明细</td></tr>
	<tr>
		<td colspan="6" style="padding:0;">
			<table class="form-table-border" style="table-layout:fixed;border:0;">
				<tr>
			      	<td style="width:6%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
			      	<td style="width:5%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
			      	<!-- <td style="width:8%;border-top:0px;border-bottom:0px;">明细序号</td> -->
			      	<td style="width:20%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>产品代码</td>
			      	<td style="width:16%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>产品名称</td>
			      	<td style="width:16%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>产品型号</td>
			      	<td style="width:6%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>送检数</td>
					<td style="width:6%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>抽检数</td>
					<td style="width:6%;border-top:0px;border-bottom:0px;text-align:center;"><font color="red">*</font>不合格数</td>
			      	<td style="width:10%;border-top:0px;border-bottom:0px;border-right:0px;"><font color="red">*</font>送检日期</td>  
		        </tr>
		        <%int bcount=0; %>
		        <s:iterator value="detailItemList" var="detailItemList" id="detailItemList" status="st">
				    <tr class="detailItemTr">
				    	<td style="border-bottom:0px;border-left:0px;">
				    		<div style="margin:0 auto;width: 42px;">
				      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('detailItemTotal','detailItemTr',this)" title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('detailItemTotal','detailItemTr',this)" title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
							</div>
				 		</td>
				 		 <td style="border-bottom:0px;text-align:center;"><%=bcount+1%></td>
				      	<!--<td style="border-bottom:0px;text-align:center;"><input style="width:80%;" name="detailNo_<%=bcount%>" id="detailNo_<%=bcount%>" value="${detailNo}" class="{required:true,messages:{required:'必填'}}"/></td> -->
				      	<td style="border-bottom:0px;text-align:center;">
				      		<input style="width:90%;max-width:148px;" fieldName="productCode" name="productCode_<%=bcount%>" id="productCode_<%=bcount%>" onclick="selectComponentCode(this);" value="${productCode}" class="{required:true,messages:{required:'必填'}}"/>
				      		<a id="selectModel" class="small-button-bg" style="margin-bottom:-6px;margin-left:-5px;" onclick="selectComponentCode(this);" href="javascript:void(0);" title="选择产品"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				      	</td>
				      	<td style="border-bottom:0px;text-align:center;">
				      		<input style="width:90%;max-width:148px;" fieldName="productName"  name="productName_<%=bcount%>" id="productName_<%=bcount%>" value="${productName}" type="text"/>
				      	</td>
				      	<td style="border-bottom:0px;text-align:center;">
				      		<input style="width:80%;max-width:128px;" fieldName="modelSpecification" name="modelSpecification_<%=bcount%>" id="modelSpecification_<%=bcount%>" value="${modelSpecification}" class="{required:true,messages:{required:'必填'}}"/>
				      	</td>
				      	<td style="border-bottom:0px;text-align:center;"><input style="width:70%" fieldName="tatolNum" name="tatolNum_<%=bcount%>" id="tatolNum_<%=bcount %>" value="${tatolNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}"/></td>
				      	<td style="border-bottom:0px;text-align:center;"><input style="width:70%" fieldName="inspectNum" name="inspectNum_<%=bcount%>" id="inspectNum_<%=bcount %>" value="${inspectNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}"/></td>
						<td style="border-bottom:0px;text-align:center;"><input style="width:70%" fieldName="defectNum" name="defectNum_<%=bcount%>" id="defectNum_<%=bcount %>" value="${defectNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}"/></td>
				      	<td style="border-bottom:0px;border-right:0px;">
				      		<input style="width:80%" fieldName="inspectDate" readonly="readonly" name="inspectDate_<%=bcount%>" id="inspectDate_<%=bcount%>" value="<s:date name='inspectDate' format='yyyy-MM-dd'/>" class="{required:true,messages:{required:'必填'}}"/>
				      		<input fieldName="sourceType" type="hidden" name="sourceType_<%=bcount%>" value="${sourceType}"/>
				      		<input fieldName="reportNo" type="hidden" name="reportNo_<%=bcount%>" value="${reportNo}"/>
				      	</td>
                            	</tr>
                            	<%bcount++; %>
		        </s:iterator>
	        </table>
	        <input type="hidden" name="detailItemTotal" id="detailItemTotal" value="<%=bcount%>"/>
	        <input type="hidden" name="detailItems" id="detailItems"/>
	    </td>
	</tr>
	<tr>
		<td colspan="6">
		<table style="width:100%;">
			<tr>
				<td style="border:0; font-weight: bold;">
					<span>不良描述&nbsp;&nbsp;</span>
					<span style="color:#777;font-size:13px;">
						<button uploadBtn=true type="button" class='btn' onclick="uploadFiles('showInspectorAttachment','inspectorAttachment');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
						<input type="hidden" name="hisInspectorAttachment" value='${inspectorAttachment}'></input>
						<input type="hidden" name="inspectorAttachment" id="inspectorAttachment" value='${inspectorAttachment}'></input>
						<span id="showInspectorAttachment"></span>
					</span>
				</td>
			</tr>
			<tr>
				<td style="border:0;">
					<textarea id="unqualifiedDescription" rows="5" style="border: 1px solid #4C9DC5;" name="unqualifiedDescription">${unqualifiedDescription}</textarea>
				</td>
			</tr>
			<tr>
				<td style="border:0;">
					<div style="float:left;">
						<span><font color="red">*</font>选择质量工程师：</span>
						<input style="width:120px;" readonly="readonly" onclick="selectObj('请选择质量工程师', this, 'MAN_DEPARTMENT_TREE','loginName','qualityEngineerLoginName');" type="text" id="qualityEngineer" name="qualityEngineer" value="${qualityEngineer}" class="{required:true,messages:{required:'必填'}}"></input>
						<input id="qualityEngineerLoginName" name="qualityEngineerLoginName" value="${qualityEngineerLoginName}" type="hidden"></input>
					</div>
					<div style="float:right;">
						<span><font color="red">*</font>报告人：</span><input style="width:120px;" readonly="readonly" onclick="selectObj('请选择报告人', this, 'MAN_DEPARTMENT_TREE');" type="text" id="inspector" name="inspector" value="<s:if test="id>0">${inspector}</s:if><s:else><%=ContextUtils.getUserName()%></s:else>"></input>
						<span style="padding-left:20px;"><font color="red">*</font>日期：</span><input rea style="margin-right:20px;width:120px;" readonly="readonly" type="text" name="reportDate" id="reportDate" value='<s:date name='reportDate' format='yyyy-MM-dd'/>' class="{required:true,messages:{required:'必填'}}"></input>
					</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="6">
			<table style="width:100%;">
				<tr>
					<td style="border:0; font-weight: bold;">质量工程师意见:</td>
				</tr>
				<tr>
					<td style="border:0;">
						<s:checkboxlist cssStyle="margin-left:8px;" name="qualityOpinion" value="qualityOpinion" theme="simple" list="qualityOpinions" listKey="value" listValue="name" cssClass="{required:true,classes:{required:'必填'}}"/>
					</td>
				</tr>
				<tr>
					<td style="border:0;">
						<span>原因描述:</span>
						<textarea rows="5" style="border: 1px solid #4C9DC5;" name="qualityEngineerOption" id="qualityEngineerOption">${qualityEngineerOption}</textarea>
					</td>
				</tr>
				<tr>
					<td style="border:0;">
						<span>
							附件:
							<button uploadBtn=true type="button" class='btn' onclick="uploadFiles('showQualityEngineerAttachment','qualityEngineerAttachment');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
							<input type="hidden" name="hisQualityEngineerAttachment" value='${qualityEngineerAttachment}'></input>
							<input type="hidden" name="qualityEngineerAttachment" id="qualityEngineerAttachment" value='${qualityEngineerAttachment}'></input>
							<span id="showQualityEngineerAttachment"></span>
						</span>
					</td>
				</tr>
				<tr >
					<td style="border:0;">
						<div style="float:left;">
							<span>选择执行单位经理：</span>
							<input style="width:120px;" readonly="readonly" onclick="selectObj('请选择执行单位经理', this, 'MAN_DEPARTMENT_TREE','loginName','selectExecuteManLoginName');" type="text" id="selectExecuteMan" name="selectExecuteMan" value="${selectExecuteMan}"></input>
							<input id="selectExecuteManLoginName" name="selectExecuteManLoginName" value="${selectExecuteManLoginName}" type="hidden"></input>
							<!--<s:checkboxlist cssStyle="margin-left:8px;" name="analyseDepartment" value="analyseDepartment" theme="simple" list="analyseDepartments" listKey="value" listValue="name"/>-->
						</div>
						<div style="float:right;">
							<span>质量工程师：</span><span style="width:120px;">${qualityEngineer}</span>
							<span style="padding-left:20px;">处理日期：</span><s:date name='qualityEngineerDate' format='yyyy-MM-dd'/>
							<input type="hidden" name="qualityEngineerDate" value="<s:date name='qualityEngineerDate' format='yyyy-MM-dd'/>"/>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6" >
			<table style="width:100%;">
				<tr>
					<td style="border:0; font-weight: bold;">执行单位签署意见</td>
				</tr>
				<tr>
					<td style="border:0;">
						<s:checkboxlist cssStyle="margin-left:8px;" name="analyseDepartmentOption" value="analyseDepartmentOption" theme="simple" list="analyseDepartmentOptions" listKey="value" listValue="name"/>
					</td>
				</tr>
				<tr>
					<td style="border:0;">
						<span>意见描述:</span>
						<textarea rows="5" style="border: 1px solid #4C9DC5;" name="concessionReason" id="concessionReason">${concessionReason}</textarea>
					</td>
				</tr>
				<tr >
					<td style="border:0;">
						<div style="float:left;margin-right:4px;" id="concessionOrganizeNameDiv">
							<span>选择让步接收评审组织人：</span>
							<input style="width:120px;" readonly="readonly" onclick="selectObj('请选择让步接收评审组织人', this, 'MAN_DEPARTMENT_TREE','loginName','concessionOrganizeLoginName');" type="text" id="concessionOrganizeName" name="concessionOrganizeName" value="${concessionOrganizeName}"></input>
							<input id="concessionOrganizeLoginName" name="concessionOrganizeLoginName" value="${concessionOrganizeLoginName}" type="hidden"></input>
						</div>
						<div style="float:left;">
							<span>选择不合格损失鉴定人员：</span>
							<input style="width:120px;" readonly="readonly" onclick="selectObj('请选择损失鉴定人员', this, 'MAN_DEPARTMENT_TREE','loginName','identifierLoginName');" type="text" id="identifier" name="identifier" value="${identifier}"></input>
							<input id="identifierLoginName" name="identifierLoginName" value="${identifierLoginName}" type="hidden"></input>
						</div>
						<div style="float:right;">
							<span>处理人员：</span>${qualityDirector}
							<span style="padding-left:20px;">日期：</span><s:date name='completeDate' format='yyyy-MM-dd'/>
							<input type="hidden" name="completeDate" value="<s:date name='completeDate' format='yyyy-MM-dd'/>"/>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6">
			<table style="width:100%;">
				<tr>
					<td style="border:0; font-weight: bold;">让步接收评审(风险评估)(并行评审)申请部门填写:</td>
				</tr>
				<tr>
					<td style="border:0;">
						<span>&nbsp;&nbsp;&nbsp;&nbsp;评审日期:</span>
						<input name="qualitySignDate" id="qualitySignDate" value="<s:date name="qualitySignDate" format="yyyy-MM-dd"/>" style="width:120px;"/>
					</td>
				</tr>
				<tr>
					<td style="border:0;">
						<span>填写与会人员:</span>
						<input name="participants" id="participants" value="${participants}" style="width:60%;max-width: 363px;"/>
						<span style="color:#999;font-size:12px;">(以英文","隔开！)</span>
					</td>
				</tr>
				<tr>
					<td style="border:0;">
						<s:checkboxlist cssStyle="margin-left:8px;" name="concessionResult" value="concessionResult" theme="simple" list="agreeConcessionOptions" listKey="value" listValue="name"/><label>风险评估意见:</label>
					</td>
				</tr>
				<tr >
					<td style="border:0;">
						<textarea id="concessionOpinion" rows="5" style="border: 1px solid #4C9DC5;" name="concessionOpinion">${concessionOpinion}</textarea>
					</td>
				</tr>
				<tr>
					<td style="border:0px;">
						<span style="color:#777;font-size:13px;">(上传评审会议附件)</span>
						<button  uploadBtn=true type="button" class='btn' onclick="uploadFiles('showAttachment','reviewAttachment');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
						<input type="hidden" name="hisReviewAttachment" value='${reviewAttachment}'></input>
						<input type="hidden" name="reviewAttachment" id="reviewAttachment" value='${reviewAttachment}'></input>
						<span id="showAttachment"></span>
					</td>
				</tr>
				<tr>
					<td style="border:0;">
						<span>选择质量部经理：</span>
						<input style="width:120px;" readonly="readonly" onclick="selectObj('请选择质量部经理', this, 'MAN_DEPARTMENT_TREE','loginName','qualityLoginName');" type="text" id="qualityName" name="qualityName" value="${qualityName}"></input>
						<input id="qualityLoginName" name="qualityLoginName" value="${qualityLoginName}" type="hidden"></input>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6">
		<table style="width:100%;">
			<tr>
				<td style="border:0; font-weight: bold;">
					<span>质量部最终处理方式(含后期整改意见):</span>
				</td>
			</tr>
			<tr>
				<td style="border:0;">
					<textarea id="finalApproach" rows="5" style="border: 1px solid #4C9DC5;" name="finalApproach">${finalApproach}</textarea>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr><td style="font-weight: bold;text-align:center;" colspan="6">不合格损失鉴定</td></tr>
	<tr>
		<td colspan="6" style="padding:0;">
			<table class="form-table-border" style="table-layout:fixed;border:0;">
				<thead>
					<tr>
				      	<td style="width:10%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
				      	<td style="width:10%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
				      	<td style="width:20%;border-top:0px;border-bottom:0px;">质量成本科目</td>
				      	<td style="width:10%;border-top:0px;border-bottom:0px;">金额</td>
				      	<td style="width:20%;border-top:0px;border-bottom:0px;border-right:0px;">责任单位</td>  
			        </tr>
		        </thead>
		        <%int ccount=0; %>
		        <tbody>
		        <s:iterator value="composingItemList" var="composingItemList" id="composingItemList" status="st">
				    <tr class="composingItemTr">
				    	<td style="border-bottom:0px;border-left:0px;">
				    		<div style="margin:0 auto;width: 42px;">
				      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('composingItemTotal','composingItemTr',this)" title="添加损失鉴定"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
							<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('composingItemTotal','composingItemTr',this)" title="删除损失鉴定"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
							</div>
				 		</td>
				 		<td style="border-bottom:0px;text-align:center;"><%=ccount+1%></td>
				      	<td style="border-bottom:0px;">
				      		<input style="width:60%;" fieldName="name" onclick="selectComposing(this)" name="name_<%=ccount%>" id="name_<%=ccount%>" value="${name}"/>
				      		<input type="hidden" fieldName="code" name="code_<%=ccount%>" id="code_<%=ccount%>" value="${code}"/>
				      		<a id="selectModel" class="small-button-bg" style="margin-bottom:-6px;margin-left:-5px;" onclick="selectComposing(this);" href="javascript:void(0);" title="选择质量成本"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
				      	</td>
				      	<td style="border-bottom:0px;"><input fieldName="value" style="width:50%;" name="value_<%=ccount%>" id="value_<%=ccount%>" value="${value}"/></td>
				      	<td style="border-bottom:0px;border-right:0px;">
				      		<input style="width:50%;" onclick="selectObj('选择责任部门', this, 'DEPARTMENT_TREE')" fieldName="departmentName" name="departmentName_<%=ccount%>" id="departmentName_<%=ccount %>" value="${departmentName}"/>
				      		<input type="hidden" fieldName="sourceType" name="sourceType_<%=ccount%>" value="${sourceType}"/>
				      		<input type="hidden" fieldName="reportNo" name="reportNo_<%=ccount%>" value="${reportNo}"/>
				      	</td>
                        </tr>
                            	<%ccount++; %>
		        </s:iterator>
		        </tbody>
	        </table>
	        <input type="hidden" name="composingItemTotal" id="composingItemTotal" value="<%=ccount%>"/>
	        <input type="hidden" name="composingItems" id="composingItems"></input>
	    </td>
	</tr>
	<!--<tr>
		<td colspan="6" style="text-align:center; font-weight: bold;">抄送选项</td>
	</tr>-->
	<tr>
		<td colspan="6" style="height: 24px;"><span style="padding:0 0 0 4px;">是否需要8D单:</span>
			<s:checkboxlist name="need8d" value="need8d" theme="simple" list="need8dOptions" listKey="value" listValue="name"/>
			<span id="show8D" style="display:none;">8D单号
				<input type="text" name="need8dNo" value="${need8dNo}" readonly="readonly"/>
				<button type="button" start8D="true" class='btn' onclick="start8D();"><span><span><b class="btn-icons btn-icons-upload"></b>触发8D流程</span></span></button>
			</span>
		</td>
	</tr>
	<%-- <tr>
		<td colspan="6">
			<span>抄送：</span>
			<input name="carbonCopy" id="carbonCopy" value="${carbonCopy}" style="width:70%;max-width:480px;"/>
		</td>
	</tr> --%>
</table>
<br/>
