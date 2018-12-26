<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	function modelFormatter(cellvalue, options, rowObject){
		if(cellvalue=='0'){
			return '趋势型';
		}else if(cellvalue=='1'){
			return '运行型';
		}else if(cellvalue=='2'){
			return '交替型';
		}else if(cellvalue=='3'){
			return '其他';
		}
	}
	
	function typeFormatter(cellvalue, options, rowObject){
		if(cellvalue=='0'){
			return '主控制图';
		}else if(cellvalue=='1'){
			return '副控制图';
		}
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name = "main">
				<div style="margin-top:15px; margin-left: 10px;">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="bsRules"  url="${spcctx}/statistics-analysis/bs-rules-list-datas.htm" code="SPC_BS_RULES" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>