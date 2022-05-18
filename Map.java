import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    public String map[][];
    private int maxX;
    private int maxY;
    private String mapLine[];
    private ArrayList players;


    public void initMap() {
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
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Map file not found");
            e.printStackTrace();
        }

        
    }
}
