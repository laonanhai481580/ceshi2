<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>OBA数据录入表</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<link rel="shortcut icon">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${ctx}/mobile/css/sm.css">
<link rel="stylesheet" href="${ctx}/mobile/css/swiper.min.css">
<link rel="stylesheet" href="${ctx}/mobile/css/style.css">
<script type="text/javascript" src="${resourcesCtx}/js/jquery-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/date.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/iscroll.js"></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
<jsp:include page="script.jsp"/>
<%@ include file="style.jsp"%>
 </head>
 <body>
	<nav class="nav_head">
		<%-- <a><img src="${ctx}/mobile/img/comeback.png"></a> --%>
		<span>OBA数据录入表</span>
	</nav>
 <form id="applicationForm" name="applicationForm" method="post">
 	<input type="hidden" name="businessUnit" id="businessUnit" value="${businessUnit}"  />
 	<input type="hidden" name="id" id="id" value="${id}"  />
 	<input type="hidden" name="vlrrItems" id="vlrrItems" value=""  />
	<table class="all_tab">
		<tbody>
			<tr>
				<td class="td_left">
					<span>日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="obaDate" id="obaDate" class="text-inp"  value="<s:date name='obaDate' format="yyyy-MM-dd"/>">
					
				</td>
			</tr>		
			<tr>
				<td class="td_left">
					<span>客户名称</span>
				</td>
				<td class="td_right">
					<%-- <input type="text" name="customerName" id="customerName" class="text-inp" value="${customerName}" > --%> 
					<s:select list="customerNames"
						listKey="value" 
						listValue="name" 
						name="customerName" 
						id="customerName" 
						emptyOption="true"
						onchange="customerNameChange(this);"
						cssClass="text-inp"
						theme="simple">
					</s:select>					
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>场地</span>
				</td>
				<td class="td_right">
					<%-- <input type="text" name="customerFactory" id="customerFactory" class="text-inp" value="${customerFactory}" > --%> 
					<s:select list="customerPlaces"
						listKey="value" 
						listValue="name" 
						name="place" 
						id="place" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>						
				</td>
			</tr>				
			<tr>
				<td class="td_left">
					<span>客户机型</span>
				</td>
				<td class="td_right">
					<input type="text" name="customerModel" id="customerModel" class="text-inp" value="${customerModel}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>欧非机型</span>
				</td>
				<td class="td_right">
					<input type="text" name="ofilmModel" id="ofilmModel" class="text-inp" value="${ofilmModel}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>产品结构</span>
				</td>
				<td class="td_right">
					<%-- <input type="text" name="productStructure" id="productStructure" class="text-inp" value="${productStructure}" > --%>
					<s:select list="productStructures"
						listKey="value" 
						listValue="name" 
						name="productStructure" 
						id="productStructure" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>						
				</td>
			</tr>	
			<tr>
				<td class="td_left">
					<span>线别</span>
				</td>
				<td class="td_right">
					<input type="text" name="productionLine" id="productionLine" class="text-inp" value="${productionLine}" >
				</td>
			</tr>	
			<tr>
				<td class="td_left">
					<span>工序</span>
				</td>
				<td class="td_right">
					<input type="text" name="workingProcedure" id="workingProcedure" class="text-inp" value="${workingProcedure}" >
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>生产日期</span>
				</td>
				<td class="td_right">
					<input type="date" name="produceDate" id="produceDate" class="text-inp" value="<s:date name='produceDate' format="yyyy-MM-dd"/>">
				</td>
			</tr>							
			<tr>
				<td class="td_left">
					<span>投入数</span>
				</td>
				<td class="td_right">
					<input type="text" name="inputCount" id="inputCount" class="text-inp" value="${inputCount}" onchange="caculateBadRate(this);">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>不良数</span>
				</td>
				<td class="td_right">
					<input type="text" name="unqualifiedCount" id="unqualifiedCount" class="text-inp" value="${unqualifiedCount}" onchange="caculateBadRate(this);">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>不良率</span>
				</td>
				<td class="td_right">
					<input type="text" name="unqualifiedRate" id="unqualifiedRate" readonly="readonly" class="text-inp" value="${unqualifiedRate}">
				</td>
			</tr>									
		</tbody>
	</table>
	<% 	
		List<Map<String,String>> defectionList=(List<Map<String,String>>)ActionContext.getContext().get("defectionList");
		Map<String,Object> valueMap=(Map<String,Object >)ActionContext.getContext().get("valueMap");
	   for (Map<String,String> map:defectionList) {
	%>		
	<div class="problem" >
		<ul>
			<li class="other_li" style="background-color:#00ffe5;"><%=map.get("typeName")%></li>
			 <% for (String itemCode:map.keySet()) {
				 if(!itemCode.equals("typeName")){
				 
			%>	
			<li>
				<div><%=map.get(itemCode)%></div>
				<div><input type="text" style="width: 55%;float: left;" vlrr=true name="<%=itemCode%>_itemName" id="<%=itemCode%>_itemName" class="text-inp" value="<%=valueMap.get(itemCode)==null?"":valueMap.get(itemCode)%>" onkeyup="mustNum(this);"><span id="<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></div>				
			</li>
			<% 					
				 }}
			%>	
		</ul>
	</div>	
	<% 					
        }
	%>	
 </form>	 
 <div class="endding" >
	<div class="buttos">
		<a onclick="saveForm();">保存</a>
	</div>
 </div>
<script type="text/javascript" >
	function nextnode(node){
		if(!node)return ;
		if(node.nodeType == 1)
			return node;
		if(node.nextSibling)
			return nextnode(node.nextSibling);
	} 
	function prevnode(node){
		if(!node)return ;
		if(node.nodeType == 1)
			return node;
		if(node.previousSibling)
			return prevnode(node.previousSibling);
	} 
	function parcheck(self,checked){
		var par =  prevnode(self.parentNode.parentNode.parentNode.previousSibling);
		if(par&&par.getElementsByTagName('input')[0]){
			par.getElementsByTagName('input')[0].checked = checked;
			parcheck(par.getElementsByTagName('input')[0],sibcheck(par.getElementsByTagName('input')[0]));
		}			
	}
	
</script>

 <script>
	function del(id){
		document.getElementById(id).value="";
	} 
 </script>

<script>
 

 $(function(){
	$("#closeId").click(function(){
		 $("#nk").hide();
		 $("#zzc").hide();
	});

	$(".polling_fdj").click(function(){
		 $("#nk").show();
		 $("#zzc").show();
	});
 
 });
</script>
<script>
	function addoperation(){
		var ulnode=document.getElementById('operation_ul');
		var linode=document.createElement('li');
		linode.class="operation_lithr";
		linode.innerHTML="<div><input type=text class='text-inp' name='bad_a'></div><div><input type=text class='text-inp' name='num_a'></div>";
		ulnode.insertBefore(linode, ulnode.children[3]);
	}
	function deleteoperation(){
		var li=document.getElementById('operation_ul').getElementsByTagName('li');
		var x=li.length;
		if(x > 3){
			document.getElementById('operation_ul').removeChild(li[3]); 
		}
	}
</script>
 </body>
</html>
