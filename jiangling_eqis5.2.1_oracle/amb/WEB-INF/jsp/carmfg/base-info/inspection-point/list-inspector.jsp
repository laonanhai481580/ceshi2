<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	<title>选择检验员</title>
	<%@include file="/common/meta.jsp" %>	
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript">
	isUsingComonLayout=false;
	function changePosition(){}
	function delRow(rowId) {
		var ids = jQuery("#inspectorList").getGridParam('selarrrow');
		if(ids.length<1){
			alert("请选中需要删除的记录！");
			return;
		}
		$.post("${mfgctx}/base-info/inspection-point/delete-inspector.htm", {
			deleteIds : ids.join(',')
		}, function(data) {
			//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
			while (ids.length>0) {
				jQuery("#inspectorList").jqGrid('delRowData', ids[0]);
			}
		});

	}
	function closeBtn(){
		window.parent.$.colorbox.close();
		//window.parent.location='${mfgctx}/base-info/inspection-point/list.htm';
	}
	function selectPerson(){
		
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'true',
			hiddenInputId:'inspectorId',
			showInputId:'inspectorName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setInspector();
			}});
	}
	function setInspector(){
		var inspectorIds = $("#inspectorId").val().split(",");
		var inspectorName = $("#inspectorName").val().split(",");
		var rows = jQuery("#inspectorList").jqGrid('getRowData'); 
		var mydata = [];
		var isExit;
		for(var i=0;i<inspectorIds.length;i++){
			for(var y=0;y<rows.length;y++){
				if(inspectorIds[i]==rows[y].userId){
					isExit = true;
					break;
				}
			}
			
			if(!isExit){
				mydata.push({name:inspectorName[i],userId:inspectorIds[i]});
				
			}
			isExit = false;
		}
		for(var i=0;i<=mydata.length;i++){
			jQuery("#inspectorList").jqGrid('addRowData',i+1,mydata[i]);
		}
	}
	
	function saveAllInspectors(){
		var inspectorPointId = $('#inspectorPointId').val();
		
		var rows = jQuery("#inspectorList").jqGrid('getRowData');
		
		var paras=new Array();
		  for(var i=0;i<rows.length;i++){
		      var row=rows[i];
		      paras.push('{"name":"'+row.name+'","userId":"'+row.userId+'"}');
		  }
		$.post("${mfgctx}/base-info/inspection-point/save-inspector.htm",{
			params : '['+ paras.toString() + ']',inspectorPointId:inspectorPointId
		},function(data){
			showMsg();
			closeBtn();
		},'json');
	}
	</script>
	</head>
	
	<body>
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<button  class='btn' type="button" onclick="selectPerson();"><span><span><b class="btn-icons btn-icons-user"></b>选择</span></span></button>
					<security:authorize ifAnyGranted="mfg-base-info-inspection-point-save-inspector">
						<button  class='btn' type="button" onclick="saveAllInspectors()"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="mfg-base-info-inspection-point-delete-inspector">
						<button class='btn' onclick="delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					</div>
					<div id="opt-content" class="form-bg">
						<div style="display: none;" id="message"><font class=onSuccess><nobr>保存成功！</nobr></font></div>
						<form id="contentForm" name="contentForm" method="post"  action="" onsubmit="return false;">
							<input type="hidden"  value="${inspectorPointId}" name="inspectorPointId" id="inspectorPointId" />
							<input id="inspectorName" name="inspectorName" type="hidden"/>
							<input id="inspectorId" name="inspectorId" type="hidden"/>
							
							<table id="inspectorList"></table>
							<div id="pager"></div> 
							<script type="text/javascript">
								$(document).ready(function(){
									var inspectorPointId = $("#inspectorPointId").val();
									jQuery("#inspectorList").jqGrid({
										url:'${mfgctx}/base-info/inspection-point/list-inspector-datas.htm',
										postData:{inspectorPointId:inspectorPointId},
										rownumbers:true,
										height:390,
										colNames:['姓名','用户ID'],
										colModel:[
										          {name:'name', index:'name',width:240},
										          {name:'userId', index:'userId',width:240}
										          ]
									});
									
						       	});
								
								
							</script>
						</form>
					</div>
					
				</aa:zone>
			</div>
	</body>
</html>