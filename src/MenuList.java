import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

enum MenuLoadState{
    Preload, MenuRowCountLoading, MenuListLoading, SetMenuListLoading, Finished
}

public class MenuList {

    public static int menuYSize = 0;

    ArrayList<Menu> menus = new ArrayList<>();
    ArrayList<ArrayList<Menu>> setMenus = new ArrayList<>();

    /**
     * load and gen menu from csv
     * @param menuTextPath csv file that contains menu info
     */
    MenuList(String menuTextPath){
        try(BufferedReader br = new BufferedReader(new FileReader(this.getClass().getResource(menuTextPath).getPath()))){
            String line;
            MenuLoadState state = MenuLoadState.Preload;

            while((line = br.readLine()) != null){

                if(line.equals("")){
                    continue;
                }

                if(line.equals("# menu row count #")){
                    state = MenuLoadState.MenuRowCountLoading;
                    continue;
                }else if (line.equals("# menu list #")){
                    state = MenuLoadState.MenuListLoading;
                    continue;
                }else if (line.equals("# set menu list #")){
                    state = MenuLoadState.SetMenuListLoading;
                    continue;
                }

                String element[] = line.split(",");
                switch (state){
                    case MenuRowCountLoading -> {
                        menuYSize = Integer.parseInt(element[0]);
                    }
                    case MenuListLoading -> {
                        addMenu(
                                element[0],
                                element[1],
                                new int[]{Integer.parseInt(element[2]), Integer.parseInt(element[3]), Integer.parseInt(element[4])}
                        );
                    }
                    case SetMenuListLoading -> {
                        ArrayList<Menu> set = new ArrayList<>();
                        for(int i = 0; i < element.length; i++){
                            Menu menu = getMenu(element[i]);
                            if (menu == null) {
                                throw new Exception("config file error : invalid menu in setmenu");
                            }
                            set.add(getMenu(element[i]));
                        }
                        setMenus.add(set);
                    }
                    case Preload -> {
                        throw new Exception("config file error : invalid format");
                    }
                    default -> {

                    }
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Menu getMenu(String menuName){
        Menu result = null;
        for(int i = 0; i < menus.size(); i++){
            if(Objects.equals(menus.get(i).name, menuName)){
                result = menus.get(i);
            }
        }
        return result;
    }

    /**
     * add order to menu
     * @param name order name
     */
    void addMenu(String name, String category, int[] price){
        Menu o = new Menu(name, category, price);
        menus.add(o);
    }

    @Override
    public String toString() {
        return "MenuList{" +
                "\n\tmenus=" + menus +
                "\n\tsetMenus=" + setMenus +
                "\n}";
    }
}
