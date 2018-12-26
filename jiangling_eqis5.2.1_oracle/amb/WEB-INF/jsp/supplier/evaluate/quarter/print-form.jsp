<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.api.entity.Option"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="com.norteksoft.product.api.ApiFactory"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<style type="text/css">
	<!--
	.Tbr_Wp{padding:6px;}
	.Tbr_Top{border-bottom:2px solid #bbb; background:#e4e4e4;}
	.Tbr_Btm{border-top:2px solid #bbb; background:#e4e4e4;}
	.AtcItem{color:#777;}
	-->
	</style>
	<style type="text/css"  media="print">
		.unPrint{display:none;}
	</style>
<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
<%
    //内部损失类型
    List<Option> evaluateLevels = ApiFactory.getSettingService().getOptionsByGroupCode("supplier_evaluate_level");
    ActionContext.getContext().put("evaluateLevels", evaluateLevels);
%>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
</object>
<script type="text/javascript">
	function contentResize(){
// 		$("#opt-content").height($(".ui-layout-center").height());
	}
	var myLayout;
	$(document).ready(function(){
		myLayout = $('body').layout({
			north__paneSelector : '#header',
			north__size : 66,
			west__size : 250,
			north__spacing_open : 31,
			north__spacing_closed : 31,
			west__spacing_open : 6,
			west__spacing_closed : 6,
			center__minSize : 400,
			resizable : false,
			paneClass : 'ui-layout-pane',
			north__resizerClass : 'ui-layout-resizer',
			west__onresize : $.layout.callbacks.resizePaneAccordions,
			center__onresize : contentResize
		});
		/* if(${canModify}==false){
			var month = document.getElementsByName("evaluate.evaluateMonth")[0].selectedOptions[0].innerHTML;
			$("#message").html("当前不允许修改评价"+month);
		} */
	});
</script>
<style type="text/css">
table tr {
	height: 29px;
}
</style>
<%
	Date date = new Date(System.currentTimeMillis());
%>
<script type="text/javascript">
		var LODOP;//声明打印的全局变量 
		var postData = {};
		var params = {
			supplierId : '${evaluate.supplierId}',
			evaluateYear : '${evaluate.evaluateYear}',
			evaluateMonth : '${evaluate.evaluateMonth}',
			materialType : '${evaluate.materialType}',
			estimateModelId : '${evaluate.estimateModelId}'
		};
		$(document).ready(function(){
			$("#evaluateForm").validate();
			$("#evaluateDate").datepicker({
				changeMonth : true,
				changeYear : true,
				minDate : '${minDateStr}',
 				maxDate : '${maxDateStr}'
			});
			$("#reportDate").datepicker({changeMonth:true,changeYear:true});
			$("#writeDate").datepicker({changeMonth:true,changeYear:true});
			$("#auditDate").datepicker({changeMonth:true,changeYear:true});
			$("#approvalDate").datepicker({changeMonth:true,changeYear:true});
			bindEvent();
			contentResize(); 
		});
		//更新实际得分
		function updateRealTotalPoints(){
			var val = 0;
			$("#estimate-service1-table-body input[realTotalPoint=true]").each(function(index,obj){
				if(!isNaN(obj.value)&&!isNaN(parseFloat(obj.value))){
					val += parseFloat(obj.value);
				}
			});
			$("#realTotalPoints").html(val.toFixed(2));
		}
		//保存供应商评价
		function saveEvaluate(){
			var materialType = document.getElementsByName("evaluate.materialType")[0].selectedOptions[0].innerHTML;
			if(materialType.length<=0){
				alert("请选物料类别");
				return;
			}
			if($("#evaluateForm").valid()){
				var params = getParams();
				$(".opt-btn .btn").attr("disabled",true);
				$("#message").html("正在保存,请稍候......");
				$.post("${supplierctx}/evaluate/quarter/save.htm",params,function(result){
// 					$("#message").html(result.message);
					alert(result.message);
					if(!result.error){
						$("#id").val(result.id);
					}
					setTimeout(function(){
						$("#message").html('');
					},1000);
// 					$(".opt-btn .btn").attr("disabled",false);
				},'json');
			}
		}
		//获取参数
		function getParams(){
			var params = {};
			$("#evaluateForm input").each(function(index,obj){
				var jObj = $(obj);
				if((obj.type=='text'||obj.type=='hidden')&&obj.name){
					params[obj.name] = jObj.val();
				}
			});
			$("#evaluateForm select").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					params[obj.name] = jObj.val();
				}
			});
			$("#evaluateForm textarea").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}
		//重置方法
		function setParams(params){
			$("#evaluateForm input").each(function(index,obj){
				if((obj.type=='text'||obj.type=='hidden')&&obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
			$("#evaluateForm select").each(function(index,obj){
				if(obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
			$("#evaluateForm textarea").each(function(index,obj){
				if(obj.name){
					if(params[obj.name]){
						$(obj).val(params[obj.name]);
					}else{
						$(obj).val('');
					}
				}
			});
			$("#realTotalPoints").html(params.allTotalPoints);
		}
		//改变路径
		function changeLocation(p){
			p = p || {};
			for(var pro in p){
				params[pro] = p[pro];
			}
			var str = '';
			for(var pro in params){
				if(!str){
					str = "?";
				}else{
					str += "&";
				}
				str += pro + "=" + params[pro];
			}
			$(document).mask();
			window.location = "${supplierctx}/evaluate/quarter/add.htm" + str;
		}
		//改变路径
		function changeLocationByMaterialType(obj){
// 			var select = document.getElementsByName("evaluate.materialType")[0];
// 			var materialType = select.selectedOptions[0].innerHTML;
            var materialType = obj.selectedOptions[0].innerHTML;
			var p = p || {};
			for(var pro in p){
				params[pro] = p[pro];
			}
			params.materialType = materialType;
			var str = '';
			for(var pro in params){
				if(!str){
					str = "?";
				}else{
					str += "&";
				}
				str += pro + "=" + params[pro];
			}
			$(document).mask();
			window.location = "${supplierctx}/evaluate/quarter/add.htm" + str;
		}
		//评价年改变
		function yearChange(obj){
			changeLocation({evaluateYear:obj.value});
		}
		//评价月改变
		function monthChange(obj){
			changeLocation({evaluateMonth:obj.value});
		}
		//加载新的评价指标
		function loadEvaluateDetail(){
			var estimateModelId = '${evaluate.estimateModelId}';
			$.post("${supplierctx}/evaluate/quarter/load-evaluate-detail.htm",{estimateModelId:estimateModelId},function(result){
				if(result.error){
					alert(result.message);
				}else{
					$("#estimate-service1-table-body").html(result.message);
					bindEvent();
				}
			},'json');
		}
		function bindEvent(){
			$("#estimate-service1-table-body input[realTotalPoint=true]").bind("keyup",function(e) {
				e.stopPropagation();
				updateRealTotalPoints();
				judegChange(this.name.split("_")[1],this.value);
			});
			$("#estimate-service1-table-body input[score=true]").bind("keyup",function(e) {
				scortChange(this.name.split("_")[1],this.value);
			});
		}
		function judegChange(modelIndicatorId,score){
			if(!isNaN(score)&&!isNaN(parseFloat(score))){
				var total = $("input[name=params\.totalPoints_"+modelIndicatorId+"]").val();
				if(!isNaN(total)&&!isNaN(parseFloat(total))&&parseFloat(score)>parseFloat(total)){
					alert("实际得分不能大于总分");
					$("input[name=params\.totalPoints_"+modelIndicatorId+"]").focus();
				}
			}
		}
		//实绩改变
		function scortChange(modelIndicatorId,score){
			if(!isNaN(score)&&!isNaN(parseFloat(score))){
				var gradeRules = autoGradeMap[modelIndicatorId].gradeRules;
				for(var i=0;i<gradeRules.length;i++){
					var rule = gradeRules[i];
					if(score >= rule.start&&score<rule.end){
						$("input[name=params\.realTotalPoints_"+modelIndicatorId+"]").val(rule.fee);
						
						break;
					}
				}
				updateRealTotalPoints();
			}
		}
		//实绩改变
		function scortChangeObj(modelIndicatorId,obj,b){
			if(b){
				var script = $("input[name=params\.script_"+modelIndicatorId+"]").val();
				if(script&&script.indexOf("X")<0){
					var B = obj.value;
					var $objParent = obj.closest("tr");
					var $preInput = $($objParent.previousElementSibling).find("input[score=true]")[0];
					var $nextInput = $($objParent.nextElementSibling).find("input[score=true]")[0];
					var $nextInputPoint = $($objParent.nextElementSibling).find("input[realtotalpoint=true]")[0];
					var A = $preInput.value;
					var point = 0;
					var rate = 0;
					if(A==""){
						rate = 0;
						point = 0;
						$nextInput.value = rate;
						$nextInputPoint.value = point;
					}else{
						A = parseFloat(A);
						if(A==0){
							rate = 0;
							point = 0;
						}else{
							rate = (1-B/A).toFixed(3);
							point = eval(script);
							if(point<0){
								point=0;
							}
						}
						$nextInput.value = rate*100;
						$nextInputPoint.value = parseFloat(point).toFixed(3);
					}
				}
			}else{
				var score = obj.value;
				if(!isNaN(score)&&!isNaN(parseFloat(score))){
					var gradeRules = autoGradeMap[modelIndicatorId].gradeRules;
					for(var i=0;i<gradeRules.length;i++){
						var rule = gradeRules[i];
						if(score >= rule.start&&score<rule.end){
							$("input[name=params\.realTotalPoints_"+modelIndicatorId+"]").val(rule.fee);
							updateRealTotalPoints();
							break;
						}
					}
				}
			}
			updateRealTotalPoints();
		}
		//实绩改变
		function scortChangeRemark(modelIndicatorId,obj){
			var script =  $("input[name=params\.formula_"+modelIndicatorId+"]").val();
			if(script.indexOf("X")<0&&script.indexOf("Y")<0){
				var B = parseFloat(obj.value);
				var $objParent = obj.closest("tr");
				var $preInput = $($objParent.previousElementSibling).find("input[score=true]")[0];
				var $nextInput = $($objParent.nextElementSibling).find("input[score=true]")[0];
				var $nextInputPoint = $($objParent.nextElementSibling).find("input[realtotalpoint=true]")[0];
				var A = $preInput.value;
				var point = 0;
				var rate = 0;
				if(A==""){
					rate = 0;
					point = 0;
					$nextInput.value = rate;
					$nextInputPoint.value = point;
				}else{
					A = parseFloat(A);
					if(A==0){
						rate=0;
						script = script.replace("B/A",0);
						point = eval(script);;
					}else{
						if($objParent.toString().indexOf("首次未成功件数")>0){
							rate = (B/A).toFixed(3);
						}else{
							rate = (1-B/A).toFixed(3);
						}
						point = eval(script);
						if(point<0){
							point=0;
						}
					}
					$nextInput.value = rate*100;
					$nextInputPoint.value = parseFloat(point).toFixed(3);
				}
				updateRealTotalPoints();
			}else if(script.indexOf("X")>0){
				var materialType =  document.getElementsByName("evaluate.materialType")[0].selectedOptions[0].innerHTML;
				if(materialType.length==0){
					alert("物料类别不能为空");
					return;
				}
				$.post("${supplierctx}/base-info/material-type-goal/select-material-type.htm?materialType="+materialType+"&type=check",function(result){
					if(result.message){
						alert(message);
					}else{
						var X = result.value;
						var B = obj.value;
						var $objParent = obj.closest("tr");
						var $preInput = $($objParent.previousElementSibling).find("input[score=true]")[0];
						var $nextInput = $($objParent.nextElementSibling).find("input[score=true]")[0];
						var $nextInputPoint = $($objParent.nextElementSibling).find("input[realtotalpoint=true]")[0];
						var A = $preInput.value;
						var point = 0;
						var rate = 0;
						if(A==""){
							rate = 0;
							point = 0;
							$nextInput.value = rate;
							$nextInputPoint.value = point;
						}else{
							A = parseFloat(A);
							if(A==0){
								rate = 0;
								script = script.replace("B/A",0);
								point = eval(script);
							}else{
								rate = (1-B/A).toFixed(3);
								point = eval(script);
								if(point<0){
									point=0;
								}
							}
							$nextInput.value = rate*100;
							$nextInputPoint.value = parseFloat(point).toFixed(3);
							updateRealTotalPoints();
						}
					}
				},'json');
			}else if(script.indexOf("Y")>0){
				var materialType =  document.getElementsByName("evaluate.materialType")[0].selectedOptions[0].innerHTML;
				if(materialType.length==0){
					alert("物料类别不能为空");
					return;
				}
				$.post("${supplierctx}/base-info/material-type-goal/select-material-type.htm?materialType="+materialType+"&type=use",function(result){
					if(result.message){
						alert(message);
					}else{
						var Y = result.value;
						var B = obj.value;
						var $objParent = obj.closest("tr");
						var $preInput = $($objParent.previousElementSibling).find("input[score=true]")[0];
						var $nextInput = $($objParent.nextElementSibling).find("input[score=true]")[0];
						var $nextInputPoint = $($objParent.nextElementSibling).find("input[realtotalpoint=true]")[0];
						var A = $preInput.value;
						var point = 0;
						var rate = 0;
						if(A==""){
							rate = 0;
							point = 0;
							$nextInput.value = rate;
							$nextInputPoint.value = point;
						}else{
							A = parseFloat(A);
							if(A==0){
								rate = 0;
								script = script.replace("B/A",0);
								point = eval(script);;
								point = 0;
							}else{
								rate = (B/A).toFixed(3);
								point = eval(script);
								if(point<0){
									point=0;
								}
							}
							$nextInput.value = rate*100;
							$nextInputPoint.value = parseFloat(point).toFixed(3);
							updateRealTotalPoints();
						}
					}
				},'json');
			}
			
		}
		//自动评分
		function autoGrade(){
			var materialType = document.getElementsByName("evaluate.materialType")[0].selectedOptions[0].innerHTML;
			if(materialType.length<=0){
				alert("请选物料类别");
				return;
			}
			$("#opt-content").attr("disabled","disabled");
			var rules = [];
			for(var pro in autoGradeMap){
				rules.push(autoGradeMap[pro]);
			}
			grade(0,rules);
		}
		var autoGradeParams = {
			
		};
		function grade(index,rules){
			if(index >= rules.length){
				$("#opt-content").attr("disabled","");
				$("#message").show().html("自动评分完成!");
				showMsg();
				return;
			}
			var ruleMap = rules[index];
			if(ruleMap.dataSourceCode){
				autoGradeParams.dataSourceCode = ruleMap.dataSourceCode;
				autoGradeParams.cycle = $("#cycle").val();
				autoGradeParams.supplierCode = $("#supplierCode").val();
				$("#message").show().html("正在自动获取指标【"+ruleMap.name+"】的实绩...");
				$.post("${supplierctx}/evaluate/quarter/load-data-source.htm",autoGradeParams,function(result){
					if(result.error){
						alert(result.message);
					}else{
						var score = result.score;
						var point = result.point;
						var script = result.script;
						if(point==0){
							point='';
						}
						if(score&&(score+'').indexOf("\.")>-1){
							score = score.toFixed(2);
						}
						if(point&&(point+'').indexOf("\.")>-1){
							point = point.toFixed(2);
						}
						if(script){
							$("input[name=params\.script_"+ruleMap.modelIndicatorId+"]").val(script);
						}
						$("input[name=params\.realTotalPoints_"+ruleMap.modelIndicatorId+"]").val(point);
						$("input[name=params\.score_"+ruleMap.modelIndicatorId+"]").val(score);
						$("#message").show().html("获取指标【"+ruleMap.name+"】的实绩成功!");
						scortChange(ruleMap.modelIndicatorId,score);
						
					}
					setTimeout(function(){
						grade(index+1,rules);					
					},1000);
				},'json');
			}else{
				scortChange(ruleMap.modelIndicatorId,$("input[name=params\.score_"+ruleMap.modelIndicatorId+"]").val());
				grade(index+1,rules);
			}
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
	 	function SaveAsFile(){
			LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
			LODOP.PRINT_INIT("供应商评价报告打印");
			var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
			var strFormHtml=strBodyStyle+"<body>"+document.getElementById("evaluateForm").innerHTML+"</body>";

			LODOP.SET_PRINT_STYLE("FontSize",12);
			LODOP.SET_PRINT_STYLE("Bold",1);
			LODOP.ADD_PRINT_TABLE(0,0,"100%","100%",strFormHtml);
			LODOP.SAVE_TO_FILE("供应商评价报告.xlsx");
		}
		function prn1_preview() {	
			CreateOneFormPage();	
			LODOP.PREVIEW();	
		}
		function CreateOneFormPage(){
			LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
			LODOP.PRINT_INIT("供应商评价报告打印");
			var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
			var strFormHtml=strBodyStyle+"<body>"+document.getElementById("evaluateForm").innerHTML+"</body>";

			LODOP.SET_PRINT_STYLE("FontSize",12);
			LODOP.SET_PRINT_STYLE("Bold",1);
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strFormHtml);
		}
		
		function updates(url){
			var url = "${supplierctx}/evaluate/quarter/supplier-evaluate-settings-erp.htm";
			if(confirm("确定要同步")){
				$("#message").html("同步中，请稍候");
				$.post(url,{},function(result){
					alert(result.message);
				},'json');
			}
		}
		  function exportToExcel(){
		    	document.getElementById("tableInfo").value=document.getElementById("table").innerHTML;
		    	$("#evaluateForm").submit();
		    	/* var url = "${supplierctx}/evaluate/quarter/export-excel.htm";
		    	$.post(url, tableInfo,function(data) {
					if (data.error) {
						alert(data.message);
						return false;
					}
				}, "json"); */
		    }
		  function prn1_preview_form(){
			  var p = p || {};
				for(var pro in p){
					params[pro] = p[pro];
				}
				var str = '';
				for(var pro in params){
					if(!str){
						str = "?";
					}else{
						str += "&";
					}
					str += pro + "=" + params[pro];
				}
			  window.open('${supplierctx}/evaluate/quarter/print-form.htm'+str);
		  }
	</script>
</head>
<div  style="display:none;">
		<%@include file="left.jsp"%>
	</div>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">

	<div class="ui-layout-center">
		<%
			String error = (String)ActionContext.getContext().get("error");
		%>
		<%
			if("".equals(error)){
		%>
			<script type="text/javascript">
				var autoGradeMap = ${autoGradeMap};
				autoGradeParams.evaluateYear = ${evaluate.evaluateYear};
				autoGradeParams.evaluateMonth = ${evaluate.evaluateMonth};
				autoGradeParams.supplierId = ${evaluate.supplierId};
				autoGradeParams.materialType = '${evaluate.materialType}';
				autoGradeParams.cycle = '${evaluate.cycle}';
			</script>
			<div class="opt-body" style="overflow-y:auto;">
			<form   method="post" id="evaluateForm" name="evaluateForm">
				<div class="opt-btn" id="btnDiv">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td valign="middle" style="padding: 0px; margin: 0px;">
							<div class="Tbr_Wp Tbr_Btm unPrint" style="width:890px;margin:auto;">
								<button class='btn' onclick="window.print();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>
								<button class='btn' onclick="window.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭页面</span></span></button>
	</div>
						    </td>
						</tr>
					</table>
				</div>
				<div id="opt-content">
				 <span id="table">
					<input type="hidden" id="id" name="id" value="${evaluate.id}" /> <input id="cycle"
						type="hidden" name="cycle" value="${evaluate.cycle}" /> <input
						type="hidden" name="startMonth" value="${evaluate.startMonth}" />
					<input type="hidden" name="estimateModelId"
						value="${evaluate.estimateModelId}" /> <input type="hidden"
						name="estimateModelName" value="${evaluate.estimateModelName}" />
					<input type="hidden" name="parentModelId"
						value="${evaluate.parentModelId}" />
					<div style="height: 30px; text-align: center;">
						<h2>供应商表现——${evaluate.estimateModelName}</h2>
					</div>
					<table class="form-table-without-border" id="header-table"
						style="width: 30%; margin: auto; ">
						<tr>
							<td style="width: 60%; "><input
								type="hidden" name="evaluateNo" value="${evaluate.evaluateNo}"></input>
								编号:${evaluate.evaluateNo}</td>
						</tr>
					</table>
					<table class="form-table-border" id="estimate-service1-table"
						style="width: 60%; margin: auto;float:left;">
						<tr>
							<td style="">供应商名称</td>
							<td style="text-align: left;">
								&nbsp;${evaluate.supplierName} 
							</td>
							<td>物料类别</td>
							<td>
							   ${evaluate.materialType }
							</td>
						</tr>
						<tr>
							<td colspan="4" style="padding: 6px;">
								<table class="form-table-border" id="estimate-service1-table-1"
									style="width: 100%;float:left;">
									<thead>
										<tr>
											<th width="10%">评价指标</th>
											<th width="20%" colspan="2">细则</th>
											<th width="7%">总分</th>
											<th width="7%">实绩</th>
											<th width="6%">得分</th>
											<th width="50%">简述</th>
										</tr>
									</thead>
									<tbody id="estimate-service1-table-body">
										${modelObj}
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="4" style="text-align: left">主要表现及改进要求</td>
						</tr>
						<tr>
							<td colspan="4">${evaluate.description}
							</td>
						</tr>
						<tr>
							<td colspan="4" style="padding:none;border-left-width:0px;border-top-width:0px;border-bottom-width:0px;border-right-width:0px;">
								<table class="form-table-border"  style="width:100%;border: none;" >
									<tr>
<!-- 										<td colspan="4" >&nbsp;</td> -->
										<td style=""><font color="red">*</font>填写人</td>
										<td>${evaluate.writeMan}</td>
										<td style=""><font color="red">*</font>填写日期</td>
										<td>${evaluate.writeDateStr}
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</span>
				</div>
				</form>
			</div>
		<%}else{ %>
		<table width=100% height=100% style="background: white;">
			<tr>
				<td valign="top">
					<h2>
						<s:actionmessage />
					</h2>
				</td>
			</tr>
		</table>
		<%} %>
	</div>

</body>
</html>