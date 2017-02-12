package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class client {

	public static void main(String[] args) throws Exception{
		
		Socket socket = new Socket("localhost", 5000);
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		//DataInputStream ds = new DataInputStream(socket.getInputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		Scanner scanner = new Scanner(System.in);
		
		for(int i = 0; i < 3; i++){
			System.out.print("> ");
			String s = scanner.nextLine();
			
			out.writeBytes(s + "\n");
			
			//receive output, check if receiving any file
			String basicInput = in.readLine();
			String[] input = basicInput.split(" ");
			if(input[0].equals("GET")){
				System.out.println("getting ready to write file");
				File file = new File(input[1]);
				FileOutputStream fos = new FileOutputStream(file);
				
				char[] bytes = new char[Integer.parseInt(input[2])];
				//while(true){
				
					in.read(bytes);
					//if(bytes == null){
						//break;
					//}
					fos.write(new String(bytes).getBytes());
				//}
				fos.close();
				
				System.out.println("finished writing");
				
			}else{
				System.out.println(basicInput);
			}
			
			
		}
		
		out.close();
		
		socket.close();
	}

}
