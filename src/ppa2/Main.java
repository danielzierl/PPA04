package ppa2;

import java.util.Arrays;
import java.util.Random;


public class Main {
    DuplicateRemover slowestDR;
    int tenSecMaxN;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.printSlowest();
        main.printTable();

        System.out.println("All done.");
    }
    /**
     * Generuje nahodna data v rozsahu do 100 000,
     * cimz se simuluje, ze cca 90% cisel je "neaktivnich"
     * @param count pocet pozadovanych cisel
     * @return pole nahodnych cisel
     */
    public int[] generateData(int count) {
        int[] result = new int[count];
        Random r = new Random();
        for (int i = 0; i < result.length; i++) {
            result[i] = r.nextInt(100000);
        }
        return result;
    }
    public void printSlowest() throws Exception {
        DuplicateRemover r1= new RemoveDuplicates1();
        DuplicateRemover r2 = new RemoveDuplicates2();
        DuplicateRemover r3 = new RemoveDuplicates3();
        int dataIncrement = 1000;
        double ratio1 = checkXSecond(r1, dataIncrement,1,false);
        double ratio2 = checkXSecond(r2, dataIncrement,1,false);
        double ratio3 = checkXSecondExponentially(r3, dataIncrement,1);
        if (ratio1<ratio2&&ratio1<ratio3){
            System.out.println("Nejpomalejsi je "+ r1.toString());
            slowestDR = r1;
        }
        if (ratio2<ratio1&&ratio2<ratio3){
            System.out.println("Nejpomalejsi je "+ r3.toString());
            slowestDR = r2;
        }
        if (ratio3<ratio1&&ratio3<ratio2){
            System.out.println("Nejpomalejsi je "+ r3.toString());
            slowestDR = r2;
        }
        checkXSecond(slowestDR, dataIncrement, 10, true);



    }
    public double checkXSecond(DuplicateRemover duplicateRemover, int dataIncrement, int thresholdTimeSeconds,boolean saved){
        double time;
        int data=0;
        int counter=0;
        do {
            data= (int) (dataIncrement*counter*counter);

            time = duplicateRemover.runTimed(generateData(data));
            counter++;

        }while (time<thresholdTimeSeconds);
        System.out.format("Metoda %s trvala pres %d sekund (konkretne %f ) pro n = %d \n",duplicateRemover.toString(), thresholdTimeSeconds,time, data);
        if (saved)tenSecMaxN=data;
        return data/time;

    }
    public double checkXSecondExponentially(DuplicateRemover duplicateRemover, int dataIncrement,int thresholdTimeSeconds) throws Exception {
        double time;
        int data=0;
        int counter=0;
        do {
            data= (int) (dataIncrement*Math.exp(counter));

            time = duplicateRemover.runTimed(generateData(data));
            counter++;
            if (counter>100){
                throw new Exception("HAHA");
            }

        }while (time<thresholdTimeSeconds);
        System.out.format("Metoda %s trvala pres %d sekund (konkretne %f ) pro n = %d \n",duplicateRemover.toString(), thresholdTimeSeconds, time, data);
        return data/time;
    }
    public void printTable(){
        DuplicateRemover r1 = new RemoveDuplicates1();
        DuplicateRemover r2 = new RemoveDuplicates2();
        DuplicateRemover r3 = new RemoveDuplicates3();
        System.out.format("%s\t\t%s  \t\t%s  \t\t%s  \t\t%s  \t\t%s  \t\t%s%n", "n","t1","a1","t2","a2","t3","a3");
        for (int i = 1000; i <= tenSecMaxN; i+=(tenSecMaxN-1000)/10) {
            int[] generatedData = generateData(i);
            double t1 = r1.runTimed(generatedData);
            double t2 = r2.runTimed(generatedData);
            double t3 = r3.runTimed(generatedData);
            double slowestTime=0;

            if (slowestDR instanceof RemoveDuplicates1){
                slowestTime=t1;
            }
            if (slowestDR instanceof RemoveDuplicates2){
                slowestTime=t2;
            }
            if (slowestDR instanceof RemoveDuplicates3){
                slowestTime=t3;
            }
            System.out.format("%d\t%f\t%f\t%f\t%f\t%f\t%f%n",i, t1,slowestTime/t1,t2,slowestTime/t2,t3,slowestTime/t3);

          
        }
    }

}
