function handle_add_note_success(data, status, xhr) {
	 console.log('Status:' + status);
}

function handle_add_note_error(data, status, xhr) {
	console.log('Status:' + status);
}

function getPathAndAddBookMark(id, path, url, title) {
  console.log('id:' + id);
  var bookmarknode = chrome.bookmarks.get(id, function(bookmarknodes) {
	console.log('folder:' + bookmarknodes[0].title);
	console.log('parent id:' + bookmarknodes[0].parentId);
	path = bookmarknodes[0].title + '/' + path;
	if(typeof bookmarknodes[0].parentId !== 'undefined') {
		getPathAndAddBookMark(bookmarknodes[0].parentId, path, url, title);
	} else {
	    console.log('Path:' + path);
		
		var payload = { }; 	
		payload = {
		  folder :  '/bookmarks' + path,
		  title : title, 
		  description : url,
		  url : true
		};

		send_ajax_post('https://mynotes.io/add', 'application/json', JSON.stringify(payload), 'undefined', handle_add_note_success, handle_add_note_error);
		
		chrome.storage.local.get('test', function(items) {
			for(key in items) {
				console.log('Value:' + items[key]);
			}
		});  
		
	}
  });
}


function logBookMark(id, bookmark) {
  console.log("We bookmarked " + bookmark.url + ".");
  getPathAndAddBookMark(bookmark.parentId, '', bookmark.url, bookmark.title);	
}

function handle_post_remove_notes_success(data, status, xhr) {
	 console.log('Status:' + status);
}

function handle_post_remove_notes_error(data, status, xhr) {
	console.log('Status:' + status);
}
function getPathAndRemoveBookMark(id, path, title) {
  console.log('id:' + id);
  var bookmarknode = chrome.bookmarks.get(id, function(bookmarknodes) {
	console.log('folder:' + bookmarknodes[0].title);
	console.log('parent id:' + bookmarknodes[0].parentId);
	path = bookmarknodes[0].title + '/' + path;
	if(typeof bookmarknodes[0].parentId !== 'undefined') {
		getPathAndRemoveBookMark(bookmarknodes[0].parentId, path, title);
	} else {
	    console.log('Path:' + path);
		
  		var payload = {};
   		payload = {
   			folder : '/bookmarks' + path,
   			title : title
   		};
   		
   		var settings = {};
	          	    
   		send_ajax_post('https://mynotes.io/remove', 'application/json',  JSON.stringify(payload), settings, handle_post_remove_notes_success, handle_post_remove_notes_error);
		
		chrome.storage.local.get('test', function(items) {
			for(key in items) {
				console.log('Value:' + items[key]);
			}
		});  
		
	}
  });
}
function removeBookMark(id, info) {
	console.log("We removed bookmark " + info.node.url);
	getPathAndRemoveBookMark(info.parentId, '', info.node.title);
	
}

function moveBookMark(id, moveInfo) {

	chrome.bookmarks.get(id, function(bookmarknodes) {
		console.log("Moved " + bookmarknodes[0].title + " from " + moveInfo.oldParentId + " to " +  moveInfo.parentId);
		getPathAndRemoveBookMark(moveInfo.oldParentId, '', bookmarknodes[0].title);	
		getPathAndAddBookMark(moveInfo.parentId, '', bookmarknodes[0].url, bookmarknodes[0].title);	
	});
	
}
chrome.bookmarks.onCreated.addListener(logBookMark);
chrome.bookmarks.onRemoved.addListener(removeBookMark);
chrome.bookmarks.onMoved.addListener(moveBookMark);

chrome.storage.local.set({'test' : 'stored!'}, function() {
  console.log("Saved");
  console.log(chrome.runtime.lastError);
});