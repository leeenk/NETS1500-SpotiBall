public class DataCollectionTester {

    public static void main(String[] args) {
        DataCollection dataCollection = new DataCollection("DaBaby", 10);
        // upload the graph data to files, do ONCE because it takes long
        Graph graph = dataCollection.buildGraph();

        // in BFS, download the graph data from the file

        // BFS bfs = new BFS();
        // bfs.populateGraph();
    }

}