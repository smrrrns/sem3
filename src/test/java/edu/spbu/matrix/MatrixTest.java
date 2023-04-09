package edu.spbu.matrix;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{
  @Test
  public void mulDD() {
    Matrix m1 = new DenseMatrix("dm1000_1.txt");
    Matrix m2 = new DenseMatrix("dm1000_2.txt");
    Matrix expected = new DenseMatrix("dd1000resm.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulDS() {
    Matrix m1 = new DenseMatrix("dm1000.txt");
    Matrix m2 = new SparseMatrix("sm1000.txt");
    Matrix expected = new SparseMatrix("ds1000resm.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulSD() {
    Matrix m1 = new SparseMatrix("sm1000.txt");
    Matrix m2 = new DenseMatrix("dm1000.txt");
    Matrix expected = new SparseMatrix("sd1000resm.txt");
    assertEquals(expected, m1.mul(m2));
  }
  @Test
  public void mulSS() {
    Matrix m1 = new SparseMatrix("sm1000_1.txt");
    Matrix m2 = new SparseMatrix("sm1000_2.txt");
    Matrix expected = new SparseMatrix("ss1000resm.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void dmulDD() throws IOException {
    Matrix m1 = new DenseMatrix("dm1000_1.txt");
    Matrix m2 = new DenseMatrix("dm1000_2.txt");
    Matrix expected = new DenseMatrix("dd1000resm.txt");
    assertEquals(expected, m1.dmul(m2));
  }
  @Test
  public void dmulSD() throws IOException {
    Matrix m1 = new DenseMatrix("dm1000.txt");
    Matrix m2 = new SparseMatrix("sm1000.txt");

    Matrix res1 = m2.dmul(m1);
    res1.writeMatrix("sdRes.txt");
    DenseMatrix res = new DenseMatrix("sdRes.txt");
    Matrix expected = new DenseMatrix("sd1000resm.txt");
    assertEquals(expected, res);
  }
  @Test
  public void dmulDS() throws IOException {
      Matrix m1 = new SparseMatrix("sm1000.txt");
      Matrix m2 = new DenseMatrix("dm1000.txt");

      Matrix res1 = m2.dmul(m1);
      res1.writeMatrix("dsRes.txt");
      Matrix expected = new DenseMatrix("ds1000resm.txt");
      DenseMatrix res = new DenseMatrix("dsRes.txt");
      assertEquals(expected, res);

//    Matrix m1 = new SparseMatrix("sm1000.txt");
//    Matrix m2 = new DenseMatrix("dm1000.txt");
//    Matrix res1 = m2.mul(m1);
//    res1.writeMatrix("dsresmr.txt");
//    DenseMatrix expected = new DenseMatrix("dsresm.txt");
//    DenseMatrix res = new DenseMatrix("dsresmr.txt");
//    assertEquals(expected, res);
  }
  @Test
  public void dmulSS() throws IOException {
    Matrix m1 = new SparseMatrix("sm1000_1.txt");
    Matrix m2 = new SparseMatrix("sm1000_2.txt");

    Matrix res1 = m1.dmul(m2);
    res1.writeMatrix("ssRes.txt");
    Matrix expected = new SparseMatrix("ss1000resdm.txt");
    SparseMatrix res = new SparseMatrix("ssRes.txt");
    assertEquals(expected, res);
//    Matrix expected = new SparseMatrix("ss1000resm.txt");
//    assertEquals(expected, m1.dmul(m2));
  }

}
