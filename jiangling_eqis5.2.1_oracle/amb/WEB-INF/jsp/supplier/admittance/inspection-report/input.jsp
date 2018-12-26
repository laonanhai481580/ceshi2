<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.supplier.entity.InspectionGrade"%>
<%@page import="com.ambition.supplier.entity.InspectionGradeType"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		function contentResize(){
			var width = $("#opt-content").width();
			$("#surveyGrade").jqGrid("setGridWidth",width);
			$("#surveyGrade").jqGrid("setGridHeight",$("#opt-content").height()-200);
		}
		$(function(){
			createGrid();
			contentResize();
			if('${workflowInfo.firstTaskId}'){
				$("#btnDiv").find("button").attr("disabled",true);
				$("#message").html("考察记录当前状态为 【${workflowInfo.currentActivityName}】 不能修改!");
				$("#newBtn").attr("disabled","");
				$("#returnBtn").attr("disabled","");
			}else{
				setTimeout(function(){
					$("#message").html("");
				}, 3000);
			}
		});
		function createGrid(){
			jQuery("#surveyGrade").jqGrid({
				url:'${supplierctx}/admittance/inspection-report/query-inspection-grade-types.htm?id=${id}',
				postData : {},
				treedatatype: "json",
				height : 120,
				mtype: "POST",
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'isLeaf',index:'isLeaf', width:1,hidden:true},
			   		{name:'level',index:'level', width:1,hidden:true},
			   		{name:'reviewerLoginName',index:'reviewerLoginName', width:1,hidden:true},
			   		{label:'　',name:'name',index:'name', width:200},
			   		{label:'权重',name:'weight',index:'weight', width:80,formatter:function(val){if(val){return val + "%";}else{return "";}}},
			   		{label:'分值',name:'totalFee',index:'totalFee', width:70},
			   		{label:'评审人员',name:'reviewer',index:'reviewer', width:160,editable:true}
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
// 				ExpandColClick : true,
				shrinkToFit : true,
				gridComplete : function(){
				},
				loadComplete : function(){
// 					var len = $("#surveyGrade")[0].p.data.length;
// 					if(len<=3){
// 						len = 3;
// 					}
// 					jQuery("#surveyGrade").jqGrid("setGridHeight",len*22+22);
				},
				cellEdit: true, 
				cellsubmit : 'clientArray',
				afterEditCell : function(rowid,nm,tmp,iRow,iCol){
					var grid = $("#surveyGrade");
					var id = iRow + "_" + nm;
					$("#" + id).attr("readonly","readonly")
						.attr("iRow",iRow)
						.attr("iCol",iCol)
						.click(function(){
							var data = $("#surveyGrade").jqGrid("getRowData",rowid);
							$("#reviewerLoginName").val(data.reviewerLoginName);
							selectObj("选择评审人员", id, "reviewerLoginName", false,"loginName",function(){
								var val = jQuery(".reviewerLoginName").val();
								grid.jqGrid("setRowData",rowid,{reviewerLoginName:val});
							});
						});
				}
			});
		}
		function submitForm(url){
			$("#surveyGrade :input").each(function(index,obj){
				var $obj = $(obj);
				var iRow = $obj.attr("iRow"),iCol = $obj.attr("iCol");
				if(iRow != undefined && iCol != undefined){
					$("#surveyGrade").jqGrid("saveCell",iRow,iCol);
				}
			});
			if($('#inspectionReportForm').valid()){
				//检查第一级是否已经设置了评审人员
				var dataIds = $("#surveyGrade").jqGrid("getDataIDs");
				for(var i=0;i<dataIds.length;i++){
					var id = dataIds[i];
					var data = $("#surveyGrade").jqGrid("getRowData",id);
					if(data.level == 0 && !data.reviewer){
						alert(data.name + "的评审人员不能为空!");
						return;
					}
				}
		 		$('#inspectionReportForm').attr("action",url);
		 		var paramsStr = "";
		 		for(var i=0;i<dataIds.length;i++){
					var id = dataIds[i];
					var data = $("#surveyGrade").jqGrid("getRowData",id);
					if(data.reviewer){
						if(paramsStr){
							paramsStr += ",";
						}
						paramsStr += "{\"id\":\"" + data.id + "\",\"reviewer\":\"" + data.reviewer + "\",\"reviewerLoginName\":\""+data.reviewerLoginName+"\",\"weight\":\""+data.weight.split("%")[0]+"\"}";						
					}
				}
				$("#paramsStr").val("[" + paramsStr + "]");
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
				$('#inspectionReportForm').submit();
			}
		}
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","#inspectionReportForm").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					if(obj.type == 'radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}
					}else if(obj.type == 'checkbox'){
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
	 		$("#message").html("正在创建新的供应商考察报告,请稍候... ...");
			window.location='${supplierctx}/admittance/inspection-report/input.htm';
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
			$("#supplierName").val(obj.name);
			$("#supplierId").val(obj.id);
		}
		//选择供应商供应的产品
	 	function selectSupplyProducts(){
			var supplierId = $("#supplierId").val();
			if(!supplierId){
				alert("请先选择供应商!");
				return;
			}
	 		var url='${supplierctx}/archives/select-supply-products.htm?multiselect=true&currentNode=inspection-report&id=' + supplierId;
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"选择供应的产品"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setSupplyProductValue(objs){
	 		var names = [],ids = [],codes = [],materialTypes = [];
	 		for(var i=0;i<objs.length;i++){
	 			names.push(objs[i].name);
	 			ids.push(objs[i].id);
	 			codes.push(objs[i].code);
	 			materialTypes.push(objs[i].materialType);
	 		}
	 		$("#inspectionMaterialType").val(materialTypes.join(","));
	 		$("#inspectionBomCodes").val(codes.join(","));
	 		$("#inspectionBomNames").val(names.join(","));
			$("#inspectionSupplyProductIds").val(ids.join(","));
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
	 	//根据流程提交表单
	 	function submitFormByTask(url){
	 		$("#surveyGrade :input").each(function(index,obj){
				var $obj = $(obj);
				var iRow = $obj.attr("iRow"),iCol = $obj.attr("iCol");
				if(iRow != undefined && iCol != undefined){
					$("#surveyGrade").jqGrid("saveCell",iRow,iCol);
				}
			});
			if($('#inspectionReportForm').valid()){
				//检查第一级是否已经设置了评审人员
				var dataIds = $("#surveyGrade").jqGrid("getDataIDs");
				for(var i=0;i<dataIds.length;i++){
					var id = dataIds[i];
					var data = $("#surveyGrade").jqGrid("getRowData",id);
					if(data.level == 0 && !data.reviewer){
						alert(data.name + "的评审人员不能为空!");
						return;
					}
				}
				$('#inspectionReportForm').attr("action",url);
				var paramsStr = "";
		 		for(var i=0;i<dataIds.length;i++){
					var id = dataIds[i];
					var data = $("#surveyGrade").jqGrid("getRowData",id);
					if(data.reviewer){
						if(paramsStr){
							paramsStr += ",";
						}
						paramsStr += "{\"id\":\"" + data.id + "\",\"reviewer\":\"" + data.reviewer + "\",\"reviewerLoginName\":\""+data.reviewerLoginName+"\",\"weight\":\""+data.weight.split("%")[0]+"\"}";						
					}
				}
				$("#paramsStr").val("[" + paramsStr + "]");
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
				$('#inspectionReportForm').submit();
			}
		}
	 	function submitImprove(id){
	 		var url='${improvectx}/correction-precaution/called-input.htm?inspectionReportId='+id;
	 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
	 			overlayClose:false,
	 			title:"改进页面",
	 			onClosed:function(){
	 			}
	 		});
	 	}
	 	function viewSupplierInfo(){
	 		var supplierId = $("#supplierId").val();
	 		if(!supplierId){
	 			alert("请选择供应商!");
	 		}else{
	 			
	 		}
	 	}
	 	function backToList(){
			$("#btnDiv").find("button.btn").attr("disabled",true);
			$("#opt-content").attr("disabled",true);
	 		$("#message").html("正在返回供应商考察台帐,请稍候... ...");
	 		window.location='${supplierctx}/admittance/inspection-report/list.htm';
	 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="admittance";
		var thirdMenu="_admittance_inspection";
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
					<security:authorize ifAnyGranted="admittance-save">
					<button class="btn" onclick="submitForm('${supplierctx}/admittance/inspection-report/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>暂存</span></span></button>
					</security:authorize>				
					<security:authorize ifAnyGranted="admittance-submit-process">
					<button class="btn" onclick="submitFormByTask('${supplierctx}/admittance/inspection-report/submit-process.htm');"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
					</security:authorize>				
					<security:authorize ifAnyGranted="admittance-add">
					<button class="btn" onclick="addNew();" id="newBtn"><span><span><b class="btn-icons btn-icons-add"></b>新建 </span></span></button> 
					</security:authorize>
					<button id="returnBtn" class='btn' type="button" onclick="backToList();"><span><span><b class="btn-icons btn-icons-undo"></b>返回台帐</span></span></button>	
					<span style="color:red;font-weight:bold;" id="message"><s:actionmessage theme="mytheme"/></span>
				</div>
			 </div>	
			<div id="opt-content">
			<input type="hidden" name="reviewerLoginName" id="reviewerLoginName" class="reviewerLoginName"></input>
			<form action="" method="post" id="inspectionReportForm" name="inspectionReportForm">
				<input type="hidden" name="id" value="${id}"></input>
				<input type="hidden" name="paramsStr" id="paramsStr"></input>
				<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
				<input type="hidden" name="taskTransact" id="taskTransact" />
				<table class="form-table-border-left" id="appraisal-table"	style="width:100%;margin-bottom:4px;">
						<caption style="height: 35px;text-align: center"><h2>潜在供应商考察报告</h2></caption>
						<caption style="margin-bottom:4px;"><div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${code}</div></caption>
						<tr>
							<td style="width:90px;"><span style="color:red;">*</span>供应商</td>
							<td>
								<input id="supplierName" value="${supplier.name}" style="width:70%;" readonly="readonly" onclick="selectSupplier()" class="{required:true}"/>
								<input type="hidden" name="supplierId" id="supplierId" value="${supplier.id}"></input>
<!-- 								<a href="javascript:void(0);viewSupplierInfo();">查看详情</a> -->
							</td>
							<td style="width:90px;">认证产品范围</td>
							<td colspan="3">
								<input name="matingProducts" value="${matingProducts}" style="width:90%;" />
							</td>
						</tr>
						<tr>
							<td style="width:90px;"><span style="color:red;">*</span>稽核日期</td>
							<td>
								<input name="inspectionDate" id="inspectionDate" class="required:true" value="${inspectionDateStr}" style="width:70%;"/>
							</td>
							<td style="width:90px;"><span style="color:red;">*</span>稽核人员</td>
							<td>
								<input name="inspectionManLoginName" id="inspectionManLoginName" value="${inspectionManLoginName}" type="hidden"/>
								<input name="inspectionMan" id="inspectionMan" class="required:true" value="${inspectionMan}" style="width:120px;" onclick="selectObj('选择考察人员','inspectionMan','inspectionManLoginName',false)"/>
							</td>
							<td style="width:90px;"><span style="color:red;">*</span>版本</td>
							<td>
								<input name="reportVersion" name="reportVersion" value="${reportVersion}" style="width:90px;" class="{required:true,number:true,min:${reportVersion}}"/>
							</td>
						</tr>
					</table>
					<table id="surveyGrade"></table>
					<table class="form-table-border-left" style="width:100%;margin-top:4px;">
						<tr>
							<td style="width:90px;"><span style="color:red;">*</span>会签人员</td>
							<td colspan="5" style="">
								<input type="hidden" name="evaluationMemberLoginNames" id="evaluationMemberLoginNames" value="${evaluationMemberLoginNames}" style="width:120px;"/>
								<input name="evaluationMembers" id="evaluationMembers" class="{required:true}" value="${evaluationMembers}" style="width:95%;" onclick="selectObj('选择会签人员','evaluationMembers','evaluationMemberLoginNames',true,'loginName')"/>
							</td>
						</tr>
				</table>
			</form>
		</div>
	</div>
</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
</html>