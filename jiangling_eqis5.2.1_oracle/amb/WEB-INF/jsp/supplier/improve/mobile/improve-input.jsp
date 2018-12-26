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
<title>进料异常矫正措施联络单</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<link rel="shortcut icon">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${ctx}/mobile/css/sm.css">
<link rel="stylesheet" href="${ctx}/mobile/css/swiper.min.css">
<link rel="stylesheet" href="${ctx}/mobile/css/style.css">
<script type="text/javascript" src="${resourcesCtx}/js/jquery-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/date.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/iscroll.js"></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
<jsp:include page="script.jsp"/>
<%@ include file="style.jsp"%>
 </head>
 <body>
	<nav class="nav_head">
		<%-- <a><img src="${ctx}/mobile/img/comeback.png"></a> --%>
		<span>进料异常矫正措施联络单</span>
	</nav>
 <form id="applicationForm" name="applicationForm" method="post">
<input type="hidden" name="id" id="id" value="${id}"  />
<input type="hidden" name="opinion" id="opinion">
<input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
	<table class="all_tab">
		<tbody>
			<tr>
				<th style="text-align: right;">编号:${formNo}</th>
			</tr>
			<tr>
				<th style="background-color:#b940fb;text-align:center;">开单填写</th>
			</tr>
			<tr>
				<td class="td_left">
					<span>发生地点</span>
				</td>
				<td class="td_right">
					<s:select list="happenSpaces"
						listKey="value" 
						listValue="value" 
						name="happenSpace" 
						id="happenSpace" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>					
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>产品阶段</span>
				</td>
				<td class="td_right">
					<!-- <a><input type="text" name="productStage" id="productStage" class="text-inp"></a> -->
			         <s:select list="productStages" 
						  theme="simple"
						  listKey="name" 
						  listValue="name" 
						  name="productStage"
						  id="productStage"
						  emptyOption="true"
						  labelSeparator=""
						  cssClass="text-inp"
						  ></s:select>					
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>事业部</span>
				</td>
				<td class="td_right">
					<a><input type="text" name="businessUnitName" id="businessUnitName" class="text-inp" value="${businessUnitName}"></a>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>开单区域</span>
				</td>
				<td class="td_right">
					<a><input type="text" name="billingArea" id="billingArea" class="text-inp" value="${billingArea}"></a>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>供应商</span>
				</td>
				<td class="td_right">
					<a><input type="text" name="supplierName" id="supplierName" class="text-inp" value="${supplierName}"></a>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>品名</span>
				</td>
				<td class="td_right">
					<input type="text" name="bomName" id="bomName" class="text-inp" value="${bomName}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>料号</span>
				</td>
				<td class="td_right">
					<input type="text" name="bomCode" id="bomCode" class="text-inp" value="${bomCode}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>检验日期</span>
				</td>
				<td class="td_right">
					<a><input type="date" name="inspectionDate" id="inspectionDate" value="<s:date name='inspectionDate' format="yyyy-MM-dd"/>"  class="text-inp" ></a>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>进料数</span>
				</td>
				<td class="td_right">
					<input type="text" name="incomingAmount" id="incomingAmount" class="text-inp" value="${incomingAmount}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>抽检数</span>
				</td>
				<td class="td_right">
					<input type="text" name="checkAmount" id="checkAmount" class="text-inp" value="${checkAmount}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>物料类别</span>
				</td>
				<td class="td_right">
					<%-- <s:select list="materialTypes" 
							  theme="simple"
							  listKey="name" 
							  listValue="name" 
							  name="materialType"
							  id="materialType"
							  emptyOption="true"
							  labelSeparator=""
							  cssClass="text-inp"
							  ></s:select> --%>
							  <input id="materialType" name="materialType" class="text-inp"  value="${materialType}"/>		
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>供应商邮箱地址</span>
				</td>
				<td class="td_right">
					<input type="text" name="supplierEmail" id="supplierEmail" class="text-inp" value="${supplierEmail}">
				</td>
			</tr>
			<tr>
				<td class="last_td">*如需发多个邮箱请用“/”分开</td>
			</tr>
		</tbody>
	</table>
	<div class="surface" style="display:block;">
		<!-- <ul>
			<li>
				<span>不良类型</span>
				<select  class="select-inp" id="badselect_id">
					<option value="外观不良">外观不良</option>
					<option value="功能不良">功能不良</option>
					<option value="尺寸不良">尺寸不良</option>
					<option value="特性不良">特性不良</option>
				</select>
			</li>
		</ul> -->
		<!-- <script>
			
			$('#badselect_id').change(function(){
				var val=document.getElementById('badselect_id');
				var i=val.selectedIndex;
				if(i==0){
					$('#surface0').attr('style','display:block;');
					$('#surface1').attr('style','display:none;');
					$('#surface2').attr('style','display:none;');
					$('#surface3').attr('style','display:none;');
				}else if(i==1){
					$('#surface0').attr('style','display:none;');
					$('#surface1').attr('style','display:block;');
					$('#surface2').attr('style','display:none;');
					$('#surface3').attr('style','display:none;');
				}else if(i==2){
					$('#surface0').attr('style','display:none;');
					$('#surface1').attr('style','display:none;');
					$('#surface2').attr('style','display:block;');
					$('#surface3').attr('style','display:none;');
				}else if(i==3){
					$('#surface0').attr('style','display:none;');
					$('#surface1').attr('style','display:none;');
					$('#surface2').attr('style','display:none;');
					$('#surface3').attr('style','display:block;');
				}
			});
		</script> -->
	</div>
	<div class="surface" id="surface0" style="display:block;">
		<ul>
			<li><span>外观不良率</span><input type="text" name="surfaceBadRate" id="surfaceBadRate" class="delineate" style="width:50%;margin-left: 0.2rem;" value="${surfaceBadRate }">%</li>
			<li>
				<span>检验状态</span>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="surfaceBadState"
				  id="surfaceBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="select-inp">
				 </s:select>
			</li>
		</ul>
	</div>
	<div class="surface" id="surface1">
		<ul>
			<li><span>功能不良率</span><input type="text" name="functionBadRate" id="functionBadRate" class="delineate" style="width:50%;margin-left: 0.2rem;" value="${functionBadRate }">%</li>
			<li>
				<span>检验状态</span>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="functionBadState"
				  id="functionBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="select-inp">
				 </s:select>
			</li>
		</ul>
	</div>
	<div class="surface" id="surface2">
		<ul>
			<li><span>尺寸不良率</span><input type="text" name="sizeBadRate" id="sizeBadRate" class="delineate" style="width:50%;margin-left: 0.2rem;" value="${sizeBadRate }">%</li>
			<li>
				<span>检验状态</span>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="sizeBadState"
				  id="sizeBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="select-inp">
				 </s:select>
			</li>
		</ul>
	</div>
	<div class="surface" id="surface3">
		<ul>
			<li><span>特性不良率</span><input type="text" name="featuresBadRate" id="featuresBadRate" class="delineate" style="width:50%;margin-left: 0.2rem;" value="${featuresBadRate }">%</li>
			<li>
				<span>检验状态</span>
	     	 	<s:select list="labTestResults" 
				  theme="simple"
				  listKey="name" 
				  listValue="name" 
				  name="featuresBadState"
				  id="featuresBadState"
				  emptyOption="true"
				  labelSeparator=""
				  cssClass="select-inp">
				 </s:select>
			</li>
		</ul>
	</div>
	<div class="admit_infor">
		<ul>
			<li>异常描述</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select1" id="down_select1" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select1').click(function(){
			$('#badDesc').slideToggle();
		});
	</script>
	<textarea id="badDesc" name="badDesc" >${badDesc}</textarea>
 	<div class="problem">
		<ul>
 			<li class="other_li" id="upload">
				<div class="upload_b" >
				<span class="upload_span" ><a class="fileClick"  onclick="_uploadFiles('showdescFile','descFile');">上传附件</a></span>
				<input type="hidden" name="descFile" id="descFile" value="${descFile}">
				<input type="hidden" name="hisdescFile" id="hisdescFile" value="${hisdescFile}"><br/>
				<p id="showdescFile" ></p>
				</div>
			</li>
		</ul>
	</div>
	<div class="syq">
		<ul class="syq_1">
			<li>
				<div class="syq_2">
					<span>检验员</span>
				</div>
				<div><input type="text" name="inspector" id="inspector" value="${inspector}" readonly="readonly"></div>
			</li>
			<li>
				<div class="syq_2">
					<span>审核</span>
				</div>
				<div id="select_man">
					<input type="text" name="reportChecker" id="reportChecker" value="${reportChecker }">
					<input type="hidden" name="reportCheckerLog" id="reportCheckerLog" value="${reportCheckerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('reportChecker','reportCheckerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('reportChecker','reportCheckerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</div>					
			</li>
			<li>
				<div class="syq_2">
					<span>核准</span>
				</div>
				<div id="select_man">
					<input type="text" name="approvaler" id="approvaler" value="${approvaler }">
					<input type="hidden" name="approvalerLog" id="approvalerLog" value="${approvalerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('approvaler','approvalerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('approvaler','approvalerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</div>					
			</li>			
			<li>
				<div class="syq_2">
					<span>SQE确认</span>
				</div>
				<div id="select_man">
					<input type="text" name="sqeChecker" id="sqeChecker" value="${sqeChecker }">
					<input type="hidden" name="sqeCheckerLog" id="sqeCheckerLog" value="${sqeCheckerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqeChecker','sqeCheckerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('sqeChecker','sqeCheckerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</div>					
			</li>	
		</ul>
	</div>
	<security:authorize ifAnyGranted="supplier-improve-conceal">
	<table class="all_tab" style="margin-top:0.5rem;">
		<tbody>
			<tr>
				<th style="background-color:#b940fb;text-align:center;border-radius: 0.4rem 0.4rem 0 0;">PMC经办</th>
			</tr>
			<tr>
				<td class="td_left">
					<span>需求交期</span>
				</td>
				<td class="td_right">
					<input type="date" name="demandDeliveryPeriod" id="demandDeliveryPeriod" class="text-inp" value="<s:date name='demandDeliveryPeriod' format="yyyy-MM-dd"/>">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>mrb申请</span>
				</td>
				<td class="td_right">
		  		    <select name="mrbApply" id="mrbApply"  class="text-inp">
                         <option value=""></option>
                         <option value="需要" <c:if test='${"需要" eq mrbApply}'>selected</c:if>>需要</option>
                         <option value="不需要" <c:if test='${"不需要" eq mrbApply}'>selected</c:if>>不需要</option>
                </select>					
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>mrb单号</span>
				</td>
				<td class="td_right">
					<input type="text" name="mrbReportNo" id="mrbReportNo" class="text-inp" value="${mrbReportNo}" >
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>审核</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="pmcChecker" id="pmcChecker" value="${pmcChecker }" >
					<input type="hidden" name="pmcCheckerLog" id="pmcCheckerLog" value="${pmcCheckerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('pmcChecker','pmcCheckerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('pmcChecker','pmcCheckerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>			
			<tr>
				<td class="td_left">
					<span>核准</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="pmcApprovaler" id="pmcApprovaler" value="${pmcApprovaler }" >
					<input type="hidden" name="pmcApprovalerLog" id="pmcApprovalerLog" value="${pmcApprovalerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('pmcApprovaler','pmcApprovalerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('pmcApprovaler','pmcApprovalerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>	
		</tbody>
	</table>
	<div class="admit_infor">
		<ul>
			<li>PMC意见</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select2" id="down_select2" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select2').click(function(){
			$('#pmcOpinion').slideToggle();
		});
	</script>	
	<textarea id="pmcOpinion" name="pmcOpinion" >${pmcOpinion}</textarea>
	<table class="all_tab" style="margin-top:0.5rem;">
		<tbody>
			<tr>
				<th style="background-color:#b940fb;text-align:center;border-radius: 0.4rem 0.4rem 0 0;">SQE1</th>
			</tr>
			<tr>
				<td class="td_left">
					<span>处理意见</span>
				</td>
				<td class="td_right">
				  <select name="sqeProcessOpinion" id="sqeProcessOpinion"  class="text-inp">
                       <option value=""></option>
                       <option value="特采" <c:if test='${"特采" eq sqeProcessOpinion}'>selected</c:if>>特采</option>
                       <option value="退货" <c:if test='${"退货" eq sqeProcessOpinion}'>selected</c:if>>退货</option>
                </select>		
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>退货通知单单号</span>
				</td>
				<td class="td_right">
					<input type="text" name="returnReportNo" id="returnReportNo" class="text-inp" value="${returnReportNo }">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>mrb单号</span>
				</td>
				<td class="td_right">
					<input type="text" name="sqeMrbReportNo" id="sqeMrbReportNo" class="text-inp" value="${sqeMrbReportNo }">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>经办人</span>
				</td>
				<td class="td_right">
					<input type="text"   class="text-inp" readonly="readonly" value="${sqeChecker }">
				</td>
			</tr>		
			<tr>
				<td class="td_left">
					<span>审核</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="sqeAuditer1" id="sqeAuditer1" value="${sqeAuditer1 }" >
					<input type="hidden" name="sqeAuditerLog1" id="sqeAuditerLog1" value="${sqeAuditerLog1 }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqeAuditer1','sqeAuditerLog1')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('sqeAuditer1','sqeAuditerLog1')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>				
			<tr>
				<td class="td_left">
					<span>核准</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="sqeApprovaler1" id="sqeApprovaler1" value="${sqeApprovaler1 }" >
					<input type="hidden" name="sqeApprovalerLog1" id="sqeApprovalerLog1" value="${sqeApprovalerLog1 }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqeApprovaler1','sqeApprovalerLog1')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<%-- <sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('sqeApprovaler1','sqeApprovalerLog1')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>			
		</tbody>
	</table>
 	<div class="problem">
		<ul>
 			<li class="other_li" id="upload">
				<div class="upload_b" >
				<span class="upload_span" ><a class="fileClick"  onclick="_uploadFiles('showsupplierFile','supplierFile');">上传附件</a></span>
				<input type="hidden" name="supplierFile" id="supplierFile" value="${supplierFile}">
				<input type="hidden" name="hissupplierFile" id="hissupplierFile" value="${hissupplierFile}"><br/>
				<p id="showsupplierFile" ></p>
				</div>
			</li>
		</ul>
	</div>	
	<div class="admit_infor">
		<ul>
			<li>品质中心意见</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select3" id="down_select3" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select3').click(function(){
			$('#qualityOpinion').slideToggle();
		});
	</script>	
	<textarea id="qualityOpinion" name="qualityOpinion" >${qualityOpinion}</textarea>	
	</security:authorize>
	<div class="admit_infor">
		<ul>
			<li>暂定对策实施</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select4" id="down_select4" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select4').click(function(){
			$('#tempCountermeasures').slideToggle();
		});
	</script>	
	<textarea id="tempCountermeasures" name="tempCountermeasures" >${tempCountermeasures}</textarea>		
	<div class="admit_infor">
		<ul>
			<li>真因定义及验证</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select5" id="down_select5" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select5').click(function(){
			$('#trueReasonCheck').slideToggle();
		});
	</script>	
	<textarea id="trueReasonCheck" name="trueReasonCheck" >${trueReasonCheck}</textarea>	
	<div class="admit_infor">
		<ul>
			<li>永久对策实施</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select6" id="down_select6" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select6').click(function(){
			$('#countermeasures').slideToggle();
		});
	</script>	
	<textarea id="countermeasures" name="countermeasures" >${countermeasures}</textarea>	
	<div class="admit_infor">
		<ul>
			<li>预防再发生</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select7" id="down_select7" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select7').click(function(){
			$('#preventHappen').slideToggle();
		});
	</script>	
	<textarea id="preventHappen" name="preventHappen" >${preventHappen}</textarea>	
	<div class="syq">
		<ul class="syq_1">
			<li>
				<div class="syq_2">
					<span>经办</span>
				</div>
				<div id="select_man">
					<input type="text" name="supplierProcesser" id="supplierProcesser" value="${supplierName }">
					<input type="hidden" name="supplierProcesserLog" id="supplierProcesserLog" value="${supplierCode }">
<!-- 					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('supplierProcesser','supplierProcesserLog')"> -->
<%-- 						<img src="${ctx}/mobile/img/fdj.png"> --%>
<!-- 					</a> -->
<!-- 						<sub>	 -->
<!-- 							<b>*</b> -->
<!-- 						</sub> -->
<!-- 					<a id="polling_ljt" onclick="del('supplierProcesser','supplierProcesserLog')"> -->
<%-- 						<img src="${ctx}/mobile/img/ljt.png"> --%>
<!-- 					</a> -->
				</div>					
			</li>
		</ul>
	</div>
	<table class="all_tab" style="margin-top:0.5rem;">
		<tbody>
			<tr>
				<th style="background-color:#b940fb;text-align:center;border-radius: 0.4rem 0.4rem 0 0;">SQE</th>
			</tr>
			<tr>
				<td class="td_left">
					<span>供应商改善效果确认</span>
				</td>
				<td class="td_right">
					<input type="text" name="checkResult" id="checkResult" class="text-inp" value="${checkResult}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>sqe追踪完成时间</span>
				</td>
				<td class="td_right">
					<input type="date" name="sqeFinishDate" id="sqeFinishDate" class="text-inp" value="<s:date name='sqeFinishDate' format="yyyy-MM-dd"/>">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>追踪人</span>
				</td>
				<td class="td_right">
					<input type="text" class="text-inp" readonly="readonly" value="${sqeChecker}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>审核</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="sqeAuditer" id="sqeAuditer" value="${sqeAuditer1 }" >
					<%-- <input type="hidden" name="sqeAuditerLog" id="sqeAuditerLog" value="${sqeAuditerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqeAuditer','sqeAuditerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('sqeAuditer','sqeAuditerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>核准</span>
				</td>
				<td class="td_right" id="select_man">
					<input type="text" name="sqeApprovaler" id="sqeApprovaler" value="${sqeApprovaler1 }" >
					<%-- <input type="hidden" name="sqeApprovalerLog" id="sqeApprovalerLog" value="${sqeApprovalerLog }">
					<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('sqeApprovaler','sqeApprovalerLog')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>	
							<b>*</b>
						</sub>
					<a id="polling_ljt" onclick="del('sqeApprovaler','sqeApprovalerLog')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a> --%>
				</td>
			</tr>							
		</tbody>
	</table>
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
  <div style="height:11rem;width: 100%"></div>
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
		var par =  prevnode(self.parentNode.parentNode.parentNode.previousSibling);
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
						prevnode(target.parentNode.previousSibling).className = 'ren';
					}else{
						tp.style.display = 'none';
						prevnode(target.parentNode.previousSibling).className = 'add';
					}	
				}
				break;
			case 'SPAN':
				var ap = nextnode(nextnode(target.nextSibling).nextSibling);
				if(ap.style.display != 'block' ){
					ap.style.display = 'block';
					target.className = 'ren';
				}else{
					ap.style.display = 'none';
					target.className = 'add';
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
	};
</script>

 <script>
	function del(id){
		document.getElementById(id).value="";
	} 
	$("#processFormId").click(function(){
		 $("#processForm").hide();
		 $("#processFormNk").hide();
	});
 </script>

<script>
</script>
</body>
</html>
