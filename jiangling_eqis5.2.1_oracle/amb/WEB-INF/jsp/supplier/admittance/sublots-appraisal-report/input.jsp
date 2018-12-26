<%@page import="com.ambition.supplier.entity.AppraisalReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<%-- 	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script> --%>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		$(function(){
			//添加验证
			$("#appraisalReportForm").validate({
			});
			$("#rectificationDate").datepicker({changeYear:'true',changeMonth:'true'});
			$("#reportDate").datepicker({changeYear:'true',changeMonth:'true'});
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
			if('${workflowInfo.firstTaskId}'){
				$("#btnDiv").find("button").attr("disabled",true);
				$("#message").html("小批鉴定记录当前状态为 【${workflowInfo.currentActivityName}】 不能修改!");
				$("#newBtn").attr("disabled","");
				$("#returnBtn").attr("disabled","");
			}else{
				setTimeout(function(){
					$("#message").html("");
				}, 3000);
			}
		});
		function submitForm(url){
			if($('#appraisalReportForm').valid()){
				$('#appraisalReportForm').attr("action",url);
				$("#btnDiv").find("button.btn").attr("disabled",true);
				var current = 0;
				var dd = setInterval(function(){
					current++;
					var str = '';
					for(var i=0;i<current;i++){
						str += ".";
					}
					$("#message").html("正在保存,请稍候..." + str);
					if(current>8){
						current = 0;
					}
				}, 500);
				$("#message").html("正在保存,请稍候...");
				$('#appraisalReportForm').submit();
			}else{
				var error = $("#appraisalReportForm").validate().errorList[0];
				$(error.element).focus();
				$("#message").html(error.message);
				setTimeout(function(){
					$("#message").html("");
				},3000);
			}
		}
		//根据流程提交表单
	 	function submitFormByTask(url){
	 		if($('#appraisalReportForm').valid()){
		 		$('#appraisalReportForm').attr("action",url);
				$("#btnDiv").find("button.btn").attr("disabled",true);
				var current = 0;
				var dd = setInterval(function(){
					current++;
					var str = '';
					for(var i=0;i<current;i++){
						str += ".";
					}
					$("#message").html("正在保存,请稍候..." + str);
					if(current>8){
						current = 0;
					}
				}, 500);
				$("#message").html("正在保存,请稍候...");
				$('#appraisalReportForm').submit();
			}else{
				var error = $("#appraisalReportForm").validate().errorList[0];
				$(error.element).focus();
				$("#message").html(error.message);
				setTimeout(function(){
					$("#message").html("");
				},3000);
			}
		}
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","#appraisalReportForm").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					if(obj.type == 'radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}else{
							if(obj.name.indexOf("params.")==0){
								if(params[obj.name] == undefined){
									params[obj.name] = 0;
								}
							}else{
								if(params[obj.name] == undefined){
									params[obj.name] = '';
								}
							}
						}
					}else if(obj.type == 'checkbox'){
						if(obj.checked){
							if(!params[obj.name]){
								params[obj.name] = jObj.val();
							}else{
								params[obj.name] = params[obj.name] + "," + jObj.val();
							}
						}else{
							if(!params[obj.name]){
								params[obj.name] = '';
							}
						}
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
			return params;
		}
		function addNew(){
			$("#btnDiv").find("button.btn").attr("disabled",true);
			$("#opt-content").attr("disabled",true);
	 		$("#message").html("正在创建新的小批鉴定报告,请稍候... ...");
			window.location='${supplierctx}/admittance/sublots-appraisal-report/input.htm';
		}
		
		function selectSupplier(){
			var url='${supplierctx}/archives/select-supplier.htm';
			$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		function setSupplierValue(objs){
			var obj = objs[0];
			var hisSupplierId = $("#supplierId").val();
			$("#supplierId").val(obj.id);
			$("#supplierName").val(obj.name);
			$("#address").val(obj.address);
			if(obj.id != hisSupplierId){
				$("#bomCodes").val('');
				$("#bomNames").val('');
			}
	 		//检测最新的小批鉴定阶段
	 		loadTimeOfPhase();
		}
		//选择供应商供应的产品
	 	function selectSupplyProducts(){
			var supplierId = $("#supplierId").val();
			if(!supplierId){
				alert("请先选择供应商!");
				return;
			}
	 		var url='${supplierctx}/archives/select-supply-products.htm?currentNode=sublots-appraisal&id=' + supplierId;
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"选择供应的产品"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setSupplyProductValue(objs){
	 		$("#bomNames").val(objs[0].name);
	 		$("#bomCodes").val(objs[0].code);
	 		$("#bomLevel").val(objs[0].importance);
	 		//检测最新的小批鉴定阶段
	 		loadTimeOfPhase();
	 	}
	 	//选择检验人员
	 	function selectObj(title,inputId,hiddenInputId,multiple,defaultTreeValue,callback){
	 		callback = callback||function(){};
			var acsSystemUrl = "${ctx}";
			popTree({ title :title,
				innerWidth:'400',
				treeType:"MAN_DEPARTMENT_TREE",
				defaultTreeValue:defaultTreeValue?defaultTreeValue:"id",
				leafPage:'false',
				multiple:multiple,
				hiddenInputId:hiddenInputId,
				showInputId:inputId,
				acsSystemUrl:acsSystemUrl,
				callBack:callback
			});
		}
	 	function submitImprove(id){
	 		var url='${improvectx}/correction-precaution/called-input.htm?sampleAppraisalId='+id;
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"改进页面",
	 			onClosed:function(){
	 			}
	 		});
	 	}
	 	//上传附件
		function uploadFiles(){
			$.upload({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
		}
	 	function backToList(){
	 		$("#message").html("正在返回小批鉴定台帐,请稍候... ...");
			$("#btnDiv").find("button.btn").attr("disabled",true);
			$("#opt-content").attr("disabled",true);
	 		window.location='${supplierctx}/admittance/sublots-appraisal-report/list.htm';
	 	}
	 	
	 	//查询最新的小批鉴定阶段
	 	function loadTimeOfPhase(){
	 		var bomCode = $("#bomCodes").val();
	 		var supplierId = $("#supplierId").val();
	 		if(supplierId&&bomCode){
	 			var url = '${supplierctx}/admittance/sublots-appraisal-report/get-time-of-phase.htm';
	 			$.post(url,{supplierId:supplierId,bomCode:bomCode},function(result){
	 				if(!result.error){
	 					$("#timeOfPhase").val(result.timeOfPhase);
	 					$("#timeOfPhaseSpan").html(result.timeOfPhase);
	 				}
	 			},'json');
	 		}else{
	 			$("#timeOfPhase").val(1);
				$("#timeOfPhaseSpan").html(1);
	 		}
	 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="admittance";
		var thirdMenu="_sublots_appraisal";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-admittance-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<div class="opt-btn">
				<div style="line-height:30px;" id="btnDiv">
					<security:authorize ifAnyGranted="admittance-batch-appraisal-save">
					<button class="btn" onclick="submitForm('${supplierctx}/admittance/sublots-appraisal-report/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>暂存</span></span></button>
					</security:authorize>				
					<security:authorize ifAnyGranted="admittance-batch-appraisal-submit">
					<button class="btn" onclick="submitFormByTask('${supplierctx}/admittance/sublots-appraisal-report/submit-process.htm');"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button> 
					</security:authorize>				
					<security:authorize ifAnyGranted="admittance-batch-appraisal-add">
					<button id="newBtn" class="btn" onclick="addNew();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>				
					<button id="returnBtn" class='btn' type="button" onclick="backToList();"><span><span><b class="btn-icons btn-icons-undo"></b>返回台帐</span></span></button>	
					<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
				</div>
			 </div>	
			<div id="opt-content">
				<form action="" method="post" id="appraisalReportForm" name="appraisalReportForm" >
					<input type="hidden" id="id" name="id" value="${id}"></input>
					<input type="hidden" id="timeOfPhase" name="timeOfPhase" value="${timeOfPhase}"></input>
					<table class="form-table-border-left" id="appraisal-table"	style="width:100%">
							<caption><h2>样件鉴定报告</h2></caption>
							<caption style="margin-bottom:4px;">
								<div style="float:left">
									小批鉴定阶段:第<span id="timeOfPhaseSpan">${timeOfPhase}</span>次小批鉴定
								</div>
								<div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${code}</div>
							</caption>
							<tr>
								<td width="10%"><span style="color:red;">*</span>供应商</td>
								<td colspan="3">
									<input id="supplierName" value="${supplier.name}" style="width:90%;" readonly="readonly" onclick="selectSupplier()" class="{required:true,messages:{required:'供应商必填!'}}"/>
									<input type="hidden" name="supplierId" id="supplierId" value="${supplier.id}"></input>
								</td>
								<td width="10%"><span style="color:red;">*</span>零件代号</td>
								<td width="15%">
									<input name="bomCodes" id="bomCodes" value="${bomCodes}" style="width:90%;" readonly="readonly" onclick="selectSupplyProducts()" class="{required:true,messages:{required:'零件代号必填!'}}"/>
								</td>
								<td width="10%"><span style="color:red;">*</span>零件名称</td>
								<td width="15%">
									<input name="bomNames" id="bomNames" value="${bomNames}" style="width:90%;" readonly="readonly" onclick="selectSupplyProducts()" class="{required:true,messages:{required:'零件名称必填!'}}"/>
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
									<button class="btn" type="button" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传</span></span></button>
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
								<td colspan="5">
									<input type="radio" name="isImprovement" <s:if test="isImprovement"> checked="checked" </s:if>value="true"/>是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="isImprovement" <s:if test="!isImprovement"> checked="checked" </s:if> value="false"/>否</td>
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
									<input name="evaluationMembers" id="evaluationMembers" class="{required:true,messages:{required:'会签人员必填!'}}" value="${evaluationMembers}" style="width:95%;" onclick="selectObj('选择会签人员','evaluationMembers','evaluationMemberLoginNames',true,'loginName')"/>
								</td>
								<td><span style="color:red;">*</span>审核人员</td>
								<td>
								<input name="auditManLoginName" id="auditManLoginName" value="${auditManLoginName}" type="hidden"/>
								<input name="auditMan" id="auditMan" value="${auditMan}" readonly="readonly" class="{required:true,messages:{required:'审核人员必填!'}}" id="auditMan" onclick="selectObj('选择审核人员','auditMan','auditManLoginName',false,'loginName')"/></td>
								<td><span style="color:red;">*</span>报告人</td>
								<td><input name="reportMan" value="${reportMan}" readonly="readonly" onclick="selectObj('reportMan')" id="reportMan" class="{required:true}"/></td>
								<td><span style="color:red;">*</span>报告日期</td>
								<td><input name="reportDate" value="${reportDateStr}" id="reportDate" class="{required:true}"/></td>
							</tr>
							<tr>
								<td style="text-align:right;" colspan="8">
									<button class="btn" onclick="submitImprove(${id});"><span><span><b class="btn-icons btn-icons-alarm"></b>发起改进</span></span></button>
								</td>
							</tr>
					</table>
				</form>
			</div>
	</div>
</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js">
</script><script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>