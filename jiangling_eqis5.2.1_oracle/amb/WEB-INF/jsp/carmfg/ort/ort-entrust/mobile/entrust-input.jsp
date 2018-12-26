<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>ort实验委托表</title>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
<link rel="shortcut icon">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${ctx}/mobile/css/sm.css">
<link rel="stylesheet" href="${ctx}/mobile/css/swiper.min.css">
<link rel="stylesheet" href="${ctx}/mobile/css/style.css">
<%@ include file="style.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/js/jquery-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<!-- <script src="https://cdn.bootcss.com/jquery/2.0.3/jquery.min.js"></script> -->
<%-- <script type="text/javascript" src="${ctx}/mobile/js/jquery-1.9.1.min.js"></script> --%>
<script type="text/javascript" src="${ctx}/mobile/js/date.js"></script>
<script type="text/javascript" src="${ctx}/mobile/js/iscroll.js"></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/validate-all-1.0.js" ></script>
<script type="text/javascript" src  ="${resourcesCtx}/widgets/validation/dynamic.validate.js" ></script>
 <%@ include file="input-script.jsp" %>
 </head>
 <body>
 <div class="cause">
	<a><span class="cause_0"><img src="${ctx}/mobile/img/comeback.png"></span></a><span class="cause_1">ort实验委托表</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
 <input  name="_childrenInfos" id="_childrenInfos" />
 <input type="hidden" name="id" id="id" value="${id}"  />
 <input type="hidden" name= "zibiao" id="zibiao" value=""/>
 <input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
 <div class="csh">
	<ul class="syq_1">
	<li>
			<div class="syq_2">
				<span>编号</span>
			</div>
			<div><input type="text" class="partOne" name="formNo" id="formNo" value="${formNo }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>样品名称</span>
			</div>
			<div><input type="text" class="partOne" name="simpleName" id="simpleName" value="${simpleName }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>规格型号</span>
			</div>
			<div><input type="text" class="partOne" name="specification" id="specification" value="${specification }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>供应商</span>
			</div>
			<div><input type="text" class="partOne" name="supplier" id="supplier" value="${supplier }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>机种编号</span>
			</div>
			<div><input type="text" class="partOne" name="productNo" id="productNo" value="${productNo }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>客户编号</span>
			</div>
			<div><input type="text" class="partOne" name="customerNo" id="customerNo" value="${customerNo }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>批号</span>
			</div>
			<div><input type="text" class="partOne" name="lotNo" id="lotNo" value="${lotNo }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>紧急度</span>
			</div>
			<div>
<!--      		<form action="" > -->
			<select class="method" name="emergencyDegree" id="emergencyDegree">
				<option>急</option>
				<option>正常</option>
			<select>
<!-- 			</form> -->
		</li>
		<li>
			<div class="syq_2"><span>样品数量</span></div>
			<div><input type="text" class="partOne" name="simpleAmount" id="simpleAmount" value="${simpleAmount }"></div>
		</li>
		<li>
			<div class="syq_2"><span>申请日期</span></div>
			<div><input type="date" isDate=true class="partOne" name="consignableDate" id="consignableDate" value="<s:date name="consignableDate" format="yyyy-MM-dd"/>"></div>
		</li>
		<li>
			<div class="syq_2"><span>申请人</span></div>
			<div><input type="text" class="partOne" name="consignor" id="consignor" value="${consignor }">
			</div>
		</li>
		<li>
			<div class="syq_2"><span>申请部门</span></div>
			<div><input type="text" class="partOne" name="department" id="department" value="${department }"></div>
		</li>
		<li>
			<div class="syq_2"><span>事业群</span></div>
			<div><input type="text" class="partOne" name="enterpriseGroup" id="enterpriseGroup" value="${enterpriseGroup }"></div>
		</li>
	</ul>
 </div>
  <div class="admit_infor">
	<ul>
		<li>样品描述</li>
		<li class="admit_infor_li"><input type="checkbox" name="down_select" id="down_select"><img src="${ctx}/mobile/img/ht.png"></li>
	</ul>
 </div>
 <textarea id="simpleDiscription"></textarea>
 <div class="problem">
	<ul >
		<li class="other_li">测试项目</li>
		<li>
			<div><input type="radio" value="HSF" name="testProject" id="testProject_a" onclick="test(testProject_a)">HSF</div>
		</li>
		<li id="admit_infor_none">
			<input type="checkbox" name="admit_select" class="inputclass" disabled="true"><span class="spanclass">RoHS</span></br>
			<input type="checkbox" name="admit_select" class="inputclass" disabled="true"><span class="spanclass">HF</span></br>
			<input type="checkbox" name="admit_select" class="inputclass" disabled="true"><span class="spanclass">VOC</span></br>
			<input type="checkbox" name="admit_select" class="inputclass" disabled="true"><span class="spanclass">Sn</span></br>
			<input type="checkbox" name="admit_select" class="inputclass" disabled="true"><span class="spanclass">Sb</span></br>
		</li>
		<li>
			<div><input type="radio" value="ORT" name="testProject" id="testProject_b" onclick="test(testProject_b)">ORT</div>
			<div><input type="text" name="testProject_2" id="a2" class="csxm_2" disabled="true" value=""></div>
		</li>
		<li>
			<div><input type="radio" value="其他" name="testProject" id="testProject_c" onclick="test(testProject_c)">其他</div>
			<div><input type="text" name="testProject_3" id="a3" class="csxm_2" disabled="true" value=""></div>
		</li>
	</ul>
 </div>
 <div class="testoperation">
		<div class="number">
			<p>序号</p>
			<p>
				<i style="background-image:url(${ctx}/mobile/img/add.png);" id="addnum"></i>
				<i style="background-image:url(${ctx}/mobile/img/jh.png);" id="delnum"></i>
			</p>
		</div>
		<div class="textcontent" id="addtextcontent">
		<%int bcount=0; %>
		   <s:iterator value="_testItems" var="items" id="items" status="ss">
			<ul class="ulcontent">
				<li id="addcontent_a"><span>ORT测试项目</span><input type="text" name="testItem" id="testItem_<%=bcount%>" value='${testItem }' class="textwidth"></li>
				<li id="addcontent_b"><span>测试条件</span><input type="text" name="testCondition" id="testCondition_<%=bcount%>" value='${testCondition }' class="textwidth"></li>
				<li id="addcontent_c"><span>判定标准</span><input type="text" name="judgeStandard" id="judgeStandard_<%=bcount%>" value='${judgeStandard }'  class="textwidth"></li>
				<li id="addcontent_d"><span>数量</span><input type="text" name="value" id="value_<%=bcount%>" value="${value }" class="textwidth"></li>
				<li id="addcontent_e"><span>结果</span><input type="text" name="testResult" id="testResult_<%=bcount%>" value="${testResult }" class="textwidth"></li>
			</ul>
			<% bcount++; %>
			</s:iterator>
			<input type="hidden" name="ingredientItemTotal" id="ingredientItemTotal" value="<%=bcount%>"/>
		</div>
		<script>
			
			$('#addnum').click(function(){
				var total=parseInt($("#ingredientItemTotal").val())+1;
				var node1=document.getElementById('addtextcontent');
				var node2=document.createElement('ul');
// 				node2.class="ulcontent";
                node2.setAttribute('class','ulcontent');
				node2.innerHTML='<li id="addcontent_a"><span>ORT测试项目</span><input type="text" name="testItem" id="testItem_"' + total +' value="${testItem }" class="textwidth"></li><li id="addcontent_b"><span>测试条件</span><input type="text" name="testCondition" id="testCondition_"'+total+' value="${testCondition }" class="textwidth"></li><li id="addcontent_c"><span>判定标准</span><input type="text" name="judgeStandard" id="judgeStandard_"'+total+' value="${judgeStandard }"  class="textwidth"></li><li id="addcontent_d"><span>数量</span><input type="text" name="value" id="value_"'+total+' value="${value }" class="textwidth"></li><li id="addcontent_e"><span>结果</span><input type="text" name="testResult" id="testResult_"'+total+' value="${testResult }" class="textwidth"></li>';
				node1.insertBefore(node2,node1.children[0]);
				$("#ingredientItemTotal").val(total);
			});
			$('#delnum').click(function(){
				var y=document.getElementById('addtextcontent').getElementsByTagName('ul');
				var z=y.length;
				if(z > 1){
					document.getElementById('addtextcontent').removeChild(y[1]);
					var total=parseInt($("#ingredientItemTotal").val());
					$("#ingredientItemTotal").val(total-1);
				}
			});
		</script>
	</div>
  <div class="admit_infor">
		<ul>
			<li>实验目的</li>
			<li class="admit_infor_li"><input type="checkbox" name="select_down" id="select_down"><img src="${ctx}/mobile/img/ht.png"></li>
		</ul>
  </div>
   <textarea id="Purpose"></textarea>
  <div class="polling">
	<ul class="polling_sell">
		<li>
			<div class="polling_sell_1"><span>实验员</span></div>
			<div class="polling_sell_2">
				<input type="text" name="operator" id="operator" value="${operator}">
				<input type="hidden" name="operatorLogin" id="operatorLogin" value="${operatorLogin }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('operator','operatorLogin')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('operator','operatorLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>	
		</li>
		<li>
			<div class="syq_2">
				<span>测试结果</span>
			</div>
			<div>
<!-- 				<form action="" > -->
					<select class="method" name="testResult" id="testResult">
						<option>PASS</option>
						<option>NG</option>
					<select>
<!-- 				</form> -->
			</div>
		</li>
		<li>
			<div class="syq_2"><span>报告编号</span></div>
			<div ><input type="text" class="partOne" name="reportNo" id="reportNo" value="${reportNo }"></div>
		</li>
	</ul>
 </div>
  <div class="syqr">
	<ul class="syq_1">
		<li class="check">
			主管审核
		</li>
		<li>
			<div class="syq_2"><span>审核人</span></div>
			<div class="polling_sell_2">
				<input type="text" style="width: 50%;" name="testDirector" id="testDirector" value="${testDirector }">
				<input type="hidden" name="testDirectorLogin" id="testDirectorLogin" value="${testDirectorLogin }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('testDirector','testDirectorLogin')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('testDirector','testDirectorLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>	
		</li>
		<li>
			<div class="syq_2"><span>审核日期</span></div>
			<div><input type="date" isDate=true class="partOne" name="shenheDate" id="shenheDate" value="<s:date name="shenheDate" format="yyyy-MM-dd"/>"></div>
		</li>
	</ul>
 </div>
 </form>
 <div class="endding" >
 <div style="color:red;" id="message"><s:actionmessage theme="mytheme"/></div>
	 <s:if test="taskId>0">
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
	</s:if>
	 <div class="buttos">
	<s:if test="taskId>0">
	        <a onclick="showProcessForm();">查看意见</a>
        <s:if test="isComplete==false">
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
	</s:if>
	<s:else>
	   <a onclick="showProcessForm();">查看意见</a>
	   <s:if test="isComplete==false">
			<a onclick="saveForm();">保存</a>
			<s:if test="taskId>0">
			<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
			</s:if>
			<s:else><a id="sub_tj" onclick="submitForm();">提交</a></s:else>
		</s:if>
	</s:else>
	</div>
	<div class="opinion">意见</div>
		<textarea id="opinionStr" onchange="setOpinionValue(this);" style="height: 3rem;  width: 90%;margin-left: 5%;" name="opinionStr" class="writetextarea"></textarea>
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
  <script>
	  $('#down_select').click(function(){
			$('#simpleDiscription').slideToggle();
		});
	  $('#select_down').click(function(){
			$('#Purpose').slideToggle();
		});

</script>
<script type="text/javascript">
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
		var par =  prevnode(self.parentNode.parentNode.parentNode.previousSibling),parspar;
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
						prevnode(target.parentNode.previousSibling).className = 'ren'
					}else{
						tp.style.display = 'none';
						prevnode(target.parentNode.previousSibling).className = 'add'
					}	
				}
				break;
			case 'SPAN':
				var ap = nextnode(nextnode(target.nextSibling).nextSibling);
				if(ap.style.display != 'block' ){
					ap.style.display = 'block';
					target.className = 'ren'
				}else{
					ap.style.display = 'none';
					target.className = 'add'
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
	$("#filecloseId").click(function(){
		 $("#file").hide();
		 $("#uploadFile").val("");
		 document.getElementById('uploadFileName').value =  '';
         document.getElementById('fileSize').innerHTML = '';
         document.getElementById('fileType').innerHTML = '';
         document.getElementById('progressNumber').innerHTML = '';
	});
	$(".polling_fdj").click(function(){
		 $("#nk").show();
		 $("#zzc").show();
	});
 
 });
</script>

</body>
</html>
