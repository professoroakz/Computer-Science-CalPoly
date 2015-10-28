import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SuffixTree {

    public Node root;
    public char[] DNASequence;
    public final List<Character> DNAAlphabet = new ArrayList<Character>();
    public Node curParent, curInternalNode, curParentChild;
    public int startPos, delta, endPos;

    public SuffixTree(String s) {
        root = new Node(null, -1, -1);
        DNASequence = s.toCharArray();
        DNAAlphabet.add('A');
    }
    
    public void constructTree() {
        Node currentParent = root;
        int currentEndPosition = DNASequence.length;
        char[] specialCaseChar;
        Node currentNode;
        Node currentInternalNode;
        // Child for a potential branch for the current node
        Node currentParentChild;
        int delta;

        // Case 1: Any of A C G T is non existent, create node with root as
        // parent
        // Case 2: A C G T is existent, create internal node
        // Case 3: A C G T is existent, internal node is existent

        for (int currentStartPosition = 0; currentStartPosition < currentEndPosition; currentStartPosition++) {
            currentParentChild = currentParent.nodeMap.get(DNASequence[currentStartPosition]);
            // If the letter already is existent as a child node
            if (currentParentChild != null) {
                while(currentParentChild.getInternalNodeStatus()) {
                    currentParent = currentParentChild;
                    currentParentChild = currentParent.nodeMap.get(DNASequence[currentStartPosition]);
                    if(currentParentChild != null){
                        break;
                    }

                    // update child indexes
                    // change intervals for all the children

             //       currentParentChild.setStartIndex(getStartIndex() - );
                }

                delta = 1;
                if(currentParentChild != null) {
                    while (DNASequence[currentParentChild.getStartIndex() + delta] == DNASequence[currentStartPosition
                        + delta]) {
                        delta++;
                }
                    }

                currentInternalNode = new Node(currentParent, // Parent
                        currentParentChild.getStartIndex(), // start position
                        currentParentChild.getStartIndex() + delta - 1); // end
                                                                            // position

                currentParent.nodeMap.put(DNASequence[currentStartPosition], currentInternalNode);

                currentParentChild.setStartIndex(currentParentChild.getStartIndex() + delta);

                currentParentChild.parent = currentInternalNode;

                currentInternalNode.nodeMap.put(DNASequence[currentParentChild.getStartIndex()], currentParentChild);

                Node nodeToInsert =
                new Node(currentInternalNode,
                        currentStartPosition + delta,
                        currentEndPosition);

                currentInternalNode.nodeMap.put(DNASequence[currentStartPosition], nodeToInsert);
                currentParent = root;
            } else {
                // Case 1
                currentNode = new Node(currentParent, currentStartPosition, currentEndPosition);
                currentParent.nodeMap.put(DNASequence[currentStartPosition], currentNode);
                currentParent = root;
            }

            // if(DNAAlphabet.contains(DNASequence[currentStartPosition])) {
            // root.nodeMap.put(DNASequence[currentStartPosition], currentNode);
            // } else {
            // try {
            // specialCaseChar =
            // specialCharacters(DNASequence[currentStartPosition]);

            // } catch(IllegalArgumentException e) {
            // System.out.println("Illegal char found in seq at pos: " +
            // currentStartPosition);
            // System.exit(0);
            // }
            // }
        }

    }

    //traverseTree takes a given suffix, given through 2 ints, and then traverses an existing 
    //suffix tree to the correct spot in the tree for inserting a leaf node and/or internal node.
    //performs traversal only.
    public void traverseTree(int start) {
    	curParent = root;
    	endPos = DNASequence.length;
		curParentChild = curParent.nodeMap.get(DNASequence[start]); //may assign null
    	delta = 0;
    	
    	while(curParentChild != null) {
    		while(DNASequence[curParentChild.getStartIndex() + delta] == DNASequence[start + delta]) {
    			if(curParentChild.getStartIndex() + delta > curParentChild.getEndIndex()) {
    				start += delta;
    				break;
    			}
    			delta++;
    		}
    		if(curParentChild.isInternalNode == true) {
    			curParent = curParentChild;
    			curParentChild = curParent.nodeMap.get(DNASequence[start + delta]); //may assign null
    			start += delta;
    			delta = 0;
    		}
    		else
    			break;
    	}
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
        private int startIndex;
        private int endIndex;

        public HashMap<Character, Node> nodeMap = new HashMap<Character, Node>();

        private Node(Node parent, int startIndex, int endIndex) {
            this.parent = parent;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
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
    }
}
