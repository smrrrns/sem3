package edu.spbu.matrix;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix {
    private final int width, high;
    private final double[][] data;
    private long hash = 0;

    /**
     * чтение матрицы из файла
     * @param fileName имя файла
     */
    public DenseMatrix(String fileName) {
        Path file = Paths.get("C://matrix files/" + fileName);

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
                    for (int j = 0; j < this.width; j++) {
                        data[i][j] = Double.parseDouble(rows.get(i).split("\\s")[j]);
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
     * @param wide количество столбцов
     */
    public DenseMatrix(int high, int wide) {
        this.data = new double[high][wide];
        this.width = wide;
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
    public long hashCode(double a, double b, double c){
        return  (long) (a * b * c);
    }

    /**
     * вывод матрицы
     */
    public void displayMatrix() {
        for (int i = 0; i < this.high; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.print('\n');
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
        if(m instanceof DenseMatrix){
            if(this.width == m.getHigh()){
                return this.mulDd((DenseMatrix)m);
            }
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

    /**
     * сравнивает с обоими вариантами
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    public static void main(String[] args) {
        //DenseMatrix m = new DenseMatrix(3, 4);
        // m.addElem(1, 1, 2.5);
        //m.displayMatrix();

        DenseMatrix matrix1 = new DenseMatrix("m1.txt");
        DenseMatrix matrix2 = new DenseMatrix("m2.txt");
        matrix1.mul(matrix2).displayMatrix();

    }
}
