<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@page import="com.ambition.supplier.entity.AppraisalReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="opt-btn">
	<div style="float: left;" id="btnDiv">
		<s:if test="task.active==0 && task.processingMode.condition == '审批式'">
			<button class='btn' type="button" onclick="completeTask('<%=TaskProcessingResult.APPROVE.name()%>','${task.agreeButton}')">
				<span><span>${task.agreeButton}</span>
				</span>
			</button>
			<button class='btn' type="button" onclick="completeTask('<%=TaskProcessingResult.REFUSE.name()%>','${task.disagreeButton}')">
				<span><span>${task.disagreeButton}</span>
				</span>
			</button>
		</s:if>
		<s:if
			test="task.active==0 && task.processingMode.condition == '会签式'">
			<button class='btn' type="button" onclick="completeTask('<%=TaskProcessingResult.AGREEMENT.name()%>','${task.addSignerButton}')">
				<span><span>${task.addSignerButton}</span>
				</span>
			</button>
			<button class='btn' type="button" onclick="completeTask('<%=TaskProcessingResult.OPPOSE.name()%>','${task.removeSignerButton}')">
				<span><span>${task.removeSignerButton}</span>
				</span>
			</button>
		</s:if>
		<s:if
			test="task.active==0 && task.processingMode.condition == '编辑式'&&isImprovement">
			<button class='btn' type="button" onclick="submitImprove('APPROVE');">
				<span><span>发起改进</span>
				</span>
			</button>
			</s:if>
		<s:if test="task.active==0 && task.processingMode.condition == '编辑式'&&!isImprovement">
			
			<button class='btn' type="button" onclick="completeTask('SUBMIT')">
				<span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span>
			</button>
		</s:if>
<%-- 		<s:if test="task.active==2"> --%>
<!-- 			<button class='btn' type="button" onclick="retriveTask()"> -->
<!-- 				<span><span>取回</span></span> -->
<!-- 			</button> -->
<%-- 		</s:if> --%>
		<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
	</div>
	<div style="float:right;padding-right:4px;line-height:30px;">
	 	<s:if test="task.active==0 && task.processingMode.condition != '编辑式'">
			<a href="#opinion-table">填写意见</a>
		</s:if>
	 	<a href="#history-table">查看所有处理意见</a>
	 </div>
</div>
<div id="opt-content" class="opt-content" style="overflow-y:auto;">
<form action="${supplierctx}/admittance/sample-appraisal-report/complete-task.htm" method="post" id="appraisalReportForm" name="appraisalReportForm" >
	<input type="hidden" name="id" value="${id}"></input>
	<input type="hidden" name="taskId" id="taskId" value="${taskId}" />
	<input type="hidden" name="taskTransact" id="taskTransact" />
	<input type="hidden" name="operateName" id="operateName" value=""></input>
	<table class="form-table-border-left" id="appraisal-table"	style="width:100%;">
		<caption><h2>样件鉴定报告</h2></caption>
		<caption style="margin-bottom:4px;"><div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${code}</div></caption>
		<tr>
			<td width="10%"><span style="color:red;">*</span>供应商</td>
			<td colspan="3">
				<input id="supplierName" value="${supplier.name}" style="width:90%;" readonly="readonly" class="{required:true}"/>
				<input type="hidden" name="supplierId" id="supplierId" value="${supplier.id}"></input>
			</td>
			<td width="10%"><span style="color:red;">*</span>零件代号</td>
			<td width="15%">
				<input name="bomCodes" id="bomCodes" value="${bomCodes}" style="width:90%;" readonly="readonly" class="{required:true}"/>
			</td>
			<td width="10%"><span style="color:red;">*</span>零件名称</td>
			<td width="15%">
				<input name="bomNames" id="bomNames" value="${bomNames}" style="width:90%;" readonly="readonly" class="{required:true}"/>
			</td>
		</tr>
		<tr>
			<td  width="10%">零件等级</td>
			<td ><input type="text" name="bomLevel" id="bomLevel" value="${bomLevel}"/></td>
			<td  width="10%">供货情况</td>
			<td ><input type="text" name="supplyCircumstances" value="${supplyCircumstances}"/></td>
			<td  width="10%">供货数量</td>
			<td ><input type="text" name="supplyNumber" value="${supplyNumber}"/></td>
			<td  width="10%">批次号</td>
			<td ><input type="text" name="batchNo" value="${batchNo}"/></td>
		</tr>
		<tr>
			<td>鉴定原因</td>
			<td  colspan="7">
				${appraisalReasons}
			</td>
		</tr>
		<tr>
			<td  colspan="2">鉴定条件</td>
			<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="appraisalConditions">${appraisalConditions}</textarea></td>
		</tr>
		<tr>
			<td  colspan="2">鉴定标准</td>
			<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="appraisalStandard">${appraisalStandard}</textarea></td>
		</tr>
		<tr>
			<td  colspan="2">外观及尺寸检验报告</td>
			<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="inspectionReport">${inspectionReport }</textarea></td>
		</tr>
		<tr>
			<td  colspan="2">材料报告</td>
			<td  colspan="6" style="text-align:left"><textarea style="width:100%" name="materialReport">${materialReport }</textarea></td>
		</tr>
		<tr>
			<td  colspan="2">
				供应商提供的文件（记录）
<!-- 				<button class="btn" type="button" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传</span></span></button> -->
				<input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
				<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
			</td>
			<td  colspan="6" style="text-align:left"  id="showAttachmentFiles">
			</td>
		</tr>
		<tr>
			<td rowspan="9">性能报告</td>
			<td>启动项目</td>
			<td colspan="5">技术要求</td>
			<td>实际情况</td>
		</tr>
		<tr>
			<td>1:基本性能</td>
			<td colspan="5">PVC面料表面不应有玷污、色泽不均、裂缝、破洞、经纬条痕明显等缺陷，表面平整、清洁。</td>
			<td>
				<textarea rows="2" style="width:100%;" name="basicInfo">${basicInfo}</textarea>
			</td>
		</tr>
		<tr>
			<td>2:耐热性</td>
			<td colspan="5">短期耐热性:100±2℃/5h～室温/2h为一个循环, 共进行2个循环，零部件在高低温时及试验后不应有变形、变色等异常现象发生。长期耐热性:将样品置于90℃的恒温箱中72h后不发生异常现象，零部件在高低温时及试验后不应有变形、变色等异常现象发生</td>
			<td>
				<textarea rows="2" style="width:100%;" name="resistHot">${resistHot}</textarea>
			</td>
		</tr>
		<tr>
			<td>3:耐液性</td>
			<td colspan="5">试验溶剂为汽油、人造汗液、皮革清洗剂，试验后不得出现异常现象。</td>
			<td>
				<textarea rows="2" style="width:100%;" name="resistFluid">${resistFluid}</textarea>
			</td>
		</tr>
		<tr>
			<td>4:剥离强度</td>
			<td colspan="5">至少2.94N/25mm</td>
			<td>
				<textarea rows="2" style="width:100%;" name="peelOffIntensity">${peelOffIntensity}</textarea>
			</td>
		</tr>
		<tr>
			<td>5:耐寒性</td>
			<td colspan="5">耐寒性:试验温度-40℃，试验后零件不应有变形、变色等异常现象发生</td>
			<td>
				<textarea rows="2" style="width:100%;" name="resistCold">${resistCold}</textarea>
			</td>
		</tr>
		<tr>
			<td>6:耐冷热交变性</td>
			<td colspan="5">'90℃/4h至室温/0.5h至-40℃/1.5h至室温/0.5h至50±2℃、 95％RH/2h至室温/0.5h为一个循环,共进行3个循环后不发生变形、变色等异常现象</td>
			<td>
				<textarea rows="2" style="width:100%;" name="resistColdAndHot">${resistColdAndHot}</textarea>
			</td>
		</tr>
		<tr>
			<td>7:阻燃性</td>
			<td colspan="5">GB8410-2006《汽车内饰材料的燃烧特性》燃烧速度≤100mm/min</td>
			<td>
				<textarea rows="2" style="width:100%;" name="resistBurn">${resistBurn}</textarea>
			</td>
		</tr>
		<tr>
			<td>性能判定结论</td>
			<td colspan="6">
				<textarea rows="2" style="width:100%;" name="capabilityConclusion">${capabilityConclusion}</textarea>
			</td>
		</tr>
		<tr>
			<td rowspan="10">鉴定结果</td>
			<td colspan="2">1、鉴定件是否为工装生产？</td>
			<td colspan="5"><input type="radio" <s:if test="isIndustry"> checked="checked" </s:if> value="true" name="isIndustry"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  <s:if test="!isIndustry"> checked="checked" </s:if> type="radio" value="false" name="isIndustry"/>否</td>
		</tr>
		<tr>
			<td colspan="2">2、鉴定件是否在批量生产等同条件下进行？</td>
			<td colspan="5">
				<input type="radio" name="isSameBatchCondition" <s:if test="isSameBatchCondition"> checked="checked" </s:if>value="true"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isSameBatchCondition" <s:if test="!isSameBatchCondition"> checked="checked" </s:if> value="false"/>否</td>
		</tr>
			<tr>
			<td colspan="2">3、鉴定件是否合格？</td>
			<td colspan="5"><input type="radio" name="isStandard"  <s:if test="%{isStandard=='合格'}"> checked="checked" </s:if>value="合格"/>合格
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input  type="radio" name="isStandard"  <s:if test="%{isStandard=='不合格'}"> checked="checked" </s:if> value="不合格"/>不合格
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input  type="radio" name="isStandard"  <s:if test="%{isStandard=='部分合格'}"> checked="checked" </s:if> value="部分合格"/>部分合格</td>
		</tr>
		<tr>
			<td colspan="2">4、是否需要改进？</td>
			<td colspan="5"><input type="radio" name="isImprovement" <s:if test="isImprovement"> checked="checked" </s:if>value="true"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isImprovement" <s:if test="!isImprovement"> checked="checked" </s:if> value="false"/>否</td>
		</tr>
			<tr>
			<td colspan="2">合格项目为：</td>
			<td colspan="5">
				<textarea rows="2" style="width:100%;" name="standardItems">${standardItems}</textarea>
			</td>
		</tr>
		<tr>
			<td colspan="7">但对以下项目需要继续进行试验或验证：</td>
		</tr>
		<tr>
			<td colspan="7"><textarea width="100%" name="needTestItems">${needTestItems}</textarea></td>
		</tr>
		<tr>
			<td colspan="7">综合结论：</td>
		</tr>
		<tr>
			<td colspan="7"><textarea width="100%" name="appraisalConclusion">${appraisalConclusion}</textarea></td>
		</tr>
		<tr>
			<td colspan="2">鉴定结果</td>
			<td colspan="5">
				<input type="radio" name="appraisalResult" <s:if test="appraisalResult=='合格'"> checked="checked" </s:if>value="<%=AppraisalReport.RESULT_PASS%>"/>合格
				<input type="radio" name="appraisalResult" <s:if test="appraisalResult=='不合格'"> checked="checked" </s:if> value="<%=AppraisalReport.RESULT_FAIL%>"/>不合格
			</td>
		</tr>
		<tr>
			<td colspan="6">不合格鉴定件需要进行整改的项目及要求</td>
			<td >整改期限要求：</td>
			<td ><input name="rectificationDate"  style="width: 70%" value="${rectificationDateStr}" id="rectificationDate"/>
			<%-- <a class="small-button-bg" onclick='submitImprove(${id});'><span class="ui-icon ui-icon-info" style='cursor:pointer;'></span></a> --%>
			</td>
		</tr>
		<tr>
			<td colspan="8"><textarea width="100%" name="rectificationRequest">${rectificationRequest}</textarea></td>
		</tr>
		<tr>
			<td><span style="color:red;">*</span>会签人员</td>
			<td>
				<input type="hidden" name="evaluationMemberLoginNames" id="evaluationMemberLoginNames" value="${evaluationMemberLoginNames}" style="width:120px;"/>
				<input name="evaluationMembers" id="evaluationMembers" class="{required:true}" value="${evaluationMembers}" style="width:95%;" onclick="selectObj('选择会签人员','evaluationMembers','evaluationMemberLoginNames',true,'loginName')"/>
			</td>
			<td><span style="color:red;">*</span>审核人员</td>
			<td>
			<input name="auditManLoginName" id="auditManLoginName" value="${auditManLoginName}" type="hidden"/>
			<input name="auditMan" id="auditMan" value="${auditMan}" readonly="readonly" class="{required:true}" id="auditMan" onclick="selectObj('选择审核人员','auditMan','auditManLoginName',false,'loginName')"/></td>
			<td><span style="color:red;">*</span>报告人</td>
			<td><input name="reportMan" value="${reportMan}" readonly="readonly" onclick="selectObj('reportMan')" id="reportMan" class="{required:true}"/></td>
			<td><span style="color:red;">*</span>报告日期</td>
			<td><input  name="reportDate" value="${reportDateStr}" id="reportDate" class="{required:true}"/></td>
		</tr>
		<tr>
			<td style="text-align:right;" colspan="8">
				<button class="btn" onclick="submitImprove(${id});"><span><span><b class="btn-icons btn-icons-alarm"></b>发起改进</span></span></button>
			</td>
		</tr>
	</table>
	<s:if test="task.active==0 && task.processingMode.condition != '编辑式'">
			<table class="form-table-border-left" id="opinion-table"	style="width:100%;margin-top:4px;">
				<tr height=29>
					<td style="background:#99CCFF;font-weight: bold;font-size:14px;">
						我的意见
					</td>
				</tr>
				<tr height=29>
					<td>
						<textarea rows="6" style="width:100%;" name="opinion"></textarea>
					</td>
				</tr>
			</table>
	</s:if>	
	<%
		List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
		if(opinionParameters==null){
			opinionParameters = new ArrayList<Opinion>();
		}
	%>
	<table class="form-table-border-left" id="history-table" style="width:100%;margin-top:4px;margin-bottom:4px;">
		<tr height=29>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:30px;">
				序
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:140px;">
				时间
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;">
				处理人员
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:90px;">
				操作类型
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;">
				处理意见
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;">
				结果
			</td>
		</tr>
		<%
			int index = 1;
			for(Opinion param : opinionParameters){
		%>
			<tr height=24>
				<td>
					<%=index++ %>
				</td>
				<td>
					<%=DateUtil.formateTimeStr(param.getAddOpinionDate()) %>
				</td>
				<td>
					<%=param.getTransactorName() %>
				</td>
				<td>
					<%=param.getTaskName() %>
				</td>
				<td>
					<%=param.getOpinion()==null?"":param.getOpinion() %>
				</td>
				<td >
					<%=param.getCustomField()==null?"":param.getCustomField() %>
				</td>
			</tr>
		<%} %>
	</table>
</form>
	<div>&nbsp;</div>
</div>				