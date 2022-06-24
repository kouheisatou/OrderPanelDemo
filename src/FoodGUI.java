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

    int sum = 0;

    private ArrayList<Menu> menuList = new ArrayList<>();

    public FoodGUI() {

        // load manu from text
        MenuList menuList = new MenuList("resource/menu.conf");
        System.out.println(menuList);

        // expand menu button to menuTab
        for(int i = 0; i < menuList.menus.size(); i++){
            genOrderBtn(menuList.menus.get(i));
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
                    if(FoodGUI.this.menuList.size() == 0) return;
                    int clickedIndex = orderJList.getSelectedIndex();
                    if(clickedIndex < 0 || clickedIndex >= FoodGUI.this.menuList.size()) return;
                    Menu clickedMenu = FoodGUI.this.menuList.get(clickedIndex);

                    // right-click popup menu
                    JPopupMenu popupMenu = new JPopupMenu();

                    // set delete menu to popup menu
                    addMenuItemToPopupMenu(popupMenu, "delete", new UnaryOperator<ActionEvent>() {
                        @Override
                        public ActionEvent apply(ActionEvent actionEvent) {
                            FoodGUI.this.menuList.remove(clickedIndex);
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

    void order(Menu food){

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Would you like to order " + food.name + "?",
                "Order Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if(confirmation == 0){
            Menu menu = food.clone();

            // apply size
            String selectedSize = buttonGroup.getSelection().getActionCommand();
            if(selectedSize.equals("Large")){
                menu.size = Size.Large;
            }else if(selectedSize.equals("Normal")){
                menu.size = Size.Normal;
            }else if(selectedSize.equals("Small")){
                menu.size = Size.Small;
            }

            // apply count
            menu.count = Integer.parseInt(countLabel.getText());

            // add order to internal list
            menuList.add(menu);

            // update view
            updateOrderList();

            // update information
            informationLabel.setText("Order for " + food.name + " received");
            JOptionPane.showMessageDialog(null, "Order for " + food.name + "received.");
        }
    }

    void genOrderBtn(Menu menu){
        // editing tab panel
        JPanel panel;

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
            menuTab.addTab(menu.category, panel);
        }
        // if tab is exist, get same category tab
        else{
            panel = (JPanel) menuTab.getComponentAt(tabIndex);
        }

        // add panel to button
        try{
            ImageIcon icon = new ImageIcon("resource/icon/" + menu.iconFileName);
            JButton button = new JButton("<html><center>" + menu.name + "<br>Small: ¥" + menu.price[0] + "<br>Normal: ¥" + menu.price[1] + "<br>Large: ¥" + menu.price[2] + "</center></html>", icon);
            panel.add(button);

            // add action listener to button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    order(menu);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void updateOrderList(){

        // update list view
        String orders[] = new String[this.menuList.size()];
        for(int i = 0; i < this.menuList.size(); i++){
            orders[i] = this.menuList.get(i).toString();
        }
        orderJList.setListData(orders);

        // reset size and count
        buttonGroup.setSelected(normalRadioButton.getModel(), true);
        countLabel.setText("1");

        // update sum
        sum = 0;
        for(int i = 0; i < menuList.size(); i++){
            Menu menu = menuList.get(i);
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
        for(int i = 0; i < menuList.size(); i++){
            msg.append('\n');
            msg.append(menuList.get(i));
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

        if(resp == 1){
            JOptionPane.showMessageDialog(null, "Thank you");
            menuList.clear();
            updateOrderList();
        }
    }
}
