<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@ include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/jquery-ui-1.8.7.js"></script>
	
	<!-- 表单和流程常用的方法封装 -->
	<c:set var="actionBaseCtx" value="${iqcctx}/inspection-ask"/>
	<script type="text/javascript">
	$(document).ready(function(){
		if('${saveSucc}'=='true'){
			if(window.parent){
				window.parent.$.colorbox.close();
				window.parent.$("#supplier").trigger("reloadGrid");
				return;				
			}
		}
	});
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
				<security:authorize ifAnyGranted="supplier-base-info-level-score-save">
					  <button class='btn' id="btnsave" type="button" onclick="saveFormBoxClose('${supplierctx}/base-info/level-score/save.htm');">
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
				   <input type="hidden"  name="scoreArea" id="scoreArea" value="${scoreArea}" />
					<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
					    <tr>
					      <td>
					              事业部: <input  name="businessUnitName" readonly="readonly" id="businessUnitName" value="${businessUnitName}" class="{required:true,messages:{required:'必填'}}"/>
					      </td>
					    </tr>
					    <tr>   
					    <td>
					        评价等级:<s:select list="auditLevels" 
								  theme="simple"
								  listKey="value" 
								  listValue="value" 
								  name="auditLevel"
								  id="auditLevel"
								  emptyOption="false"
								  labelSeparator=""
								  ></s:select>
					    </td>
					    </tr>
					    <tr>
					       <td>
						      得分:<input  name="scoreStart" id="scoreStart" value="${scoreStart}" class="{required:true,messages:{required:'必填'}}" />至<input  name="scoreEnd" id="scoreEnd" value="${scoreEnd}" class="{required:true,messages:{required:'必填'}}" />
						   </td>
					    </tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>