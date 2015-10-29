import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SuffixTree {

    public Node root;
    public char[] DNASequence;
    public final List<Character> DNAAlphabet = new ArrayList<Character>();
    public Node curParent, curInternalNode, curParentChild; 
    public int suffixMatchCount, insertRank, startPos, delta, endPos; //startPos and endPos will assume array index notation , delta represents 
    									//distance away from beginning of child node that first conflict was found

    public SuffixTree(String s) {
        root = new Node(null, -1, -1);
        root.setInsertedOrder(-2);
        DNASequence = s.toCharArray();
        DNAAlphabet.add('A');
        insertRank = 0;
    }

    public void constructTree() {
    	
    	endPos = DNASequence.length;
    	for(startPos = 0; startPos < endPos; startPos++) {
    		System.out.println( (startPos + 1)+ " rounds of construct called!!!!");
    		traverseTree(startPos);
    		insertNode();
    		insertRank++;
    	}

    }
	// insertNode() should always be called after a call to traverseTree(). traverseTree() sets up the global class variables
	//to correct values for inserting a new node. Then insertNode() will handle the actual insertion. And modification of node
	// properties. insertNode() does not alter any global class variables in any way.
    public void insertNode() {
        if(root == null) throw new IllegalArgumentException("Root cannot be null!");
        System.out.println("Calling insertNode()");
        Node currentNode;

        if(curParentChild != null) {
        	// all nodes created inside are internal nodes
        	System.out.println("curParent INFO "); 
        	curParent.printSelf();
        	currentNode = new Node(curParent, curParentChild.getStartIndex(), curParentChild.getStartIndex() + delta - 1); //may not need plus 1
        	currentNode.setInternalNodeStatus(true);
        	System.out.println("Creating an internal Node with properties");
        	currentNode.printSelf();
        	currentNode.nodeMap.put(DNASequence[curParentChild.getStartIndex() + delta], curParentChild);
        	curParent.nodeMap.put(DNASequence[currentNode.getStartIndex()], currentNode);
        	curParent = currentNode;
        	curParentChild.setParent(currentNode);
        	curParentChild.setStartIndex(currentNode.getEndIndex() + 1);
        	System.out.println("startPos and curParent starting index: " + startPos + " " + curParent.getStartIndex());
        	curParentChild = curParent.nodeMap.get(DNASequence[startPos + suffixMatchCount]);
        }
        //All nodes created outside the while loop are leaf nodes.
        currentNode = new Node(curParent, startPos + suffixMatchCount, endPos - 1); //curParent.getEndIndex() + delta + 1
        currentNode.setInsertedOrder(insertRank);
        System.out.println("The value being produced on a array index: " + (startPos + suffixMatchCount) + " Delta: " + delta);
        curParent.nodeMap.put(DNASequence[startPos + suffixMatchCount], currentNode);
        System.out.println("Created a leaf node with properties #1");
        currentNode.printSelf();
    }

    //traverseTree takes a given suffix, given through an ints, and then traverses an existing
    //suffix tree to the correct spot in the tree for inserting a leaf node and/or internal node.
    //performs traversal only. Traversal is recorded through global class variables.
    public void traverseTree(int start) { //parameter start should be assumed to using array index notation.
    	System.out.println("SEARCHING for pos to put suffix starting at: " + start + "//////////////////////////////////////////");
    	suffixMatchCount = 0;
    	curParent = root;
    	endPos = DNASequence.length;
		curParentChild = curParent.nodeMap.get(DNASequence[start]); //may assign null
		System.out.println("DNA char retrieved from first child assignement: " + start + " " + DNASequence[start]);
    	delta = 0;

    	while(curParentChild != null) {
    		System.out.println("child is not null");
			System.out.println("First comp: " + (curParentChild.getStartIndex() + delta) + " Second Comp: " + (start + delta));
    		while(DNASequence[curParentChild.getStartIndex() + delta] == DNASequence[start + delta]) {
    			System.out.println("First comp: " + (curParentChild.getStartIndex() + delta) + " Second Comp: " + (start + delta));
    			System.out.println(delta);
    			if(curParentChild.getStartIndex() + delta > curParentChild.getEndIndex()) {
    				System.out.println("delta oversteps child bounds");
    				break;
    			}
    			suffixMatchCount++;
    			delta++;
    		}
    		if(curParentChild.isInternalNode == true && curParentChild.getStartIndex() + delta > curParentChild.getEndIndex()) {
    			System.out.println("child is internal node");
    			curParent = curParentChild;
    			curParentChild = curParent.nodeMap.get(DNASequence[start + delta]); //may assign null
    			start += delta;
    			delta = 0;
    		}
    		else {
                break;
            }
    	}
    	System.out.println("Delta: " + delta + " , curParent: " + curParent.getInsertedOrder() + " and internal status: "
    			+ curParent.getInternalNodeStatus() + " start/end: " + curParent.getStartIndex() + "/" + curParent.getEndIndex());
    	if(curParentChild != null)
    		System.out.println(", curParentChild: " + curParentChild.getInsertedOrder());
    	else System.out.println("child is null at finish");
    	System.out.println();
    }

    public int[] findStartPositions(String query) {
          Node currentParent, currentChild;
          ArrayList<Integer> retVals = new ArrayList<Integer>();
          int startCompare, endCompare;
          HashMap<Character, Node> currentParentsChildrenMap;

          int queryPosition = 0;

          currentParent = root;
          currentParentsChildrenMap = currentParent.nodeMap;

          System.out.println(queryPosition + " : " + query.charAt(queryPosition));
          currentChild = currentParentsChildrenMap.get(query.charAt(queryPosition));

          for(int i = 0; i < DNASequence.length; i++) {
            System.out.print(DNASequence[i]);
          }

          System.out.println("");

          System.out.println(query);

          while(currentChild != null) {
               startCompare = currentChild.getStartIndex();
               endCompare = currentChild.getEndIndex();

               // the +1 because we want to compare the value at the end position
               for(; startCompare < endCompare + 1; startCompare++) {
System.out.println(queryPosition + " : " + query.charAt(queryPosition) + " - " + startCompare + " : " + DNASequence[startCompare]);
                    // if the next character in the query still matches the correct value
                    if(DNASequence[startCompare] == query.charAt(queryPosition++)) {
System.out.println("check length (queryPosition, then length): " + queryPosition + " : " + query.length());
                         // if we have looked through each letter of the query successfully
                         if(queryPosition == query.length()) {
                              retVals.addAll(getLeafIndexes(currentChild));
                              return convertToArray(retVals);
                         }

                    } else {
                         // otherwise, we found a mismatch, so our query isn't here
                         return null;
                    }
               }
               
               currentParent = currentChild;
               currentParentsChildrenMap = currentParent.nodeMap;
               currentChild = currentParentsChildrenMap.get(query.charAt(queryPosition));

          }

          if(retVals.size() > 0) {
               return convertToArray(retVals);//retVals.toArray(useless);
          }

          // we didn't find any locations of this query
          return null;
     }

     private int[] convertToArray(ArrayList<Integer> vals) {
        int[] ret = new int[vals.size()];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = vals.get(i).intValue();
        }
        return ret;
     }

     public ArrayList<Integer> getLeafIndexes(Node leafAncestor) {
System.out.println("G: " + (leafAncestor.nodeMap.get('G') != null) + " - A: " + (leafAncestor.nodeMap.get('A') != null) + " - T: " + (leafAncestor.nodeMap.get('T') != null) + " - C: " + (leafAncestor.nodeMap.get('C') != null));
          ArrayList<Integer> values = new ArrayList<Integer>();

          if(!leafAncestor.getInternalNodeStatus()) {
               values.add(leafAncestor.getInsertedOrder());
          } else {
               for(Character c : leafAncestor.nodeMap.keySet()) {
                    values.addAll(getLeafIndexes(leafAncestor.nodeMap.get(c)));
               }
          }

          return values;
     }

    /* Leaf node or internal node */
    //If a leaf node, it can never have children. If an internal node it can l=only have at most 5 children.
    private class Node {
        private Node parent;
        private boolean isInternalNode;
        private int insertedOrder;
        private int startIndex; //all bounds of beginning and end are inclusive. They are part of the node.
        private int endIndex;

        public HashMap<Character, Node> nodeMap = new HashMap<Character, Node>();

        private Node(Node parent, int startIndex, int endIndex) {
            this.parent = parent;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.insertedOrder = -1;
            this.isInternalNode = false;
        }

        public void setInsertedOrder(int insertedOrder) {
            this.insertedOrder = insertedOrder;
        }

        public int getInsertedOrder() {
            return insertedOrder;
        }

        public void setInternalNodeStatus(boolean internalNodeStatus) {
            this.isInternalNode = internalNodeStatus;
        }

        public boolean getInternalNodeStatus() {
            return isInternalNode;
        }

        public void setStartIndex(int index) {
            this.startIndex = index;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setEndIndex(int index) {
            this.startIndex = index;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void setParent(Node newParent) {
            this.parent = newParent;
        }

        public Node getParent() {
            return parent;
        }
        
        public void printSelf() {
        	System.out.println("isInternalNode: " + isInternalNode + " insertedOrder: " + insertedOrder + 
        			" startIndex: " + startIndex + " endIndex: " + endIndex);
        }
        
        public int getSizeOfNode() {
        	int rtn = endIndex - startIndex + 1;
        	if(endIndex == -1 || startIndex == -1)
        		rtn = -1;
        	return rtn;
        }
    }
}
