import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SuffixTree {

public Node root;
public char[] DNASequence;
public final List<Character> DNAAlphabet = new ArrayList<Character>();

    public SuffixTree(String s){
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

        for(int currentStartPosition = 0; currentStartPosition < currentEndPosition; currentStartPosition++) {
            currentParentChild = currentParent.nodeMap.get(DNASequence[currentStartPosition]);
            // If the letter already is existent as a child node
            if(currentParentChild != null) {
                delta = 1;
                while(DNASequence[currentParentChild.getStartIndex() + delta] == DNASequence[currentStartPosition + delta]) {
                    delta++;
                }

                currentInternalNode = new Node(
                    currentParent,
                    currentParentChild.getStartIndex(),
                    currentParentChild.getStartIndex() + delta - 1);

                currentParent.nodeMap.put(DNASequence[currentStartPosition], currentInternalNode);

                currentParentChild.setStartIndex(currentParentChild.getStartIndex() + delta);

                currentParentChild.parent = currentInternalNode;

                currentInternalNode.nodeMap.put(DNASequence[currentParentChild.getStartIndex()], currentParentChild);

            } else {
                currentNode = new Node(currentParent, currentStartPosition, currentEndPosition);
                currentParent.nodeMap.put(DNASequence[currentStartPosition], currentNode);
            }

            // if(DNAAlphabet.contains(DNASequence[currentStartPosition])) {
            //     root.nodeMap.put(DNASequence[currentStartPosition], currentNode);
            // } else {
            //     try {
            //         specialCaseChar = specialCharacters(DNASequence[currentStartPosition]);

            //     } catch(IllegalArgumentException e) {
            //         System.out.println("Illegal char found in seq at pos: " + currentStartPosition);
            //         System.exit(0);
            //     }
            // }
        }

    }

    public char[] specialCharacters(char c) throws IllegalArgumentException {
        char[] retChar = new char[2];
        switch(c) {
            case 'W': retChar[0] = 'A'; retChar[1] = 'T'; break;
            case 'M': retChar[0] = 'A'; retChar[1] = 'C'; break;
            case 'S': retChar[0] = 'C'; retChar[1] = 'G'; break;
            case 'K': retChar[0] = 'G'; retChar[1] = 'T'; break;
            case 'R': retChar[0] = 'A'; retChar[1] = 'G'; break;
            case 'Y': retChar[0] = 'C'; retChar[1] = 'T'; break;
            default: throw new IllegalArgumentException("Something strange happend");
        }

        return retChar;
    }

    /* Leaf node */
    private class Node {
        private Node parent;
        private int startIndex;
        private int endIndex;
        public HashMap<Character, Node> nodeMap = new HashMap<Character, Node>();

        private Node(Node parent, int startIndex, int endIndex) {
            this.parent = parent;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public void setStartIndex(int index){
            this.startIndex = index;
        }

        public int getStartIndex(){
            return startIndex;
        }

        public void setEndIndex(int index){
            this.startIndex = index;
        }

        public int getEndIndex(){
            return endIndex;
        }

        public void setParent(Node newParent){
            this.parent = newParent;
        }

        public Node getParent(){
            return parent;
        }
    }
}
