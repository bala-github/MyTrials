function getPathPromise(id, path) {
	return new Promise(function(resolve, reject) {
		console.log('id:' + id);
		//getPath(id, path, resolve, reject);			
		var bookmarknode = chrome.bookmarks.get(id, function(bookmarknodes) {
			console.log('folder:' + bookmarknodes[0].title);
			console.log('parent id:' + bookmarknodes[0].parentId);
			path = bookmarknodes[0].title + '/' + path;
		
			if(typeof bookmarknodes[0].parentId !== 'undefined') {		
				resolve(getPathPromise(bookmarknodes[0].parentId, path));
			} else {
				resolve(path);
			}
		});	
	});
}

function handle_add_note_success(data, status, xhr) {
	 console.log('Status:' + status);
}

function handle_add_note_error(data, status, xhr) {
	console.log('Status:' + status);
}



function logBookMark(id, bookmark) {
  console.log("We bookmarked  " + bookmark.url + "...");
  //getPathAndAddBookMark(bookmark.parentId, '', bookmark.url, bookmark.title);	
  var pathPromise = getPathPromise(bookmark.parentId, '');
  
  pathPromise.then(function(value) {
	console.log('Path is ' + value);
	var myNotes = new MyNotesFunc();
	var note = new myNotes.Note('/bookmarks' + value, bookmark.title, bookmark.url, true);

	note.add(undefined, handle_add_note_success, handle_add_note_error);

  });
}

function handle_remove_notes_success(data, status, xhr) {
	 console.log('Status:' + status);
}

function handle_remove_notes_error(data, status, xhr) {
	console.log('Status:' + status);
}

function removeBookMark(id, info) {
	console.log("We removed bookmark " + info.node.url);
	
	var pathPromise = getPathPromise(info.parentId, '');
  
	pathPromise.then(function(value) {
		console.log('Path is ' + value);
		
		var folder = '';
		var title = '';
		if(info.node.url === undefined) { 
			//folder
			folder = '/bookmarks' + value + info.node.title;
		} else {
			//url
			folder = '/bookmarks' + value;
			title = info.node.title;
		}

		var myNotes = new MyNotesFunc();
		var note = new myNotes.Note(folder, title);
		note.remove(undefined, handle_remove_notes_success, handle_remove_notes_error);

	});
}

function moveBookMark(id, moveInfo) {

	chrome.bookmarks.get(id, function(bookmarknodes) {
		
		if(bookmarknodes[0].url !== undefined) {
			//We moved a url.
			console.log("Moved " + bookmarknodes[0].title + " from " + moveInfo.oldParentId + " to " +  moveInfo.parentId);
			var pathPromise = getPathPromise(moveInfo.oldParentId, '');
			pathPromise.then(function(value) {
				console.log('Old Path is ' + value);
				var myNotes = new MyNotesFunc();
				var note = new myNotes.Note('/bookmarks'+ value , bookmarknodes[0].title);	
				note.remove(undefined, handle_remove_notes_success, handle_remove_notes_error);
				
			});
			
			pathPromise = getPathPromise(moveInfo.parentId, '');
			
			pathPromise.then(function(value) {
				console.log('New Path is ' + value);
				var myNotes = new MyNotesFunc();
				var note = new myNotes.Note('/bookmarks'+ value , bookmarknodes[0].title, bookmarknodes[0].url, true);	
				note.add(undefined, handle_add_note_success, handle_add_note_error);
			});

			//getPathAndRemoveBookMark(moveInfo.oldParentId, '', bookmarknodes[0].title);	
			//getPathAndAddBookMark(moveInfo.parentId, '', bookmarknodes[0].url, bookmarknodes[0].title);	
		} else {
			//We moved a folder
			var oldPathPromise = getPathPromise(moveInfo.oldParentId, '');
			var newPathPromise = getPathPromise(moveInfo.parentId, '');
			
			Promise.all([oldPathPromise , newPathPromise]).then(function (values) {
				var oldPath = '/bookmarks' + values[0] + bookmarknodes[0].title;	
				var newPath = '/bookmarks' + values[1] + bookmarknodes[0].title;	
				console.log('OldPath is ' + oldPath);
				console.log('NewPath is ' + newPath);
				var myNotes = new MyNotesFunc();
				var oldNote = new myNotes.Note(oldPath, undefined);
				var newNote = new myNotes.Note(newPath, undefined);
				
				oldNote.move(newNote, undefined, handle_add_note_success, handle_add_note_error);
				
			});
	
		}
	});
	
}


chrome.bookmarks.onCreated.addListener(logBookMark);
chrome.bookmarks.onRemoved.addListener(removeBookMark);
chrome.bookmarks.onMoved.addListener(moveBookMark);


chrome.storage.local.set({'test' : 'stored!'}, function() {
  console.log("Saved");
  console.log(chrome.runtime.lastError);
});