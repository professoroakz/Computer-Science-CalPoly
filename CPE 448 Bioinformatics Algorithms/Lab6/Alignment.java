import java.util.*;

public class Alignment implements Comparable <Alignment> {
     String sequenceName;
     double score;
     int position;

     public Alignment(String sequence, double score, int pos) {
          sequenceName = sequence;
          this.score = score;
          position = pos;
     }

     @Override
     public int compareTo(Alignment otherAlignment) {
          int retVal;

          if(Double.isNaN(this.score) || Double.isNaN(otherAlignment.score)) {
               
               if(Double.isNaN(this.score)) {
                    return -1;
               } else {
                    return 1;
               }
          }

          if(this.score == otherAlignment.score) {
               retVal = 0;
          } else if(this.score > otherAlignment.score) {
               retVal = 1;
          } else {
               retVal = -1;
          }

          return retVal;
     }


}
