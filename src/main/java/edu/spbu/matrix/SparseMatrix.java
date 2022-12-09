package edu.spbu.matrix;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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
          for (int j = 0; j < this.width; j++) {
            data.get(i).put(j,Double.parseDouble(rows.get(i).split("\\s")[j]));
            this.hash += hashCode(data.get(i).get(j), i, j);
          }
        }
        this.data = data;
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public SparseMatrix(double[][] arr){

}
  public SparseMatrix(int high, int width){
    this.data = new HashMap<>();
    this.width = width;
    this.high = high;
  }
  public  void addElem(int high, int width, double num){
    if (num != 0 && high > 0 && width > 0 && high < this.high && width < this.width) {
      if (!this.data.containsKey(high)) this.data.put(high, new HashMap<Integer, Double>());
      this.data.get(high).put(width, num);
      this.hash += hashCode(num, high, width);
    }
  }

  public double getElem(int high, int width) {
    try {
      return this.data.get(high).get(width);
    }
    catch (Exception e) {
      return 0;
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

  public SparseMatrix transposeMatrix(){

    return this;
  }

  public void displayMatrix(){
    if(this.data != null) {
      for (int i = 0; i < this.high; i++) {
        HashMap<Integer, Double> numbers = this.data.get(i);
        if(numbers != null) {
          System.out.print("row " + i + ":");
          for (int j = 0; j < this.width; j++) {
            //if (numbers.containsKey(j))
                System.out.print("col:" + numbers.get(j) + " ");
          }
        }
        System.out.println();
      }
    }

  }

  /**
   * однопоточное умножение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o)
  {
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
      SparseMatrix sMatr = new SparseMatrix(3,3);
      sMatr.addElem(2,3, 15);
      System.out.println(sMatr.getElem(2,3));
      sMatr.displayMatrix();
  }
}
