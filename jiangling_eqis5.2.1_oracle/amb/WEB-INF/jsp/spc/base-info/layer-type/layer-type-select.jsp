<%@ page import="com.ambition.spc.processdefine.service.ProcessDefineManager"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%Map<String,Integer> pointKeyMap = ProcessDefineManager.getPointKeyMap();%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript">
/****************** 页面使用方法 *******************/
//选择判断规则
/* function selectLayerType(){
	var url='${spcctx}/layer-type/layer-type-select.htm';
	$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
		overlayClose:false,
		title:"选择层别信息"
	});
} */
	//选择之后的方法 data格式{id:'a',name:'a',code:'a'}
//function setLayerType(data){}

/****************** 页面使用方法结束 **********************/
	isUsingComonLayout=false;
	var multiselect = "${multiselect}";
	var params = '';
	$(document).ready(function(){
		contentResize();
		$("#layer-type-tree").jstree({
			json_data : {
				data : [
					{ 
						"data" : "层别类别", 
						"state" : "closed",
						attr:{
							id:'root',
							level : 0,
							rel:'drive'
						}
					}
				],
				ajax : { 
					"url" : "${spcctx}/base-info/layer-type/list-datas.htm",
					data : function(n){
						return {date:(new Date()).getTime()};	
					}
				}
			},
			plugins : ["themes", "json_data","checkbox","types","search"],
			core : { "initially_open" : ["root"] },
			types : {
				valid_children:'drive',
				types:{
					drive:{
						icon:{
							image:'${mfgctx}/images/_drive.png'
						}
					}
				}
			},
			"ui" : {
			},
			contextmenu : {
				items : {
				}
			}
		});
		
	});
	
	function contentResize(){
		var height = $(window).height() - 115;
		var display = $("#searchDiv").css("display");
		if(display=='block'){
			height -= $("#searchDiv").height();
			$(".search").html("隐藏查询");
		}else{
			height += 10;
			$(".search").html("显示查询");
		}
		$("#gridList").jqGrid("setGridHeight",height);
	}
	
	/* function getChild(node){
		var datas = [];
		if(node.childNodes.length>0){
			for(var i=0;i<node.childNodes.length;i++){
				var results = getChild(node.childNodes[i]);
				for(var j=0;j<results.length;j++){
					datas.push(results[j]);
				}
			}
		}else{
			datas.push({code:node.attr("code")});
		}
		return datas;
	} */
	//确定
	function realSelect(){
		var nodes = $("#layer-type-tree").find("li.jstree-checked");
		if(!nodes){
			alert("请选择层别信息!");
			return;
		}
		var objs=[];
		for(var i=0;i<nodes.length;i++){
			if(nodes[i]){
				if($(nodes[i]).attr("id")=='root'){
				}else{
					objs.push({
						id:$(nodes[i]).attr("id"),detailName:$(nodes[i]).attr("name"),detailCode:$(nodes[i]).attr("code"),sampleMethod:$(nodes[i]).attr("sampleMethod"),isInputValue:$(nodes[i]).attr("isInputValue")
					});
				}
			}
		}
		if($.isFunction(window.parent.setLayerType)){
			if(objs.length>0){
				window.parent.setLayerType(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("请选择层别信息!");
			}
		}else{
			alert("页面还没有 setLayerType 方法!");
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
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
			<div id="layer-type-tree" class="demo">菜单加载中,请稍候...</div>
		</div>
	</div>
</body>
</html>