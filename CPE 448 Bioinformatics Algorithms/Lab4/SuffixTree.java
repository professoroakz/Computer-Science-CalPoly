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
        root.setInsertedOrder(0);
        DNASequence = s.toCharArray();
        DNAAlphabet.add('A');
        insertRank = 1;
    }

    public void constructTree() {
    	
    	endPos = DNASequence.length;
    	for(startPos = 0; startPos < endPos; startPos++) {
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
    		if(curParentChild.isInternalNode == true && delta > curParentChild.getEndIndex()) {
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

    public char[] specialCharacters(char c) throws IllegalArgumentException {
        char[] retChar = new char[2];
        switch (c) {
        case 'W':
            retChar[0] = 'A';
            retChar[1] = 'T';
            break;
        case 'M':
            retChar[0] = 'A';
            retChar[1] = 'C';
            break;
        case 'S':
            retChar[0] = 'C';
            retChar[1] = 'G';
            break;
        case 'K':
            retChar[0] = 'G';
            retChar[1] = 'T';
            break;
        case 'R':
            retChar[0] = 'A';
            retChar[1] = 'G';
            break;
        case 'Y':
            retChar[0] = 'C';
            retChar[1] = 'T';
            break;
        default:
            throw new IllegalArgumentException("Something strange happend");
        }

        return retChar;
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
