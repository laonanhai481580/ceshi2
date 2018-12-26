<%@ page contentType="text/html;charset=UTF-8"%>
<div id="tabs-1" style="padding:0px;">
	<span id="info-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
   	<table style="width:100%;margin:0px;padding:0px;text-align: center;">
   		<input type="hidden" name="id" value="${qualityFeature.id }"/>
		<tr>
			<td colspan="2" valign="middle" style="width:100%;">
				<fieldSet> 
					<table>
						<tr>
							<td  >名称：</td>
							<td  colspan="5"><input name="name" style="width:98%" value="${qualityFeature.name }"/></td>
<!-- 							<td colspan="2">&nbsp;&nbsp;</td> -->
						</tr>
						<tr>
						   <td >简码：</td>
							<td colspan="2"><input name="code" value="${qualityFeature.code }"/></td>
							<td>CPK目标</td><td><input name="cpkGoal" value="${qualityFeature.cpkGoal }"/></td>
						</tr>
						<tr>
							<td colspan="4" align="left">
								<input type="radio" name="paramType" checked="checked" value="1"/>计量型&nbsp;&nbsp;
<!-- 								<input type="radio" name="paramType" value="2"/>计数型&nbsp;&nbsp; -->
<!-- 								<input type="radio" name="paramType" value="3"/>其他 -->
							</td>
							<td style="text-align: right;">排序：</td>
							<td style="text-align:left;"><input style="width:70px;" name="orderNum" value="${qualityFeature.orderNum}" class="{digits:true,min:0}"/></td>
						</tr>
					</table>
				</fieldSet>
			</td>
		</tr>
    	<tr>
    		<td style="width:50%;">
			    <fieldSet> 
			    	<legend>规格类型</legend>
			    	<table>
			    		<tr>
			    			<td style="text-align:left;"><input type="radio" name="specificationType" id="specificationType1" value="double" checked="checked"/><label for="specificationType1">双侧公差</label></td>
			    		</tr>
			    		<tr>
			    			<td style="text-align:left;"><input type="radio" name="specificationType" id="specificationType2" value="single-u" /><label for="specificationType2">单侧上公差</label></td>
			    		</tr>
			    		<tr>
			    			<td style="text-align:left;"><input type="radio" name="specificationType" id="specificationType3" value="single-l" /><label for="specificationType3">单侧下公差</label></td>
			    		</tr>
			    	</table>
			    </fieldSet>&nbsp;
			    <fieldSet> 
			    	<legend>规格限</legend>
			    	<table>
			    		<tr>
			    			<td colspan="2">&nbsp;</td>
			    			<td>
			    				<span style="font-size: 20px;">+</span>
			    			</td>
			    			<td><input name="add" id="add" style="width:60px;" value="0"/></td>
			    		</tr>
			    		<tr>
			    			<td><span style="color:red;">*</span>目标值：</td>
			    			<td><input name="targeValue" id="targeValue" style="width:100px;" value="${qualityFeature.targeValue }" class="{number:true}"/></td>
			    			<td colspan="2">&nbsp;</td>
			    		</tr>
			    		<tr>
			    			<td colspan="2">&nbsp;</td>
			    			<td>
			    				<span style="font-size: 20px;">-</span>
			    			</td>
			    			<td><input name="del" id="del" style="width:60px;" value="0"/></td>
			    		</tr>
			    	</table>
			    </fieldSet>
		    </td>
		    <td style="width:50%;">
			    <fieldSet> 
			    	<table>
			    		<tr>
			    			<td align="right"><span style="color:red;">*</span>样本容量：</td>
			    			<td align="left"><input style="width: 160px;" name="sampleCapacity" id="sampleCapacity" value="${qualityFeature.sampleCapacity}" class="{digits:true,min:0}"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right"><span style="color:red;">*</span>有效容量：</td>
			    			<td align="left"><input style="width: 160px;" name="effectiveCapacity"  id="effectiveCapacity" value="${qualityFeature.effectiveCapacity}" class="{digits:true,min:0}"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right">默认控制图：</td>
			    			<td align="left">
			    				<select name="controlChart" style="width:163px;" onchange="onchangeChart();" id="controlChart">
			    					<option value="1">1-均值-极差控制图</option>
			    					<option value="2">2-均值-标准差控制图</option>
			    					<option value="4">4-单值-移动极差控制图</option>
			    				</select>
			    			</td>
			    		</tr>
			    		<tr>
			    			<td align="right">移动极差间距：</td>
			    			<td align="left"><input name="rangeInterval" id="rangeInterval" style="width:144px;" value="${qualityFeature.rangeInterval }"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right"><span style="color:red;">*</span>显示精度：</td>
			    			<td align="left"><input name="precs" id="precs" style="width:144px;" value="2"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right">计量单位：</td>
			    			<td align="left">
			    				<s:select list="units"
										  listKey="value" 
										  listValue="value" 
							              labelSeparator=""
							  			  emptyOption="true"
										  theme="simple"
										  cssStyle="width:163px;"
										  name="unit"
										  value="unit">
								</s:select>
			    			</td>
			    		</tr>
			    		<tr>
			    			<td align="right"><span style="color:red;">*</span>合理上限：</td>
			    			<td align="left"><input style="width: 160px;" name="upperLimit" id="upperLimit" value="${qualityFeature.upperLimit}" class="{number:true}"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right"><span style="color:red;">*</span>合理下限：</td>
			    			<td align="left"><input style="width: 160px;" name="lowerLimit" id="lowerLimit" value="${qualityFeature.lowerLimit}" class="{number:true}"/></td>
			    		</tr>
			    		<tr>
			    			<td></td>
			    			<td style="text-align: left;">
				    			<input type="checkbox" name="isNoAccept" id="isNoAccept-1" value="true"/>
				    			<label class="checkboxLabel" for="isNoAccept-1">超出合理限不接受</label>
			    			</td>
			    		</tr>
			    	</table>
			    </fieldSet>
		    <td>
	    </tr>
    </table>
</div>
<div id="tabs-2" style="padding:0px;">
	<span id="limit-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
	<table id="limit_table" style="width:100%;margin:0px;padding:4px;text-align: center;">
<!-- 		<tr style="display: none;"> -->
<!-- 			<td colspan="2" valign="middle" style="width:100%;"> -->
<!-- 				<fieldSet>  -->
<!-- 					<table> -->
<!-- 						<tr> -->
<!-- 							<td>控制标准差倍数：</td> -->
<!-- 							<td><input name="multiple" value="3"/></td> -->
<!-- 						</tr> -->
<!-- 					</table> -->
<!-- 				</fieldSet>			 -->
<!-- 			</td> -->
<!-- 		</tr> -->
		<tr>
			<td>
				<fieldSet>
					<legend>质量特性状态</legend>
					<table>
			    		<tr>
			    			<td><input type="radio" name="isAutoCl" value="Y" checked="checked" />分析阶段</td>
			    		</tr>
			    		<tr>
			    			<td><input type="radio" name="isAutoCl" value="N"/>控制阶段</td>
			    		</tr>
			    	</table>
				</fieldSet>
			</td>
		<tr>
			<td>
				<fieldSet>
					<legend>控制限确定方法</legend>
					<table>
			    		<tr style="display: none;">
			    			<td colspan="3" align="left"><input type="radio" name="method" value="variable" />制定μ和Cpk值</td>
			    		</tr>
			    		<tr style="display: none;">
			    			<td align="right">μ=</td>
			    			<td colspan="2" align="left"><input name="u" value="${qualityFeature.u }"/></td>
			    		</tr>
			    		<tr style="display: none;">
			    			<td align="right">Cpk=</td>
			    			<td colspan="2" align="left"><input name="cpk" value="${qualityFeature.cpk }"/></td>
			    		</tr>
			    		<tr style="display: none;">
			    			<td colspan="3" align="left"><input type="radio" name="method" value="constant" checked="checked"/>常数</td>
			    		</tr>
			    		<tr>
			    			<td>&nbsp;</td>
			    			<td>X/XBar</td>
			    			<td>R/S/MR</td>
			    		</tr>
			    		<tr>
			    			<td align="right">UCL：</td>
			    			<td><input name="ucl1" value="${qualityFeature.ucl1 }" class="{number:true}"/></td>
			    			<td><input name="ucl2" value="${qualityFeature.ucl2 }" class="{number:true}"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right">CL：</td>
			    			<td><input name="cl1" value="${qualityFeature.cl1 }" class="{number:true}"/></td>
			    			<td><input name="cl2" value="${qualityFeature.cl2 }" class="{number:true}"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right">LCL：</td>
			    			<td><input name="lcl1" value="${qualityFeature.lcl1 }" class="{number:true}"/></td>
			    			<td><input name="lcl2" value="${qualityFeature.lcl2 }" class="{number:true}"/></td>
			    		</tr>
			    	</table>
				</fieldSet>
			</td>
		</tr>
		<tr>
			<td>
				<fieldSet>
					<legend>控制限限制</legend>
					<table>
						<tr>
			    			<td>&nbsp;</td>
			    			<td>最小值</td>
			    			<td>最大值</td>
			    		</tr>
			    		<tr>
			    			<td align="right">UCL：</td>
			    			<td><input name="uclMin" id="uclMin" value="${qualityFeature.uclMin }" class="{number:true}"/></td>
			    			<td><input name="uclMax" id="uclMax" value="${qualityFeature.uclMax }" class="{number:true}"/></td>
			    		</tr>
			    		<tr>
			    			<td align="right">LCL：</td>
			    			<td><input name="lclMin" id="lclMin" value="${qualityFeature.lclMin }" class="{number:true}"/></td>
			    			<td><input name="lclMax" id="lclMax" value="${qualityFeature.lclMax }" class="{number:true}"/></td>
			    		</tr>
					</table>
				</fieldSet>
<!-- 				<fieldSet> -->
<!-- 					<legend>当前控制限</legend> -->
<!-- 					<table> -->
<!-- 			    		<tr> -->
<!-- 			    			<td>&nbsp;</td> -->
<!-- 			    			<td>X/XBar</td> -->
<!-- 			    			<td>R/S/MR</td> -->
<!-- 			    		</tr> -->
<!-- 			    		<tr> -->
<!-- 			    			<td align="right">UCL：</td> -->
<%-- 			    			<td><input name="uclCurrent1" value="${qualityFeature.uclCurrent1 }"/></td> --%>
<%-- 			    			<td><input name="uclCurrent2" value="${qualityFeature.uclCurrent2 }"/></td> --%>
<!-- 			    		</tr> -->
<!-- 			    		<tr> -->
<!-- 			    			<td align="right">CL：</td> -->
<%-- 			    			<td><input name="clCurrent1" value="${qualityFeature.clCurrent1 }"/></td> --%>
<%-- 			    			<td><input name="clCurrent2" value="${qualityFeature.clCurrent2 }"/></td> --%>
<!-- 			    		</tr> -->
<!-- 			    		<tr> -->
<!-- 			    			<td align="right">LCL：</td> -->
<%-- 			    			<td><input name="lclCurrent1" value="${qualityFeature.lclCurrent1 }"/></td> --%>
<%-- 			    			<td><input name="lclCurrent2" value="${qualityFeature.lclCurrent2 }"/></td> --%>
<!-- 			    		</tr> -->
<!-- 			    	</table> -->
<!-- 				</fieldSet> -->
			</td>
		</tr>
	</table>
</div>
<div id="tabs-3" style="padding:0px;">
	<div style="line-height: 30px;">
		&nbsp;
		<a class="small-button-bg" style="vertical-align: middle;" href="javascript:selectBsRules();" title="添加判断准则"><span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span></a>
		<a class="small-button-bg" style="vertical-align: middle;" onclick="delBules();" href="#" title="删除判断准则"><span class="ui-icon ui-icon-closethick" style='cursor:pointer;'></span></a>
		<span id="judge-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
	</div>
	<table id="judge_table"></table>
</div>
<div id="tabs-4" style="padding:0px;">
	<div style="line-height: 30px;">
		&nbsp;
		<a class="small-button-bg" style="vertical-align: middle;" href="javascript:selectLayerType();" title="添加层别信息"><span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span></a>
		<a class="small-button-bg" style="vertical-align: middle;" onclick="delType();" href="#" title="删除层别信息"><span class="ui-icon ui-icon-closethick" style='cursor:pointer;'></span></a>
		<span id="level-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
	</div>
	<table id="level_table"></table>
</div>
<div id="tabs-5" style="padding:0px;">
	<div style="line-height: 30px;">
		&nbsp;
		<a class="small-button-bg" style="vertical-align: middle;" href="javascript:selectPerson();" title="添加通知人员"><span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span></a>
		<a class="small-button-bg" style="vertical-align: middle;" onclick="delPerson();" href="#" title="删除通知人员"><span class="ui-icon ui-icon-closethick" style='cursor:pointer;'></span></a>
		<span id="person-message" style="color: red; padding-left: 4px; font-weight: bold;"></span>
	</div>
	<input id="personId" name="personId" type="hidden"/>
	<input id="personName" name="personName" type="hidden"/>
	<table id="person_table"></table>
</div>