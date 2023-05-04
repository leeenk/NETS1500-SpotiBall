
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Run {

    // presents UI to user - store artist1, artist2

    // create instance of Data Collection, initialized to artist 1 (initially pitbull) and desired limiting size
    // run buildGraph method to output created graph

    // Run BFS with graph, artist1 as source, artist2 as target

    // present list o user (correctly display that no connection exists if returned list is empty)
    public static void main(final String[] args) {
        final JFrame parent = new JFrame("SpotiBall");
        JButton button = new JButton();

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
            // traverse parent array and output path associated with the two artists
        });

    }

}
