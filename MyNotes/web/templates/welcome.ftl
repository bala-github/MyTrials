 <!DOCTYPE html>
 <#-- @ftlvariable name="" type="org.bala.MyNotes.views.UserView" -->
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script>
    $(document).on('click','.notemarker',function(){
 
 
		console.log('file name');
		console.log($("#" + this.id + "_label").text());
		
		if($("#" + this.id).hasClass("glyphicon-triangle-top")) {
			$("#" + this.id +"_description").toggle();
        	$("#" + this.id + "_action").toggle();
            $("#" + this.id).toggleClass("glyphicon-triangle-top");
            $("#" + this.id).toggleClass("glyphicon-triangle-bottom");
            
        } else {
          console.log('Fetching details');
          $("#" + this.id +"_loading").toggle();
          $("#" + this.id +"_error").hide();
           
		  var payload = { };
          payload = {
              title : $("#" + this.id + "_label").text(), 
            };
        
          $.ajax({
           type : 'POST',
           url  : '/details',
           contentType : 'application/json',
           index : this.id,
           data : JSON.stringify(payload),
           error : function(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);     
              console.log('err:' + err);
              console.log(xhr.status); 
              $("#" + this.index +"_error").text(xhr.responseText).show();
              $("#" + this.index +"_loading").toggle();
			}, 
			success : function(data, status, xhr) {
			 console.log(this.index);
			 
			  $.ajax({
			    type : 'GET',
			    url : data['link'],
			    index : this.index,
			    contentType: 'application/json',
			    success : function(data, status, xhr) {
			    	console.log(data);
			    	console.log(this.index);
			        var content = JSON.parse(data);
			        console.log('url'+ content['title']);
			        $("textarea#" + this.index + "_description_text").val(content['title']);
        			$("#" + this.index +"_loading").toggle();
        			$("#" + this.index +"_description").toggle();
        			$("#" + this.index + "_action").toggle();
			        $("#" + this.index + "_action").focus();
			        $("#" + this.index).toggleClass("glyphicon-triangle-top");
        			$("#" + this.index).toggleClass("glyphicon-triangle-bottom");
			    }
			  });			
			}		
        });
      }  
        
    });    

    $(document).on('click', '#welcome_link', function() {
    	$(".jumbotron").show();
    	$("#view_notes_form").show();
    	$("#add_note_form").hide();    	
    });

    
    $(document).on('click', '#view_notes_link', function() {
    	$(".jumbotron").hide();
    	$("#view_notes_form").show();
    	$("#add_note_form").hide();
    	
    });
        
     $(document).on('click', '#add_note_link', function() {
    	$(".jumbotron").hide();
    	$("#view_notes_form").hide();
    	$("#add_note_form").show();    	
    });
    
    $(document).ready(function() {
    	console.log("Going to fetch list of notes");
    	$("#loading_details").toggle();
    	  $.ajax({
           type : 'GET',
           url  : '/list',
           contentType : 'application/json',
           error : function(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);
              console.log('err:' + err);
              console.log(xhr.status);
              console.log(xhr.responseText);
              $("#view_notes_div").text(xhr.responseText);
              $("#loading_details").toggle(); 
          }, 
          success : function(data, status, xhr) {
             
              console.log("Data:" + data); 
              
              var htmlContent = '';
              var i=1; 
              jQuery.each(data['entries'], function() {
              	console.log(this['name']);
              	 htmlContent= htmlContent + `<div id="note`+ i +`_row" class="row">
            <div class="col-md-10 col-xs-12">
            <table>
               <tr> 
               <td style="vertical-align: top;">
               <span class="glyphicon glyphicon-triangle-bottom notemarker" id="note`+ i +`"></span></td><td><label id="note`+ i + `_label" style="white-space:normal !important;">`+  this['name'] + `</label></td>
               </tr>
             </table>  
            </div>
        </div>
		<div class="row" id="note`+ i + `_loading" style ="display:none;">
			Loading...
		</div>
		 <div class="row" id="note`+ i + `_error" style ="display:none;">
			
		</div>
        <div class="row" id="note`+ i + `_description" style="display:none;">
            <div class="col-md-6 col-xs-12">
                <textarea id="note` + i + `_description_text" class="form-control" rows="5"> www.config.tomcat.com </textarea>
            </div>
        </div>

        <div class="row" id="note`+ i +`_action" style="display:none;">
             </br>
            <div class="col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" >
             <button class="btn btn-primary" type="button">Update</button>
            </div>

            <div class="col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" >
             <button class="btn btn-primary" type="button">Remove</button>
            </div>
        </div>
              	`;
              	i=i+1;
              });  
              $("#view_notes_div").html(htmlContent);
              $("#loading_details").toggle(); 
          }
          
        });
    });    
  </script>
</head>

<body style="overflow-y: scroll;">

<!-- http://www.w3schools.com/bootstrap/bootstrap_case_navigation.asp -->
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                       
      </button>
      <a class="navbar-brand" href="#">My Notes</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a id="welcome_link" href="#">About</a></li>
      
            <li><a id="view_notes_link" href="#">View Notes</a></li>
            <li><a id="add_note_link" href="#">Add a Note</a></li>
        
      </ul>
    
      <ul class="nav navbar-nav navbar-right">
        <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
            Welcome ${user.name?html} !<span class="caret"></span></a> 
            <ul class="dropdown-menu">
                <li><a href="signout">SignOut</a></li>
            </ul>
         </li>
      </ul>
    </div>
  </div>
</nav>

<!-- Bootstrap requires a containing element to wrap elements http://www.w3schools.com/bootstrap/bootstrap_get_started.asp -->
 
<div class="container-fluid">

<!--A jumbotron indicates a big box for calling extra attention to some special content or information. http://www.w3schools.com/bootstrap/bootstrap_jumbotron_header.asp-->
<div class="jumbotron">
  <h1>My Notes</h1>
  <p>Jot down all things important to you, in your Github repo, and access it from any browser or android phones. Use the browser plugins (Available for Chrome, FireFox) to bookmark urls and access it from anywhere.
  </p>
</div>

<div id="loading_details" style="display:none;">
  Loading...
</div>
<form id="view_notes_form">
  <div id="view_notes_div">
   
   </div> 
</form>

<form id="add_note_form" style="display:none;">

        <div class="row">
            <div class="col-xs-12">
               <label style="white-space:normal !important;">Title</label></td>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-sm-12">
                <input id="add_note_title" type="text" class="form-control">
            </div>
        </div>
     
        <div class="row">
            <div class="col-xs-12">
               <label style="white-space:normal !important;">Description</label></td>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-sm-12">
                <textarea id="add_note_description" class="form-control" rows="5"></textarea>
            </div>
        </div>

        <div class="row">
             
            <div class="checkbox col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" ><label><input id="add_note_isur" type="checkbox">URL</label> </div>

        </div>

        <div class="row">
            <div class="col-sm-1 col-xs-2" >
                 <button id="add_note" class="btn btn-primary" type="button">Save</button>
            </div>
            <div class="col-sm-1 col-sm-offset-0  col-xs-offset-1 col-xs-2" >
             <button id="remove_note" class="btn btn-primary" type="button">Cancel</button>
            </div>
        </div> 

</form>

</body>
</html>
