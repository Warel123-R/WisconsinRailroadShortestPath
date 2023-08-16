// --== CS400 File Header Information ==--
// Name: Ronald Mishiev
// Email: Mishiev@wisc.edu
// Group and Team: CQ Blue
// Group TA: Rahul CHouldry
// Lecturer: Florian
// Notes to Grader: N/A

import java.util.PriorityQueue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
		implements GraphADT<NodeType, EdgeType> {

	/**
	 * While searching for the shortest path between two nodes, a SearchNode
	 * contains data about one specific path between the start node and another node
	 * in the graph. The final node in this path is stored in it's node field. The
	 * total cost of this path is stored in its cost field. And the predecessor
	 * SearchNode within this path is referened by the predecessor field (this field
	 * is null within the SearchNode containing the starting node in it's node
	 * field).
	 *
	 * SearchNodes are Comparable and are sorted by cost so that the lowest cost
	 * SearchNode has the highest priority within a java.util.PriorityQueue.
	 */
	protected class SearchNode implements Comparable<SearchNode> {
		public Node node;
		public double cost;
		public SearchNode predecessor;

		public SearchNode(Node node, double cost, SearchNode predecessor) {
			this.node = node;
			this.cost = cost;
			this.predecessor = predecessor;
		}

		public int compareTo(SearchNode other) {
			if (cost > other.cost)
				return +1;
			if (cost < other.cost)
				return -1;
			return 0;
		}
	}

	/**
	 * This helper method creates a network of SearchNodes while computing the
	 * shortest path between the provided start and end locations. The SearchNode
	 * that is returned by this method is represents the end of the shortest path
	 * that is found: it's cost is the cost of that shortest path, and the nodes
	 * linked together through predecessor references represent all of the nodes
	 * along that shortest path (ordered from end to start).
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return SearchNode for the final end node within the shortest path
	 * @throws NoSuchElementException when no path from start to end is found or
	 *                                when either start or end data do not
	 *                                correspond to a graph node
	 */
	protected SearchNode computeShortestPath(NodeType start, NodeType end) throws NoSuchElementException {
		// throws exception if the params are invalid
		if (!this.containsNode(start)) {
			throw new NoSuchElementException("start node is not present in the path");
		} else if (!containsNode(end)) {
			throw new NoSuchElementException("desired end node is not present in the path");
		}

		Hashtable<Node, SearchNode> nodeSearchNodes = new Hashtable<>();
		PriorityQueue<SearchNode> queue = new PriorityQueue<>();
		Set<Node> visited = new HashSet<>();

		initializeQueue(nodes.get(start), nodeSearchNodes, queue);

		while (!queue.isEmpty()) {
			SearchNode nodeSearchNode = queue.remove();
			Node node = nodeSearchNode.node;

			// continues if we havent gone down this path yet
			if (!visited.contains(node)) {
				visited.add(node);

				if (node.equals(nodes.get(end))) {
					return nodeSearchNode;
				}

				for (Edge edge : node.edgesLeaving) {
					if (!visited.contains(edge.successor)) {

						// if the edge predecessor isnt alrdy visited, puts it in
						processingSearchNode(nodeSearchNode, edge, nodeSearchNodes, queue);
					}
				}
			}
		}
		// throw exception if invalid
		throw new NoSuchElementException("no path exists");
	}

	/**
	 * helper method for processing the path, taked in the various needed fields and
	 * sets them to the correct path
	 */
	private void processingSearchNode(SearchNode nodeSearchNode, Edge edge, Hashtable<Node, SearchNode> nodeSearchNodes,
			PriorityQueue<SearchNode> pqueue) {
		double totalCost = nodeSearchNode.cost + edge.data.doubleValue();
		SearchNode neighborSearchNode = nodeSearchNodes.get(edge.successor);

		if (neighborSearchNode == null) {
			// if it hasnt been visited, make new searchnode and add to hashtable & priority
			// queue
			neighborSearchNode = new SearchNode(edge.successor, totalCost, nodeSearchNode);
			nodeSearchNodes.put(edge.successor, neighborSearchNode);
			pqueue.add(neighborSearchNode);
			return;
		}

		if (totalCost < neighborSearchNode.cost) {
			// updates the current cost and node to the new one
			neighborSearchNode.cost = totalCost;
			neighborSearchNode.predecessor = nodeSearchNode;

			// fixes the priority queue to keep the order
			pqueue.remove(neighborSearchNode);
			pqueue.add(neighborSearchNode);
			return;
		}
	}

	/**
	 * helper method for initializing the fields in the main compute method this
	 * helps code readability and realiability, while helping with debugging
	 * 
	 * @return the initialized searchnode data necessary for the compute method
	 */
	private SearchNode initializeQueue(Node source, Hashtable<Node, SearchNode> nodeSearchNodes,
			PriorityQueue<SearchNode> queue) {
		SearchNode sourceSearchNode = new SearchNode(source, 0, null);
		nodeSearchNodes.put(source, sourceSearchNode);
		queue.add(sourceSearchNode);
		return sourceSearchNode;
	}

	/**
	 * Returns the list of data values from nodes along the shortest path from the
	 * node with the provided start value through the node with the provided end
	 * value. This list of data values starts with the start value, ends with the
	 * end value, and contains intermediary values in the order they are encountered
	 * while traversing this shorteset path. This method uses Dijkstra's shortest
	 * path algorithm to find this solution.
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return list of data item from node along this shortest path
	 */
	public List<NodeType> shortestPathData(NodeType start, NodeType end) {
		SearchNode searchNode = computeShortestPath(start, end);
		List<NodeType> path = new ArrayList<NodeType>();
		while (searchNode != null) {
			path.add(searchNode.node.data);
			searchNode = searchNode.predecessor;
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * Returns the cost of the path (sum over edge weights) of the shortest path
	 * from the node containing the start data to the node containing the end data.
	 * This method uses Dijkstra's shortest path algorithm to find this solution.
	 *
	 * @param start the data item in the starting node for the path
	 * @param end   the data item in the destination node for the path
	 * @return the cost of the shortest path between these nodes
	 */
	public double shortestPathCost(NodeType start, NodeType end) {
		SearchNode searchNode = computeShortestPath(start, end);
		return searchNode.cost;
	}
}
//
//	/**
//	 * graph for test, built similar to ones in lecture on a larger scale.
//	 */
//	private static DijkstraGraph<String, Double> testGraph() {
//        /**
//         * tester graph:
//         *        A
//         *       / \
//         *     2/   \3
//         *     /     \
//         *    C---4---D
//         *   / \     / \
//         * 5/   \<6>/   \5
//         * /     \ /     \
//         * B---3--F---2---E
//         *  \            /
//         *  7\          /4
//         *    \        /
//         *     G------H
//         *        3
//         */
//		DijkstraGraph<String, Double> graph = new DijkstraGraph<String, Double>();
//		graph.insertNode("A");
//		graph.insertNode("B");
//		graph.insertNode("C");
//		graph.insertNode("D");
//		graph.insertNode("E");
//		graph.insertNode("F");
//		graph.insertNode("G");
//		graph.insertNode("H");
//		graph.insertNode("J"); // unreachable node
//
//		graph.insertEdge("A", "C", 2.0);
//		graph.insertEdge("C", "A", 2.0);
//
//		graph.insertEdge("A", "D", 3.0);
//		graph.insertEdge("D", "A", 3.0);
//
//		graph.insertEdge("B", "C", 5.0);
//		graph.insertEdge("C", "B", 5.0);
//
//		graph.insertEdge("B", "F", 3.0);
//		graph.insertEdge("F", "B", 3.0);
//
//		graph.insertEdge("B", "G", 7.0);
//		graph.insertEdge("G", "B", 7.0);
//
//		graph.insertEdge("C", "D", 4.0);
//		graph.insertEdge("D", "C", 4.0);
//
//		graph.insertEdge("C", "F", 6.0);
//		graph.insertEdge("F", "C", 6.0);
//
//		graph.insertEdge("D", "E", 5.0);
//		graph.insertEdge("E", "D", 5.0);
//
//		graph.insertEdge("D", "F", 6.0);
//		graph.insertEdge("F", "D", 6.0);
//
//		graph.insertEdge("E", "F", 2.0);
//		graph.insertEdge("F", "E", 2.0);
//
//		graph.insertEdge("E", "H", 4.0);
//		graph.insertEdge("H", "E", 4.0);
//
//		graph.insertEdge("G", "H", 3.0);
//		graph.insertEdge("H", "G", 3.0);
//
//		printShortestPath(graph, "D", "H");
//		printShortestPath(graph, "A", "F");
//		printShortestPath(graph, "E", "H");
//		printShortestPath(graph, "B", "H");
//		printShortestPath(graph, "B", "E");
//		printShortestPath(graph, "C", "H");
//
//		return graph;
//	}
//
//	/**
//	 * print helper method for tracking the process that the algorithm undergoes
//	 */
//	private static void printShortestPath(DijkstraGraph<String, Double> graph, String source, String target) {
//		List<String> shortestPath = graph.shortestPathData(source, target);
//		System.out.printf("displaying shortest path from %s to %s = %s with shortestPathCost=%s%n", source, target,
//				shortestPath, graph.shortestPathCost(source, target));
//	}
//
//	/**
//	 * test for finding different paths through various scenarios through the graph
//	 * above
//	 * 
//	 * @returns false if an exception is thrown or wrong path
//	 */
//	@Test
//	public void testShortestPath() {
//		try {
//			DijkstraGraph<String, Double> graph = testGraph();
//
//			List<String> shortestPath1 = graph.shortestPathData("D", "H");
//			assertEquals("[D, E, H]", shortestPath1.toString()); // 9.0
//			List<String> shortestPath2 = graph.shortestPathData("A", "F");
//			assertEquals("[A, C, F]", shortestPath2.toString()); // 8.0
//			List<String> shortestPath3 = graph.shortestPathData("E", "H");
//			assertEquals("[E, H]", shortestPath3.toString()); // 4.0
//			List<String> shortestPath4 = graph.shortestPathData("B", "H");
//			assertEquals("[B, F, E, H]", shortestPath4.toString()); // 9.0
//			List<String> shortestPath5 = graph.shortestPathData("B", "E");
//			assertEquals("[B, F, E]", shortestPath5.toString()); // 5.0
//			List<String> shortestPath6 = graph.shortestPathData("C", "H");
//			assertEquals("[C, F, E, H]", shortestPath6.toString()); // 12.0
//		} catch (Exception e) {
//			fail("thrown exception");
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * test for finding the correct costs of the combined edge weights with the
//	 * pathes in previous tester
//	 * 
//	 * @return false if exception is wrong or wrong cost
//	 */
//	@Test
//	public void testCost() {
//		try {
//			DijkstraGraph<String, Double> graph = testGraph();
//
//			double shortestPath1 = graph.shortestPathCost("D", "H");
//			assertEquals(9.0, shortestPath1);
//			double shortestPath2 = graph.shortestPathCost("A", "F");
//			assertEquals(8.0, shortestPath2);
//			double shortestPath3 = graph.shortestPathCost("E", "H");
//			assertEquals(4.0, shortestPath3);
//			double shortestPath4 = graph.shortestPathCost("B", "H");
//			assertEquals(9.0, shortestPath4);
//			double shortestPath5 = graph.shortestPathCost("B", "E");
//			assertEquals(5.0, shortestPath5);
//			double shortestPath6 = graph.shortestPathCost("C", "H");
//			assertEquals(12.0, shortestPath6);
//		} catch (Exception e) {
//			fail("thrown exception");
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * test for non-existent path
//	 * 
//	 * @return false if it successfully finds a path(since it isnt supposed to exist
//	 * @return false if wrong exception is thrown
//	 */
//	@Test
//	public void testShortestPathUnreachable() {
//		try {
//			DijkstraGraph<String, Double> graph = testGraph();
//			graph.shortestPathData("D", "J");
//			fail("should have thrown nosuchelement");
//		} catch (NoSuchElementException e) {
//		} catch (Exception e) {
//			fail("should have thrown nosuchelement");
//		}
//	}
//
//	/**
//	 * test for finding cost of unreachable node
//	 * 
//	 * @return false if cost is found for the unreachable path, or wrong exception
//	 *         is thrown
//	 */
//	@Test
//	public void testShortestCostPathUnreachable() {
//		try {
//			DijkstraGraph<String, Double> graph = testGraph();
//			double shortestPath1 = graph.shortestPathCost("D", "J");
//			assertEquals(Double.MAX_VALUE, shortestPath1);
//		} catch (NoSuchElementException e) {
//		} catch (Exception e) {
//			fail("should have thrown nosuchelement");
//		}
//	}
//}
