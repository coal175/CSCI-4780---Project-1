package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class client {

	public static void main(String[] args) throws Exception{
		
		File cwd = new File("C:\\Users\\Kylem\\workspace\\server");
		
		Socket socket = new Socket("localhost", 5000);
		DataOutputStream outServer = new DataOutputStream(socket.getOutputStream());
		BufferedReader inServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		Scanner scanner = new Scanner(System.in);
		
		for(int i = 0; i < 15; i++){
			System.out.print("> ");
			String s = scanner.nextLine();
			
			//check to see if put command is used.   if yes, then add on metadata and send command
			String[] inputArray = s.split(" ");
			if(inputArray[0].equals("put") && inputArray.length == 2){
				
				//check if file exists
				File outFile = new File(cwd.getAbsolutePath() + "\\" + inputArray[1]);
				if(!outFile.exists()){
					System.out.println(inputArray[1] + " does not exist");
					continue;
				}
				
				byte[] bytes = new byte[(int) outFile.length()];
				FileInputStream fs = new FileInputStream(outFile);
				fs.read(bytes);
				fs.close();
				
				outServer.writeBytes("PUT " + inputArray[1] + " " + bytes.length + "\n"); //tell server to prepare to receive file
				outServer.write(bytes, 0, bytes.length); //This could probably be simplifed to just outServer.write(bytes)
				
				System.out.println("finished sending file");
				continue;
			}else if(inputArray[0].equals("quit")){
				outServer.writeBytes(s + "\n");
				socket.close();
				break;
			}
			
			outServer.writeBytes(s + "\n");
			
			//receive output and handle it based on command
			String basicInput = inServer.readLine();
			String[] input = basicInput.split(" ");
			
			if(input[0].equals("GET")){
				System.out.println("getting ready to write file");
				
				File file = new File(input[1]);
				FileOutputStream fos = new FileOutputStream(file);
				
				char[] bytes = new char[Integer.parseInt(input[2])];
				inServer.read(bytes);

				fos.write(new String(bytes).getBytes());
				fos.close();
				
				System.out.println("finished writing");
			}if(inputArray[0].equals("ls") && inputArray.length ==1){
				//fix formatting
				String[] listings = input[0].split("\\?");
				for(int k = 0; k < listings.length; k++){
					System.out.println(listings[k]);
				}
			}else{
				System.out.println(basicInput);
			}
		}
		
		outServer.close();
		scanner.close();
		socket.close();
	}

}
