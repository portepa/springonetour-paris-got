var stompClient = null;

function httpGet(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous
    xmlHttp.send(null);
}

httpGet('/vote', function(data) {
  console.log(data);
});

function setConnected(connected) {
    document.getElementById('response').innerHTML = '';
}

function connect() {
    var socket = new SockJS('/sockjs');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/vote', function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function showMessageOutput(messageOutput) {
    console.log('messag output');
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(JSON.stringify(messageOutput)));
    response.appendChild(p);
}