import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Game {
    private static final String SERVER = "localhost";
    private static final int PORT = 9000;
    private static BufferedReader in;
    private static Socket server;

    public static void main(String[] args) {
        Map map = new Map();
        map.loadMap();
        map.printMap();
        try {
            server = new Socket(SERVER, PORT);
            gameLoop(server,map);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(Arrays.deepToString(map.map));
    }

    private static void gameLoop(Socket server,Map map) {
        Boolean playing = true;
        OutputStream outstream;
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            outstream = server.getOutputStream();
            PrintWriter out = new PrintWriter(outstream);
            // String status = "ready";
            // out.print(status);
            String response = in.readLine();
            if (response.startsWith("WELCOME")) {
                //TODO add player
                System.out.println("Starting Game");
            }

            while (playing) {
                if (in.ready()) {
                    response = in.readLine();
                    System.out.println(response);
                    if(response.startsWith("POS")){
                        setPlayerPosition(map,response);
                    }
                    if(response.startsWith("OPP")){
                        setOpponentPosition(map,response);
                    }
                }
                System.out.println("Player Moved");
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private static void setPlayerPosition(Map map,String pos){
        String[] args = pos.split(",");
        map.setString("@",Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        map.printMap();
    }
    private static void setOpponentPosition(Map map,String pos){
        String[] args = pos.split(",");
        map.setString("$",Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        map.printMap();
    }

}
