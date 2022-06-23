public class Order {
    String name;
    int price;
    Size size;
    String category;
    String iconFileName;

    Order(String name, int price, String category, String iconFileName){
        this.name = name;
        this.price = price;
        this.category = category;
        this.iconFileName = iconFileName;
        this.size = Size.Normal;
    }

    @Override
    public String toString() {
        return name + " ¥" + price + " (" + size + ")";
    }

    public Order clone(){
        Order newOrder = new Order(name, price, category, iconFileName);
        newOrder.size = size;
        return newOrder;
    }
}
