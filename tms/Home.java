package tms;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Home extends JFrame {

    String username;

    public static void main(String[] args) {
        new Home("").setVisible(true);
    }

    public Home(String username) {
        super("Travel and Tourism Management System");
        this.username = username;
        setForeground(Color.CYAN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Use the actual screen size so UI scales correctly on different laptops
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int sw = screen.width;
        int sh = screen.height;

        // sensible minimums so layout doesn't collapse on very small screens
        int barHeight = Math.max(70, sh / 12); // header bar height
        int sideWidth = Math.max(230, sw / 7); // side dashboard width

        // set the frame size to the screen size (keeps consistent across displays)
        setBounds(0, 0, sw, sh);

        JPanel bar = new JPanel();
        bar.setLayout(null);
        bar.setBounds(0, 0, sw, barHeight);
        bar.setBackground(new Color(29, 69, 103));
        bar.setBorder(new TitledBorder(new LineBorder(Color.black, 2), "",
                TitledBorder.CENTER, TitledBorder.TOP, null, new Color(34, 139, 34)));
        this.add(bar);

        JLabel Name = new JLabel("Welcome  " + username);
        Name.setForeground(new Color(250, 243, 155));
        Name.setBounds(120, 2, 300, Math.max(30, barHeight - 10));
        Name.setFont(new Font("Tahoma", Font.PLAIN, Math.max(14, barHeight / 4)));
        bar.add(Name);

        JLabel l1 = new JLabel("Travel and Tourism Management System");
        l1.setForeground(new Color(250, 243, 155));
        l1.setFont(new Font("Tahoma", Font.PLAIN, Math.max(24, barHeight / 1)));
        l1.setBounds(sideWidth + 20, 2, sw - sideWidth - 40, barHeight - 10);
        bar.add(l1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/logo.jpg"));
        Image temp = i1.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(temp);
        JLabel logoL = new JLabel(i3);
        logoL.setBounds(20, 0, 80, 80);
        bar.add(logoL);

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons/home.jpg"));
        // scale background to available area (screen minus dashboard and header)
        int bgX = sideWidth;
        int bgY = barHeight;
        int bgW = sw - sideWidth;
        int bgH = sh - barHeight;
        Image temp2 = i2.getImage().getScaledInstance(bgW, bgH, Image.SCALE_SMOOTH);
        ImageIcon i4 = new ImageIcon(temp2);
        JLabel NewLabel = new JLabel(i4);
        NewLabel.setBounds(bgX, bgY, bgW, bgH);
        add(NewLabel);

        Dashboard dash = new Dashboard(username);
        dash.setBounds(0, barHeight, sideWidth, sh - barHeight);
        add(dash);
        dash.setVisible(true);

        // already sized to the screen, show the frame
        setVisible(true);
        getContentPane().setBackground(Color.WHITE);
    }

}
