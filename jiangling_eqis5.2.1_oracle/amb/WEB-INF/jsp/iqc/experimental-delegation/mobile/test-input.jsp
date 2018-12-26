<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>进货检验试验委托单</title>
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
		<a><img src="${ctx}/mobile/img/comeback.png"></a>
		<span>进货检验试验委托单</span>
	</nav>
 <form id="applicationForm" name="applicationForm" method="post">
<input type="hidden" name="id" id="id" value="${id}"  />
<input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
	<table class="all_tab">
		<tbody>
			<tr>
				<th style="text-align: right;">编号:${experimentalNo}</th>
			</tr>
			<tr>
				<td class="td_left">
					<span>样品名称sample</span>
				</td>
				<td class="td_right">
					<input type="text" id="sampleName" name="sampleName" class="text-inp" value="${sampleName}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>规格型号specification</span>
				</td>
				<td class="td_right">
					<input type="text" name="specificationModel" id="specificationModel" class="text-inp" value="${specificationModel}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>供应商supplier</span>
				</td>
				<td class="td_right">
					<input type="hidden" id="supplierCode" name="supplierCode" value="${supplierCode}"/>
					<input type="text" id="supplierName" name="supplierName" value="${supplierName}" class="text-inp">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>机种编号Product NO</span>
				</td>
				<td class="td_right">
					<input type="text" name="meachineNo" id="meachineNo" class="text-inp" value="${meachineNo}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>客户编号sample amount</span>
				</td>
				<td class="td_right">
					<input type="text" name="coustomerCode" id="coustomerCode" class="text-inp" value="${coustomerCode}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>批号Consignable date</span>
				</td>
				<td class="td_right">
					<input type="text" name="batchNo" id="batchNo" class="text-inp" value="${batchNo}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>紧急度Energency degree</span>
				</td>
				<td class="td_right">
					<s:select list="emergency_degree"
						listKey="value" 
						listValue="value" 
						name="emergencyDegree" 
						id="emergencyDegree" 
						emptyOption="true"
						onchange=""
						cssClass="text-inp"
						theme="simple">
					</s:select>	
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>样品数量sample amount</span>
				</td>
				<td class="td_right">
					<input type="text" name="sampleAmount" id="simpleAmount" class="text-inp" value="${simpleAmount}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>申请日期Consignable date</span>
				</td>
				<td class="td_right">
					<input type="date" name="consignableDate" id="consignableDate" class="text-inp" value="${consignableDate}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>申请人Consignor</span>
				</td>
				<td class="td_right">
					<input type="text" name="consignor" id="consignor" class="text-inp" value="${consignor}">
				</td>
			</tr>
			<tr>
				<td class="td_left">
					<span>申请部门Department</span>
				</td>
				<td class="td_right">
					<input type="text" name="consignDev" id="consignDev" class="text-inp" value="${consignDev}">
				</td>
			</tr>
		</tbody>
	</table>
  	<div class="admit_infor">
		<ul>
			<li>样品描述</br>Sample Discription</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select1" id="down_select1" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select1').click(function(){
			$('#sampleDiscription').slideToggle();
		});
	</script>
	<textarea id="sampleDiscription" name="sampleDiscription" >${sampleDiscription}</textarea>
  <div class="problem">
		<ul>
			<li class="other_li">测试项目Text Items</li>
			<li>
				<div><input type="radio" value="HSF" name="testProject" id="testProject_a" onclick="test(testProject_a)" <s:if test='%{testProject=="HSF"}' >checked="true"</s:if> value="HSF">HSF</div>
			</li>
<!--  			<li id="admit_infor_none">
				<input type="checkbox" name="testHsf" id="a1"  <s:if test='%{testHsfs.contains("RoHS")}'  >checked="true"</s:if> class="inputclass" disabled="true"><span class="spanclass" for="a1">RoHS</span>
				<input type="checkbox" name="testHsf" id="a2"  <s:if test='%{testHsfs.contains("HF")}'  >checked="true"</s:if> style="margin-left: 2rem;" class="inputclass" disabled="true"><span class="spanclass" for="a2">HF</span>
				<input type="checkbox" name="testHsf" id="a3" <s:if test='%{testHsfs.contains("VOC")}'  >checked="true"</s:if> style="margin-left: 2rem;" class="inputclass" disabled="true"><span class="spanclass" for="a3">VOC</span>
				<input type="checkbox" name="testHsf" id="a4"  <s:if test='%{testHsfs.contains("Sn")}'  >checked="true"</s:if> style="margin-left: 2rem;" class="inputclass" disabled="true"><span class="spanclass" for="a4">Sn</span>
				<input type="checkbox" name="testHsf" id="a5"  <s:if test='%{testHsfs.contains("Sb")}'  >checked="true"</s:if>style="margin-left: 2rem;" class="inputclass" disabled="true"><span class="spanclass" for="a5">Sb</span>	
			</li>  -->
			<li id="admit_infor_none">
				<s:iterator value="testHsfs" id="option">
					<input id="testHsf${option.id}" style="margin-left: 2rem;"   type="checkbox" class="inputclass"  <s:if test="testHsf.contains(#option.value)" > checked="checked" </s:if> value="${option.value}" name="testHsf" />
					<span class="spanclass" for="testHsf${option.id}" >${option.name}</span>
	   			</s:iterator>
			</li>
			<li>
				<div><input type="radio" value="ORT" name="testProject" id="testProject_b" onclick="test(testProject_b)" value="ORT"  <s:if test='%{testProject=="ORT"}'  >checked="true"</s:if>>ORT</div>
				<div><input type="text" name="testOrt"  id="a2" class="text-inp" disabled="true" value="${testOrt}"></div>
			</li>
			<li>
				<div><input type="radio" value="其他" name="testProject" id="testProject_c" onclick="test(testProject_c)" value="其他"  <s:if test='%{testProject=="其他"}'  >checked="true"</s:if>  >其他</div>
				<div><input type="text" name="testOther" id="a3" class="text-inp" disabled="true" value="${testOther}"></div>
			</li>
		</ul>
 </div>
   <script>
	function test(id){
		var t = $(id).attr('value');
		if(t=='HSF'){
			$('.inputclass').removeAttr('disabled','true');
			$('#a2').attr('disabled','true');
			document.getElementById('a2').value ='';
			$('#a3').attr('disabled','true');
			document.getElementById('a3').value ='';
		}else if(t=='ORT'){
			$('#a2').removeAttr('disabled','true');
			$('.inputclass').removeAttr('checked','checked');
			$('.inputclass').attr('disabled','true');
			$('#a3').attr('disabled','true');
			document.getElementById('a3').value ='';
		}else if(t=='其他'){
			$('#a3').removeAttr('disabled','true');
			$('.inputclass').removeAttr('checked','checked');
			$('.inputclass').attr('disabled','true');
			$('#a2').attr('disabled','true');
			document.getElementById('a2').value ='';
		}
	}
  </script>
  <input type="hidden" name="_childrenInfos" id="_childrenInfos" />
  <div class="operation">
		<ul id="operation_ul">
			<li class="operation_lifir">
				<div class="divspan"><span>操作</span></div>
				<div>
					<i style="background-image:url(${ctx}/mobile/img/add.png)" onclick="addoperation()"></i>
					<i style="background-image:url(${ctx}/mobile/img/jh.png)" onclick="deleteoperation()"></i>
				</div>
			</li>
			<li class="operation_lisec">
				<div class="divspan"><span>ORT测试项目</span></div>
				<div class="divspan"><span>测试结果</span></div>
			</li>
			<%int bcount=0; %>
		    <s:iterator value="_ortItems" var="ingredient" id="ingredient" status="st">
				<li class="operation_lithr">
					<div><input type=text class="text-inp" name="ortItemName" id="ortItemName_<%=bcount%>" value="${ortItemName }"></div>
					<div><input type=text class="text-inp" name="ortResult" id="ortResult_<%=bcount%>" value="${ortResult }"></div>
				</li>
			<% bcount++; %>
			</s:iterator>
		</ul>
		 <input type="hidden" name="ingredientItemTotal" id="ingredientItemTotal" value="<%=bcount%>"/>
	</div>
   <script>
   function addoperation(){
		var ulnode=document.getElementById('operation_ul');
		var linode=document.createElement('li');
		var total=parseInt($("#ingredientItemTotal").val())+1;
// 		linode.class="operation_lithr";
		linode.setAttribute('class','operation_lithr');
		linode.innerHTML="<div><input type=text class='text-inp' name='ortItemName' id='ortItemName_"+total+"'/></div><div><input type=text class='text-inp' name='ortResult' id='ortResult_"+total+"'/></div>";
		ulnode.insertBefore(linode, ulnode.children[linode.length-1]);
		$("#ingredientItemTotal").val(total);
	}
	function deleteoperation(){
		var li=document.getElementById('operation_ul').getElementsByClassName('operation_lithr');
		var x=li.length;
// 		document.getElementById('operation_ul').getElementsByClassName('operation_lithr').removeChild(li[x-1]);
		if(x > 1){
			document.getElementById('operation_ul').removeChild(li[x-1]); 
			var total=parseInt($("#ingredientItemTotal").val());
			$("#ingredientItemTotal").val(total-1);
		}else{
			alert("至少保留一行");
		}
		
	}
// 	function addoperation(){
// 		var ulnode=document.getElementById('operation_ul');
// 		var linode=document.createElement('li');
// 		var total=parseInt($("#ingredientItemTotal").val())+1;
// 		linode.class="operation_lithr";
// 		linode.innerHTML="<li class='operation_lithr'><div><input type=text class='text-inp' name='ortItemName' id='ortItemName_"+total+"'/></div><div><input type=text class='text-inp' name='ortResult' id='ortResult_"+total+"'/></div><li>";
// 		ulnode.insertBefore(linode, ulnode.children[3]);
// 		$("#ingredientItemTotal").val(total);
// 	}
// 	function deleteoperation(){
// 		var li=document.getElementById('operation_ul').getElementsByTagName('li');
// 		var x=li.length;
// 		if(x > 3){
// 			document.getElementById('operation_ul').removeChild(li[3]); 
// 		}
// 		var total=parseInt($("#ingredientItemTotal").val());
// 		$("#ingredientItemTotal").val(total-1);
// 	}
  </script>	
  	<div class="admit_infor">
		<ul>
			<li>实验目的</br>&nbsp&nbsp Purpose</li>
			<li class="admit_infor_li">
				<input type="checkbox" name="down_select2" id="down_select2" >
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		</ul>
	</div>
	<script>
		$('#down_select2').click(function(){
			$('#purpose').slideToggle();
		});
	</script>
	<textarea id="purpose" name="purpose" >${purpose}</textarea>  
  <div class="syq">
	<ul class="syq_1">
		<li>
			<div class="syq_2">
				<span>实验结果Test Result</span>
			</div>
			<div><input type="text" name="experimentalResult" id="experimentalResult" value="${experimentalResult}"></div>
		</li>		
		<li>
			<div class="polling_sell_1">
				<p><span>实验员Operator</span></p>
			</div>
			<div class="polling_sell_2">
				<input type="text" name="experimentalMan" id="experimentalMan" value="${experimentalMan }">
				<input type="hidden" name="experimentalManLogin" id="experimentalManLogin" value="${experimentalManLogin }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('experimentalMan','experimentalManLogin')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('experimentalMan','experimentalManLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>					
		</li>		
		<li>
			<div class="syq_2">
				<span>测试报告编号Report NO</span>
			</div>
			<div><input type="text" name="reportFormNo" id="reportFormNo" value="${reportFormNo}"></div>
		</li>
	</ul>
 </div>
 <div class="polling">
	<ul class="polling_sell">
		<li class="company">实验室主管审核</li>
		<li>
			<div class="polling_sell_1">
				<p><span>审核人</span></p>
			</div>
			<div class="polling_sell_2">
				<input type="text" name="auditMan" id="auditMan" value="${auditMan }">
				<input type="hidden" name="auditManLogin" id="auditManLogin" value="${auditManLogin }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('auditMan','auditManLogin')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('auditMan','auditManLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>					
		</li>			
		<li>
			<div class="polling_sell_1"><p><span>审核日期</span></p></div>
			<div class="polling_sell_3">
				<input type="date" name="auditTime" id="auditTime" value="${auditTime}">
				
			</div>
		</li>
	</ul>
 </div>
 </form>
 <div class="endding" >
	<div class="give">
		<a class="polling_fdj" onclick="searchPerson('copyPersonUser','copyPerson');">抄送</a>
		<input type="text" name="copyPersonUser"  placeholder="选择抄送人" id="copyPersonUser">
		<input type="hidden" name="copyPerson"   id="copyPerson">
		<i onclick="del('copyPersonUser','copyPerson')"></i>
	</div>
	<div class="give">
		<a class="polling_fdj" onclick="searchPerson('assigneeUser','assignee');">指派</a>
		<input type="text" name="assigneeUser" value="" placeholder="选择指派人" id="assigneeUser">
		<input type="hidden" name="assignee" value=""  id="assignee">
		<i onclick="del('assigneeUser','assignee')"></i>
	</div>
	 <div class="buttos">
	<s:if test="taskId>0">
		   <s:if test="task.active==0">
			 <a onclick="saveForm();">保存</a>
			<s:if test="task.processingMode.name()==\"TYPE_EDIT\"">
				<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
			</s:if>
			<s:elseif test="task.processingMode.name()==\"TYPE_APPROVAL\"">
				  <a onclick="_completeTask('APPROVE');">同意</a><a onclick="_completeTask('REFUSE');">不同意</a>
			</s:elseif>
		</s:if>
	</s:if>
	<s:else>
		<a onclick="saveForm();">保存</a>
		<s:if test="taskId>0">
		<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
		</s:if>
		<s:else><a id="sub_tj" onclick="submitForm();">提交</a></s:else>
	</s:else>
	</div>
 </div>
 <div id="zzc" style="display:none"></div>
  <div id="nk" style="display:none">
	<div id="ym"><p><span>用户树</span><a id="closeId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div id="navigation"><button type="button" onclick="setInputValue();">确定</button><input type="text" name="searchTag" id="searchTag"><a href="javascript:;" onclick="searchElement()"><img src="${ctx}/mobile/img/fdj.png"></a></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
		<ul id="root" style="margin:0 6px;">
			<li>
				<label><a href="javascript:;" >欧菲光-CCM</a></label>
				${userTreeHtml}
			</li>
		</ul>
	</div>
 </div>
   <div id="file" style="display:none">
  <div id="ym"><p><span>上传附件</span><a id="filecloseId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
  <div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
	<form id="form1" enctype="multipart/form-data" method="post" action="${carmfgctx}/common/upload.htm">
	  <ul id="root" style="margin:0 6px;">
	      <li style="margin-left:20px;">
				 <div class="row">
				      <label for="fileToUpload">选择文件</label><br/>
				      <input type="file" name="uploadFile" id="uploadFile" onchange="fileSelected();"/>
				 </div>
			 </li>
			 <li style="margin-left:5px;">
			    <div id="fileNameDiv"><input style="width:150px;" name="uploadFileName" readonly=readonly id="uploadFileName"/></div>
			 </li>
			 <li style="margin-left:20px;">
				<div class="row">
				  <input type="button" onclick="uploadFiles()" value="Upload" />
		       </div>
	       </li>
			 <li style="margin-left:20px;">
			   <div id="fileSize"></div>
			</li>
			<li style="margin-left:20px;">
			   <div id="fileType"></div>
			</li>
			
	       <li style="margin-left:20px;">
	          <div id="progressNumber"></div>
	       </li>
      </ul>
   </form>
   </div>
 </div>
<script type="text/javascript" >
	function addEvent(el,name,fn){
		if(el.addEventListener) return el.addEventListener(name,fn,false);
		return el.attachEvent('on'+name,fn);
	}
	
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
	function sibcheck(self){
		var sbi = self.parentNode.parentNode.parentNode.childNodes,n=0;
		for(var i=0;i<sbi.length;i++){
			if(sbi[i].nodeType != 1)
				n++;
			else if(sbi[i].getElementsByTagName('input')[0].checked)
				n++;
		}
		return n==sbi.length?true:false;
	}
	addEvent(document.getElementById('root'),'click',function(e){
		e = e||window.event;
		var target = e.target||e.srcElement;
		var tp = nextnode(target.parentNode.nextSibling);
		switch(target.nodeName){
			case 'A':
				if(tp&&tp.nodeName == 'UL'){
					if(tp.style.display != 'block' ){
						tp.style.display = 'block';
						prevnode(target.parentNode.previousSibling).className = 'ren';
					}else{
						tp.style.display = 'none';
						prevnode(target.parentNode.previousSibling).className = 'add';
					}	
				}
				break;
			case 'SPAN':
				var ap = nextnode(nextnode(target.nextSibling).nextSibling);
				if(ap.style.display != 'block' ){
					ap.style.display = 'block';
					target.className = 'ren';
				}else{
					ap.style.display = 'none';
					target.className = 'add';
				}
				break;
			case 'INPUT':
				if(target.checked){
					if(tp){
						var checkbox = tp.getElementsByTagName('input');
						for(var i=0;i<checkbox.length;i++)
							checkbox[i].checked = true;
					} 
				}else{
					if(tp){
						var checkbox = tp.getElementsByTagName('input');
						for(var i=0;i<checkbox.length;i++)
							checkbox[i].checked = false;
					}
				}
				parcheck(target,sibcheck(target));
				break;
		}
	});
	window.onload = function(){
		var labels = document.getElementById('root').getElementsByTagName('label');
		for(var i=0;i<labels.length;i++){
			var span = document.createElement('span');
			span.style.cssText ='display:inline-block;height:24px;vertical-align:middle;width:21px;cursor:pointer;';
			span.innerHTML = ' ';
			span.className = 'add';
			if(nextnode(labels[i].nextSibling)&&nextnode(labels[i].nextSibling).nodeName == 'UL')
				labels[i].parentNode.insertBefore(span,labels[i]);
			else
				labels[i].className = 'rem';
		}
	};
</script>

 <script>
	function del(id){
		document.getElementById(id).value="";
	} 
 </script>

<script>
</script>
</body>
</html>
