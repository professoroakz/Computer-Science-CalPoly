// PLEASE INSERT THIS ENTIRE DOCUMENT INTO SuffixTree.java
// THESE ARE TWO METHODS NECESSARY FOR SEARCHING THROUGH THE TREE FOR A QUERY

     public int[] findStartPositions(String query) {
          Node currentParent, currentChild;
          ArrayList<Integer> retVals = new ArrayList<Integer>();
          int startCompare, endCompare;
          HashMap<Character, Node> currentParentsChildrenMap;

          int queryPosition = 0;

          currentParent = root;
          currentParentsChildrenMap = currentParent.nodeMap;

          currentChild = currentParentsChildrenMap.get(query.charAt(queryPosition));

          while(currentChild != null) {
               startCompare = currentChild.getStartIndex();
               endCompare = currentChild.getEndIndex();

               // the +1 because we want to compare the value at the end position
               for(; startCompare < endCompare + 1; startCompare++) {
                    // if the next character in the query still matches the correct value
                    if(DNASequence[startCompare] == query.charAt(queryPosition++)) {

                         // if we have looked through each letter of the query successfully
                         if(queryPosition == query.length()) {
                              retVals.addAll(getLeafIndexes(currentChild));
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
               return retVals.toArray();
          }

          // we didn't find any locations of this query
          return null;
     }

     public ArrayList<Integer> getLeafIndexes(Node leafAncestor) {
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
