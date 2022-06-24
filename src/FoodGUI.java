import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
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
    private JScrollPane orderListScrollPanel;
    ButtonGroup buttonGroup;

    int sum = 0;
    private ArrayList<Menu> orderList = new ArrayList<>();
    private MenuList menuList = null;

    public FoodGUI() {

        // load manu from text
        menuList = new MenuList("resource/menu.conf");
        System.out.println(menuList);

        // expand menu button to menuTab
        for(int i = 0; i < menuList.menus.size(); i++){
            genOrderBtn(menuList.menus.get(i));
        }

        // if set menu exists, add set menu tab
        if(!menuList.setMenus.isEmpty()){
            addSetMenuTab();
        }

        // set action listeners
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });
        countDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newCount = Integer.parseInt(countLabel.getText())-1;
                if(newCount <= 0){
                    return;
                }
                countLabel.setText(newCount + "");
            }
        });
        countUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newCount = Integer.parseInt(countLabel.getText())+1;
                if(newCount >= 10){
                    return;
                }
                countLabel.setText(newCount + "");
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
                    if(FoodGUI.this.orderList.size() == 0) return;
                    int clickedIndex = orderJList.getSelectedIndex();
                    if(clickedIndex < 0 || clickedIndex >= FoodGUI.this.orderList.size()) return;
                    Menu clickedMenu = FoodGUI.this.orderList.get(clickedIndex);

                    // right-click popup menu
                    JPopupMenu popupMenu = new JPopupMenu();

                    // set delete menu to popup menu
                    addMenuItemToPopupMenu(popupMenu, "delete", new UnaryOperator<ActionEvent>() {
                        @Override
                        public ActionEvent apply(ActionEvent actionEvent) {
                            FoodGUI.this.orderList.remove(clickedIndex);
                            updateOrderList();
                            return null;
                        }
                    });

                    // set updateCount menu to popup menu
                    addMenuItemToPopupMenu(popupMenu, "update order count", new UnaryOperator<ActionEvent>() {
                        @Override
                        public ActionEvent apply(ActionEvent actionEvent) {

                            // set count menu to countChangePopup menu
                            JPopupMenu countChangePopupMenu = new JPopupMenu();
                            for(int i = 1; i < 10; i++){
                                int temp = i;
                                addMenuItemToPopupMenu(countChangePopupMenu, i + "", new UnaryOperator<ActionEvent>() {
                                    @Override
                                    public ActionEvent apply(ActionEvent actionEvent) {
                                        clickedMenu.count = temp;
                                        updateOrderList();
                                        return null;
                                    }
                                });
                            }

                            countChangePopupMenu.show(orderJList, e.getX(), e.getY());
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
                                    clickedMenu.size = Size.Large;
                                    updateOrderList();
                                    return null;
                                }
                            });

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "normal", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedMenu.size = Size.Normal;
                                    updateOrderList();
                                    return null;
                                }
                            });

                            addMenuItemToPopupMenu(sizeChangePopupMenu, "small", new UnaryOperator<ActionEvent>() {
                                @Override
                                public ActionEvent apply(ActionEvent actionEvent) {
                                    clickedMenu.size = Size.Small;
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

    void setOrder(ArrayList<Menu> foods){

        int total = 0;
        StringBuilder orderList = new StringBuilder();
        for(int i = 0; i < foods.size(); i++){
            Menu order = foods.get(i).clone();
            order.count = getSelectedCount();
            order.size = getSelectedSize();
            orderList.append(order);
            total += foods.get(i).getCurrentPrice();
            if(i != foods.size() -1){
                orderList.append("\n");
            }
        }

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                orderList + "\nTOTAL ¥" + total*getSelectedCount(),
                "Order Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
        if(confirmation == 1){
            return;
        }

        for(int i = 0; i < foods.size(); i++){
            order(foods.get(i));
        }

        // update information
        informationLabel.setText("Set order received");
        JOptionPane.showMessageDialog(
                null,
                "Order received.\n" + orderList,
                "Order Confirmation",
                JOptionPane.INFORMATION_MESSAGE
        );

        // reset size and count
        buttonGroup.setSelected(normalRadioButton.getModel(), true);
        countLabel.setText("1");
    }

    void singleOrder(Menu food){

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Would you like to order " + food.name + "?",
                "Order Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                food.icon
        );
        if(confirmation == 1){
            return;
        }

        order(food);

        // update information
        informationLabel.setText("Order for " + food.name + " received");
        JOptionPane.showMessageDialog(
                null,
                "Order for " + food.name + "received.", "Order Confirmation",
                JOptionPane.INFORMATION_MESSAGE, food.icon
        );

        // reset size and count
        buttonGroup.setSelected(normalRadioButton.getModel(), true);
        countLabel.setText("1");
    }

    void order(Menu food){

        Menu menu = food.clone();

        // apply count and size
        menu.count = getSelectedCount();
        menu.size = getSelectedSize();

        // add order to internal list
        orderList.add(menu);

        // update view
        updateOrderList();
    }

    void genOrderBtn(Menu menu){
        // editing tab panel
        JPanel panel;
        JScrollPane scrollPane;

        // judge category tab already exists
        int tabIndex = -1;
        for(int i = 0; i < menuTab.getComponentCount(); i++) {
            if(Objects.equals(menu.category, menuTab.getTitleAt(i))) {
                tabIndex = i;
            }
        }

        // if category tab is not exist, gen new tab
        if(tabIndex == -1){
            panel = new JPanel();
            panel.setLayout(new GridLayout(MenuList.menuYSize, 1));
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(panel);
            menuTab.addTab(menu.category, scrollPane);
        }
        // if tab is exist, get same category tab
        else{
            scrollPane = (JScrollPane) menuTab.getComponentAt(tabIndex);
            panel = (JPanel) scrollPane.getViewport().getView();
        }

        // add panel to button
        try{
            JButton button = new JButton("<html><center>" + menu.name + "<br>Small: ¥" + menu.price[0] + "<br>Normal: ¥" + menu.price[1] + "<br>Large: ¥" + menu.price[2] + "</center></html>", menu.icon);
            button.setMaximumSize(new Dimension());
            panel.add(button);

            // add action listener to button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    singleOrder(menu);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void updateOrderList(){

        // update list view
        String orders[] = new String[this.orderList.size()];
        for(int i = 0; i < this.orderList.size(); i++){
            orders[i] = this.orderList.get(i).toString();
        }
        orderJList.setListData(orders);

        // update sum
        sum = 0;
        for(int i = 0; i < orderList.size(); i++){
            Menu menu = orderList.get(i);
            sum += (menu.getCurrentPrice() * menu.count);
        }
        totalLabel.setText(sum + "");
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

    void checkout(){
        StringBuilder msg = new StringBuilder("Would you like to checkout?");
        for(int i = 0; i < orderList.size(); i++){
            msg.append('\n');
            msg.append(orderList.get(i));
        }
        msg.append('\n');
        msg.append("Total : ");
        msg.append(sum);

        int resp = JOptionPane.showConfirmDialog(
                null,
                msg.toString(),
                "Checkout Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if(resp == 0){
            JOptionPane.showMessageDialog(null, "Thank you");
            orderList.clear();
            updateOrderList();
        }
    }

    void addSetMenuTab(){
        JPanel tabRoot = new JPanel();
        BoxLayout g = new BoxLayout(tabRoot, BoxLayout.Y_AXIS);
        tabRoot.setLayout(g);

        for(int i = 0; i < menuList.setMenus.size(); i++){

            JPanel row = new JPanel();
            BoxLayout b = new BoxLayout(row, BoxLayout.X_AXIS);
            row.setLayout(b);
            row.setBackground(Color.white);
            row.setBorder(new LineBorder(Color.GRAY, 2, true));

            ArrayList<Menu> setMenuList = menuList.setMenus.get(i);

            for(int j = 0; j < setMenuList.size(); j++){
                Menu menu = setMenuList.get(j);
                JLabel label = new JLabel(menu.icon);
                label.setText("<html><center>" + menu.name + "<br>Small: ¥" + menu.price[0] + "<br>Normal: ¥" + menu.price[1] + "<br>Large: ¥" + menu.price[2] + "</center></html>");
                row.add(label);
            }

            row.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {
                    row.setBorder(new LineBorder(Color.white, 2, true));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    row.setBorder(new LineBorder(Color.red, 2, true));
                    setOrder(setMenuList);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    row.setBorder(new LineBorder(Color.orange, 2, true));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    row.setBorder(new LineBorder(Color.lightGray, 2, true));
                }
            });

            tabRoot.add(row);
        }

        JScrollPane scrollPane = new JScrollPane(tabRoot);
        menuTab.addTab("SetMenu", scrollPane);

    }

    private Size getSelectedSize(){
        Size result = null;
        String selectedSize = buttonGroup.getSelection().getActionCommand();
        if(selectedSize.equals("Large")){
            result = Size.Large;
        }else if(selectedSize.equals("Normal")){
            result = Size.Normal;
        }else if(selectedSize.equals("Small")){
            result = Size.Small;
        }
        return result;
    }

    private int getSelectedCount() {
        return Integer.parseInt(countLabel.getText());
    }
}
