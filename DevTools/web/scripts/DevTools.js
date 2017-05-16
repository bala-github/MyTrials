var DevTools = (function () {

 	function send_ajax_get(url, settings, successHandler, errorHandler) {
 	
 		if(typeof settings === undefined) {
 			settings = {};
 		} 
 		
 		if(typeof errorHandler === undefined) {
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
 	
 		if(typeof settings === undefined) {
 			settings = {};
 		} 
 		
 		if(typeof errorHandler === undefined) {
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
 	
 	var url = "http://localhost/api/";
 		
	function DevTools() {
		
	}
	
	DevTools.prototype.getESQuery = function(sql) {
		var data = {'input' : sql};
			
		var promise = new Promise(function(resolve, reject) {
		
			$.ajax({
	 			type : 'POST',
	 			url : url + 'action/convert_to_es',
	 			contentType : 'application/json',
	 			data : JSON.stringify(data),
	 			success : function(data, status, xhr) { resolve(data); },
	 			error : function(data, errorStatus, err) { reject('An error occured.'); }
	 		});		
			
		});
		
		return promise;
		
	}

	return DevTools;
})();