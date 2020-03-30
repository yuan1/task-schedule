window.onload = function () {
    const client = new WebsocketClient('/endpoint', function (data) {
        console.log(data);
    });
    client.connect();
    window.client = client;
};

function sendMessage() {
    console.log("send message");
    window.client.send("hello world")
}