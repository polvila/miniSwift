//package miniSwift;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeInterface extends Remote {
	//replicates the key-value pair to the list of nodes
	public void replicate(String key, NodeInterface[] node) throws RemoteException;
	//will allow clients to store the object based on the associated key into the object store
	public void put(String key, String dataObject) throws RemoteException;
	//will be used to retrive the data object associated with the key in the storage servers
	public String get(String key) throws RemoteException;
	//delete an objecte and all its replicas from the system
	public void delete(String key) throws RemoteException, NoObjectException;
}
