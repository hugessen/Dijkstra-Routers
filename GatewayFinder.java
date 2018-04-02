import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GatewayFinder {
  static int[][] matrix;
  static int numRouters;
  
  public static void main(String[] args) throws NumberFormatException, IOException {
    int[] gateways;
    Scanner stdin = new Scanner(System.in);
    numRouters = Integer.valueOf(stdin.nextLine().trim()) + 1;
    matrix = new int[numRouters][numRouters];
    for (int i = 1; i < numRouters; i++) {
      String[] values = stdin.nextLine().trim().split(" ");
      int j = 1;
      for (String v: values)
        matrix[i][j++] = Integer.valueOf(v);
    }
    
    //Gateways
    String[] gateway_vals = stdin.nextLine().trim().split(" ");
    gateways = new int[gateway_vals.length];
    int j = 0;
    for (String v: gateway_vals)
      gateways[j++] = Integer.valueOf(v);
    
    dijkstra:
    for (int i = 1; i < numRouters; i++) {
      for (int g: gateways) {
        if (i == g) continue dijkstra;
      }
      System.out.println("Forwarding table for " + i);
      System.out.println("To   Cost   Next");
      for(int g:gateways)
        dijkstra(i,g);
    }
  }
  
  public static void dijkstra(int source, int dest) {
    int[] dist = new int[numRouters];
    int[] prev = new int[numRouters];
    ArrayList<Integer> unvisited = new ArrayList<Integer>();
    
    for (int i = 1; i < numRouters; i++) {
      prev[i] = -1;
      dist[i] = Integer.MAX_VALUE;
      unvisited.add(i);
    }
    dist[source] = 0;
    
    //Get vertex with min dist
    int current = 1;
    while(unvisited.size() > 0 && current != dest) {
      current = unvisited.get(0);
      for(int node:unvisited) {
        if (dist[node] < dist[current]) {
          current = node;
        }
      }
      unvisited.remove(unvisited.indexOf(current));
      
      //Get neighbours
      ArrayList<Integer> neighbours = new ArrayList<Integer>();
      for (int i = 1; i < numRouters; i++) 
        if (matrix[current][i] != -1)
          neighbours.add(i);
      
      for (int n:neighbours) {
        if (Arrays.asList(unvisited).contains(n))
          continue;
        int alt = dist[current] + matrix[current][n];
        if (alt < dist[n]) {
          dist[n] = alt;
          prev[n] = current;
        }
      }
    }
    int next = current;
    while(prev[prev[next]] != -1)
      next = prev[next];
    System.out.printf("%d      %d      %d", dest, dist[dest], next);
    System.out.println();
  }
}