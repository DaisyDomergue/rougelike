import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/* 
 This is the server class. Listens for incoming connections at port 9000.

*/
public class Server {
    private static final int PORT = 9000;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT); // create server socket and listen for connections
            System.out.println("Server started!");
            waitForPlayers(server);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Connection Broken");
        }
    }

    public static void waitForPlayers(ServerSocket server) {
        try {
            PlayerThread p1 = new PlayerThread(server.accept(), "p1"); // accept connection from client "p1"
            PlayerThread p2 = new PlayerThread(server.accept(), "p2"); // accept connection from client "p2"
            p1.setOpponent(p2);
            p2.setOpponent(p1);
            p1.setPosition(1, 1);
            p2.setPosition(48, 18);
            p1.start();
            p2.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

/*
 * This class represents connected clients/players in the server.
 * Every client is executed as a thread in the server.
 */
class PlayerThread extends Thread {
    String mark; // p1 for player 1 and p2 for player 2
    PlayerThread opponent;
    int Score; // current score of a player
    int position[] = new int[2]; // current position of a player in x and y cord.
    Socket socket; // socket to send data to client
    BufferedReader input; // get input from client
    PrintWriter output; // send output from client

    // thread handler to initialize stream fields
    public PlayerThread(Socket socket, String mark) {
        this.socket = socket;
        this.mark = mark;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + mark); // send greeting to client
            output.println("MESSAGE Waiting for opponent to connect"); // for first client, inform that another player
                                                                       // hasnt connected yet.
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }

    /*
     * Set the opponent of a player
     */
    public void setOpponent(PlayerThread opponent) {
        this.opponent = opponent;
    }

    /*
     * Set the position of a player in game field.
     * Input the cords in position array.
     */
    public void setPosition(int x, int y) {
        this.position[0] = x;
        this.position[1] = y;
    }

    public void setMyNewPosition(String pos) {
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        this.position[0] = x;
        this.position[1] = y;
    }

    /*
     * Send the position of a player from server to client
     */
    public void sendPosition() {
        this.output.println("POS" + "," + position[0] + "," + position[1]);
        this.output.flush();
    }

    /*
     * Send the position of an opponent from server to client
     */
    public void sendOpponentPosition() {
        this.output.println("OPP" + "," + opponent.position[0] + "," + opponent.position[1]);
        this.output.flush();
    }

    /*
     * Send any string message from server to client
     */
    public void sendMessage(String message) {
        this.output.println(message);
        this.output.flush();
    }

    /*
     * Update the score of a player and its opponent
     */
    public void setScore(int sc) {
        this.Score = sc;
        opponent.setOpponentScore();
    }

    /*
     * Print the opponent's score in client's terminal.
     */
    public void setOpponentScore() {
        this.output.println("OSCO" + "," + opponent.Score);
        this.output.flush();
    }

    /* 
     */
    public void run() {
        try {
            // The thread is only started after everyone connects.
            this.sendMessage("MESSAGE All players connected");

            // Send strting position of player and its opponent
            if (mark.equals("p1")) {
                this.sendPosition();
                this.sendOpponentPosition();
            }
            if (mark.equals("p2")) {
                this.sendPosition();
                this.sendOpponentPosition();
            }

            // Repeatedly get commands from the client and process them.
            while (true) {
                // if a socket is closed and connection is lost, an opponent must have
                // disconnected
                if (socket.isClosed()) {
                    System.out.println("MSG,Opponent Disconnected");
                    opponent.sendMessage("MSG,Opponent Disconnected");
                }
                // if command arrives from client, run this
                if (input.ready()) {
                    String command = input.readLine();
                    System.out.println(command);
                    // code to handle player quitting
                    if (command.equals("QUIT")) {
                        System.out.println("MSG,Opponent Disconnected");
                        opponent.sendMessage("MSG,Opponent Disconnected");
                    }
                    // code to handle new position of player after movement
                    if (command.startsWith("NPOS")) {
                        // System.out.println(mark);
                        setMyNewPosition(command);
                        opponent.sendOpponentPosition();
                    }
                    // code to handle score of player after finding treasure
                    if (command.startsWith("SCO")) {
                        setScore(Integer.parseInt(command.split(",")[1]));
                    }

                }
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Player died: " + e);
            System.out.println("MSG,Opponent Disconnected");
            opponent.sendMessage("MSG,Opponent Disconnected");
        } finally {
            try {
                System.out.println("MSG,Opponent Disconnected");
                opponent.sendMessage("MSG,Opponent Disconnected");
                // close socket for the connected player if the opponent disconected and stop
                // game
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
