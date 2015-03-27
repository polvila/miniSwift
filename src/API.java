//package miniSwift;

import java.rmi.*;


public interface API extends Remote {

	//will allow clients to store the object based on the associated key into the object store
	public void put(String key, String dataObject) throws RemoteException;
	//will be used to retrive the data object associated with the key in the storage servers
	public String get(String key) throws RemoteException;
	//delete an objecte and all its replicas from the system
	public void delete(String key) throws RemoteException, NoObjectException;
}
