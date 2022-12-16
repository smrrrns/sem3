package edu.spbu.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix {
    private final int width, high;
    private double[][] data;
    public long hash = 0;

    private static final String path = "C://Users//Sonya//IdeaProjects//sem3/";

    /**
     * чтение матрицы из файла
     * @param fileName имя файла
     */
    public DenseMatrix(String fileName) {
        Path file = Paths.get(path + fileName);

        try (Scanner obj = new Scanner(file)) {
            ArrayList<String> rows = new ArrayList<>();

            while (obj.hasNextLine())
                rows.add(obj.nextLine());

            if( rows.size() == 0 || rows.get(0).split("\\s").length == 0){
                this.high = 0;
                this.width = 0;
                this.data = new double[0][0];
            }
            else {
                this.high = rows.size();
                this.width = rows.get(0).split("\\s").length;

                double[][] data = new double[this.high][this.width];
                for (int i = 0; i < this.high; i++) {
                    String [] numbers = rows.get(i).split("\\s");
                    for (int j = 0; j < this.width; j++) {
                        data[i][j] = Double.parseDouble(numbers[j]);
                        this.hash += hashCode(data[i][j], i, j);
                    }
                }
                this.data = data;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * преобразование двумерного массива в DenseMatrix
     * @param arr двумерный массив
     */
    public DenseMatrix(double[][] arr){
        if (arr != null) {
            this.high = arr.length;
            this.width = arr[0].length;
            double[][] data = new double[this.high][this.width];

            for (int i = 0; i < this.high; i++) {
                for (int j = 0; j < this.width; j++) {
                    data[i][j] = arr[i][j];
                    this.hash += hashCode(arr[i][j], i, j);
                }
            }
            this.data = data;
        }
        else {
            this.high = 0;
            this.width = 0;
            this.data = new double[0][0];
        }
    }

    /**
     * создание пустой матрицы размера high*wide
     * @param high количество строк
     * @param width количество столбцов
     */
    public DenseMatrix(int high, int width) {
        this.data = new double[high][width];
        this.width = width;
        this.high = high;
    }

    /**
     * добавление элемента num на место [row][col]
     * @param high количество строк
     * @param width количество столбцов
     * @param num число, которое нужно поставить на место [high][width]
     */
    public void addElem(int high, int width, double num) {
        this.data[high][width] = num;
        this.hash += hashCode(num, high, width);
    }

    /**
     * элемент матрицы с индексом [high][width]
     * @param high номер строки
     * @param width номер столбца
     * @return элемент с индексом [high][width]
     */
    public double getElem(int high, int width) {
        if (high <= this.high && width <= this.width) {
            return this.data[high][width];
        } else return 0;
    }

    /**
     * хэшкод
     * @param a значение матрицы на месте [b][c]
     * @param b номер строки
     * @param c номер столбца
     * @return хэшкод
     */
    @Override
    public long hashCode(double a, int b, int c){
        return  (long) (a * b * c);
    }

    /**
     * вывод матрицы
     */
    public void displayMatrix() {
        for (int i = 0; i < this.high; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print(this.data[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * размер и хэш матрицы
     * @return количество строк, количество столбцов, хэш матрицы
     */
    @Override
    public String toString() {
        return "high: " + high + '\n' + "wide: " + width + '\n' + "hash: " + hash;
    }

    /**
     * транспонирование матрицы
     * @return транспонированная матрица
     */
    public DenseMatrix transposeMatrix() {
        if (this.data != null) {
            DenseMatrix result = new DenseMatrix(this.width, this.high);
            for (int i = 0; i < this.high; ++i) {
                for (int j = 0; j < this.width; ++j) {
                    result.addElem(j, i, this.getElem(i, j));
                }
            }
            return result;
        }
        return this;
    }

    /**
     * @return количество строк
     */
    public int getHigh(){
        return this.high;
    }

    /**
     * @return количество столбцов
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * умножение двух Dense матриц
     * @param m Dense матрица
     * @return Dense матрица
     */
    public DenseMatrix mulDd(DenseMatrix m){
        if(m != null) {
            DenseMatrix res = new DenseMatrix(this.high, m.width);
            DenseMatrix transposedM = m.transposeMatrix();

            for (int i = 0; i < res.high; i++){
                for (int j = 0; j < res.width; j++){
                    for (int k = 0; k < m.high ; k++){
                        res.data[i][j] += this.data[i][k]*transposedM.data[j][k];
                    }
                    res.hash += hashCode(res.data[i][j], i,j);
                }
            }
            return res;
        }
        return null;
    }

    /**
     * однопоточное умножение матриц
     * должно поддерживаться для всех 4-х вариантов
     *
     * @param m матрица
     * @return матрица
     */
    @Override
    public Matrix mul(Matrix m) {
        if(this.width == m.getHigh()){
            if(m instanceof DenseMatrix)
                return this.mulDd((DenseMatrix)m);
            else if(m instanceof SparseMatrix)
                return (((SparseMatrix)m).transposeMatrix().mulSd(this.transposeMatrix())).transposedMatrix();
        }
        return this;
    }

    /**
     * многопоточное умножение матриц
     *
     * @param m матрица
     * @return матрица
     */
    @Override
    public Matrix dmul(Matrix m) {
        return null;
    }

    public boolean matrixCompare(Matrix m) {
        if (m instanceof DenseMatrix) {
            if (((DenseMatrix) m).hash != this.hash || ((DenseMatrix) m).high != this.high || ((DenseMatrix) m).width != this.width)
                return false;
            else {
                for (int i = 0; i < this.high; i++) {
                    for (int j = 0; j < this.width; j++) {
                        if (((DenseMatrix) m).data[i][j] != this.data[i][j]) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        else if (m instanceof SparseMatrix) {
            if (((SparseMatrix) m).getHash() != this.hash || ((SparseMatrix) m).getHigh() != this.high || ((SparseMatrix) m).getWidth() != this.width)
                return false;
            else {
                for (int i = 0; i < this.high; i++) {
                    for (int j = 0; j < this.width; j++) {
                        if (((SparseMatrix) m).getElem(i,j) != this.data[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }

        }
        else
            throw new NullPointerException("parameter type is wrong");
        //return false;

    }

    public static boolean ifDense(String file){
        Path file1 = Paths.get(path + file);

        try (Scanner obj = new Scanner(file1)) {
            ArrayList<String> rows = new ArrayList<>();

            while (obj.hasNextLine())
                rows.add(obj.nextLine());

            if( rows.size() == 0 || rows.get(0).split("\\s").length == 0){
                throw new NullPointerException("null matrix");
            }
            else {
                int zeros = 0;
                int nums = 0;
                //int h = rows.size();
                int w = rows.get(0).split("\\s").length;

                //double[][] data = new double[high][width];
                for (String row : rows) {
                    String[] numbers = row.split("\\s");
                    for (int j = 0; j < w; j++) {
                        if (Double.parseDouble(numbers[j]) == 0)
                            zeros += 1;
                        else nums += 1;
                    }
                }
                return nums > zeros;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * сравнивает с обоими вариантами
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof String){
            if(ifDense((String) o))
            {DenseMatrix dM = new DenseMatrix((String) o);
                return this.matrixCompare(dM);
            }
            else
            {SparseMatrix sM = new SparseMatrix((String) o);
                return this.matrixCompare(sM);
            }
        }
        else if(o instanceof DenseMatrix)
            return this.matrixCompare((DenseMatrix)o);

        else if(o instanceof SparseMatrix)
            return this.matrixCompare((SparseMatrix)o);

        else throw new NullPointerException("object type is wrong");

    }

    public static void main(String[] args) {
        //DenseMatrix m = new DenseMatrix(3, 4);
        // m.addElem(1, 1, 2.5);
        //m.displayMatrix();

        //DenseMatrix matrix1 = new DenseMatrix("m1.txt");
        //SparseMatrix matrix2 = new SparseMatrix("m2.txt");
        //matrix2.mul(matrix1).displayMatrix();
        //matrix1.displayMatrix();
        DenseMatrix matrix1 = new DenseMatrix("m1.txt");
        DenseMatrix matrix2 = new DenseMatrix("m2.txt");
        System.out.println(matrix1.equals(matrix2));

    }
}
