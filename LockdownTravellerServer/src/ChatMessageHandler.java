import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessageHandler extends Handler {
    Socket sendTo;
    ChatMessageRequest chatMessageRequest;
    Socket from;
    public ChatMessageHandler(Socket sendTo,ChatMessageRequest chatMessageRequest,Socket from) {
       this.chatMessageRequest=chatMessageRequest;
       this.sendTo=sendTo;
       this.from=from;
    }
    void sendMessage()
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp=sdf.format(new Date());

            try {
                PreparedStatement preparedStatement= Server.getConnection().prepareStatement("insert into Chat values(?,?,?,?);");
                preparedStatement.setString(1,chatMessageRequest.getID());
                preparedStatement.setString(2,chatMessageRequest.getMessage());
                preparedStatement.setString(3,timestamp);
                preparedStatement.setString(4,chatMessageRequest.getTo());
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        try {
            while (sendTo.isConnected())
            {
                try {
                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(sendTo.getOutputStream());
                    objectOutputStream.writeObject(chatMessageRequest);
                    objectOutputStream.flush();
                    System.out.println("Message sent");

                    ObjectInputStream objectInputStream=new ObjectInputStream(sendTo.getInputStream());
                    ChatMessageRequest response=(ChatMessageRequest) objectInputStream.readObject();
                    System.out.println("Message received");

                    ObjectOutputStream oos=new ObjectOutputStream(from.getOutputStream());
                    oos.writeObject(response);
                    System.out.println("Message Sent");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }
}
