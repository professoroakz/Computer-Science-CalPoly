import java.lang.Math;

public class Gene {
     private int geneSize;
     private int codingSequenceSize;
     private int exonSize;

     private String geneId;

     private int lowestNucleotide;
     private int highestNucleotide;

     public Gene() {
          geneSize = 0;
          codingSequenceSize = 0;
          exonSize = 0;
     }

     public int getGeneSize() {
          // add one because math needs it
          // and use Math.abs because it's possible things could be reversed
          return Math.abs(highestNucleotide - lowestNucleotide) + 1;
          // return geneSize();
     }

     public int getCodingSequenceSize() {
          return codingSequenceSize;
     }

     public int getIntronSize() {
          return geneSize - exonSize;
     }

     public int getExonSize() {
          return exonSize;
     }

     public void incrementGeneSize(int nextIncrement) {
          geneSize += nextIncrement;
     }

     public void incrementCodingSequenceSize(int nextIncrement) {
          codingSequenceSize += nextIncrement;
     }

     public void incrementExonSize(int nextIncrement) {
          exonSize += exonSize;
     }

     public void setLowestNucleotide(int value) {
          lowestNucleotide = value;
     }

     public void setHighestNucleotide(int value) {
          highestNucleotide = value;
     }

     public String getGeneId() {
          return geneId;
     }

     public void setGeneId(String id) {
          geneId = id;
     }
}
