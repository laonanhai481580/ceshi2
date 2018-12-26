<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@ include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>	
	<script type="text/javascript">
	//关闭
	function closeView(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
				<div class="opt-btn">
					<button class='btn' onclick="closeView();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>	
				</div>
				<s:if test="error">
					<div id="opt-content">
						<h2><s:text name='对不起，没有符合条件的计量器具卡片！'/></h2>
					</div>
				</s:if><s:else>
				<div id="opt-content" style="text-align: center;">
					<div id="tabs-1">
						<aa:zone name="viewZone">
							 <form  id="cpForm" name="cpForm" method="post" action="">
							 <table class="form-table-border-left" style="width:100%;margin: auto;">
								<caption style="height: 50px"><h2><s:text name='计量器具卡片'/></h2></caption>
								<tr>
									<td><s:text name='器具编号'/>:</td>
									<td>${equipment.measurementNo}</td>
									<td><s:text name='类别'/>:</td>
									<td>${equipment.measurementType}</td>
									<td ><s:text name='校验方式'/>:</td>
									<td>${equipment.checkForm}</td>
								</tr>
								<tr>
									<td><s:text name='器具名称'/>:</td>
									<td>${equipment.measurementName}</td>
									<td><s:text name='器具型号/规格'/>:</td>
									<td>${equipment.measurementSpecification}</td>   
									<td><s:text name='生产厂家'/>:</td>
									<td>${equipment.manufacturer}</td>  
								</tr>   
								<tr>
									<td><s:text name='使用部门'/>:</td>
									<td>${equipment.useDept}</td>
									<td><s:text name='校验周期'/>:</td>
									<td>${equipment.testCycle}</td>
									<td><s:text name='器具状态'/> :</td>
									<td>${equipment.measurementState}</td>
								</tr>
							</table>						
							</form>
							
						</aa:zone>
					</div>
									
				</div>	
			</s:else>
		</div>		
	</div>	
</body>
</html>