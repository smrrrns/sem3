package edu.spbu.matrix;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{
  private int high, width;
  private HashMap<Integer,HashMap<Integer, Double>> data;
  private long hash = 0;

  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) {
    Path file = Paths.get("C://matrix files/" + fileName);

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
    return  (long) (a * b * c);
  }

  public int getHigh(){
  return this.high;
  }

  public int getWidth(){
    return this.width;
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
                //res.hash += hashCode(res.data.get(i).get(j), i,j);
            }
        }
        return res;

    }return null;
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
    if(m instanceof SparseMatrix){
      if(this.width == m.getHigh())
        return this.mulSs((SparseMatrix)m);
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

  /**
   * сравнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    return false;
  }

  public static void main(String[] args) {
      SparseMatrix sMatr1 = new SparseMatrix("m1.txt");
      SparseMatrix sMatr2 = new SparseMatrix("m2.txt");
      //sMatr.addElem(1,2, 24.8);
      //double elem = sMatr.getElem(1,0);
      //System.out.println(elem);
      SparseMatrix m = sMatr1.mulSs(sMatr2);
      m.displayMatrix();
      //sMatr2.transposedMatrix().displayMatrix();
      //System.out.println(sMatr1.data.get(1).keySet());
  }
}
