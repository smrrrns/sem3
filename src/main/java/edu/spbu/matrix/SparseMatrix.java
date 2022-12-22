package edu.spbu.matrix;

import edu.spbu.MatrixGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static edu.spbu.matrix.DenseMatrix.ifDense;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
  private int high, width;
  private HashMap<Integer,HashMap<Integer, Double>> data;
  private long hash = 0;
    private final double DELTA = 0.001;
    private static final String path = "C://Users//Соня Смирнова//IdeaProjects/java-template/";

  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) {
    Path file = Paths.get(path + fileName);

    try (Scanner obj = new Scanner(file)) {
      ArrayList<String> rows = new ArrayList<>();

      while (obj.hasNextLine())
        rows.add(obj.nextLine());

      if( rows.size() == 0 || rows.get(0).split("\\s").length == 0){
        this.high = 0;
        this.width = 0;
        this.data = null;
      }
      else {
        this.high = rows.size();
        this.width = rows.get(0).split("\\s").length;

        HashMap<Integer,HashMap<Integer, Double>> data = new HashMap<>();

        for (int i = 0; i < this.high; i++) {
          String[] numbers = rows.get(i).split("\\s");
          for (int j = 0; j < this.width; j++) {
            if(Double.parseDouble(numbers[j]) != 0) {
              //data.computeIfAbsent(i, t -> new HashMap<>());
                if(!data.containsKey(i)) data.put(i, new HashMap<>());
              data.get(i).put(j, Double.parseDouble(numbers[j]));
              this.hash += hashCode(data.get(i).get(j), i, j);
            }
          }
        }
        this.data = data;
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public SparseMatrix(){
    this.width =0;
    this.high = 0;
    this.data = null;

}
  public SparseMatrix(int high, int width){
    this.data = new HashMap<>();
    this.width = width;
    this.high = high;
  }
  public  void addElem(int high, int width, double num){
    if (num != 0){
      if(high >= 0 && width >= 0
              && high <= this.high && width <= this.width) {
      if (!this.data.containsKey(high)) this.data.put(high, new HashMap<Integer, Double>());
      this.data.get(high).put(width, num);
      this.hash += hashCode(num, high, width);
    }
    }
  }

 public double getElem(int high, int width) {
   if(high > this.high || width > this.width){
       throw new RuntimeException("coordinates are too big");
   }
   else{
       if(this.data.get(high) == null) return 0;
       Double res = this.data.get(high).get(width);
       if(res != null) return res;
       else return 0;
   }
 }

  @Override
  public long hashCode(double a, int b, int c) {
    return  (long) (a * Math.pow(2,b) * Math.pow(3,c));
  }

  public int getHigh(){
  return this.high;
  }


    public int getWidth(){
    return this.width;
  }

  public long getHash(){
      return this.hash;
  }

  public HashMap<Integer,HashMap<Integer, Double>> getData(){
      return this.data;
  }
  public void displayMatrix(){
    if(this.data != null) {
      for (int i = 0; i < this.high; ++i) {
        HashMap<Integer, Double> numbers = this.data.get(i);
        if(numbers != null) {
          //System.out.print("row " + (i+1) + ": ");
          for (int j = 0; j < this.width; ++j) {
            if (numbers.containsKey(j))
                  System.out.print("[" + i + "," + j + "] = " + numbers.get(j) + "; ");
                  //System.out.print((j+1) +": "+  (numbers.get(j)) + " ");
          }
        }
          System.out.println();
      }
    }
    else System.out.println("matrix is emty");

  }

  public SparseMatrix transposeMatrix() {
    if (this.data != null) {
        SparseMatrix result = new SparseMatrix(this.width, this.high);
        HashMap<Integer, HashMap<Integer, Double>> data = new HashMap<>();
      for (Integer i: this.data.keySet()) {
        for (Integer j : this.data.get(i).keySet()) {
            //if (!data.containsKey(j)) data.put(j, new HashMap<Integer, Double>());
            data.putIfAbsent(j, new HashMap<Integer, Double>());
            data.get(j).put(i, this.getElem(i,j));
        }
      }
      result.data =data;

      return result;
    }
    else return this;
  }

    public SparseMatrix transposedMatrix() {
        if (this.data != null) {
            SparseMatrix res = new SparseMatrix(this.width, this.high);
            for (Map.Entry<Integer, HashMap<Integer, Double>> line: this.data.entrySet()) {
                for (Map.Entry<Integer, Double> elem: line.getValue().entrySet()) {
                    res.addElem(elem.getKey(), line.getKey(), elem.getValue());
                }
            }
            return res;
        }
        return this;
    }

  public SparseMatrix mulSs(SparseMatrix m){
    if(m != null){
      SparseMatrix res = new SparseMatrix(this.high,m.width);
      HashMap<Integer, HashMap<Integer, Double>> data = new HashMap<>();
      SparseMatrix transposedM = m.transposeMatrix();

        for (Integer i: this.data.keySet()){
            for (Integer j:  transposedM.data.keySet()){
                double sum = 0;
                for (Integer k: this.data.get(i).keySet()) {
                    sum += this.getElem(i, k) * transposedM.getElem(j, k);
                }

                    res.data.putIfAbsent(i, new HashMap<Integer, Double>());
                    res.addElem(i,j, sum);
                //res.hash += hashCode(res.getElem(i,j), i,j);
            }
        }
        return res;

    }return null;
  }





  public SparseMatrix mulSd(DenseMatrix m){
      if(m != null){
          SparseMatrix res = new SparseMatrix(this.high,m.getWidth());
          HashMap<Integer, HashMap<Integer, Double>> data = new HashMap<>();
          DenseMatrix transposedM = m.transposeMatrix();

          for (Integer i: this.data.keySet()){
              for (int j = 0; j < transposedM.getWidth(); j++){
                  double sum = 0;
                  for (Integer k: this.data.get(i).keySet()) {
                      sum += this.getElem(i, k) * transposedM.getElem(j, k);
                  }

                  res.data.putIfAbsent(i, new HashMap<>());
                  res.addElem(i,j, sum);

              }
          }
          return res;

      }

      return null;
  }
    public void writeMatrix(String fName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fName));
        for (int i = 0; i < this.high; ++i) {
            HashMap<Integer, Double> line = this.data.get(i);
            if (line != null) {
                for (int j = 0; j < this.width; ++j) {
                    if (line.containsKey(j)) out.write(Double.toString(line.get(j)));
                    else out.write("0");
                    out.write(" ");
                }
            }
            else {
                for (int j = 0; j < this.width; ++j) {
                    out.write("0 ");
                }
            }
            out.write('\n');
        }
        out.flush();
        out.close();
    }


    /**
   * однопоточное умножение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param m
   * @return
   */
  @Override public Matrix mul(Matrix m)
  {
      if(this.width == m.getHigh()){
    if(m instanceof SparseMatrix)
        return this.mulSs((SparseMatrix)m);
    else if(m instanceof DenseMatrix)
        return this.mulSd((DenseMatrix) m);
    }
    return null;
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

    public boolean matrixCompare(Matrix m) {
        if (m instanceof SparseMatrix) {
            if (((SparseMatrix) m).getHash() != this.hash || ((SparseMatrix) m).getHigh() != this.high || ((SparseMatrix) m).getWidth() != this.width)
                return false;
            else {
                for (int i = 0; i < this.high; i++) {
                    for (int j = 0; j < this.width; j++) {
                        if (Math.abs(((SparseMatrix) m).getElem(i, j) - this.getElem(i, j)) > DELTA ) {
                            System.out.println("Elements on place [" + i + ", " + j + "] - " +
                                    this.getElem(i,j) + " and " + ((SparseMatrix) m).getElem(i, j) + " are not equal");
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        else
            return false;

    }


    /**
   * сравнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
      if(o instanceof String){
          if(!ifDense((String) o))
          {SparseMatrix sM = new SparseMatrix((String) o);
              return this.matrixCompare(sM);
          }
          else
              return false;
      }
      else if(o instanceof SparseMatrix)
          return this.matrixCompare((SparseMatrix)o);
      else if(o instanceof DenseMatrix)
          return false;
      else throw new NullPointerException("object type is wrong");

  }

  public static void main(String[] args) throws IOException {
      //SparseMatrix sMatr1 = new SparseMatrix("m3.txt");
      //SparseMatrix sMatr2 = new SparseMatrix("m2.txt");
      //sMatr.addElem(1,2, 24.8);
      //double elem = sMatr.getElem(1,0);
      //System.out.println(elem);
      //SparseMatrix m = sMatr1.mulSs(sMatr2);
      //m.displayMatrix();
      new MatrixGenerator(7,5, "SparseBig1.txt", 2000).generate();
      //new MatrixGenerator(2, 5,"Dense.txt", 7).generate();
      SparseMatrix sM1 = new SparseMatrix("SparseBig1.txt");
      SparseMatrix sM2 = new SparseMatrix("SparseBig.txt");
      //Matrix sMres = sM1.mul(sM2);
      System.out.print(sM1.equals(sM2));
      //sMatr2.transposedMatrix().displayMatrix();
      //sMres.writeMatrix("SSRes.txt");
  }
}
