package ppa2;

	/**
	 * class containing duplicator remover 3 with method that removes duplicate entries in an array
	 */
	public class RemoveDuplicates3 extends AbstractRemover implements DuplicateRemover {
	/**
	 * Pouziva redukci pomoci pole zaznamu, zda dane cislo bylo nalezeno v datech ci nikoli
	 *
	 * @param data vstupni pole dat
	 * @return vysledna data s odstranenymi duplikaty
	 */
	public int[] removeDuplicates(int[] data) {
	//nejdrive jen zjistime, kolik mame unikatnich cisel
		boolean[] encountered = new boolean[1000000];
		int count = 0; //pocet unikatnich cisel
		for (int i = 0; i < data.length; i++) {
			if (!encountered[data[i]]) { //nove objevene cislo
				encountered[data[i]] = true;
				count++;
			}
		}
	//v promenne count je ted pocet unikatnich cisel
	//pole encountered ted pouzijeme jeste jednou stejnym zpusobem
		encountered = new boolean[1000000];
		int[] result = new int[count];
		int index = 0;
		for (int i = 0; i < data.length; i++) {
			if (!encountered[data[i]]) {
				result[index] = data[i];
				encountered[data[i]] = true;
				index++;
			}
		}
		return result;
	}

	/**
	 * @return name of the duplicate remover
	 */
	public String toString() {
		return "Remove Duplicates 3";
	}

}
