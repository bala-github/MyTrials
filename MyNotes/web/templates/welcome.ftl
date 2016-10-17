 <!DOCTYPE html>
 <#-- @ftlvariable name="" type="org.bala.MyNotes.views.UserView" -->
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script>
 
 	function send_ajax_get(url, settings, successHandler, errorHandler) {
 	
 		if(typeof settings == 'undefined') {
 			settings = {};
 		} 
 		
 		if(typeof errorHandler == 'undefined') {
 			errorHandler = function (xhr,errorStatus,err) {};
 		} 
 		
 		$.ajax({
 			type : 'GET',
 			url : url,
 			settings : settings,
 			success : successHandler,
 			error : errorHandler
 		});
 	}
 	
 	function send_ajax_post(url, contentType, data, settings,  successHandler, errorHandler) {
 	
 		if(typeof settings == 'undefined') {
 			settings = {};
 		} 
 		
 		if(typeof errorHandler == 'undefined') {
 			errorHandler = function (xhr,errorStatus,err) {};
 		} 
 		
 		$.ajax({
 			type : 'POST',
 			url : url,
 			contentType : contentType,
 			data : data,
 			settings : settings,
 			success : successHandler,
 			error : errorHandler
 		});
 	}
 	 	
    function handle_get_details_success(data, status, xhr) {
    	console.log(data);
    	console.log(this.settings.index );
        var content = JSON.parse(data);
        console.log('url'+ content['title']);
        $("textarea#" + this.settings.index  + "_description_text").val(content['description']);
		$("#" + this.settings.index +"_loading").toggle();
		$("#" + this.settings.index +"_description").show();
		$("#" + this.settings.index  + "_action").show();
        $("#" + this.settings.index  + "_action").focus();
        $("#" + this.settings.index ).toggleClass("glyphicon-triangle-top");
		$("#" + this.settings.index ).toggleClass("glyphicon-triangle-bottom");
	}
	
	function handle_post_get_notes_success(data, status, xhr) {
			 console.log("Modifed:" + this.settings.index);
			 var settings = {};
			 settings = {'index' : this.settings.index};
			 send_ajax_get(data['link'],settings, handle_get_details_success, 'undefined');
	
	}	
			    
   function handle_post_get_notes_error(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);     
              console.log('err:' + err);
              console.log(xhr.status); 
              $("#" + this.settings.index +"_error").text(xhr.responseText).show();
              $("#" + this.settings.index +"_loading").toggle();
              $("#" + this.settings.index).toggleClass("glyphicon-triangle-top");
              $("#" + this.settings.index).toggleClass("glyphicon-triangle-bottom");
	}
	
	function handle_get_list_notes_error(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);
              console.log('err:' + err);
              console.log(xhr.status);
              console.log(xhr.responseText);
              $("#view_notes_div").text(xhr.responseText);
              $("#loading_details").toggle(); 
     }
          
	function handle_get_list_notes_success(data, status, xhr) {
             
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
               <span class="glyphicon glyphicon-triangle-bottom notemarker" id="note`+ i +`"></span></td><td><label id="note`+ i + `_label" style="white-space:normal !important;">`+  this['name'].replace('.json','')  + `</label></td>
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
			
    $(document).on('click','.notemarker',function(){
 
 
 
		console.log('file name');
		console.log($("#" + this.id + "_label").text());
		
		if($("#" + this.id).hasClass("glyphicon-triangle-top")) {
			// user is hiding details.
			$("#" + this.id +"_description").hide();
        	$("#" + this.id + "_action").hide();
        	$("#" + this.id +"_error").hide();
            $("#" + this.id).toggleClass("glyphicon-triangle-top");
            $("#" + this.id).toggleClass("glyphicon-triangle-bottom");
            
        } else {
          //user is fetching details
          console.log('Fetching details');
          $("#" + this.id +"_loading").toggle();
          $("#" + this.id +"_error").hide();
           
		  var payload = { };
          payload = {
              title : $("#" + this.id + "_label").text(), 
            };
          
          var settings = {};
          
          settings = {'index' : this.id};
          
          send_ajax_post('/details', 'application/json',  JSON.stringify(payload), settings, handle_post_get_notes_success, handle_post_get_notes_error);
          
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
    	send_ajax_get('/list', 'undefined', handle_get_list_notes_success, handle_get_list_notes_error);
    });   
    
    $(document).on('click', '#add_note_button', function() {
    	console.log('Add note button clicked');
    	console.log('Title:' + $('#add_note_title').val());
    	console.log('Description:' + $('textarea#add_note_descripition').val());
    	console.log('isChecked:' + $('#add_note_isurl').prop('checked'));
    	$("#add_note_status").text('Uploading Note...'); 
    	$("#add_note_status").show();
    
        var payload = { };

        payload = {
              title : $('#add_note_title').val(), 
              description : $('textarea#add_note_descripition').val(),
              isurl : $('#add_note_isurl').prop('checked')
            };

		send_ajax_post('/add', 'application/json', JSON.stringify(payload), 'undefined', handle_add_note_success, handle_add_note_error);
    });
     
    function handle_add_note_success(data, status, xhr) {
			$("#add_note_status").text('Added Note...');
			 console.log('Status:' + status);
	}
	
	function handle_add_note_error(data, status, xhr) {
			$("#add_note_status").text('Added Note...');
			 console.log('Status:' + status);
	}
	
   /*  
    $(document).ready(function () {
        $(".navbar-nav li a").click(function(event) {
        $(".navbar-collapse").collapse('hide');
      });
    });
     */
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

		<div id="add_note_status" class="row text-center" style="display:none;">
        	
        </div>
        
        <div class="row" >
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
                <textarea id="add_note_descripition" class="form-control" rows="5"></textarea>
            </div>
        </div>

        <div class="row">
             
            <div class="checkbox col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" ><label><input id="add_note_isurl" type="checkbox">URL</label> </div>

        </div>

        <div class="row">
            <div class="col-sm-1 col-xs-2" >
                 <button id="add_note_button" class="btn btn-primary" type="button">Save</button>
            </div>
            <div class="col-sm-1 col-sm-offset-0  col-xs-offset-1 col-xs-2" >
             <button id="remove_note_button" class="btn btn-primary" type="button">Cancel</button>
            </div>
        </div> 

</form>

</body>
</html>
