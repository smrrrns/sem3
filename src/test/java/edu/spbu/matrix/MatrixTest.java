package edu.spbu.matrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{
  @Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("matrix2000_1.txt");
    Matrix m2 = new DenseMatrix("matrix2000_2.txt");
    Matrix expected = new DenseMatrix("matrix2000_res.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulDS() {
    Matrix m1 = new DenseMatrix("DenseBig.txt");
    Matrix m2 = new SparseMatrix("SparseBig.txt");
    Matrix expected = new SparseMatrix("denseSparseRes.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulSD() {
    Matrix m1 = new SparseMatrix("SparseBig.txt");
    Matrix m2 = new DenseMatrix("DenseBig.txt");
    Matrix expected = new SparseMatrix("sparseDenseRes.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulSS() {
    Matrix m1 = new SparseMatrix("SparseBig1.txt");
    Matrix m2 = new SparseMatrix("SparseBig.txt");
    Matrix expected = new SparseMatrix("SSRes.txt");
    assertEquals(expected, m1.mul(m2));
  }
}
