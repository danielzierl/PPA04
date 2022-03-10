package ppa2;

import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * main class
 */
public class Main {

	/**
	 * instance of duplicate remover that has the slowest runtime and is calculated in method print slowest
	 */
	DuplicateRemover slowestDuplicateRemover;

	/**
	 * max number of elements we can remove duplicates from so that the slowest duplicate remover has a runtime close to 10 sec
	 */
	int tenSecondsElements;
	/**
	 * saved time for 10second run, used to save time
	 */
	double finalTime;

    /**
     * instances of all 3 duplicate removing methods
     */
    DuplicateRemover r1;
    DuplicateRemover r2;
    DuplicateRemover r3;

	/**
	 * measures total runtime of the program and calls printSlowest, printTable
	 *
	 * @param args nothing
	 */
	public static void main(String[] args) throws TimeoutException {
		Main main = new Main();
        main.init();

		long t1 = System.currentTimeMillis();
		main.printSlowest();
		main.printTable();


		System.out.println("All done. Total runtime: " + ((System.currentTimeMillis() - t1) / 1000.0) + " seconds");
	}
    private void init(){
        r1 = new RemoveDuplicates1();
        r2 = new RemoveDuplicates2();
        r3 = new RemoveDuplicates3();
    }

	/**
	 * Generuje nahodna data v rozsahu do 100 000,
	 * cimz se simuluje, ze cca 90% cisel je "neaktivnich"
	 *
	 * @param count pocet pozadovanych cisel
	 * @return pole nahodnych cisel
	 */
	public static int[] generateData(int count) throws TimeoutException {
		if (count > 50000000) {
			throw new TimeoutException("data count was too long ");
		}
		int[] result = new int[count];
		Random r = new Random();
		for (int i = 0; i < result.length; i++) {
			result[i] = r.nextInt(100000);
		}
		return result;
	}

	/**
	 * A method that tries all removeDuplicates methods for 1 sec and saves the slowest one
	 */
	public void printSlowest() {

		double ratio1 = checkXSecond(r1, 1, false);
		double ratio2 = checkXSecond(r2, 1, false);
		double ratio3 = checkXSecond(r3, 1, false);
		calcSlowestRemoverBasedOnRatios(ratio1,ratio2,ratio3);

		System.out.format("%nMerim %s pro 10 sekund, prosim nezastavovat%n%n", slowestDuplicateRemover.toString());
		checkXSecond(slowestDuplicateRemover, 10, true);
	}
    private void calcSlowestRemoverBasedOnRatios(double ratio1, double ratio2, double ratio3){
        if (ratio1 < ratio2 && ratio1 < ratio3) {
            System.out.println("Nejpomalejsi je " + r1);
            slowestDuplicateRemover = r1;
        }
        if (ratio2 < ratio1 && ratio2 < ratio3) {
            System.out.println("Nejpomalejsi je " + r2);
            slowestDuplicateRemover = r2;
        }
        if (ratio3 < ratio1 && ratio3 < ratio2) {
            System.out.println("Nejpomalejsi je " + r3);
            slowestDuplicateRemover = r3;
        }
    }

	/**
	 * checks when the removeDuplicates goes over a specified threshold time, if not it selects the correct growth function
	 *
	 * @param duplicateRemover     algorithm that can remove duplicates from an array
	 * @param thresholdTimeSeconds threshold in seconds until which we test
	 * @param saved                saves the data about the run
	 * @return data to time ratio of running algorithm
	 */
	public double checkXSecond(DuplicateRemover duplicateRemover, int thresholdTimeSeconds, boolean saved) {
		double time = 0;
		int data = 1000;
		try {
			do {
				data *= 1.15;
				time = duplicateRemover.runTimed(generateData(data));
			} while (time < thresholdTimeSeconds);
			System.out.format("Metoda %s trvala pres %d sekund (konkretne %f ) pro n = %d \n", duplicateRemover, thresholdTimeSeconds, time, data);
		} catch (OutOfMemoryError ex) {
			System.out.format("Zda se ze dosla pamet pro %s, predbezne zastavuji na %f sekundy pro n = %d %n", duplicateRemover, time, data);
		} catch (TimeoutException ex) {
			System.out.format("Zda se ze se program snazil generovat moc prvku pro %s, predbezne zastavuji na %f sekundy pro n = %d %n", duplicateRemover, time, data);
		}
		if (saved) {
			tenSecondsElements = data;
			finalTime = time;
		}
		return data / time;
	}

	/**
	 * prints the final table
	 */
	public void printTable() throws TimeoutException {
		//zrudnost, ale funguje velmi dobre
		System.out.format("%s\t%s  \t%s  \t%s  \t%s  \t%s  \t%s%n", "n    ", "t1    ", "a1    ", "t2    ", "a2    ", "t3    ", "a3    ");
		System.out.println("--------------------------------------------------------------------------------------------------");
		for (int i = 1000; i <= tenSecondsElements; i += (tenSecondsElements - 1000) / 10.0) {
			double t1;
			double t2;
			double t3;
			double slowestTime ;

			int[] generatedData = generateData(i);

			//this if here shaves off around 10 seconds from runtime, could be just the else part, but it will be slower
			if (i > tenSecondsElements - 11) {
				if (slowestDuplicateRemover instanceof RemoveDuplicates1) {
					t1 = finalTime;
					t2 = r2.runTimed(generatedData);
					t3 = r3.runTimed(generatedData);
				} else if (slowestDuplicateRemover instanceof RemoveDuplicates2) {
					t1 = r1.runTimed(generatedData);
					t2 = finalTime;
					t3 = r3.runTimed(generatedData);
				} else {
					t1 = r1.runTimed(generatedData);
					t2 = r2.runTimed(generatedData);
					t3 = finalTime;
				}
			} else {
				t1 = r1.runTimed(generatedData);
				t2 = r2.runTimed(generatedData);
				t3 = r3.runTimed(generatedData);
			}
			//optimisation
			slowestTime=getSlowestTime(t1,t2,t3);

			System.out.format("%d\t%f  \t%f  \t%f  \t%f  \t%f  \t%f%n", i, t1, slowestTime / t1, t2, slowestTime / t2, t3, slowestTime / t3);


		}
	}

    /**
     * sets the slowest time value based on the slowestDuplicateRemover instance
     * @param t1 time of dupRemover1
     * @param t2 time of dupRemover2
     * @param t3 time of dupRemover3
     * @return slowest time
     */
    private double getSlowestTime(double t1, double t2, double t3){
        double slowestTime=0;
        if (slowestDuplicateRemover instanceof RemoveDuplicates1) {
            slowestTime = t1;
        }
        if (slowestDuplicateRemover instanceof RemoveDuplicates2) {
            slowestTime = t2;
        }
        if (slowestDuplicateRemover instanceof RemoveDuplicates3) {
            slowestTime = t3;
        }
        return slowestTime;
    }

}
