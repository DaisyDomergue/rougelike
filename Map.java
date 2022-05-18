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

    public Map() {
        this.screen = TerminalFacade.createScreen();
        this.screen.startScreen();
        System.out.println("Created Screen");
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
}
