import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Menu {

    public static int menuYSize = 0;

    ArrayList<Order> orders = new ArrayList<Order>();

    /**
     * load and gen menu from csv
     * @param menuTextPath csv file that contains menu info
     */
    Menu(String menuTextPath){
        int count = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(menuTextPath))){
            String line;
            while((line = br.readLine()) != null){

                String element[] = line.split(",");
                if(count == 0){
                    menuYSize = Integer.parseInt(element[0]);
                }else{
                    addMenu(element[0], Integer.parseInt(element[1]), element[2], element[3]);
                }
                count ++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * add order to menu
     * @param name order name
     * @param iconFileName icon image file path
     */
    void addMenu(String name, int price, String category, String iconFileName){
        Order o = new Order(name, price, category, iconFileName);
        orders.add(o);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "orders=" + orders +
                "}";
    }
}
