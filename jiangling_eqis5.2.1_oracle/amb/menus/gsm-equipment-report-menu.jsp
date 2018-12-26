<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	$( "#accordion1" ).accordion({
		animated:false,
		collapsible:false,
		event:'click',
		fillSpace:true 
	});
	
});
</script>
	<script type="text/javascript" class="source">
		$(function () {
			if(thirdMenu=="allEquipment"){
				$("#allEquipment").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul><li onclick='selectedNode(this)' id='myEquipmentReportInput'><a href='${gsmctx }/equipment/input.htm'>量检具卡片</a></li>"+
					      "<li onclick='selectedNode(this)' id='myEquipmentReport'><a href='${gsmctx }/equipment/list.htm'>量检具台账</a></li>"+
// 				          "<li onclick='selectedNode(this)' id='borrow'><a href='${gsmctx }/equipment/borrow-list.htm'>领用登记</a></li>"+
// 				          "<li onclick='selectedNode(this)' id='return'><a href='${gsmctx }/equipment/return-list.htm'>归还登记</a></li>"+
				          "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:0});
			}else if(thirdMenu=="deptEquipment"){
				$("#deptEquipment").jstree({ 
					"html_data" : {
						"data" :  
				          /* "<ul>"+
						      "<li onclick='selectedNode(this)' id='deptEquipment_list'><a href='${gsmctx }/equipment/dept-list.htm'>部门量具台账</a></li>"+
			    		  "</ul>" */
							"<ul>"+
						      <c:forEach items="${deptOption}" var="options" varStatus="base">
						      "<li onclick='selectedNode(this)' id='${options.name}'><a href='${gsmctx }/equipment/dept-list.htm?dept=${options.name}'>${options.name}</a></li>"+
				    		  </c:forEach>
			    		  "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:1});
			}else if(thirdMenu=="transfer"){
				$("#transfer").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul>"+
						      "<li onclick='selectedNode(this)' id='transfer_list'><a href='${gsmctx }/equipment/transfer-list.htm'>责任人转移台账</a></li>"+
			    		  "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:2});
			}else if(thirdMenu=="state"){
				$("#state").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul><li onclick='selectedNode(this)' id='myEquipment'><a href='${gsmctx }/equipment/list-y.htm'>隐藏台帐</a></li></ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:3});
			}
		});
		function selectedNode(obj) {
			window.location = $(obj).children('a').attr('href');
		}
	   function _change_menu(url){
			window.location=url;
		}
	</script>
<div id="accordion1" class="basic">
	<security:authorize ifAnyGranted="gsm_equipment_list">
		<h3><a id="_allEquipment" onclick="_change_menu('${gsmctx}/equipment/list.htm');">量检具管理</a></h3>
		<div><div id="allEquipment" class="demo">数据加载中......</div></div>
	</security:authorize>
	<security:authorize ifAnyGranted="gsm_equipment-dept_list">
		<h3><a id="_deptEquipment"  onclick="_change_menu('${gsmctx }/equipment/dept-list.htm');">部门量检具台帐</a></h3>
		<div><div id="deptEquipment" class="demo">数据加载中......</div></div>
	</security:authorize>
	<security:authorize ifAnyGranted="gsm_equipment_transfer_list">
		<h3><a id="_transfer"  onclick="_change_menu('${gsmctx }/equipment/transfer-list.htm');">责任人转移台帐</a></h3>
		<div><div id="transfer" class="demo">数据加载中......</div></div>
	</security:authorize>
	<security:authorize ifAnyGranted="gsm_equipment_list-y">
		<h3><a id="_state" onclick="_change_menu('${gsmctx}/equipment/list-y.htm');">隐藏台帐</a></h3>
		<div><div id="state" class="demo">数据加载中......</div></div>
	</security:authorize>
</div>