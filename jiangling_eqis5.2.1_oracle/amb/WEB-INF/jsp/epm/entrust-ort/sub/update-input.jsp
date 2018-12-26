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
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<button class='btn'
					onclick="saveForm('${epmctx}/entrust-ort/sub/update-after.htm')">
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
							<td colspan="2">修改页面</td>
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
							<td >不良数量</td>
							<td>
								<input style="width: 120px;float: left;" name="defectNumber" id="defectNumber" value="" ></input>
							</td>
						</tr>
						<tr>
							<td>备注</td>
							<td >
								<textarea stage="three" style="width:95%;" name="remark" >${remark }</textarea>
							</td>	
							<td >不良率</td>
							<td>
								<input style="width: 120px;float: left;" name="defectRate" id="defectRate" value="" ></input>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</aa:zone>
	</div>
</body>
</html>