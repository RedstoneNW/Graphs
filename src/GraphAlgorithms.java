import java.util.*;

public class GraphAlgorithms {

  /**
   * Die Main Methode zur Erzeugung des Graphens und zur Ausfuehrung der Methoden
   * @param args Argumente der Main Methode
   */
  public static void main(String[] args) {
    Graph graph = new Graph();
    graph.addVertex(new Vertex("1"));
    graph.addVertex(new Vertex("2"));
    graph.addVertex(new Vertex("3"));
    graph.addVertex(new Vertex("6"));
    graph.addEdge(new Edge(graph.getVertex("1"),graph.getVertex("3"),2.0));
    graph.addEdge(new Edge(graph.getVertex("3"),graph.getVertex("2"),3.4));
    graph.addEdge(new Edge(graph.getVertex("2"),graph.getVertex("1"),5.6));
    graph.addEdge(new Edge(graph.getVertex("3"),graph.getVertex("6"),10.9));
    graph.addEdge(new Edge(graph.getVertex("1"),graph.getVertex("6"),42.0));
    System.out.println("----------List----------");
    list(graph);
    System.out.println("---------Matrix---------");
    matrix(graph);
    System.out.println("-----Prim-Algorithm-----");
    matrix(prims(graph));
    System.out.println("---Kruskals-Algorithm---");
    matrix(kruskal(graph));
  }

  /**
   * Die Methode erzeugt eine Adjazenzliste und gibt diese aus
   * @param graph Der Graph von dem die Adjazenzliste erstellt werden soll
   */
  public static void list(Graph graph) {
    List<Vertex> vertices = graph.getVertices();
    vertices.toFirst();
    List<List<String[]>> adjacenceList = new List<>();
    while (vertices.hasAccess()) {
      List<String[]> nb = new List<>();
      List<Vertex> neighbours;
      List<Edge> ed;
      neighbours = graph.getNeighbours(vertices.getContent());
      ed = graph.getEdges(vertices.getContent());
      nb.toFirst();
      ed.toFirst();
      neighbours.toFirst();
      while (neighbours.hasAccess()) {
        nb.insert(new String[]{vertices.getContent().getID() + "", ""});
        nb.append(new String[]{neighbours.getContent().getID() + "", new String(ed.getContent().getWeight() + "")});
        neighbours.next();
        ed.next();
      }
      adjacenceList.append(nb);
      vertices.next();
    }

    adjacenceList.toFirst();
    while (adjacenceList.hasAccess()) {
      adjacenceList.getContent().toFirst();
      while (adjacenceList.getContent().hasAccess()) {
        System.out.print(adjacenceList.getContent().getContent()[0] + "(" + adjacenceList.getContent().getContent()[1] + ") ");
        adjacenceList.getContent().next();
      }
      System.out.println();
      adjacenceList.next();
    }
  }

  /**
   * Die Methode erzeugt eine Adjazenzmatrix und gibt diese aus
   * @param graph Der Graph von dem die Adjazenzmatrix erstellt werden soll
   */
  public static void matrix(Graph graph) {
    List<Vertex> vertices = graph.getVertices();
    int length = 0;
    vertices.toFirst();
    while (vertices.hasAccess()) {
      length++;
      vertices.next();
    }
    String[][] matrix = new String[length+1][length+1];
    matrix[0][0] = " | ";
    vertices.toFirst();
    for (int i = 1; i < length+1; i++) {
      matrix[i][0] = vertices.getContent().getID() + "|";
      vertices.next();
    }
    vertices.toFirst();
    for (int i = 1; i < length+1; i++) {
      matrix[0][i] = vertices.getContent().getID() + "   ";
      vertices.next();
    }

    vertices.toFirst();
    for (int i = 1; i < length+1; i++) {
      List<Edge> edgeList = graph.getEdges(vertices.getContent());
      for (int x = 1; x < length+1; x++) {
        edgeList.toFirst();
        while (edgeList.hasAccess()) {
          String vert0h = edgeList.getContent().getVertices()[0].getID() + "   ";
          String vert1h = edgeList.getContent().getVertices()[1].getID() + "   ";
          String vert0v = edgeList.getContent().getVertices()[0].getID() + "|";
          String vert1v = edgeList.getContent().getVertices()[1].getID() + "|";
          if (!Objects.equals(edgeList.getContent().getVertices()[0].getID(), vertices.getContent().getID()) && vert0h.equals(matrix[0][x]) && !vert0v.equals(matrix[i][0])) {
            matrix[i][x] = edgeList.getContent().getWeight() + " ";
            edgeList.toLast();
          } else if (edgeList.getContent().getVertices()[1] != vertices.getContent() && vert1h.equals(matrix[0][x]) && !vert1v.equals(matrix[i][0])) {
            matrix[i][x] = edgeList.getContent().getWeight() + " ";
            edgeList.toLast();
          } else {
            matrix[i][x] = "--- ";
          }
          edgeList.next();
        }
      }
      vertices.next();
    }

    for (String[] x : matrix)
    {
     for (String y : x)
     {
          System.out.print(y + " ");
     }
     System.out.println();
     }
  }

  /**
   * Die Methode ermittelt den minimalen Spannbaum mithilfe des Algorithmus von Prim
   * @param graph Der Graph von dem der MST ermittelt werden soll
   * @return Gibt den minimalen Spannbaum zurueck
   */
  public static Graph prims(Graph graph) {
    List<Edge> mstEd = new List<>();
    graph.setAllVertexMarks(false);
    graph.setAllEdgeMarks(false);
    List<Vertex> vertices = graph.getVertices();
    vertices.toFirst();
    Vertex start = vertices.getContent();
    start.setMark(true);
    List<Edge> edgeList = new List<>();
    while (!graph.allVerticesMarked()) {
      List<Edge> newEdges = graph.getEdges(start);
      newEdges.toFirst();
      while (newEdges.hasAccess()) {
        if (!newEdges.getContent().isMarked() &&
                !(newEdges.getContent().getVertices()[0].isMarked() && newEdges.getContent().getVertices()[1].isMarked())) {
          edgeList.append(newEdges.getContent());
        }
        newEdges.next();
      }
      edgeList.toFirst();
      Edge minEdge = edgeList.getContent();
      edgeList.next();
      while (edgeList.hasAccess()) {
        if (edgeList.getContent().getWeight() < minEdge.getWeight() && !(minEdge.getVertices()[0].isMarked() && minEdge.getVertices()[1].isMarked())) {
          minEdge = edgeList.getContent();
        }
        edgeList.next();
      }
      if (!(minEdge.getVertices()[0].isMarked() && minEdge.getVertices()[1].isMarked())){
        mstEd.append(minEdge);
      }
      minEdge.setMark(true);
      edgeList.toFirst();
      while (edgeList.hasAccess()) {
        if (edgeList.getContent().isMarked()) {
          edgeList.remove();
        }
        edgeList.next();
      }
      minEdge.getVertices()[0].setMark(true);
      minEdge.getVertices()[1].setMark(true);
      edgeList.toFirst();
      vertices.toFirst();
      boolean end = false;
      while (vertices.hasAccess() && !end) {
        if (!vertices.getContent().isMarked()) {
          start = vertices.getContent();
          end = true;
        }
        vertices.next();
      }

    }
    Graph mst = new Graph();
    List<Vertex> orgVert = graph.getVertices();
    orgVert.toFirst();
    while (orgVert.hasAccess()) {
      mst.addVertex(orgVert.getContent());
      orgVert.next();
    }
    mstEd.toFirst();
    while (mstEd.hasAccess()) {
      mst.addEdge(mstEd.getContent());
      mstEd.next();
    }
    return mst;
  }

  /**
   * Die Methode ermittelt den minimalen Spannbaum mithilfe des Algorithmus von Kruskal
   * @param graph Der Graph von dem der MST ermittelt werden soll
   * @return Gibt den minimalen Spannbaum zurueck
   */
  public static Graph kruskal(Graph graph) {
    Graph mst = new Graph();
    List<Edge> edges = graph.getEdges();
    List<Vertex> vertices = graph.getVertices();
    vertices.toFirst();
    while (vertices.hasAccess()) {
      mst.addVertex(vertices.getContent());
      vertices.next();
    }
    edges.toFirst();
    int length = 0;
    while (edges.hasAccess()) {
      length++;
      edges.next();
    }
    Edge[] edgeArr = new Edge[length];
    edges.toFirst();
    for (int i = 0; i < length; i++) {
      edgeArr[i] = edges.getContent();
      edges.next();
    }
    quickSort(edgeArr,0,edgeArr.length-1);
    graph.setAllVertexMarks(false);
    for (int i = 0; i < length; i++) {
      if (!(edgeArr[i].getVertices()[0].isMarked() && edgeArr[i].getVertices()[1].isMarked())) {
        mst.addEdge(edgeArr[i]);
        edgeArr[i].getVertices()[0].setMark(true);
        edgeArr[i].getVertices()[1].setMark(true);
      }
    }
    return mst;
  }

  /**
   * Die Methode tauscht zwei Elemente in einem Array
   * @param arr Das Array in welchem die Elemente getauscht werden sollen
   * @param i Position des ersten zu tauschenden Elements
   * @param j Position des zweiten zu tauschenden Elements
   */
  static void swap(Edge[] arr, int i, int j)
  {
    Edge temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  /**
   * Die Methode nimmt das letzte Element als pivot und positioniert das Pivot an die richtige Position.
   * Alle kleineren Elemente werden dann links vom pivot und alle groesseren Elemente nach rechts
   * @param arr Array welches sortiert werden soll
   * @param low Untere Grenze des zu sortierenden Arrays
   * @param high Obere Grenze des zu sortierenden Arrays
   * @return Gibt die Position des Pivots zurueck
   */
  static int partition(Edge[] arr, int low, int high)
  {
    double pivot = arr[high].getWeight();

    int i = (low - 1);

    for (int j = low; j <= high - 1; j++) {

      if (arr[j].getWeight() < pivot) {

        i++;
        swap(arr, i, j);
      }
    }
    swap(arr, i + 1, high);
    return (i + 1);
  }

  /** Methode die ein Array mithilfe des Quicksorts sortiert
   * @param arr Array welches sortiert wird
   * @param low Untere Grenze des zu sortierenden Arrays
   * @param high Obere Grenze des zu sortierenden Arrays
   */
  static void quickSort(Edge[] arr, int low, int high)
  {
    if (low < high) {
      int pi = partition(arr, low, high);

      quickSort(arr, low, pi - 1);
      quickSort(arr, pi + 1, high);
    }
  }
}