package edu.spbu.matrix;

import edu.spbu.MatrixGenerator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix {
    final int width, high;
    double[][] data;
    public long hash = 0;

    private static final String path = "C://Users//Соня Смирнова//IdeaProjects/java-template/";

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
                    for (int k = 0; k < m.high ; k++) {
                        res.data[i][j] += this.data[i][k] * transposedM.data[j][k];

                    }
                    res.hash += hashCode(res.data[i][j], i, j);
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
                return (((SparseMatrix)m).transposeMatrix().mulSd(this.transposeMatrix())).transposeMatrix();
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
        if(m instanceof DenseMatrix) return this.threadMulDD((DenseMatrix)m, Runtime.getRuntime().availableProcessors());
        else if(m instanceof SparseMatrix) return this.threadMulDS((SparseMatrix)m, Runtime.getRuntime().availableProcessors());
        else return null;
    }

    /** Многопоточное умножение матриц.
     *
     * @param m  Первая (левая) матрица.
     * @param threadCount Число потоков.
     * @return Результирующая матрица.
     */
    public Matrix threadMulDD(DenseMatrix m, int threadCount)
    {
        assert threadCount > 0;

        int rowCount = this.high;             // Число строк результирующей матрицы.
        int colCount = m.width;         // Число столбцов результирующей матрицы.
        DenseMatrix result = new DenseMatrix(rowCount,colCount);  // Результирующая матрица.

        int cells = (rowCount * colCount) / threadCount;  // Число вычисляемых ячеек на поток.
        int first = 0;  // Индекс первой вычисляемой ячейки.
        final MultiplierThread[] multiplierThreads = new MultiplierThread[threadCount];  // Массив потоков.

        // Создание и запуск потоков.
        for (int i = threadCount - 1; i >= 0; --i) {
            int last = first + cells;  // Индекс последней вычисляемой ячейки.
            if (i == 0) {
                /* Один из потоков должен будет вычислить не только свой блок ячеек,
                   но и остаток, если число ячеек не делится нацело на число потоков. */
                last = rowCount * colCount;
            }
            multiplierThreads[i] = new MultiplierThread(this, m, result, first, last);
            multiplierThreads[i].start();
            first = last;
        }

        // Ожидание завершения потоков.
        try {
            for (MultiplierThread t : multiplierThreads)
                t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Matrix threadMulDS(SparseMatrix m, int threadCount)
    {
        assert threadCount > 0;

        int rowCount = this.high;             // Число строк результирующей матрицы.
        int colCount = m.getWidth();         // Число столбцов результирующей матрицы.
        DenseMatrix result = new DenseMatrix(rowCount,colCount);  // Результирующая матрица.

        int cells = (rowCount * colCount) / threadCount;  // Число вычисляемых ячеек на поток.
        int first = 0;  // Индекс первой вычисляемой ячейки.
        final MultiplierThread[] multiplierThreads = new MultiplierThread[threadCount];  // Массив потоков.

        // Создание и запуск потоков.
        for (int i = threadCount - 1; i >= 0; --i) {
            int last = first + cells;  // Индекс последней вычисляемой ячейки.
            if (i == 0) {
                /* Один из потоков должен будет вычислить не только свой блок ячеек,
                   но и остаток, если число ячеек не делится нацело на число потоков. */
                last = rowCount * colCount;
            }
            multiplierThreads[i] = new MultiplierThread(this, m, result, first, last);
            multiplierThreads[i].start();
            first = last;
        }

        // Ожидание завершения потоков.
        try {
            for (MultiplierThread t : multiplierThreads)
                t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }


    public boolean matrixCompare(Matrix m) {
        if (m instanceof DenseMatrix) {
            if (((DenseMatrix) m).high != this.high || ((DenseMatrix) m).width != this.width){
                System.out.println("(((");
                return false;
            }

            else {
                for (int i = 0; i < this.high; i++) {
                    for (int j = 0; j < this.width; j++) {
                        if (((DenseMatrix) m).data[i][j] != this.data[i][j]) {
                            System.out.println(")))");
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        else if(m instanceof SparseMatrix){
           return false;

        }

        else {
            System.out.println("what..");
            return false;
        }
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
                int nums = 0;
                //int h = rows.size();
                int w = rows.get(0).split("\\s").length;

                //double[][] data = new double[high][width];
                for (String row : rows) {
                    String[] numbers = row.split("\\s");
                    for (int j = 0; j < w; j++) {
                        if (Double.parseDouble(numbers[j]) != 0)
                            nums += 1;
                    }
                }
                return (float)nums/(rows.size()*rows.get(0).split("\\s").length) >  0.2;

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
                return false;
        }
        else if(o instanceof DenseMatrix)
            return this.matrixCompare((DenseMatrix)o);

        else if(o instanceof SparseMatrix)
            return false;
        else if(o instanceof Matrix)
            return false;
        else throw new NullPointerException("object type is wrong");

    }
    public void writeMatrix(String fName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fName));
        for (int i = 0; i < this.high; ++i) {
            for (double x : this.data[i]) {
                out.write(Double.toString(x));
                out.write(" ");
            }
            out.write('\n');
        }
        out.flush();
        out.close();
    }


    public static void main(String[] args) throws IOException {
        //DenseMatrix m = new DenseMatrix(3, 4);
        // m.addElem(1, 1, 2.5);
        //m.displayMatrix();
        new MatrixGenerator(0, 6,"sm1000_1.txt", 1000).generate();
        new MatrixGenerator(1, 6,"sm1000_2.txt", 1000).generate();
        DenseMatrix m1 = new DenseMatrix("sm1000_1.txt");
        DenseMatrix m2 = new DenseMatrix("sm1000_2.txt");
        //SparseMatrix m2 = new SparseMatrix("sm1000.txt");
        long startTime = System.nanoTime();


        Matrix res2 = m1.dmul(m2);
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Execution time(ms) " + (estimatedTime/ 1000000));


        Matrix res1 = m1.mul(m2);
        res1.writeMatrix("ss1000resm.txt");

        res2.writeMatrix("ss1000resdm.txt");

        //System.out.println(res1.matrixCompare(res2));
        for (int row = 0; row < 1000; ++row) {
            for (int col = 0; col < 1000; col++) {

                if (res2.getElem(row,col) != res1.getElem(row,col)) {
                    System.out.println("Error in multithreaded calculation!" + " "+  row + " " + col + " " + res2.getElem(row,col) + " " + res1.getElem(row,col));

                }
            }
        }


    }
}
