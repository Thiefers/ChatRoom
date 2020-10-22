package chat.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOStream {

//	@SuppressWarnings("finally")
	public static Object readMsg(Socket socket) {
		Object object = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			object = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
//		finally {
			return object;
//		}
	}
	
	public static void writeMsg(Socket socket, Object message) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
