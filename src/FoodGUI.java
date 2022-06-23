import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FoodGUI {
    private JPanel root;
    private JButton checkOutButton;
    private JLabel totalLabel;
    private JTabbedPane menuTab;
    private JLabel informationLabel;
    private JList orderJList;

    private ArrayList<Order> orderList = new ArrayList<>();

    public FoodGUI() {

        // load manu from text
        Menu menu = new Menu("resource/menu.csv");
        System.out.println(menu);

        // expand menu button to menuTab
        for(int i = 0; i < menu.orders.size(); i++){
            genOrderBtn(menu.orders.get(i));
        }

        // set action listeners
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // on right-clicked in list
        orderJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {

                    int clickedIndex = orderJList.getSelectedIndex();
                    Order clickedOrder = orderList.get(clickedIndex);

                    // set delete menu to popup menu
                    JPopupMenu popupMenu = new JPopupMenu();
                    addMenuItemToPopupMenu(popupMenu, "delete", new UnaryOperator<ActionEvent>() {
                        @Override
                        public ActionEvent apply(ActionEvent actionEvent) {
                            orderList.remove(clickedIndex);
                            updateOrderList();
                            return null;
                        }
                    });

                    // set change-size menu to popup menu
                    // when change-size menu called, "small", "normal", "large" popup menu displayed
                    addMenuItemToPopupMenu(popupMenu, "change size", new UnaryOperator<ActionEvent>() {
                        @Override
                        public ActionEvent apply(ActionEvent actionEvent) {
                            JPopupMenu sizeChangePopupMenu = new JPopupMenu();

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "small", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedOrder.size = Size.Small;
                                    updateOrderList();
                                    return null;
                                }
                            });

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "normal", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedOrder.size = Size.Normal;
                                    updateOrderList();
                                    return null;
                                }
                            });

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "Large", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedOrder.size = Size.Large;
                                    updateOrderList();
                                    return null;
                                }
                            });

                            sizeChangePopupMenu.show(orderJList, e.getX(), e.getY());
                            return null;
                        }
                    });

                    popupMenu.show(orderJList, e.getX(), e.getY());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FoodGUI");
        frame.setContentPane(new FoodGUI().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    void order(Order food){

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Would you like to order " + food.name + "?",
                "Order Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if(confirmation == 0){
            // add order to internal list
            orderList.add(food.clone());
            updateOrderList();

            // update information
            informationLabel.setText("Order for " + food.name + " received");
            JOptionPane.showMessageDialog(null, "Order for " + food.name + "received.");

        }
    }

    void genOrderBtn(Order order){
        // editing tab panel
        JPanel panel;

        // judge category tab already exists
        int tabIndex = -1;
        for(int i = 0; i < menuTab.getComponentCount(); i++) {
            if(Objects.equals(order.category, menuTab.getTitleAt(i))) {
                tabIndex = i;
            }
        }

        // if category tab is not exist, gen new tab
        if(tabIndex == -1){
            panel = new JPanel();
            panel.setLayout(new GridLayout(Menu.menuYSize, Menu.menuXSize));
            menuTab.addTab(order.category, panel);
        }
        // if tab is exist, get same category tab
        else{
            panel = (JPanel) menuTab.getComponentAt(tabIndex);
        }

        // add panel to button
        try{
            ImageIcon icon = new ImageIcon("resource/icon/" + order.iconFileName);
            JButton button = new JButton(order.name + "(¥" + order.price + ")", icon);
            panel.add(button);

            // add action listener to button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    order(order);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void updateOrderList(){

        String orders[] = new String[this.orderList.size()];

        for(int i = 0; i < this.orderList.size(); i++){
            orders[i] = this.orderList.get(i).toString();
        }
        orderJList.setListData(orders);
    }

    void addMenuItemToPopupMenu(JPopupMenu popupMenu, String menuItemTitle, UnaryOperator<ActionEvent> onClick){
        JMenuItem menuItem = new JMenuItem(menuItemTitle);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onClick.apply(actionEvent);
            }
        });
        popupMenu.add(menuItem);
    }
}
