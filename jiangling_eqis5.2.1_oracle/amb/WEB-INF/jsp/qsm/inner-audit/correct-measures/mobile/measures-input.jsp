<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>不符合与纠正措施报告</title>
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
	<a><span class="cause_0"><img src="${ctx}/mobile/img/comeback.png"></span></a><span class="cause_1">不符合与纠正措施报告</span>
 </div>
 <form id="applicationForm" name="applicationForm" method="post">
 <input type="hidden" name="id" id="id" value="${id}"  />
 <input type="hidden" name="inputformortaskform" value="inputform"/>
<input name="taskId" id="taskId" value="${taskId}" type="hidden"/>
 	<div class="syq">
	<ul class="syq_1">
	<li>
			<div class="syq_2">
				<span>编号</span>
			</div>
			<div>${formNo }</div>
	</li>
	<li>
			<div class="syq_2">
				<span>事业部</span>
			</div>
			<div >
				<s:select list="businessUnits"
					listKey="value" 
					listValue="name" 
					name="businessUnitCode" 
					id="businessUnitCode" 
					cssClass="method"
					onchange=""
					theme="simple">
				</s:select>
			</div>
	</li>
		<li>
			<div class="syq_2"><span>受审部门</span></div>
			<div >
				<input type="text" class="method" id="auditDept" name="auditDept" value="${auditDept }">
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>部门负责人</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="departmentLeader" name="departmentLeader" value="${departmentLeader }" >
				 <input type="hidden" name="departmentLeaderLogin" id="departmentLeaderLogin"  value="${departmentLeaderLogin}" />
				<a id="polling_fdj_4" class="polling_fdj" onclick="searchPerson('departmentLeader','departmentLeaderLogin')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_d" onclick="del('departmentLeader','departmentLeaderLogin')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>
		</li>
		<li>
			<div class="syq_2"><span>发起人</span></div>
			<div>
				<input type="text" id="setMan" name="setMan" value="${setMan }" class="method">
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>审核日期</span>
			</div>
			<div ><input type="date" isDate=true class="method" name="auditDate" id="auditDate"  value="<s:date name="auditDate" format="yyyy-MM-dd"/>"></div>
		</li>
		<li>
			<div class="syq_2">
				<span>审核类型</span>
			</div>
			<div>
				<s:select list="auditTypes"
					listKey="value" 
					listValue="name" 
					name="auditType" 
					id="auditType" 
					cssClass="method"
					onchange=""
					theme="simple" >
				</s:select>
			</div>
		</li>
	</ul>
</div>
<div class="problem">
	<ul >
		<li class="other_li">问题描述</li>
		<li>
			<div>不符合类型</div>
			<div >
				<s:select list="inconformityTypes"
					listKey="value" 
					listValue="name" 
					name="inconformityType" 
					id="inconformityType" 
					cssClass="method"
					onchange=""
					theme="simple">
				</s:select>
			</div>
		</li>
		<li>
			<div class="polling_sell_1"><p><span>审核</span></p></div>
			<div class="polling_sell_2">
				<input type="text" id="auditMan1" name="auditMan1" value="${auditMan1 }">
				 <input type="hidden" name="auditMan1Login" id="auditMan1Login"  value="${auditMan1Login}" />
				<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('auditMan1','auditMan1Login')">
					<img src="${ctx}/mobile/img/fdj.png">
				</a>
					<sub>
						<b>*</b>
					</sub>
				<a id="polling_ljt_a" onclick="del('auditMan1','auditMan1Login')">
					<img src="${ctx}/mobile/img/ljt.png">
				</a>
			</div>
		</li>
		<li>
			<div class="syq_2">
				<span>日期</span>
			</div>
			<div><input type="date" isDate=true class="method" name="auditMan1Date" id="auditMan1Date" value="<s:date name="auditMan1Date" format="yyyy-MM-dd"/>"></div>
		</li>
		<li>
			<div class="upload_c" >
			<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showattachment1','attachment1');">上传附件</a></span>
			<input type="hidden" name="attachment1" id="attachment1" value="${attachment1}">
			<input type="hidden" name="hisattachment1" id="hisattachment1" value="${hisattachment1}"><br/>
			
			</div>
			<div class="upload_b"><p id="showattachment1"></p></div>
		</li>	
	</ul>
</div>
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
				<div class="divspan"><span>不符合标准条款</span></div>
			</li>
			<%int bcount=0; %>
		    <s:iterator value="_correctMeasuresItems" var="item" id="item" status="st">
				<li class="operation_lithr">
					<div><input type=text class="textwidth" name="clauseName" id="clauseName_<%=bcount%>" value="${clauseName }"></div>
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
//			 		linode.class="operation_lithr";
					linode.setAttribute('class','operation_lithr');
					linode.innerHTML="<div><input type=text class='textwidth' name='clauseName' id='clauseName_"+total+"'/></div>";
					ulnode.insertBefore(linode, ulnode.children[linode.length-1]);
					$("#ingredientItemTotal").val(total);
				}
				function deleteoperation(){
					var li=document.getElementById('operation_ul').getElementsByClassName('operation_lithr');
					var x=li.length;
//			 		document.getElementById('operation_ul').getElementsByClassName('operation_lithr').removeChild(li[x-1]);
					if(x > 1){
						document.getElementById('operation_ul').removeChild(li[x-1]); 
						var total=parseInt($("#ingredientItemTotal").val());
						$("#ingredientItemTotal").val(total-1);
					}else{
						alert("至少保留一行");
					}
					
				}
		</script>
<div class="admit_infor">
	<ul>
		<li>不符合事实陈述</li>
		<li class="admit_infor_li">
		<input type="checkbox" class="textbox" name="remark1" id="remark1" onclick="fillcontent('assess')">
		<img src="${ctx}/mobile/img/ht.png"></li>
	 </ul>
 </div>
 <textarea id="assess" class="fill_content"></textarea>
 <div class="problem">
		<ul>
			<li class="other_li">原因分析</li>
			<li>
				<div>改善担当</div>
				<div class="polling_sell_2">
					<input type="text" id="auditMan2" name="auditMan2" value="${auditMan2 }">
					 <input type="hidden" name="auditMan2Login" id="auditMan2Login"  value="${auditMan2Login}" />
					<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('auditMan2','auditMan2Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_a" onclick="del('auditMan2','auditMan2Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
			<li>
				<div>日期</div>
				<div><input type="date" name="auditMan2Date" id="auditMan2Date" class="method"></div>
			</li>
		</ul>
	</div>
 <div class="admit_infor">
	 <ul>
		<li>原因分析</li>
		<li class="admit_infor_li">
			<input type="checkbox" class="textbox" name="remark2" id="remark2" onclick="fillcontent('assess1')">
			<img src="${ctx}/mobile/img/ht.png">
		</li>
	 </ul>
    </div>
   <textarea id="assess1" class="fill_content"></textarea>
 <div class="problem">
		<ul>
			<li class="other_li">纠正措施</li>
			<li>
				<div>改善担当</div>
				<div class="polling_sell_2">
					<input type="text" id="auditMan3" name="auditMan3" value="${auditMan3 }">
					 <input type="hidden" name="auditMan3Login" id="auditMan3Login"  value="${auditMan3Login}" />
					<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('auditMan3','auditMan3Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_a" onclick="del('auditMan3','auditMan3Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
			<li>
				<div>日期</div>
				<div><input type="date" name="auditMan3Date" id="auditMan3Date" class="method"></div>
			</li>
			<li>
				<div>预计完成时间</div>
				<div><input type="date" name="departmentLeader3Date" id="departmentLeader3Date" class="method"></div>
			</li>
			<li>
				<div>审核员</div>
				<div><input type="text" name="departmentLeader3" id="departmentLeader3" class="method"></div>
			</li>
			<li>
				<div class="upload_c" >
				<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showattachment3','attachment3');">上传附件</a></span>
				<input type="hidden" name="attachment3" id="attachment3" value="${attachment3}">
				<input type="hidden" name="hisattachment3" id="hisattachment3" value="${hisattachment3}"><br/>
				
				</div>
				<div class="upload_b"><p id="showattachment3"></p></div>
			</li>	
		</ul>
	</div>
 <div class="admit_infor">
	 <ul>
		<li>纠正措施</li>
		<li class="admit_infor_li">
			<input type="checkbox" class="textbox" name="remark3" id="remark3" onclick="fillcontent('assess3')">
			<img src="${ctx}/mobile/img/ht.png">
		</li>
	  </ul>
    </div>
    <textarea id="assess3" class="fill_content"></textarea>
	<div class="problem">
		<ul>
			<li class="other_li">纠正措施完成情况</li>
			<li>
				<div>改善担当</div>
				<div class="polling_sell_2">
					<input type="text" id="auditMan4" name="auditMan4" value="${auditMan4 }" class="partOne">
					 <input type="hidden" name="auditMan4Login" id="auditMan4Login"  value="${auditMan3Login}" />
					<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('auditMan4','auditMan4Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_a" onclick="del('auditMan4','auditMan4Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
			<li>
				<div>日期</div>
				<div><input type="date" name="auditMan4Date" id="auditMan4Date" class="method"></div>
			</li>
			<li>
				<div>负责人确认</div>
				<div class="polling_sell_2">
					<input type="text" id="departmentLeader4" name="departmentLeader4" value="${departmentLeader4 }" class="partOne">
					 <input type="hidden" name="departmentLeader4Login" id="departmentLeader4Login"  value="${departmentLeader4Login}" />
					<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('departmentLeader4','departmentLeader4Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_a" onclick="del('departmentLeader4','departmentLeader4Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
			<li>
				<div>日期</div>
				<div><input type="date" name="departmentLeader4Date" id="departmentLeader4Date" class="method"></div>
			</li>
			<li>
				<div class="upload_c" >
				<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showattachment4','attachment4');">上传附件</a></span>
				<input type="hidden" name="attachment4" id="attachment4" value="${attachment4}">
				<input type="hidden" name="hisattachment4" id="hisattachment4" value="${hisattachment4}"><br/>
				
				</div>
				<div class="upload_b"><p id="showattachment4"></p></div>
			</li>	
		</ul>
	</div>
	<div class="admit_infor">
		 <ul>
			<li>纠正措施完成情况</li>
			<li class="admit_infor_li">
				<input type="checkbox" class="textbox" name="remark4" id="remark4" onclick="fillcontent('assess4')">
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		  </ul>
    </div>
    <textarea id="assess4" class="fill_content"></textarea>
	<div class="problem">
		<ul>
			<li class="other_li">措施验证</li>
			<li>
				<div>审核员</div>
				<div class="polling_sell_2">
					<input type="text" id="auditMan5" name="auditMan5" value="${auditMan5 }">
					 <input type="hidden" name="auditMan5Login" id="auditMan5Login"  value="${auditMan5Login}" />
					<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('auditMan5','auditMan5Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_a" onclick="del('auditMan5','auditMan5Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
			<li>
				<div>日期</div>
				<div><input type="date" name="auditMan5Date" id="auditMan5Date" class="method"></div>
			</li>
			<li>
				<div class="upload_c" >
				<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showattachment5','attachment5');">上传附件</a></span>
				<input type="hidden" name="attachment5" id="attachment5" value="${attachment5}">
				<input type="hidden" name="hisattachment5" id="hisattachment5" value="${hisattachment5}"><br/>
				
				</div>
				<div class="upload_b"><p id="showattachment5"></p></div>
			</li>	
		</ul>
	</div>
	<div class="admit_infor">
		 <ul>
			<li>措施验证</li>
			<li class="admit_infor_li">
				<input type="checkbox" class="textbox" name="remark5" id="remark5" onclick="fillcontent('assess5')">
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		  </ul>
    </div>
    <textarea id="assess5" class="fill_content"></textarea>
	<div class="problem">
		<ul >
			<li class="other_li">问题关闭</li>
			<li>
				<div>组长确认</div>
				<div class="polling_sell_2">
					<input type="text" id="auditMan6" name="auditMan6" value="${auditMan6 }">
					 <input type="hidden" name="auditMan6Login" id="auditMan6Login"  value="${auditMan6Login}" />
					<a id="polling_fdj_1" class="polling_fdj" onclick="searchPerson('auditMan6','auditMan6Login')">
						<img src="${ctx}/mobile/img/fdj.png">
					</a>
						<sub>
							<b>*</b>
						</sub>
					<a id="polling_ljt_a" onclick="del('auditMan6','auditMan6Login')">
						<img src="${ctx}/mobile/img/ljt.png">
					</a>
				</div>
			</li>
			<li>
				<div>日期</div>
				<div><input type="date" name="auditMan6Date" id="auditMan6Date" class="method"></div>
			</li>
			<li>
				<div class="upload_c" >
				<span><img src="${ctx}/mobile/img/sc1.png" /><a class="upload_c"  onclick="_uploadFiles('showattachment6','attachment6');">上传附件</a></span>
				<input type="hidden" name="attachment6" id="attachment6" value="${attachment6}">
				<input type="hidden" name="hisattachment6" id="hisattachment6" value="${hisattachment6}"><br/>
				
				</div>
				<div class="upload_b"><p id="showattachment6"></p></div>
			</li>	
		</ul>
	</div>
 	<div class="admit_infor">
		 <ul>
			<li>问题关闭</li>
			<li class="admit_infor_li">
				<input type="checkbox" class="textbox" name="remark6" id="remark6" onclick="fillcontent('assess6')">
				<img src="${ctx}/mobile/img/ht.png">
			</li>
		  </ul>
    </div>
    <textarea id="assess6" class="fill_content"></textarea>
 </form>
 <div class="endding" >
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
 <script>
		function fillcontent(id){
			$('#'+id).slideToggle();
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
// 	$(".polling_fdj").click(function(){
// 		 $("#nk").show();
// 		 $("#zzc").show();
// 	});
 
 });
</script>

</body>
</html>
