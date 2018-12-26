<%@page import="com.ambition.carmfg.entity.MfgCheckItem"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String baseUrl = request.getContextPath();
	String hisDatas = "[]";
	List<MfgCheckItem> checkItems = (List<MfgCheckItem>)request.getAttribute("checkItems");
	int checkItemsHeight=checkItems.size();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<style type="text/css">
	 	.collapse{
			height:49px;
			width:6px;
			cursor:pointer;
			background-image:url('<%=baseUrl%>/images/collapse.jpg');
		}
		.expand{
			width:6px;
			height:49px;
			cursor:pointer;
			background-image:url('<%=baseUrl%>/images/expand.jpg');
		} 
		ul{
			margin: 0px;
			padding: 0px;
		}
		li{
			list-style:none;
			margin:0px;
			text-align:center;
			font-weight:bold;
			background-color:green;
		}
	       html,body {
            overflow:hidden;
            margin:0px;
            width:100%;
            height:100%;
        }

        .virtual_body {
            width:100%;
            height:100%;
            overflow-y:scroll;
            overflow-x:auto;
        }
        .fixed_div {
            position:absolute;
            z-index:2008;
        }
	</style>
	<script type="text/javascript">
	  var icons = {
	      header: null,
	      activeHeader: null
	    };
	  var tempItemString;
		$(function() {
			jsTreeVal();
			tempItemString=parent.window.getTempItem();
			$(function() {
				$( "#accordion1" ).accordion({
					animated:false,
					collapsible:false,
					event:'click',
					fillSpace:true 
				});
			});
// 			resolutionString();
			/* setTimeout(function(){
				$("#left-right").height($(window).height()-70);
			},500); */
		});
		
		function resolutionString(){
			var tempItemStrs=tempItemString.split(",");
			for(var i=0;i<tempItemStrs.length;i++){
				if(tempItemStrs[i]!=""){
					$(":input[name="+tempItemStrs[i]+"]").parent().attr("style","background-color: yellow;");
					$(":input[itemName="+tempItemStrs[i]+"]").attr("disabled","disabled");
					$(":input[itemName="+tempItemStrs[i]+"]").attr("status");
					$(":input[parentName="+$(":input[itemName="+tempItemStrs[i]+"]").attr("status")+"]").attr("style","background-color: yellow;");
					$(":input[parentName="+$(":input[itemName="+tempItemStrs[i]+"]").attr("status")+"]").attr("disabled","disabled");
				}
			}
		}
		
		var isUsingComonLayout=false;
		function initLayout(){
			$(".opt-body").height(34);
			var height = $(window).height()-34;
			$(".left-left").height($(window).height());
			$(".left").width($("#header").width());
			$(".split").width(24);
			$("#left-tabs").height(height);
			$("#right-tabs").height(height);
			//左边的宽度
			var leftWidth = 160;
			$(".left-left").width(leftWidth);
			$(".left-right .main").width($(".left").width()-166);
		}
		function contentResize(obj){
			$(obj).removeClass();
			var display = $(".left-left").css("display");
			var width = 0;
			if(display=='none'){
				$(".left-left").css("display","table-cell");
				width = $(".left").width() - 166 - 10;
				$(obj).addClass("collapse").attr("title","Close");	
			}else{
				$(".left-left").css("display","none");
				width = $(".left").width() - 6 - 15;	
				$(obj).addClass("expand").attr("title","Open");		
			}
		}
		$(document).ready(function(){
			initLayout();
			$("#left-tabs").tabs();
			$("#right-tabs").tabs();
			 jsTreeVal();
		});

		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
		//定位
		function changeScrollIntoView(obj){
			var objName=$(obj).attr("name");
			var oDiv=document.getElementById(objName); 
			 var t=document.createElement("input");t.type="text";
			 oDiv.insertBefore(t,oDiv.firstChild);
			 t.focus();
			 oDiv.removeChild(t);
		}
		function changeItemStatus(obj){
			if($(obj).attr("checked")){
				$(":input[status="+$(obj).attr("parentName")+"]").attr("checked","checked");
				$(":input[status="+$(obj).attr("parentName")+"]").val("已领取");
				$(":input[status="+$(obj).attr("parentName")+"]").parent().attr("style","color:#00E5EE;");
			}else{
				$(":input[status="+$(obj).attr("parentName")+"]").removeAttr("checked");
				$(":input[status="+$(obj).attr("parentName")+"]").val("未领取");
				$(":input[status="+$(obj).attr("parentName")+"]").parent().removeAttr("style");
			}
			changeScrollIntoView(obj);
		}
		function jsTreeVal(){
		<%
			String dataHtml="<ul>",treeMenu="";
			
			for(MfgCheckItem item:checkItems){
				if(item.getParentItemName()!=null){
					treeMenu=item.getParentItemName();
					dataHtml+="<li><input parentName=\'"+item.getParentItemName()+"\' name=\'"+item.getParentItemName()+"\' type=\'checkbox\' id=\'checkbox"+item.getParentItemName()+"\' onclick=\'changeItemStatus(this);\'/>"+
					"<label for=\'checkbox"+item.getParentItemName()+"\'>全选【"+item.getParentItemName()+"】类</label></li>";
				}
			}
			dataHtml+="</ul>";
		%>
		var datastr="<%=dataHtml%>";
		var treeMenu="<%=treeMenu%>";
		$("#plant_parameter").jstree({ 
				"html_data" : {
					"data" :datastr
				},
				"ui" : {
					"initially_select" : [ treeMenu ]
				},
				"plugins" : [ "themes", "html_data","ui" ]
			});
		}
		function itemStatus(obj){
			if($(obj).attr("checked")){
				$(obj).parent().attr("style","color:#00E5EE;");
				var checked=true;
				$(":input[status="+$(obj).attr("status")+"]").each(function(index,obj){
					if(!$(obj).attr("checked")){
						checked=false;
					}
				});
				if(checked){
					$("#checkbox"+$(obj).attr("status")).attr("checked","checked");
				}
			}else{
				$(obj).parent().attr("style","color:#000000;");
				var parentNameItem=$(obj).attr("status");
				$("#checkbox"+parentNameItem).removeAttr("checked");
			}
		}
		
		//确认领取的检验任务
		function sureCheckItem(){
			//验证任务领取状况
			var id=$("#id").val();
			var datas=new Object();
			var parentColspan= new Object();
			var m=1;
			var tempparent="";
			var items="";
			$(":input[fieldName=itemStatus]:checked").each(function(index,obj){
				items+=$(obj).attr("itemName")+",";
				var parent=$(obj).attr("status");
				if(tempparent==parent){
					m++;
				}else{
					tempparent=parent;
					m=1;
				}
				if(tempparent!=""){
					parentColspan[parent]=m;
				}
			});
			var url="${iqcctx}/inspection-report/validate-item-status.htm";
			$.post(url,{"reportId":id,"items":items},function(result){
				var itemObj=eval("("+result+")");
				if(itemObj.itemNameStr!=""){
				}
			});
			if(items!=""){
				var itemArray=items.split(",");
				for(var i=0;i<itemArray.length;i++){
					datas[i+"_itemName"]=itemArray[i];
				}
			}
			if(window.parent.completeItem){
				window.parent.completeItem(datas,parentColspan);
				window.parent.$.colorbox.close();
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="opt-body" style="margin-bottom:2px;" id="header">
		<div class="opt-btn">
			<button  class='btn' type="button" onclick="sureCheckItem();"><span><span><b class="btn-icons btn-icons-ok"></b>确定领取项</span></span></button>	
			<button  class='btn' type="button" onclick="javascript:window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
		</div>
	</div>
	<div class="fixed_div" id="left-tabs" style="width:20%;height: 100%;">
	    <div id="left-tabs-1" style="width:100%;height: 100%;">
			<div id="accordion1"class="basic" >
				<h3><a>检验分类</a></h3>
				<div>
					<div  id="plant_parameter" class="demo">分类加载中,请稍候...</div>
				</div>
			</div>
		</div>
	</div>
	 <div class="virtual_body" style="width:80%;float: right;height: 100%;">
		<input type="hidden" id="id" name="id" value="${id}"/>
	    <div class="left-right,main" style="height:100%;text-align: center;height:<%=checkItemsHeight*30+250%>px;">
	       <table  class="form-table-border-left">
				<%
					StringBuffer flagIds = new StringBuffer("");
					int i=1,flag = 0;
					boolean isLast = false;
					String tempParentItemName="";
					for(MfgCheckItem item:checkItems){
						flag++;
						if(flag==checkItems.size()){
							isLast = true;
						}
						flagIds.append(",a" + flag);
				%>
				<tr>
						<%if(item.getParentItemName()!=null){ 
							tempParentItemName=item.getParentItemName();
						%>
						<td rowspan="<%=item.getParentRowSpan()%>" style="width:15%;text-align: center;font-weight: bold;"><a id="<%=item.getParentItemName()%>" ><%=item.getParentItemName()%></a></td>
						<%} %>
					<td checkItem="Info" style="height:20px;">
						&nbsp;&nbsp;&nbsp;
						<input status="<%=tempParentItemName%>" type="checkbox" fieldName="itemStatus" itemName="<%=item.getCheckItemName()%>" onclick="itemStatus(this);" name="a<%=flag %>_itemStatus" value="未领取"/><%=item.getCheckItemName() %>
						<input type="hidden" fieldName="checkItemName"  name="<%=item.getCheckItemName()==null?"":item.getCheckItemName().replaceAll("\n","")%>" value="<%=item.getCheckItemName()==null?"":item.getCheckItemName().replaceAll("\n","")%>"/>
					</td>
				</tr>
				<%}%>
			</table> 
	    </div>
	</div>
	<script type="text/javascript">
	$(function() {
		resolutionString();
	});
	</script>
</body>
</html>
