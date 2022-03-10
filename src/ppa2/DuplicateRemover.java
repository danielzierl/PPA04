	package ppa2;

	/**
	 * interface implementing common behaviour of a duplicate remover -> removing duplicates and running with measured time
	 */
	public interface DuplicateRemover {
		int[] removeDuplicates(int[] data);

		double runTimed(int[] data);
	}
