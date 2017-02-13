package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class server {
	public static void main(String[] args) throws Exception {
		
		//Get current directory
		File cwd = new File(".");
		cwd = cwd.getAbsoluteFile().getParentFile().getParentFile(); //This code need some work.  It is probably an AWFUL way to do things
		System.out.println(cwd.getAbsolutePath());
		//cwd = new File(cwd.getAbsoluteFile() + "\\..");
		//cwd = cwd.getAbsoluteFile().getParentFile();
		//System.out.println(cwd.getAbsolutePath());

		Socket socket = (new ServerSocket(5000)).accept();

		BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream outClient = new DataOutputStream(socket.getOutputStream());
		
		while (true) {
			String line = inClient.readLine();
			
			if(line != null){
				String[] arr = line.split(" ");
				
				if(arr[0].equals("get") && arr.length == 2){ //check if the command is get and if there is an argument
					//determine if file exists
					File getFile = new File(cwd.getAbsolutePath() + "\\" + arr[1]);
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
					outClient.write(bytes, 0, bytes.length);
					
					System.out.println("finished sending file");
				}else if(arr[0].equals("PUT") && arr.length == 3){ //check if command is put and the amount is arguments is correct
					File newFile = new File(cwd + "\\" + arr[1]);
					FileOutputStream fos = new FileOutputStream(newFile);
					
					char[] bytes = new char[Integer.parseInt(arr[2])];
					inClient.read(bytes);

					fos.write(new String(bytes).getBytes());
					fos.close();
				}else if(arr[0].equals("ls") && arr.length == 1){
					String[] dir = cwd.list();
					
					String output = "";
					for(int i = 0; i < dir.length-1; i++){
						output += dir[i] + "?"; //a question mark is illegal in file names
					}
					output += dir[dir.length-1] + "\n";
					
					outClient.writeBytes(output);
				}else if(arr[0].equals("pwd") && arr.length == 1){
					outClient.writeBytes(cwd.getAbsolutePath() + "\n");
				}else if(arr[0].equals("cd") && arr.length == 2){
					if(arr[1].equals("..")){
						cwd = cwd.getAbsoluteFile().getParentFile();
					}else if(arr[1].equals(".")){
						//nothing to be done here
					}else if(arr[1].equals("/")){
						cwd = new File("\\");
					}else{
						cwd = new File(cwd.getAbsolutePath() + "\\" + arr[1]);
					}
		
					outClient.writeBytes(cwd.getAbsolutePath() + "\n");
				}else{
				
					outClient.writeBytes("Command not recognized\n");
				}
			}
		}
	}
}
