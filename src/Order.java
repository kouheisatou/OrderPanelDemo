public class Order {
    String name;
    int price;
    Size size;
    String category;
    String pathToIcon;

    Order(String name, int price, String category, String pathToIcon){
        this.name = name;
        this.price = price;
        this.category = category;
        this.size = Size.Normal;
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", category='" + category + '\'' +
                '}';
    }
}
