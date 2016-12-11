// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// Search the bookmarks when entering the search keyword.

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


	
function clickHandler(e) {
  send_ajax_get('https://mynotes.io/user', 'undefined', handle_get_user_success, handle_post_get_notes_error);
}

function handle_get_user_success(data, status, xhr) {
	var content = '';
	content = content + `<p>Hi ` + data['name'] + `!</p> ` +
	`<p> Sync bookmarks in this browser with your dropbox folder` +
	`&nbsp;&nbsp; <button id="sync_button">Sync now</button> `;

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
	 
	 send_ajax_get('https://mynotes.io/user', 'undefined', handle_get_user_success, handle_get_user_error);
}

$(document).on('click', '#register_link', function() {
	chrome.tabs.create({url: 'https://www.dropbox.com/oauth2/authorize?response_type=code&client_id=1qg4oqlortls1ho'});
});


$(document).on('click', '#register_button', function() {
	send_ajax_get('https://mynotes.io/access_token?code=' + $('#token').val(), handle_get_login_success, handle_get_login_success);
});

function handle_get_login_success(data, status, xhr) {
	send_ajax_get('https://mynotes.io/user', 'undefined', handle_get_user_success, handle_get_user_error);
}

function test() {
	send_ajax_get('https://mynotes.io/user', 'undefined', handle_get_user_success, handle_get_user_error);
}


$(document).ready(function () {
	getUser();	
});