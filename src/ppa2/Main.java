package ppa2;

import java.util.Random;

/**
 * main class
 */
public class Main {

    /**
     * instance of duplicate remover that has the slowest runtime and is calculated in method print slowest
     */
    DuplicateRemover slowestDR;

    /**
     * max number of elements we can remove duplicates from so that the slowest duplicate remover has a runtime close to 10 sec
     */
    int tenSecMaxN;
    //double finalTime;

    /**
     * measures total runtime of the program and calls printSlowest, printTable
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();

        //old
        /*int[] gen = main.generateData(10);
        int[] data1 = new RemoveDuplicates1().removeDuplicates(gen);
        int[] data2 = new RemoveDuplicates2().removeDuplicates(gen);
        int[] data3 = new RemoveDuplicates3().removeDuplicates(gen);*/

        long t1 = System.currentTimeMillis();
        main.printSlowest();
        main.printTable();
        long t2 = System.currentTimeMillis();

        System.out.println("All done. Total runtime: "+((t2-t1)/1000.0)+" seconds");
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
        System.out.println("gen "+count);
        return result;
    }

    /**
     * A method that tries all removeDuplicates methods for 1 sec and saved the slowest one
     */
    public void printSlowest(){
        DuplicateRemover r1= new RemoveDuplicates1();
        DuplicateRemover r2 = new RemoveDuplicates2();
        DuplicateRemover r3 = new RemoveDuplicates3();
        int dataIncrement = 10;
        double ratio1 = checkXSecond(r1, dataIncrement,1,false);
        double ratio2 = checkXSecond(r2, dataIncrement,1,false);
        double ratio3 = checkXSecond(r3, dataIncrement,1,false);
        if (ratio1<ratio2&&ratio1<ratio3){
            System.out.println("Nejpomalejsi je "+ r1.toString());
            slowestDR = r1;
        }
        if (ratio2<ratio1&&ratio2<ratio3){
            System.out.println("Nejpomalejsi je "+ r2.toString());
            slowestDR = r2;
        }
        if (ratio3<ratio1&&ratio3<ratio2){
            System.out.println("Nejpomalejsi je "+ r3.toString());
            slowestDR = r3;
        }
        System.out.format("Merim %s pro 10 sekund%n", slowestDR.toString());
        checkXSecond(slowestDR, dataIncrement, 10, true);



    }

    /**
     *  checks when the removeDuplicates goes over a specified threshold time, if not it selects the correct growth function
     * @param duplicateRemover algorithm that can remove duplicates from an array
     * @param dataIncrement a constant changing the growing rates
     * @param thresholdTimeSeconds threshold in seconds until which we test
     * @param saved saves the data about the run
     * @return data to time ratio of running algorithm
     */
    public double checkXSecond(DuplicateRemover duplicateRemover, int dataIncrement, int thresholdTimeSeconds,boolean saved){
        double time=0;
        int data=1;
        int counter=0;
        try{
        do {

            if(Math.abs(time-thresholdTimeSeconds)>0.5*thresholdTimeSeconds) {
                data += dataIncrement*Math.exp(counter);
            }else if(Math.abs(time-thresholdTimeSeconds)>0.2*thresholdTimeSeconds) {
                data += dataIncrement*counter*counter*counter*counter;
            }else{
                data+=dataIncrement*counter*counter*counter;
            }
            time = duplicateRemover.runTimed(generateData(data));
            counter++;

        }while (time<thresholdTimeSeconds);
        System.out.format("Metoda %s trvala pres %d sekund (konkretne %f ) pro n = %d \n",duplicateRemover.toString(), thresholdTimeSeconds,time, data);
        }catch (OutOfMemoryError ex){
            System.out.format("Zda se ze dosla pamet pro %s, predbezne zastavuji na %f sekundy pro n = %d %n",duplicateRemover, time, data);
        }
        if (saved){
            tenSecMaxN=data;
            //finalTime=time;
        }
        return data/time;

    }

    /**
     * prints the final table
     */
    public void printTable(){
        DuplicateRemover r1 = new RemoveDuplicates1();
        DuplicateRemover r2 = new RemoveDuplicates2();
        DuplicateRemover r3 = new RemoveDuplicates3();
        System.out.format("%s\t\t%s  \t\t%s  \t\t%s  \t\t%s  \t\t%s  \t\t%s%n", "n","t1","a1","t2","a2","t3","a3");
        System.out.println("----------------------------------------------------------------------");
        for (int i = 1000; i <= tenSecMaxN; i+=(tenSecMaxN-1000)/10) {
            int[] generatedData = generateData(i);

            double t1 = r1.runTimed(generatedData);
            double t2 = r2.runTimed(generatedData);
            double t3 = r3.runTimed(generatedData);
            double slowestTime=0;

            //optimisation
            if (slowestDR instanceof RemoveDuplicates1){
                slowestTime=t1;
            }
            if (slowestDR instanceof RemoveDuplicates2){
                slowestTime=t2;
            }
            if (slowestDR instanceof RemoveDuplicates3){
                slowestTime=t3;
            }
            System.out.format("%d\t|%f\t%f\t%f\t%f\t%f\t%f%n",i, t1,slowestTime/t1,t2,slowestTime/t2,t3,slowestTime/t3);


        }
    }

}
