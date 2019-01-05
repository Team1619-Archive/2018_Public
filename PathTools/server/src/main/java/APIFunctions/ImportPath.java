package APIFunctions;

import org.apache.commons.io.IOUtils;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImportPath {
    private WebSocket webSocket;
    private JSONObject jsonObject;

    public ImportPath(WebSocket webSocket, JSONObject jsonObject) {
        this.webSocket = webSocket;
        this.jsonObject = jsonObject;
    }

    public void sendFileContents() {
        File path = new File("./data/" + this.jsonObject.getString("path")).getAbsoluteFile();

        String pathJSONString = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            pathJSONString = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            System.out.println("File " + path + " does not exist");
        }

        this.webSocket.send(pathJSONString);
    }
}
