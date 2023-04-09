package edu.spbu.matrix;
import java.util.HashMap;

class MultiplierThread extends Thread
{
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;
    private final Matrix resultMatrix;
    private final int first;
    private final int last;
    /** число членов суммы при вычислении значения ячейки. */
    private final int sumLength;

    /**
     * @param firstMatrix  первая матрица
     * @param secondMatrix вторая матрица
     * @param resultMatrix результирующая матрица
     * @param first   начальный индекс
     * @param last    конечный индекс
     */
    public MultiplierThread(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix, final int first, final int last)
    {
        this.firstMatrix  = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.first   = first;
        this.last    = last;

        sumLength = secondMatrix.getWidth();
    }

    /**Вычисление значения в одной ячейке.
     *
     * @param row номер строки ячейки.
     * @param col номер столбца ячейки.
     */
    public void calcValue(final int row, final int col)
    {
        double sum = 0;
        for (int i = 0; i < sumLength; ++i)
            sum += firstMatrix.getElem(row,i) * secondMatrix.getElem(i,col);
        if(resultMatrix instanceof SparseMatrix) ((SparseMatrix) resultMatrix).data.putIfAbsent(row, new HashMap<>());
        resultMatrix.addElem(row,col,sum);
    }


    /** рабочая функция потока. */
    @Override
    public void run()
    {
        //System.out.println("Thread " + getName() + " started. Calculating cells from " + first + " to " + last + "...");

        final int colCount = secondMatrix.getWidth();
        for (int index = first; index < last; ++index) {
            calcValue(index / colCount, index % colCount);

        }

        //System.out.println("Thread " + getName() + " finished.");
    }
}
