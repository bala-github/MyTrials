 <!DOCTYPE html>
 <#-- @ftlvariable name="" type="org.bala.MyNotes.views.UserView" -->
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script src="scripts/base64.js"></script>
  <script src="scripts/MyNotes.js"></script>
  <script>
 
  
 	var myNotes = myNotes || new MyNotesFunc();
 		 	
    $(document).ready(function() {
    	console.log("Going to fetch list of notes");
    	$("#loading_details").show();
    	var settings = {};
    	settings = {'dir' : '/'};
    	myNotes.getNotes('', settings, handle_list_notes_success, handle_list_notes_error);
    });   

	function handle_list_notes_success(data, status, xhr) {
             
              console.log("Data:" + data); 
              
              var htmlContent = '';
              var i=1; 
              var file='glyphicon glyphicon-file';
              var folder='glyphicon glyphicon-folder-close';
              var dir=this.settings.dir;
              
              console.log('Current dir[' + dir + ']');
              
              
              $("#loading_details").hide();
               
              var link = '<a href="#" class="dirlink" id="/">/</a>';
              var currentdir='/';
              
              if(dir != '/') {
              
              	  var dirs = dir.split("/");
              	  
                  console.log('Length:' + dirs.length);
                  
	              for(i = 0; i < dirs.length; i++) {
	              
	                 if(dirs[i] == '')
	                   continue;
	                   
	                 currentdir = currentdir + dirs[i] + '/';
	                 
	              	 link = link + `&nbsp;<a href="#" class="dirlink" id="` + currentdir + `">`+ dirs[i] +`/</a>`;
	              }
              }
              
              htmlContent = htmlContent + `
              <div id="note_directory" style="display:none;">`+ dir + `</div>
              <div id="note_directory_text" style="text-align:center;">
    			<p>You are in `+ link +` </p>     	
       		 </div>    
        `
              jQuery.each(data['entries'], function() {
              	console.log('Before decoding:' + this['name']);
              	console.log('After decoding:' + this['name'].replace('.json', ''));
              	 htmlContent= htmlContent + `
     	<div id="note`+ i + `_section"> 
        <div id="note`+ i +`_row" class="row note_row">
            <div class="col-md-10 col-xs-12">
            <table>
               <tr> 
               <td style="vertical-align: top;"> <span class="glyphicon glyphicon-triangle-right notemarker" id="note`+ i +`"></span></td>
               <td style="vertical-align: top;"><span class="` + (this['name'].includes('.json') ? file : folder ) + `" notetype id="note` + i + `_type"></span></td>
               <td><label id="note`+ i + `_label" style="white-space:normal !important;">&nbsp;`+ this['name'].replace('.json', '')   + `</label></td>
               </tr>
             </table>  
            </div>
        </div>
		
		<div class="row" id="note`+ i + `_loading" style ="display:none;">
			Loading...
		</div>
		
		<div class="row" id="note`+ i + `_updating" style = "display:none;">
			Updating...
		</div>
		
		<div class="row" id="note`+ i + `_deleting" style ="display:none;">
			Deleting...
		</div>
		
		 <div class="row" id="note`+ i + `_error" style ="display:none;">
			
		</div>
        <div class="row" id="note`+ i + `_description" style="display:none;">
            <div class="col-md-6 col-xs-12">
                <textarea id="note` + i + `_description_text" class="form-control" rows="5"> www.config.tomcat.com </textarea>
            </div>
        </div>

        <div class="row" id="note`+ i + `_isurl_row" style="display:none;">
             
            <div class="checkbox col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" ><label><input id="note`+ i + `_isurl" type="checkbox">URL</label> </div>

        </div>

        <div class="row" id="note`+ i +`_action" style="display:none;">
             </br>
            <div class="col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" >
             <button class="btn btn-primary update_button" type="button" id="note` + i + `_update">Update</button>
            </div>

            <div class="col-sm-offset-0 col-sm-2 col-xs-offset-0 col-xs-4" >
             <button class="btn btn-primary remove_button" type="button" id="note` + i + `_remove">Remove</button>
            </div>
        </div>
        </div>
              	`;
              	i=i+1;
              });  
              $("#loading_details").hide();
              $("#view_notes_div").html(htmlContent);
              
     }
     
      
	function handle_list_notes_error(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);
              console.log('err:' + err);
              console.log(xhr.status);
              console.log(xhr.responseText);
              $("#view_notes_div").text(xhr.responseText);
              $("#loading_details").hide(); 
     }
                    	 	
 	$(document).on('click','.dirlink',function(){
 		var settings = {};
    	settings = {'dir' : this.id};
    	$("#loading_details").show();
    	myNotes.getNotes(this.id, settings, handle_list_notes_success, handle_list_notes_error);  			
	 });


	 $(document).on('click', '.update_button', function() {
   		var note_id= this.id.replace('_update','');
   		
   		console.log('Update note' + $('#note_directory').text() + '- ' + $("#"+note_id+"_label").text().trim());
   		$("#" + note_id +"_updating").show();
   		$("#" + note_id +"_error").hide();
   		
   		var note = new myNotes.Note($('#note_directory').text(), $("#"+note_id+"_label").text().trim(), 
   		$("textarea#"+ note_id +"_description_text").val(), $("#"+ note_id +"_isurl").prop('checked'));

   		var settings = {};
	          
	    settings = {'index' : note_id};
	    
   		note.add(settings, handle_update_notes_success, handle_update_notes_error);
   		
   	}); 

    function handle_update_notes_success(data, status, xhr) {
		console.log('note id' + this.settings.index);	 
		$("#" + this.settings.index + "_updating").hide();
		
		
	}	
			    
   function handle_update_notes_error(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);     
              console.log('err:' + err);
              console.log(xhr.status); 
              $("#" + this.settings.index +"_error").text(xhr.responseText).show();
              $("#" + this.settings.index +"_updating").hide();
	}	
	 
	 
   $(document).on('click', '.remove_button', function() {
   		var note_id= this.id.replace('_remove','');
   		
   		console.log('Deleting note' + $('#note_directory').text() + '- ' + $("#"+note_id+"_label").text().trim());
   		$("#" + note_id +"_deleting").show();
   		$("#" + note_id +"_error").hide();
   		
   		var note = new myNotes.Note($('#note_directory').text(), $("#"+note_id+"_label").text().trim());

   		var settings = {};
	          
	    settings = {'index' : note_id};
	     		
   		note.remove(settings, handle_remove_notes_success, handle_remove_notes_error);
   		
   }); 
	
    function handle_remove_notes_success(data, status, xhr) {
		console.log('note id' + this.settings.index);	 
		$("#" + this.settings.index + "_deleting").hide();
		$("#" + this.settings.index + "_section").hide();
		
	}	
			    
   function handle_remove_notes_error(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);     
              console.log('err:' + err);
              console.log(xhr.status); 
              $("#" + this.settings.index +"_error").text(xhr.responseText).show();
              $("#" + this.settings.index +"_deleting").hide();
	}	
	
   $(document).on('click','.notemarker',function(){
 		console.log('file name');
		console.log($("#" + this.id + "_label").text());
		
		if($("#" + this.id).hasClass("glyphicon-triangle-bottom")) {
			// user is hiding details.
			$("#" + this.id +"_description").hide();
			$("#" + this.id +"_isurl_row").hide();
        	$("#" + this.id + "_action").hide();
        	$("#" + this.id +"_error").hide();
            $("#" + this.id).toggleClass("glyphicon-triangle-bottom");
            $("#" + this.id).toggleClass("glyphicon-triangle-right");
            
        } else {
          //user is fetching details
          console.log('Fetching details[' + $("#" + this.id + "_label").text() + ']-' + $("#" + this.id + "_label").text().trim());
          $("#" + this.id +"_loading").toggle();
          $("#" + this.id +"_error").hide();
          
          if($("#"+this.id+"_type").hasClass("glyphicon glyphicon-file")) {
          
          	  var note = new myNotes.Note($('#note_directory').text(), $("#" + this.id + "_label").text().trim());
          	 
	          var settings = {};
	          
	          settings = {'index' : this.id};
	          

	          note.view(settings, handle_get_details_success, handle_get_details_error);
          } else {
          
    		  var settings = {};
    		  settings = {'dir' : $('#note_directory').text()  + $("#" + this.id + "_label").text().trim() + '/'};
    		  myNotes.getNotes($('#note_directory').text()  + $("#" + this.id + "_label").text().trim() + '/', settings, handle_list_notes_success, handle_list_notes_error);  			
          }
      }  
        
    });    
    
    	
		    
   function handle_get_details_error(xhr,errorStatus,err) {
              console.log('errorStatus:' + errorStatus);     
              console.log('err:' + err);
              console.log(xhr.status); 
              $("#" + this.settings.index +"_error").text(xhr.responseText).show();
              $("#" + this.settings.index +"_loading").toggle();
              $("#" + this.settings.index).toggleClass("glyphicon-triangle-bottom");
              $("#" + this.settings.index).toggleClass("glyphicon-triangle-right");
	}
	

   function handle_get_details_success(data, status, xhr) {
    	console.log(data);
    	console.log(this.settings.index );
        var content = JSON.parse(data);
        console.log('url'+ content['title']);
        console.log('isURL' + content['url']);
        $("textarea#" + this.settings.index  + "_description_text").val(content['description']);
        $("#" + this.settings.index +"_isurl_row").show();
        $("#" + this.settings.index +"_isurl").prop('checked', content['url']);
		$("#" + this.settings.index +"_loading").toggle();
		$("#" + this.settings.index +"_description").show();
		$("#" + this.settings.index  + "_action").show();
        $("#" + this.settings.index  + "_action").focus();
        $("#" + this.settings.index ).toggleClass("glyphicon-triangle-bottom");
		$("#" + this.settings.index ).toggleClass("glyphicon-triangle-right");
	}
			
 

    $(document).on('click', '#welcome_link', function() {
    	$(".jumbotron").show();
    	$("#view_notes_form").show();
    	$("#add_note_form").hide(); 
    	$("#welcome").addClass('active');
    	$("#add_notes").removeClass('active');
    	$("#view_notes").removeClass('active');  
    	
    	$(".navbar-collapse").collapse('hide'); 	
    	
    	$("#loading_details").show();
    	var settings = {};
    	settings = {'dir' : '/'};
    	myNotes.getNotes('', settings, handle_list_notes_success, handle_list_notes_error);
    });

    
    $(document).on('click', '#view_notes_link', function() {
    	$(".jumbotron").hide();
    	$("#view_notes_form").show();
    	$("#add_note_form").hide();
    	$("#welcome").removeClass('active');
    	$("#add_notes").removeClass('active');
    	$("#view_notes").addClass('active'); 
    	
    	$(".navbar-collapse").collapse('hide'); 
    	
    	$("#loading_details").show();
   		
   		var settings = {};
    	settings = {'dir' : $('#note_directory').text()};
    	console.log('Dir[' + $('#note_directory').text() + ']');    	
    	myNotes.getNotes($('#note_directory').text(), settings, handle_list_notes_success, handle_list_notes_error);   	
    	
    });
        
     $(document).on('click', '#add_note_link', function() {
    	$(".jumbotron").hide();
    	$("#view_notes_form").hide();
    	$("#add_note_form").show();   
    	
    	clear_add_notes_screen();
    	$('#add_note_folder').val($('#note_directory').text()); 	
    	$("#welcome").removeClass('active');
    	$("#add_notes").addClass('active');
    	$("#view_notes").removeClass('active'); 
    	
    	$(".navbar-collapse").collapse('hide');    	
    });
    

    function clear_add_notes_screen() {
    	$('textarea#add_note_descripition').val('');
    	$('#add_note_title').val('');
    	$('#add_note_isurl').prop('checked', false);
    }
    $(document).on('click', '#cancel_note_button', clear_add_notes_screen());
    
    $(document).on('click', '#add_note_button', function() {
    	console.log('Add note button clicked');
    	console.log('Title:' + $('#add_note_title').val());
    	console.log('Description:' + $('textarea#add_note_descripition').val());
    	console.log('isChecked:' + $('#add_note_isurl').prop('checked'));
    	console.log('Title After Encoding:' + $('#add_note_title').val());
    	$("#add_note_status").text('Uploading Note...'); 
    	$("#add_note_status").show();
        console.log('isurl' +  $('#add_note_isurl').prop('checked'));
    	var note = new myNotes.Note($('#add_note_folder').val(), $('#add_note_title').val(), $('textarea#add_note_descripition').val(), $('#add_note_isurl').prop('checked'));
    	
    	note.add('undefined', handle_add_note_success, handle_add_note_error);
    			
    });
     
    function handle_add_note_success(data, status, xhr) {
			$("#add_note_status").text('Added Note...');
			console.log('Status:' + status);
			clear_add_notes_screen();
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
        <li id="welcome" class="active"><a id="welcome_link" href="#">About</a></li>
      
            <li id="view_notes"> <a id="view_notes_link" href="#">View Notes</a></li>
            <li id="add_notes"> <a id="add_note_link" href="#">Add a Note</a></li>
        
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
               <label style="white-space:normal !important;">Folder</label></td>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-sm-12">
                <input id="add_note_folder" type="text" class="form-control">
            </div>
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
             <button id="cancel_note_button" class="btn btn-primary" type="button">Cancel</button>
            </div>
        </div> 

</form>

</body>
</html>
