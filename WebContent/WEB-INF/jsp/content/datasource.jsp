<%@ page import="domainhealth.core.jmx.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.NUM_AVAILABLE%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.NUM_UNAVAILABLE%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>							
	</tr>							
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.ACTIVE_CONNECTONS_CURRENT_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.CONNECTION_DELAY_TIME%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>							
	</tr>							
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.FAILED_RESERIVE_REQUEST_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.FAILURES_TO_RECONNECT_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>							
	</tr>	
	<tr>
		<td class="datarow" class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.LEAKED_CONNECTION_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.WAITING_SECONDS_HIGH_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>							
	</tr>	
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.WAITING_FOR_CONNECTION_CURRENT_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.WAITING_FOR_CONNECTION_FAILURES_TOTAL%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>							
	</tr>											