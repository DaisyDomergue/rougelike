import java.util.Arrays;

public class Game {
    public static void main(String[] args){
        Map map = new Map();
        map.loadMap();
        map.printMap();
        // System.out.println(Arrays.deepToString(map.map));
    }
}
