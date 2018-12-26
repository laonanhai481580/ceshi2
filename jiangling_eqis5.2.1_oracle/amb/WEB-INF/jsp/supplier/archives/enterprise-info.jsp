<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%@include file="edit_header.jsp"%>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		isUsingComonLayout=false;
		var treeMenu='enterprise-info';
	</script>
	<form action="" method="post" id="form" name="form">
		<div id="opt-content" class="form-bg">
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">基本信息</a>
					</li>
					<li><a href="#tabs-2">供应产品</a>
					</li>
					<li><a href="#tabs-3">体系证书</a>
					</li>
				</ul>
				<div id="tabs-1" style="padding:0px;">
					<table style="width:100%;margin:0px;padding:0px;">
						<tr>
							<td style="width:120px;" valign="top">
								<div id="treeDiv" style="padding-top: 4px;"></div>
							</td>
							<td style="width:6px;background:#AED5F9;">
								&nbsp;
							</td>
							<td>
								<div class="opt-body opt-btn" style="line-height: 40px;">
									<button class='btn' onclick="saveSupplier();">
										<span><span><b class="btn-icons btn-icons-save"></b>保存</span></span>
									</button>
									<button class='btn' type="reset">
										<span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span>
									</button>
									<span id="message"
										style="color: red; padding-left: 4px; font-weight: bold;"></span>
								</div>
								<div class="content" style="height:40px;overflow:auto;">
									<table class="form-table-border-left">
										<tr>
											<td colspan="2" style="font-weight: bold;">企业情况</td>
										</tr>
										<tr>
											<td colspan="2"><textarea rows="4"
													name="enterpriseDescription" style="width: 100%;"
													class="{maxlength:1000}">${supplier.enterpriseDescription}</textarea>
											</td>
										</tr>
										<tr>
											<td colspan="2" style="font-weight: bold;">主要生产设备、模具、器具等工艺装备及工艺流程</td>
										</tr>
										<tr>
											<td width="20%" style="">设备情况</td>
											<td width="80%"><textarea rows="2"
													name="equipmentDescription" style="width: 100%;"
													class="{maxlength:255}">${supplier.equipmentDescription}</textarea>
											</td>
										</tr>
										<tr>
											<td style="">模具夹具等工艺装备情况</td>
											<td><textarea rows="2" name="toolingDescription"
													style="width: 100%;" class="{maxlength:255}">${supplier.toolingDescription}</textarea>
											</td>
										</tr>
										<tr>
											<td style="">主要工艺流程（关键重要工序）</td>
											<td><textarea rows="2" name="flowDescription"
													style="width: 100%;" class="{maxlength:255}">${supplier.flowDescription}</textarea>
											</td>
										</tr>
										<tr>
											<td colspan="2" style="font-weight: bold;">质量保证</td>
										</tr>
										<tr>
											<td colspan="2"><textarea rows="2" name="qualityAssurance"
													style="width: 100%;" class="{maxlength:255}">${supplier.qualityAssurance}</textarea>
											</td>
										</tr>
										<tr>
											<td colspan="2" style="font-weight: bold;">工厂管理</td>
										</tr>
										<tr>
											<td colspan="2"><textarea rows="2"
													name="manageDescription" style="width: 100%;"
													class="{maxlength:255}">${supplier.manageDescription}</textarea>
											</td>
										</tr>
										<tr>
											<td colspan="2" style="font-weight: bold;">技术改造情况</td>
										</tr>
										<tr>
											<td colspan="2"><textarea rows="2"
													name="techTransDes"
													style="width: 100%;" class="{maxlength:255}">${supplier.techTransDes}</textarea>
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<%@include file="edit_grid.jsp"%>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
</html>