//package Client.src.com.company;
//
//import java.awt.font.TextHitInfo;
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
//            Message message=null;
//            try {
//                message= (Message) this.inputStream.readObject();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            System.out.println(message);
//        }
//
//    }
//
//}
