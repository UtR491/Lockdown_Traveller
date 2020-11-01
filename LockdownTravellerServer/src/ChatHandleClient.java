//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class ChatHandleClient {
//    private Socket socket;
//     ObjectOutputStream objectOutputStream;
//     private ChatMessageRequest chatMessageRequest;
//
//
//    public ChatHandleClient(ObjectOutputStream objectOutputStream,ChatMessageRequest chatMessageRequest,Socket socket) {
//        this.objectOutputStream = objectOutputStream;
//        this.chatMessageRequest=chatMessageRequest;
//        this.socket=socket;
//    }
//
//    public void run() {
//        String username=null;
//        try {
//            username= (String) this.objectInputStream.readObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Disconnected");
//            Server.chat_map.remove(username,socket);
//            return;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Server.chat_map.put(username,socket);
//        while (true) {
//            Chat_Message message=null;
//            try {
//                message = (Chat_Message) this.objectInputStream.readObject();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Disconnected");
//                Server.chat_map.remove(username,socket);
//                return;
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            String to;
//            assert message != null;
//            to= message.getTo();
//            Socket receiver;
//            receiver=   Server.chat_map.get(to);
//            if(receiver==null){
//                String userid= bookingRequest.getUserId();
//                try {
//                    PreparedStatement preparedStatement= Server.getConnection().prepareStatement("insert into Notification values (?,?,1)");
//                    preparedStatement.setString(1, userid);
//                    preparedStatement.setString(2, String.valueOf(message));
//                    preparedStatement.executeQuery();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//                continue;
//            }
//            try {
//                ObjectOutputStream receiverStream = new ObjectOutputStream(receiver.getOutputStream());
//                receiverStream.writeObject(message);
//                receiverStream.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
