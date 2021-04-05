import java.util.Iterator;

public class Graph {
    private final int V;
    private int E;
    private LinkedList<GraphNode>[] adjacencyList;

    public Graph (int[] capacities) {
        this.V = capacities.length;
        E = 0;
        adjacencyList = (LinkedList<GraphNode>[]) new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjacencyList[i] = new LinkedList<>();
            adjacencyList[i].append(new GraphNode(capacities[i], i));
        }
    }

    public void addEdge(int v, int w) {
        adjacencyList[v].append(adjacencyList[w].getHead().getValue());
        adjacencyList[w].append(adjacencyList[v].getHead().getValue());
        E++;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public LinkedList<GraphNode> adj(int v) {
        return adjacencyList[v];
    }
}

 // New a unit for holding data in generic manner.
class GraphNode {
    private int index;
    private int capacity;
    public GraphNode(int capacity, int index){
        this.capacity = capacity;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getCapacity() {
        return capacity;
    }
}
