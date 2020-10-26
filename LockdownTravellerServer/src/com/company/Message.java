package com.company;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String from;
    private String to;


    public Message(String message, String from, String to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }


    public String getTo(){
        return this.to;
    }
    public String getMessage(){
        return this.message;
    }



    public String toString() {
        return String.format("Message : %s\nFrom : %s\nTo : admin"
                , message, from, to);

    }

}
