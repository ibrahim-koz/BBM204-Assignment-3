import java.util.ArrayList;

public class BreadthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private ArrayList<ArrayList<Integer>> broadcastSteps;
    private Stack<Integer> backwardSteps;
    private final int s;
    private int[] returnValues = new int[2];

    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        broadcastSteps = new ArrayList<>();
        backwardSteps = new Stack<>();
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        Queue<LinkedList.Node> queue = new Queue<>();
        marked[s] = true;
        queue.enqueue(G.adj(s).getHead());
        int[] dis = new int[G.V()];
        dis[s] = 0;
        while (!queue.isEmpty()) {
            ArrayList <Integer> temp = new ArrayList<>();
            LinkedList.Node <GraphNode> v = queue.dequeue();
            backwardSteps.push(v.getValue().getIndex());
            temp.add(v.getValue().getIndex());
            LinkedList.Node <GraphNode> w = v.getNext();

            while (w != null) {
                if (!marked[w.getValue().getIndex()]) {
                    edgeTo[w.getValue().getIndex()] = v.getValue().getIndex();
                    marked[w.getValue().getIndex()] = true;
                    queue.enqueue(G.adj(w.getValue().getIndex()).getHead());
                    temp.add(w.getValue().getIndex());
                    dis[w.getValue().getIndex()] = dis[v.getValue().getIndex()] + 1;
                }
                w = w.getNext();
            }
            broadcastSteps.add(temp);
        }
        int maxDis = 0;
        int nodeIdx = 0;

        // get farthest node distance and its index
        for(int i = 0; i < G.V(); ++i) {
            if(dis[i] > maxDis) {
                maxDis = dis[i];
                nodeIdx = i;
            }
        }
        returnValues[0] = maxDis;
        returnValues[1] = nodeIdx;
    }


    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    public Stack<Integer> getBackwardSteps() {
        return backwardSteps;
    }

    public ArrayList<ArrayList<Integer>> getBroadcastSteps() {
        return broadcastSteps;
    }

    public int getEdgeTo(int i){
        return edgeTo[i];
    }

    public int[] getReturnValues(){
        return returnValues;
    }
}
