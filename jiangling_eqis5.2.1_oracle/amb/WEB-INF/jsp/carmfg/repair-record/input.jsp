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
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object> 
	<script type="text/javascript">
	$(document).ready(function(){
		$("#datepicker1").datetimepicker({changeYear:true,changeMonth:true});
		//添加验证
		$("#cpForm").validate({});
	});
	
	function submitForm(url){
// 		alert("dd");
        if($('#cpForm').valid()){
		var params = getParams();
		$.post(url,params,function(result){
		if(result.error){
		$("#message").html(result.message);
		
		} else {

	$("#message").html("保存成功！");
					}
					showMsg();
				}, 'json');
			}
		}
	
	//获取表单的值
	function getParams() {
		var params = {};
		$(":input", "#opt-content").each(function(index, obj) {
			var jObj = $(obj);
			if (obj.name && jObj.val()) {
				if (obj.type == 'checkbox') {
					if (obj.checked) {
						params[obj.name] = jObj.val();
					}
				} else {
					params[obj.name] = jObj.val();
				}
			}
		});
		return params;
	}

	//选择维修部门
	function selectDept(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择维修部门',
			innerWidth:'400',
			treeType:'DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.id,
			showInputId:obj.id,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	//选择人员
	function selectPerson(obj,title){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"选择维修人员",
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.id,
			showInputId:obj.id,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	function goback() {
		window.location = "${mfgctx}/data-acquisition/repair-record-report-input.htm";
	}

 </script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu = "data_acquisition";
	var thirdMenu = "repair_record_report_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	 <div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-data-acquisition-menu.jsp" %>
</div>
     <div class="ui-layout-center" >
			<div class="opt-body" style="overflow:auto;">
				<div class="opt-btn" id="btnDiv">
					<button class='btn' type="button" onclick="submitForm('${mfgctx}/data-acquisition/save.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					<button class='btn' type="button" onclick="goback();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
					<span style="display: none;color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
				</div>
				<div id="opt-content" style="text-align: center;">
					<form action="" method="post" id="cpForm" name="cpForm">
					<input type="hidden" name="id" value="${repairRecordReport.id}"/>
					<input type="hidden" name="params.saveType" value="input"/>
					<div style="height: 30px;text-align: center"><h2>维修记录表</h2></div>
					<table class="form-table-border-left" style="width:90%;margin: auto;">
				<tr>
							<td colspan="1"><span style="color:red;">*</span>日期</td>
							<td><input type="text" id="datepicker1" name="repairDate" class="{required:true,messages:{required:'必填'}}"   value='<s:date name="repairDate" format="yyyy-MM-dd"/>' readonly="readonly"/></td>
							<td><span style="color:red;">*</span>维修部门</td>
							<td><input id="repairDept" name="repairDept"  type="text" value="${repairDept}" class="{required:true,messages:{required:'必填'}}"  readonly="readonly"   onclick="selectDept(this)"/>
							</td>
							<td><span style="color:red;">*</span>维修人员</td>
							<td><input id="repairPerson" name="repairPerson" type="text" value="${repairPerson}" class="{required:true,messages:{required:'必填'}}"  onclick="selectPerson(this)"/></td>
						    <td><span style="color:red;">*</span>工单/反馈单号</td>
						    <td><input id="reportNo" name="reportNo" type="text" value="${reportNo}" class="{required:true,messages:{required:'必填'}}" /></td>
							</tr>
				    <tr>
					<td><span style="color:red;">*</span>不良来源</td>
					<td colspan="1"><input name="badSource" type="radio" value="售后" <s:if test="infoSource==售后">checked</s:if>/>售后<input name="badSource" type="radio" value="生产" <s:if test="infoSource==生产">checked</s:if>/>生产</td>
					<td><span style="color:red;">*</span>产品型号</td>
					<td><input name="productModel" value="${productModel}"  class="{required:true,messages:{required:'必填'}}" /></td>
					</tr>
					<tr>
				    <td><span style="color:red;">*</span>不良现象</td>
					<td colspan="7"><textarea id="badPhenomenon" class="{required:true,messages:{required:'必填'}}"  name="badPhenomenon">${badPhenomenon}</textarea></td>	
					</tr>
                     <tr>
                     <td><span style="color:red;">*</span>不良原因</td>
                     <td colspan="7"><textarea id="badCause" class="{required:true,messages:{required:'必填'}}"  name="badCause">${badCause}</textarea></td>
                     </tr>
                     <tr>
                       <td><span style="color:red;">*</span>处置措施</td>
                     <td colspan="7"><textarea id="disposalMeasure" name="disposalMeasure" class="{required:true,messages:{required:'必填'}}" >${disposalMeasure}</textarea></td>	
                     </tr>	
                   <tr>
                     <td><span style="color:red;">*</span>不良部位</td>
                     <td colspan="7"><textarea id="badPosition" name="badPosition" class="{required:true,messages:{required:'必填'}}" >${badPosition}</textarea></td>	
                     </tr>
                       <tr>
                      <td><span style="color:red;">*</span>更换物料</td>
                     <td colspan="7"><textarea id="replaceMaterial" name="replaceMaterial" class="{required:true,messages:{required:'必填'}}" >${replaceMaterial}</textarea></td>	
                     </tr>
                     <tr>
                     <td><span style="color:red;">*</span>更换物料数</td>
                     <td><input name="replaceMaterialAmount" value="${replaceMaterialAmount}"  class="{digits:true,messages:{required:'请输入数字'}}" /></td>
                     <td><span style="color:red;">*</span>报废产品数</td>
                     <td><input name="rejectProductAmount" value="${rejectProductAmount}"  class="{digits:true,messages:{required:'请输入数字'}}" /></td>
                      <td><span style="color:red;">*</span>工时（小时）</td>
                      <td><input name="workHours" value="${workHours}"  class="{digits:true,messages:{required:'请输入数字'}}" /></td>
                     </tr>
                     <tr>
                     <td><span style="color:red;">*</span>材料费</td>
                     <td><input name="materialCost" value="${materialCost}" class="{digits:true,messages:{required:'请输入数字'}}"/></td>  
                     <td><span style="color:red;">*</span>产品费</td>
                     <td><input name="productCost" value="${productCost}"  class="{digits:true,messages:{required:'请输入数字'}}"/></td>  
                      <td><span style="color:red;">*</span>工时费</td>
                     <td><input name="workHoursCost" value="${workHoursCost}" class="{digits:true,messages:{required:'请输入数字'}}"/></td>  
                     <td><span style="color:red;">*</span>维修费合计</td>
                     <td><input name="repairTotalCost" value="${repairTotalCost}"  class="{digits:true,messages:{required:'请输入数字'}}"/></td>  
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