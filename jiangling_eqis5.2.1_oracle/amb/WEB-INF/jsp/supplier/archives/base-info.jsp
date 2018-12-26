<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<%@include file="edit_header.jsp"%>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
	    isUsingComonLayout=false;
		var treeMenu='base-info';
		$(document).ready(function(){
			$("#createDate").datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
			$("#gaizhiDate").datepicker({"dateFormat":'yy-mm-dd',changeMonth:true,changeYear:true});
		});
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
											<td width="20%" style="">法人代表</td>
											<td width="30%"><input type="text" name="legalPerson"
												style="" value="${supplier.legalPerson}" /></td>
											<td width="20%" style="">企业性质</td>
											<td width="30%"><s:select list="enterprisePropertys"
													theme="simple" listKey="name" listValue="name"
													value="supplier.enterpriseProperty" name="enterpriseProperty"
													labelSeparator="" cssStyle="width:155px;"></s:select></td>
										</tr>
										<tr>
											<td style="">隶属关系</td>
											<td><input type="text" name="membership" style=""
												value="${supplier.membership}" /></td>
											<td style="">创立时间</td>
											<td><input type="text" name="createDate" style=""
												value="${supplier.createDateStr}" id="createDate" /></td>
										</tr>
										<tr>
											<td style="">改制时间</td>
											<td><input type="text" name="gaizhiDate" style=""
												value="${supplier.gaizhiDateStr}" id="gaizhiDate" /></td>
											<td style="">员工人数(人)</td>
											<td><input type="text" name="headcount" style=""
												value="${supplier.headcount}"
												class="{digits:true,messages:{digits:'格式不正确!'}}" /></td>
										</tr>
										<tr>
											<td style="">传真</td>
											<td><input type="text" name="fax" style=""
												value="${supplier.fax}" /></td>
											<td style="">手机</td>
											<td><input type="text" name="mobilephone" style=""
												value="${supplier.mobilephone}" /></td>
										</tr>
										<tr>
											<td style="">管理人员(人)</td>
											<td><input type="text" name="managerCount" style=""
												value="${supplier.managerCount}"
												class="{digits:true,messages:{digits:'格式不正确!'}}" /></td>
											<td style="">直接工作(人)</td>
											<td><input type="text" name="onLineOperationCount"
												style="" value="${supplier.onLineOperationCount}"
												class="{digits:true,messages:{digits:'格式不正确!'}}" /></td>
										</tr>
										<tr>
											<td style="">间接工人(人)</td>
											<td><input type="text" name="indirectOperationCount"
												style="" value="${supplier.indirectOperationCount}"
												class="{digits:true,messages:{digits:'格式不正确!'}}" /></td>
											<td style="">技术人员(人)</td>
											<td><input type="text" name="technologyCount" style=""
												value="${supplier.technologyCount}"
												class="{digits:true,messages:{digits:'格式不正确!'}}" /></td>
										</tr>
										<tr>
											<td style="">质检人员(人)</td>
											<td><input type="text" name="qualityControlCount"
												style="" value="${supplier.qualityControlCount}"
												class="{digits:true,messages:{digits:'格式不正确!'}}" /></td>
											<td style="">占地面积(M<SUP>2</SUP>)</td>
											<td><input type="text" name="floorArea" style=""
												value="${supplier.floorArea}" class="{min:0,number:true}" /></td>
										</tr>
										<tr>
											<td style="">建筑面积(M<SUP>2</SUP>)</td>
											<td><input type="text" name="builtUpArea" style=""
												value="${supplier.builtUpArea}" class="{min:0,number:true}" />
											</td>
											<td style="">生产面积(M<SUP>2</SUP>)</td>
											<td><input type="text" name="productArea" style=""
												value="${supplier.productArea}" class="{min:0,number:true}" />
											</td>
										</tr>
										<tr>
											<td style="">股权关系</td>
											<td><input type="text" name="equityStake" style=""
												value="${supplier.equityStake}" /></td>
											<td style="">注册资金(万)</td>
											<td><input type="text" name="registrationAsset" style=""
												value="${supplier.registrationAsset}" class="{min:0,number:true}" />
											</td>
										</tr>
										<tr>
											<td style="">总资产(万)</td>
											<td><input type="text" name="grossAssets" style=""
												value="${supplier.grossAssets}" class="{min:0,number:true}" />
											</td>
											<td style="">净资产(万)</td>
											<td><input type="text" name="netAssets" style=""
												value="${supplier.netAssets}" class="{min:0,number:true}" /></td>
										</tr>
										<tr>
											<td style="">负债总额(万)</td>
											<td><input type="text" name="totalLiabilities" style=""
												value="${supplier.totalLiabilities}"
												class="{min:0,number:true}" /></td>
											<td style="">固定资产(万)</td>
											<td><input type="text" name="fixedAssets" style=""
												value="${supplier.fixedAssets}" class="{min:0,number:true}" />
											</td>
										</tr>
										<tr>
											<td style="">固定资产净值(万)</td>
											<td><input type="text" name="netFixedAssets" style=""
												value="${supplier.netFixedAssets}" class="{min:0,number:true}" />
											</td>
											<td style="">管理类别</td>
											<td><input type="text" name="manageType" style=""
												value="${supplier.manageType}" /></td>
										</tr>
										<tr>
											<td style="">是否通过3C认证</td>
											<td><input type="radio" name="certificationOf3c" style=""
												value="true"
												<s:if test="supplier.certificationOf3c==true">checked</s:if> />是
												<input type="radio" name="certificationOf3c" style=""
												value="false"
												<s:if test="supplier.certificationOf3c!=true">checked</s:if> />否
											</td>
											<td>是否通过TS16949审核</td>
											<td><input type="radio" name="certificationOfTS16949"
												style="" value="true"
												<s:if test="supplier.certificationOfTS16949==true">checked</s:if> />是
												<input type="radio" name="certificationOfTS16949" style=""
												value="false"
												<s:if test="supplier.certificationOfTS16949!=true">checked</s:if> />否
											</td>
										</tr>
										<tr>
											<td style="">其他认证情况</td>
											<td><input type="text" name="otherCertification" style=""
												value="${supplier.otherCertification}" /></td>
											<td style="">主要产品类别</td>
											<td><input type="text" name="majorProductType" style=""
												value="${supplier.majorProductType}" /></td>
										</tr>
										<tr>
											<td style="height: 60px; " valign="top">备注</td>
											<td colspan="3"><textarea name="remark"
													style="width: 100%; height: 60px;">${supplier.remark}</textarea>
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