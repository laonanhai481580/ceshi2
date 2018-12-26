<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
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