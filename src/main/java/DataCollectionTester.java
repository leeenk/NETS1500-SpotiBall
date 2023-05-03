import java.util.LinkedList;

public class DataCollectionTester {

    public static void main(String[] args) {
        //DataCollection dataCollection = new DataCollection("Pitbull", 500);
        // upload the graph data to files, do ONCE because it takes long
        //Graph graph = dataCollection.buildGraph();

        // in BFS, download the graph data from the file

        BFS bfs = new BFS();
        bfs.populateGraph();
        LinkedList<String> list = bfs.runBFS("Pitbull", "Ive Queen");
        for (String s : list) {
            System.out.println(s);
        }
    }

}