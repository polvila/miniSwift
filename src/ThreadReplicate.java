//package miniSwift;

public class ThreadReplicate extends Thread{
	private NodeInterface[] s;
	private String key;
	private NodeInterface n;
	
	public ThreadReplicate(NodeInterface n, NodeInterface[] s, String key){
		super();
		this.s = s;
		this.key = key;
		this.n = n;
	}
	
	public void run() {
		try {
			n.replicate(key, s);
		}catch ( Exception e){
			e.printStackTrace();
		}
	}
}
