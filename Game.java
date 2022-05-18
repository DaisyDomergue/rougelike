import java.util.Arrays;

public class Game {
    public static void main(String[] args){
        Map map = new Map();
        map.initMap();
        System.out.println(Arrays.deepToString(map.map));
    }
}
