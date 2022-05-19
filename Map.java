import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

public class Map {
    public String map[][];
    private int maxX;
    private int maxY;
    private String mapLine[];
    private ArrayList players;
    Screen screen;
    Player p1;
    
    public Map() {
        this.screen = TerminalFacade.createScreen();
        this.screen.startScreen();
        System.out.println("Created Screen");
        p1 = new Player();
    }

    public void loadMap() {
        this.maxX = 50;
        this.maxY = 20;
        this.map = new String[50][20];
        this.mapLine = new String[20];
        this.players = new ArrayList<>();

        File file = new File("map.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                for (int i = 0; i < 20; i++) {
                    mapLine[i] = sc.nextLine();
                }
            }

            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 20; j++) {
                    map[i][j] = String.valueOf(mapLine[j].charAt(i));
                }
            }
            System.out.println("Done Loading Map from File");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Map file not found");
            e.printStackTrace();
        }

    }

    public void setString(String set, int x, int y) {
        map[x][y] = set;
    }

    public void printMap() {
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                switch (map[j][i]) {
                    case "@":
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.YELLOW, Terminal.Color.BLACK);
                        this.screen.putString(j + j + 1, i, map[j][i], Terminal.Color.YELLOW, Terminal.Color.BLACK);
                        break;

                    case "$":
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.GREEN, Terminal.Color.BLACK);
                        this.screen.putString(j + j + 1, i, map[j][i], Terminal.Color.GREEN, Terminal.Color.BLACK);
                        break;

                    case "G":
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.GREEN, Terminal.Color.BLACK);
                        break;

                    default:
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.WHITE, Terminal.Color.BLACK);
                        this.screen.putString(j + j + 1, i, map[j][i], Terminal.Color.WHITE, Terminal.Color.BLACK);
                        break;
                }
            }
        }
        this.screen.refresh();
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    // private Player p1(int index) {
    // return players.get(index);
    // }

    public void movePlayerRight() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosX(pX + 1);
        map[pX][pY] = " ";
        map[pX + 1][pY] = "@";
    }

    public void movePlayerLeft() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosX(pX - 1);
        map[pX][pY] = " ";
        map[pX - 1][pY] = "@";
    }

    public void movePlayerDown() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosy(pY + 1);
        map[pX][pY] = " ";
        map[pX][pY + 1] = "@";
    }

    public void movePlayerUp() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosy(pY - 1);
        map[pX][pY] = " ";
        map[pX][pY - 1] = "@";
    }

    public int getPlayerX() {
        return p1.getX();
    }

    public int getPlayerY() {
        return p1.getY();
    }

    // String held in the map array at a given coordinate pair
    public String getStringAt(int x, int y) {
        return map[x][y];
    }
}
