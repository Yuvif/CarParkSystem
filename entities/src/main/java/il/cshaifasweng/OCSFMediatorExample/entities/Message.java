package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message implements Serializable{
    int id;
    LocalDateTime timeStamp;
    String message;
    String data;
    ArrayList<?> table = null;
    Object obj = null;

    public Message(int id, LocalDateTime timeStamp, String message) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public Message(int id, String message) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = null;
    }

    public Message(int id, String message,String data) {
        this.id = id;
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public Message(ArrayList<?> a_t){
        this.table = a_t;
    }

    public Message(Object edited,String message){
        this.obj = edited;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(ArrayList<?> a_t){this.table = a_t;}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<?> getTable(){
        return table;
    }

    public Object getObj(){
        return obj;
    }

    public void setMessage(String msg, ArrayList<Price> p_t) {
        this.table = p_t;
        this.message = msg;
    }
}
