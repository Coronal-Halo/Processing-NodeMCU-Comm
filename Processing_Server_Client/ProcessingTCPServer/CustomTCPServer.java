import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/* A simple TCP server that listens to one port, reads input message from client and sends back response*/

class CustomTCPServer implements Runnable{
  Socket socket = null;
  ObjectOutputStream oos = null;
  ObjectInputStream ois = null;
  String host = "192.168.43.195";
  int port_server = 80;
  
  String inputStreamData;                             //the Data in the inputStream; seperately declared from input since this variable is used to monitor the state of br.readline()
  String inputMsg = null;			                        //Input message from client
  String resp = null;                                //Response from the server
  boolean shutdown = false;    	                      	//Flag to indicate is the server should keep running
  public ServerSocket server;	                        //public ServerSocket object

  public void run() {
      try {
          //Create the socket server object
          server = new ServerSocket(port_server);		
          //server.setSoTimeout(6000);
          
          //keep listens for 6 secs or until receives 'exit' call from client or is manually shutdown
          while(!shutdown){
              System.out.println("Waiting for client..."); 
             
              // Creating socket and waiting for client connection
              Socket socket = server.accept();		
              
              // Creating input and output IOStreams to reveive request and send response
              BufferedReader br = new BufferedReader(
                      new InputStreamReader(socket.getInputStream()));
              BufferedWriter bw = new BufferedWriter(
                      new OutputStreamWriter(socket.getOutputStream()));
              //DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
              //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
              
              //Keep consuming the input buffer till finish reading the message from client
              while (true) {
                // NOTE: The following line keeps consuming input data; br.readLine() goes back to NULL after being consumed
                  inputStreamData = br.readLine();
                  if (inputStreamData == null || inputStreamData.equals(".")) {
                      break;
                  }
                  //If a not-null input msg is detected, Send input back to client as response 
                  //and stores the client msg in the "input" variable
                  resp = inputStreamData;
                  inputMsg = inputStreamData;
                  bw.write(resp);    
                  bw.flush();
                  System.out.println("client input is: " + inputMsg + "; Server resposne is: " + resp);
                  //dOut.writeByte(1);
                  //oos.writeByte(1);
              }
        
              //close resources
              socket.close();
        
              //terminate the server if server sends exit request
              if(inputStreamData != null && inputStreamData.equalsIgnoreCase("exit")) break;
          }
          
          server.close();
          System.out.println("Server shut down");
        } catch (IOException e1) {
			  //If ever occurs IOException, close the server
          System.out.println("IOException on server mode.");
          if (server != null && !server.isClosed()){
            try {
              server.close(); 
              System.out.println("Server shut down");
            } catch (IOException e2) {
              e2.printStackTrace(System.err); 
            }
          }
        }
    }
    
    public void shutdown(boolean status) {
      shutdown = status;
      if(shutdown == true) {
        System.out.println("Shutting down Socket server...");
      }
    }
    
    // Return the most recent input from the client
    public String getClientInput() {
      return inputMsg; 
    }
    
    // Return the most recent response from this server
    public String getServerResponse() {
      return resp;
    }
}
