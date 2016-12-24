var MyNotesFunc = (function () {
 
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
 	 	
 	function MyNotes() {
 		
 	}
 	
 	MyNotes.prototype.getNotes = function(path, settings,  successHandler, errorHandler) {
 		send_ajax_get('https://mynotes.io/list?path='+ path + '/', settings, successHandler, errorHandler);
 	};

	MyNotes.prototype.getUser = function(settings,  successHandler, errorHandler) {
		send_ajax_get('https://mynotes.io/user', settings, successHandler, errorHandler);
	};	

	MyNotes.prototype.registerUser = function(token, settings,  successHandler, errorHandler) {
		send_ajax_get('https://mynotes.io/access_token?code=' + token, settings, successHandler, errorHandler);
	};	
	
 	MyNotes.prototype.Note =  function(folder, title, description, isurl) {
 		
 		this.folder = folder || 'undefined';
 		this.title = title || 'undefined';
 		this.description = description || 'undefined';
 		this.isurl = isurl;
 		
 	};
 	
 	/*
 	//Getters and Setters. Should they be defined on MyNotes.prototype.Note.prototype ???
 	Object.defineProperties(MyNotes.prototype.Note, {
 		"getFolder" : { get : function() {return this.folder; }},
 		"setFolder" : {set : function(folder){this.folder = folder;}},
 		"getTitle" :  {get : function() {return this.title; }},
 		"setTitle" :  {set : function(title){this.title = title}},
 		"getDescription" : {get : function() {return this.description; }},
 		"setDescription" : {set : function(description) {this.description = description}},
 		"getIsUrl" : {get : function() {return this.isurl; }},
 		"setIsUrl" : {set : function(isurl) { this.isurl = isurl;}} 		
 	});
 	*/
 	
 	MyNotes.prototype.Note.prototype.add = function(settings, successHandler, errorHandler) {
	
	   	
	   if(this.folder == 'undefined') {
	   		throw (new Error('Folder is not defined'));
	   }	

	   if(this.title == 'undefined') {
	   		throw (new Error('Title is not defined'));
	   }	

	   if(this.description == 'undefined') {
	   		throw (new Error('Description is not defined'));
	   }	
	   if(this.isurl == 'undefined') {
	   		throw (new Error('IsUrl is not defined'));
	   }	
	   
       var payload = { };

       payload = {
              folder : this.folder,
              title : this.title, 
              description : this.description,
              url : this.isurl
            };

		send_ajax_post('https://mynotes.io/add', 'application/json', JSON.stringify(payload), settings, successHandler, errorHandler);
	
	};

	MyNotes.prototype.Note.prototype.remove = function(settings, successHandler, errorHandler) {
	
	   if(this.folder == 'undefined') {
	   		throw (new Error('Folder is not defined'))
	   }	

	   if(this.title == 'undefined') {
	   		throw (new Error('Title is not defined'))
	   }	

       var payload = { };

       payload = {
              folder : this.folder,
              title : this.title, 
            };

	   send_ajax_post('https://mynotes.io/remove', 'application/json',  JSON.stringify(payload), settings, successHandler, errorHandler);	
	};

	MyNotes.prototype.Note.prototype.view = function(settings, successHandler, errorHandler) {
	
	   if(this.folder == 'undefined') {
	   		throw (new Error('Folder is not defined'))
	   }	

	   if(this.title == 'undefined') {
	   		throw (new Error('Title is not defined'))
	   }	

       var payload = { };

       payload = {
              folder : this.folder,
              title : this.title, 
            };

	  send_ajax_post('https://mynotes.io/details', 'application/json',  JSON.stringify(payload), settings, successHandler, errorHandler);	
	};
 	
 	return MyNotes;
 
})();

