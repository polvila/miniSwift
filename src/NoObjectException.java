


public class NoObjectException extends Exception{

	private static final long serialVersionUID = 1L;

	NoObjectException(){
		super("This object is not stored at miniSwift");
	}
}
