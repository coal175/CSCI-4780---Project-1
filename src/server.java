package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) throws Exception {
		
		//Get current directory
		File file = new File("C:\\Users\\Kylem\\workspace");
		System.out.println(file.getAbsolutePath());

		Socket socket = (new ServerSocket(5000)).accept();

		BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream outClient = new DataOutputStream(socket.getOutputStream());
		
		
		while (true) {
			
			String line = inClient.readLine();
			if(line != null){
				String[] arr = line.split(" ");
				
				if(arr[0].equals("get") && arr.length == 2){ //check if the command is get and if there is an argument
					//determine if file exists
					File getFile = new File(file.getAbsolutePath() + "\\" + arr[1]);
					System.out.println(getFile.getAbsolutePath());
					
					if(!getFile.exists()){
						outClient.writeBytes("File does not exist\n");
						continue;
					}
					System.out.println("File found");
					
					//read file into byte array
					byte[] bytes = new byte[(int) getFile.length()];
					
					FileInputStream fs = new FileInputStream(getFile);
					fs.read(bytes);
					fs.close();
					
					outClient.writeBytes("GET " + arr[1] +  " " + bytes.length + "\n");
					//for(int i = 0; i < bytes.length; i=i+8){
					outClient.write(bytes, 0, bytes.length);
						//outClient.write
					//}
					//outClient.write(null);
					
					System.out.println("finished sending file");
				}else{
					outClient.writeBytes("Command not recognized\n");
				}
				
			}
		}

	}
}
