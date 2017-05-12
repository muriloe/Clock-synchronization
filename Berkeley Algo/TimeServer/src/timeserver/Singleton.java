/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeserver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author murilo.erhardt
 */
public class Singleton {
     private static Singleton myObj;
     private List<Long> listOfClientTime;
     private List<String> listOfClientAddr;

    public static Singleton getMyObj() {
        return myObj;
    }

    public static void setMyObj(Singleton myObj) {
        Singleton.myObj = myObj;
    }

     private Singleton(){
        listOfClientTime = new ArrayList<>();
        listOfClientAddr = new ArrayList<>();
    }
    
    public static Singleton getInstance(){
        if(myObj == null){
            myObj = new Singleton();
        }
        return myObj;
    }

    public void addClientTime(Long time){
        listOfClientTime.add(time);     
    }
    
    public List<Long> getListOfClientTime() {
        return listOfClientTime;
    }

    public void setListOfClientTime(List<Long> listOfClientTime) {
        this.listOfClientTime = listOfClientTime;
    }
    
    public List<String> getClientAddr() {
        return listOfClientAddr;
    }

    public void setClientAddr(List<String> clientAddr) {
        this.listOfClientAddr = clientAddr;
    }
    
    public void addClienteAddr(String addr){
        listOfClientAddr.add(addr);
    }
}
