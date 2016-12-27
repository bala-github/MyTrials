// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// Search the bookmarks when entering the search keyword.

var myNotes = new MyNotesFunc();

function handle_get_user_success(data, status, xhr) {
	var content = '';
	content = content + `<p>Hi ` + data['name'] + `!</p> ` +
	`<p> Sync bookmarks in this browser with your dropbox folder </p>` +
	`<button id="update_bookmarks">Sync now</button> </p> ` +
	`<p id="result"> </p>`;
     
	$('#bookmarks').html(content);
	

}	
			
function handle_get_user_error(xhr,errorStatus,err) {

	if(err == 'Unauthorized') {
		var content = '';
		content = content + `<p>Welcome! You need to register. </p> ` +
		`<p> <a id='register_link' href='#'>Click here </a>  to get a token from DropBox.</p> ` +
		`<p> Paste the token below and click register <p> ` +
		`<input id="token">  &nbsp;&nbsp; <button id="register_button">Register</button> `;

		$('#bookmarks').html(content);
	}

}
function getUser() {
	 console.log('get user called');	 
	 myNotes.getUser(undefined, handle_get_user_success, handle_get_user_error);
}

$(document).on('click', '#register_link', function() {
	chrome.tabs.create({url: 'https://www.dropbox.com/oauth2/authorize?response_type=code&client_id=1qg4oqlortls1ho'});
});


$(document).on('click', '#register_button', function() {
	myNotes.registerUser($('#token').val(), undefined, handle_get_login_success, handle_get_login_success);
});

function dumpBookMarks() {
	var bookmarks = new Array();
	var folderIds = new Map();
	console.log("Dumping book marks");
	chrome.bookmarks.getTree(
    function(bookmarkTreeNodes) {
       dumpTreeNodes(bookmarkTreeNodes[0].children, '/bookmarks', bookmarks, folderIds);
	   console.log(bookmarks.sort());
	   console.log(folderIds);
	   var settings = { "bookmarks" : bookmarks, "folderIds" : folderIds};
	   
	   myNotes.getNotes('/bookmarks', true, settings, syncBookMarks, syncErrors);
    });
	
}

function syncBookMarks(data, status, xhr) {
	console.log('Status:' + status);
	console.log(data['entries']);
	var serverBookMarks = new Array();
	for(let entry of data['entries']) {
		//console.log(entry);
		if(entry['pathDisplay'] && entry['pathDisplay'].endsWith('.json')) {
			//console.log(entry['pathDisplay']);
			serverBookMarks.push(entry['pathLower'].replace('.json','') + "\x1e" + entry['pathDisplay'].replace('.json',''));
		}
	}
	console.log('Bookmars in browser...');
	console.log(this.settings.bookmarks.sort());
	console.log('Bookmars in server...');
	console.log(serverBookMarks.sort());
	
	var bookmarksNotInServer = new Array();
	bookmarksNotInBrowser = new Array();
	var i = 0, j= 0, result = 0;
	while(i < this.settings.bookmarks.length && j < serverBookMarks.length) {
	    result = this.settings.bookmarks[i].split("\x1e")[0].localeCompare(serverBookMarks[j].split("\x1e")[0]);
		
		
		if(result == 0) {
			//console.log('i='+i + 'j='+ j);
			i++;
			j++;
			continue;
		} else if (result < 0) {
			//console.log('i='+i);
			var fields = this.settings.bookmarks[i].split("\x1e");
			var note = new myNotes.Note(fields[1].substring(0, fields[1].lastIndexOf('/')), fields[1].substr(fields[1].lastIndexOf('/') + 1), fields[2], true);
			
			bookmarksNotInServer.push(note);
			i++;	
		} else {
			//console.log('j='+ j);
			//console.log(this.settings.bookmarks[i].split("\x1e")[0].toLowerCase());
			//console.log(serverBookMarks[j].toLowerCase());
			//console.log(this.settings.bookmarks[i].split("\x1e")[0].toLowerCase().localeCompare(serverBookMarks[j].toLowerCase()));

			bookmarksNotInBrowser.push(serverBookMarks[j]);
			j++;
		}
	}

	while(i < this.settings.bookmarks.length) {
		var fields = this.settings.bookmarks[i].split("\x1e");
		var note = new myNotes.Note(fields[1].substring(0, fields[1].lastIndexOf('/')), fields[1].substr(fields[1].lastIndexOf('/') + 1), fields[2], true);

		bookmarksNotInServer.push(note);
		i++;
	}
	
	while(j < serverBookMarks.length) {
		bookmarksNotInBrowser.push(serverBookMarks[j]);
		j++;
	}
	
	console.log('Bookmarks not in server...');
	console.log(JSON.stringify(bookmarksNotInServer));
	
	myNotes.uploadNotes(bookmarksNotInServer, undefined, uploadSuccess, uploadError);
	
	console.log('Bookmarks not in browser...');
	//console.log(bookmarksNotInBrowser);
	
	for(let entry of bookmarksNotInBrowser) {
		var bookmark = entry.split("\x1e")[1];
		var folders = bookmark.split("/");
		console.log("Creating bookmark " + bookmark);
		
		var note = new myNotes.Note(bookmark.substring(0, bookmark.lastIndexOf('/')), bookmark.substr(bookmark.lastIndexOf('/') + 1));
		console.log(this.settings.folderIds);
		var settings = { "folders" : folders,  "folderIds" : this.settings.folderIds, "folder" : note.folder};
		note.view(settings, getBookMarkSuccess, getBookMarkErrors);
	}
}

function syncErrors(xhr,errorStatus,err) {
	console.log('Stats:' + errorStatus);
}

function uploadSuccess(data, status, xhr) {
	$('#result').text(JSON.stringify(data));
}
function uploadError(xhr,errorStatus,err) {
	console.log('Error:' + errorStatus);
	$('#result').text('Error Synchronizing...');
}

function getBookMarkSuccess(data, status, xhr) {
	console.log('We need to create bookmark' + this.settings.folder);
	console.log('Content is ' + data); 
	console.log(this.settings);
	
	createBookMark(this.settings.folders, 2 /*0 is empty, 1 is bookmarks*/, this.settings.folderIds, '/bookmarks', data);
}

function createBookMark(folders, startIndex, folderIds, path, content) {
    //folder that we need to check if it exists.
    var folder = path + '/' + folders[startIndex];
	console.log('StartIndex:' + startIndex);
	console.log('folder that we need to check if it exists.' + folder);
	console.log(folderIds);
	console.log(folderIds.get(folder.toLowerCase()));
	
	if(startIndex > 5) {
		return;
	}
	
	if(startIndex == folders.length-1) {
		//we have created all the parent folders.
		
		var bookmark = { "parentId" : folderIds.get(path.toLowerCase()), "title" : folders[startIndex], "url" :JSON.parse(content).description};
		console.log(bookmark);
		
		chrome.bookmarks.create(bookmark);
	    return;						   
	}
	
	if(folderIds.get(folder.toLowerCase()) !== undefined) {
		return createBookMark(folders, startIndex + 1, folderIds, folder, content);
	} else {
		//have to create a parent folder.
		console.log('Creating parent folder');
		var bookmark = { "parentId" : folderIds.get(path.toLowerCase()), "title" : folders[startIndex]};
		console.log(bookmark);
		
		chrome.bookmarks.create(bookmark, function(bookmarkNode) {
		
			folderIds.set(folder.toLowerCase(), bookmarkNode.id);
			console.log(folderIds);
			return createBookMark(folders, startIndex + 1, folderIds, folder, content);
		});
	}

	
}
function getBookMarkErrors(xhr,errorStatus,err) {
	console.log('Error Status:' + errorStatus);
}

function dumpTreeNodes(bookmarkTreeNodes, parent, bookmarks, folderIds) {
	
	for (var i = 0; i < bookmarkTreeNodes.length; i++) {
		if(!bookmarkTreeNodes[i].children) {
			bookmarks.push((parent + '/' + bookmarkTreeNodes[i].title).toLowerCase() + "\x1e" + parent + '/' + bookmarkTreeNodes[i].title + "\x1e" + bookmarkTreeNodes[i].url);
		} else {
			folderIds.set((parent + '/' + bookmarkTreeNodes[i].title).toLowerCase(), bookmarkTreeNodes[i].id);
			dumpTreeNodes(bookmarkTreeNodes[i].children, parent + '/' + bookmarkTreeNodes[i].title, bookmarks, folderIds);
		}
	}	
}
$(document).on('click', '#update_bookmarks', function() {
		console.log("Dumping bookmarks...");
		$('#result').text("Synchronizing...");
		dumpBookMarks();
});



function handle_get_login_success(data, status, xhr) {
	myNotes.getUser(undefined, handle_get_user_success, handle_get_user_error);
}




$(document).ready(function () {
	getUser();	
	

});

