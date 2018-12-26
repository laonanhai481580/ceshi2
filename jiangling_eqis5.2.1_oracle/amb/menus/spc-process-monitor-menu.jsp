<%@ page import="com.ambition.spc.processdefine.service.ProcessDefineManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%Map<String,Integer> pointKeyMap = ProcessDefineManager.getPointKeyMap();%>
<script>
	$(function() {
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true }
		);
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
	
 	//监控方案树
	function refreshMointorTree(){
		$("#monitor-program-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "监控方案", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/monitor-program/monit-program-list.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : [ "themes","json_data","ui","crrm","contextmenu",'types'],
			core : { "initially_open" : ["root"] },
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${spcctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
				"initially_select" : ["${monitProgram.id}"]
			},
			contextmenu : {
				items : {
					create:null,
					rename :null,
					remove : null,
					ccp : null
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var level = data.rslt.obj.attr("level");
			var fileId = '';
			if(data.rslt.obj.attr("id")=='root'){
				alert("请选择监控方案");
			}else{
			$("#monitorProgramId").val(data.rslt.obj.attr("id"));
			var picurl='${spcctx }/base-info/monitor-program/look-monitor-program-pic.htm?monitorProgramId='+data.rslt.obj.attr("id")+"&nowtime=" + (new Date()).getTime();
			$("#pic").load(picurl,function(){
				refsh();
			});
			var cpktableurl='${spcctx }/base-info/monitor-program/look-cpk-table.htm?monitorProgramId='+data.rslt.obj.attr("id")+"&nowtime=" + (new Date()).getTime();
			$("#cpkparentTable").load(cpktableurl,function(){
			});
			/* 
			$.post('${spcctx}/base-info/monitor-program/list-attachurl.htm',{id:data.rslt.obj.attr("id")},function(result){
				fileId=result.attachUrl;
				if(fileId){
					$("#monitorImage").attr("src",$.getDownloadPath(fileId));
				}
			},'json'); */
			}
		});
	}
	
	
	
</script>
	<div id="accordion1" class="basic">
       <h3>
			<security:authorize ifAnyGranted="base-info-monitor-program-monitor-list">
				<a id="_monitorTree" onclick="_change_menu('${spcctx}/base-info/monitor-program/monitor-list.htm');">过程监控</a>
			</security:authorize>
		</h3>
		<div>
			<div id="monitor-program-tree" class="demo">菜单加载中,请稍候...</div>
			<script src="${ctx}/widgets/jstree/jquery.jstree.js" type="text/javascript"></script>
			<script type="text/javascript" class="source">
			$(function () {
				if(thirdMenu=="monitorTree"){
					refreshMointorTree();
					$("#accordion1").accordion({active:0});
				}
				if(thirdMenu=="_messages"){
					$("#accordion1").accordion({active:1});
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
		<h3>
			<security:authorize ifAnyGranted="spc_process-monitor_exception-message">
				<a id="_message" onclick="javascript:window.location='${spcctx }/process-monitor/exception-message.htm';">异常消息</a>
			</security:authorize>	
		</h3>
		<div style="padding:0px;">
			<div id="_messages" class="west-notree" onclick="javascript:window.location='${spcctx }/process-monitor/exception-message.htm';">
				<span>异常消息</span>
			</div>
		</div>
	</div>