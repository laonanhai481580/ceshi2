<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/tablednd/jquery.tablednd.js"></script>
<script type="text/javascript">
	function contentResize(){
		var tableHeight = $(".ui-layout-center").height();
		var tableWidth = $(".ui-layout-center").width()-204;
		$("#list1").jqGrid('setGridHeight',tableHeight-75);
		$("#list1").jqGrid('setGridWidth',tableWidth);
		$("#checkGradeTypeTree").height(tableHeight - 104);
	}
	$(document).ready(function(){
		$("#checkGradeTypeTree").jstree({ 
			"json_data" : {
				"data" : [
					{ 
						"attr" : { "id" : "-1"},
						"state" : "closed",
						"data" : { 
							"title" : "物料级别"
						},
					}
				],
				"ajax" : {
					"url" : "${mfgctx}/manu-analyse/materiel-node-load-datas.htm",
					data : function(node){
						return {id:node.attr("id")};
					}
				},
				"themes" : {  
		            "theme" : "default",  
		            "dots" : false,  
		            "icons" : false  
		        }, 
			},
			core : { "initially_open" : ["-1"] },
			"plugins" : [ "themes", "json_data", "checkbox", "ui"]
		}).bind("select_node.jstree",function(e,data){
			if($(data.rslt.obj).hasClass("jstree-closed")){
				$.jstree._reference("#checkGradeTypeTree").open_node($(data.rslt.obj),null,false);
			}else if($(data.rslt.obj).hasClass("jstree-open")){
				$.jstree._reference("#checkGradeTypeTree").close_node($(data.rslt.obj),null,false);
			}else{
				var childCount = $.jstree._reference("#checkGradeTypeTree")._get_children($(data.rslt.obj)).length;
				if(childCount == 0){
					loadCheckGradeByCheckGradeTypeId(data.rslt.obj.attr("id"),$.jstree._reference("#checkGradeTypeTree").get_text($(data.rslt.obj)));
				}
			}
		});
		$("button","#checkGradeToolbar").attr("disabled","disabled");
		contentResize();
	});
	var lastsel = null,editing=false;
	function makeEditable(editable) {
		if (editable) {
			editing = false;
			lastsel=null;
		} else {
			editing = true;
		}
	}
	function getMenuIds(){   
		 var authchecked="";
		 var checkedId="";
		 var information="";
		 var orderNum="";
		 var j=0;
		 $(".jstree-checked").each(function(i, element){
			 if($(element).attr("name")!=undefined){
				 authchecked +=$(element).attr("name") + ","; 
	             checkedId+=$(element).attr("id")+",";
	             orderNum=$(element).attr("orderNum");
	             if(orderNum=="1"){
                 	information+="一级物料大类为:【"+$(element).attr("name")+"】";
                 }else if(orderNum=="2"){
                 	information+="二级物料大类为:【"+$(element).attr("name")+"】";
                 }else if(orderNum=="3"){
                 	information+="三级物料大类为:【"+$(element).attr("name")+"】<br/>";
                 }
			 }
        });
		 if(window.parent.showCheckedMateriel){
			window.parent.showCheckedMateriel(authchecked,checkedId,information);
		 }
		 window.parent.$.colorbox.close(); 
	}
</script>
</head>
	<body onload="getContentHeight();">
	<div class="ui-layout-center">
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
						<button class='btn' onclick="getMenuIds();"><span><span>确定</span></span></button>
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content" class="form-bg">
						<form  id="materielForm" name=""materielForm"" method="post" action="">
							<div class="opt-body" id="checkGradeTypeTree" style="padding:6px;width:270px;overflow:auto;">
							</div>
						</form>
					</div>
				</aa:zone>
			</div>
		</div>
	</body>
</html>