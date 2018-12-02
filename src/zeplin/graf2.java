/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zeplin;

/**
 *
 * @author bedirhan
 */
public class graf2 {
    int plaka=0;
    int komsular[];
    boolean durum=false;
    
    
    public graf2(int plaka){
        this.plaka=plaka;
    }
    public void komsuekle(int []komsu,int n){
        komsular=new int[n];
        for (int i = 0; i < n; i++) {
            komsular[i]=komsu[i];            
        }
    }
    public String toString(){
               String result="";
               for (int i = 0; i < komsular.length; i++) {
                result+=i+"=>"+komsular[i]+"\n";
               }
               return result;
            }
    
    
}
