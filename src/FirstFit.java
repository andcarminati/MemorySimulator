
public class FirstFit extends AbstractAllocator{

	public FirstFit(int size) {
		super(size);
		// TODO Stub de construtor gerado automaticamente
	}

	@Override
	Chunk findFreeChunk(int size) {
		
		Chunk chunk = initialChunk;
		while(chunk != null){
			if(size < chunk.size() && chunk.isAvailable()){
				return chunk;
			}
			chunk = chunk.getNext();
		}
		return null;
	}

}
