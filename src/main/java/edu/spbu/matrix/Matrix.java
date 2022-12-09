package edu.spbu.matrix;

/**
 *
 */
public interface Matrix
{

  public void displayMatrix();

  /**
   * @return количество строк
   */
  int getHigh();

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
