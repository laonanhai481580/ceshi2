<%@page import="com.ambition.supplier.entity.SupplierLinkMan"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%@include file="edit_header.jsp"%>
<script type="text/javascript">
    isUsingComonLayout=false;
	var treeMenu='company-info';
	function setInspectionBomValue(datas){
		alert("input");
	}
</script>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<form action="" method="post" id="form" name="form">
		<div id="opt-content" class="form-bg" style="height:100%;">
			<div id="tabs" style="height:100%;">
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
							<td>
								<div class="content" style="height:40px;overflow:auto;">
									<table class="form-table-border-left">
									<tr>
										<td width="20%" style="">基本信息</td>
										<td colspan="3">
							              <input type="hidden" name="baseInfoFile" id="baseInfoFile" value='${evaluate.baseInfoFile}'></input>
							              <span id='baseInfoFileHidden'></span>
											</td>
									</tr>
									<tr>
										<td width="20%" style="">三证</td>
										<td colspan="3">
							              <input type="hidden" name="threePapersFile" id="threePapersFile" value='${evaluate.threePapersFile}'></input>
							              <span id='threePapersFileHidden'></span>
											</td>
									</tr>
									<tr>
										<td width="20%" style="">廉洁协议</td>
										<td colspan="3">
							              <input type="hidden" name="integrityAgreementFile" id="integrityAgreementFile" value='${evaluate.integrityAgreementFile}'></input>
							              <span id='integrityAgreementFileHidden'></span>
											</td>
									</tr>
									<tr>
										<td width="20%" style="">样品评估表</td>
										<td colspan="3">
							              <input type="hidden" name="sampleEvaluateFile" id="sampleEvaluateFile" value='${evaluate.sampleEvaluateFile}'></input>
							              <span id='sampleEvaluateFileHidden'></span>
											</td>
									</tr>
									<tr>
										<td width="20%" style="">审厂资料</td>
										<td colspan="3">
							              <input type="hidden" name="factoryAuditFile" id="factoryAuditFile" value='${evaluate.factoryAuditFile}'></input>
							              <span id='factoryAuditFileHidden'></span>
											</td>
									</tr>
									<tr>
										<td width="20%" style="">采购框架协议</td>
										<td colspan="3">
							              <input type="hidden" name="purchasingFile" id="purchasingFile" value='${evaluate.purchasingFile}'></input>
							              <span id='purchasingFileHidden'></span>
											</td>
									</tr>
									<tr>
										<td width="20%" style="">供应商竞争力分析表</td>
										<td colspan="3">
							              <input type="hidden" name="supplierAnalyzeFile" id="supplierAnalyzeFile" value='${evaluate.supplierAnalyzeFile}'></input>
							              <span id='supplierAnalyzeFileHidden'></span>
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
</html>