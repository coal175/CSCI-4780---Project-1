package client;

import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.Scanner;

public class client {

	public static void main(String[] args) throws Exception{
		
		File file = new File(".");
		System.out.println(file.getAbsolutePath());
		
		Socket socket = new Socket("localhost", 5000);
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		Scanner scanner = new Scanner(System.in);
		
		for(int i = 0; i < 3; i++){
			System.out.print("> ");
			String s = scanner.nextLine();
			System.out.println("sending");
			
			out.writeBytes(s + "\n");
			
			//receive output
		}
		
		out.close();
		
		socket.close();
	}

}
