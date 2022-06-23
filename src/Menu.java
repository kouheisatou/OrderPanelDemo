import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Menu {
    ArrayList<Order> orders = new ArrayList<Order>();

    /**
     * load and gen menu from csv
     * @param menuTextPath csv file that contains menu info
     */
    Menu(String menuTextPath){
        try(BufferedReader br = new BufferedReader(new FileReader(menuTextPath))){
            String line;
            while((line = br.readLine()) != null){
                String element[] = line.split(",");
                addMenu(element[0], Integer.parseInt(element[1]), element[2], element[3]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * add order to menu
     * @param name order name
     * @param pathToIcon icon image file path
     */
    void addMenu(String name, int price, String category, String pathToIcon){
        Order o = new Order(name, price, category, pathToIcon);
        orders.add(o);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "orders=" + orders +
                "}";
    }
}
