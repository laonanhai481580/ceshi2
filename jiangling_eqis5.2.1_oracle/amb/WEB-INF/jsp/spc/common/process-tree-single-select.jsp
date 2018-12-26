<%@ page import="com.ambition.spc.processdefine.service.ProcessDefineManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%Map<String,Integer> pointKeyMap = ProcessDefineManager.getPointKeyMap();%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
/******************页面使用方法********************/
/*  //选择过程范围
	function selectProcess(){
		$.colorbox({href:"${spcctx}/common/process-tree-select.htm",
			iframe:true, 
			innerWidth:350, 
			innerHeight:400,
			overlayClose:false,
			title:"选择过程范围"
		});
	} */
	//选择之后的方法 data格式{id:'a',name:'a',code:'a'}
	//function setProcessRange(datas){}

/********************页面使用方法结束********************/
	var params = '';
	$(document).ready(function(){
		$("#process-define-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "产品列表", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/process-define/point-list.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : [ "themes","json_data","ui","crrm","contextmenu",'types'],
			core : { "initially_open" : ["root"]},
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							//image:'${spcctx}/images/_drive.png'
						}
					}
				}
			}
		}).bind("select_node.jstree",function(e,data){
			var dataId = data.rslt.obj.attr("id");
			if(dataId == 'root'){
				selId=null;
				selName=null;
				return;
			}
			selId = dataId;
			selName = data.rslt.obj.attr("name");
			/**var level = ;
			if(level > 0){
				setFeatureTree(data.rslt.obj.attr("id"),data.rslt.obj.attr("name"));
			}*/
		});
	});
	var selId = null,selName = null;
	//确定
	function realSelect(){
		if(!selId){
			alert("请选择过程范围!");
			return;
		}
		if($.isFunction(window.parent.setProcessRange)){
			window.parent.setProcessRange([{id:selId,name:selName}]);
			window.parent.$.colorbox.close();
		}else{
			alert("页面还没有 setProcessRange 方法!");
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' onclick="realSelect();">
					<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
				</button>
				<button class='btn' onclick="cancel();">
					<span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span>
				</button>
			</div>
			<div id="opt-content" style="padding-top:6px;margin:0px;">
				<div id="process-define-tree" class="demo">菜单加载中,请稍候...</div>
			</div>
		</div>
	</div>
</body>
</html>