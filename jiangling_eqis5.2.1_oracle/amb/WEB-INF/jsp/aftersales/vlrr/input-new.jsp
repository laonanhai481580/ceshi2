<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$(":input[isDate=true]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
	});
	function customerNameChange(){
		var customerName=$("#customerName").val();
		var url = "${aftersalesctx}/base-info/customer/place-select.htm?customerName="+customerName;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var places = result.places;
				var placeArr = places.split(",");
				var place = document.getElementById("customerFactory");
				place.options.length=0;
				var opp1 = new Option("","");
				place.add(opp1);
 				for(var i=0;i<placeArr.length;i++){
 					var opp = new Option(placeArr[i],placeArr[i]);
 					place.add(opp);
 				}
 			}
 		},'json');
	}	
	function selectBusinessUnit(obj){
		window.location.href = encodeURI('${aftersalesctx}/vlrr/input-new.htm?businessUnit='+ obj.value);
	}
	function addRowHtml(obj){
		var table = $(obj).closest("table");
		var clonetr = table.clone(false);
		table.after(clonetr);
		var total = $("#fir");
		var num = total.val();
		clonetr.find(":input").each(function(index ,obj){
			obj=$(obj);
			var fieldName=obj.attr("fieldName");
			obj.attr("id","a"+num+"_"+fieldName).val("");
			obj.attr("name","a"+num+"_"+fieldName);
		});
		$("#fir").val(parseInt(num)+1);
		$("#flagIds").val($("#flagIds").val()+","+"a"+num);
	}
	function removeRowHtml(obj){
		var table = $(obj).closest("table");
		var pre = table.prev("table").attr("name");
		var next = table.next("table").attr("name");
		if (next != undefined) {
			table.remove();
		} else if (pre != undefined) {
			table.remove();
		} else {
			alert('至少要保留一列');
		}
	}
 	function modelClick(){
		var customerName=$("#customerName").val();
		if(!customerName){
			alert("请先选择客户！");
			return;
		}
 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择机型"
 		});
 	}
 	function setProblemValue(datas){
 		$("#customerModel").val(datas[0].value);
 		$("#ofilmModel").val(datas[0].key);
 	}
	function customerNameChange(obj){
		var customerName=$("#factory").val();
		var url = "${aftersalesctx}/base-info/customer/place-select.htm?customerName="+customerName;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
 				var places = result.places;
				var placeArr = places.split(",");
				var place = document.getElementById("customerFactory");
				place.options.length=0;
				var opp1 = new Option("","");
				place.add(opp1);
 				for(var i=0;i<placeArr.length;i++){
 					var opp = new Option(placeArr[i],placeArr[i]);
 					place.add(opp);
 				}
 			}
 		},'json');
	}
	function caculateBadCount(obj){
		var id=obj.id;
		var flag=id.split("_")[0];		
		var badCount=0;
		var table = $(obj).closest("table");
		$(table).find(":input[isItem=true]").each(function(index,o){
			var count=o.value;
			if(count){
				badCount+=parseInt(count);
			};
		});
		if(badCount>0){
			$("#" + flag + "_unqualifiedCount").val(parseInt(badCount));
		};
		caculateBadRate(obj);
	}
	function caculateBadRate(obj){
		var id=obj.id;
		var flag=id.split("_")[0];		
		var inputCount = $("#" + flag + "_inputCount").val();
		var unqualifiedCount = $("#" + flag + "_unqualifiedCount").val();
		if(isNaN(inputCount)){
			$("#" + flag + "_inputCount").focus();
			$("#" + flag + "_unqualifiedRate").val("");
			return;
		}
		if(isNaN(unqualifiedCount)){
			$("#" + flag + "_unqualifiedCount").focus();
			$("#" + flag + "_unqualifiedRate").val("");
			return;
		}

		if((inputCount-0)<(unqualifiedCount-0)){
			alert("不良数不能大于投入数！");
			$("#" + flag + "_unqualifiedRate").val("");
			return;
		}
		var rate=unqualifiedCount*100/inputCount;
		$("#" + flag + "_unqualifiedRate").val(rate.toFixed(2)+"%");
	}
	function saveForm() {
		var isRight = false;
		<security:authorize ifAnyGranted="AFS_VLRR_DATA_SAVE">
			isRight =  true;
		</security:authorize>
		if(!isRight){
			alert("你没有权限保存！");
			return ;
		}
		if($("#inputform").valid()){
			var item=getItemDatas();
			var part=getPartDatas();
			var flagIds=$("#flagIds").val();
			var params = {};
			params["item"] = item;
			params["part"] = part;
			params["flagIds"] = flagIds;
			$("#message").html("正在保存，请稍候... ...");
			var url="${aftersalesctx}/vlrr/save-new.htm";
			$.post(encodeURI(url),params,function(result){
				if(result.error){
					$("#message").html("保存失败"+result.message);
				}else{
					$("#message").html(result.message);
					var i=0;
					$("table[itemData=true]").each(function(index,obj){
						if(i==0){
							$(obj).find(":input").each(function(index1,o){
								$(o).val("");
							});
						}else{
							$(obj).remove();
						}
						i++;
					});
				};
			},'json');
		}		
	}
	function getItemDatas(){
		var infovalue="";
		$("table[itemData=true]").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		var item ="["+infovalue.substring(1)+"]";
		return item;
	}
	function getPartDatas(){
		var value="";
		$("table[partData=true]").find(":input").each(function(index,obj){
			iobj = $(obj);
			value += ",\""+iobj.attr("name")+"\":\""+iobj.val()+"\"";
		});
		return "[{"+value.substring(1)+"}]";
	}
	function getTdItem(obj){
		var value="";
		$(obj).find(":input").each(function(index,obj){
			iobj = $(obj);
		    value += ",\""+iobj.attr("name").split("_")[1]+"\":\""+iobj.val()+"\"";
		});
		return ",{"+value.substring(1)+"}";
   	}
	function mustNum(obj){
		var id=obj.id;
		var value=obj.value;
		if(value&&isNaN(value)){
			$("#"+id+"_span").html("*必须为数字!");	
		}else{
			$("#"+id+"_span").html("");
		}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="vlrr";
		var thirdMenu="vlrr_data";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-vlrr-third-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="AFS_VLRR_DATA_SAVE">
					<button class='btn' onclick="saveForm();" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>
				<span style="color:red;" id="message">
					<s:actionmessage theme="mytheme" />
				</span>
			</div>
			<div><iframe id="iframe" style="display:none;"></iframe></div>
			<div id="opt-content" style="text-align: center;">
				<form id="inputform" name="inputform" method="post" action="">
					 <%@ include file="input-form.jsp"%>
				</form>		
			</div>
		</div>
	</div>
</body>
</html>