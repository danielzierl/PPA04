package ppa2;

	/**
	 * class containing duplicator remover 2 with method that removes duplicate entries in an array
	 */
	public class RemoveDuplicates2 extends AbstractRemover implements DuplicateRemover {
	/**
	 * Prochazi vsechny polozky a provadi ostraneni vsech duplikatu jedne polozky najednou
	 *
	 * @param data vstupni pole dat
	 * @return vysledna data s odstranenymi duplikaty
	 */
	public int[] removeDuplicates(int[] data) {
		int[] result = data;
		for (int i = 0; i < result.length; i++) {
			//spocteme, kolik ma polozka result[i] duplikatu
			int count = 0; //pocet duplikatu
			for (int j = i + 1; j < result.length; j++) {
				if (result[j] == result[i]) {
					count++;
				}
			}
			//pokud je alespon jeden duplikat, pak ho odstranime
			if (count > 0) {
				//vysledek bude o count kratsi
				int[] newResult = new int[result.length - count];
				//prvky az do indexu i muzeme jednoduse zkopirovat
				for (int k = 0; k <= i; k++) {
					newResult[k] = result[k];
				}
				int index = i + 1; //index v cilovem poli
				for (int k = i + 1; k < result.length; k++) {
					if (result[k] != result[i]) { //neni duplikat
						newResult[index] = result[k];
						index++;
					}
				}

				result = newResult;

			}
		}
		return result;
	}

	/**
	 * @return name of the duplicate remover
	 */
	public String toString() {
		return "Remove Duplicates 2";
	}

}
