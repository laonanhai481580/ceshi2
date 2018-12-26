<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		$(function(){
			//添加验证
			$("#inspectionDate").val('<%=com.ambition.supplier.utils.DateUtil.formateDateStr(Calendar.getInstance())%>').datepicker();
			$("#qualitySignDate").datepicker({changeYear:'true',changeMonth:'true'});
			$("#directorOpininDate").datepicker({changeYear:'true',changeMonth:'true'});
			$("#discoverDate").datepicker({changeYear:'true',changeMonth:'true'});
			$("input","#DefectiveGoodsForm").each(function(index,obj){
				if(obj.name&&obj.name.indexOf("params.")>-1){
					if(obj.name.indexOf("_") == -1){
						$(obj).attr("readonly","readonly").click(function(){
							selectDefectionCode(this);
						});
					}else{
						$(obj).bind("keyup",function(){
							caculateBadNumbers();
						}).bind("change",function(){
							caculateBadNumbers();
						});
					}
				}
			});
			$("#partCode").click(function(){selectComponentCode()});
			$("#partName").click(function(){selectComponentCode()});
			$("#dutySupplier").click(function(){selectDutySupplier()});
		});
		function caculateBadNumbers(rowId){
			var val = 0;
			$("input","#DefectiveGoodsForm").each(function(index,obj){
				if(obj.name&&obj.name.indexOf("params.")>-1&&obj.name.indexOf("_") > -1){
					if(!isNaN(obj.value)&&!isNaN(parseInt(obj.value))){
						val += parseInt(obj.value);
					}
				}
			});
			$("#" + "unqualifiedAmount").val(val);
		}
		function submitForm(url){
			if($('#DefectiveGoodsForm').valid()){
				var params = getParams();
				$("#btnDiv").find("button.btn").attr("disabled",true);
				$("#message").html("正在保存,请稍候... ...");
				$.post(url,params,function(result){
					$("#btnDiv").find("button.btn").attr("disabled",false);
					$("#message").html("");
					if(result.error&&result.error!=""){
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
			$(":input","form").each(function(index,obj){
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
			return params;
		}
		function addNew(){
			window.location='${iqcctx}/defective-goods/input.htm';
		}
		
		function selectObj(title,id,treeType){
			var acsSystemUrl = "${ctx}";
			popTree({ title :title,
				innerWidth:'400',
				treeType:treeType,
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:id,
				showInputId:id,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
		
		//选择质量成本
		var selComposingItemId = '';
		function selectComposing(composingItemId){
			selComposingItemId = composingItemId;
			var url = '${costctx}/common/composing-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择质量成本"
	 		});
// 	 		setFullComposingValue([{
// 	 			code : 'aa',
// 	 			name :'培训费用',
// 	 			remark : 'asdfasdf'
// 	 		}]);
		}
	//  //选择之后的方法 data格式{key:'a',value:'a'}
	 	function setFullComposingValue(datas){
	 		var obj = datas[0];
	 		var index = selComposingItemId.split("_")[1];
	 		var val = "<td style=\"text-align:center\">";
	 		val += "<a href=\"#\" onclick=\"javascript:deleteComposingItem('"+selComposingItemId+"')\" style=\"font-weight:bold;\" title='清除质量成本'>清除</a>";
	 		val += "&nbsp;<a href=\"#\" onclick=\"javascript:selectComposing('"+selComposingItemId+"')\" style=\"font-weight:bold;\" title='选择质量成本'>选择</a>"
	 		val += "</td>";
	 		val += "<td style=\"text-align:center\"><input type='hidden' name='composingItems.name"+index+"' value='"+obj.name+"'></input>"+obj.name+"</td>";
	 		val += "<td style=\"text-align:center\"><input type='text' name='composingItems.name"+index+"_departmentName' id='composingItemsname"+index+"_departmentName' style='width:120px;'class='{required:true}' onclick='selectObj(\"选择部门\",\"composingItemsname"+index+"_departmentName\",\"DEPARTMENT_TREE\")'></input></td>";
	 		val += "<td style=\"text-align:center;\"><input type='text' id='composingItemsName"+index+"' name='composingItems.name"+index+"_amount' class='{min:0,number:true,required:true}' style='width:80px;'></input></td>";
	 		val += "<td>"+obj.remark+"<input type='hidden' name='composingItems.name"+index+"_remark' value='"+obj.remark+"'></input></td>";
	 		$("#" + selComposingItemId).html(val);
	 		$("#composingItemsName" + index).bind("keyup",function(){
	 			caculateAmount();
	 		}).bind("change",function(){
	 			caculateAmount();
	 		});
	 		caculateAmount();
	 	}
	 	//计算费用
	 	function caculateAmount(){
	 		var val = 0;
	 		$("input","#defective-goods-composing-table-body").each(function(index,obj){
	 			if(obj.id){
	 				if(!isNaN(obj.value)&&!isNaN(parseFloat(obj.value))){
						val += parseFloat(obj.value);
					}
	 			}
	 		});
	 		$("#totalComposingFee").html(val);
	 	}
	 	//清除费用
	 	function deleteComposingItem(composingItemId){
	 		var val = "<td style=\"text-align:center\">";
	 		val += "<a href=\"#\" onclick=\"javascript:selectComposing('"+composingItemId+"')\" style=\"font-weight:bold;\" title='选择质量成本'>选择</a>"
	 		val += "</td>";
	 		val += "<td></td>";
	 		val += "<td></td>";
	 		val += "<td></td>";
	 		$("#" + composingItemId).html(val);
	 		caculateAmount();
	 	}
	 	//选择不良代码
	 	var selectInputObj = '';
	 	function selectDefectionCode(obj){
	 		selectInputObj = obj;
	 		var url = '${mfgctx}/common/defection-code-bom.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择不良代码"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setDefectionValue(data){
	 		$(selectInputObj).val(data[0].value).focus();
	 	}
	 	
	 	//选择物料
	 	function selectComponentCode(){
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择物料"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setBomValue(datas){
	 		$("#partCode").val(datas[0].key);
	 		$("#partName").val(datas[0].value);
	 	}
	 	
		function selectDutySupplier(){
			var url='${supplierctx}/archives/select-supplier.htm';
			$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		function setSupplierValue(objs){
			$("#dutySupplier").val(objs[0].name);
		}
	 	
	 	//发起改进
	 	function importImprove(){
	 		var id = '${id}';
	 		if(confirm("确定要发起改进吗？")){
				$.post("${iqcctx}/defective-goods/import-improve.htm?id="+id, {}, function(result) {});
			}
	 	}
	 	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="defectiveReport";
		var thirdMenu="myDefectiveInput";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-defective-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body" style="overflow-y:auto;">
					<div class="opt-btn">
						<div style="line-height:30px;" id="btnDiv">
							<security:authorize ifAnyGranted="defective-save">
								<button class='btn' type="button" onclick="submitForm('${iqcctx}/defective-goods/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
								<button class='btn' onclick="addNew();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
							</security:authorize>
							<button class='btn' type="button" onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
							<%-- <button class='btn' onclick="submitForm('${mfgctx}/defective-goods/ledger/submit-process.htm')"><span><span>提交</span></span></button> --%>
							<!-- <button class='btn' onclick="importImprove();"><span><span>发起改进</span></span></button> -->
							<span style="display: none;color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
						</div>
					 </div>	
					<div id="opt-content" style="text-align: center;">
						<form action="" method="post" id="DefectiveGoodsForm" name="DefectiveGoodsForm">
							<input type="hidden" name="id" id="id" value="${id}"/>
							<input type="hidden" name="params.saveType" value="input"/>
							<div style="height: 30px;text-align: center"><h2>不合格品处理单</h2></div>
							<div style="height: 25px;width:95%;text-align: right"><font color="red">*</font>不合格品编号&nbsp;&nbsp;<input type="text"  name="unqualityNo" value="${unqualityNo}" class="{required:true,messages:{requried:'编号不能为空!'}}"/></div>
							<table class="form-table-border-left" id="rejectTable"	style="width:90%;margin: auto;">
								<tr>
									<td style="width:14%;"><span style="color:red">*</span>检验日期</td>
									<td style="width:20%">
										<input type="text" name="inspectionDate" id="inspectionDate" value="${inspectionDate}" class="{required:true}"/>
									</td>
									<td>质检报告单号</td>
									<td><input type="text" name="qualityTestingReportNo" value="${qualityTestingReportNo}"/></td>
									<td>批次号</td>
									<td>
										<input type="text" name="batchNo" value="${batchNo}"/>
									</td>
								</tr>
								<tr>
									<td style="width:13%;">产品类型</td>
									<td style="width:20%">
										<s:select list="productModelOptions" 
												  listKey="value" 
												  listValue="value"
												  theme="simple"
												  name="productModel">
									    </s:select>
									</td>
									<td style="width:13%;">产品型号</td>
									<td style="width:20%">
										<s:select list="modelSpecifications" 
												  listKey="value" 
												  listValue="value"
												  theme="simple"
												  name="modelSpecification"></s:select>
									</td>
									<td style="width:13%;">物料名称</td>
									<td style="width:20%"><input type="text" name="materialName" id="partName" value="${materialName}"/></td>
								</tr>
								<tr>
									<td style="width:14%;">物料代码</td>
									<td style="width:20%"><input type="text" name="materialCode" id="partCode" value="${materialCode}"/></td>
									<td>责任供应商</td>
									<td><input name="dutySupplier" id="dutySupplier" value="${dutySupplier}" readonly="readonly"/></td>
									<td>产品阶段</td>
									<td>
										<s:select list="productPhaseOptions" 
												  listKey="value" 
												  listValue="value"
												  theme="simple"
												  name="productPhase"></s:select>
									</td>
								</tr>
								<tr>
									<td valign="top">不良描述</td>
									<td colspan="5">
										<textarea rows="5" style="width: 100%" name="unqualifiedDescription">${unqualifiedDescription}</textarea>
									</td>
								</tr>
								<tr>
									<td>不合格等级</td>
									<td>
										<select name="unqualifiedGrade">
											<option value="A类">A类</option>
											<option value="B类">B类</option>
											<option value="C类">C类</option>
										</select>
									</td>
									<td>不合格类别</td>
									<td>
										<s:select list="unqualifiedTypeOptions" 
												  listKey="value" 
												  listValue="value"
												  theme="simple"
												  name="unqualifiedType"></s:select>
									</td>
									<td>不合格来源</td>
									<td>
										<s:select list="unqualifiedSourceOptions" 
												  listKey="value" 
												  listValue="value"
												  theme="simple"
												  name="unqualifiedSource"></s:select>
									</td>
								</tr>
								<tr>
									<td>工序</td>
									<td>
										<s:select list="workProcedureOptions" 
												  listKey="value" 
												  listValue="value"
												  theme="simple"
												  name="workProcedure"></s:select>
									</td>
									<td>发现日期</td>
									<td><input name="discoverDate" id="discoverDate" value="${discoverDate }"/></td>
									<td>是否批量</td>
									<td>
										<select name="isBatch">
											<option value="true">是</option>
											<option value="false">否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><span style="color:red">*</span>来料数</td>
									<td><input name="stockAmount" value="${stockAmount}" class="{required:true}"/></td>
									<td><span style="color:red">*</span>检验数</td>
									<td><input name="inspectionAmount" value="${inspectionAmount}" class="{required:true}"/></td>
									<td><span style="color:red">*</span>合格数</td>
									<td><input name="qualifiedAmount" value="${qualifiedAmount}" class="{required:true}"/></td>
								</tr>
								<tr>
									<td><span style="color:red">*</span>不良数量</td>
									<td colspan="5">
										<input type="text" id="unqualifiedAmount" name="unqualifiedAmount" value="${unqualifiedAmount}" class="{number:true,required:true,min:0}"/>
									</td>
								</tr>
								<tr>
									<td colspan="6" style="text-align:center;clear:both;padding:8px;" align="center">
										<table width=100%>
											<caption style="font-weight: bold; padding-bottom:8px;">不良项目</caption>
											${badItemStrs}
										</table>
									</td>
								</tr>
								<tr>
									<td style="text-align:center" rowspan="3">原<br/>因<br/>分<br/>析<br/>及<br/>处<br/>理<br/>意<br/>见</td>
									<td colspan="5" style="text-align:right">提出部门&nbsp;&nbsp;<input type="text" name="analyseDepartment" id='analyseDepartment' value="${analyseDepartment}" onclick="selectObj('选择部门','analyseDepartment','DEPARTMENT_TREE')" style="width:90px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
									提出人&nbsp;&nbsp;<input type="text" name="analyseUser" id="analyseUser" value="${analyseUser}" onclick="selectObj('选择人员','analyseUser','MAN_DEPARTMENT_TREE')" style="width:90px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
									部门负责人&nbsp;&nbsp;<input type="text" name="analyseDepartmentPrincipal" id="analyseDepartmentPrincipal" value="${analyseDepartmentPrincipal}" onclick="selectObj('选择人员','analyseDepartmentPrincipal','MAN_DEPARTMENT_TREE')" style="width:90px;"/></td>
								</tr>
								<tr>
									<td	colspan="4" style="text-align:center">原因分析</td>
									<td style="text-align:center;">处理意见</td>
								</tr>
								<tr>
									<td	colspan="4"><textarea rows="5" style="width:100%" name="analyseReason">${analyseReason}</textarea></td>
									<td valign="top">
									<s:iterator value="disposeMethodOptions" id="option">
										<input type="checkbox" name="disposeMethod" value="${option.value}"/>${option.name}</br>
									</s:iterator>
									</td>
								</tr>
								<tr>
									<td style="text-align:center" rowspan="2">技<br/>术<br/>部<br/>门<br/>评<br/>审<br/>意<br/>见</td>
									<td	colspan="5"><textarea rows="5" style="width:100%" name="technologyReason">${technologyReason}</textarea></td>
								</tr>
								<tr>
									<td colspan="5" style="text-align:right">提出部门&nbsp;&nbsp;<input type="text" name="technologyDepartment" id="technologyDepartment" value="${technologyDepartment}" onclick="selectObj('选择部门','technologyDepartment','DEPARTMENT_TREE')" style="width:90px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
									提出人&nbsp;&nbsp;<input type="text" name="technologyUser" id="technologyUser" value="${technologyUser}" onclick="selectObj('选择人员','technologyUser','MAN_DEPARTMENT_TREE')" style="width:90px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
									部门负责人&nbsp;&nbsp;<input type="text" name="technologyDepartmentPrincipal" id="technologyDepartmentPrincipal" value="${technologyDepartmentPrincipal}" onclick="selectObj('选择人员','technologyDepartmentPrincipal','MAN_DEPARTMENT_TREE')" style="width:90px;"/></td>
								</tr>
								<tr>
									<td>质量管理部会签/日期 </td>
									<td colspan="5">
										<textarea rows="5" style="width:100%" name="qualitySignResult">${qualitySignResult}</textarea>
										<div style="width:100%;margin-right:20px;margin-top:8px;text-align:right;">
											会签日期&nbsp;&nbsp;<input type="text" name="qualitySignDate" id="qualitySignDate" value="${qualitySignDate}" style="width:90px;"/>
										</div>
									</td>
									
								</tr>
								<tr>
									<td >公司主管领导意见/日期 </td>
									<td colspan="5">
										<textarea rows="5" style="width:100%" name="directorOpinion">${directorOpinion}</textarea>
										<div style="width:100%;margin-right:20px;margin-top:8px;text-align:right;">
											会签日期&nbsp;&nbsp;<input type="text" name="directorOpininDate" id="directorOpininDate" value="${directorOpininDate}" style="width:90px;" />
										</div>
									</td>
								</tr>
								<tr>
									<td style="text-align:center" rowspan="2">复检结论<br/>与终结</td>
									<td	colspan="5"><textarea rows="3" style="width:100%" name="reinspedionResult"></textarea></td>
								</tr>
								<tr>
									<td colspan="5" style="text-align:right">质检员&nbsp;&nbsp;<input type="text" name="qualityInspector" id="qualityInspector" value="${qualityInspector}" onclick="selectObj('选择人员','qualityInspector','MAN_DEPARTMENT_TREE')" style="width:90px;"/></td>
								</tr>
								<tr>
									<td colspan="6" style="padding: 6px;">
										<table class="form-table-border" id="defective-goods-composing-table"
											style="width: 100%">
											<caption style="font-weight: bold; padding:4px;">不合格损失鉴定</caption>
											<thead>
												<tr>
													<th style="width:120px;"></th>
													<th style="text-align:center;width:180px;">费用名称</th>
													<th style="text-align:center;width:120px;">部门</th>
													<th style="text-align:center;width:140px;">金额(元)</th>
													<th>简述</th>
												</tr>
											</thead>
											<tbody id="defective-goods-composing-table-body">
												${composingItems}
											</tbody>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="6" style="text-align:right">鉴定人&nbsp;&nbsp;<input type="text" type="text" name="identifier" id="identifier" value="${identifier}" onclick="selectObj('选择人员','identifier','MAN_DEPARTMENT_TREE')" style="width:90px;"/></td>
								</tr>
							</table>
						</form>
					</div>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>