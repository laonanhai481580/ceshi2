<%@page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.entity.AbnormalReason"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	
	<%String typeId = Struts2Utils.getParameter("typeId");%>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#contentForm").validate();
		});
	
		function selectMeasures(obj){
			$.colorbox({href:"${spcctx}/base-info/improve-measure/measures-select.htm",
				iframe:true, 
				innerWidth:750, 
				innerHeight:500,	
				overlayClose:false,	
				title:"选择改善措施"
			});
	 	}
		
		function setMeasureValue(datas){
			var measure="";
			for(var i=0;i<datas.length;i++){
				var obj = datas[i];
				measure+=obj.measure+',';
			}
			measure=measure.substring(0, measure.length-1);
			$('#measures').val(measure);
		}
		
		function submitForm(url){
			$('#contentForm').attr('action',url);
			$('#contentForm').submit();
		}	
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div class="opt-btn" id="btnDiv">
				<security:authorize ifAnyGranted="spc_base-info_abnormal-reason_save">
					<button class='btn' onclick="submitForm('${spcctx}/base-info/abnormal-reason/save.htm?typeId=<%=typeId%>');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<button class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
					<span id="message" style="color: red; padding-left: 4px; font-weight: bold;"><s:actionmessage theme="mytheme"/></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post">
						<div><input type="hidden" name="id" value="${id}"></input></div>
						<table id="limit_table" style="width:100%;">
							<tr>
								<td style="width:15%;text-align:right;">原因类型：</td>
								<td>${reasonType }</td>
							</tr>
							<tr>
								<td style="width:15%;text-align:right;"><span style="color:red;">*</span>原因代码：</td>
								<td><input name="reasonNo" value="${reasonNo }" style="width:90%;" class="{required:true,messages:{required:'原因代码必填!'}}"/></td>
							</tr>
							<tr>
								<td style="width:15%;text-align:right;"><span style="color:red;">*</span>原因描述：</td>
								<td><textarea name="reason" style="width:90%;" rows="10" class="{required:true,messages:{required:'原因描述必填!'}}">${reason }</textarea></td>
							</tr>
							<tr>
								<td style="width:15%;text-align:right;">改善措施：</td>
								<td><textarea name="measures" id="measures" style="width:90%;" rows="10" >${measures }</textarea>
									<security:authorize ifAnyGranted="spc_base-info_improve-measure_measures-select">
										<a class="small-button-bg" style="vertical-align: top;" onclick="selectMeasures(this);" href="javascript:void(0);" title="添加改善措施"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
									</security:authorize>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>