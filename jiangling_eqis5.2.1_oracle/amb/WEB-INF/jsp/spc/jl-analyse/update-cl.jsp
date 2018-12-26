<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.processdefine.service.ProcessDefineManager"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>控制线</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/widgets/validation/cmxform.css"/>
	<script src="${ctx}/widgets/validation/jquery.metadata.js" type="text/javascript"></script>
	<script type="text/javascript">
	var isUsingComonLayout=false;
	function save(){
		var params=getParams();
		var temp=true;
		if(params['params.isAuto']=='N'){
		 $("#contentForm").validate({
			  rules: {
				  xucl:"required",
				  xcl:"required",
				  xlcl:"required",
				  sucl:"required",
				  scl:"required",
				  slcl:"required"
			  }
		 });
			 temp=$("#contentForm").valid();
		}
		if(temp){
		$("#message").html("<font color='red'>保存中...</font>");
		$.post("${spcctx}/jl-analyse/save-cl.htm",params,function(){
 				$("#message").html("<font color='red'>保存成功!</font>");
		},'json');
		}
	}
	
	function getParams(){
		var params={};
		$(":input","#contentForm").each(function(index,obj){
			var jObj=$(obj);
			if(obj.type=='radio'){
				if(obj.checked){
					params["params." +obj.name] = jObj.val();
				}
			}else{
				params["params." +obj.name] = jObj.val();
			}
		});
		return params;
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<security:authorize ifAnyGranted="spc_jl-analyse_save-cl">
					<button class='btn' type="button" onclick="save();"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<span style="margin-left: 6px; line-height: 30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
			</div>
			<form id="contentForm" name="contentForm" method="post">
				<input type="hidden" name="featureId" value="${featureId}"></input>
				<div>
					<table class="form-table-border-left" style="width: 98%; margin-left: 10px; margin-top: 10px;">
						<tr>
							<td colspan="4"><input type="radio" name="isAuto" value="Y" <s:if test="%{#isAuto==\"Y\"}">checked="checked"</s:if>>分析阶段</input>
								<input type="radio" name="isAuto" value="N" <s:if test="%{#isAuto==\"N\"}">checked="checked"</s:if>>控制阶段</input>
							</td>
						</tr>
						<tr>
							<td colspan="4"><span>主图控制限</span></td>
						</tr>
						<tr>
							<td width="10%">上控制限</td>
							<td width="30%" align="left"><input name="xucl" id="xucl" type="text" value="${xucl}" /></td>
							<td width="10%">控制用上控制限</td>
							<td width="*%">${xucl}</td>
						</tr>
						<tr>
							<td width="19%">目标值</td>
							<td width="*%" align="left"><input name="xcl" id="xcl" type="text" value="${xcl}" /></td>
							<td width="19%">原目标值</td>
							<td width="*%" align="left">${xcl}</td>
						</tr>
						<tr>
							<td width="19%">下控制限</td>
							<td width="*%" align="left"><input name="xlcl" id="xlcl" type="text" value="${xlcl}" /></td>
							<td width="19%">控制用下控制限</td>
							<td width="*%" align="left">${xlcl}</td>
						</tr>
						<tr>
							<td nowrap="nowrap" colspan="4"><span class="add_table_name">副图控制限</span></td>
						</tr>
						<tr>
							<td width="19%">上控制限</td>
							<td width="*%" align="left"><input name="sucl" id="sucl" type="text" value="${sucl}" /></td>
							<td width="19%">控制用上控制限</td>
							<td width="*%" align="left">${sucl}</td>
						</tr>
						<tr>
							<td width="19%">目标值</td>
							<td width="*%" align="left"><input name="scl" id="scl" type="text" value="${scl}" /></td>
							<td width="19%">原目标值</td>
							<td width="*%" align="left">${scl}</td>
						</tr>
						<tr>
							<td width="19%">下控制限</td>
							<td width="*%" align="left"><input name="slcl" id="slcl" type="text" value="${slcl}" /></td>
							<td width="19%">控制用下控制限</td>
							<td width="*%" align="left">${slcl}</td>
						</tr>
					</table>
				</div>
				<div>
					<table class="form-table-border" style="width: 98%; margin-left: 10px; margin-top: 10px;">
						<tr>
							<td colspan="3" style="background: #99CCFF;">主图控制限</td>
							<td colspan="3" style="background: #99CCFF;">副图控制限</td>
							<td colspan="2" style="background: #99CCFF;">基本信息</td>
						</tr>
						<tr>
							<td style="background: #99CCFF;">上控制限</td>
							<td style="background: #99CCFF;">目标值</td>
							<td style="background: #99CCFF;">下控制限</td>
							<td style="background: #99CCFF;">上控制限</td>
							<td style="background: #99CCFF;">目标值</td>
							<td style="background: #99CCFF;">下控制限</td>
							<td style="background: #99CCFF;">创建人</td>
							<td style="background: #99CCFF;">创建时间</td>
						</tr>
						<s:iterator value="controlLimits" id="controlLimit">
							<tr>
								<td>${controlLimit.xucl}</td>
								<td>${controlLimit.xcl}</td>
								<td>${controlLimit.xlcl}</td>
								<td>${controlLimit.sucl}</td>
								<td>${controlLimit.scl}</td>
								<td>${controlLimit.slcl}</td>
								<td>${controlLimit.creator}</td>
								<td>${controlLimit.createdTime}</td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>