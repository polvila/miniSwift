//package miniSwift;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Proxy extends UnicastRemoteObject implements API{
	private static final long serialVersionUID = 1L;
	static API obj;
	private int[][] storageNodes;
	private int numNodes;


	public Proxy(int numNodes) throws RemoteException {
		super();
		this.numNodes = numNodes;
		this.storageNodes = new int[numNodes/2][2];
		int sn=1;
		for(int f=0; f<numNodes/2; f++){
			for(int c=0; c<2; c++){
				storageNodes[f][c]= sn;
				sn++;
			}
		}
	}

	public void put(String key, String dataObject) throws RemoteException {
		try {
			long object_id = (Long) sha1(key);
			int s = (int) (object_id / (long) (Math.pow(2,32)/numNodes));
			s++;
			NodeInterface nodeInterface =  (NodeInterface) Naming.lookup("//localhost:1099/s" + s);
			nodeInterface.put(key, dataObject);

			//REPLICATE 
			NodeInterface[] nodes = new NodeInterface[(numNodes/2)-1];	//list of storage nodes to replicate
			int c;
			c= s%2!=0?0:1;		
			int i = 0;
			for (int f=0; f<numNodes/2; f++){
				if(!(storageNodes[f][c]==s)){
					nodes[i] = (NodeInterface) Naming.lookup("//localhost:1099/s" + storageNodes[f][c]);
					i++;
				}
			}

			new ThreadReplicate(nodeInterface, nodes, key).start();


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	public String get(String key) throws RemoteException {
		long object_id;
		try {
			object_id = sha1(key);
			int s = (int) (object_id / (long) (Math.pow(2,32)/6));
			s++;
			NodeInterface[] nodes = new NodeInterface[3];
			int c;
			c= s%2!=0?0:1;		
			int i = 0;
			for (int f=0; f<3; f++){
				nodes[i] = (NodeInterface) Naming.lookup("//localhost:1099/s" + storageNodes[f][c]);
				i++;
			}
			int getNode = (int) (Math.random() * 3);
			String dataObject = nodes[getNode].get(key);
			
			return dataObject;

		} catch (NoSuchAlgorithmException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void delete(String key) throws RemoteException, NoObjectException {
		long object_id;
		try {
			object_id = sha1(key);
			int s = (int) (object_id / (long) (Math.pow(2,32)/6));
			s++;
			int c;
			c= s%2!=0?0:1;		
			for (int f=0; f<3; f++){
				NodeInterface n = (NodeInterface) Naming.lookup("//localhost:1099/s" + storageNodes[f][c]);
				n.delete(key);
			}

		}catch (NoObjectException e) {
			throw new NoObjectException();
		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		} catch (RemoteException e){
			e.printStackTrace();
		}

	}

	public static final int HASH_32 = 4;

	static long sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest d = MessageDigest.getInstance("SHA1");
		byte[] result = d.digest(input.getBytes());
		long hash = 0;
		for (int i = 0; i < HASH_32; i++) {
			hash |= result[i] & 0xFF;
			hash <<= 8;
		}
		return hash >> 8;
	}

	//MAIN


	public static void main(String args[]) { 
		System.setProperty("java.security.policy", "server.policy");
	     if ( System.getSecurityManager() == null ) {
	            System.setSecurityManager(new RMISecurityManager( ) );
		}
		try { 
		
			obj = new Proxy(Integer.parseInt(args[0])); 

			Naming.rebind("//localhost/proxy", obj); 
			System.out.println("Proxy bound in registry");

		} catch (Exception e) { 
			System.out.println("Server err: " + e.getMessage()); 
			e.printStackTrace(); 
		} 
	} 

}
