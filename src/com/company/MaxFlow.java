package com.company;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.LinkedList;

//Student name = Nabeel Shanaz
//Student ID = 2019240

class MaxFlow {
    private int NoOfVertices;  // Number of vertices in the graph
    public static ArrayList<String> augmentPaths = new ArrayList<>();

    /* Returns true if there is a path from source 'src' to sink
     'sinkNode' in residual graph. Also fills parent[] to store the
     path */
    boolean breadthFirstSearch(int rsdlGraph[][], int src, int sinkNode, int parent[])  //rsdlGraph is residual graph.
    {
        // Create a visited array and mark all vertices as
        // not visited

        boolean visited[] = new boolean[NoOfVertices];
        for (int i = 0; i < NoOfVertices; ++i)
            visited[i] = false;

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue
                = new LinkedList<Integer>();
        queue.add(src);
        visited[src] = true;
        parent[src] = -1;

        // Standard BreadthFirstSearch Loop
        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < NoOfVertices; v++) {
                if (visited[v] == false
                        && rsdlGraph[u][v] > 0) {
                    // If we find a connection to the sink node, then there
//                     is no point in BreadthFirstSearch anymore
//                     We just have to set its parent and
                    // can return true
                    if (v == sinkNode) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // returns false, since we ddnt reach sink node
        // in BreadthFirstSearch from source
        return false;
    }

    // Returns tne maximum flow from s to t in the given
    // graph
    int fordFulkerson(int graph[][], int s, int t)
    {
        int u, v;

        // Create a residual graph and fill the residual
        // graph with given capacities in the original graph
        // as residual capacities in residual graph

        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        int rGraph[][] = new int[NoOfVertices][NoOfVertices];

        for (u = 0; u < NoOfVertices; u++)
            for (v = 0; v < NoOfVertices; v++)
                rGraph[u][v] = graph[u][v];

        // This array is filled by BFS and to store path
        int parent[] = new int[NoOfVertices];

        int max_flow = 0; // There is no flow initially
        int counter = 0;

        // Augment the flow while tere is path from source
        // to sink
        while (breadthFirstSearch(rGraph, s, t, parent)) {
            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            counter++;
            int path_flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                path_flow
                        = Math.min(path_flow, rGraph[u][v]);
            }

            // update residual capacities of the edges and
            // reverse edges along the path
            ArrayList<String> pathsDisplay = new ArrayList<>();
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
                String paths = (u+">"+v+" ");
                //adding paths to the arraylist
                pathsDisplay.add(paths);
            }

            Collections.reverse(pathsDisplay);
            System.out.println("augmented path "+ counter +" : "+pathsDisplay);
            System.out.println();
            // Add path flow to overall flow
            max_flow += path_flow;
        }

        // Return the overall flow
        return max_flow;
    }

     String getPath(String fileName, String directory) {

         File folder = new File(directory);
         File[] listOfFiles = folder.listFiles();
         MaxFlow m = new MaxFlow();
         if (listOfFiles != null) {
             for (File file : listOfFiles) {
                 if (file.isFile() && file.getName().equals(fileName)) {
                     String filePath = file.getPath();
                     m.FileInputSystem(filePath);
                     return null;
                 }
             }
         }
         System.out.println("theres no such file in the directory");
        return null;
    }


    public String FileInputSystem(String filePath){
        int graphLength = 0;
        MaxFlow m = new MaxFlow();
        int[][] graph = new int[0][];
        try {
            //the file to be opened for reading
            FileInputStream fis = new FileInputStream(filePath);
            Scanner scFis = new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read

            int loop = 0;
            int nodes = 0;
            int sourceNode = 0;

            long startTime = System.nanoTime();

            while (scFis.hasNextLine()) {

                if (loop == 0) {
                    String[] firstLine =  scFis.nextLine().split(" ");
                    nodes = Integer.parseInt(firstLine[0]);
                    graphLength = nodes;
                    graph = new int[graphLength][graphLength];
                    for (int x = 0; x < nodes; x++) { //graph.length
                        for (int y = 0; y < nodes; y++) {  //graph.length
                            graph[x][y] = 0;
                        }
                    }
                } else {
                    String[] graphRow = scFis.nextLine().split(" ");
                    int startNode = Integer.parseInt(graphRow[0]);
                    int endNode = Integer.parseInt(graphRow[1]);
                    int capacity = Integer.parseInt(graphRow[2]);
                    graph[startNode][endNode] = capacity;

                    if (loop == 1) {
                        sourceNode = startNode;
                    }
                }
                loop++;
            }

            int count = 0;
            // the augment paths are displayed

            for (int i = 0; i < augmentPaths.size(); i++) {
                count--;
                String val = augmentPaths.get(count);
                if (!(val.substring(0, 1).equals(String.valueOf(0)))) {//source
                    System.out.print(val);
                } else if (i == 0) {
                    System.out.print(val);

                } else {
                    System.out.print(val);
                }
            }

            System.out.println(" ");
            System.out.println("Source node        : "+ sourceNode);
            System.out.println("Destination node   : "+ (nodes - 1));
            System.out.println(" ");
            System.out.println("===== SOLUTION GRAPH =====");
            System.out.println(" ");
            for (int x = 0; x < nodes; x++) { //graph.length
                System.out.println(Arrays.toString(graph[x]));
            }

            System.out.println("==========================");
//            System.out.println();
            m.NoOfVertices = nodes;
            System.out.println("");
            System.out.println("The maximum possible flow is "
                    + m.fordFulkerson(graph, 0, nodes - 1));


            scFis.close();     //closes the scanner

            long endTime = System.nanoTime();
            double totElapsedTime = (double) (endTime - startTime);
            System.out.println();
            System.out.println("Total elapsed time : " + totElapsedTime * 0.000000001 + "s");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Driver program to test above functions
    public static void main(String[] args)
            throws java.lang.Exception {
        System.out.println("    MAX FLOW FINDER");

        MaxFlow m = new MaxFlow();
        Scanner sc = new Scanner(System.in);

        String otherFile;
        String fileName;
        String directory = "C:\\Users\\94776\\IdeaProjects\\MaxFlow\\src\\com\\company\\algo-tut";

        while (true){

            System.out.println("");
            System.out.println("+++++++++++++++++++++++");
            System.out.println("Enter B to choose bridge graph type");
            System.out.println("Enter L to choose ladder graph type");
            System.out.println("Enter T to choose other graphs");
            System.out.println("Enter Q to quit program");
            System.out.println("+++++++++++++++++++++++");
            String graphType = sc.next().toUpperCase();
            if (graphType.equals("B") || graphType.equals("L") || graphType.equals("Q")) {
                if (graphType.equals("B")){
                    graphType = "bridge";
                } if (graphType.equals("L")) {
                    graphType = "ladder";
                } if (graphType.equals("Q"))  {
                    System.exit(0);
                }
                String fileNumber;
                while (true){
                    System.out.println("Enter file number of graph type u picked");
                    fileNumber = sc.next();
                    try{
                        Integer.parseInt(fileNumber);
                        if ( 0 < Integer.parseInt(fileNumber) && Integer.parseInt(fileNumber) < 10) {
                            break;
                        }else {
                            System.out.println("File number not in range");
                        }
                    }catch(Exception e ) {
                        System.out.println("Invalid input, Enter the file number");
                    }
                }
                fileName = graphType + "_" + fileNumber + ".txt";
                System.out.println("File Name:  " + fileName);
                m.getPath(fileName,directory);
                continue;
            } if (graphType.equals("T")) {
                System.out.println("Enter file name:");
                otherFile = sc.next();
                fileName = otherFile;
                m.getPath(fileName,directory);
            }
            else {
                System.out.println("Invalid input");
            }
        }

    }
}






