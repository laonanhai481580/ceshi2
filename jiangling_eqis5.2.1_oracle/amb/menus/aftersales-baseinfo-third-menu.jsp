<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>

<script>
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
				var aIndex = 0;
				var aId = thirdMenu;
				if(thirdMenu=="lar_target"){
					$("#lar_target").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+
					          <security:authorize ifAnyGranted="AFS_BASEINFO_LAR_TARGET_LIST">
					          "<li onclick='selectedNode(this)' id='_lar_target'><a href='${aftersalesctx}/base-info/lar-target/list.htm'>LAR目标值维护</a></li>"
					          +</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_lar_target";
				}else if(thirdMenu=="defectionItem"){
					$("#defectionItem").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+
					          <security:authorize ifAnyGranted="AFS_BASEINFO_DEFECTION_LIST">
					          "<li onclick='selectedNode(this)' id='_defectionClass'><a href='${aftersalesctx}/base-info/defection-item/defection-class-list.htm'>不良分类</a></li>"+
					          "<li onclick='selectedNode(this)' id='_defectionItem'><a href='${aftersalesctx}/base-info/defection-item/list.htm'>不良项目</a></li>"
					          +</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_defectionItem";
				}else if(thirdMenu=="customer"){
					$("#customer").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+
					          <security:authorize ifAnyGranted="AFS_BASEINFO_CUSTOMER_LIST">
					          "<li onclick='selectedNode(this)' id='_customer'><a href='${aftersalesctx}/base-info/customer/customer-list.htm'>客户清单</a></li>"
					          +</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_customer";
				}else if(thirdMenu=="vlrr_warming"){
					$("#vlrr_warming").jstree({ 
						"html_data" : {
							"data" :
					          "<ul>"+
					          <security:authorize ifAnyGranted="AFS_BASEINFO_VLRR_WARMING_LIST">
					          "<li onclick='selectedNode(this)' id='_vlrr_warming'><a href='${aftersalesctx}/base-info/vlrr-warming/list.htm'>VLRR机种+目标维护</a></li>"
					          +</security:authorize>
						      "</ul>"
						},
						"ui" : {
							"initially_select" : [ treeMenu ]
						},
						"plugins" : [ "themes", "html_data","ui" ]
					});
					aId = "_vlrr_warming";
				}
				$("#accordion1 > h3 > a").each(function(index,obj){
					if(obj.id == aId){
						aIndex = index;
						return false;
					}
				});
				$("#accordion1").accordion({active:aIndex});
			});
			function selectedNode(obj) {
				window.location = $(obj).children('a').attr('href');
			}
		   function _change_menu(url){
				window.location=url;
			}
			</script>
			
	<div id="accordion1" class="basic">
		<security:authorize ifAnyGranted="AFS_BASEINFO_LAR_TARGET_LIST">
		<h3><a id="_lar_target" onclick="_change_menu('<grid:authorize code="AFS_BASEINFO_LAR_TARGET_LIST" systemCode="aftersales"></grid:authorize>');">LAR目标值维护</a></h3>
		<div>
			<div id="lar_target" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>

		<security:authorize ifAnyGranted="AFS_BASEINFO_DEFECTION_LIST">
		<h3><a id="_defectionItem" onclick="_change_menu('<grid:authorize code="AFS_BASEINFO_DEFECTION_LIST" systemCode="aftersales"></grid:authorize>');">不良项目维护</a></h3>
		<div>
			<div id="defectionItem" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>

		<security:authorize ifAnyGranted="AFS_BASEINFO_CUSTOMER_LIST">
		<h3><a id="_customer" onclick="_change_menu('<grid:authorize code="AFS_BASEINFO_CUSTOMER_LIST" systemCode="aftersales"></grid:authorize>');">客户清单维护</a></h3>
		<div>
			<div id="customer" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
		
		<security:authorize ifAnyGranted="AFS_BASEINFO_VLRR_WARMING_LIST">
		<h3><a id="_vlrr_warming" onclick="_change_menu('<grid:authorize code="AFS_BASEINFO_VLRR_WARMING_LIST" systemCode="aftersales"></grid:authorize>');">VLRR机种+目标维护</a></h3>
		<div>
			<div id="vlrr_warming" class="demo">菜单加载中,请稍候...</div>
		</div>
		</security:authorize>
	</div>
