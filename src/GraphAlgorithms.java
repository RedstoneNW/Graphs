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
    graph.addEdge(new Edge(graph.getVertex("1"),graph.getVertex("3"),5.5));
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

    System.out.println("---Dijkstra-Algorithm---");
    List<Vertex> dijkstra = dijkstra(graph,new Vertex("1"),new Vertex("6"));
    System.out.println("From Vertex to Vertex");
    if (dijkstra != null) {
      dijkstra.toFirst();
      System.out.print(dijkstra.getContent().getID());
      dijkstra.next();
      while (dijkstra.hasAccess()) {
        System.out.print(" -> " + dijkstra.getContent().getID());
        dijkstra.next();
      }
    } else {
      System.out.println("No Path");
    }
    System.out.println();
	System.out.println("----Breadth-Traverse----");
	System.out.println(breadthTraverse(graph,new Vertex("2"),true));
	System.out.println("-----Depth-Traverse-----");
	System.out.println(depthTraverse(graph,new Vertex("6"),true));
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
        nb.insert(new String[]{vertices.getContent().getID(), ""});
        nb.append(new String[]{neighbours.getContent().getID(), ed.getContent().getWeight() + ""});
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
    mst.setAllVertexMarks(false);
    for (int i = 0; i < length; i++) {
      mst.addEdge(edgeArr[i]);
      mst.setAllVertexMarks(false);
      if (hasCycle(mst,edgeArr[i].getVertices()[0], null)) {
        mst.removeEdge(edgeArr[i]);
      }
      mst.setAllVertexMarks(false);
      if (hasCycle(mst,edgeArr[i].getVertices()[1], null)) {
        mst.removeEdge(edgeArr[i]);
      }
    }
    return mst;
  }

  /**
   * Die Methode uebrprueft, ob in dem uebergebenen Graphen eine Schleife existiert
   * @param graph Der Graph der ueberprueft wird
   * @param source Der Knoten von dem die Suche beginnt
   * @param parent Der Elternknoten des aktuellen Knotens
   * @return true, wenn eine Schleife gefunden wurde. Andernfalls false
   */
  public static boolean hasCycle(Graph graph, Vertex source, Vertex parent) {
    source.setMark(true);
    List<Vertex> neighbours = graph.getNeighbours(source);
    neighbours.toFirst();
    while (neighbours.hasAccess()) {
      Vertex neighbour = neighbours.getContent();
      if (!neighbour.isMarked()) {
        if (hasCycle(graph, neighbour, source)) {
          return true;
        }
      } else if (!neighbour.equals(parent)) {
        return true;
      }
      neighbours.next();
    }
    return false;
  }


  /**
   * Die Methode tauscht zwei Elemente in einem Array
   * @param arr Das Array in welchem die Elemente getauscht werden sollen
   * @param i Position des ersten zu tauschenden Elements
   * @param j Position des zweiten zu tauschenden Elements
   */
  private static void swap(Edge[] arr, int i, int j)
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
  private static int partition(Edge[] arr, int low, int high)
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
  private static void quickSort(Edge[] arr, int low, int high)
  {
    if (low < high) {
      int pi = partition(arr, low, high);

      quickSort(arr, low, pi - 1);
      quickSort(arr, pi + 1, high);
    }
  }

  /**
   * Ermittelt den kuerzesten Weg zwischen den Knoten source und target mithilfe des Dijkstra Algorithmus und gibt die Liste der abzulaufenen Knoten zurueck
   * @param graph Der Graph in dem der Weg gefunden werden soll
   * @param source Der Ausgangsknoten
   * @param target Der Zielknoten
   * @return Eine Liste mit den Knoten die abgelaufen werden muessen, um den Zielknoten vom Startknoten zu erreichen
   */
  public static List<Vertex> dijkstra(
          final Graph graph, final Vertex source, final Vertex target) {
    graph.setAllVertexMarks(false);
    List<Vertex> vertexList = graph.getVertices();
    vertexList.toFirst();
    Vertex startVertex = vertexList.getContent();
    if (startVertex == null) {
      return null;
    }
    int numberOfVertices = getNumberOfVertices(graph);
    double[] distances = new double[numberOfVertices];
    Vertex[] predecessors = new Vertex[numberOfVertices];
    for (int i = 0; i < numberOfVertices; i++) {
      distances[i] = Double.MAX_VALUE;
      predecessors[i] = null;
    }
    distances[getIndexOfVertex(graph.getVertices(), source)] = 0;
    while (!graph.allVerticesMarked()) {
      Vertex currentVertex = getVertexWithSmallestDistance(graph, distances);
      if (currentVertex == null) {
        return null;
      }
      currentVertex.setMark(true);
      int currentVertexIndex = getIndexOfVertex(
              graph.getVertices(), currentVertex);
      List<Vertex> neighbors = graph.getNeighbours(currentVertex);
      neighbors.toFirst();
      while (neighbors.hasAccess()) {
        Vertex neighbor = neighbors.getContent();
        if (!neighbor.isMarked()) {
          int neighborIndex = getIndexOfVertex(graph.getVertices(), neighbor);
          double distance = distances[currentVertexIndex]
                  + graph.getEdge(currentVertex, neighbor).getWeight();
          if (distance < distances[neighborIndex]) {
            distances[neighborIndex] = distance;
            predecessors[neighborIndex] = currentVertex;
          }
        }
        neighbors.next();
      }
    }
    return getShortestPath(predecessors, target, graph.getVertices());
  }

  /**
   * Ermittelt den Pfad zum Ziel
   * @param predecessors Das Array der Vorgaengerknoten zu dem dazugehoerigen Knoten
   * @param target Zielknoten
   * @param allVertices Alle Knoten zur Referenz der Vorgaengerknoten
   * @return Liste der Knoten in der Reihenfolge in der sie gegangen werden muessen um den kuerzesten Weg zu gehen
   */
  private static List<Vertex> getShortestPath(
          final Vertex[] predecessors, final Vertex target, final List<Vertex> allVertices) {
    List<Vertex> reversedShortestPath = new List<>();
    Vertex currentVertex = target;

    while (currentVertex != null) {
      reversedShortestPath.append(currentVertex);
      int index = getIndexOfVertex(allVertices, currentVertex);
      if (index == -1) {
        return null;
      }
      currentVertex = predecessors[index];
    }
    List<Vertex> shortestPath = new List<>();
    reversedShortestPath.toLast();
    while (reversedShortestPath.hasAccess()) {
      shortestPath.append(reversedShortestPath.getContent());
      reversedShortestPath.remove();
      reversedShortestPath.toLast();
    }
    return shortestPath;
  }

  /**
   * Eine Hilfsmethode zur Bestimmung der Anzahl der Knoten eines Graphen
   * @param graph Der Graph von dem die Knoten gez
   * @return Anzahl der Knoten
   */
  private static int getNumberOfVertices(final Graph graph) {
    List<Vertex> vertices = graph.getVertices();
    vertices.toFirst();
    int numberOfVertices = 0;
    while (vertices.hasAccess()) {
      numberOfVertices++;
      vertices.next();
    }
    return numberOfVertices;
  }

  /**
   * Eine Hilfsmethode zur Bestimmung der Position eines Vertex in einer Liste
   * @param vertices Die Liste der Knoten
   * @param vertex Der Knoten der gefunden werden soll
   * @return Position des Knotens in der Liste
   */
  private static int getIndexOfVertex(
          final List<Vertex> vertices, final Vertex vertex) {
    vertices.toFirst();
    int index = 0;
    while (vertices.hasAccess()) {
      if (vertices.getContent().getID().equals(vertex.getID())) {
        return index;
      }
      index++;
      vertices.next();
    }
    return -1;
  }

  /**
   * Methode zur Ermittlung des Knotens mit der kleinsten Entfernungssumme
   * @param graph Graph von dem die kleinste Entfernungssumme gefunden werden soll
   * @param distances Bisherige Entfernungen zur Beruecksictigung
   * @return Knoten der kleinsten Entfernungssumme
   */
  private static Vertex getVertexWithSmallestDistance(
          final Graph graph, final double[] distances) {
    List<Vertex> vertices = graph.getVertices();
    vertices.toFirst();
    Vertex vertexWithSmallestDistance = null;
    double smallestDistance = Double.MAX_VALUE;
    while (vertices.hasAccess()) {
      Vertex currentVertex = vertices.getContent();
      if (!currentVertex.isMarked()) {
        double distance = distances[getIndexOfVertex(
                graph.getVertices(), currentVertex)];
        if (distance < smallestDistance) {
          smallestDistance = distance;
          vertexWithSmallestDistance = currentVertex;
        }
      }
      vertices.next();
    }
    return vertexWithSmallestDistance;
  }
  
  /**
  * Die Methode breadthTraverse sucht in dem Baum nach dem Knoten vertex mithilfe der Breitentraversierung
  * @param graph Der Graph in dem der Knoten gesucht werden soll
  * @param pVertex Der Knoten der gesucht werden soll
  * @return true, wenn der Knoten gefunden wurde. Sonst false
  */
    public static boolean breadthTraverse(final Graph graph, final Vertex pVertex, final boolean fullTraverse) {
    graph.setAllVertexMarks(false);
    boolean found = false;
	List<Vertex> vertexList = graph.getVertices();
    vertexList.toFirst();
    Vertex startVertex = vertexList.getContent();
    if (startVertex == null) {
      return false;
    }
    Queue<Vertex> verticesToVisit = new Queue<>();
    startVertex.setMark(true);
    verticesToVisit.enqueue(startVertex);
    while (!verticesToVisit.isEmpty()) {
      Vertex currentVertex = verticesToVisit.front();
      verticesToVisit.dequeue();
	  System.out.println(currentVertex.getID());
      if (currentVertex.getID().equals(pVertex.getID())) {
        if (!fullTraverse) {
          return true;
        } else {
          found = true;
        }
      }
      List<Vertex> neighbors = graph.getNeighbours(currentVertex);
      neighbors.toFirst();
      while (neighbors.hasAccess()) {
        Vertex neighbor = neighbors.getContent();
        if (!neighbor.isMarked()) {
          neighbor.setMark(true);
          verticesToVisit.enqueue(neighbor);
        }
        neighbors.next();
      }
    }
    return found;
  }
  
  /** 
  * Die Methode depthTraverse sucht in dem Baum nach dem Knoten pVertex mithilfe der Tiefentraversierung
  * @param graph Der graph in dem der Knoten gesucht werden soll
  * @param pVertex Der Knoten der gesucht werden soll
  * @return true, wenn der Knoten gefunden wurde. Sonst false
  */
    public static boolean depthTraverse(final Graph graph, final Vertex pVertex, final boolean fullTraverse) {
    graph.setAllVertexMarks(false);
    boolean found = false;
	List<Vertex> vertexList = graph.getVertices();
    vertexList.toFirst();
    Vertex startVertex = vertexList.getContent();
    if (startVertex == null) {
      return false;
    }
    Stack<Vertex> verticesToVisit = new Stack<>();
    startVertex.setMark(true);
    verticesToVisit.push(startVertex);
    while (!verticesToVisit.isEmpty()) {
      Vertex currentVertex = verticesToVisit.top();
	  System.out.println(currentVertex.getID());
      verticesToVisit.pop();
      if (currentVertex.getID().equals(pVertex.getID())) {
        if (!fullTraverse) {
          return true;
        } else {
          found = true;
        }
      }
      List<Vertex> neighbors = graph.getNeighbours(currentVertex);
      neighbors.toFirst();
      while (neighbors.hasAccess()) {
        Vertex neighbor = neighbors.getContent();
        if (!neighbor.isMarked()) {
          neighbor.setMark(true);
          verticesToVisit.push(neighbor);
        }
        neighbors.next();
      }
    }
    return found;
  }
}