<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<table class="form-table-border-left" style="border:0px; table-layout: fixed;">
	<tbody>
			<tr >	
              	<td  style="width:60px;text-align:center;border-top:0px;border-left:0px;">Number<br/>序号</td>
<!--               	<td  style="width:60px;text-align:center;border-top:0px;border-left:0px;"><br/>状态</td> -->
				<td  style="width:150px;text-align:center;border-top:0px;"> Component Structure Drawings<br/>拆解部件名称</td>
				<td  style="width:150px;text-align:center;border-top:0px;">Homogeneous material<br/>均质材料名称</td>
				<td style="width:150px;text-align:center;border-top:0px;">Homogeneous material Type name<br/>均质材料型号</td>
				<td  style="width:150px;text-align:center;border-top:0px;"><br/>均质材料属性</td>
				<td style="width:60px;text-align:center;border-top:0px;">Weight<br/>重量</td>
				<td style="width:60px;text-align:center;border-top:0px;">Unit<br/>单位</td>
				<td style="width:150px;text-align:center;border-top:0px;">Vender<br/>制造商</td>
				<td style="width:60px;text-align:center;border-top:0px;">Cd<br/>镉</td>
				<td style="width:60px;text-align:center;border-top:0px;">Pb<br/>铅</td>
				<td style="width:60px;text-align:center;border-top:0px;">Hg<br/>汞</td>
				<td style="width:60px;text-align:center;border-top:0px;">Cr+6<br/>六价铬</td>
				<td style="width:60px;text-align:center;border-top:0px;">PBBs<br/>多溴联苯</td>
				<td style="width:60px;text-align:center;border-top:0px;">PBDEs<br/>多溴联苯醚</td>
				<td style="width:60px;text-align:center;border-top:0px;">DEHP<br/>邻苯二甲酸</td>
				<td style="width:60px;text-align:center;border-top:0px;">BBP<br/>邻苯二甲酸丁苯酯</td>
				<td style="width:60px;text-align:center;border-top:0px;">DBP<br/>邻苯二甲酸二丁酯</td>
				<td style="width:60px;text-align:center;border-top:0px;">DIBP<br/>邻苯二甲酸儿異酯</td>
				<td style="width:60px;text-align:center;border-top:0px;">Br<br/>溴</td>
				<td style="width:60px;text-align:center;border-top:0px;">Cl<br/>氯</td>
				<td style="width:60px;text-align:center;border-top:0px;">Sb<br/>锑</td>
				<td style="width:60px;text-align:center;border-top:0px;">Be<br/>铍</td>
				<td style="width:60px;text-align:center;border-top:0px;">As<br/>砷</td>
				<td style="width:60px;text-align:center;border-top:0px;">PFOS<br/>全氟辛烷磺酸盐</td>
				<td style="width:60px;text-align:center;border-top:0px;">PFOA<br/>全氟辛烷磺酸盐</td>
				<td style="width:100px;text-align:center;border-top:0px;">Analyze report number<br/>测试报告编号</td>
				<td style="width:100px;text-align:center;border-top:0px;">Analyze Report Issue Date<br/>测试报告日期</td>
				<td style="width:100px;text-align:center;border-top:0px;">Analyze Report finish Date<br/>测试报告到期</td>
				<td style="width:100px;text-align:center;border-top:0px;">Lab<br/>测试机构</td>
				<td style="width:100px;text-align:center;border-top:0px;">Attach analyze report ducument<br/>测试报告档案</td>
				<td style="width:100px;text-align:center;border-top:0px;">Attach MSDS<br/>MSDS文件</td>
				<td style="width:100px;text-align:center;border-top:0px;">Exemption<br/>豁免</td>
				<td style="width:100px;text-align:center;border-top:0px;">Remark<br/>备注/退件记录</td>
			</tr>
			<s:iterator value="_gpMaterialSubs" id="item" var="item">
			<tr class="gpMaterialSubs" zbtr2=true>
				<td style="text-align: center;"><a class="small-button-bg"
					addBtn="true" onclick="addRowHtml(this)" href="#" title="添加"> <span
						class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> <a
					class="small-button-bg" delBtn="true" onclick="removeRowHtml(this)"
					href="#" title="删除"> <span class="ui-icon ui-icon-minus"
						style='cursor: pointer;'></span></a></td>
				<td style="text-align: center;"><input style="width: 85%;" stage="three" name="partName" onclick="selectProject(this);" value="${partName}"/>
					<a class="small-button-bg" onclick="selectProject(this);"  style="float:right;"><span class="ui-icon ui-icon-search"></span></a>
				</td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="averageMaterialName" value="${averageMaterialName}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="averageMaterialModel" value="${averageMaterialModel}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="averageMaterialAttribute" value="${averageMaterialAttribute}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="two" name="averageMaterialWeight" onchange="setweigh(this)" value="${averageMaterialWeight}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="unit" value="${unit}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="manufacturer" value="${manufacturer}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text1" value="${text1}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text2" value="${text2}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text3" value="${text3}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text4" value="${text4}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text5" value="${text5}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text6" value="${text6}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text7" value="${text7}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text8" value="${text8}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text9" value="${text9}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text10" value="${text10}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text11" value="${text11}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text12" value="${text12}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text13" value="${text13}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text14" value="${text14}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text15" value="${text15}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text16" value="${text16}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="text17" value="${text17}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="testReportNo" value="${testReportNo}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="testReportDate" isDate="true" value="<s:date name='testReportDate' format="yyyy-MM-dd"/>"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="two" name="testReportExpire" readonly="ture"  value="<s:date name='testReportExpire' format="yyyy-MM-dd"/>"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="testReportDepart" value="${testReportDepart}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="three" type="hidden" isFile="true" name="testReportFile" value="${testReportFile}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="three" type="hidden" isFile="true" name="msdsFile" value="${msdsFile}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="exemption" value="${exemption}"/></td>
				<td style="text-align: center;"><input style="width: 95%;" stage="one" name="remark" value="${remark}"/>
				<input type="hidden" style="width: 95%;" stage="one" name="averageId" value="${averageId}"/>
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>