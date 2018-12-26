<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%@include file="scripts.jsp"%>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="bom";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-base-info-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<table style="width:100%;height:100%;" cellpadding="0" cellspacing="0" id="top">
				<tr>
					<td valign="top" style="width:80px;">
						<div class="opt-btn" style="line-height:28px;">
							<table style="margin:0px;padding:0px;" >
								<tr>
									<td>质量特性</td>
								</tr>
							</table>
						</div>
						<div class="opt-body" id="qualityFeature" style="padding:6px;width:200px;overflow:auto;"></div>
					</td>
					<td style="width:6px;border-left:1px solid #99bbe8;border-right:1px solid #99bbe8;"></td>
					<td valign="top">
						<form id="contentForm" name="contentForm" method="post" action="">
							<div id="checkGradeToolbar" class="opt-btn" style="line-height:28px;">
								<security:authorize ifAnyGranted="spc_base-info_process-define_save">
									<button class="btn" type="button" onclick="submitForm('${spcctx}/base-info/process-define/save.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
								</security:authorize>
								<button class="btn" type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
								<security:authorize ifAnyGranted="spc_base-info_process-define_copy">
									<button class="btn" type="button" onclick="copyFeature()"><span><span><b class="btn-icons btn-icons-copy"></b>复制</span></span></button>
								</security:authorize> 
								<span id="message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
							</div>
							<div class="opt-body" style="padding:6px;border:0px;">
								<div id="tabs" style="height:100%;">
									<ul>
										<li><a href="#tabs-1">基本信息</a>
										</li>
										<li><a href="#tabs-2">控制限</a>
										</li>
										<li><a href="#tabs-3">判断准则</a>
										</li>
										<li><a href="#tabs-4">层别信息</a>
										</li>
										<li><a href="#tabs-5">异常通知人员</a>
										</li>
									</ul>
									<%@include file="grids.jsp"%>
								</div>
							</div>
						</form>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>