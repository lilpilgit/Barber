<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<html>
<head>
    <title>404 | Page Not Found</title>
</head>
<body>
<h1>An error occurred...</h1>
<p>
    There was probably a problem if you are here ... Try again later, we are already working on it ...<br><br>
    <em><%= exception%>
    </em>
</p>
<%-- Text in a pre element is displayed in a fixed-width font, and it preserves both spaces and line breaks --%>
<pre>
      <%exception.printStackTrace(new java.io.PrintWriter(out));%>
</pre>
</body>
</html>
