package edu.spbu.matrix;

import java.util.HashMap;

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

  }
  public SparseMatrix(double[][] arr){

}
  public SparseMatrix(int high, int width){

  }
  public  void addElem(int high, int width, double num){

  }

  public double getElem(int high, int width) {
    return 0;
  }

  public int getHigh(){
  return 0;
  }

  public int getWidth(){
    return this.width;
  }

  public SparseMatrix transposeMatrix(){
    return this;
  }

  public void displayMatrix(){

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

  }
}
