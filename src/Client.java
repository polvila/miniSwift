//package miniSwift;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Client {
	public static void main( String argv[ ] ) {
		API api;
		try{
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

			api = (API) Naming.lookup("//localhost:1099/proxy");

			//Menu to interact with client
			System.out.println("Welcome to the miniSwift Object Store\nPlease select an option:\n"
					+ "\t1.\tStore an object into the miniSwift\n"
					+ "\t2.\tRetrieve an object stored at miniSwift\n"
					+ "\t3.\tDelete an object from miniSwift\n"
					+ "\t4.\tExit");
			String option = keyboard.readLine();
			String key;
			String value;
			boolean exit = false;
			while(!exit){
				switch(option){
				case "1":
					System.out.println("Please introduce the object id");
					key = keyboard.readLine();
					System.out.println("Please introduce the value to store");
					value = keyboard.readLine();
					api.put(key, value);
					System.out.println("Object introduced succesfully in the miniSwift");
					break;
				case "2":
					System.out.println("Please introduce the object id");
					key = keyboard.readLine();
					String data = api.get(key);
					if(data == null){
						System.out.println("This object is not stored at miniSwift");
					}else{
						System.out.println("Your data:");
						System.out.println(data + "\n");
					}
					break;
				case "3":
					System.out.println("Please introduce the object id you want to delete");
					key = keyboard.readLine();
					try{
						api.delete(key);
						System.out.println("Object deleted succesfully from miniSwift");
					}catch (NoObjectException e){
						System.out.println("This object is not stored at miniSwift");
					}catch (RemoteException e){
						System.out.println("There was a problem while deleting the object from miniSwift");
					}


					break;
				case "4":
					System.out.println("Thank you and see you soon :)");
					exit = true;
					break;
				default : 
					System.out.println("Option no valid");

					break;
				}
				if(!option.equals("4")){
					System.out.println("Please select an option:\n"
							+ "\t1.\tStore an object into the miniSwift\n"
							+ "\t2.\tRetrieve an object stored at miniSwift\n"
							+ "\t3.\tDelete an object from miniSwift\n"
							+ "\t4.\tExit");
					option = keyboard.readLine();
				}

			}


		}catch (Exception e){
			e.printStackTrace();
		}

	} 
}
