<%@ page import="domainhealth.core.jmx.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<tr> 
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.SESSIONS_CURRENT_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.SESSIONS_HIGH_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>							
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<c:out value="${resourcename}"/>/<%=WebLogicMBeanPropConstants.SESSIONS_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%">&nbsp;</td>
	</tr>											