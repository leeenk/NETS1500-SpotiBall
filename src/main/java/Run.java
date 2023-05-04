
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Run {
    public static void main(final String[] args) {
        final JFrame parent = new JFrame("SpotiBall");
        JButton button = new JButton();

//        File file = new File("files/spotify.png");
//        BufferedImage bufferedImage = null;
//        try {
//            bufferedImage = ImageIO.read(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ImageIcon imageIcon = new ImageIcon("files/spo.png");
        Image im = imageIcon.getImage().getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(im);

        parent.setLayout(new FlowLayout());
        parent.getContentPane().setBackground(Color.decode("#95F5B7"));

        JLabel label = new JLabel();
        label.setSize(10, 10);
        label.setIcon(imageIcon);
        label.setOpaque(false);

        parent.add(label);
        parent.setSize(300, 300);
        parent.setLocation(200, 200);
        String text = "Welcome to Spotiball! Click to Begin.";
        button.setText(text);
        parent.add(button);
        parent.pack();
        parent.setVisible(true);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String name = JOptionPane.showInputDialog(parent,
                        "Enter First Artist: ", null);
                String nameTwo = JOptionPane.showInputDialog(parent,
                        "Enter Second Artist: ", null);
                BFS bfs = new BFS();
                bfs.populateGraph();
                List<String> list = bfs.run(name, nameTwo);
                String finalList = "";
                for (int i = 0; i < list.size(); i++) {
                    finalList = list.get(0);
                }
                for (int i = 1; i < list.size(); i++) {
                    finalList = finalList + " -> " + list.get(i);
                }
                JOptionPane.showMessageDialog(parent, finalList);
            }
        });


    }



//
//    public void paint(Graphics g) {
//
//        Toolkit t = Toolkit.getDefaultToolkit();
//        Image i = t.getImage("files/spotify.png");
//        g.drawImage(i, 120,100,this);
//
//    }

}
