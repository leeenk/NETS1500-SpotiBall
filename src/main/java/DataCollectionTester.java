import java.util.LinkedList;
import java.util.List;

public class DataCollectionTester {

    public static void main(String[] args) {
        //DataCollection dataCollection = new DataCollection("Pitbull", 600);
        //DataCollection dataCollection = new DataCollection("Pitbull", 500);
        // upload the graph data to files, do ONCE because it takes long
        //Graph graph = dataCollection.buildGraph();

        // in BFS, download the graph data from the file

        BFS bfs = new BFS();
        bfs.populateGraph();
        List<String> list = bfs.run("Pitbull", "Lunay");
        for (String s : list) {
            System.out.println(s);
        }
    }
}