import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
        button.setText("Welcome to Spotiball! Click to Begin.");
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
            }
            // traverse parent array and output path associated with the two artists
        });
    }

}
