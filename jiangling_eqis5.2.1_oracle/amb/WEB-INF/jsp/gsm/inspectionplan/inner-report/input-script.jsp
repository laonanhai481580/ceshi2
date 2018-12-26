<%@page import="com.ambition.gsm.entity.CheckReportItem"%>
<%@page import="com.ambition.gsm.entity.CheckReportDetail"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="actionBaseCtx" value="${gsmctx}/inspectionplan/inner-report" />
<script type="text/javascript">
	
	function getScrollTop(){
		 return $("#opt-content").height()+23;
	}
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#checkReportDetails").position().top + $("#checkReportDetails").height()-18;
		if(tableTop<scrollTop){
			$("#scroll").hide();
		}else{
			$("#scroll").show();
		}
	}
	function initScrollDiv(){
		var width = $(".ui-layout-center").width()-30;
		var offset = $("#checkReportDetails").find("div").width(width).offset();
		var contentWidth =  $("#checkReportDetails").find("table").width();
		$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
	}
	function contentResize(){
		initScrollDiv();
		checkScrollDiv();
	}
	function  onLoadding(measurementName,measurementSpecification,manufacturer,id){
		var params = {
				measurementName : measurementName,
				measurementSpecification : measurementSpecification,
				manufacturer : manufacturer,
				id:id
			};
			$("#scroll").hide();
			$("#checkReportDetails").html("<div style='padding:4px;'><b>检验项目加载中,请稍候... ...</b></div>");
			var url = "${actionBaseCtx}/test-items.htm";
			$("#checkReportDetails").load(url,params,function(){
				children:{
					checkReportDetails:{
						entityClass:"<%=CheckReportDetail.class.getName()%>"//子表1实体全路径
					}
				}
				$.clearMessage();
				var contentWidth =  $("#checkReportDetails").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();  
			});
	}
	function judgeValue(){
		var  measurementName=$("#measurementName").val();
		var  measurementSpecification=$("#measurementSpecification").val();
		var  manufacturer=$("#manufacturer").val();
		var  id=$("#id").val();
		if(measurementName!=""&&measurementSpecification!=""&&manufacturer!=""){
			onLoadding(measurementName,measurementSpecification,manufacturer,id);
		}
	}
	//选择不良类别
	var idIndex=null;
	function standardItemClick(obj){
		idIndex = obj.id;
		var measurementName=$("#measurementName").val();
		if(!measurementName){
			alert("请先填写仪器名称！");
			return;
		}
		var url = '${gsmctx}/base/equipment-standard/standard-item-multi-select.htm?measurementName='+measurementName;
			$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"选择标准件"
			});
	}
	function setItemValue(datas){
		var idFirst = idIndex.split("_")[0];
		$("#"+idFirst+"_standardNo").val(datas[0].standardNo);
		$("#"+idFirst+"_standardName").val(datas[0].standardName);
		$("#"+idFirst+"_validityDate").val(datas[0].validityDate);
		$("#"+idFirst+"_certificateNo").val(datas[0].certificateNo);
 	}
	
	function selectPerson1(obj, hidden) {
		var data = {
			treeNodeData : "loginName,name",
			chkboxType : "{'Y' : 'ps', 'N' : 'ps' }",
			departmentShow : ''
		};
		var multiple = "true";
		if (multiple == "true") {
			data = {
				treeNodeData : "loginName,name",
				chkStyle : "checkbox",
				chkboxType : "{'Y' : 'ps', 'N' : 'ps' }",
				departmentShow : ''
			};
		}
		var zTreeSetting = {
			leaf : {
				enable : false,
				multiLeafJson : "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
			},
			type : {
				treeType : "MAN_DEPARTMENT_TREE",
				showContent : "[{'id':'id','code':'code','name':'name'}]",
				noDeparmentUser : false,
				onlineVisible : true
			},
			data : data,
			view : {
				title : "用户树",
				width : 300,
				height : 400,
				url : webRoot
			},
			feedback : {
				enable : true,
				showInput : "showInput",
				showThing : "{'name':'name'}",
				hiddenInput : "hiddenInput",
				hiddenThing : "{'loginName':'loginName'}",
				append : false
			},
			callback : {
				onClose : function(api) {
					if (multiple == "true") {
						$("#" + obj).val(ztree.getNames());
						$("#" + hidden).val(ztree.getLoginNames());
					} else {
						$("#" + showInputId).val(ztree.getName());
						$("#" + hiddenInputId).val(ztree.getLoginName());
					}
				}
			}
		};
		popZtree(zTreeSetting);
	}
	function copyPersonLoginName(showId, hidden, show, hiddenNames) {
		var names = $("#" + showId).val();
		var hiddenValue = $("#" + hidden).val();
		if (show != '') {
			var nameArry = names.split(",");
			var newNames = '';
			var hiddenArray = jstree.getLoginNames().split(",");
			var newHidden = '';
			for (var i = 0; i < nameArry.length; i++) {
				if (show.indexOf(nameArry[i]) < 0) {
					if (newNames == '') {
						newNames = nameArry[i];
					} else {
						newNames += "," + nameArry[i];
					}
				}
			}
			for (var i = 0; i < hiddenArray.length; i++) {
				if (hiddenValue.indexOf(hiddenArray[i]) < 0) {
					if (newHidden == '') {
						newHidden = hiddenArray[i];
					} else {
						newHidden += "," + hiddenArray[i];
					}
				}
			}
			$("#" + showId).val(show + "," + newNames);
			$('#' + hidden).attr("value", hiddenValue + "," + newHidden);
		} else {
			$('#' + hidden).attr("value", jstree.getLoginNames());
		}
	}
	function clearValue(showInputId, hiddenInputId) {
		$("#" + showInputId).val("");
		$("#" + hiddenInputId).val("");
	}
</script>