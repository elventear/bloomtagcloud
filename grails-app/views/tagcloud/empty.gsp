<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>State Tag Cloud Initialization</title>
    <link href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700' rel='stylesheet' type='text/css'>
    <r:require module="cloud"/>
    <r:layoutResources/>
</head>
<body>
    <div class="cloud">
        <ul>
        <% states.each{ state, count -> %>
            <% def (max_height, min_height, max_weight, min_weight) = [64, 16, 900, 100] %>
            <% def height=8*Math.round(((count-min)*(max_height-min_height)/(max-min)+min_height)/8)%>
            <% def weight=100*Math.round(((count-min)*(max_weight-min_weight)/(max-min)+min_weight)/100) %>
                <li style="font-size:<%=height%>pt; font-weight:<%=weight%>"><%=state%></li>
            <%}%>
        </ul>
    </div>
</body>
</html>
