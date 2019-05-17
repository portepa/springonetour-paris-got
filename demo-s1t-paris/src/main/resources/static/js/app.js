var stompClient = null;

let votes = [];

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
    votes = JSON.parse(data);
    render();
});

function connect() {
    var socket = new SockJS('/sockjs');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/vote', function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function sendMessage(voteIndex) {
    stompClient.send("/vote", {}, JSON.stringify({ voteIndex }));
}

function render() {
    if (votes.length > 0) {
        const occ = _.countBy(votes.map(v => v.voteIndex));
        console.log(occ);
        let valLeft = occ[0]/votes.length * 100; // should be %age of width
        let valRight = occ[1]/votes.length * 100;
        valLeft = Number.isNaN(valLeft) ? 0 : valLeft;
        valRight = Number.isNaN(valRight) ? 0 : valRight;
        valLeft = valLeft > 50 ? Math.min(valLeft, 90) : Math.max(valLeft, 10);
        valRight = valRight > 50 ? Math.min(valRight, 90) : Math.max(valRight, 10);
        document.getElementsByClassName('left')[0].style.width = valLeft + "%";
        document.getElementsByClassName('right')[0].style.width = valRight + "%";
        document.getElementById('left-subtitle').innerHTML = occ[0] || 0;
        document.getElementById('right-subtitle').innerHTML = occ[1] || 0;
    }
}

function showMessageOutput(messageOutput) {
    console.log(votes);
    votes.push(messageOutput);
    render();
}