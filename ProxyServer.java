package Proxy;

import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class ProxyServer {

	//cache is a Map: the key is the URL and the value is the file name of the file that stores the cached content
	Map<String, String> cache;
	
	ServerSocket proxySocket;

	DataOutputStream os;
	DataInputStream is;

	String logFileName = "proxy.log";

	public static void main(String[] args) {
		new ProxyServer().startServer(Integer.parseInt(args[0]));
	}

	void startServer(int proxyPort) {

		cache = new ConcurrentHashMap<>();

		// create the directory to store cached files. 
		File cacheDir = new File("cached");
		if (!cacheDir.exists() || (cacheDir.exists() && !cacheDir.isDirectory())) {
			cacheDir.mkdirs();
		}

		/**
			 * To do:
			 * create a serverSocket to listen on the port (proxyPort)
			 * Create a thread (RequestHandler) for each new client connection 
			 * remember to catch Exceptions!
			 *
		*/
		try {
		proxySocket = new ServerSocket(8008);
		Socket client = proxySocket.accept();
		is = new DataInputStream(client.getInputStream());
		os = new DataOutputStream(client.getOutputStream());

		String line = is.readLine();

		os.writeBytes("Hello\n");

		System.out.println("First print = " + line);
		System.out.println("Second print = test");

		writeLog(line);

		client.close();
		}
		catch(Exception e){
			System.out.println(e);
		}

		
	}



	public String getCache(String hashcode) {
		return cache.get(hashcode);
	}

	public void putCache(String hashcode, String fileName) {
		cache.put(hashcode, fileName);
	}

	public synchronized void writeLog(String info) {
		
			/**
			 * To do
			 * write string (info) to the log file, and add the current time stamp 
			 * e.g. String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			 *
			*/
		try{
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			info = "Date: " + timeStamp + " " + info;

			FileWriter fw = new FileWriter("proxy.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			pw.println(info + "\n");

			pw.flush();

			pw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}