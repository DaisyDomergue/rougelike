import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

public class Game {
    private static final String SERVER = "localhost";
    private static final int PORT = 9000;
    private static BufferedReader in;
    private static Socket server;
    private static Map map;

    public static void main(String[] args) {
        map = new Map();
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
                movementController();
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    // public void setPosition(int x, int y){
    //     this.position[0] = x;
    //     this.position[1] = y;
    // }
    // public void sendPosition(){
    //     this.output.println("POS"+","+position[0]+","+position[1]);
    //     this.output.flush();
    // }
    private static void setPlayerPosition(Map map,String pos){
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        map.p1.setPosX(x);
        map.p1.setPosy(y);
        map.setString("@",x,y);
        map.printMap();
    }
    private static void setOpponentPosition(Map map,String pos){
        String[] args = pos.split(",");
        map.setString("$",Integer.parseInt(args[1]),Integer.parseInt(args[2]));
        map.printMap();
    }
    public static boolean validMove(String move) {
        int posX = map.getPlayerX();
        int posY = map.getPlayerY();
        int maxX = map.getMaxX();
        int maxY = map.getMaxY();

        if (move.equals("right")) {
            if (posX < maxX && !map.getStringAt(posX + 1, posY).equals("#")) {
                return true;
            }
            return false;
        } else if (move.equals("left")) {
            if (posX > 0 && !map.getStringAt(posX - 1, posY).equals("#")) {
                return true;
            }
            return false;
        } else if (move.equals("down")) {
            if (posY < maxY && !map.getStringAt(posX, posY + 1).equals("#")) {
                return true;
            }
            return false;
        } else {// move.equals("up")
            if (posY > 0 && !map.getStringAt(posX, posY - 1).equals("#")) {
                return true;
            }
            return false;
        }
    }
    public static void movementController() {
        //System.out.println("Starting Movement Controls");
        // boolean stop = false;
        // while (!stop) {
            Key key = map.screen.readInput();
            while (key == null) {
                key = map.screen.readInput();
            }

            // Move around with arrow keys in normal map view escape closes the application
            switch (key.getKind()) {
                case Escape:
                    // stop = true;
                    break;
                case ArrowRight:
                    if (validMove("right")) {
                        map.movePlayerRight();
                    }
                    map.printMap();
                    break;
                case ArrowLeft:
                    if (validMove("left")) {
                        map.movePlayerLeft();
                    }
                    map.printMap();
                    break;

                case ArrowDown:
                    if (validMove("down")) {
                        map.movePlayerDown();
                    }
                    map.printMap();
                    break;

                case ArrowUp:
                    if (validMove("up")) {
                        map.movePlayerUp();
                    }
                    map.printMap();
                    break;
                default:
                    break;
            }
            // try {
            //    // System.out.println("sending map to server");
            //     op.writeObject(map);
            //     op.flush();
            //    // System.out.println("Sent new message");

            //     Map map = (Map) input.readObject();
            //     map = map;
            //     map.printMap();
            //    // System.out.println("Getting map from server");
            // } catch (IOException | ClassNotFoundException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }
        // }
    }

}
