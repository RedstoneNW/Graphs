public class Main {
  
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
    prim(graph);
  }

  public static void list(Graph graph) {
    List<Vertex> verts = new List<Vertex>();
    verts = graph.getVertices();
    verts.toFirst();
    List<List<String[]>> adjacenceList = new List<>();
    while (verts.hasAccess()) {
      List<String[]> nb = new List<>();
      List<Vertex> neighboors = new List<>();
      List<Edge> ed = new List<>();
      neighboors = graph.getNeighbours(verts.getContent());
      ed = graph.getEdges(verts.getContent());
      nb.toFirst();
      ed.toFirst();
      neighboors.toFirst();
      while (neighboors.hasAccess()) {
        nb.insert(new String[]{new String(verts.getContent().getID() + ""), ""});
        nb.append(new String[]{new String(neighboors.getContent().getID() + ""), new String(ed.getContent().getWeight() + "")});
        neighboors.next();
        ed.next();
      }
      adjacenceList.append(nb);
      verts.next();
    }

    //Printing List
    adjacenceList.toFirst();
    while (adjacenceList.hasAccess()) {
      adjacenceList.getContent().toFirst();
      while (adjacenceList.getContent().hasAccess()) {
        System.out.print(adjacenceList.getContent().getContent()[0] + "(" + adjacenceList.getContent().getContent()[1] + ") ");
        adjacenceList.getContent().next();
      }
      System.out.println("");
      adjacenceList.next();
    }
  }

  public static void matrix(Graph graph) {
    List<Vertex> verts = graph.getVertices();
    int length = 0;
    verts.toFirst();
    while (verts.hasAccess()) { 
      length++;
      verts.next();
    } // end of while
    String[][] matrix = new String[length+1][length+1];
    matrix[0][0] = " | ";
    verts.toFirst();
    for (int i = 1; i < length+1; i++) {
      matrix[i][0] = verts.getContent().getID() + "|";
      verts.next();
    }
    verts.toFirst();
    for (int i = 1; i < length+1; i++) {
      matrix[0][i] = verts.getContent().getID() + "   ";
      verts.next();
    }

    verts.toFirst();
    for (int i = 1; i < length+1; i++) {
      List<Edge> edgeList = graph.getEdges(verts.getContent());
      for (int x = 1; x < length+1; x++) {
        edgeList.toFirst();
        while (edgeList.hasAccess()) {
          String vert0h = edgeList.getContent().getVertices()[0].getID() + "   ";
          String vert1h = edgeList.getContent().getVertices()[1].getID() + "   ";
          String vert0v = edgeList.getContent().getVertices()[0].getID() + "|";
          String vert1v = edgeList.getContent().getVertices()[1].getID() + "|";
          if (edgeList.getContent().getVertices()[0].getID() != verts.getContent().getID() && vert0h.equals(matrix[0][x]) && !vert0v.equals(matrix[i][0])) {
            matrix[i][x] = edgeList.getContent().getWeight() + " ";
            edgeList.toLast();
          } else if (edgeList.getContent().getVertices()[1] != verts.getContent() && vert1h.equals(matrix[0][x]) && !vert1v.equals(matrix[i][0])) {
            matrix[i][x] = edgeList.getContent().getWeight() + " ";
            edgeList.toLast();
          } else {
            matrix[i][x] = "--- ";
          }
          edgeList.next();
        }
      }
      verts.next();
    }

    //Print Matrix
    for (String[] x : matrix)
    {
     for (String y : x)
     {
          System.out.print(y + " ");
     }
     System.out.println();
     }
  }

  public static void prim(Graph graph) {
    Graph mst = new Graph();
    List<Vertex> verts = graph.getVertices();
    verts.toFirst();

    while (verts.hasAccess()) {
      mst.addVertex(verts.getContent());
      verts.next();
    }

    verts.toFirst();

    while (verts.hasAccess()) {
      List<Edge> edges = graph.getEdges(verts.getContent());
      edges.toFirst();
      Edge mE = new Edge(new Vertex(""),new Vertex(""),Integer.MAX_VALUE);
      while (edges.hasAccess()) {
        if (edges.getContent().getWeight() < mE.getWeight()) {
          mE = edges.getContent();
        }
        edges.next();
      }
      mst.addEdge(mE);
      verts.next();
    }

    list(mst);
    matrix(mst);
  }

}