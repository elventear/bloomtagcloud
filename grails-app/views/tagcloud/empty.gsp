<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>State Tag Cloud Initialization</title>
</head>
<body>
    <% states.each{ state, count -> %>
    <% def height=(count-min)*(max_height-min_height)/(max-min)+min_height %>
        <span style="font-size:<%=height%>pt"><%=state%></span>
    <%}%>
    <h1><%=states%></h1> 
    <h2><%=max%></h2> 
    <h2><%=min%></h2> 
</body>
</html>
