package botrestapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.ControllerController;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * Created by sinakashipazha on 2/27/2017 AD.
 */

public class TelegramGate {

    private static int portNumber;
    private static String url = "/" + Token.getToken();

    public static void main(String[] args) {
        portNumber = Integer.parseInt(args[0]);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
            server.createContext(url, new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {
        private ControllerController controllerController = new ControllerController();

        private JSONObject createRegularMessageFromCallbackQuery(JSONObject jsonObject) {
            JSONObject result = new JSONObject();
            result.put("update_id", jsonObject.getInt("update_id"));

            JSONObject message = new JSONObject();
            jsonObject = jsonObject.getJSONObject("callback_query");

            message.put("from", jsonObject.getJSONObject("from"));
            message.put("text", jsonObject.getString("data"));

            jsonObject = jsonObject.getJSONObject("message");

            message.put("chat", jsonObject.getJSONObject("chat"));
            message.put("date", jsonObject.getInt("date"));
            message.put("message_id", jsonObject.getInt("message_id"));


            result.put("message", message);

            System.out.println(result);

            return result;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {

            String response = "hello world!";
            InputStream temp = t.getRequestBody();
            BufferedReader in = new BufferedReader(new InputStreamReader(temp));
            String input, temp2 = "";
            StringBuilder inputBuilder = new StringBuilder();
            while ((temp2 = in.readLine()) != null) {
                inputBuilder.append(temp2);
            }
            input = inputBuilder.toString();

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            try {
                System.out.println(input);
                JSONObject jsonObject = new JSONObject(input);
                String command = "";

                if (jsonObject.has("callback_query")) {
                    jsonObject = createRegularMessageFromCallbackQuery(jsonObject);
                }

                command = jsonObject.getJSONObject("message")
                        .getString("text");
                controllerController.controllerFactory(command, jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
