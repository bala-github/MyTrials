 <!DOCTYPE html>
 <#-- @ftlvariable name="" type="org.bala.MyNotes.views.LogoutView" -->
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>

<body style="overflow-y: scroll;">

<!-- http://www.w3schools.com/bootstrap/bootstrap_case_navigation.asp -->
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">My Notes</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
     </div>
  </div>
</nav>

<!-- Bootstrap requires a containing element to wrap elements http://www.w3schools.com/bootstrap/bootstrap_get_started.asp -->
 
<div class="container-fluid">

<!--A jumbotron indicates a big box for calling extra attention to some special content or information. http://www.w3schools.com/bootstrap/bootstrap_jumbotron_header.asp-->
<div class="jumbotron">
  
  <p>Thank You <strong>${user.name?html} :)</strong> You have been logged out sucessfully.</p>
  </br>
  <p>Want to sign in again? <a class="btn btn-primary" href="/">Click Here</a></p>
</div>

</body>
</html>
