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

public class ServerThread implements Runnable {
  // caller will create a socket and send it to the 'this' in the constructor
  Socket clientSocket = null;

  public ServerThread(Socket clientSocket) {
    // this thread is now using the socket the caller (server) accepted
    this.clientSocket = clientSocket;
  }

  // ServerThread implements Runnable.  In threading, run is essentially the
  // main method of the thread.  Calling 'ServerThreadInstance'.start() in the
  // server will run this method.
  public void run() {
    // call the loop method on this threads socket (assigned in the constructor)
    try {
      loop(this.clientSocket);

      // loop returns once 'quit' is called
      // close the client and return out of the method
      this.clientSocket.close();

    } catch (Exception e) {
      System.err.println("Error");
    }
  }

  // helper method which does all of the logic for processing the input
  public static void loop(Socket socket) throws Exception{
      //Get current directory
      File cwd = new File(".");
      cwd = cwd.getAbsoluteFile().getParentFile(); //This code need some work.  It is probably an AWFUL way to do things

      BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      DataOutputStream outClient = new DataOutputStream(socket.getOutputStream());

      while (true) {
        String line = inClient.readLine();

        if(line != null){
          String[] arr = line.split(" ");

        if(arr[0].equals("get") && arr.length == 2){ //check if the command is get and if there is an argument
            //determine if file exists
            File getFile = new File(cwd.getAbsolutePath() + "/" + arr[1].trim());
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

            outClient.writeBytes("GET " + arr[1].trim() +  " " + bytes.length + "\n");
            outClient.write(bytes, 0, bytes.length);

            System.out.println("finished sending file");
        }else if(arr[0].equals("PUT") && arr.length == 3){ //check if command is put and the amount is arguments is correct
            File newFile = new File(cwd + "/" + arr[1].trim());
            FileOutputStream fos = new FileOutputStream(newFile);

            char[] bytes = new char[Integer.parseInt(arr[2])];
            inClient.read(bytes);

            fos.write(new String(bytes).getBytes());
            fos.close();
        }else if(arr[0].equals("ls") && arr.length == 1){
            String[] dir = cwd.list();

            String output = "";
            for(int i = 0; i < dir.length; i++){
              output += dir[i] + "?"; //a question mark is illegal in file names
            }

            outClient.writeBytes(output + "\n");
        }else if(arr[0].equals("pwd") && arr.length == 1){
            outClient.writeBytes(cwd.getAbsolutePath() + "\n");
        }else if(arr[0].equals("cd") && arr.length == 2){
            if(arr[1].equals("..")){
              cwd = cwd.getAbsoluteFile().getParentFile();
            }else if(arr[1].equals(".")){
              //nothing to be done here
            }else if(arr[1].equals("/")){
              cwd = new File("/");
            }else{
              File temp = new File(cwd.getAbsolutePath() + "/" + arr[1].trim());
              if(!temp.exists()){
                outClient.writeBytes("directory does not exist\n");
              continue;
              }
              cwd = new File(temp.getAbsolutePath());
            }

            outClient.writeBytes(cwd.getAbsolutePath() + "\n");
        }else if(arr[0].equals("delete") && arr.length == 2){
            File deleteFile = new File(cwd.getAbsolutePath() + "/" + arr[1]);

            if(!deleteFile.exists()){
              outClient.writeBytes("File does not exist\n");
              continue;
            }
            System.out.println("File found");

            if(!deleteFile.delete()){
          outClient.writeBytes("Delete failed\n");
            }else{
          outClient.writeBytes("Delete Succeeded\n");
            }
        }else if(arr[0].equals("mkdir") && arr.length == 2){
            File newDirectory = new File(cwd.getAbsolutePath() + "/" + arr[1]);
            System.out.println(newDirectory.getAbsolutePath());

            if(newDirectory.exists()){
          outClient.writeBytes("Folder already exists\n");
          continue;
            }
            System.out.println("Folder not found");

            if(!newDirectory.mkdir()){
          outClient.writeBytes("mkdir failed\n");
            }else{
          outClient.writeBytes("mkdir succeeded\n");
            }
        }else if(arr[0].equals("quit") && arr.length == 1){
            return;
        }else{
            outClient.writeBytes("Command not recognized\n");
        }
        }
      }
    }
}
