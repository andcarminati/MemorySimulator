package simulator.allocators;
import simulator.core.AbstractAllocator;
import simulator.core.Chunk;

public class FirstFit extends AbstractAllocator{

	public FirstFit(int size) {
		super(size);
		// TODO Stub de construtor gerado automaticamente
	}

	@Override
	public Chunk findFreeChunk(int size) {
		
		Chunk chunk = getInitialChunk();
		while(chunk != null){
			if(size < chunk.size() && chunk.isAvailable()){
				return chunk;
			}
			chunk = chunk.getNext();
		}
		return null;
	}

}
