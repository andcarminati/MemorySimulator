package simulator.core;
import simulator.allocators.FirstFit;

public class AllocatorTest {

	
	
	
	
	static void testarAlocador(AbstractAllocator alocador){
		
		int[] demandas={100, 50, 20, 40, 70, 500, 1000, 30, 10, 120, 128, 256};
		int[] enderecosIniciais = new int[demandas.length];
		
		System.out.println("Testando alocador " + alocador.getClass().getName() + ":");
		
		for (int i = 0; i < demandas.length; i++) {
			System.out.println("\nAlocando memoria para: " + demandas[i] + " words");
			enderecosIniciais[i] = alocador.allocate(demandas[i]);
			alocador.dump();
		}
		
		for (int i = 0; i < enderecosIniciais.length; i++) {
			int index = enderecosIniciais.length-i-1;
			System.out.println("\nDesalocando memoria usada para: " + 
						demandas[index] + " words"+ " - endereco:" + enderecosIniciais[index]);
			alocador.deallocate(enderecosIniciais[index]);
			alocador.dump();
			
		}
	}
	
	
	public static void main(String[] args) {
		
		AbstractAllocator alocador = new FirstFit(5000);
		testarAlocador(alocador);
		
	}
}
