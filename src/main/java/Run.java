
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.List;

public class Run {
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
                for (String indName: list) {
                    finalList = finalList + indName + " ";
                }
                JOptionPane.showMessageDialog(parent, finalList);
            }
        });

    }

}
