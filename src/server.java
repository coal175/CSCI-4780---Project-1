package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) throws Exception {
		
		//Get current directory
		File file = new File("");
		System.out.println(file.getAbsolutePath());

		Socket socket = (new ServerSocket(5000)).accept();

		BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		while (true) {
			
			String line = inClient.readLine();
			if(line != null){
				String[] arr = line.split(" ");
				
				if(arr[0].equals("get") && arr.length == 2){ //check if the command is get and if there is an argument
					//determine if file exists
					File getFile = new File(file.getAbsolutePath() + "\\" + arr[1]);
					
					if(!getFile.exists()){
						System.out.println("return does not exist");
					}
					
					
				}else{
					System.out.println("return error");
				}
				
			}
		}

	}
}
