import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodGUI {
    private JPanel root;
    private JButton tempuraButton;
    private JButton ramenButton;
    private JButton udonButton;
    private JTextPane textPane1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JList list1;
    private JButton button4;
    private JTabbedPane tabbedPane1;

    public FoodGUI() {
        tempuraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                order("Tempura");
            }
        });
        ramenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                order("Ramen");
            }
        });
        udonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                order("Udon");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FoodGUI");
        frame.setContentPane(new FoodGUI().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Menu menu = new Menu("menu.txt");
        System.out.println(menu.toString());
    }

    void order(String food){

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Would you like to order " + food + "?",
                "Order Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if(confirmation == 0){
            textPane1.setText("Order for " + food + " received");
            JOptionPane.showMessageDialog(null, "Order for " + food + "received.");

        }
    }
}
