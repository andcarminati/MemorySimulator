public abstract class AbstractAllocator {

	protected Chunk initialChunk;

	public AbstractAllocator(int size) {
		initialChunk = new Chunk(0, size);
	}

	public int allocate(int size) {
		// normalize to multiples of 10 bytes
		if(size % 10 != 0){
			int div = size/10;
			size = (div+1)*10;
		}
		
		int startAddress = -1;
		Chunk chunk = findFreeChunk(size);
		if(chunk != null){
			chunk.allocate(size);
			startAddress = chunk.getStart();
		}
		return startAddress;
	}

	public void deallocate(int mem) {

		Chunk chunk = findChunk(mem);
		if (chunk != null) {
			chunk.deallocate();
		} else {
			System.out.println("Chunk not found!");
		}

	}

	protected Chunk findChunk(int inicio) {

		Chunk bloco = initialChunk;

		while (bloco != null) {
			if (bloco.getStart() == inicio) {
				return bloco;
			}
			bloco = bloco.getNext();
		}
		return null;
	}

	abstract Chunk findFreeChunk(int size);
	
	public void dump(){
		
		Chunk chunk = initialChunk;
		
		while(chunk != null){
			String status = chunk.isAvailable() ? "(L)" : "(U)";
			System.out.print("[" + chunk.getStart() + status + chunk.getFinish() + "]");
			chunk = chunk.getNext();
		}
		System.out.println("");
	}
}
