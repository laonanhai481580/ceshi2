<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu = "_cpkRuleList";
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
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="spc-cpkRule-input">
						<button class="btn" onclick="iMatrix.addRow();">
							<span>
								<span>
									<b class="btn-icons btn-icons-add"></b>新建
								</span>
							</span>
						</button> 
					</security:authorize>
					
					<security:authorize ifAnyGranted="spc-cpkRule-delete">
						<button class='btn' type="button" onclick="iMatrix.delRow();">
							<span>
								<span>
									<b class="btn-icons btn-icons-delete"></b>删除
								</span>
							</span>
						</button>
					</security:authorize>
						<button class='btn' type="button" onclick="iMatrix.showSearchDIV(this);">
							<span>
								<span>
									<b class="btn-icons btn-icons-search"></b>查询
								</span>
							</span>
						</button>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">
					setTimeout("$('#message').hide('show');",3000);
				</script>
				<div id="opt-content">
					<form id="contentForm"  method="post"  action="">
						<grid:jqGrid gridId="cpkRuleList"  url="${spcctx}/base-info/cpk-rule/list-datas.htm" code="SPC_CPK_RULE" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>