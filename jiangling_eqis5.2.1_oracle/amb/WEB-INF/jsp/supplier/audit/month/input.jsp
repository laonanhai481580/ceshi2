<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@ include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/jquery-ui-1.8.7.js"></script>
	
	<!-- 表单和流程常用的方法封装 -->
	<script type="text/javascript">
	$(document).ready(function(){
		if('${saveSucc}'=='true'){
			if(window.parent){
				window.parent.$.colorbox.close();
				window.parent.$("#supplier").trigger("reloadGrid");
				return;				
			}
		}
		$("input[name='firstCheckDate']").datepicker({
			changeMonth : true,
			changeYear : true
		});
		$("input[name='secondCheckDate']").datepicker({
			changeMonth : true,
			changeYear : true
		});
		var attachments = [
		   				{showInputId:'checkFileShow',hiddenInputId:'checkFile'}
		   			];
		parseDownloadFiles(attachments);
	});
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
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId,
			callback:function(files){
				if(showId=="showbad"){
					for(var i=0;i<files.length;i++){
						if(i==0){
							$("#pices0" ).attr("src",$.getDownloadPath(files[0].id));
							$("#pices0" ).attr("alt",$.getDownloadPath(files[0].fileName));
						}
						
					}
				}
				
			}
		});
	}
    

	  function saveFormBoxClose(url){
// 			if(!$('#inputForm').valid()){
// 				var error = $("#inputForm").validate().errorList[0];
// 				$(error.element).focus();
// 				return;
// 			}
			var startScore = $("#scoreStart").val();
			var endScore = $("#scoreEnd").val();
			var str = startScore + "<=评价分"+  "<=" + endScore;
			$("#scoreArea").val(str);
			$(".opt-btn").find("button.btn").attr("disabled",true);
			$.showMessage("保存中......");
			$("#inputForm").attr("action",url).submit();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="supplier-audit-save">
					  <button class='btn' id="btnsave" type="button" onclick="saveFormBoxClose('${supplierctx}/audit/year/save.htm');">
								<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
					  </button>
				</security:authorize>
				<span style="color:red;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
			</div>
			<div id="opt-content" style="text-align: center;">
				<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
						<div style="">&nbsp;</div>
				</div>
				<form id="inputForm" name="inputForm" method="post" action="">
				   <input name="id" value="${id }" type="hidden" id="id"/>
				   <input name="supplierId" value="${supplierId }" type="hidden" id="supplierId"/>
					<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					    <tr>
					      <td>供应商名称</td>
					      <td>
					          <input  name="supplierName" id="supplierName" value="${supplierName}" readonly="readonly"/>
					      </td>
					      <td>供货事业部</td>
					        <td>
					          <input  name="supplyFactory" id="supplyFactory" value="${supplyFactory}" />
					      </td>
					      <td>供应物料</td>
					        <td>
					          <input  name="supplyMaterial" id="supplyMaterial" value="${supplyMaterial}" readonly="readonly"/>
					      </td>
					    </tr>
					    <tr>   
					        <td colspan='6'>
					         1计划: <input style="width:80px;" name="firstDayPlan" id="firstDayPlan" value="${firstDayPlan}" />&nbsp;&nbsp;&nbsp;
					         1实际:<input style="width:80px;" name="firstDayDesign" id="firstDayDesign" value="${firstDayDesign}" />&nbsp;&nbsp;&nbsp;
					         2月计划:<input style="width:80px;" name="secondeDayPlan" id="secondeDayPlan" value="${secondeDayPlan}" />&nbsp;&nbsp;&nbsp;
					         2月实际:<input style="width:80px;" name="secondeDayDesign" id="secondeDayDesign" value="${secondeDayDesign}" />&nbsp;&nbsp;&nbsp;
					                      年份:<input style="width:80px;" name="year" id="year" value="${year}" readonly="readonly"/>
					                      月份:<input style="width:80px;" name="monthOfYear" id="monthOfYear" value="${monthOfYear}" readonly="readonly"/>
					      </td>
					    </tr>
					    <tr>
					       <td colspan='6'>
					         3月计划: <input style="width:80px;" name="marchPlan" id="marchPlan" value="${marchPlan}" />&nbsp;&nbsp;&nbsp;
					         3月实际:<input style="width:80px;" name="marchDesign" id="marchDesign" value="${marchDesign}" />&nbsp;&nbsp;&nbsp;
					         4月计划:<input style="width:80px;" name="aprilPlan" id="aprilPlan" value="${aprilPlan}" />&nbsp;&nbsp;&nbsp;
					         4月实际:<input style="width:80px;" name="aprilDesign" id="aprilDesign" value="${aprilDesign}" />&nbsp;&nbsp;&nbsp;
					       </td>
					    </tr>
					    <tr>
					       <td colspan='6'>
					         5月计划: <input style="width:80px;" name="mayPlan" id="mayPlan" value="${mayPlan}" />&nbsp;&nbsp;&nbsp;
					         5月实际:<input style="width:80px;" name="mayDesign" id="mayDesign" value="${mayDesign}" />&nbsp;&nbsp;&nbsp;
					         6月计划:<input style="width:80px;" name="junePlan" id="junePlan" value="${junePlan}" />&nbsp;&nbsp;&nbsp;
					         6月实际:<input style="width:80px;" name="juneDesign" id="juneDesign" value="${juneDesign}" />&nbsp;&nbsp;&nbsp;
					       </td>
					    </tr>
					     <tr>
					       <td colspan='6'>
					         7月计划: <input style="width:80px;" name="julyPlan" id="julyPlan" value="${julyPlan}" />&nbsp;&nbsp;&nbsp;
					         7月实际:<input style="width:80px;" name="julyDesign" id="julyDesign" value="${julyDesign}" />&nbsp;&nbsp;&nbsp;
					         8月计划:<input style="width:80px;" name="augustPlan" id="augustPlan" value="${augustPlan}" />&nbsp;&nbsp;&nbsp;
					         8月实际:<input style="width:80px;" name="augustDesign" id="augustDesign" value="${augustDesign}" />&nbsp;&nbsp;&nbsp;
					       </td>
					    </tr>
					    <tr>
					       <td colspan='6'>
					         9月计划: <input style="width:80px;" name="septemberPlan" id="septemberPlan" value="${septemberPlan}" />&nbsp;&nbsp;&nbsp;
					         9月实际:<input style="width:80px;" name="septemberDesign" id="septemberDesign" value="${septemberDesign}" />&nbsp;&nbsp;&nbsp;
					         10月计划:<input style="width:80px;" name="octoberPlan" id="octoberPlan" value="${octoberPlan}" />&nbsp;&nbsp;&nbsp;
					         10月实际:<input style="width:80px;" name="octoberDesign" id="octoberDesign" value="${octoberDesign}" />&nbsp;&nbsp;&nbsp;
					       </td>
					    </tr>
					      <tr>
					       <td colspan='6'>
					         11月计划: <input style="width:80px;" name="novemberPlan" id="novemberPlan" value="${novemberPlan}" />&nbsp;&nbsp;&nbsp;
					         11月实际:<input style="width:80px;" name="novemberDesign" id="novemberDesign" value="${novemberDesign}" />&nbsp;&nbsp;&nbsp;
					         12月计划:<input style="width:80px;" name="decemberPlan" id="decemberPlan" value="${decemberPlan}" />&nbsp;&nbsp;&nbsp;
					         12月实际:<input style="width:80px;" name="decemberDesign" id="decemberDesign" value="${decemberDesign}" />&nbsp;&nbsp;&nbsp;
					       </td>
					    </tr>
					    <tr>
					       <td>首次稽核日期</td>
					       <td><input  name="firstCheckDate" id="firstCheckDate" value="<s:date name='firstCheckDate' format="yyyy-MM-dd"/>" /></td>
					       <td>二次稽核日期</td>
					       <td><input  name="secondCheckDate" id="secondCheckDate" value="<s:date name='secondCheckDate' format="yyyy-MM-dd"/>" /></td>
					       <td>最终稽核结果</td>
					       <td><input  name="finalCheckResult" id="finalCheckResult" value="${finalCheckResult}" /></td>
                        </tr>
                          <tr>
					       <td>确认人</td>
					       <td><input  name="checker" id="checker" value="${checker }" /></td>
					       <td>稽核报告</td>
					       <td><button class='btn' type="button" onclick="uploadFiles('checkFileShow','checkFile');"><span><span><b class="btn-icons btn-icons-upload"></b>上传照片</span></span></button>
					          <input type="hidden" name="hisAttachmentFilesFile" value='${checkFile}'></input>
					          <input type="hidden" name="checkFile" id="checkFile" value='${checkFile}'></input>
					          <span id='checkFileShow'></span></td>
					       <td>备注</td>
					       <td><input  name="remark" id="remark" value="${remark}" /></td>
                        </tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>