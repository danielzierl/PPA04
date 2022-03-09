package ppa2;

/**
 * class containing duplicator remover 1 with method that removes duplicate entries in an array
 */
public class RemoveDuplicates1 extends AbstractRemover implements DuplicateRemover{
	/**
	 * Odstrani z pole prvek na indexu index
	 * @param data vstupni pole dat
	 * @param index index polozky, ktera se ma odstranit
	 * @return vysledne pole bez jedne polozky
	 */
	public static int[] removeItem(int[] data, int index) {
		//vysledne pole bude o 1 kratsi
		int[] result = new int[data.length - 1];
		//zkopirujeme prvky az do indexu i
		for (int i = 0; i < index; i++) {
			result[i] = data[i];
		}
//i-ty prvek preskocime a zkopirujeme vsechny zbyvajici prvky
		for (int i = index + 1; i < data.length; i++) {
			result[i - 1] = data[i];
		}
		return result;
	}

	/**
	 * Prochazi vsechny polozky a odstranuje duplikaty metodou removeItem()
	 * @param data vstupni pole dat
	 * @return vysledna data s odstranenymi duplikaty
	 */
	public int[] removeDuplicates(int[] data) {
		int[] result = data;
		for (int i = 0; i < result.length; i++) {
			for (int j = i + 1; j < result.length; j++) {
				if (result[j] == result[i]) {
					result = removeItem(result, j);
					j--;

				}
			}
		}
		return result;
	}

	/**
	 *
	 * @return name of the duplicate remover
	 */
	public String toString(){
		return "Remove Duplicates 1";
	}


}
