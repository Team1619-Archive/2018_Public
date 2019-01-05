import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;

public class Server extends WebSocketServer {
    private Router router = new Router();

    public Server(InetSocketAddress address) {
        super(address);
    }

    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("New connection to " + webSocket.getRemoteSocketAddress());
    }

    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Connection closed " + webSocket.getRemoteSocketAddress());
    }

    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("New message from " + webSocket.getRemoteSocketAddress());

        router.routeRequest(webSocket, new JSONObject(s));
    }

    public void onError(WebSocket webSocket, Exception e) {

    }

    public void onStart() {
        System.out.println("Server started");
    }

    public static void main(String[] args) {
        WebSocketServer server = new Server(new InetSocketAddress("localhost", 7777));
        System.out.println("Starting up server");
        server.run();
        System.out.println("Server shutdown");
    }
}
