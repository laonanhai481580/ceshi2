<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>供应商评价表</title>
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
	<a><span class="cause_0"><%-- <img src="${ctx}/mobile/img/comeback.png"> --%></span></a><span class="cause_1">供应商评价表</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
 <input type="hidden" name="id" id="id" value="${id}"  />
 <input type="hidden" name="opinion" id="opinion">
 <input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
 <div class="syq">
	<ul class="syq_1">
		<li>
			<div class="syq_2">
				<span>事业群</span>
			</div>
			<div><input type="text" class="partOne" name="businessUnitName" id="businessUnitName" value="${businessUnitName }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>供应商分类</span>
			</div>
			<div>
<!-- 			 <form action="" id="gys_fl_1"> -->
					<s:select list="supplierTypes" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  name="supplierType"
					  id="supplierType"
					  emptyOption="false"
					  labelSeparator=""
					  cssClass="gys_fl_2"
					  ></s:select>
<!-- 			</form> -->
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>紧急程度</span>
			</div>
			<div>
<!-- 				<form action="" id="gys_fl_1"> -->
					<s:select list="urgencyLevels" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  name="urgencyLevel"
					  id="urgencyLevel"
					  emptyOption="false"
					  labelSeparator=""
					   cssClass="gys_fl_2"
					  ></s:select>
<!-- 				</form> -->
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>制单人</span>
			</div>
			<div>
				<input type="text" class="partOne" name="formCreator" id="formCreator" value="${formCreator}">
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>制单日期</span>
			</div>
			<div><input type="date" isDate=true class="partOne" name="formCreateDate" id="formCreateDate" value="<s:date name="formCreateDate" format="yyyy-MM-dd"/>"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>表单编号</span>
			</div>
			<div class="formNoDiv" style='font-size:0.7rem!important'><span class="formNoText">${formNo }</span></div>
		</li>
		<li>
			<div class="syq_2">
				<span>部门</span>
			</div>
			<div><input type="text" class="partOne" id="department" name="department" value="${department }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>公司</span>
			</div>
			<div><input type="text" class="partOne" id="company" name="company" value="${company }"></div>
		</li>
		<li id="ku_user_6">
			<div class="syq_2">
				<span>评价日期</span>
			</div>
			<div><input type="date"  class="partOne" id="evaluateDate" name="evaluateDate" value="<s:date name="formCreateDate" format="yyyy-MM-dd"/>"></div>
		</li>
	</ul>
 </div>
 <div class="gys_mc">
	<ul class="gys_mc_a">
		<li>
			<div class="syq_2"><span>供应商名称</span></div>
			<div><input type="text" class="partTwo" id="supplierName"  name="supplierName" class="{required:true,messages:{required:'必填!'}}" value="${supplierName }"></div>
		</li>
		<li>
			<div class="syq_2"><span>供应商编号</span></div>
			<div><input type="text" class="partTwo" id="supplierCode"  name="supplierCode" value="${supplierCode }"></div>
		</li>
		<li>
			<div class="syq_2"><span>提供产品/服务范围</span></div>
			<div><input type="text" class="partTwo"  name="supplierServices" id="supplierServices" value="${supplierServices}"></div>
		</li>
		<li>
			<div class="syq_2"><span>公司地址</span></div>
			<div><input type="text" class="partTwo"  name="supplierCompanyAddress" id="supplierCompanyAddress" value="${supplierCompanyAddress}"></div>
		</li>
		<li>
			<div class="syq_2"><span>营业执照号码</span></div>
			<div><input type="text" class="partTwo"  id="businessLicenseCode" name="businessLicenseCode" value="${businessLicenseCode }"></div>
		</li>
		<li>
			<div class="syq_2"><span>法人代表</span></div>
			<div><input type="text" class="partTwo"  id="legalRepresentative" name="legalRepresentative" value="${legalRepresentative }"></div>
		</li>
		<li>
			<div class="syq_2"><span>业务负责人/电话</span></div>
			<div><input type="text" class="partTwo"  id="supportTelephone" name="supportTelephone" value="${supportTelephone }"></div>
		</li>
		<li>
			<div class="syq_2"><span>品质负责人/电话</span></div>
			<div><input type="text" class="partTwo"   id="qualitySupportTelephone" name="qualitySupportTelephone" value="${qualitySupportTelephone }"></div>
		</li>
		<li>
			<div class="syq_2"><span>公司电话</span></div>
			<div><input type="text" class="partTwo"  id="companyTelephone" name="companyTelephone" value="${companyTelephone }"></div>
		</li>
		<li>
			<div class="syq_2"><span>传真</span></div>
			<div><input type="text" class="partTwo"  id="companyFax" name="companyFax" value="${companyFax }"></div>
		</li>
		<li>
			<div class="syq_2"><span>备注</span></div>
			<div><input type="text" class="partTwo"  name="remark" id="remark" value="${remark }"></div>
		</li>
	</ul>
 </div>
 <div class="upload">
	<ul class="upload_a">
		<li>
			<div class="syq_2"><span>基本信息</span></div>
			<div class="upload_b">
<%-- 			<span class="upload_span"><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showbaseInfoFile','baseInfoFile');">上传附件</a></span> --%>
			<input type="hidden" name="baseInfoFile" id="baseInfoFile" value="${baseInfoFile}">
			<input type="hidden" name="hisbaseInfoFile" id="hisbaseInfoFile" value="${baseInfoFile}">
			<p class="receive" id="showbaseInfoFile"></p>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>三证</span></div>
			<div class="upload_b" >
<%-- 			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showthreePapersFile','threePapersFile');">上传附件</a></span> --%>
			<input type="hidden" name="threePapersFile" id="threePapersFile" value="${threePapersFile}">
			<input type="hidden" name="histhreePapersFile" id="histhreePapersFile" value="${histhreePapersFile}">
			<p id="showthreePapersFile"></p>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>廉洁协议</span></div>
			<div class="upload_b">
<%-- 			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showintegrityAgreementFile','integrityAgreementFile');">上传附件</a></span> --%>
			<input type="hidden" name="integrityAgreementFile" id="integrityAgreementFile" value="${integrityAgreementFile}">
			<input type="hidden" name="hisintegrityAgreementFile" id="hisintegrityAgreementFile" value="${integrityAgreementFile}"><br/>
			<p id="showintegrityAgreementFile"></p>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>样品评估表</span></div>
			<div class="upload_b">
<%-- 			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showsampleEvaluateFile','sampleEvaluateFile');">上传附件</a></span> --%>
			<input type="hidden" name="sampleEvaluateFile" id="sampleEvaluateFile" value="${sampleEvaluateFile}">
			<input type="hidden" name="hissampleEvaluateFile" id="hissampleEvaluateFile" value="${sampleEvaluateFile}">
			<p id="showsampleEvaluateFile"></p>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>审厂资料</span></div>
			<div class="upload_b">
<%-- 			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showfactoryAuditFile','factoryAuditFile');">上传附件</a></span> --%>
			<input type="hidden" name="factoryAuditFile" id="factoryAuditFile" value="${factoryAuditFile}">
			<input type="hidden" name="hisfactoryAuditFile" id="hisfactoryAuditFile" value="${factoryAuditFile}">
			<p id="showfactoryAuditFile"></p>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>采购框架协议</span></div>
			<div class="upload_b">
<%-- 			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showpurchasingFile','purchasingFile');">上传附件</a></span> --%>
			<input type="hidden" name="purchasingFile" id="purchasingFile" value="${purchasingFile}">
			<input type="hidden" name="hispurchasingFile" id="hispurchasingFile" value="${purchasingFile}">
			<p id="showpurchasingFile"></p>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>供应商竞争力分析表</span></div>
			<div class="upload_b">
<%-- 			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="fileClick"  onclick="_uploadFiles('showsupplierAnalyzeFile','supplierAnalyzeFile');">上传附件</a></span> --%>
			<input type="hidden" name="supplierAnalyzeFile" id="supplierAnalyzeFile" value="${supplierAnalyzeFile}">
			<input type="hidden" name="hissupplierAnalyzeFile" id="hissupplierAnalyzeFile" value="${supplierAnalyzeFile}">
			<p id="showsupplierAnalyzeFile"></p>
			</div>
		</li>
	</ul>
 </div>
 <div class="polling">
	<ul class="polling_sell">
		<li>
			<div class="polling_sell_1"><p><span>采购中心审核</span></p></div>
			<div class="polling_sell_2">
				<input type="text" name="purchaseChecker" id="purchaseChecker" value="${purchaseChecker }">
				<input type="hidden" name="purchaseCheckerLog" id="purchaseCheckerLog" value="${purchaseCheckerLog }">
				<a id="polling_fdj" class="polling_fdj"  onclick="searchPerson('purchaseChecker','purchaseCheckerLog')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('purchaseChecker','purchaseCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购中心评价</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="purchaseEvaluate" name="purchaseEvaluate" value="${purchaseEvaluate }">
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购中心部门长</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="bossPurchase" name="bossPurchase" value="${bossPurchase }">
				 <input type="hidden" name="bossPurchaseLog" id="bossPurchaseLog"  value="${bossPurchaseLog}" />
				<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('bossPurchase','bossPurchaseLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_a" onclick="del('bossPurchase','bossPurchaseLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>工程研发部评价人</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="developChecker" name="developChecker" value="${developChecker }" >
				<input type="hidden" name="developCheckerLog" id="developCheckerLog"  value="${developCheckerLog}" />
				<a id="polling_fdj_2" class="polling_fdj" onclick="searchPerson('developChecker','developCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
				<%-- 	<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_b" onclick="del('developChecker','developCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>工程研发部审核方式</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="developAuditType" name="developAuditType" value="${developAuditType }">
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>工程研发部审核等级</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="developAuditLevel" name="developAuditLevel" value="${developAuditLevel }">
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>工程研发部部门长</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="bossDevelopChecker" name="bossDevelopChecker" value="${bossDevelopChecker }" >
				<input type="hidden" name="bossDevelopCheckerLog" id="bossDevelopCheckerLog"  value="${bossDevelopCheckerLog}" />
				<a id="polling_fdj_3" class="polling_fdj" onclick="searchPerson('bossDevelopChecker','bossDevelopCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_c" onclick="del('bossDevelopChecker','bossDevelopCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>QPA分数</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="developQpaScore" name="developQpaScore" value="${developQpaScore }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>QSA分数</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="developQsaScore" name="developQsaScore" value="${developQsaScore }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心QS评价人</span></p></div>
			<div class="polling_sell_2">
				<input type="text"  id="qualityQsChecker" name="qualityQsChecker" value="${qualityQsChecker }" >
				<input type="hidden" name="qualityQsCheckerLog" id="qualityQsCheckerLog"  value="${qualityQsCheckerLog}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('qualityQsChecker','qualityQsCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('qualityQsChecker','qualityQsCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心QS审核方式</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="qualityQsAuditType" name="qualityQsAuditType" value="${qualityQsAuditType }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心QS审核等级</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="qualityQsAuditLevel" name="qualityQsAuditLevel" value="${qualityQsAuditLevel }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心QS部门长审核</span></p></div>
			<div class="polling_sell_2">
				<input type="text"  id="bossQsQuality" name="bossQsQuality" value="${bossQsQuality }" >
				<input type="hidden" name="bossQsQualityLog" id="bossQsQualityLog"  value="${bossQsQualityLog}" />
				<a id="polling_fdj_5" class="polling_fdj" onclick="searchPerson('bossQsQuality','bossQsQualityLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_e" onclick="del('bossQsQuality','bossQsQualityLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>CSR分数</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="csrScore" name="csrScore" value="${csrScore }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心SQE评价人</span></p></div>
			<div class="polling_sell_2"> 
				<input type="text" id="qualitySqeChecker" name="qualitySqeChecker" value="${qualitySqeChecker }" >
				<input type="hidden" name="qualitySqeCheckerLog" id="qualitySqeCheckerLog"  value="${qualitySqeCheckerLog}" />
				<a id="polling_fdj_6" class="polling_fdj" onclick="searchPerson('qualitySqeChecker','qualitySqeCheckerLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_f" onclick="del('qualitySqeChecker','qualitySqeCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心SQE审核方式</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="qualitySqeAuditType" name="qualitySqeAuditType" value="${qualitySqeAuditType }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心SQE审核等级</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="qualitySqeAuditLevel" name="qualitySqeAuditLevel" value="${qualitySqeAuditLevel }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心SQE部门长审核</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="bossSqeQuality" name="bossSqeQuality" value="${bossSqeQuality }" />
				<input type="hidden" name="bossSqeQualityLog" id="bossSqeQualityLog"  value="${bossSqeQualityLog}" />
				<a id="polling_fdj_7" class="polling_fdj" onclick="searchPerson('bossSqeQuality','bossSqeQualityLog')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				
				<a id="polling_ljt_g" onclick="del('bossSqeQuality','bossSqeQualityLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>QPA分数</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="qualityQpaScore" name="qualityQpaScore" value="${qualityQpaScore }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购管理部评价人</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="purchaseManageChecker" name="purchaseManageChecker" value="${purchaseManageChecker }" />
				<input type="hidden" name="purManagerCheckerLog" id="purManagerCheckerLog"  value="${purManagerCheckerLog}" />
				<a id="polling_fdj_8" class="polling_fdj" onclick="searchPerson('purchaseManageChecker','purManagerCheckerLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_h" onclick="del('purchaseManageChecker','purManagerCheckerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购管理部审核方式</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="purchaseManageType" name="purchaseManageType" value="${purchaseManageType }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购管理部审核等级</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="purchaseManageLevel" name="purchaseManageLevel" value="${purchaseManageLevel }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购管理部（经理或以上）</span></p></div>
			<div class="polling_sell_2">
				<input type="text"  id="purchaseManageManager" name="purchaseManageManager" value="${purchaseManageManager }"/>
				<input type="hidden" name="purManagerLog" id="purManagerLog"  value="${purManagerLog}" />
				<a id="polling_fdj_9" class="polling_fdj"  onclick="searchPerson('purchaseManageManager','purManagerLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_i" onclick="del('purchaseManageManager','purManagerLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>QSA分数</span></p></div>
			<div class="polling_sell_3">
				<input type="text" id="purchaseQsaScore" name="purchaseQsaScore" value="${purchaseQsaScore }">
				
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>品质中心部门长审核</span></p></div>
			<div class="polling_sell_2">
				<input type="text"  id="bossQuality" name="bossQuality" value="${bossQuality }" >
					              <input type="hidden" name="bossQualityLog" id="bossQualityLog"  value="${bossQualityLog}" />
				<a id="polling_fdj_10" class="polling_fdj" onclick="searchPerson('bossQuality','bossQualityLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_j" onclick="del('bossQuality','bossQualityLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>采购管理部部门长</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="bossPurchaseManage" name="bossPurchaseManage" value="${bossPurchaseManage }">
					             <input type="hidden" name="bossPurchaseManageLog" id="bossPurchaseManageLog"  value="${bossPurchaseManageLog}" />
				<a id="polling_fdj_11" class="polling_fdj"  onclick="searchPerson('bossPurchaseManage','bossPurchaseManageLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_k" onclick="del('bossPurchaseManage','bossPurchaseManageLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>集团COO</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="bossCoo" name="bossCoo" value="${bossCoo }" >
				<input type="hidden" name="bossCooLog" id="bossCooLog"  value="${bossCooLog}" />
				<a id="polling_fdj_12" class="polling_fdj"  onclick="searchPerson('bossCoo','bossCooLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_l" onclick="del('bossCoo','bossCooLog')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>集团总经理</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="manager" name="manager" value="${manager }" >
				  <input type="hidden" name="managerLog" id="managerLog"  value="${managerLog}" />
				<a id="polling_fdj_13" class="polling_fdj" onclick="searchPerson('manager','managerLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_m" onclick="del('manager','managerLog')"> 
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>集团高级副总裁</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="superManager" name="superManager" value="${superManager }" >
				<input type="hidden" name="superManagerLog" id="superManagerLog"  value="${superManagerLog}" />
				<a id="polling_fdj_14" class="polling_fdj" onclick="searchPerson('superManager','superManagerLog');">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt_n">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>
		</li>
	</ul>
 </div>
 </form>
 <div class="endding" >
 <s:if test="isComplete!=true">
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
	<s:else>
	   <a onclick="showProcessForm();">查看意见</a>
		<a onclick="saveForm();">保存</a>
		<s:if test="taskId>0">
		<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
		</s:if>
		<s:else><a id="sub_tj" onclick="submitForm();">提交</a></s:else>
	</s:else>
	</div>
	<div class="opinion">意见</div>
		<textarea id="opinionStr" onchange="setOpinionValue(this);" style="height: 3rem;  width: 90%;margin-left: 5%;" name="opinionStr" class="writetextarea"></textarea>
 </div>
 <div style="height:11rem;width: 100%"></div>
 </s:if>
<%--  <div class="endding" >
	<ul class="gg">
		<li>
			<div class="endding_a">
				<form name="tj_text" action="" id="endding_1">
					<input type="button" name="submit" onclick="submitForm();" src="${ctx}/mobile/img/tj2.png">
                    <div>
						<s:if test="taskId>0">
							<s:if test="task.active==0">
								<button type="button" class="lgbs_btn lg_btn" onclick="saveForm();">保存</button>
								<s:if test="task.processingMode.name()==\"TYPE_EDIT\"">
									<button type="button" class="lgbs_btn lg_btn" onclick="_completeTask('SUBMIT');">提交</button>
								</s:if>
								<s:elseif test="task.processingMode.name()==\"TYPE_APPROVAL\"">
									<button type="button" class="lgbs_btn lg_btn" onclick="_completeTask('APPROVE');">同意</button>
									<button type="button" class="lgbs_btn lg_btn" onclick="_completeTask('OPPOSE');">不同意</button>
								</s:elseif>
							</s:if>
						</s:if>
						<s:else>
							<button type="button" class="lgbs_btn lg_btn" onclick="saveForm();">保存</button>
							<button type="button" class="lgbs_btn lg_btn" onclick="submitForm();">提交</button>
						</s:else>
					</div>
				</form>
			</div>
			<span>提交</span>
		</li>
		<li>
			<div><a id="endding_2"><img src="${ctx}/mobile/img/zf.png"></a></div>
			<span>转发</span>
		</li>
	</ul>
 </div> --%>
 <div id="zzc" style="display:none"></div>
 <div id="nk" style="display:none">
	<div id="ym"><p><span>用户树</span><a id="closeId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div id="navigation"><button type="button" onclick="setInputValue();">确定</button><input type="text" name="searchTag" id="searchTag"><a href="javascript:;" onclick="searchElement()"><img src="${ctx}/mobile/img/fdj.png"></a></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
		<ul id="root" style="margin:0 6px;">
			<li>
				<label><a href="javascript:;" >欧菲光-CCM</a></label>
			    <!-- <ul class="two">
					<li style="margin-left:20px;">
						<label><a href="javascript:;" >开发部</a></label>
						 <ul class="two">
							<li style="margin-left:20px;">
								<label><a href="javascript:;" >前端</a></label>
								<ul class="two" style="margin-left:30px;">
									<li><label><input type="checkbox" value="123456"><a  href="javascript:;" >美工</a></label></li>
									<li><label><input type="checkbox" value="123456"><a href="javascript:;" >页面</a></label></li>
								</ul>
							</li>
							<li style="margin-left:20px;">
								<label><a href="javascript:;">后台</a></label>
								<ul class="two" style="margin-left:30px;">
									<li><label><input type="checkbox" value="123456"><a href="javascript:;">平台</a></label></li>
									<li><label><input type="checkbox" value="123456"><a href="javascript:;" >产品</a></label></li>
								</ul>
							</li>
				        </ul>
						
						
						<ul class="two" style="margin-left:30px;">
							<li><label><input type="checkbox" value="123456"><a  href="javascript:;" >CCM高兴园区</a></label></li>
							<li><label><input type="checkbox" value="123456"><a href="javascript:;" >CCM下罗园区</a></label></li>
						</ul>
					</li>
					<li style="margin-left:20px;">
						<label><a href="javascript:;">供应商</a></label>
						<ul class="two" style="margin-left:30px;">
							<li><label><input type="checkbox" value="123456"><a href="javascript:;">思迅</a></label></li>
							<li><label><input type="checkbox" value="123456"><a href="javascript:;" >绿草</a></label></li>
						</ul>
					</li>
				</ul> -->
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
				  <input type="button" onclick="uploadFiles()" value="上传" />
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
 <div id="processForm" style="display:none"></div>
 <div id="processFormNk" style="display:none">
 <div id="ym"><p><span>查看意见</span><a id="processFormId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
	   <%
		List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
		if(opinionParameters==null){
			opinionParameters = new ArrayList<Opinion>();
		}
	%>
	<table class="form-table-border-left" id="history-table" style="width:100%;margin: auto;">
		<tr height=28>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;text-align: center">
				办理人
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;text-align: center">
				意见
			</td>
		</tr>
		<%
			int index = 1;
			for(Opinion param : opinionParameters){
		%>
			<tr height=24>
				<td>
					<%=param.getTransactorName() %>
				</td>
				<td>
					<%=param.getOpinion()==null?"":param.getOpinion() %>
				</td>
			</tr>
		<%} %>
	</table>
	</div>
 </div>
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
	$("#processFormId").click(function(){
		 $("#processForm").hide();
		 $("#processFormNk").hide();
	});
// 	$(".polling_fdj").click(function(){
// 		 $("#nk").show();
// 		 $("#zzc").show();
// 	});
 
 });
</script>

</body>
</html>
