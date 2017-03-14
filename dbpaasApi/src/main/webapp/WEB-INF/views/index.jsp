<%-- 
    Document   : aaa
    Created on : 2016-8-2, 10:26:35
    Author     : tangchao
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@page import="com.ant.dbpaas.test.model.TestData" %>
<%@page import="com.ant.dbpaas.test.model.Page" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        	<script type="text/javascript">

	    function deleteData(id){
                window.location.href="test/delete/"+id;
	    }
            
	    function addData(){
                window.location.href="test/addPage";
	    }
            
	    function alterData(id){
                window.location.href="test/alter/"+id;
	    }
            
            function metaCheckCluster(){
                window.location.href="api/meta/checkCluster";
	    }
            
            function metaCheckNode(){
                window.location.href="api/meta/checkNode";
	    }
            
	    function selectTestData(){
                var env = document.getElementById("enviroment").value;
	        document.getElementById("pageForm").action="test/start/" + env;
	        document.getElementById("pageForm").submit();
	    }
            
	    function turnPage(action){
	       var total =  document.getElementById("totalPage").value;
	       var currentPage = document.getElementById("currentPage").value;
	       currentPage = parseInt(currentPage);
	       page = parseInt(currentPage);
	       switch(action) {
                        case 'first' :
                            page = 1;
                            break;
                        case 'prior' :
                            page--;
                            break;
                        case 'next' :
                            page++;
                            break;
                        case 'last' :
                            page = total;
                            break;
                        case 'jump' :
                            page = currentPage;
                            break;
                        case 'rows' :
                            page = 1;
                            break;
                    }
                    if (page < 1) {
                        page = 1;
                        alert("first page");
                        return;
                    }
                    if (page > total) {
                        page = total;
                        alert("the end");
                        return;
                    }
                    document.getElementById("currentPage").value = page;
                    document.getElementById("pageForm").action = "turnPage.do";
                    document.getElementById("pageForm").submit();
                }
	</script>
        <title>CC(check change) System</title>
    </head>

    <body>
        <h1></h1>

        <form id="pageForm" method="post">
            CC ${enviroment} System 
            <select id="enviroment" >
                <c:forEach var="env" items="${envList}">
                    <option >${env}</option>
                </c:forEach>
            </select>
            <input type="button" value="start" onclick="selectTestData()" >
            <input type="button" value="ClusterCheck" onclick="metaCheckCluster()" >
            <input type="button" value="NodeCheck" onclick="metaCheckNode()" >
            
            
            <table width="1200" border="0" cellpadding="2" cellspacing="1" style="word-break:break-all;text-align:left">
                <tr><th width="100">apiName</th>
                    <th width="200">api</th>
                    <th width="400">result</th>
                    <th width="400">realResult</th>
                    <th width="100">op</th>
                </tr>
                
                <c:forEach var="api" items="${list}">
                    <tr>
                        <td>${api.apiName }</td>
                        <td>${api.api }</td>
                        <td>${api.result }</td>
                        <td>${api.realResult }</td>
                        <td>
                            <input type="button" value="calibrate" onclick="alterData(${api.id})" >
                            <input type="button" value="delete" onclick="deleteData(${api.id})" >
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <br>
            
            <table width="95%">
                <tr>
                    <td align="left">
                        <input type="button" value="First" onclick="turnPage('first')"/>
                        <input type="button" value="Prev" onclick="turnPage('prior')"/>
                        <input type="button" value="Next" onclick="turnPage('next')"/>
                        <input type="button" value="End" onclick="turnPage('last')"/>
                       
                        <input type="hidden" name="totalPage" id="totalPage" value="${page.totalPage }"/>
                        <input type="text" size="3" maxlength="5"  id="currentPage" name="currentPage" value="${page.currentPage}"
                               onkeydown="if (window.event.keyCode===13) {turnPage('jump');}"/>
                        /${page.totalPage} Pages     ${page.rowsCount} Rows
                        
                        Page
                        <input type="text" size="3" maxlength="5"  id="rowsPerPage" name="rowsPerPage" value="${page.rowsPerPage}"
                               onkeydown="if (window.event.keyCode===13) {turnPage('rows');}"/>Row
                    </td>
                    <td align="right">
                        <input type="button" value="Add" onclick="addData()">
                    </td>
                </tr>
            </table>
                               
        </form>
                               
    </body>

</html>
