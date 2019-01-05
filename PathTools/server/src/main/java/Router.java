import APIFunctions.ImportPath;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Router {

    public void routeRequest(WebSocket webSocket, JSONObject jsonObject) {
        String command = jsonObject.getString("command");

        if (command.equals("IMPORT_PATH")) {
            System.out.println("successful IMPORT_PATH");
            (new ImportPath(webSocket, jsonObject)).sendFileContents();
        } else {
            System.out.println("Command " + command + " does not exist");
        }
    }
}
