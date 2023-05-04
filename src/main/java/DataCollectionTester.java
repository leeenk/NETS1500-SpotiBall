public class DataCollectionTester {

    public static void main(String[] args) {
        DataCollection dataCollection = new DataCollection("Snoop Dogg", 500);
        Graph graph = dataCollection.buildGraph();
    }
}