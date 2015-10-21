import java.util.HashMap;

public class SuffixTree {

public Node root;

    public SuffixTree(){
        root = new Node(null, -1, -1);
        Inputfile input = new inputFile();
    }

    public void constructTree() {

    }

    private enum DNAAlphabet {
        A, C, G, T, U, $
    }
    /* Leaf node */
    private class Node {
        private Node parent;
        private int startIndex;
        private int endIndex;
        public HashMap<DNAAlphabet, Node> nodeMap = new HashMap<DNAAlphabet, Node>();

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