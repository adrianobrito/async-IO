$(function () {
    "use strict";

    var content = $('#content');
    var input = $('#input');
    var status = $('#status');
    var myName = false;
    var author = null;
    var logged = false;
    var socket = $.atmosphere;
    var request = { url: document.location.toString() + 'meteor',
                    logLevel : 'debug',
                    transport : 'websocket',
                    reconnectInterval : 5000,
                    fallbackTransport: 'long-polling'};


    request.onOpen = function(response) {
        content.html($('<p>', { text: 'Atmosphere connected using ' + response.transport }));
        input.removeAttr('disabled').focus();
        status.text('Choose name:');
    };

    request.onMessage = function (response) {
        var message = response.responseBody;
        
        content.html(message);
    };

    request.onError = function(response) {
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your '
            + 'socket or the server is down' }));
    };

    request.onClose = function(response) {
        logged = false;
    }

    // This is where the clients handshakes the persistent connection
    var subSocket = socket.subscribe(request);

});

