
public class WorstFit extends AbstractAllocator{

	public WorstFit(int tamanho) {
		super(tamanho);
		// TODO Stub de construtor gerado automaticamente
	}

	@Override
	Chunk findFreeChunk(int size) {
		
		Chunk biggestChunk = initialChunk;
		Chunk chunk = initialChunk;
		
		while(chunk != null){
			if(size < chunk.size() && chunk.isAvailable()){
				if(chunk.size() > biggestChunk.size()){
					biggestChunk = chunk;
				}
				
			}
			chunk = chunk.getNext();
		}
		
		if(biggestChunk.size() < size){
			biggestChunk = null;
		}
		
		return biggestChunk;
	}

}
