import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FoodGUI {
    private JPanel root;
    private JList orderJList;
    private JButton checkOutButton;
    private JLabel totalLabel;
    private JTabbedPane menuTab;

    private ArrayList<Order> orderList = new ArrayList<>();
    private ArrayList<String> tabList = new ArrayList<>();
    int menuXSize = 2;
    int menuYSize = 2;

    public FoodGUI() {

        // load manu from text
        Menu menu = new Menu("resource/menu.csv");
        System.out.println(menu);

        // expand menuPanel button to root
        for(int i = 0; i < menu.orders.size(); i++){
            genOrderBtn(menu.orders.get(i));
        }

        // set action listeners
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

    void order(String food){

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Would you like to order " + food + "?",
                "Order Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if(confirmation == 0){
            totalLabel.setText("Order for " + food + " received");
            JOptionPane.showMessageDialog(null, "Order for " + food + "received.");

        }
    }

    void genOrderBtn(Order order){
        // editing tab panel
        JTabbedPane tab;

        // if category tab is not exist, gen new tab
        if(!tabList.contains(order.category)){
            tabList.add(order.category);

            tab = new JTabbedPane();
            tab.setLayout(new GridLayout(menuXSize, menuYSize));

            menuTab.addTab(order.category, tab);

        }
        // if tab is exist, get same category tab
        else{
            tab = (JTabbedPane) menuTab.getComponent(tabList.indexOf(order.category));
        }

        try{
            ImageIcon icon = new ImageIcon("resource/icon/" + order.iconFileName);
            JButton button = new JButton(order.name + "(¥" + order.price + ")", icon);
            int size = tab.getComponentCount();
            System.out.println(size);
            tab.add(button);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
