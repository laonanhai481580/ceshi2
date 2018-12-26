<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	var isUsingComonLayout=false;
	$(function(){
		//添加验证
		editFormValidate();
	});
	function editFormValidate(){
		$("#editForm").validate({
			submitHandler: function() {
				$(".opt_btn").find("button.btn").attr("disabled","disabled");
				aa.submit('editForm','','main',showMsg);
			}
		});
	}
	function saveForm(url){
		var params = {};
		if($("#editForm").valid()){			
			$("#editForm :input[name]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
		}else{
			return;
		};
		$("#message").html("正在保存处理结果,请稍候... ...");	
		$.post(encodeURI(url),params,function(result){
			if(result.error){
				$("#message").html("保存失败"+result.message);
			}else{
				$("#message").html(result.message);
			};
		},'json');
	}
	function supplierClick(){
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm?state=合格",iframe:true,
			width:$(window).width()<1000?$(window).width()-10:1000,
			height:$(window).height()<600?$(window).height()-10:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#supplierNameTarget").val(obj.name);
		$("#supplierCodeTarget").val(obj.code);
	} 	
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn'
					onclick="saveForm('${epmctx}/entrust-hsf/sub/update-after.htm')">
					<span><span><b class="btn-icons btn-icons-save"></b>保存
					</span>
					</span>
				</button>
				<span style="color:red;" id="message">
				</span>
			</div>
			<div id="opt-content">
				<form id="editForm" name="editForm" method="post" action="">
					<div id="hiddenDiv">
						<input type="hidden" name="id" id="id" value="${id }" />
					</div>
					<table  class="form-table-border-left" style="width:90%;margin: auto;border:0px;">

						<tr>
							<td colspan="4">修改页面</td>
						</tr>	
						<tr>						
							<td >测试后结果</td>
							<td>
								 <s:select list="testResults" 
								  theme="simple"
								  listKey="value" 
								  listValue="name" 
								  name="testAfter"
								  id="testAfter"
								  cssStyle="width: 120px;"
								  emptyOption="false"
								  ></s:select> 
							</td>
							<td >状态</td>
							<td>
								 <s:select list="updateStates" 
								  theme="simple"
								  listKey="value" 
								  listValue="name" 
								  name="state"
								  id="state"
								  cssStyle="width: 120px;"
								  emptyOption="false"
								  ></s:select> 
							</td>
						</tr>	
						<tr>
							<td colspan="4">目标供应商</td>
						</tr>	
						<tr>
							<td >名称</td>
							<td>
								<input style="width: 120px;float: left;" name="supplierNameTarget" id="supplierNameTarget" value="" class="{required:true,messages:{required:'必填'}}"></input>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="supplierClick()" href="javascript:void(0);" title="选择供应商"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
							</td>
							<td >代码</td>
							<td>
								<input style="width: 120px;float: left;" name="supplierCodeTarget" id="supplierCodeTarget" value="" class="{required:true,messages:{required:'必填'}}"></input>
							</td>
						</tr>							
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>