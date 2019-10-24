package simulator.core;

public class Chunk {

	private int start;
	private int finish;
	private Chunk next;
	private Chunk prev;
	private boolean available;
	
	public Chunk(int inicio, int tamanho){
		this.start=inicio;
		this.finish=inicio+tamanho-1;
		available=true;
		next=null;
		prev=null;
	}
	
	public boolean isAvailable(){
		return available;
	}
	
	public int size(){
		return finish-start+1;
	}
	
	public int getStart(){
		return start;
	}
	
	public int getFinish(){
		return finish;
	}
	
	public Chunk getNext(){
		return next;
	}
	
	public void allocate(int size){
		available = false;
		if(size() != size){
			int resto = size() - size;
			
			Chunk nextChunk = new Chunk(start+size, resto);
			this.finish = start+size-1;
			
			if(next != null){
				next.prev = nextChunk;
			}
			nextChunk.next = next;
			next = nextChunk;	
			nextChunk.prev = this;	
		}
	}
	
	public void deallocate(){
		available=true;
		if(next != null && next.available){
			int novoTamanho = size()+next.size();
			finish = start+novoTamanho-1;
			next=next.next;
		}
		if(prev != null && prev.available){
			prev.deallocate();
		}
	}
}
