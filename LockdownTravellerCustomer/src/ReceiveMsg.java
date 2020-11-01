//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.Socket;
//
//public class ReceiveMsg implements Runnable{
//    private Socket socket;
//    private ObjectInputStream inputStream;
//    ReceiveMsg(Socket socket) throws IOException {
//        this.socket=socket;
//        this.inputStream=new ObjectInputStream(socket.getInputStream());
//    }
//
//
//    public void run(){
//        while (true){
//            ChatMessageRequest message=null;
//            try {
//                message= (ChatMessageRequest) this.inputStream.readObject();
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            System.out.println(message);
//        }
//
//    }
//
//}
