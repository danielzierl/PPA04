package ppa2;

public abstract class AbstractRemover implements DuplicateRemover{
	public double runTimed(int[] data) {
		long t1 = System.currentTimeMillis();
		removeDuplicates(data);
		long t2 =System.currentTimeMillis();
		return ((t2-t1)/1000.0);
	}
}
