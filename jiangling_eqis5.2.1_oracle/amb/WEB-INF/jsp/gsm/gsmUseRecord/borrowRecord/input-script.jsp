<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
//选择用户
function _selectUser(showInputId,hiddenInputId,treeValue,multiple){
	popZtree({
        leaf: {
            enable: false,
            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
        },
        type: {
            treeType: "MAN_DEPARTMENT_TREE",
            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
            noDeparmentUser:true,
            onlineVisible:true
        },   //organization1
        data: {
            treeNodeData:"id,loginName,name",
            chkStyle:"",
            chkboxType:"{'Y':'ps','N':'ps'}",
            departmentShow:""
        },
        view: {
            title: title,
            width: 300,
            height:400,
            url:'${ctx}'
        },
        feedback:{
            enable: true,
            showInput:showInputId,
            showThing:"{'name':'name'}",
            hiddenInput:hiddenInputId,
            hiddenThing:"{'id':'id'}",
            append:false
        },
        callback: {
            onClose:function(api){
                if(hiddenInputId){
                    var currentClickNodeData = api.single.getCurrentClickNodeData();
                    var user = $.parseJSON(currentClickNodeData);
                    $("#"+hiddenInputId).val(user.loginName);
                    loginName = user.loginName;
                    fieldName = hiddenInputId.split("_")[1];
                    //审核人员时带出主管和部门
                    if(fieldName == 'preserverLogin'){
                    	var rowId = hiddenInputId.split("_")[0];
                    	groupHeaderChange(loginName,rowId);
    					/**$.post("${supplierctx}/auditeck-plan/get-director.htm?auditGroupLeaderLogin="+loginName,{},function(result){
    						if(result!=null){
    							if(result.ParticipateInDept){
    								var $dept = $("#" + rowindex + "_ParticipateInDept");
	    							if($dept.length>0){
	    								$dept.val(result.ParticipateInDept);
	    							}
    							}
    						}
    					},"json");*/ 
    				}
                }
            }
        }
    });
}
//审核组长变化时
function groupHeaderChange(loginName,rowId){
	var url = '${supplierctx}/common/query-organization-by-loginname.htm?loginName='+loginName;
	$.post(url,function(result){
		var $inputObj = $("#" + rowId + "_ParticipateInDept");
		if(result.error){
			$inputObj.val("");
		}else{
			$inputObj.val(result.terrainName);
		}
	},'json');
}
var orderId="";
function Epm(obj){
	orderId = obj.id.split("_")[0];
	$.colorbox({href:"${gsmctx}/equipment/list-view.htm",iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择设备"
	});
}
function setEpmValue(objs){
	var obj = objs[0];
	$("#"+orderId+"_managerAssets").val(obj.managerAssets);
	$("#"+orderId+"_equipmentName").val(obj.equipmentName);
	$("#"+orderId+"_equipmentModel").val(obj.equipmentModel);
}


		
</script>