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
		resizeOptContent();
		initContextMenu();//初始化右键菜单
		bindAddContextMenu();//绑定添加的右键菜单
		$(".light").remove();
		createLights(lights);
	}
	//图表JAVASCRIPT
	var lights = <%=lights%>;
	var funs = {
		'add' : function(event,p,targetId){
			var $image = $("#monitorImage");
			var monitorProgramId=$("#monitorProgramId").val();
			var light = {
				imageWidth : $image.width(),
				imageHeight : $image.height(),
				myLeft : p.left,
				myTop : p.top + $("#opt-content").scrollTop(),
				monitorProgramId:monitorProgramId,
				type:"add"
			};
			lights.push(light);
			createLights([light]);
			var url = '${spcctx}/base-info/monitor-point/add-monit-point.htm?'+$.param(light);
			$.colorbox({href:url,iframe:true, innerWidth:900, innerHeight:600,
				overlayClose:false,
				title:"添加监控灯",
				onClosed:function(){}
			});
		},
		'update' : function(event,p,targetElement){
			for(var i=0;i<lights.length;i++){
				if('light' + lights[i].id == targetElement){
					var url = '${spcctx}/base-info/monitor-point/add-monit-point.htm?'+$.param(lights[i]);
	 				$.colorbox({href:url,iframe:true, innerWidth:900, innerHeight:600,
	 					overlayClose:false,
	 					title:"修改监控灯",
	 					onClosed:function(){}
	 				});
					break;
				}
			};
		},
		'delete' : function(event,p,targetId){
			for(var i=0;i<lights.length;i++){
				if('light' + lights[i].id == targetId){
					if(confirm("确定要删除该灯吗？")){
						$("#" + targetId).remove();
						$.post("${spcctx}/base-info/monitor-point/delete.htm?deleteIds="+lights[i].id, {}, function(result) {},'json');
					}
					lights.splice(i,1);
					break;
				}
			};
		}
	};
	
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
	
	function initContextMenu(){
		$(".context-menu-list li").mouseover(function(event){
			$(this).css("background-color","#39F");
		}).mouseout(function(event){
			$(this).css("background-color","");
		}).click(function(event){
			var position = $(this).parent().position();
			$(this).parent().hide();
			var code = $(this).attr("code");
			if($.isFunction(funs[code])){
				funs[code](event,position,$(this).parent().attr("targetId"));
			}
		});
		$(window).click(function(){
			$(".context-menu-list").hide();
			$("#qualityFeatureTable").hide();
		}).keyup(function(){
			$(".context-menu-list").hide();
			$("#qualityFeatureTable").hide();
		});
	}
	//绑定添加右键事件
	function bindAddContextMenu(){
		$("#monitorImage").mouseup(function(event){
			if(event.which==3){
				$(".context-menu-list").hide();
				var set = $("#opt-content").offset();
				$("#addMenu").css({top:(event.pageY-set.top + $("#opt-content").scrollTop())+"px",left:(event.pageX-set.left)+"px"})
							.show();
				$("#addMenu").attr("targetId",this.id);
			}
		});
	}
	
	function bindLightContextMenu(obj){
		obj.mouseup(function(event){
			if(event.which==3){
				$(".context-menu-list").hide();
				var set = $("#opt-content").offset();
				$("#allMenu").css({top:(event.pageY-set.top + $("#opt-content").scrollTop())+"px",left:(event.pageX-set.left)+"px"})
							.show();
				$("#allMenu").attr("targetId",this.id);
			}
		});
	}
	
	function afterSaveLight(result){
		if($("#light" + result).length==0){
			lights.push(result);
		}else{
			for(var i=0;i<lights.length;i++){
				var i=lights[i];
				alert(i);
				if(i.id == result.id){
					i.color = result.color;
					break;
				}
			}
		}
		$(".light").remove();
		createLights(lights);
	}
	
	function loadPage(){
		window.parent.location.reload();
	} 
	
	function loadImage(){
		var url = '${goalctx}/goal-statistics/upload.htm';
		$.colorbox({href:url,iframe:true, innerWidth:400, innerHeight:200,
			overlayClose:false,
			title:"上传图片",
			onClosed:function(){
				$.post("${goalctx}/goal-statistics/get-src.htm",function(result){
					if(result.error){
						alert(result.message);
					}else{
						setMonitorImage(result);
					}
				},'json');
			}
		});
	}
	
	function setMonitorImage(result){
		if(result!=null){
			for(var i=0;i<result.length;i++){
				var obj = result[i];
				$('#monitorImage').attr("src","${ctx}/images/goal/"+obj.name);
			}
		}
	}
	//上传附件
	function uploadFiles(){
		var monitorProgramId=$("#monitorProgramId").val();
		$.upload({
			appendTo : '#opt-content',
			callback:function(files){
				if(files.length>0){
					$.post("${spcctx}/base-info/monitor-program/save-attach.htm?attachUrl="+files[0].id+"&&id="+monitorProgramId, {}, function(result) {},'json');
					$("#monitorImage").attr("src",$.getDownloadPath(files[0].id));
				}
			}
		});
	}
	//创建灯
	function createLights(lights){
		for(var i=0;i<lights.length;i++){
			var light = lights[i];
			var $image = $("#monitorImage");
			var imageWidth = $image.width(),imageHeight = $image.height();
			var html = "<div id='light"+light.id+"' class='light "+light.color+"-light' title="+light.name+" style='cursor:pointer;' ></div>";
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
			var $light = $("#light"+light.id)
				.css(pCss)
				.draggable({
					scroll : false,
					appendTo : '#opt-content',
					stop: function(event, ui) {
						for(var j=0;j<lights.length;j++){
							var l = lights[j];
							if('light'+l.id == this.id){
								l.myLeft = ui.position.left;
								l.myTop = ui.position.top;
								var $image = $("#monitorImage");
								l.imageWidth = $image.width();
								l.imageHeight = $image.height();
								l.monitorProgramId=$("#monitorProgramId").val();
								l.type='move';
								$.post("${spcctx}/base-info/monitor-point/save.htm?"+$.param(l), {}, function(result) {},'json');
								break;
							}
						}
					}
				});
			bindLightContextMenu($light);
		}
	}
</script>

<input type="hidden" name="monitorProgramId" id="monitorProgramId" value="${monitProgram.id}"/>
<img  width="100%" id="monitorImage" style="position:absolute;left:0px;top:0px;z-index:1"></img>
<div id="test1"></div>	
<!-- 添加的菜单 -->
<ul id="addMenu" class="context-menu-list">
	<li code="add">
		<a class="menuicon add" href="#"></a>
		<span class="label">添加</span>
	</li>
</ul>
<!-- 所有的菜单 -->
<ul id="allMenu" class="context-menu-list">
	<li code="add">
		<a class="menuicon add" href="#"></a>
		<span class="label">添加</span>
	</li>
	<li code="update">
		<a class="menuicon update" href="#"></a>
		<span class="label">修改</span>
	</li>
	<li code="delete">
		<a class="menuicon delete" href="#"></a>
		<span class="label">清除</span>
	</li>
</ul>
