import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        Map<Character, Integer> table = new Hashtable<>();

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));

        String[] pairs = reader.readLine().split(" ");
        System.out.println("capacities: " + pairs.length);

        char[] alphabet = new char[pairs.length / 2];
        int[] capacities = new int[pairs.length / 2];

        for (int i = 0; i < pairs.length / 2; i++) {
            alphabet[i] = pairs[2 * i].charAt(0);
            capacities[i] = Integer.parseInt(pairs[2 * i + 1]);
        }

        for (int i = 0; i < capacities.length; i++)
            table.put(alphabet[i], i);

        Graph graph = new Graph(capacities);

        int startNode = table.get(reader.readLine().charAt(0));

        String row;
        while ((row = reader.readLine()) != null) {
            String[] tokens = row.split(" ");
            graph.addEdge(table.get(tokens[0].charAt(0)), table.get(tokens[1].charAt(0)));
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));

        writer.write("Graph structure:\n");

        for (char c : alphabet) {
            LinkedList.Node<GraphNode> currentNode = graph.adj(table.get(c)).getHead();
            writer.write(String.format("%c(%d)-->", c, currentNode.getValue().getCapacity()));

            if (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
                writer.write(alphabet[currentNode.getValue().getIndex()] + "");
            }

            while (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
                writer.write(" " + alphabet[currentNode.getValue().getIndex()]);
            }
            writer.write("\n");
        }

        writer.write("Broadcast steps:\n");

        BreadthFirstPaths bfp = new BreadthFirstPaths(graph, startNode);


        for (ArrayList<Integer> i : bfp.getBroadcastSteps()) {
            if (i.size() > 1) {
                writer.write(alphabet[i.get(0)] + "-->");
                writer.write(alphabet[i.get(1)]);
                for (int j = 2; j < i.size(); j++) {
                    writer.write(" " + alphabet[i.get(j)]);
                }
                writer.write("\n");
            }
        }

        writer.write("Message passing:\n");

        ArrayList<GraphNode>[] al = new ArrayList[graph.V()];

        // Backward propagation is undergo by using edgeTo mechanism through backwards.
        for (int i = 0; i < graph.V(); i++) {
            al[i] = new ArrayList<>();
        }

        while (!bfp.getBackwardSteps().isEmpty()) {
            int index = bfp.getBackwardSteps().pop();
            char letter = alphabet[index];
            if (al[bfp.getEdgeTo(index)].size() == 0) {
                if (al[index].size() == 0)
                    al[bfp.getEdgeTo(index)].add(graph.adj(index).getHead().getValue());
                else {
                    if (al[index].get(0).getCapacity() < graph.adj(index).getHead().getValue().getCapacity()) {
                        al[bfp.getEdgeTo(index)].add(graph.adj(index).getHead().getValue());
                    } else {
                        for (int i = 0; i < al[index].size(); i++) {
                            al[bfp.getEdgeTo(index)].add(al[index].get(i));
                        }
                    }

                }
                writer.write(String.format("%c--->[%c,%d]--->%c\n", alphabet[index], alphabet[al[bfp.getEdgeTo(index)].get(0).getIndex()],
                        al[bfp.getEdgeTo(index)].get(0).getCapacity(), alphabet[bfp.getEdgeTo(index)]));
            } else if (index != startNode) {
                int x;
                if (al[index].size() == 0) {
                    x = graph.adj(index).getHead().getValue().getCapacity();
                    int y = al[bfp.getEdgeTo(index)].get(0).getCapacity();
                    if (al[bfp.getEdgeTo(index)].get(0).getCapacity() < graph.adj(index).getHead().getValue().getCapacity()) {
                        for (int i = 0; i < al[bfp.getEdgeTo(index)].size(); i++) {
                            al[bfp.getEdgeTo(index)].remove(0);
                        }
                        al[bfp.getEdgeTo(index)].add(graph.adj(index).getHead().getValue());
                    } else if (al[bfp.getEdgeTo(index)].get(0).getCapacity() == graph.adj(index).getHead().getValue().getCapacity()) {
                        al[bfp.getEdgeTo(index)].add(graph.adj(index).getHead().getValue());
                    }
                    writer.write(String.format("%c--->[%c,%d]--->%c\n", alphabet[index], alphabet[graph.adj(index).getHead().getValue().getIndex()],
                            graph.adj(index).getHead().getValue().getCapacity(), alphabet[bfp.getEdgeTo(index)]));
                } else {
                    x = al[index].get(0).getCapacity();
                    int y = al[bfp.getEdgeTo(index)].get(0).getCapacity();
                    if (al[bfp.getEdgeTo(index)].get(0).getCapacity() < al[index].get(0).getCapacity()) {
                        ;
                        for (int i = 0; i < al[bfp.getEdgeTo(index)].size(); i++) {
                            al[bfp.getEdgeTo(index)].remove(0);
                        }
                        for (int i = 0; i < al[index].size(); i++) {
                            al[bfp.getEdgeTo(index)].add(al[index].get(i));
                        }
                    } else if (al[bfp.getEdgeTo(index)].get(0).getCapacity() == al[index].get(0).getCapacity()) {
                        for (int i = 0; i < al[index].size(); i++) {
                            al[bfp.getEdgeTo(index)].add(al[index].get(i));
                        }
                    }
                    writer.write(String.format("%c--->", alphabet[index]));
                    for (int i = 0; i < al[graph.adj(index).getHead().getValue().getIndex()].size(); i++) {
                        writer.write(String.format("[%c,%d]", alphabet[al[graph.adj(index).getHead().getValue().getIndex()].get(i).getIndex()],
                                al[graph.adj(index).getHead().getValue().getIndex()].get(i).getCapacity()));
                    }
                    writer.write(String.format("--->%c\n", alphabet[bfp.getEdgeTo(index)]));
                }

            }
        }

        writer.write("Best node-->");
        writer.write(alphabet[al[startNode].get(0).getIndex()]);
        for (int i = 1; i < al[startNode].size(); i++) {
            writer.write(", " + alphabet[al[startNode].get(i).getIndex()]);
        }

        writer.write("\nPossible roots-->");

        Graph graph2 = new Graph(capacities);
        for (int i = 0; i < graph2.V(); i++) {
            if (i != startNode) {
                graph2.addEdge(i, bfp.getEdgeTo(i));
            }
        }

        // Create a new graph corresponds to MST and find the longest path on this tree by using twice
        // time BST
        BreadthFirstPaths bfp2 = new BreadthFirstPaths(graph2, bfp.getReturnValues()[1]);
        int iiiii = bfp2.getReturnValues()[1];
        ArrayList<Integer> bestNodes = new ArrayList<>();

        for (int i = 0; i < bfp2.getReturnValues()[0] + 1; i++) {
            bestNodes.add(iiiii);
            iiiii = bfp2.getEdgeTo(iiiii);
        }

        if (bestNodes.size() % 2 == 0){
            writer.write( alphabet[bestNodes.get(((int)(bestNodes.size()) / 2) )] + ", "+  alphabet[bestNodes.get(((int)(bestNodes.size() - 1) / 2) )]);
        }
        else{
            writer.write(alphabet[bestNodes.get(((int)(bestNodes.size() - 1) / 2) )]);
        }
        int x = 3;
        writer.close();

    }
}
