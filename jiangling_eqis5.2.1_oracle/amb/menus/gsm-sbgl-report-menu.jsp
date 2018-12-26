<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
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
<div id="accordion1" class="basic">
	<h3><a id="_swatche_carBody_list" onclick="_change_menu('${gsmctx }/swatche/carBody-list.htm');">色板台帐</a></h3>
	<div>
		<div id="swatcheList" class="demo"></div>
		
		<script type="text/javascript" class="source">
		$(function () {
			if(thirdMenu=="swatcheList"){
				$("#swatcheList").jstree({ 
					"html_data" : {
						"data" :  
							 "<ul>"+
					          <security:authorize ifAnyGranted="swatche_list">
							   "<li onclick='selectedNode(this)' id='_swatche_carBody_list'><a href='${gsmctx }/swatche/carBody-list.htm?sbType=车身色板'>车身色板</a></li>"+
					          </security:authorize>
					          <security:authorize ifAnyGranted="swatche_carInteriorTrim_list">
						      "<li onclick='selectedNode(this)' id='_swatche_carInteriorTrim_list'><a href='${gsmctx }/swatche/carInteriorTrim-list.htm?sbType=车内饰色板'>车内饰色板</a></li>"+
						      </security:authorize>
						      "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:0});
			}else if(thirdMenu=="recipientList"){
				$("#recipientList").jstree({ 
					"html_data" : {
						"data" :  
				          "<ul>"+
				             <security:authorize ifAnyGranted="recipient_list">
				               "<li onclick='selectedNode(this)' id='_recipient_list'><a href='${gsmctx }/recipient/list.htm'>色板发放回收记录表</a></li>"+
				               </security:authorize>
				            "</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:1});
			}else if(thirdMenu=="recyclingList"){
				$("#recyclingList").jstree({ 
					"html_data" : {
						"data" :
							"<ul>"+
							<security:authorize ifAnyGranted="recycling_list">
							   "<li onclick='selectedNode(this)' id='_recycling_list'><a href='${gsmctx }/recycling/list.htm'>回收记录表</a></li>"+
							</security:authorize>
							"</ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({active:2});
			}
		});
		function selectedNode(obj) {
			window.location = $(obj).children('a').attr('href');
		}
	   function _change_menu(url){
			window.location=url;
		}
		</script>
		
	</div>
    <security:authorize ifAnyGranted="recipient_list">
		<h3><a id="_recipient_list"  onclick="_change_menu('${gsmctx }/recipient/list.htm');">领用台帐</a></h3>
		<div>
			<div id="recipientList" class="demo"></div>
		</div>
	</security:authorize>
    <security:authorize ifAnyGranted="recycling_list">
		<h3><a id="_recycling_list" onclick="_change_menu('${gsmctx }/recycling/list.htm');">回收台帐</a></h3>
		<div>
			<div id="recyclingList" class="demo"></div>
		</div>
	</security:authorize>
</div>