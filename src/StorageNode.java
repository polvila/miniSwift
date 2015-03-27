//package miniSwift;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;

public class StorageNode extends UnicastRemoteObject implements NodeInterface {
	private static final long serialVersionUID = 1L;
	static NodeInterface obj;
	private HashMap<String, String> storage;
	
	public StorageNode() throws RemoteException {
		super();
		storage = new HashMap<String, String>();
	}

	public void replicate(String key, NodeInterface[] nodes) throws RemoteException {
		for(int i=0; i<2; i++){
			nodes[i].put(key, storage.get(key));
		}
	}
	
	public void put(String key, String dataObject) throws RemoteException {
		storage.put(key, dataObject);
	}

	public String get(String key) throws RemoteException {
		return storage.get(key);
	}

	public void delete(String key) throws RemoteException, NoObjectException  {
		String obj = storage.remove(key);
		if(obj == null) throw new NoObjectException();
	} 


	public static void main(String args[]) throws Exception  { 
		 System.setProperty("java.security.policy", "server.policy");
	     if ( System.getSecurityManager() == null ) {
	            System.setSecurityManager(new RMISecurityManager( ) );
		 }
	     
		try { 

		    obj = new StorageNode(); 
		    
		    int  id = Integer.parseInt(args[0]);
		    Naming.rebind("//localhost/s" + id, obj); 
		    System.out.println("Server s"+id+" bound in registry");
		    

		    
		} catch (Exception e) { 
		    System.out.println("Server err: " + e.getMessage()); 
		    e.printStackTrace(); 
		} 
    }

	
}
