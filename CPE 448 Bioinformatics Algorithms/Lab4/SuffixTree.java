import java.util.HashMap;

public class SuffixTree {

public Node root;
public char[] DNASequence;
public final List<Character> DNAAlphabet = new ArrayList<Character>('A', 'C', 'G', 'T', '$');

    public SuffixTree(String s){
        root = new Node(null, -1, -1);
        DNASequence = s.toCharArray();
    }

    public void constructTree() {
        Node currentParent = root;
        int currentEndPosition = DNASequence.length;
        char[] specialCaseChar;

        for(int currentStartPosition = 0; currentStartPosition < currentEndPosition; currentStartPosition++) {
            Node currentNode = new Node(currentParent, currentStartPosition, currentEndPosition);

            if(DNAAlphabet.contains(DNASequence[currentStartPosition])) {
                root.nodeMap.put(DNASequence[currentStartPosition], currentNode);
            } else {
                try {
                    specialCaseChar = specialCharacters(DNASequence[currentStartPosition]);

                } catch(IllegalArgumentException e) {
                    System.out.println("Illegal char found in seq at pos: " + currentStartPosition);
                    System.exit(0);
                }
            }
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