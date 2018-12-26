<%@page import="com.ambition.iqc.entity.SampleScheme"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.carmfg.bom.web.BomAction"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>检验方案变更</title>
		<%@include file="/common/meta.jsp" %>	
		<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
		<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
		<script type="text/javascript">
			var isUsingComonLayout=false;
			$(function(){
				$("#sampleTransfer").validate({
				});
				$('#auditTime').timepicker();
			});
			function save(){
				if($("#sampleTransfer").valid()){
					var params = getParams();
					$(".btn").attr("disabled","disabled");
					$("#message").html("正在保存,请稍候... ...");
					$.post("${iqcctx}/inspection-report/sample-transfer/save.htm",params,function(result){
						$(".btn").attr("disabled","");
						if(result.error){
							$("#message").html("<font color=red>" + result.message + "</font>");
						}else{
							$("#message").html("保存成功!");
							$("#id").val(result.id);
						}
						setTimeout(function(){
							$("#message").html("");
						},3000);
					},'json');
				}
			}
			//获取参数
			function getParams(){
				var params = {};
				$(":input","#sampleTransfer").each(function(index,obj){
					var jObj = $(obj);
					if(obj.name){
						params[obj.name] = jObj.val();
					}
				});
				return params;
			}
			function checkBomClick(){
				$.colorbox({href:"${iqcctx}/statistical-analysis/bom-code-select.htm",
					iframe:true, 
					innerWidth:550, 
					innerHeight:350,
		 			overlayClose:false,
		 			title:"选择物料BOM"
		 		});
		 	}
			function setBomValue(datas){
				$("#checkBomCode").val(datas[0].value);
				$("#checkBomName").val(datas[0].name);
		 	}
			
			function supplierClick(){
				$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",
					iframe:true, 
					innerWidth:550, 
					innerHeight:350,
					overlayClose:false,
					title:"选择供应商"
				});
			}
			
			function setSupplierValue(objs){
				var obj = objs[0];
				$("#supplierName").val(obj.name);
				$("#supplierCode").val(obj.code);
			}
		</script>
	</head>
	<body>
		<div class="opt-body">
			<div class="opt-btn" style="line-height:33px;">
			<button  class='btn' type="button" onclick="save();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<span style="padding-left:4px;padding-top:4px;color:red;" id="message"></span>
			</div>
			<div id="opt-content">
				<form id="sampleTransfer" name="sampleTransfer" method="post" action="">
					<input type="hidden" name="id" id="id"></input>
					<table>
						<tr>
							<td align=right style="width:90px;">厂区<font color="red">*</font></td>
							<td>
							 <select name="businessUnitName" id="businessUnitName" class="xla_k">
		                         <option value=""></option>
		                         <option value="下罗IQC" <c:if test='${"下罗IQC" eq businessUnitName}'>selected</c:if>>下罗IQC</option>
		                         <option value="高新IQC" <c:if test='${"高新IQC" eq businessUnitName}'>selected</c:if>>高新IQC</option>
		                         <option value="培训中心IQC" <c:if test='${"培训中心IQC" eq businessUnitName}'>selected</c:if>>培训中心IQC</option>
               				 </select>
							</td>
						</tr>					
						<tr>
							<td align=right style="width:90px;">供应商<font color="red">*</font></td>
							<td>
								<input id="supplierName" name="supplierName" class="{required:true}" onclick="supplierClick()"></input>
								<input type="hidden" id="supplierCode" name="supplierCode"></input>
							</td>
						</tr>
						<tr>
							<td align=right>物料<font color="red">*</font></td>
							<td>
								<input id="checkBomCode" name="checkBomCode" class="{required:true,messages:{required:'请输入编码!'}}" onclick="checkBomClick(this)"/>
								<input id="checkBomName" name="checkBomName" type="hidden"/>
							</td>
						</tr>
						<tr>
							<td align=right>抽样方案<font color="red">*</font></td>
							<td>
								<select name="targetRule">
									<option value="<%=SampleScheme.ORDINARY_TYPE%>"><%=SampleScheme.ORDINARY_TYPE%></option>
									<option value="<%=SampleScheme.RELAX_TYPE%>"><%=SampleScheme.RELAX_TYPE%></option>
									<option value="<%=SampleScheme.TIGHTEN_TYPE%>"><%=SampleScheme.TIGHTEN_TYPE%></option>
									<option value="<%=SampleScheme.EXEMPTION_TYPE%>"><%=SampleScheme.EXEMPTION_TYPE%></option>
								</select>
							</td>
						</tr>
						<tr>
							<td align=right>生效时间<font color="red">*</font></td>
							<td>
								<input id="auditTime" name="auditTime" value="${auditTimeStr}"/> 
								<input type="hidden" name="auditMan" value="${auditMan}"/> 
								<input type="hidden" name="auditState" value="${auditState}"/> 
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>
</html>