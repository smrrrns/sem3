package edu.spbu.matrix;

import java.io.IOException;

/**
 *
 */
public interface Matrix
{

  public void displayMatrix();

  //public SparseMatrix transposeM(SparseMatrix m);
  //public DenseMatrix transposeM(DenseMatrix m);

  long hashCode(double a, int b, int c);

  /**
   * @return количество строк
   */
  int getHigh();
  int getWidth();

  double getElem(int i, int j);
  void addElem(int high, int width, double num);

  public void writeMatrix(String fName) throws IOException;

  /**
   * @return количество столбцов
   */



  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   * @param o
   * @return
   */
  Matrix mul(Matrix o);

  /**
   * многопоточное умножение матриц
   * @param o
   * @return
   */
  Matrix dmul(Matrix o);

  boolean matrixCompare(Matrix m);


  Matrix transposeMatrix();
}
