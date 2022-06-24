import javax.swing.*;

public class Menu {
    String name;
    int[] price;
    Size size;
    String category;
    String iconFileName;
    ImageIcon icon;
    int count;

    Menu(String name, String category, String iconFileName, int[] price) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.iconFileName = iconFileName;
        this.size = Size.Normal;
        this.count = 0;
        this.icon = new ImageIcon("resource/icon/" + iconFileName);
    }

    @Override
    public String toString() {
        return name + " (" + size + ") ¥" + getCurrentPrice() + " x " + count;
    }

    public int getCurrentPrice(){
        int p = 0;
        if(size == Size.Large){
            p = price[2];
        }else if(size == Size.Normal){
            p = price[1];
        }else if(size == Size.Small){
            p = price[0];
        }
        return p;
    }

    public Menu clone(){
        int[] copiedPrice = {price[0], price[1], price[2]};
        Menu copiedMenu = new Menu(name, category, iconFileName, copiedPrice);
        copiedMenu.size = size;
        return copiedMenu;
    }
}
