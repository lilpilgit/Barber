<%@page session="false"%>
<%String contextPath=request.getContextPath();%>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="0; url=<%=contextPath%>/app">
    <script type="text/javascript">
        function onLoadHandler() {
            window.location.href = "<%=contextPath%>/app";
        }
        window.addEventListener("load", onLoadHandler);
    </script>
    <title>Page Redirection</title>
</head>
<body>
If you are not redirected automatically, follow the <a href='<%=contextPath%>/app'>link</a>
</body>
</html>
