<%--
  Created by IntelliJ IDEA.
  User: evgen
  Date: 30.12.2019
  Time: 19:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Секвенции</title>

    <style>
        <%@include file="/WEB-INF/css/style.css"%>
    </style>
    <script type="text/javascript">
        <%@include file="/WEB-INF/js/jquery-3.4.1.min.js"%>
    </script>
    <script type="text/javascript">
        <%@include file="/WEB-INF/js/script.js"%>
    </script>

</head>
<body>

<p>
    <%-- комментарий
<%-- @ page import="java.util.Date" %>
<%
    java.util.Date now = new java.util.Date();
    String someString = "Текущая дата : " + now;
%>
<%= someString %>

<%@ page import="logic.Expressions" %>
<%
    logic.BinaryTree expressions = new logic.Expressions();
%>
<%= someString %>
--%>
</p>

<button class="rulesBtn cantUseRule" onclick="useRule(0); return false;" disabled><img src="images/1.jpg" alt="1"  style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(1); return false;" disabled><img src="images/2.jpg" alt="2" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(2); return false;" disabled><img src="images/3.jpg" alt="3" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(3); return false;" disabled><img src="images/4.jpg" alt="4" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(4); return false;" disabled><img src="images/5.jpg" alt="5" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(5); return false;" disabled><img src="images/6.jpg" alt="6" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(6); return false;" disabled><img src="images/7.jpg" alt="7" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(7); return false;" disabled><img src="images/8.jpg" alt="8" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(8); return false;" disabled><img src="images/9.jpg" alt="9" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(9); return false;" disabled><img src="images/10.jpg" alt="10" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(10); return false;" disabled><img src="images/11.jpg" alt="11" style="vertical-align: middle"> </button>
<button class="rulesBtn cantUseRule" onclick="useRule(11); return false;" disabled><img src="images/12.jpg" alt="12" style="vertical-align: middle"> </button>

<!--input type="text" readonly> <input type="text" readonly> <input type="text" readonly-->
<form id="seqForm">

</form>

<form id="inputForm" metod="GET">
    <strong>Ответ сервлета </strong>:<span id="ajaxUserServletResponse"></span>
    <br>
    <label for="inputSeq">Введите секвенцию: </label>
    <input type="text" id="inputSeq" pattern="[+a-zA-Z=>&\-\(\)]+" required>
    <button type="submit">Окей</button>
</form>

<div class="b-popup" metod="GET" id="popup1">
    <form class="b-popup-content" >
        <p>Введите выражение</p>
        <!-- Продумать регулярное выражение для input-->
        <strong>Ответ сервлета </strong>:<span id="addExprResponse"></span>
        <input type="text" id="addExpression" placeholder="Введите выражение: " pattern="[+a-zA-Z=>&\-\(\)]+" required>
        <input type="reset" value="Закрыть" onclick="PopUpHide();">
        <input type="submit">
    </form>
</div>

</body>
</html>