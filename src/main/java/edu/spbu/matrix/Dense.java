package edu.spbu.matrix;

public class Dense {
    int wide, high;
    double [][]data;
    long hash = 0;

    public Dense(int high, int wide){
        this.data = new double[high][wide];
        this.wide = wide;
        this.high = high;
    }

    synchronized public void addElem(int row, int col, double num){
        this.data[row][col] = num;
        this.hash += (long) (num*row*col);
    }

    public void displayMatrix(){
        for (int i = 0; i < this.high ; i++)
        {
            for (int j = 0; j < this.wide; j++)
            {
                System.out.print(data[i][j] + " ");
            }
            System.out.print('\n');// переводим на новую строку
        }

    }


    Matrix mul(Matrix o){
        Dense m = new Dense(2,3);

        return null;
    }


    @Override
    public boolean equals(Object obj) {

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "high: " + high + '\n' + "wide: " + wide + '\n' + "hash: " + hash;
    }

    public static void main(String[] args) {
        Dense m = new Dense(4,4);
        m.addElem(1,1,3);
        //System.out.println(m.toString());
        m.displayMatrix();

    }


}

