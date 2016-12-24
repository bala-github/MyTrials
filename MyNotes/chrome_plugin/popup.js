// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// Search the bookmarks when entering the search keyword.

var myNotes = new MyNotesFunc();

function handle_get_user_success(data, status, xhr) {
	var content = '';
	content = content + `<p>Hi ` + data['name'] + `!</p> ` +
	`<p> Sync bookmarks in this browser with your dropbox folder </p>` +
	`<button id="update_bookmarks">Sync now</button> </p> `;

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
	 //send_ajax_get('https://mynotes.io/user', 'undefined', handle_get_user_success, handle_get_user_error);
	 myNotes.getUser('undefined', handle_get_user_success, handle_get_user_error);
}

$(document).on('click', '#register_link', function() {
	chrome.tabs.create({url: 'https://www.dropbox.com/oauth2/authorize?response_type=code&client_id=1qg4oqlortls1ho'});
});


$(document).on('click', '#register_button', function() {
	myNotes.registerUser($('#token').val(), 'undefined', handle_get_login_success, handle_get_login_success);
});

function dumpBookMarks() {
	var bookmarks = new Array();
	console.log("Dumping book marks");
	chrome.bookmarks.getTree(
    function(bookmarkTreeNodes) {
       dumpTreeNodes(bookmarkTreeNodes[0].children, '/bookmarks', bookmarks);
	   //console.log(bookmarkTreeNodes);
    });
	console.log(bookmarks.sort());
}

function dumpTreeNodes(bookmarkTreeNodes, parent, bookmarks) {
	
	for (var i = 0; i < bookmarkTreeNodes.length; i++) {
		if(!bookmarkTreeNodes[i].children) {
			
			
			bookmarks.push(parent + '/' + bookmarkTreeNodes[i].title + "------" + bookmarkTreeNodes[i].url);
		} else {
			console.log('Title' + bookmarkTreeNodes[i].title);
			console.log('i' + i);
			dumpTreeNodes(bookmarkTreeNodes[i].children, parent + '/' + bookmarkTreeNodes[i].title, bookmarks);
			console.log('i' + i);
		}
	}	
}
$(document).on('click', '#update_bookmarks', function() {
		console.log("Dumping bookmarks...");
		dumpBookMarks();
});



function handle_get_login_success(data, status, xhr) {
	myNotes.getUser('undefined', handle_get_user_success, handle_get_user_error);
}




$(document).ready(function () {
	getUser();	
	

});

