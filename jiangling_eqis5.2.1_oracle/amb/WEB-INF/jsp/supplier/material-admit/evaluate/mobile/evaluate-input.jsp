<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>样品评估报告</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<link rel="shortcut icon">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${ctx}/mobile/css/sm.css">
<link rel="stylesheet" href="${ctx}/mobile/css/swiper.min.css">
<link rel="stylesheet" href="${ctx}/mobile/css/style.css">
<%@ include file="style.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/js/jquery-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<!-- <script src="https://cdn.bootcss.com/jquery/2.0.3/jquery.min.js"></script> -->
<%-- <script type="text/javascript" src="${ctx}/mobile/js/jquery-1.9.1.min.js"></script> --%>
<script type="text/javascript" src="${ctx}/mobile/js/date.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/iscroll.js"></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
 <%@ include file="input-script.jsp" %>
 </head>
 <body>
 <div class="cause">
	<a><span class="cause_0"><%-- <img src="${ctx}/mobile/img/comeback.png"> --%></span></a><span class="cause_1">样品评估报告</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
 <input type="hidden" name="id" id="id" value="${id}"  />
 <input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
	<div class="syq">
		<ul class="syq_1">
			<li>
				<div class="syq_2">
					<span>编号</span>
				</div>
				<div class="syq_2"><span>${formNo }</span></div>
			</li>
			<li>
				<div class="syq_2"><span>事业部</span></div>
				<div >
					<input type="text" class="method" id="businessUnitName" name="businessUnitName" value="${businessUnitName }">
				</div>
			</li>
			<li>
			<div class="syq_2">
				<span>紧急程度</span>
			</div>
			<div >
				<s:select list="urgencyLevels" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="degreeLevel"
				  id="degreeLevel"
				  emptyOption="false"
				  labelSeparator=""
				  cssClass="method"
				  ></s:select>
			</div>
			</li>
			<li>
				<div class="syq_2"><span>产品型号</span></div>
				<div >
					<input type="text" class="method" id="productType" name="productType" value="${productType }">
				</div>
			</li>
			<li>
			<div class="syq_2"><span>物料编号</span></div>
			<div >
				<input type="text" id="materialCode" class="method" name="materialCode" value="${materialCode }" >
			</div>
			</li>
			<li>
				<div class="syq_2"><span>物料名称</span></div>
				<div >
					<input type="text" class="method" id="materialName" name="materialName" value="${materialName }">
				</div>
			</li>
			<li>
				<div class="syq_2"><span>物料类别</span></div>
				<div >
					<input type="text" class="method" id="materialType" name="materialType" value="${materialType }">
				</div>
			</li>
			<li>
			<div class="syq_2"><span>供应商</span></div>
			<div class="syq_2">
				<input type="text" class="method" id="supplier" name="supplier" value="${supplier }" >
			</div>
			</li>
			<li>
			<div class="syq_2">
				<span>是否合格供应商</span>
			</div>
			<div >
			 <s:select list="labIfTests" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="qualifiedSupplier"
				  id="qualifiedSupplier"
				  emptyOption="false"
				  labelSeparator=""
				  cssClass="method"
				  ></s:select>
			</div>
			</li>
			<li>
				<div class="syq_2">
					<span>送检日期</span>
				</div>
				<div ><input type="date" isDate=true class="method" name="sendInspectionDate" id="sendInspectionDate"  value="<s:date name="sendInspectionDate" format="yyyy-MM-dd"/>"></div>
			</li>
			<li>
				<div class="syq_2"><span>送样次数</span></div>
				<div >
					<input type="text" class="method" id="sendInspectionTimes" name="sendInspectionTimes" value="${sendInspectionTimes }">
				</div>
			</li>
			<li>
				<div class="syq_2"><span>样品数量</span></div>
				<div >
					<input type="text" class="method" id="materialAmount" name="materialAmount" value="${materialAmount }">
				</div>
			</li>
			<li>
				<div class="syq_2"><span>供应商邮箱</span></div>
				<div >
					<input type="text" class="method" id="supplierEmail" name="supplierEmail" value="${supplierEmail }">
				</div>
			</li>
		</ul>
	</div>
	<div class="problem1">
		<ul>
			<li class="other_li">送样原因</li>
			<li class="problem2">
				<div>送样原因</div>
			</li>
			<li>
				<div >
				<s:checkboxlist  name="sendReason" id="sendReason" value="#request.sendReasonList" theme="simple" list="sendReasons" listKey="value" listValue="name"/>
				</div>
			</li>
			<li class="problem2">
				<div>其他原因</div>
				<div><input type="text" name="otherReason" id="otherReason" class="method"></div>
				</li>
		</ul>
	</div>
	<div class="problem1">
		<ul>
			<li class="other_li">产商样件评估</li>
			<li >
				<div>产商样件评估</div>
				<div >
	             <s:checkboxlist cssStyle="margin-left:8px;" name="materialEvaluate" id="materialEvaluate" value="#request.materialEvaluateList" theme="simple" list="evaluates" listKey="value" listValue="name"/>
			</div>
			</li>
			<li class="problem2">
				<div>其他</div>
				<div><input type="text" name="otherEvaluate" id="otherEvaluate" class="method"></div>
			</li>
			<li class="problem2">
				<div>测试报告编号</div>
				<div><input type="text" name="testReportNum" id="testReportNum" class="method"></div>
			</li>
			<li></li>
			<li class="problem2">
				<div class="upload_c" >
				<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showfileName','fileName');">上传附件</a></span>
				<input type="hidden" name="fileName" id="fileName" value="${fileName}">
				<input type="hidden" name="hisfileName" id="hisfileName" value="${hisfileName}"><br/>
				</div >
				<div class="upload_b"><p class="receive" id="showfileName"></p></div>
			</li>	
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li >
				<div>样品正确性</div>
				<div >
				 <s:select list="labIfTests" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="materialValidity"
				  id="materialValidity"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="method"
				  ></s:select>
				</div>
			</li>
			<li>
				<div>样品使用紧急情况</div>
				<div><s:select list="materialUseinfos" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="materialUseInfo"
				  id="materialUseInfo"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="method"
				  ></s:select></div>
			</li>
			<li>
				<div>填表人</div>
				<div><input type="text" name="creatorName" id="creatorName" value="${creatorName}" disabled="true" class="method"></div>
			</li>
			<li>
				<div>部门</div>
				<div><input type="text" name="dept" id="dept" value="${dept}" class="method"></div>
			</li>
			<li>
			<div class="syq_2"><span>审核</span></div>
			<div class="polling_sell_2">
				<input type="text" id="reportChecker" name="reportChecker" value="${reportChecker }" >
				 <input type="hidden" name="reportCheckerLog" id="reportCheckerLog"  value="${reportCheckerLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('reportChecker','reportCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('reportChecker','reportCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>
			</li>
		</ul>
	</div>
	<div class="problem1">
		<ul>
			<li class="other_li1">
				<p class="Sampleevaluation">样品综合评估项目，标准体术（由工程部、品保部、品质中心提出）</p>
			</li>
			<li class="problem2">
				<div>评估项目</div>
			</li>
			<li>
			<div>
					 <input type="checkbox" name="supplierEvaluateProject" id="supplierEvaluateProject" value="厂商样品规格书参数与所需规格要求的符合性"  <s:if test='%{supplierEvaluateProject=="厂商样品规格书参数与所需规格要求的符合性"}'>checked="true"</s:if> /><label for="supplierEvaluateProject1">厂商样品规格书参数与所需规格要求的符合性</label>
				</div>
			<li class="problem2">
				<div>评估标准</div>
				<div><input id="supplierEvaluateStandar" name="supplierEvaluateStandar" value="	" class="method"/></div>
			</li>
			<li class="problem2">
				<div>OK或NG</div>
				<div>
					<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="supplierEvaluateResult"
				  id="supplierEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				   cssClass="method"
				  ></s:select>
				</div>
			</li>
			<li class="problem2">
				<div>实物样品(评估项目)</div>
			</li>
			<li>
				<div>
					<s:checkboxlist cssStyle="margin-left:8px;" name="sampleEvaluateProject" id="sampleEvaluateProject" value="#request.sampleEvaluateProjectList" theme="simple" list="evaluateProjects" listKey="value" listValue="name"/>
				</div>
			</li>
			<li class="problem2">
				<div>评估标准</div>
				<div><input id="sampleEvaluateStandar" name="sampleEvaluateStandar" value="${sampleEvaluateStandar}" class="method"/></div>
			</li>
			<li class="problem2">
				<div>OK或NG</div>
				<div>
					<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="sampleEvaluateResult"
				  id="sampleEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				   cssClass="method"
				  ></s:select>
				</div>
			</li>
			<li class="problem2">
				<div>RD评估人</div>
				<div class="polling_sell_2">
					<input type="text" id="rdProcesser" name="rdProcesser" value="${rdProcesser }" >
					<input type="hidden" name="rdProcesserLog" id="rdProcesserLog"  value="${rdProcesserLog}" />
					<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('rdProcesser','rdProcesserLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_d" onclick="del('rdProcesser','rdProcesserLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
		</ul>
	</div>

	<div class="problem">
		<ul>
			<li class="other_li1">
				<p class="Sampleevaluation">样品综合评估项目，标准体术（由工程部、品保部、品质中心提出）</p>
			</li>
			<li>
				<div>产品HSF符合性确认</div>
				<div>
				 <s:select list="hsfValues" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="hsfEvaluateProject"
				  id="hsfEvaluateProject"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="method"
				  ></s:select>
				</div>
			</li>
			<li>
				<div>评估标准</div>
				<div><input style="float:left;" id="hsfEvaluateStandar" name="hsfEvaluateStandar"  value="${hsfEvaluateStandar}" class="method"/></div>
			</li>
			<li>
				<div>OK或NG</div>
				<div>
				 <s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="hsfEvaluateResult"
				  id="hsfEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				 cssClass="method"
				  ></s:select>
				</div>
			</li>
			<li>
				<div>QS办理人</div>
				<div class="polling_sell_2">
					<input type="text" id="hsfProcesser" name="hsfProcesser" value="${hsfProcesser }" >
					<input type="hidden" name="hsfProcesserLog" id="hsfProcesserLog"  value="${hsfProcesserLog}" />
					<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('hsfProcesser','hsfProcesserLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_d" onclick="del('hsfProcesser','hsfProcesserLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li1">
				<p class="Sampleevaluation">样品综合评估项目，标准体术（由工程部、品保部、品质中心提出）</p>
			</li>
			<li>
				<div>评估项目</div>
				<div>
					<input type="checkbox" name="msdsEvaluateProject" id="msdsEvaluateProject" value="化学品MSDS"  <s:if test='%{msdsEvaluateProject=="化学品MSDS"}'>checked="true"</s:if> /><label for="ifTrust">化学品MSDS</label>
				</div>
			</li>
			<li>
				<div>评估标准</div>
				<div><input id="msdsEvaluateStandar" name="msdsEvaluateStandar" value="${msdsEvaluateStandar}" class="method"/></div>
			</li>
			<li>
				<div>OK或NG</div>
				<div>
					<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="msdsEvaluateResult"
				  id="msdsEvaluateResult"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="method"
				  ></s:select>
				</div>
			</li>
			<li>
				<div>EHS办理人</div>
				<div class="polling_sell_2">
					<input type="text" id="msdsProcesser" name="msdsProcesser" value="${msdsProcesser }" >
				 <input type="hidden" name="msdsProcesserLog" id="msdsProcesserLog"  value="${msdsProcesserLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('msdsProcesser','msdsProcesserLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('msdsProcesser','msdsProcesserLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li">
				入料检验
			</li>
			<li>
				<div >检验结果:</div>
				<div>
					<s:radio cssClass="method1" name="inspectionResult" id="inspectionResult" value="inspectionResult" theme="simple" list="inspectionResults" listKey="value" listValue="name"/>
				</div>
			</li>
			<li>
				<div>供应商产品出货检验报告</div>
				<div>
					<s:radio cssClass="method1" name="outInspectionResult" id="outInspectionResult" value="outInspectionResult" theme="simple" list="outInspectionResults" listKey="value" listValue="name"/>
				</div>
			</li>
			<li>
				<div>填表人</div>
				<div class="polling_sell_2">
					<input type="text" id="inspectionReporter" name="inspectionReporter" value="${inspectionReporter }" >
				 <input type="hidden" name="inspectionReporterLog" id="inspectionReporterLog"  value="${inspectionReporterLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('inspectionReporter','inspectionReporterLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('inspectionReporter','inspectionReporterLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
			<li>
				<div>SQE审核</div>
				<div class="polling_sell_2">
					<input type="text" id="inspectionChecker" name="inspectionChecker" value="${inspectionChecker }" >
				 <input type="hidden" name="inspectionCheckerLog" id="inspectionCheckerLog"  value="${inspectionCheckerLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('inspectionChecker','inspectionCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('inspectionChecker','inspectionCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
			<li  >
				<div class="upload_c" >检验报告
				<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showinspectionReportFile','inspectionReportFile');">上传附件</a></span>
				<input type="hidden" name="inspectionReportFile" id="inspectionReportFile" value="${inspectionReportFile}">
				<input type="hidden" name="hisinspectionReportFile" id="hisinspectionReportFile" value="${hisinspectionReportFile}"><br/>
			</div>
			<div class="upload_b"><p id="showinspectionReportFile"></p></div>
		</li>	
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li">
				RD
			</li>
			<li>
				<div>是否试做</div>
				<div>
					<s:radio cssClass="method1" name="rdIfTest" id="rdIfTest" value="rdIfTest" theme="simple" list="ifTests" listKey="value" listValue="name"/>
				</div>
			</li>
			<li>
				<div>建议与评价</div>
				<div>
					<input type="text" name="rdOpinion" id="rdOpinion" class="method">
				</div>
			</li>
			<li>
				<div>填表人</div>
				<div class="polling_sell_2">
					<input readonly="readonly" name="rdReporter" disabled="true" value="${rdProcesser}" />
				</div>
			</li>
			<li  >
			<div class="upload_c" >RD报告
			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showrdReportFile','rdReportFile');">上传附件</a></span>
			<input type="hidden" name="rdReportFile" id="rdReportFile" value="${rdReportFile}">
			<input type="hidden" name="hisrdReportFile" id="hisrdReportFile" value="${hisrdReportFile}"><br/>
			
			</div>
			<div class="upload_b"><p id="showrdReportFile"></p></div>
		</li>	
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li">
				QS
			</li>
			<li>
				<div>建议与评价</div>
				<div>
					<input type="text" name="qsOpinion" id="qsOpinion" class="method">
				</div>
			</li>
			<li>
				<div>填表人</div>
				<div class="polling_sell_2">
				<input readonly="readonly" disabled="true" value="${hsfProcesser}" />
				</div>
			</li>
			<li >
			<div class="upload_c" >QS报告
			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showqsReportFile','qsReportFile');">上传附件</a></span>
			<input type="hidden" name="qsReportFile" id="qsReportFile" value="${qsReportFile}">
			<input type="hidden" name="hisqsReportFile" id="hisqsReportFile" value="${hisqsReportFile}"><br/>
			
			</div>
			<div class="upload_b"><p id="showqsReportFile"></p></div>
		</li>	
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li class="other_li">
				LAB
			</li>
			<li>
				<div>是否试验</div>
				<div>
					<s:radio cssClass="method1" name="labIfTest" id="labIfTest" value="labIfTest" theme="simple" list="labIfTests" listKey="value" listValue="name"/>
				</div>
			</li>
			<li>
				<div>实验内容</div>
				<div>
					<s:radio cssClass="method1" name="labTestContent" id="labTestContent" value="labTestContent" theme="simple" list="testContents" listKey="value" listValue="name"/>
				</div>
			</li>
			<li>
				<div>结果</div>
				<div>
					<s:radio cssClass="method1" name="labTestResult" id="labTestResult" value="labTestResult" theme="simple" list="labTestResults" listKey="value" listValue="name"/>
				</div>
			</li>
			<li>
				<div>报告编号</div>
				<div>
					<input type="text" name="labTestNum" id="labTestNum" class="method">
				</div>
			</li>
			<li>
				<div>填表人</div>
				<div class="polling_sell_2">
					<input type="text" id="labReporter" name="labReporter" value="${labReporter }" >
				 <input type="hidden" name="departmentLeaderLogin" id="departmentLeaderLogin"  value="${labReporterLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('labReporter','labReporterLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('labReporter','labReporterLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
			<li>
			<div class="upload_c" >LAB报告
			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showlabReportFile','labReportFile');">上传附件</a></span>
			<input type="hidden" name="labReportFile" id="labReportFile" value="${labReportFile}">
			<input type="hidden" name="hislabReportFile" id="hislabReportFile" value="${hislabReportFile}"><br/>
			</div>
			<div class="upload_b"><p id="showlabReportFile"></p></div>
		</li>	
		</ul>
	</div>
	<div class="problem1">
		<ul>
			<li class="other_li">
				综合评估
			</li>
			<li>
			<div >
				 <input type="radio" id="evaluateResult1" name="evaluateResult" value="合格" <s:if test="evaluateResult=='合格'">checked="true"</s:if> title="合格"/><label for="evaluateResult1">合格</label>
           		 (<s:radio cssStyle="margin-left:8px;" name="evaluateOkInfo" id="evaluateOkInfo" value="evaluateOkInfo" theme="simple" list="okInfos" listKey="value" listValue="name"/>)
			</div>
			</li>
			<li>
			<div >
				<input  type="radio" id="evaluateResult2" name="evaluateResult" value="不合格" <s:if test="evaluateResult=='不合格'">checked="true"</s:if> title="不合格"/><label for="evaluateResult2">不合格</label>
				(<s:radio cssStyle="margin-left:8px;" name="evaluateNgInfo" id="evaluateNgInfo" value="evaluateNgInfo" theme="simple" list="ngInfos" listKey="value" listValue="name"/>)
				</div>
			</li>
			<li class="problem2">
				<div >填表人</div>
				<div class="polling_sell_2" >
					<input type="text" id="evaluateReporter" name="evaluateReporter" value="${evaluateReporter }" >
				 <input type="hidden" name="evaluateReporterLog" id="evaluateReporterLog"  value="${evaluateReporterLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('evaluateReporter','evaluateReporterLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('evaluateReporter','evaluateReporterLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
			<li class="problem2">
				<div>审核</div>
				<div class="polling_sell_2">
					<input type="text" id="evaluateChecker" name="evaluateChecker" value="${evaluateChecker }" >
				 <input type="hidden" name="evaluateCheckerLog" id="evaluateCheckerLog"  value="${evaluateCheckerLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('evaluateChecker','evaluateCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('evaluateChecker','evaluateCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
		</ul>
	</div>
	<div class="problem">
		<ul>
			<li >
				<div>是否送事业部长审核</div>
				<div>
					<s:radio cssClass="method1"
					name="ifToManager" 
					id="ifToManager" 
					value="ifToManager" 
					theme="simple" 
					list="labIfTests" 
					onclick="showManager(this);"
					listKey="value" 
					listValue="name"/>
				</div>
			</li>
		</ul>
	</div>
	<div class="problem" style="display:none" id="managerId">
		<ul>
			<li class="problem2">
				<div>部长</div>
				<div class="polling_sell_2">
				     <input type="text" id="managerName" name="managerName" value="${managerName }" >
				 <input type="hidden" name="managerLoginName" id="managerLoginName"  value="${managerLoginName}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('managerName','managerLoginName')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('managerName','managerLoginName')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
				</div>
			</li>
		</ul>
	</div>
 </form>
 <div class="endding" >
 <div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
	 <s:if test="taskId>0">
		<div class="give">
			<a class="polling_fdj" onclick="searchPerson('copyPersonUser','copyPerson');">抄送</a>
			<input type="text" name="copyPersonUser"  placeholder="选择抄送人" id="copyPersonUser">
			<input type="hidden" name="copyPerson"   id="copyPerson">
			<i onclick="del('copyPersonUser','copyPerson')"></i>
		</div>
		<div class="give">
			<a class="polling_fdj" onclick="searchPerson('assigneeUser','assignee');">指派</a>
			<input type="text" name="assigneeUser" value="" placeholder="选择指派人" id="assigneeUser">
			<input type="hidden" name="assignee" value=""  id="assignee">
			<i onclick="del('assigneeUser','assignee')"></i>
		</div>
	</s:if>
	 <div class="buttos">
	<s:if test="taskId>0">
	        <a onclick="showProcessForm();">查看意见</a>
        <s:if test="isComplete==false">
			   <s:if test="task.active==0">
				 <a onclick="saveForm();">保存</a>
				<s:if test="task.processingMode.name()==\"TYPE_EDIT\"">
					<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
				</s:if>
				<s:elseif test="task.processingMode.name()==\"TYPE_APPROVAL\"">
					  <a onclick="_completeTask('APPROVE');">同意</a><a onclick="_completeTask('REFUSE');">不同意</a>
				</s:elseif>
	    </s:if>
	</s:if>
	</s:if>
	<s:else>
	   <a onclick="showProcessForm();">查看意见</a>
	   <s:if test="isComplete==false">
			<a onclick="saveForm();">保存</a>
			<s:if test="taskId>0">
			<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
			</s:if>
			<s:else><a id="sub_tj" onclick="submitForm();">提交</a></s:else>
		</s:if>
	</s:else>
	</div>
	<div class="opinion">意见</div>
		<textarea id="opinionStr" onchange="setOpinionValue(this);" style="height: 3rem;  width: 90%;margin-left: 5%;" name="opinionStr" class="writetextarea"></textarea>
 </div>
 
 <div id="zzc" style="display:none"></div>
 <div id="nk" style="display:none">
	<div id="ym"><p><span>用户树</span><a id="closeId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div id="navigation"><button type="button" onclick="setInputValue();">确定</button><input type="text" name="searchTag" id="searchTag"><a href="javascript:;" onclick="searchElement()"><img src="${ctx}/mobile/img/fdj.png"></a></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
		<ul id="root" style="margin:0 6px;">
			<li>
				<label><a href="javascript:;" >欧菲光-CCM</a></label>
				${userTreeHtml}
			</li>
		</ul>
	</div>
 </div>
  <div id="file" style="display:none">
  <div id="ym"><p><span>上传附件</span><a id="filecloseId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
  <div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
	<form id="form1" enctype="multipart/form-data" method="post" action="${carmfgctx}/common/upload.htm">
	  <ul id="root" style="margin:0 6px;">
	      <li style="margin-left:20px;">
				 <div class="row">
				      <label for="fileToUpload">选择文件</label><br/>
				      <input type="file" name="uploadFile" id="uploadFile" onchange="fileSelected();"/>
				 </div>
			 </li>
			 <li style="margin-left:5px;">
			    <div id="fileNameDiv"><input style="width:150px;" name="uploadFileName" readonly=readonly id="uploadFileName"/></div>
			 </li>
			 <li style="margin-left:20px;">
				<div class="row">
				  <input type="button" onclick="uploadFiles()" value="Upload" />
		       </div>
	       </li>
			 <li style="margin-left:20px;">
			   <div id="fileSize"></div>
			</li>
			<li style="margin-left:20px;">
			   <div id="fileType"></div>
			</li>
			
	       <li style="margin-left:20px;">
	          <div id="progressNumber"></div>
	       </li>
      </ul>
   </form>
   </div>
 </div>
 <script>
		function fillcontent(id){
			$('#'+id).slideToggle();
		}
   </script>
 <script type="text/javascript" >
	function addEvent(el,name,fn){
		if(el.addEventListener) return el.addEventListener(name,fn,false);
		return el.attachEvent('on'+name,fn);
	}
	
	function nextnode(node){
		if(!node)return ;
		if(node.nodeType == 1)
			return node;
		if(node.nextSibling)
			return nextnode(node.nextSibling);
	} 
	function prevnode(node){
		if(!node)return ;
		if(node.nodeType == 1)
			return node;
		if(node.previousSibling)
			return prevnode(node.previousSibling);
	} 
	function parcheck(self,checked){
		var par =  prevnode(self.parentNode.parentNode.parentNode.previousSibling),parspar;
		if(par&&par.getElementsByTagName('input')[0]){
			par.getElementsByTagName('input')[0].checked = checked;
			parcheck(par.getElementsByTagName('input')[0],sibcheck(par.getElementsByTagName('input')[0]));
		}			
	}
	function sibcheck(self){
		var sbi = self.parentNode.parentNode.parentNode.childNodes,n=0;
		for(var i=0;i<sbi.length;i++){
			if(sbi[i].nodeType != 1)
				n++;
			else if(sbi[i].getElementsByTagName('input')[0].checked)
				n++;
		}
		return n==sbi.length?true:false;
	}
	addEvent(document.getElementById('root'),'click',function(e){
		e = e||window.event;
		var target = e.target||e.srcElement;
		var tp = nextnode(target.parentNode.nextSibling);
		switch(target.nodeName){
			case 'A':
				if(tp&&tp.nodeName == 'UL'){
					if(tp.style.display != 'block' ){
						tp.style.display = 'block';
						prevnode(target.parentNode.previousSibling).className = 'ren'
					}else{
						tp.style.display = 'none';
						prevnode(target.parentNode.previousSibling).className = 'add'
					}	
				}
				break;
			case 'SPAN':
				var ap = nextnode(nextnode(target.nextSibling).nextSibling);
				if(ap.style.display != 'block' ){
					ap.style.display = 'block';
					target.className = 'ren'
				}else{
					ap.style.display = 'none';
					target.className = 'add'
				}
				break;
			case 'INPUT':
				if(target.checked){
					if(tp){
						var checkbox = tp.getElementsByTagName('input');
						for(var i=0;i<checkbox.length;i++)
							checkbox[i].checked = true;
					} 
				}else{
					if(tp){
						var checkbox = tp.getElementsByTagName('input');
						for(var i=0;i<checkbox.length;i++)
							checkbox[i].checked = false;
					}
				}
				parcheck(target,sibcheck(target));
				break;
		}
	});
	window.onload = function(){
		var labels = document.getElementById('root').getElementsByTagName('label');
		for(var i=0;i<labels.length;i++){
			var span = document.createElement('span');
			span.style.cssText ='display:inline-block;height:24px;vertical-align:middle;width:21px;cursor:pointer;';
			span.innerHTML = ' ';
			span.className = 'add';
			if(nextnode(labels[i].nextSibling)&&nextnode(labels[i].nextSibling).nodeName == 'UL')
				labels[i].parentNode.insertBefore(span,labels[i]);
			else
				labels[i].className = 'rem';
		}
	}
</script>

 <script>
	function del(id){
		document.getElementById(id).value="";
	} 
 </script>

<script>
 

 $(function(){
	$("#closeId").click(function(){
		 $("#nk").hide();
		 $("#zzc").hide();
	});
	$("#filecloseId").click(function(){
		 $("#file").hide();
		 $("#uploadFile").val("");
		 document.getElementById('uploadFileName').value =  '';
         document.getElementById('fileSize').innerHTML = '';
         document.getElementById('fileType').innerHTML = '';
         document.getElementById('progressNumber').innerHTML = '';
	});
// 	$(".polling_fdj").click(function(){
// 		 $("#nk").show();
// 		 $("#zzc").show();
// 	});
 
 });
</script>

</body>
</html>
