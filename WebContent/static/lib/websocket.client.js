class WebsocketClient {
    constructor(endpoint, callback) {

        this.webSocket = null;
        this.endpoint = endpoint;
        this.callback = callback;
    }

    getServerUrl() {
        const host = window.location.host;
        const pathname = window.location.pathname;
        return "ws://" + host + pathname + this.endpoint;
    }

    connect() {
        try {
            this.webSocket = new WebSocket(this.getServerUrl());
            // 
            // Implement WebSocket event handlers!
            //
            this.webSocket.onopen = (event) => {
                this.callback({
                    action: 'onopen',
                    body: event
                });
                console.log('onopen::' + JSON.stringify(event, null, 4));
            };

            this.webSocket.onmessage = (event) => {
                var msg = event.data;
                this.callback({
                    action: 'onmessage',
                    body: msg
                });
            };
            this.webSocket.onclose = (event) => {
                this.callback({
                    action: 'onclose',
                    body: event
                });
                console.log('onclose::' + JSON.stringify(event, null, 4));
            };
            this.webSocket.onerror = (event) => {
                this.callback({
                    action: 'onerror',
                    body: event
                });
                console.log('onerror::' + JSON.stringify(event, null, 4));
            }

        } catch (exception) {
            console.error(exception);
        }
    }

    getStatus() {
        return this.webSocket.readyState;
    }

    send(message) {

        if (this.webSocket.readyState === WebSocket.OPEN) {
            this.webSocket.send(message);

        } else {
            console.error('webSocket is not open. readyState=' + this.webSocket.readyState);
        }
    }

    disconnect() {
        if (this.webSocket.readyState === WebSocket.OPEN) {
            this.webSocket.close();

        } else {
            console.error('webSocket is not open. readyState=' + this.webSocket.readyState);
        }
    }
}