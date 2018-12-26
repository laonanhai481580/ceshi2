<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="contentleadTable" id="portal_supplier_message_div">
	<table class="leadTable">
		<thead>
			<tr>
				<th >标题</th>
<!-- 				<th >发布人</th> -->
				<th >发布时间</th>
			</tr>
		</thead>
		<tbody id="portal_supplier_message"> 
			<s:iterator value="datas" var="item">
				<tr>
					<td>
						<a target='_blank' href='${hostApp}/osm/supplier-announcement/view.htm?_from=portal&&id=${item.id}'>
							${item.title}
						</a>
					</td>
<!-- 					<td> -->
<%-- 						${item.publisher} --%>
<!-- 					</td> -->
					<td style="width:25%">
						${item.releaseTime}
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>