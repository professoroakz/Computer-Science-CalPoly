import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class DNADecoder {

          File fastaFile;
          File gffFile;
          Scanner fastaScanner;
          Scanner gffScanner;
          PrintWriter writer;
          String outputFilename;

          String seq = "";

          int gcCount;
          int nCount;

          Gene gene;
          ArrayList<Gene> genes;

          public DNADecoder(String fileName) {
               gcCount = 0;
               nCount = 0;

               outputFilename = fileName;
               genes = new ArrayList<Gene>();
          }

          public boolean readFiles(File fastaFile, File gffFile) {
               this.fastaFile = fastaFile;
               this.gffFile = gffFile;

               try {
                    fastaScanner = new Scanner(this.fastaFile);
               }
               catch(FileNotFoundException e) {
                    System.out.println("FASTA file " + fastaFile.getName() + " is missing.");
                    return false;
               }

               try {
                    gffScanner = new Scanner(this.gffFile);
               }
               catch(FileNotFoundException e) {
                    System.out.println("GFF file " + gffFile.getName() + " is missing.");
                    return false;
               }

               try {
                    writer = new PrintWriter(outputFilename);
               }
               catch(FileNotFoundException e) {
                    System.out.println("Error when writing to " + outputFilename);
                    return false;
               }

               fastaScanner.nextLine(); // Scan the first line, but we don't care about it
               while(fastaScanner.hasNextLine()) {
                    seq += fastaScanner.nextLine();
               }

               return true;

          }

          public void decodeDNA() {
               boolean addedCurrentGene = false;

               String gffLine;
               String geneLinePieces[];
               String currentGeneAttribute;
               String currentGeneId;

               writer.println("Gene #,Gene size,CDS size,Intron size,Exon size,Ratio of CDS/Gene size,Relative Gene Coverage,Gene Density");

               gene = new Gene();

               gffLine = gffScanner.nextLine();
               // split up the line by the tab-deliminations
               geneLinePieces = gffLine.split("\t");
               // System.out.println(geneLinePieces[geneLinePieces.length - 1]);

               checkFeature(geneLinePieces);

               currentGeneId = findNewGeneId(geneLinePieces[geneLinePieces.length -1]);
               gene.setGeneId(currentGeneId);
               // System.out.println(currentGeneId);
               // System.out.println(currentGeneId.substring(0, currentGeneId.length() - 1));
               
               while(gffScanner.hasNextLine()) {
                    gffLine = gffScanner.nextLine();
                    geneLinePieces = gffLine.split("\t");

                    // check if we are still looking at the current gene-variant
                    if(gffLine.contains(currentGeneId)) {
                         checkFeature(geneLinePieces);
                    }
                    // otherwise, we're either on a new gene, or new gene-variant
                    else {
                         // now that the current gene ended,
                         // we need to add it to our genes
                         genes.add(gene);
                         addedCurrentGene = true;

                         currentGeneId = findNewGeneId(geneLinePieces[geneLinePieces.length - 1]);
                         // if this isn't a variant, we are on a new Gene
                         if(!isVariantOfGene(currentGeneId)) {
                              gene = new Gene();
                              addedCurrentGene = false;
                              gene.setGeneId(currentGeneId);
                              checkFeature(geneLinePieces);
                         }
                         // otherwise, it is a variant, and we want to ignore until we find one that isn't
                         else {
                              // need to loop until the actual next gene
                              while(gffScanner.hasNextLine()) {
                                   gffLine = gffScanner.nextLine();
                                   geneLinePieces = gffLine.split("\t");

                                   // we need to check if we are on a new gene, or a variant of another one
                                   if(!gffLine.contains(currentGeneId.substring(0, currentGeneId.length() - 1))) {
                                        currentGeneId = findNewGeneId(geneLinePieces[geneLinePieces.length - 1]);
                                        // check if we're looking at a variant of an existant gene
                                        if(!isVariantOfGene(currentGeneId)) {
                                             // System.out.println(currentGeneId);
                                             break;
                                        }
                                        else {
                                             // this is a variant of a gene we have already processed
                                             // so we want to keep going again
                                        }
                                   }
                              }

                              // check if we failed while-condition, or broke out
                              if(gffScanner.hasNextLine()) {
                                   gene = new Gene();
                                   addedCurrentGene = false;
                                   // need to check the first line
                                   checkFeature(geneLinePieces);
                                   gffLine = gffScanner.nextLine();
                                   geneLinePieces = gffLine.split("\t");
                                   currentGeneId = findNewGeneId(geneLinePieces[geneLinePieces.length - 1]);
                                   // then get the second line before continuing
                                   gene.setGeneId(currentGeneId);
                                   checkFeature(geneLinePieces);
                              }
                              else {
                                   // otherwise, we finished the file, so don't worry, we'll pop out completely
                              }
                         }
                         
                    }
               }

               if(!addedCurrentGene) {
                    genes.add(gene);
               }

               // start doing math things and printing here
               doMathAndOutputs();

               try {
                    writer.close();
                    System.out.println("Congratulations! Your file is successfully downloaded to '" + outputFilename + "'");
               }
               catch(Exception e) {
                    // don't do anything
               }

          }

          private void doMathAndOutputs() {
               int geneNum = 1;
               for(Gene g : genes) {
                    // if(geneNum == 9) System.out.println(g.getGeneId() + " - " + g.getCodingSequenceSize());

                    // Gene number
                    writer.print("Gene " + geneNum + ",");
                    // Gene size
                    writer.print(g.getGeneSize() + " nucleotides,");
                    // CDS size
                    writer.print(g.getCodingSequenceSize() + " nucleotides,");
                    // Intron size
                    writer.print(g.getIntronSize() + " nucleotides,");
                    // Exon size
                    writer.print(g.getExonSize() + " nucleotides,");
                    // CDS / Gene Size ratio
                    writer.print(getCDSToGeneSizeRatio(g) + ",");
                    // Relative Gene Coverage
                    writer.print(getRelativeGeneCoverage() + "%,");
                    // Gene Density
                    writer.println(getGeneDensity());
                    geneNum++;
               }
          }

          private String getCDSToGeneSizeRatio(Gene g) {
               int cds = g.getCodingSequenceSize();
               int geneSize = g.getGeneSize();

               DecimalFormat df = new DecimalFormat("#.#");

               return df.format((double)cds / geneSize);
          }

          private String getRelativeGeneCoverage() {
               int numGenes = genes.size();
               double avgGeneSize;
               double retNum;

               int geneNucleotides = 0;

               for(Gene g : genes) {
                    geneNucleotides += g.getGeneSize();
               }

               avgGeneSize = (double)geneNucleotides / numGenes;

               retNum = avgGeneSize / seq.length();

               return String.format("%.4f", (float)retNum);
          }

          private String getGeneDensity() {
               int numGenes = genes.size();

               DecimalFormat df = new DecimalFormat("#.#");

               return df.format((double)numGenes / ((double)seq.length() / 10000));

               // return df.format(((double)numGenes / seq.length()) * 10000);
          }

          private boolean isVariantOfGene(String id) {
               // do this to be able to check variants
               id = id.substring(0, id.length() - 1);

               for(Gene g : genes) {
                    if(g.getGeneId().contains(id)) {
                         return true;
                    }
               }

               return false;
          }

          private String findNewGeneId(String attribute) {
               // bring the start of the string to the correct location for the actual geneId
               attribute = attribute.substring(attribute.indexOf(" ") + 3);

               return attribute.substring(0, attribute.indexOf("\""));
          }

          private void checkFeature(String[] pieces) {
               String feature = pieces[2];
               int start = Integer.parseInt(pieces[3]);
               int end = Integer.parseInt(pieces[4]);

               if(feature.contains("stop")) {
                    // if this gene is showing 'stop' last
                    if(gene.getLowestNucleotide() == -1) {
                         gene.setLowestNucleotide(start);
                    }
                    else {
                         gene.setHighestNucleotide(end);
                    }
               }
               else if(feature.contains("CDS")) {
                    // System.out.println("end: " + end + " - start: " + start);
                    gene.incrementCodingSequenceSize(end - start + 1);
               }
               else if(feature.contains("exon")) {
                    // System.out.println("end: " + end + " - start: " + start);
                    gene.incrementExonSize(end - start + 1);
               }
               // otherwise it's a start_codon
               else {
                    // if this gene is showing 'start' first
                    if(gene.getLowestNucleotide() == -1) {
                         gene.setLowestNucleotide(start);
                    }
                    else {
                         gene.setHighestNucleotide(end);                         
                    }
               }
          }
}
