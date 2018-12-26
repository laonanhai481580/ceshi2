<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>  
<%@page import="com.ambition.spc.entity.QualityFeature"%> 
<%@page import="java.util.List"%>  
<%@page import="com.ambition.spc.entity.MointCpkMoudle"%> 
<% 
	ValueStack valueStack2=(ValueStack)request.getAttribute("struts.valueStack");
	List<MointCpkMoudle> mointCpkMoudles =(List) valueStack2.findValue("mointCpkMoudles");
	DecimalFormat df=new DecimalFormat("0.000");
%>   
<%if(mointCpkMoudles!=null){ 
    for(int i=0;i<mointCpkMoudles.size();i++){
		MointCpkMoudle m=mointCpkMoudles.get(i);%> 
<div id="cpkTable<%=m.getId() %>" style="position: absolute;z-index: 1;border: 4;display:block;top:<%=m.getMyTop() %>px;left:<%=m.getMyLeft() %>px;" >
	<table style="background:#efefef;" class="form-table-border-left" >
	<tr><td>CPK</td><td><%= df.format(m.getCpk())%></td></tr>
	<tr><td>max</td><td><%= df.format(m.getMax())%></td></tr>
	<tr><td>min</td><td><%= df.format(m.getMin())%></td></tr>
	<tr><td>avg</td><td><%= df.format(m.getAvg())%></td></tr>
	</table>
	</div>
	 <script type="text/javascript">
	 $(document).ready( function() {
	 setTimeout(function(){
		 var $image = $("#monitorImage");
		 var imageWidth = $image.width(),imageHeight = $image.height();
		 var pCss = {'z-index' : 1,'display': 'block'};
		 if(imageWidth != <%=m.getImageWidth()%>){
					var top = <%=m.getMyTop() %> * imageHeight/ <%=m.getImageHeight()%>,left = <%=m.getMyLeft() %> * imageWidth/ <%=m.getImageWidth()%>;
					if(imageWidth > <%=m.getImageWidth()%>){
						pCss.top = (top + 1) +"px";
						pCss.left = (left + 1) +"px";
					}else{
						pCss.top = (top - 1) +"px";
						pCss.left = (left - 1) +"px";
					}
				}else{
					pCss.top = <%=m.getMyTop()%> + "px";
					pCss.left = <%=m.getMyLeft()%> + "px";
				}
			$("#cpkTable<%=m.getId() %>").css(pCss);
	 }, 2000);
	 });
	 </script> 
	 <%}}%>