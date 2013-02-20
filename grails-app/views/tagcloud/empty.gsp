<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>State Tag Cloud Initialization</title>
    <link href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700' rel='stylesheet' type='text/css'>
    <style type="text/css">
        body {font-family: 'Ubuntu', sans-serif; background-color: #073642; color: #839496;}
        div.cloud {width: 50%; margin-left: auto; margin-right: auto; margin-top: 50px; margin-bottom: 50px;}
        div.cloud ul {padding: 2px; line-height: 3em; text-align: center; margin: 0;}
        div.cloud li {display: inline; margin-left: 5px; margin-right: 5px;}
    </style>
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
