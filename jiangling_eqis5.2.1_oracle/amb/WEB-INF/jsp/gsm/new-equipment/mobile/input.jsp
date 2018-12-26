<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>新进检测设备申请登记表</title>
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
	<a><span class="cause_0"><img src="${ctx}/mobile/img/comeback.png"></span></a><span class="cause_1">实验委托单</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
 <input  name="_childrenInfos" id="_childrenInfos" />
 <input type="hidden" name="id" id="id" value="${id}"  />
<!--  <input type="hidden" name="opinion" id="opinion"> -->
 <input type="hidden" name= "zibiao" id="zibiao" value=""/>
 <input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
 <div class="csh">
	<ul class="syq_1">
	<li>
			<div class="syq_2">
				<span>编号</span>
			</div>
			<div><input type="text" class="partOne" name="code" id="code" value="${code }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>事业部</span>
			</div>
			<div>
                <s:select list="businessDivisions" 
					  theme="simple"
					  listKey="name" 
					  listValue="name" 
					  cssClass="method"
					  labelSeparator=""
					  name="businessDivision"
					  id="businessDivision"
					  emptyOption="false"></s:select>
            </div>
		</li>
		<li>
			<div class="syq_2">
				<span>申请部门</span>
			</div>
			<div><input type="text" class="partOne" name="applyDepartment" id="applyDepartment" value="${applyDepartment }"></div>
		</li>
		<li>
			<div class="polling_sell_1"><span>申请人</span></div>
			<div class="polling_sell_2">
				<input type="text" name="proposer" id="proposer" value="${proposer}">
				<input type="hidden" name="proposerLogin" id="proposerLogin" value="${proposerLogin }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('proposer','proposerLogin')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('operator','operatorLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>	
		</li>
		<li>
			<div class="syq_2">
				<span>工号</span>
			</div>
			<div><input type="text" class="partOne" name="jobNumber" id="jobNumber" value="${jobNumber }"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>日期</span>
			</div>
			<div><input type="date" isDate=true class="partOne" name="applyDate" id="applyDate" value="<s:date name="applyDate" format="yyyy-MM-dd"/>"></div>
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
		   <s:iterator value="_newEquipmentSublists" var="items" id="items" status="ss">
			<ul class="ulcontent">
				<li id="addcontent_a"><span>设备名称</span><input type="text" name="deviceName" id="deviceName_<%=bcount%>" value='${deviceName }' class="textwidth"></li>
				<li id="addcontent_b"><span>型号规格</span><input type="text" name="modelSpecification" id="modelSpecification_<%=bcount%>" value='${modelSpecification }' class="textwidth"></li>
				<li id="addcontent_c"><span>厂商</span><input type="text" name="manufacturer" id="manufacturer_<%=bcount%>" value='${manufacturer }'  class="textwidth"></li>
				<li id="addcontent_d"><span>出厂编号</span><input type="text" name="factoryNumber" id="factoryNumber_<%=bcount%>" value="${factoryNumber }" class="textwidth"></li>
				<li id="addcontent_e"><span>保管人</span><input type="text" name="preserver" id="preserver_<%=bcount%>" value="${preserver }" class="textwidth"></li>
				<li id="addcontent_f"><span>仪器管理编号</span><input type="text" name="nstrumentNumber" id="nstrumentNumber_<%=bcount%>" value="${nstrumentNumber }" class="textwidth"></li>
				<li id="addcontent_g"><span>备注</span><input type="text" name="remark" id="remark_<%=bcount%>" value="${remark }" class="textwidth"></li>
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
				node2.innerHTML='<li id="addcontent_a"><span>设备名称</span><input type="text" name="deviceName" id="deviceName_"' + total +' value="${deviceName }" class="textwidth"></li><li id="addcontent_b"><span>型号规格</span><input type="text" name="modelSpecification" id="modelSpecification_"'+total+' value="${modelSpecification }" class="textwidth"></li><li id="addcontent_c">'+
				'<span>厂商</span><input type="text" name="manufacturer" id="manufacturer_"'+total+' value="${manufacturer }"  class="textwidth"></li><li id="addcontent_d"><span>出厂编号</span><input type="text" name="factoryNumber" id="factoryNumber_"'+total+' value="${factoryNumber }" class="textwidth"></li><li id="addcontent_e">'+
				'<span>保管人</span><input type="text" name="preserver" id="preserver_"'+total+' value="${preserver }" class="textwidth"></li>'+
				'<span>仪器管理编号</span><input type="text" name="nstrumentNumber" id="nstrumentNumber_"'+total+' value="${nstrumentNumber }" class="textwidth"></li>'+
				'<span>备注</span><input type="text" name="remark" id="remark_"'+total+' value="${remark }" class="textwidth"></li>';
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
			<li>实验室意见</li>
			<li class="admit_infor_li"><input type="checkbox" name="select_down" id="select_down"><img src="${ctx}/mobile/img/ht.png"></li>
		</ul>
  </div>
   <textarea id="opinion"></textarea>
  <div class="polling">
	<ul class="polling_sell">
		<li>
			<div class="polling_sell_1"><span>确认人</span></div>
			<div class="polling_sell_2">
				<input type="text" name="confirmor" id="confirmor" value="${confirmor}">
				<input type="hidden" name="confirmorLogin" id="confirmorLogin" value="${confirmorLogin }">
				<a id="polling_fdj" class="polling_fdj" onclick="searchPerson('confirmor','confirmorLogin')" >
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<%-- <sub>	
						<b>*</b>
					</sub>
				<a id="polling_ljt" onclick="del('operator','operatorLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a> --%>
			</div>	
		</li>
		<li>
			<div class="syq_2">
				<span>确认时间</span>
			</div>
			<div>
			  <input type="date" isDate=true class="partOne" name="affirmDate" id="affirmDate" value="<s:date name="affirmDate" format="yyyy-MM-dd"/>">
			</div>
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
<!-- 	        <a onclick="showProcessForm();">查看意见</a> -->
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
<!-- 	   <a onclick="showProcessForm();">查看意见</a> -->
	   <s:if test="isComplete==false">
			<a onclick="saveForm();">保存</a>
			<s:if test="taskId>0">
			<a id="sub_tj" onclick="_completeTask('SUBMIT');">提交</a>
			</s:if>
			<s:else><a id="sub_tj" onclick="submitForm();">提交</a></s:else>
		</s:if>
	</s:else>
	</div>
<!-- 	<div class="opinion">意见</div> -->
<!-- 		<textarea id="opinionStr" onchange="setOpinionValue(this);" style="height: 3rem;  width: 90%;margin-left: 5%;" name="opinionStr" class="writetextarea"></textarea> -->
 </div>
 <div style="height:11rem;width: 100%"></div>

 
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
 <div id="processForm" style="display:none"></div>
 <div id="processFormNk" style="display:none">
 <div id="ym"><p><span>查看意见</span><a id="processFormId"><img src="${ctx}/mobile/img/cha.png" /></a></p></div>
	<div class="dsb" style="overflow-y:scroll; overflow-x:scroll;">
	   <%
		List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
		if(opinionParameters==null){
			opinionParameters = new ArrayList<Opinion>();
		}
	%>
	<table class="form-table-border-left" id="history-table" style="width:100%;margin: auto;">
		<tr height=28>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:100px;text-align: center">
				办理人
			</td>
			<td style="background:#99CCFF;font-weight: bold;font-size:14px;text-align: center">
				意见
			</td>
		</tr>
		<%
			int index = 1;
			for(Opinion param : opinionParameters){
		%>
			<tr height=24>
				<td>
					<%=param.getTransactorName() %>
				</td>
				<td>
					<%=param.getOpinion()==null?"":param.getOpinion() %>
				</td>
			</tr>
		<%} %>
	</table>
	</div>
 </div>
  <script>
	  $('#select_down').click(function(){
			$('#opinion').slideToggle();
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
	$("#processFormId").click(function(){
		 $("#processForm").hide();
		 $("#processFormNk").hide();
	});
 });
</script>

</body>
</html>
