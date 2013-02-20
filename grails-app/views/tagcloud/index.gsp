<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>State Tag Cloud Initialization</title>
    <link href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700' rel='stylesheet' type='text/css'>
    <r:require modules="bootstrap, cloud"/>
    <r:layoutResources/>
</head>
<body>
    <div class="cloud center">
        <button class="btn-large" onClick="window.location.href='<%=createLink(action: 'empty')%>'">Press me</button> 
        <span class="btn-cap center">Press the button to load Tag Cloud</span>
    </div>
</body>
</html>

