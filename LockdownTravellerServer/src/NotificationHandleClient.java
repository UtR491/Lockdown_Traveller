

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NotificationHandleClient {



    public class HandleClient implements Runnable {
        private Socket socket;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;
        BookingRequest bookingRequest;
        String userid= bookingRequest.getUserId();


        public HandleClient(Socket socket) {
            this.socket = socket;
            try {
                this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            Server.map.put(userid,socket);
            while (true) {
                Notification message=null;
                try {
                    message = (Notification) this.objectInputStream.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Disconnected");
                    Server.map.remove(userid,socket);
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String to;
                to= message.getTo();
                Socket receiver;
                receiver=   Server.map.get(to);
                if(receiver==null){
                    try {
                        Notification errorMessage= new Notification("Receiver not active", "server", userid);
                        objectOutputStream.writeObject(errorMessage);
                        objectOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Disconnected");
                        Server.map.remove(userid,socket);
                        return;
                    }
                    continue;
                }
                try {
                    ObjectOutputStream receiverStream = new ObjectOutputStream(receiver.getOutputStream());
                    receiverStream.writeObject(message);
                    receiverStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
