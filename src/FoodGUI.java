import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class FoodGUI {
    private JPanel root;
    private JButton checkOutButton;
    private JLabel totalLabel;
    private JTabbedPane menuTab;
    private JLabel informationLabel;
    private JList orderJList;
    private JRadioButton largeRadioButton;
    private JRadioButton normalRadioButton;
    private JRadioButton smallRadioButton;
    private JButton countUpButton;
    private JButton countDownButton;
    private JLabel countLabel;
    ButtonGroup buttonGroup;

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
        countDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countLabel.setText((Integer.parseInt(countLabel.getText())-1) + "");
            }
        });
        countUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countLabel.setText((Integer.parseInt(countLabel.getText())+1) + "");
            }
        });

        // init radio button
        buttonGroup = new ButtonGroup();
        largeRadioButton.setActionCommand("Large");
        normalRadioButton.setActionCommand("Normal");
        smallRadioButton.setActionCommand("Small");
        buttonGroup.add(largeRadioButton);
        buttonGroup.add(normalRadioButton);
        buttonGroup.add(smallRadioButton);


        // on right-clicked in list
        orderJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {

                    // null check
                    if(orderList.size() == 0) return;
                    int clickedIndex = orderJList.getSelectedIndex();
                    if(clickedIndex < 0 || clickedIndex >= orderList.size()) return;
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

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "Large", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedOrder.size = Size.Large;
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

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "small", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedOrder.size = Size.Small;
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
            Order order = food.clone();

            // apply size
            String selectedSize = buttonGroup.getSelection().getActionCommand();
            if(selectedSize.equals("Large")){
                order.size = Size.Large;
            }else if(selectedSize.equals("Normal")){
                order.size = Size.Normal;
            }else if(selectedSize.equals("Small")){
                order.size = Size.Small;
            }

            // apply count
            order.count = Integer.parseInt(countLabel.getText());

            // add order to internal list
            orderList.add(order);

            // update view
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
            panel.setLayout(new GridLayout(Menu.menuYSize, 1));
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
