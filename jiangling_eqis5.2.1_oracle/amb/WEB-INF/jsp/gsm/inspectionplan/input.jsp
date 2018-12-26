<%@ page import="com.ambition.gsm.entity.GsmInspectionRecord"%>
<%@ page import="com.ambition.gsm.entity.InspectionRecordAttach"%>
<%@ page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<%Long init = 0L; %>
	<style>td{font:11pt Helvetica, Arial, sans-serif;}</style>
	<link href="${ctx}/widgets/swfupload/css/default.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="${ctx}/widgets/swfupload/css/button.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$('#productionTime','#measurementForm').datepicker({changeMonth:true,changeYear:true});
		$('#purchaseDate','#measurementForm').datepicker({changeMonth:true,changeYear:true});
		$('#storageDate','#measurementForm').datepicker({changeMonth:true,changeYear:true});
		$("#measurementForm").validate({
			rules: {
			measurementNo:{required: true},
			measurementName:{required: true},
			measurementSpecification:{required: true},
			useDept:{required: true}
		}});
	});
	function submitForm(url){
		var productionTime=new Date($("#productionTime").val());
		var purchaseDate=new Date($("#purchaseDate").val());
		var storageDate=new Date($("#storageDate").val());
		if(Date.parse(productionTime)-Date.parse(purchaseDate)>0){
			alert("<s:text name='生产日期应早于购入日期！！'/>");
			return false;
		}
		if(Date.parse(productionTime)-Date.parse(storageDate)>0){
			alert("<s:text name='生产日期应早于入库日期！！'/>");
			return false;
		}
		var params = getParams();
		$('#measurementForm').attr('action',url);
		$('#measurementForm').submit();
	}
	
	//获取表单的值
	function getParams(){
		var params = {};
		$("#opt-content form input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}

	function dateclick(obj){
		$(obj).datepicker({changeMonth:true,changeYear:true});
	}
	
	//导入
	function upload(id){
		var url = '${gsmctx}/inspectionplan/upload.htm?id=' + id;
		$.colorbox({href:url,iframe:true, innerWidth:500, innerHeight:300,
			overlayClose:false,
			title:"<s:text name='导入'/>",
			onClosed:function(){
				$.post("${gsmctx}/inspectionplan/get-attach-name.htm?id="+id+"",id,function(result){
					if(result.error){
						alert(result.message);
					}else{
						getAttachName(id,result);
					}
				},'json');
			}
		});
	}
	function getAttachName(id,result){
		var totalhtml='';
		for(var i=0;i<result.length;i++){
			var html = '';
			var obj = result[i];
			html="<a href='${gsmctx}/inspectionplan/exupload.htm?id="+obj.id+"'>"+obj.name+"</a>&nbsp;&nbsp;&nbsp;";
			html=html+"<a href='${gsmctx}/inspectionplan/uploaddelete.htm?id="+obj.id+"'><s:text name='删除'/></a><br/>";
			totalhtml=totalhtml+html;
		}
		$("#" + id + "_attachs").html(totalhtml);
	}
	
	//文件下载
	function downloadFile(id){
		location.href = "download_file.jsp?id="+id;
	}
	//选择人员
	function storagerClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='编辑人'/>",
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
	//选择部门
	function useDeptClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='选择部门'/>",
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
	</script>
</head>

<body style="font-size: 14px" onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
		var secMenu="inspectionPlanReport";
		var thirdMenu="myInspectionPlanInput";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-inspection-plan-input.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow: auto;">
			<div class="opt-btn">
				<button class='btn' onclick="submitForm('${gsmctx}/inspectionplan/save.htm?id=${id}')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
				<button class='btn' onclick="addrow();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name='制定抽检计划'/></span></span></button>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr><s:text name='删除成功'/></nobr></font></div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm" name="measurementForm">
					<input type="hidden" name="params.savetype" value="input"/>
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<caption style="height: 50px"><h2><s:text name='检定记录'/></h2></caption>
						<tr>
							<td><span style="color:red">*</span><s:text name='器具编号'/></td>
							<td><input name="measurementNo" disabled="disabled" value="${equipment.measurementNo}" ></input></td>
							<td><s:text name='器具类别'/></td>
							<td><s:select theme="simple" name="measurementType"  disabled=" true" list="measurementTypes"  listValue="value" listKey="name"></s:select></td>
							<td><s:text name='校检形式'/></td>
							<td><s:select theme="simple" name="checkForm"  disabled="true" list="checkForms"  listValue="value" listKey="name"></s:select></td>
						</tr>
						<tr>
							<td ><span style="color:red">*</span><s:text name='器具名称'/></td>
							<td><input name="measurementName"  disabled="disabled" value=${equipment.measurementName} ></input></td>
							<td ><span style="color:red">*</span><s:text name='器具型号/规格'/></td>
							<td><input name="measurementSpecification"  disabled="disabled" value=${equipment.measurementSpecification} ></input></td>
							<td ><s:text name='测量范围'/></td>
							<td><input name="measurementRange" disabled="disabled"  value=${equipment.measurementRange} ></input></td>
						</tr>
						<tr>
							<td ><s:text name='分值度'/></td>
							<td><input name="degreeOfScore" disabled="disabled" value=${equipment.degreeOfScore} ></input></td>
							<td ><s:text name='准确度'/></td>
							<td><input name="degreeOfAccuracy" disabled="disabled" value=${equipment.degreeOfAccuracy} ></input></td>
							<td ><s:text name='精度等级'/></td>
							<td><s:select theme="simple" disabled="true" name="precisionGrade" list="precisionGrades"  listValue="value" listKey="name"></s:select></td>
						</tr>
						
						<tr>
							<td ><s:text name='生产厂家'/></td>
							<td><input name="manufacturer" disabled="disabled" value=${equipment.manufacturer} ></input></td>
							<td ><s:text name='出厂编号'/></td>
							<td><input name="outFactoryNO" disabled="disabled" value=${equipment.outFactoryNO} ></input></td>
							<td ><s:text name='生产时间'/></td>
							<td ><input name="productionTime" disabled="disabled" id="productionTime"  readonly="readonly" class="line" value=${equipment.productionTime} ></input></td>
						</tr>
						
						<tr>
							<td ><s:text name='购入日期'/></td>
							<td ><input name="purchaseDate" disabled="disabled" id="purchaseDate"  readonly="readonly" class="line" value=${equipment.purchaseDate} ></input></td>
							<td ><s:text name='购入价格'/></td>
							<td><input name="purchasingAmount" disabled="disabled" value=${equipment.purchasingAmount} ></input></td>
							<td ><s:text name='采购员'/></td>
							<td><input name="buyer"  disabled="disabled" value=${equipment.buyer} ></input></td>
						</tr>
						
						<tr>
							<td ><s:text name='入库类别'/></td>
							<td><input name="storageType" disabled="disabled" value=${equipment.storageType} ></input></td>
							<td ><s:text name='入库时间'/></td>
							<td ><input name="storageDate" disabled="disabled" id="storageDate"  readonly="readonly" class="line" value=${equipment.storageDate} ></input></td>
							<td ><s:text name='入库人'/></td>
							<td><input name="storager" disabled="disabled" value="${equipment.storager}" id="storager" onclick="storagerClick(this);" ></input></td>
						</tr>
						
							<tr>
							<td ><s:text name='存放地点'/></td>
							<td><input name="storageLocation" disabled="disabled" value=${equipment.storageLocation} ></input></td>
							<td ><s:text name='管理类别'/></td>
							<td><s:select theme="simple" disabled="true" name="managementType" list="managementTypes"  listValue="value" listKey="name"></s:select></td>
							<td ><s:text name='分值类别'/> </td>
							<td><input name="legalType" disabled="disabled" value=${equipment.legalType} ></input></td>
						</tr>
						<tr>
							<td ><span style="color:red">*</span><s:text name='使用部门'/></td>
							<td><input name="useDept" disabled="disabled" value="${equipment.useDept}" id="useDept" onclick="useDeptClick(this);" ></input></td>
							<td ><s:text name='检定周期'/></td>
							<td><s:select theme="simple" disabled="true" name="testCycle" list="testCycles"  listValue="value" listKey="name"></s:select></td>
							<td ><s:text name='量具状态'/></td>
							<td><s:select theme="simple" disabled="true" name="measurementState" list="measurementStates"  listValue="value" listKey="name"></s:select></td>
						</tr>
						<tr>
							<td><s:text name='管理员'/></td>
							<td><input name="adminMan" id="adminMan" disabled="true" value="${equipment.adminMan}" onclick="storagerClick(this);"></input></td>
							<td><s:text name='计量员'/></td>
							<td><input name="measureMan" id="measureMan" disabled="true" value="${equipment.measureMan}" onclick="storagerClick(this);"></input></td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td colspan="6">
								<table class="form-table-border-left" id="list4"	style="width:100%">
									<tr>
										<td style="width:10%"><s:text name='使用人'/></td>
										<td style="width:15%"><s:text name='计划日期'/></td>
										<td style="width:15%"><s:text name='实际日期'/></td>
										<td style="width:10%"><s:text name='校验状态'/></td>
										<td style="width:10%"><s:text name='结果'/></td>
										<td style="width:10%"><s:text name='鉴定员'/></td>
										<td style="width:15%"><s:text name='附件'/></td>
										<td style="width:15%"><s:text name='是否为计划检定'/></td>
									</tr>
									<s:iterator value="inspectionRecords" id="inspectionRecord" var="inspectionRecord">
										<%
											ValueStack valueStack = (ValueStack)request.getAttribute("struts.valueStack");
											GsmInspectionRecord inspectionRecord = (GsmInspectionRecord)valueStack.findValue("inspectionRecord");
										%>
										<%if(inspectionRecord.getIsPlan()) {%>
										<tr>
											<input type="hidden" style="width:100%" name="params.id" value=<%=inspectionRecord.getId() %> ></input>
											<td style="width:10%"><input style="width:100%" name="params.user" value="${inspectionRecord.user}"  ></input></td>
											<td style="width:15%"><input style="width:100%" name="params.planDate" readonly="readonly" class="line" value=${inspectionRecord.planDate}></input></td>
											<td style="width:15%"><input style="width:100%"  id="datepicker<%=inspectionRecord.getId()%>"  readonly="readonly" name="params.actualDate"  class="line" value=${inspectionRecord.actualDate} ></input></td>
											<script type="text/javascript">
												$('#datepicker<%=inspectionRecord.getId()%>','#measurementForm').datepicker({changeMonth:true,changeYear:true});
											</script>
											<td style="width:10%">
												<select   name="params.checkState"  value="${inspectionRecord.checkState}">
													<option value="合格"<s:if test="%{#inspectionRecord.checkState=='合格'}">selected="selected"</s:if>><s:text name='gsm.inspectionplan.input.xzzd36'/></option>
													<option value="不合格" <s:if test="%{#inspectionRecord.checkState=='不合格'}">selected="selected"</s:if>><s:text name='gsm.inspectionplan.input.xzzd37'/></option>
												</select>
											</td>
											<td style="width:10%"><s:select theme="simple" name="params.resultState" emptyOption="true"  list="resultStates"  listValue="value" listKey="name" value="resultState"></s:select></td>
											<td style="width:10%"><input style="width:100%" name="params.surveyor"  value=${inspectionRecord.surveyor} ></input></td>
											<td style="width:15%">
												<div id="<%=inspectionRecord.getId() %>_attachs">
													<%
													init = inspectionRecord.getId();
													List<InspectionRecordAttach> attachlist=inspectionRecord.getInspectionRecordAttachs();
													for(int i=0;i<attachlist.size();i++){
														InspectionRecordAttach inspectionRecordAttach=(InspectionRecordAttach)attachlist.get(i);
														out.write("<a href='");
													%>
														${gsmctx}
													<%
													out.write("/inspectionplan/exupload.htm?id="+inspectionRecordAttach.getId()+"'>"+inspectionRecordAttach.getAttachname()+"</a>&nbsp;&nbsp;&nbsp;");
													out.write("<a href='");
													%>
														${gsmctx}
													<%
														out.write("/inspectionplan/uploaddelete.htm?id="+inspectionRecordAttach.getId()+"'><s:text name='gsm.inspectionplan.input.xzzd38'/></a><br/>");
													}%>
												</div>
												<button class='btn' type='button' onclick="upload(<%=inspectionRecord.getId() %>);"><span><span><b class="btn-icons btn-icons-upload"></b><s:text name='gsm.inspectionplan.input.xzzd39'/></span></span></button>
											</td>
											<td style="width:15%"><input style="width:100%" id="isPlan" name="params.isPlan" value="<%=inspectionRecord.getIsPlan()==true?"是":"否" %>" readonly="readonly"></input></td>
										</tr>
										<%}else{ %>
										<tr>
											<input type="hidden" style="width:100%" name="params.simpleid" value=<%=inspectionRecord.getId() %> ></input>
											<td style="width:10%"><input style="width:100%" name="params.simpleuser" value="${inspectionRecord.user}" onclick=""></input></td>
											<td style="width:15%"></td>
											<td style="width:15%"><input style="width:100%"  id="datepicker<%=inspectionRecord.getId()%>" name="params.simpleactualDate"  class="line" value=${inspectionRecord.actualDate} ></input></td>
											<script type="text/javascript">
												$('#datepicker<%=inspectionRecord.getId()%>','#measurementForm').datepicker({changeMonth:true,changeYear:true});
											</script>
											<td style="width:10%">
												<select name="params.simplecheckState"  value="${inspectionRecord.checkState}">
													<option value="合格"<s:if test="%{#inspectionRecord.checkState=='合格'}">selected="selected"</s:if>><s:text name='合格'/></option>
													<option value="不合格" <s:if test="%{#inspectionRecord.checkState=='不合格'}">selected="selected"</s:if>><s:text name='不合格'/></option>
												</select>
											</td>
											<td style="width:10%"><s:select theme="simple" name="params.resultState"  list="resultStates" emptyOption="true" listValue="value" listKey="name" ></s:select></td>
											<td style="width:10%"><input style="width:100%" name="params.simplesurveyor"  value=${inspectionRecord.surveyor} ></input></td>
											<td style="width:15%">
												<div id="<%=inspectionRecord.getId() %>_attachs">
												<%
												List<InspectionRecordAttach> attachlist=inspectionRecord.getInspectionRecordAttachs();
												for(int i=0;i<attachlist.size();i++){
													InspectionRecordAttach inspectionRecordAttach=(InspectionRecordAttach)attachlist.get(i);
													out.write("<a href='");
												%>
													${gsmctx}
													<%
														out.write("/inspectionplan/exupload.htm?id="+inspectionRecordAttach.getId()+"'>"+inspectionRecordAttach.getAttachname()+"</a>&nbsp;&nbsp;&nbsp;");
														out.write("<a href='");
													%>
													${gsmctx}
													<%
													out.write("/inspectionplan/uploaddelete.htm?id="+inspectionRecordAttach.getId()+"'><s:text name='删除'/></a><br/>");
												}%>
												</div>
												<button class='btn' type='button' onclick="upload(<%=inspectionRecord.getId() %>);"><span><span><b class="btn-icons btn-icons-upload"></b><s:text name='上传附件'/></span></span></button>
											</td>
											<td style="width:15%"><input style="width:100%" id="isPlan" name="params.simpleisPlan"  value="<%=inspectionRecord.getIsPlan()==true?"是":"否" %>" readonly="readonly"></input></td>
										</tr>
										<%} %>
									</s:iterator>
								</table>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var flag=<%=init%>+1;
	function addrow(){
		var str='<tr>'+'<td style="width:10%"><input style="width:100%" name="params.simpleuser"  ></input></td>'
				+'<td style="width:15%"></td>'
				+'<td style="width:15%"><input style="width:100%" name="params.simpleactualDate" id="datepicker'+flag+'"" onclick="dateclick();" class="line" readonly="readonly" ></input></td>'
				+"<td style='width:10%'><select name='params.simplecheckState' ><option value='合格'><s:text name='合格'/></option><option value='不合格' ><s:text name='不合格'/></option></select></td>"
				+'<td style="width:10%"><select name="params.resultState" id="params_resultState">'
				+'<option value=""></option>'
				+"<option value='按时完成'><s:text name='按时完成'/></option>"
				+"<option value='进行中'><s:text name='进行中'/></option>"
				+"<option value='超期未完成'><s:text name='超期未完成'/></option>"
				+"<option value='超期完成'><s:text name='超期完成'/></option>"
				+'</select>'
				+'<td style="width:10%"><input style="width:100%" name="params.simplesurveyor"   ></input></td>'
				+'<td style="width:15%"><input style="width:100%" name="params.simpleattach"  ></input></td>'
				+'<td style="width:15%"><input  name="params.simpleisPlan" value="否" readonly="readonly"></input></td>'
				+'</tr>';
		$("#list4").append(str);
		$('#datepicker'+flag,'#measurementForm').datepicker({changeMonth:true,changeYear:true});
		flag++;
	};
</script>
</html>