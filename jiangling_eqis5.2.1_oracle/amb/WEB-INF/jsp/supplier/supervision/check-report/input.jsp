<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
	<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/jquery.metadata.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
	var params = {},hisParams = {};
	$(document).ready(function(){
		jQuery('#checkDate').attr("readonly","readonly")
		.click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		//添加验证
		$("#reportGradeForm").validate({
		});
		$("#reportGradeForm :input").each(function(index,obj){
			if(obj.name&&obj.name.indexOf("params.")>-1&&obj.name.indexOf("realFee")>-1){
				$(obj).bind("keyup",function(){
					caculateFees();
				}).bind("change",function(){
					caculateFees();
				});
			}
		});
	});
	function caculateFees(){
		var val = 0;
		$("#inspection-table tbody tr").each(function(index,obj){
			$(obj).find(":input").each(function(i,item){
			if(item.name.indexOf("realFee")>-1&&item.name!='totalrealFee'){
				if(!isNaN(item.value)&&!isNaN(parseFloat(item.value)&&item.value.match(/^[0-9].*$/))){
					val += parseFloat(item.value);
				}
			}
			});
		});
		val = val.toFixed(2);
		$("#" + "realFeeSpan").html(val);
		$("#" + "totalrealFee").val(val);
	}
	/////////////////供应商选择//////////////////////////////////
	function supplierNameClick(obj){
		var url='${supplierctx}/archives/select-supplier.htm';
		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:480,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#supplierId").val(obj.id);
		$("#supplierName").val(obj.name);
		$("#supplierCode").val(obj.code);
	}
	/////////////////供应商选择结束//////////////////////////////////
	
	
	/////////////////计划选择//////////////////////////////////
	function planCodeClick(obj){
		var url='${supplierctx}/supervision/check-plan/select-check-plan.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择稽查计划"
		});
	}
	function setCheckPlanValue(objs){
		var obj = objs[0];
		$("#supplierId").val(obj.supplierId);
		$("#supplierName").val(obj.supplierName);
		$("#supplierCode").val(obj.supplierCode);
		$("#checkDate").val(obj.planDate);
		$(":input[name=checkBomName]").val(obj.checkBomName);
		$(":input[name=checkBomCode]").val(obj.checkBomCode);
		$("#planCode").val(obj.planCode);
		$("#checkPlanId").val(obj.id);
		$("#id").val(obj['checkReport.id']);
	}
	/////////////////计划选择结束//////////////////////////////////
	
	
	////////////////选择物料BOM////////////////////////////////////
	function checkBomNameClick(obj){
		var url = '${carmfgctx}/common/product-bom-select.htm';
		$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"选择物料BOM"
		});
	}
	function setBomValue(objs){
		var obj = objs[0];
		$(":input[name=checkBomCode]").val(obj.key);
		$(":input[name=checkBomName]").val(obj.value);
	}
	////////////////选择物料BOM结束////////////////////////////////////
	
	function saveReport(){
		if($('#reportGradeForm').valid()){
			var url = '${supplierctx}/supervision/check-report/save.htm';
			var params = getParams();
			
			$("#btnDiv").find("button.btn").attr("disabled",true);
			$("#message").html("正在保存,请稍候... ...");
			$.post(url,params,function(result){
				$("#btnDiv").find("button.btn").attr("disabled",false);
				$("#message").html("");
				if(result.error){
					alert(result.message);
				}else{
					$("#id").val(result.id);
					$("#message").html("保存成功!");
					showMsg();
				}
			},"json");
		}
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$("#reportGradeForm :input").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name){
				if(obj.type == 'radio'){
					if(obj.checked){
						params[obj.name] = jObj.val();
					}else if(!params[obj.name]){
						params[obj.name] = '';
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
		var items = "";
		$("#inspection-table tbody tr").each(function(index,obj){
			var p = "";
			$(obj).find(":input").each(function(i,item){
				if(p){
					p += ",";
				}
				p += "\"" + item.name + "\":\"" + item.value + "\"";
			});
			if(items){
				items += ",";
			}
			items +=  "{" + p + "}";
		});
		params["items"] = "[" + items + "]";
		return params;
	}
	function addNew(){
		window.location="${supplierctx}/supervision/check-report/input.htm";
	}
	//选择检验人员
 	function selectObj(){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"选择监察小组成员",
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'true',
			hiddenInputId:"groupMembers",
			showInputId:"groupMembers",
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="supervision";
		var thirdMenu="_check_report_input";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supervision-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
				<form action="" id="reportGradeForm">
				<div class="opt-btn" id="btnDiv" style="line-height:30px;">
					<security:authorize ifAnyGranted="supervision-check-save">
					<button class="btn" onclick="saveReport();" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button> 
					</security:authorize>				
					<security:authorize ifAnyGranted="supervision-check-input">
					<button class="btn" onclick="addNew();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>				
					<span style="display: none;color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
				</div>
				<div id="opt-content" >
					<table class="form-table-without-border" id="appraisal-table"	style="width:100%;">
						<caption style="height: 27px;text-align: center"><h2>供应商监察评分表</h2></caption>
								<tr height=20>
									<td colspan="6" style="text-align: right;padding-right:4px;">
										编号:${checkReport.code}
										<input type="hidden" name="id" id="id" value="${id}"/>
									</td>
								</tr>
								<tr height=29>
								<td  style="width: 10%"><font color=red>*</font>计划编号</td>
									<td  style="width: 20%">
										<input  readonly="readonly" value="${planCode}" id="planCode" name="planCode" onclick="planCodeClick(this)" class="{required:true}"></input>
									</td>
									<td  style="width: 10%"><font color=red>*</font>供应商名称</td>
									<td  style="width: 20%">
										<input  readonly="readonly" value="${supplierName}" id="supplierName" name="supplierName" onclick="supplierNameClick(this)" class="{required:true}"></input>
										<input type="hidden" id="supplierId" name="supplierId" value="${supplierId}"/>
										<input type="hidden" name="supplierCode" id="supplierCode" value="${supplierCode}"/>
										<input type="hidden" id="checkPlanId" name="checkPlanId" value="${checkPlanId}"/>
									</td>
									<td  style="width: 10%">机型</td>
									<td  style="width: 20%"> 
										<s:select list="modelSpecifications"
											listKey="name"
											listValue="name"
											emptyOption="true"
											name="modelSpecification"
											theme="simple"
											cssStyle="width:153px;"
											></s:select></td>
								</tr>
								
								<tr height=29>
								<td><span ><font color=red>*</font>监察时间</span></td>
								<td><input readonly="readonly" id="checkDate" name="checkDate" value="${checkDateString}" class="{required:true}"></input></td>
								<td><span ><font color=red>*</font>零件名</span></td>		
								<td><input readonly="readonly" name="checkBomName" value="${checkBomName}" onclick="checkBomNameClick(this)" class="{required:true}"></input></td>
								<td><span ><font color=red>*</font>零件号</span></td>
								<td><input readonly="readonly" name="checkBomCode" value="${checkBomCode}" onclick="checkBomNameClick(this)" class="{required:true}"></input></td>		
								</tr>
								</table>
								<table>
								<tr height=29>
									<td>审核理由</td>
									<td>
										<s:iterator value="auditReasons" id="option">
											<input name="auditReason" type="checkbox" value="${option.name}" <s:if test="%{#option.checked==1}">checked="checked"</s:if>></input>${option.name}&nbsp;&nbsp;
										</s:iterator>
									</td>
								</tr>
								</table>
						<table class="form-table-border-left" id="inspection-table"	style="width:100%;margin-top:4px;">
							<thead>
								<tr style="background:#99CCFF;">
									<th colspan="2" width="10%" style="text-align:center">项目</th>
									<th width="5%">序号</th>
									<th width="20%">确认事项</th>
									<th width="5%">权重</th>
									<th width="5%">上次得分</th>
									<th width="17%">问题</th>
									<th width="5%">本次得分</th>
									<th width="17%">问题</th>
									<th width="16%">备注</th>
								</tr>
							</thead>
							<tbody>
								${reportGradeStr}
							</tbody>
						</table>
				</div>
				</form>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>