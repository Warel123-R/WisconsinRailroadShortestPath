//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class AlgorithmEngineerTests {

	private RailroadInterface<String, Double> graph = null;

	/**
	 * graph for test, built similar to ones in lecture on a larger scale.
	 */
	@BeforeEach
	public void testGraph() {
		/**
		 * tester graph:
		 *        A
		 *       / \
		 *     2/   \3
		 *     /     \
		 *    C---4---D
		 *   / \     / \
		 * 5/   \<6>/   \5
		 * /     \ /     \
		 * B---3--F---2---E
		 *  \            /
		 *  7\          /4
		 *    \        /
		 *     G------H
		 *        3
		 *
		 *
		 *        J <- (unreachable)
		 */
		graph = new RailroadAE<String, Double>();
		graph.insertNode("A");
		graph.insertNode("B");
		graph.insertNode("C");
		graph.insertNode("D");
		graph.insertNode("E");
		graph.insertNode("F");
		graph.insertNode("G");
		graph.insertNode("H");
		graph.insertNode("J"); // unreachable node

		graph.insertEdge("A", "C", 2.0);
		graph.insertEdge("C", "A", 2.0);

		graph.insertEdge("A", "D", 3.0);
		graph.insertEdge("D", "A", 3.0);

		graph.insertEdge("B", "C", 5.0);
		graph.insertEdge("C", "B", 5.0);

		graph.insertEdge("B", "F", 3.0);
		graph.insertEdge("F", "B", 3.0);

		graph.insertEdge("B", "G", 7.0);
		graph.insertEdge("G", "B", 7.0);

		graph.insertEdge("C", "D", 4.0);
		graph.insertEdge("D", "C", 4.0);

		graph.insertEdge("C", "F", 6.0);
		graph.insertEdge("F", "C", 6.0);

		graph.insertEdge("D", "E", 5.0);
		graph.insertEdge("E", "D", 5.0);

		graph.insertEdge("D", "F", 6.0);
		graph.insertEdge("F", "D", 6.0);

		graph.insertEdge("E", "F", 2.0);
		graph.insertEdge("F", "E", 2.0);

		graph.insertEdge("E", "H", 4.0);
		graph.insertEdge("H", "E", 4.0);

		graph.insertEdge("G", "H", 3.0);
		graph.insertEdge("H", "G", 3.0);
	}

	/*
	 * getSize test
	 */
	@Test
	public void test1() {
		try {
			//checks for correct size
			assertEquals(9, graph.getSize());

			//checks size method after removal
			graph.removeNode("D");
			assertEquals(8, graph.getSize());

			//checks size method after remove edges instead of a node
			graph.removeEdge("G", "H");
			graph.removeEdge("H", "G");
			graph.removeEdge("B", "G");
			graph.removeEdge("G", "B");
			assertEquals(8, graph.getSize());


		} catch (Exception e) {
			fail();
		}
	}

	/*
	 * getRoute test
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void test2() {
		try {

			List<String> shortestPath1 = (List<String>) graph.getRoute("D", "H");
			assertEquals("[D, E, H]", shortestPath1.toString()); // 9.0
			List<String> shortestPath2 = (List<String>) graph.getRoute("A", "F");
			assertEquals("[A, C, F]", shortestPath2.toString()); // 8.0
			List<String> shortestPath3 = (List<String>) graph.getRoute("E", "H");
			assertEquals("[E, H]", shortestPath3.toString()); // 4.0
			List<String> shortestPath4 = (List<String>) graph.getRoute("B", "H");
			assertEquals("[B, F, E, H]", shortestPath4.toString()); // 9.0
			List<String> shortestPath5 = (List<String>) graph.getRoute("B", "E");
			assertEquals("[B, F, E]", shortestPath5.toString()); // 5.0
			List<String> shortestPath6 = (List<String>) graph.getRoute("C", "H");
			assertEquals("[C, F, E, H]", shortestPath6.toString()); // 12.0

		} catch (Exception e) {
			fail();
		}
	}

	/*
	 * getRouteWithStops test
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test3() {
		try {

			List<String> shortestPath1 = (List<String>)
					graph.getRouteWithStops("D", new ArrayList<String>(Arrays.asList("F", "E", "H")));
			assertEquals("[D, F, E, H]", shortestPath1.toString());

			List<String> shortestPath2 = (List<String>)
					graph.getRouteWithStops("A", new ArrayList<String>(Arrays.asList("B", "E", "G")));
			assertEquals("[A, C, B, F, E, H, G]", shortestPath2.toString());

			List<String> shortestPath3 = (List<String>)
					graph.getRouteWithStops("F", new ArrayList<String>(Arrays.asList("E", "H", "G", "B", "C", "A", "D", "E")));
			assertEquals("[F, E, H, G, B, C, A, D, E]", shortestPath3.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/*
	 * getDistance test
	 */
	@Test
	public void test4() {
		try {

			assertEquals(9, graph.getDistance("D", "H"));
			assertEquals(8, graph.getDistance("A", "F"));
			assertEquals(4, graph.getDistance("E", "H"));
			assertEquals(9, graph.getDistance("B", "H"));
			assertEquals(5, graph.getDistance("B", "E"));
			assertEquals(12, graph.getDistance("C", "H"));

		} catch (Exception e) {
			fail();
		}
	}

	/*
	 * invalid test
	 */
	@Test
	public void test5() {
		try {

			//testing route and routewithstops with unreachable node

			try {
				graph.getRoute("D", "J");
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			try { //unreachable node as first destination
				graph.getRouteWithStops("D", new ArrayList<String>(Arrays.asList("J", "E", "H")));
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			try { //unreachable node as second destination
				graph.getRouteWithStops("D", new ArrayList<String>(Arrays.asList("F", "J", "H")));
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			try { //unreachable node as last destination
				graph.getRouteWithStops("D", new ArrayList<String>(Arrays.asList("F", "E", "J")));
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			try { //unreachable node as location for getRoutWithStops
				graph.getRouteWithStops("J", new ArrayList<String>(Arrays.asList("F", "E", "H")));
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			try { //unreachable node as location for getRout
				graph.getRouteWithStops("J", new ArrayList<String>(Arrays.asList("F")));
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			//testing distance with unreachable node

			try { //unreachable node as location for getDistance
				graph.getDistance("J", "F");
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

			try { //unreachable node as destination for getDistance
				graph.getDistance("D", "j");
				fail("should have thrown nosuchelement");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				fail("should have thrown nosuchelement");
			}

		} catch (Exception e) {
			fail();
		}
	}

	/*

	 * data wrangler test 1, adding a duplicate

	 */

	@Test

	public void CodeReviewOfDataWranglerTest1() {

		try {

			loadStationDW ls = new loadStationDW();

			railNetworkDW<StationDW, Double> networkloaded = null;

			networkloaded = ls.loadRailroad("DataWranglerTestfile.dot");

//duplicate station

			StationDW duplicateStation = new StationDW("Milwaukee", 43.04, -87.91);

			try {

				networkloaded.addStation(duplicateStation);

			} catch (NoSuchElementException e) {

				fail("since it is a duplicate, it should be found");

			}

			if (networkloaded.getNodeCount() != 14) {

				fail("should be the same as before since duplicates dont get added");

			}

		} catch (Exception e) {

			e.printStackTrace();

			fail("shouldnt have been an exception");

		}


	}


	/*

	 * data wrangler test 2

	 */

	@Test

	public void CodeReviewOfDataWranglerTest2() {

		try {

			loadStationDW ls = new loadStationDW();

			railNetworkDW<StationDW, Double> networkloaded = null;

			networkloaded = ls.loadRailroad("DataWranglerTestfile.dot");


//getting a station with non-existent

			StationDW nonExistentStation = networkloaded.getStation("NonExistent");

			if (nonExistentStation != null) {

				fail("should return null for non-existent station");

			}

		} catch (Exception e) {

			e.printStackTrace();

			fail("shouldnt have been an exception");

		}

	}
}
