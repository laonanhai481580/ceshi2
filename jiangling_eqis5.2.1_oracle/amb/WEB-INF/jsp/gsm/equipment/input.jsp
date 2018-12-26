<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.gsm.entity.InspectionMsaplan"%>
<%@page import="com.ambition.gsm.entity.InspectionPlan"%>
<%@page import="com.ambition.gsm.entity.GsmEquipmentMaintenance"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.norteksoft.product.api.entity.Option"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<link href="${ctx}/widgets/multiselect/jquery.multiselect.css" rel="stylesheet" type="text/css"/>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/multiselect/jquery.multiselect.zh-cn.js" type="text/javascript"></script>
	<style type="text/css"></style>
 	<script type="text/javascript">
	$(document).ready(function(){
		$('#purchaseTime').datepicker({changeMonth:true,changeYear:true}); 
		$(":input[isLogin]").bind("click",function(){
			selectObj("选择人",$("#"+this.id).prev().attr("name"),this.id,"loginName");
		});
		var attachments = [
		   				   {showInputId:'attachmentFiles',hiddenInputId:'calibration'}
		   			      ];
		parseDownloadFiles(attachments);
			var attachmentMap = {};
			for(var i=0;i<attachments.length;i++){
				attachmentMap[attachments[i].hiddenInputId] = true;
			}
		setTimeout(function(){$("#message").html("");},3000);
	});
	//选择检验人员
 	function selectObj(title,hiddenInputId,showInputId,treeValue){
		var acsSystemUrl = "${ctx}";
		popTree({ title : title,
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:treeValue?treeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId :hiddenInputId,
			showInputId : showInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	function submitForm(url){ 
		//提交前的条件
		if($("#measurementForm").valid()){
			var scrapDates = $("#datepicker1").val();
			if(scrapDates != null && scrapDates != ""){
				var scrapDate=new Date($("#datepicker1").val());
				var dateOfPurchase=new Date($("#datepicker2").val());
				if(Date.parse(dateOfPurchase)-Date.parse(scrapDate)>0){
					alert("<s:text name='采购日期大于报废日期！！'/>");
					return false;
				}   
			}
			$('#measurementForm').attr('action',url);
			$('#measurementForm').submit();
		}else{
			var error = $("#measurementForm").validate().errorList[0];
			$(error.element).focus();
		}
	}
	//选择部门
	function selectDept(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择部门',
			innerWidth : '400',
			treeType : 'DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'false',
			hiddenInputId : obj.id,
			showInputId : obj.id,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
			}
		});
	}
	function selectMaxAssets(obj){
		var value = obj.value;
		var params = {
				managerAssets : value
			};
		var url ="${gsmctx}/equipment/select-max.htm";
		$.post(url,params,function(result){
			if(result.error){
				alert(result.message);
			}else{
				$("#"+obj.id).val(result.value);
			}
		},'json');
		
	}
	//上传附件
	function uploadFiles1(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId
				
		});
	}
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val().length;
			if(files>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
			}
		}
	}
	</script>
	<%@ include file="input-script.jsp" %>
</head> 
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="equipment";
		var thirdMenu="allEquipment";
		var treeMenu="myEquipmentReportInput";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-equipment-report-menu.jsp" %>
	</div>	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="gsm_equipment_save">
					<button class="btn" onclick="window.location = '${gsmctx}/equipment/input.htm'"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<button class='btn' onclick="submitForm('${gsmctx}/equipment/savegsm.htm?saveType=input');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<button class='btn' onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>取消</span></span></button>				
				<span style="color: red;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
			</div>
			
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm">
					<input type="hidden" name="id" id="id" value="${id}"/>
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<caption style="height: 50px;font-size: 24px;font-weight: bold;"><s:text name="量检具信息"/></caption>
						<tr>
							<td>公司主体</td>
							<td><input id="companyMain" name="companyMain" value="${companyMain}"/></td>
							<td>所属事业部</td>
							<td>
								<input name="processSection" id="processSection" value=${processSection }></input>
							</td>
							<td>使用部门</td>
							<td><input id="devName" name="devName"  value="${devName}" onclick="selectDept(this);" readonly="readonly" class="{required:true,messages:{required:'必填'}}"/></td>
						</tr>
						<tr>
							<td>工序</td>
							<td><input id="workProducre" name="workProducre" value="${workProducre}"/></td>
							<td>安装地点</td>
							<td><input id="address" name="address" value="${address}"/></td>
							<td>责任人</td>
							<td>
								<input type="hidden" id="dutyLoginMan" name="dutyLoginMan" value="${dutyLoginMan}" />
								<input id="dutyMan" readonly="readonly" hiddenInputId="dutyLoginMan" name="dutyMan" value="${dutyMan}" isLogin="true" class="{required:true,messages:{required:'必填'}}"/>
							</td>
						</tr>
						<tr>
							<td>抄送</td>
							<td>
<%-- 								<input type="hidden" id="copyLoginMan" name="copyLoginMan" value="${copyLoginMan}"/> --%>
<%-- 								<input id="copyMan" readonly="readonly" hiddenInputId="copyLoginMan" name="copyMan" value="${copyMan}" isLogin="true"/> --%>
								<input style="float:left;width:200px" name="copyMan" id="copyMan" value="${copyMan}" />
	          	 				<input style="float:left;" type='hidden' name="copyLoginMan" id="copyLoginMan" value="${copyLoginMan}" />
	           					<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectPerson1('copyMan','copyLoginMan');"><span class="ui-icon ui-icon-search"  ></span></a>
	           					<a class="small-button-bg" style="margin-left:2px;" onclick="clearValue('copyMan','copyLoginMan')" href="javascript:void(0);" title="<s:text name='清空'/>">
	           					<span class="ui-icon ui-icon-trash" style='cursor:pointer;'></span></a>
							</td>
							<td>主管</td>
							<td>
								<input type="hidden" id="directorLogin" name="directorLogin" value="${directorLogin}" />
								<input id="director" readonly="readonly" hiddenInputId="directorLogin" name="director" value="${director}" isLogin="true" />
							</td>
							<td>联系方式</td>
							<td><input id="contact" name="contact" value="${contact}"/></td>
							
						</tr>
						<tr>
							<td>固定资产编号</td>
							<td>
								<input id="fixedAssets" name="fixedAssets" value="${fixedAssets}" />
							</td>
							<td>管理编号</td>
							<td>
								<input id="managerAssets" name="managerAssets" value="${managerAssets}" onchange="selectMaxAssets(this);"/>
							</td>
							<td>采购日期</td>
							<td><input id="purchaseTime" name="purchaseTime" readonly="readonly" value='<s:date name="purchaseTime" format="yyyy-MM-dd HH:mm"/>'/></td>
						</tr>
						<tr>
							<td>设备名称</td>
							<td>
								<input id="equipmentName" name="equipmentName" value="${equipmentName}"/>
							</td>
							<td>规格型号</td>
							<td><input id="equipmentModel" name="equipmentModel" value="${equipmentModel}" class="{required:true,messages:{required:'必填'}}"/></td>
							<td>测量范围</td>
							<td><input id="measuringRange" name="measuringRange" value="${measuringRange}"/></td>
						</tr>
						<tr>
							<td>精度/分度</td>
							<td><input id="accuracy" name="accuracy" value="${accuracy}"/></td>
							<td>制造商</td>
							<td><input id="manufacturer" name="manufacturer" value="${manufacturer}"/></td>
							<td>机身号</td>
							<td><input id="factoryNumber" name="factoryNumber" value="${factoryNumber}"/></td>
						</tr>
						<tr>
							<td>校准方式</td>
							<td>
								<s:select list="checkForms" 
								  theme="simple"
								  listKey="value" 
								  listValue="name" 
								  labelSeparator=""
								  name="checkMethod"
								  emptyOption="true"></s:select>
							</td>
							<td>分级</td>
							<td>
								<s:select list="measurement_level" 
								  theme="simple"
								  listKey="value" 
								  listValue="name" 
								  labelSeparator=""
								  name="equipmentLevel"
								  emptyOption="true"></s:select>
							</td>
							<td>频率（月）</td>
							<td>
<%-- 								<s:select list="testCycles"  --%>
<%-- 								  theme="simple" --%>
<%-- 								  listKey="value"  --%>
<%-- 								  listValue="name"  --%>
<%-- 								  labelSeparator="" --%>
<%-- 								  name="frequency" --%>
<%-- 								  emptyOption="true"></s:select> --%>
								<input id="frequency" name="frequency" value="${frequency}" class="{number:true}"/>
							</td>
						</tr>
						<tr>
							
							<td>量检具状态</td>
							<td>
								<s:select list="measurementStates" 
								  theme="simple"
								  listKey="value" 
								  listValue="name" 
								  labelSeparator=""
								  name="measurementState"
								  emptyOption="true"></s:select>
							</td>
							<td>停用备注</td>
							<td>
								<input id="remark2" name="remark2" value="${remark2}" />
							</td>
							<td>检验报告</td>
							<td>
								<input type="hidden" name="attachmentFilesHidden" value='${calibration}' ></input>
								<input type="hidden" id="calibration" name="calibration"  value='${calibration}' ></input>
								<button  class="btn" type="button" onclick="uploadFiles1('attachmentFiles','calibration');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
								<span id="attachmentFiles"></span>
								<a style="display: block; height:15px;float:left" class="class='btn'"  onclick="showPic(this);" href="javascript:void(0);" ></a>
							</td>
						</tr>
						<tr>
						
						
						<td>盘点人</td>
						<td><input id="stocktakingMan" name="stocktakingMan" value="${stocktakingMan}"/></td> 
 						<td>盘点时间</td>
						<td>
						<input id="stocktakingTime" name="stocktakingTime" readonly="readonly" value='<s:date name="stocktakingTime" format="yyyy-MM-dd HH:mm"/>'/></td> 
						</tr>
						<tr><td colspan="6" style="font-weight: bold;text-align: center;">校验记录</td></tr>
						<tr>
							<td colspan="6">
								<table class="form-table-border-left" style="width:100%;margin: auto;">
									<tr>
										<td>校验计划时间</td>
										<td>实际校验时间</td>
										<td>校验人</td>
										<td>校验状态</td>
										<td>校验结果</td>
										<td>备注</td>
									</tr>
									<s:iterator value="inspectionPlans">
									<tr>
										<td>
											<s:date name="inspectionPlanDate" format="yyyy-MM-dd HH:mm"/>
										</td>
										<td>
											<s:date name="actualInspectionDate" format="yyyy-MM-dd HH:mm"/>
										</td>
										<td>
											${inspectionPeople}
										</td>
										<td>
											${inspectionState}
										</td>
										<td>
											${checkResult}
										</td>
										<td>
											${remark}
										</td>
									</tr>
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
</html>