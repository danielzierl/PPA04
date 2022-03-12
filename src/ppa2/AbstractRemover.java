	package ppa2;

	/**
	 * abstract class implementing method runTimed in interface DuplicateRemover
	 */
	public abstract class AbstractRemover implements DuplicateRemover {
		public double runTimed(int[] data) {
			long t1 = System.nanoTime();
			removeDuplicates(data);

			//return in seconds
			return ((System.nanoTime() - t1) / 1000000000.0);
		}
	}
