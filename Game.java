import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

//Acts like the client
public class Game {
    private static final String SERVER = "localhost";
    private static final int PORT = 9000;
    private static BufferedReader in;
    private static PrintWriter out;
    private static Socket server;
    private static Map map;

    public static void main(String[] args) {
        map = new Map();
        map.loadMap();
        map.printMap();
        
        try {
            server = new Socket(SERVER, PORT);
            gameLoop(server, map);
        } catch (IOException e) {
            map.message = "Server Not Found Please Start Server and Restart Game";
            map.printMap();
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        // System.out.println(Arrays.deepToString(map.map));
    }

    private static void gameLoop(Socket server, Map map) {
        Boolean playing = true;
        OutputStream outstream;
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            outstream = server.getOutputStream();
            out = new PrintWriter(outstream,true);
            // String status = "ready";
            // out.print(status);
            String response = in.readLine();
            if (response.startsWith("WELCOME")) {
                // TODO add player
                System.out.println("Starting Game");
            }

            while (playing) {
                if (in.ready()) {
                    response = in.readLine();
                    System.out.println(response);
                    if (response.startsWith("POS")) {
                        setPlayerPosition(map, response);
                    }
                    if (response.startsWith("OPP")) {
                        setOpponentPosition(map, response);
                    }
                    if(response.startsWith("OSCO")){
                        map.p2.score = Integer.parseInt(response.split(",")[1]);
                    }
                    if(response.startsWith("MSG")){
                        map.message = response.split(",")[1];
                    }
                }
                // System.out.println("Player Moved");
                movementController();
                if(map.p1.score + map.p2.score == 11){
                    playing = false;
                    if(map.p1.score > map.p2.score){
                        map.gameOver = "You win!!!";
                    }else if(map.p1.score < map.p2.score){
                        map.gameOver = "You Lose!!";
                    }else{
                        map.gameOver = "Game ended on uncertain conditions";
                    }
                }
                Thread.sleep(200);
                map.printMap();
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // public void setPosition(int x, int y){
    // this.position[0] = x;
    // this.position[1] = y;
    // }
    // public void sendPosition(){
    // this.output.println("POS"+","+position[0]+","+position[1]);
    // this.output.flush();
    // }
    private static void setPlayerPosition(Map map, String pos) {
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        map.setString(" ", map.getPlayerX(), map.getPlayerY());
        map.p1.setPosX(x);
        map.p1.setPosy(y);
        map.setString("@", x, y);
        out.println("NPOS"+","+map.getPlayerX()+","+map.getPlayerY());
        map.printMap();
    }

    private static void setOpponentPosition(Map map, String pos) {
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        map.setString(" ", map.p2.getX(), map.p2.getY());
        map.p2.setPosX(x);
        map.p2.setPosy(y);
        map.setString("$", x, y);
        map.printMap();
    }

    public static boolean validMove(String move) {
        int posX = map.getPlayerX();
        int posY = map.getPlayerY();
        int maxX = map.getMaxX();
        int maxY = map.getMaxY();
        String nextTile;
        switch (move) {
            case "right":
                nextTile = map.getStringAt(posX + 1, posY);
                break;

            case "left":
                nextTile = map.getStringAt(posX - 1, posY);
                break;

            case "down":
                nextTile = map.getStringAt(posX , posY + 1);
                break;

            case "up":
                nextTile = map.getStringAt(posX , posY - 1);
                break;
        
            default:
                nextTile = "#";
                break;
        }
        if(nextTile.equals("#") || nextTile.equals("$")){
            System.out.println("Wall or Enemy Found");
            return false;
        }
        if(nextTile.equals("G")){
            map.p1.score++;
            out.println("SCO"+","+map.p1.score);
            return true;
        }
        return true;

        // if (move.equals("right")) {
        //     if (posX < maxX && !map.getStringAt(posX + 1, posY).equals("#")) {
        //         return true;
        //     } /*
        //        * else if (posX < maxX && !map.getStringAt(posX + 1, posY).equals("$")) {
        //        * return true;
        //        * }
        //        */
        //     return false;
        // } else if (move.equals("left")) {
        //     if (posX > 0 && !map.getStringAt(posX - 1, posY).equals("#")) {
        //         return true;
        //     } /*
        //        * else if (posX > 0 && !map.getStringAt(posX - 1, posY).equals("$")) {
        //        * return true;
        //        * }
        //        */
        //     return false;
        // } else if (move.equals("down")) {
        //     if (posY < maxY && !map.getStringAt(posX, posY + 1).equals("#")) {
        //         return true;
        //     } /*
        //        * else if (posY < maxY && !map.getStringAt(posX, posY + 1).equals("$")) {
        //        * return true;
        //        * }
        //        */
        //     return false;
        // } else {// move.equals("up")
        //     if (posY > 0 && !map.getStringAt(posX, posY - 1).equals("#")) {
        //         return true;
        //     } /*
        //        * else if (posY > 0 && !map.getStringAt(posX, posY - 1).equals("$")) {
        //        * return true;
        //        * }
        //        */
        //     return false;
        // }
    }

    // public static void

    public static void movementController() {
        // System.out.println("Starting Movement Controls");
        // boolean stop = false;
        // while (!stop) {
        Key key = map.screen.readInput();
        // while (key == null) {
        //     key = map.screen.readInput();
        // }

        // Move around with arrow keys in normal map view escape closes the application
        try{
        switch (key.getKind()) {
            case Escape:
                // stop = true;     
                try {
                    out.println("QUIT");
                    out.flush();
                    server.close();
                    map.screen.stopScreen();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case ArrowRight:
                if (validMove("right")) {
                    setPlayerPosition(map, "POS"+","+(map.p1.getX()+1)+","+map.p1.getY());
                }
                break;
            case ArrowLeft:
                if (validMove("left")) {
                    setPlayerPosition(map, "POS"+","+(map.p1.getX()-1)+","+map.p1.getY());
                }
                break;

            case ArrowDown:
                if (validMove("down")) {
                    setPlayerPosition(map, "POS"+","+map.p1.getX()+","+(map.p1.getY()+1));
                }
                break;

            case ArrowUp:
                if (validMove("up")) {
                    setPlayerPosition(map, "POS"+","+map.p1.getX()+","+(map.p1.getY()-1));
                }
                break;
            default:
                break;
        }
    }catch(NullPointerException e){

    }
        // try {
        // // System.out.println("sending map to server");
        // op.writeObject(map);
        // op.flush();
        // // System.out.println("Sent new message");

        // Map map = (Map) input.readObject();
        // map = map;
        // map.printMap();
        // // System.out.println("Getting map from server");
        // } catch (IOException | ClassNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
    }

}
