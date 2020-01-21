/************************************************************************************
*
*  The class DepthFirstSearch contains methods to perform depth-first search
*   on a graph from a given source vertex, and to produce the shortest path 
*   (ignoring weights) from the source to any vertex.
*  It contains an inner class VertexData that maintains its distance from
*   the source vertex and its parent in the DFS tree
*
* @author Litha Thampan
*
************************************************************************************/

import java.util.*;

class DepthFirstSearch {

    // An enumeration class for colors
  enum Color  {
    WHITE,     // color for vertices that has not yet been discovered by DFS 
    GRAY,      // color for verrtices that has been discovered but not fully processed
    BLACK;     // color for vertices that have been fully processed
  }
    // An inner class for vertex data
  class VertexData {

    private Color color;
    private int vertexIndex, 
                parentIndex;    // Index of parent vertex in DFS tree
    private double distance;    // Distance to source vertex in DFS tree        
    private int startTime;
    private int endTime;
    private EdgeList treechildEdges;
    // Constructor creates a vertex with parent initialized to -1 (no parent),
    //  distance initialized to infinity, and color initialized to white
    public VertexData(int vertexIndex) {
      this.vertexIndex = vertexIndex;
      parentIndex = -1;    // no parent
      distance = Double.POSITIVE_INFINITY;
      color = Color.WHITE;   // not visited yet
      treechildEdges = new EdgeList();      // edgelist of immediate children of the node.
    }
    
    // get and set methods
    public int getVertexIndex()  { return vertexIndex; }
    public int  getParentIndex() { return parentIndex; }
    public double getDistance()  { return distance; }
    public Color getColor()      { return color; }
    
    public void setParent(int parentIndex)   { this.parentIndex = parentIndex; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setColor(Color color)        { this.color = color; }
  } // inner class Vertex data
  
  private int timeValue;  //time Value
  private Graph theGraph;
  private int sourceIndex;  // Index of source vertex
  private VertexData[] vertexData;
  private StackInterface <Integer> stack;
  private int lastVisited;  // Variable to keep track of parent of the DFS traversal
  private EdgeList treeEdges;
  private EdgeList crossEdges;
  private EdgeList forwardEdges;
  private EdgeList backEdges;

  // Constructor sets graph and index of source vertex
  public DepthFirstSearch(Graph theGraph, int sourceIndex) {
    this.theGraph = theGraph;
    this.sourceIndex = sourceIndex;
    vertexData = new VertexData[theGraph.getNumberOfVertices()];
    for (int i = 0; i < theGraph.getNumberOfVertices(); i++)
      vertexData[i] = new VertexData(i);
    this.treeEdges = new EdgeList();
    this.crossEdges = new EdgeList();
    this.forwardEdges = new EdgeList();
    this.backEdges = new EdgeList();
    this.timeValue = 0;
  }
  
  // Constructor sets graph and default source index of 0
  public DepthFirstSearch (Graph theGraph) {
    this(theGraph, 0);
  }

  public void doDFS() {

    stack = new ArrayStack(theGraph.getNumberOfVertices());   

    // Initialize queue with source vertex
    vertexData[sourceIndex].setColor(Color.GRAY);
    vertexData[sourceIndex].setDistance(0);
    stack.push(sourceIndex);
    lastVisited = sourceIndex;
    doDFSHelper();
  
  }
  public void doDFSHelper(){     //Helper function for recursion
    int currentVertexIndex,      // current vertex being processed;
        adjacentVertexIndex;     // a vertex adjacent to current vertex
    Edge currentEdge = null;
    
    if(!stack.isEmpty()){
        currentVertexIndex = stack.peek();
        vertexData[currentVertexIndex].startTime = timeValue++;
        Iterator<Edge> edgeIterator = theGraph.getVertexEdgeIterator(currentVertexIndex);
        while (edgeIterator.hasNext()) {
            currentEdge = edgeIterator.next();            
            adjacentVertexIndex = currentEdge.getEndVertex();
            if (vertexData[adjacentVertexIndex].getColor() == Color.WHITE) {  // newly discovered node . Adding to the DFS tree
                vertexData[adjacentVertexIndex].setParent(currentVertexIndex);
                vertexData[adjacentVertexIndex].setDistance(vertexData[currentVertexIndex].getDistance() + 1);
                vertexData[adjacentVertexIndex].setColor(Color.GRAY);
                stack.push(adjacentVertexIndex);
                lastVisited = adjacentVertexIndex;
                treeEdges.addEdge(currentEdge);
                vertexData[currentVertexIndex].treechildEdges.addEdge(currentEdge);
                doDFSHelper();
            } 
            else if(vertexData[adjacentVertexIndex].getColor() == Color.GRAY) {  // Already Visited - GRAY . EDGE back to an ancestor
                backEdges.addEdge(currentEdge);
            } 
            else{  // Already Visited - BLACK
                if(vertexData[currentVertexIndex].startTime < vertexData[adjacentVertexIndex].startTime ){ // The child started after current (current is an ancenstor)
                    forwardEdges.addEdge(currentEdge);
                } 
                else{ // The child started before current (current is not an ancestor)
                    crossEdges.addEdge(currentEdge);
                }
            }
        }
        vertexData[currentVertexIndex].setColor(Color.BLACK);
        stack.pop();
        vertexData[currentVertexIndex].endTime = timeValue++;
    }


  }
  public String getPathFromSource(int vertexIndex) { // traversed from destination to source through parent link
    if(vertexData[vertexIndex].getDistance() == Double.POSITIVE_INFINITY)
      return ("No path from source vertex to vertex " + vertexIndex + ".");
    else
      return ("(" + getPathFromSourceHelper(vertexIndex) + ")" );
  }
  
  public String getPathFromSourceHelper(int vertexIndex) { //Helper function for recursion
    if (vertexData[vertexIndex].getParentIndex() == -1) 
      // at source vertex
      return Integer.toString(vertexIndex);
    else
      return getPathFromSourceHelper(vertexData[vertexIndex].getParentIndex()) + " " + Integer.toString(vertexIndex);
  }
 
  public String dfsTreeToString() { // traversed from source to every (reachable) node throughtreeEdges
    return dfsTreeToStringHelper(this.sourceIndex);
  }
  public String dfsTreeToStringHelper(int vertexIndex){  //Helper function for recursion
    Iterator<Edge> edgeIterator = vertexData[vertexIndex].treechildEdges.getEdgeListIterator();
    Edge currentEdge = null;
    String returnString = "";
    returnString += "( [" + vertexIndex + ","+ vertexData[vertexIndex].startTime + "," + vertexData[vertexIndex].endTime + "]";
    while (edgeIterator.hasNext()) {
        currentEdge = edgeIterator.next();
        returnString += " " + dfsTreeToStringHelper(currentEdge.getEndVertex()) ;
    }
    returnString += " )";
    return returnString;
  }
  public Iterator<Edge> getTreeEdgeIterator() { //returns TreeEdgeIterator
    return treeEdges.getEdgeListIterator();
  } 
  public Iterator<Edge> getBackEdgeIterator() {  //returns BackEdgeIterator
    return backEdges.getEdgeListIterator();
  }
  public Iterator<Edge> getForwardEdgeIterator() {  //returns ForwardEdgeIterator
    return forwardEdges.getEdgeListIterator();
  }
  public Iterator<Edge> getCrossEdgeIterator() {  //returns CrossEdgeIterator
    return crossEdges.getEdgeListIterator();
  }
}