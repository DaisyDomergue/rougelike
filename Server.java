import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 9000;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started!");
            waitForPlayers(server);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void waitForPlayers(ServerSocket server){
        try {
            PlayerThread p1 = new PlayerThread(server.accept(),"p1");
            PlayerThread p2 = new PlayerThread(server.accept(),"p2");
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
class PlayerThread extends Thread {
    String mark;
    PlayerThread opponent;
    int position[] = new int[2];
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    // thread handler to initialize stream fields
    public PlayerThread(Socket socket,String mark) {
        this.socket = socket;
        this.mark = mark;
        try {
            input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + mark);
            output.println("MESSAGE Waiting for opponent to connect");
        } catch (IOException e) {
            System.out.println("Player died: " + e);
        }
    }
    public void setOpponent(PlayerThread opponent) {
        this.opponent = opponent;
    }
    public void setPosition(int x, int y){
        this.position[0] = x;
        this.position[1] = y;
    }
    public void sendPosition(){
        this.output.println("POS"+","+position[0]+","+position[1]);
        this.output.flush();
    }
    public void sendOpponentPosition(){
        this.output.println("OPP"+","+opponent.position[0]+","+opponent.position[1]);
        this.output.flush();
    }
    public void sendMessage(String message){
        this.output.println(message);
        this.output.flush();
    }
    public void run() {
        try {
            // The thread is only started after everyone connects.
            this.sendMessage("MESSAGE All players connected");

            if(mark.equals("p1")){
                this.sendPosition();
                this.sendOpponentPosition();
            }
            if(mark.equals("p2")){
                this.sendPosition();
                this.sendOpponentPosition();
            }
            
            // Repeatedly get commands from the client and process them.
            while (true) {
                if(input.ready()){
                    String command = input.readLine();
                    System.out.println(command);
                }
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Player died: " + e);
        } finally {
            try {socket.close();} catch (IOException e) {}
        }
    }
}