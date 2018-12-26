<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.spc.entity.MonitProgram"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	ValueStack valueStack=(ValueStack)request.getAttribute("struts.valueStack");
	MonitProgram monitProgram =(MonitProgram) valueStack.findValue("monitProgram");
	String lights=(String)valueStack.findValue("lights");
%>
<script type="text/javascript">
	function refsh(){
		var fileId = '<%=monitProgram==null?"":monitProgram.getAttachUrl()%>';
		if(fileId){
			$("#monitorImage").attr("src",$.getDownloadPath(fileId));
		}
		initContextMenu();
		resizeOptContent();
		$(".light").remove();
		createLights(lights);
	}
	//图表JAVASCRIPT
	var lights = <%=lights%>;
	function loadPage(){
		window.parent.location.reload();
	} 
	
	function initContextMenu(){
		$("#bodyid").click(function(){
			$(".context-menu-list").hide();
			$("#qualityFeatureTable").hide();
		}).keyup(function(){
			$(".context-menu-list").hide();
			$("#qualityFeatureTable").hide();
		});
	}

	function resizeOptContent(){
		$(".opt-body").height($(".ui-layout-center").height());
		$("#opt-content").width($(".opt-body").width())
		.height($(".opt-body").height()-33)
		.css({
			top:'33px',
			left:'0px'
		});
		var imageHeight = $("#monitorImage").height();
		if(imageHeight < $("#opt-content").height()){
			$("#opt-content").height(imageHeight);
		}
	}
	
	function contentResize(){
		resizeOptContent();
		$(".light").remove();
		createLights(lights);
	}
	
	/* $(document).ready( function() {
		var fileId = '${monitProgram.attachUrl}';
		if(fileId){
			$("#monitorImage").attr("src",$.getDownloadPath(fileId));
		}
//			$.post("${goalctx}/goal-statistics/get-src.htm",function(result){
//				if(result.error){
//					alert(result.message);
//				}else{
//					setMonitorImage(result);
//				}
//			},'json');
		resizeOptContent();
		initContextMenu();//初始化右键菜单
		bindAddContextMenu();//绑定添加的右键菜单
		createLights(lights);
	}); */
	//创建灯
	function createLights(lights){
		for(var i=0;i<lights.length;i++){
			var light = lights[i];
			var $image = $("#monitorImage");
			var imageWidth = $image.width(),imageHeight = $image.height();
			var html = "<div id='light"+light.id+"' class='light "+light.color+"-light' title="+light.name+" style='cursor:pointer;' onclick='showQualityFeature("+light.id+","+light.myTop+","+light.myLeft+","+light.imageWidth+","+light.imageWidth+");'></div>";
			$("#opt-content").append(html);
			var pCss = {
				'z-index' : 1
			};
			if(imageWidth != light.imageWidth){
				var top = light.myTop * imageHeight/light.imageHeight,left = light.myLeft * imageWidth/light.imageWidth;
				if(imageWidth > light.imageWidth){
					pCss.top = (top + 1) +"px";
					pCss.left = (left + 1) +"px";
				}else{
					pCss.top = (top - 1) +"px";
					pCss.left = (left - 1) +"px";
				}
			}else{
				pCss.top = light.myTop + "px";
				pCss.left = light.myLeft + "px";
			}
			var $light = $("#light"+light.id).css(pCss);
		}
	}
</script>

<input type="hidden" name="monitorProgramId" id="monitorProgramId" value="${monitProgram.id}"/>
<img  width="100%" id="monitorImage" style="position:absolute;left:0px;top:0px;z-index:1"></img>
