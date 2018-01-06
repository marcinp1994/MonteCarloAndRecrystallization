/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rekrystalizacja;

/**
 *
 * @author Marcin
 */
public class Komorka {
    
    private int id;
    private int H;

    public int getH() {
        return H;
    }

    public void setH(int H) {
        this.H = H;
    }
    private double q;
    private boolean isRec;
    private boolean willRec;
 
   
 
 
    Komorka(){
        id=-1;
        q=0.0;
        isRec=false;
        willRec=false;
        H=5;
       
    }
 
    public boolean isWillRec() {
        return willRec;
    }
 
 
    public void setWillRec(boolean willRec) {
        this.willRec = willRec;
    }
 
   
    public boolean isRec() {
        return isRec;
    }
 
 
    public void setRec(boolean isRec) {
        this.isRec = isRec;
    }
 
 
    public double getQ() {
        return q;
    }
 
 
    public void setQ(double q) {
        this.q = q;
    }
 
 
    public int getId() {
        return id;
    }
 
 
    public void setId(int id) {
        this.id = id;
    }
}
    

