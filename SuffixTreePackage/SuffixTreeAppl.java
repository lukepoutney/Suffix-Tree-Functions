package SuffixTreePackage;

/**
 * Class with methods for carrying out applications of suffix trees
 * David Manlove, Jan 03.  Modified by David Manlove, Jan 07 and Jan 09.
 */

public class SuffixTreeAppl {

	/** The suffix tree */
	private SuffixTree t;

	/**
	 * Default constructor.
	 */
	public SuffixTreeAppl () {
		t = null;
	}
	
	/**
	 * Constructor with parameter.
	 * 
	 * @param tree the suffix tree
	 */
	public SuffixTreeAppl (SuffixTree tree) {
		t = tree;
	}
	
	/**
	 * Search the suffix tree t representing string s for a target x.
	 * Stores -1 in Task1Info.pos if x is not a substring of s,
	 * otherwise stores p in Task1Info.pos such that x occurs in s
	 * starting at s[p] (p counts from 0)
	 * - assumes that characters of s and x occupy positions 0 onwards
	 * 
	 * @param x the target string to search for
	 * 
	 * @return a Task1Info object
	 */
	public Task1Info searchSuffixTree(byte[] x) {
		
		Task1Info task1 = new Task1Info(); 
		int pos, j, len;
		SuffixTreeNode current;
		pos = 0;  // position in x
		current = t.getRoot();
		len = x.length-1;
		
		while (true) {
			// search for child which has left edge label equal to our current position in x
			current = t.searchList(current.getChild(), x[pos]);
			
			// if no matches are found terminate unsuccessfully
			// otherwise if all characters have been matched terminate successfully
			// otherwise continue to check values between left edge label of next node
			if (current == null) {
				task1.setPos(-1);
				return task1;
			} else if (pos == len) {
				task1.setPos(current.getLeftLabel()-len);
				task1.setMatchNode(current);
				return task1;
			}
			else {
				// move to next character and label to continue checking edge
				j = current.getLeftLabel() + 1;
				pos++;
				
				// enters only if the edge above node has multiple characters
				// terminates when string found or edge label matches fully
				while (j <= current.getRightLabel()) {
					if (t.getString()[j] == x[pos]) {
						if (pos == len) {
							task1.setMatchNode(current);
							task1.setPos(j-len);
							return task1;
						} else {
							j++;
							pos++;
						}
					}
					else {
						task1.setPos(-1);
						return task1;
					}
				}
				// succeeded in matching whole edge, go further down tree
			}
		}
	}
	
	/**
	 * Search suffix tree t representing string s for all occurrences of target x.
	 * Stores in Task2Info.positions a linked list of all such occurrences.
	 * Each occurrence is specified by a starting position index in s
	 * (as in searchSuffixTree above).  The linked list is empty if there
	 * are no occurrences of x in s.
	 * - assumes that characters of s and x occupy positions 0 onwards
	 * 
	 * @param x the target string to search for
	 * 
	 * @return a Task2Info object
	 */
	public Task2Info allOccurrences(byte[] x) {
		
		SuffixTreeAppl suffixTreeAppl = new SuffixTreeAppl(t);
		Task1Info task1 = suffixTreeAppl.searchSuffixTree(x);

		// matching suffix node means all leaf nodes below are matches
		SuffixTreeNode start = task1.getMatchNode();

		Task2Info task2 = new Task2Info();
		if (start == null) {
			// if no match exists
			return task2;
		} else if (start.getSuffix() == -1) {
			// if multiple instances exist call recursive function to count it 
			task2 = recLeafCount(start.getChild(), task2);
		} else {
			// if string is unique return its position
			task2.addEntry(start.getSuffix());
		}
		return task2;
	}
	
	// return a task 2 object with positions of every occurrence of a leaf node
	public Task2Info recLeafCount(SuffixTreeNode current, Task2Info task2) {
		Task2Info empty = new Task2Info();
		// return an empty object to the previous call if no node exists
		// otherwise is node is a non leaf so we check its child
		// otherwise add an entry
		if (current == null) {
			return empty;
		} else if (current.getSuffix() == -1 ) {
			recLeafCount(current.getChild(), task2);
		} else {
			task2.addEntry(current.getSuffix());
		}
		// repeat process on sibling node
		recLeafCount(current.getSibling(), task2);
		return task2;
	}

	/**
	 * Traverses suffix tree t representing string s and stores ln, p1 and
	 * p2 in Task3Info.len, Task3Info.pos1 and Task3Info.pos2 respectively,
	 * so that s[p1..p1+ln-1] = s[p2..p2+ln-1], with ln maximal;
	 * i.e., finds two embeddings of a longest repeated substring of s
	 * - assumes that characters of s occupy positions 0 onwards
	 * so that p1 and p2 count from 0
	 * 
	 * @return a Task3Info object
	 */	
	public Task3Info traverseForLrs () {
		
		Task3Info task3 = new Task3Info();
		SuffixTreeNode root = t.getRoot();
		
		// if 0 children from root
		if (root.getChild() == null) {
			return task3;
		}
		task3 = recFindLrs(task3, root, 0);
		return task3;
	}
	
	// recursively check for a node with 2 or more children with maximum depth
	public Task3Info recFindLrs(Task3Info task3, SuffixTreeNode current, int depth) {
		SuffixTreeNode child = current.getChild();
		
		// case for only 1 child leaf
		if (child.getSuffix() != -1 && child.getSibling() == null ) {
			// not a repeated substring so we return
			return task3;
		// case for 1 child non leaf
		} else if (child.getSuffix() == -1 && child.getSibling() == null ){
			// node could have a repeated substring below it so continue downward
			depth += current.getRightLabel() - current.getLeftLabel() + 1;
			task3 = recFindLrs(task3, child, depth);
		} else {
			// if more than 1 child update depth
			depth += current.getRightLabel() - current.getLeftLabel() + 1;
			
			// if child is non leaf continue downward
			// otherwise check its sibling
			while (child != null) {
				if (child.getSuffix() == -1 ) {
					task3 = recFindLrs(task3, child, depth);	
				}
				child = child.getSibling();
				
			}
			// check if depth is more than current length
			// if so replace length and occurrence locations
			if (task3.getLen() < depth) {
				task3.setLen(depth-1);
				task3.setPos1(current.getChild().getLeftLabel()-task3.getLen());
				task3.setPos2(current.getChild().getSibling().getLeftLabel()-task3.getLen());
			}
					
		}
		return task3;
	}

	/**
	 * Traverse generalised suffix tree t representing strings s1 (of length
	 * s1Length), and s2, and store ln, p1 and p2 in Task4Info.len,
	 * Task4Info.pos1 and Task4Info.pos2 respectively, so that
	 * s1[p1..p1+ln-1] = s2[p2..p2+ln-1], with len maximal;
	 * i.e., finds embeddings in s1 and s2 of a longest common substring 
         * of s1 and s2
	 * - assumes that characters of s1 and s2 occupy positions 0 onwards
	 * so that p1 and p2 count from 0
	 * 
	 * @param s1Length the length of s1
	 * 
	 * @return a Task4Info object
	 */
	public Task4Info traverseForLcs (int s1Length) {
		Task4Info task4 = new Task4Info();
		SuffixTreeNode root = t.getRoot();
		
		// if 0 children from root
		if (root.getChild() == null) {
			return task4;
		}
		task4 = recFindLcs(task4, root, 0, s1Length+1);
		return task4;
	}
	
	// recursively check for a node with 2 or more children from opposing strings with maximum depth
	public Task4Info recFindLcs(Task4Info task4, SuffixTreeNode current, int depth, int splitPoint) {
		SuffixTreeNode child = current.getChild();
		
		// case for only 1 child leaf
		if (child.getSuffix() != -1 && child.getSibling() == null ) {
			// not a repeated substring so we return
			return task4;
		// case for 1 child non leaf
		} else if (child.getSuffix() == -1 && child.getSibling() == null ){
			// could have a substring below it so continue downward
			depth += current.getRightLabel() - current.getLeftLabel() + 1;
			task4 = recFindLcs(task4, child, depth, splitPoint);
		} else {
			// if more than 1 child update depth
			depth += current.getRightLabel() - current.getLeftLabel() + 1;
			task4.setString1Leaf(false);
			task4.setString2Leaf(false);	
			int s1Location = 0;
			int s2Location = 0;

			// if child is non leaf continue downward
			// otherwise check its sibling
			while (child != null) {
				if (child.getSuffix() == -1 ) {
					task4 = recFindLcs(task4, child, depth, splitPoint);	
				}
				// update booleans to keep track of the node
				//and also note the edge for which it holds true
				if (child.getLeftLabel() <= splitPoint) {
					task4.setString1Leaf(true);
					s1Location = child.getLeftLabel();
				} else {
					task4.setString2Leaf(true);
					s2Location = child.getLeftLabel();
				}
				child = child.getSibling();
				
			}
			// check if depth is more than current length
			// if so replace length and occurrence locations
			if (task4.getString1Leaf() == true && task4.getString2Leaf() == true && task4.getLen() < depth) {
				task4.setLen(depth-1);
				task4.setPos1(s1Location-task4.getLen());
				task4.setPos2(s2Location-task4.getLen()-splitPoint);
			}
					
		}
		return task4;
	}
	
}
