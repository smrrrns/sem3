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

  public void writeMatrix(String fName) throws IOException;

  /**
   * @return количество столбцов
   */
  //int getWidth();

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

}
