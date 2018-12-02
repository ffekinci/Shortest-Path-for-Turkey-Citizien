package zeplin;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;


import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

/**
 *
 * @author bedirhan
 */
public class Zeplin {

    public static int[][] komsuSehir1 = new int[82][];
    public static int[][] yollar = new int[250][2];
    public static final double R = 6372.8; // In kilometers
    public static double[][] kordi = new double[82][3];//0 enlem    1 boylam     2 rakım
    public static int[][] yolcuk = new int[82][82];
    public static int[] ek = new int[82];
    public static double maliyet[][] = new double[82][82];
    public static double diji[][];
    public static int dboyut;
    public static int komsular [][]=new int[82][14];
    
    
    public Zeplin(double s1,double s2) {
        komsulukal(s1);
        enlemboylamal(s2);
    }

    public boolean eklenmismi(int a) {
        for (int i = 0; i < 81; i++) {
            if (ek[i] == a) {
                return true;
            }
        }
        return false;
    }

    public void ekliiller() {
        for (int i = 0; i < 81; i++) {
            if (ek[i] != 0) {
                System.out.print(ek[i] + " ");
            }
        }
        System.out.println("");
    }

    public boolean komsusumu(int x, int y) {
        for (int i = 0; i < komsuSehir1[x].length; i++) {
            if (komsuSehir1[x][i] == y) {
                return true;
            }
        }
        return false;
    }
    
    public int dizisay(int dizi[]){
        dboyut=0;
        for (int i = 0; i < dizi.length; i++) {
            if(dizi[i]!=0)
                dboyut++;
            
        }
        return dboyut;
    }

    
    

    public boolean ykontrol(int x, int y) {

        if (yolcuk[x][y] == 1) {
            return true;
        }
        return false;
    }

    public double maliyet(double mesafe, int yükseklik) {
        double maliyet = Math.sqrt(Math.pow(mesafe, 2) + Math.pow(yükseklik / 1000, 2));
        return maliyet;
    }

    public void enlemboylamal(double s2) {
        long starttime = System.nanoTime();
        File f = new File("kordinat.txt");
        double[] lat = new double[82];
        double[] lng = new double[82];
        int[] plaka = new int[82];
        int[] rakim = new int[82];

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String satir = br.readLine();

            for (int i = 1; i < 82; i++) {

                //System.out.println(satir);
                // System.out.println("");
                satir = br.readLine();
                String[] parca = satir.split(",");
                kordi[i][0] = Double.parseDouble(parca[0]);
                //System.out.println(lat[i]);

                kordi[i][1] = Double.parseDouble(parca[1]);
                //System.out.println(lng[i]);

//                String last = satir.substring(16);
//                int son = last.indexOf(',');
                plaka[i] = Integer.parseInt(parca[2]);
                //System.out.println(plaka[i]);

                kordi[i][2] = Integer.parseInt(parca[3]);
                //System.out.println(rakim[i]);
                //System.out.println(kordi[i][0]+" "+kordi[i][1]+" "+kordi[i][2]+"");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        long endtime = System.nanoTime();
        
        s2 = (double)(endtime-starttime)/1000000000;
        System.out.println("Enlem boylam yükseklik bilgilerinin okunması için geçen süre: "+s2);
    }

    public double mesafe(double enlem1, double boylam1, double enlem2, double boylam2) {

        double enlem = Math.toRadians(enlem2 - enlem1);
        double boylam = Math.toRadians(boylam2 - boylam1);
        enlem1 = Math.toRadians(enlem1);
        enlem2 = Math.toRadians(enlem2);

        double a = Math.sin(enlem / 2) * Math.sin(enlem / 2) + Math.sin(boylam / 2) * Math.sin(boylam / 2) * Math.cos(enlem1) * Math.cos(enlem2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    public double egim(int bas, int bit, int kalkis, int inis,int n) {
        double mesafe = mesafe(kordi[bas][0], kordi[bas][1], kordi[bit][0], kordi[bit][1]);
        double y1= kordi[bas][2]; double y2= kordi[bit][2];
        if ((bas != kalkis) && (bas != inis)) {
            y1 += 50.0;
        }
        if ((bit != kalkis) && (bit != inis)) {
            y2 += 50.0;
        }
       
        double yukseklik = y1 - y2;
        double son = Math.atan(Math.abs(yukseklik) / mesafe);

        maliyet[bas][bit] = maliyet[bit][bas] = Math.sqrt(Math.pow(mesafe, 2) + Math.pow(yukseklik/1000, 2));

        if(n==5)
            System.out.print(bas + " ile " + bit + " arasındaki mesafe: " + mesafe + "  yükseklik:  " + Math.abs(yukseklik) + " Maliyet: " + maliyet[bas][bit] + "  Eğim: " + Math.toDegrees(son) + "   \n");

        return Math.toDegrees(son);
    }
    
  
     

    public boolean yolvarmi(int yolcu, double egim) {
        if (egim <= 80 - yolcu) {
            return true;
        }
        return false;
    }

    void komsulukal(double s1) {
        long starttime = System.nanoTime();
        File Dosya = new File("komsulukplaka2.txt");
        String s;
        try {
            FileReader fr = new FileReader(Dosya);
            BufferedReader br = new BufferedReader(fr);
            for (int k = 1; k < 82; k++) {
                s = br.readLine();
                String[] parca = s.split(",");
                //System.out.println(parca.length);
                // System.out.println(parca[0]+" "+parca[1]+" "+parca[2]+" "+parca[3]+" "+parca[4]+" "+parca[5]+" "+parca[6]+" ");
                komsuSehir1[k] = new int[parca.length];
                for (int j = 0; j < parca.length; j++) {
                    komsuSehir1[k][j] = Integer.parseInt(parca[j]);
                }
            }

            fr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        long endtime = System.nanoTime();
        s1 = (double)(endtime-starttime)/1000000000;
        System.out.println("Komsuluk bilgilerinin okunması için geçen süre: "+s1);
    }

    void yaz(int [] dizi) {
        System.out.println("");
        for (int i = 0; i < dizisay(dizi); i++) {
           System.out.print(dizi[i]+" ");
        }
        System.out.println("");
    }

    public boolean yolkontrol(int a, int b) {
        int sayac = 0;
        for (int i = 1; i < 250; i++) {
            if ((yollar[i][0] == a && yollar[i][1] == b) || (yollar[i][0] == b && yollar[i][1] == a)) {
                sayac++;
            }
        }
        if (sayac == 0) {
            return false;
        } else {
            return true;
        }
    }
    public void sifirla(int matris[][]){
        for (int i = 0; i < matris.length; i++) {
            for (int j = 0; j < matris[i].length; j++) {
                matris[i][j]=0;
            }
        }
    }
    public void sifirla(double matris[][]){
        for (int i = 0; i < matris.length; i++) {
            for (int j = 0; j < matris[i].length; j++) {
                matris[i][j]=0;
            }
        }
    }
    public void sifirladizi(int dizi[]){
        for (int i = 0; i < dizi.length; i++) {
           dizi[i]=0;
        }
    }
    
    public void Kaydet(String d,String s1) {
       
        try {
            File dosya = new File(d);
            
            FileWriter yazici = new FileWriter(dosya);
            
            BufferedWriter yaz = new BufferedWriter(yazici);
            String parca[]=s1.split("/");
            for (int i = 0; i < parca.length; i++) {
                yaz.write(parca[i]);
                yaz.newLine();
            }
            
            
            yaz.close();
            
        } catch (Exception hata) {
            hata.printStackTrace();
        }
    }
    public void grafik() throws URISyntaxException{
        String url = "http://localhost/Zeplin/";

        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Graph<String, DefaultWeightedEdge> createGraph() {
        Graph<String, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        return g;
    }
    
    
    public void diji(int bas,int bit,int n, double[][] karlar){
        sifirla(yolcuk);
        sifirla(maliyet);
        sifirladizi(ek);
       
        graf2 g[] = new graf2[81];
        g[0] = new graf2(bas);

        Graph<String, DefaultWeightedEdge> iller = createGraph();
        DefaultWeightedEdge e = new DefaultWeightedEdge();

        int tmpKomsular[] = new int[11];
        int sayac = 0;
        int sayac2 = 1;
        ek[0] = bas;
        iller.addVertex("" + g[0].plaka);

        try {
            for (int j = 0; j < 81; j++) {
                for (int i = 1; i < komsuSehir1[g[j].plaka].length; i++) {
                    if (yolvarmi(n, egim(g[j].plaka, komsuSehir1[g[j].plaka][i], bas, bit,n)) == true) {
                        if (ykontrol(g[j].plaka, komsuSehir1[g[j].plaka][i]) == false) {

                            yolcuk[g[j].plaka][komsuSehir1[g[j].plaka][i]] = 1;
                            yolcuk[komsuSehir1[g[j].plaka][i]][g[j].plaka] = 1;
                            tmpKomsular[sayac] = komsuSehir1[g[j].plaka][i];
                                iller.addVertex("" + komsuSehir1[g[j].plaka][i]);
                            e = iller.addEdge("" + komsuSehir1[g[j].plaka][0], "" + komsuSehir1[g[j].plaka][i]);
                            komsular[komsuSehir1[g[j].plaka][0]][sayac]=komsuSehir1[g[j].plaka][i];
                            double m = maliyet[g[j].plaka][komsuSehir1[g[j].plaka][i]];
                            iller.setEdgeWeight(e, m);
                            sayac++;
                            if(n==5)
                            System.out.println(iller.toString() + "  " + iller.getEdgeWeight(iller.getEdge("" + komsuSehir1[g[j].plaka][0], "" + komsuSehir1[g[j].plaka][i])));
                            
                        }
                    }
                }
                g[j].komsuekle(tmpKomsular, sayac);

                if (n==5) {
                    for (int i = 0; i < g[j].komsular.length; i++) {
                        System.out.print(g[j].komsular[i] + "  ");
                    }
                    System.out.println("\n");
                }

                for (int k = 0; k < sayac; k++) {
                    if (eklenmismi(tmpKomsular[k]) == false) {
                        g[sayac2] = new graf2(tmpKomsular[k]);
                        ek[sayac2] = tmpKomsular[k];
                        sayac2++;
                    }
                }
                sayac = 0;
            }
        } catch (Exception x) {
           // x.printStackTrace();
        }

        ekliiller();
        //System.out.println("\n\n");
//        
//        System.out.print("yol:");
//        z3.dij2(bas, bit);
//        
//        for (int i = 1; i < 82; i++) {
//            for (int j = 0; j < 12; j++) {
//                System.out.print(komsular[i][j]+"  ");
//            }
//            System.out.println("");

        
//        }  
        String y="";
        int [] dijiyol=new int [50];
        if (eklenmismi(bit)) {
            double km=DijkstraShortestPath.findPathBetween(iller,""+ bas,""+ bit).getWeight();
            double tmaliyet=(n*20)-(km*10);
            System.out.println("n: "+n+"   "+bas+" ile "+bit+" arasındaki yollar: "+DijkstraShortestPath.findPathBetween(iller,""+ bas,""+ bit).toString()+"  Maliyeti :"+km+" Kar: "+tmaliyet);
            System.out.println("%50  kar için 1 yolcudan alınması gereken miktar:   "+Math.abs(km*10)*(1.5)/n);
            karlar[n][0] = tmaliyet;
            karlar[n][1] = n;
            y="n: "+n+"   "+bas+" ile "+bit+" arasındaki yollar: "+DijkstraShortestPath.findPathBetween(iller,""+ bas,""+ bit).toString()+"  Maliyeti :"+km+" Kar: "+tmaliyet;
            //Kaydet(""+n+"yolcu.txt", "n: "+n+"   "+bas+" ile "+bit+" arasındaki yollar: "+DijkstraShortestPath.findPathBetween(iller,""+ bas,""+ bit).toString()+"  Maliyeti :"+km+" Kar: "+tmaliyet);
            
            {
            String s=DijkstraShortestPath.findPathBetween(iller,""+ bas,""+ bit).toString();
            String k="";
            String parca[]=s.split(",");
            for (int i = 0; i < parca.length; i++) {
                //System.out.println(parca[i]);
                k+=parca[i].substring(parca[i].indexOf("(")+1, parca[i].indexOf(":")-1)+" "+parca[i].substring(parca[i].indexOf(":")+2, parca[i].indexOf(")"))+" ";
                //System.out.println(k);        
            }
            String parca2[]=k.split(" ");
            int x=Integer.parseInt(parca2[0]);
            dijiyol[0]=x;
            k="["+kordi[x][0]+","+kordi[x][1];
            int sayac3=1;
            for (int i = 1; i < parca2.length-1; i+=2) {
                x=Integer.parseInt(parca2[i]);
                dijiyol[sayac3++]=x;
                k+=","+kordi[x][0]+","+kordi[x][1];
            }
            x=Integer.parseInt(parca2[parca2.length-1]);
            dijiyol[sayac3]=x;
            k+=","+kordi[x][0]+","+kordi[x][1]+"]";
            //System.out.println(k);
            Kaydet(""+n+"gorsel.txt", k);
            String s2="";
                for (int i = 0; i < sayac3; i++) {
                    s2+=dijiyol[i]+" ile "+dijiyol[i+1]+" arasındaki mesafe: "+maliyet[dijiyol[i]][dijiyol[i+1]]+" ";
                }
            Kaydet(""+n+"yolcu.txt", y+"/"+s2);
        }
        }else{
             System.out.println("n: "+n+"   "+"yol yok");
        }
        System.out.println("\n\n\n");
        
        
    }
    
    public static double Max(double[][] dizi){ 
    double max = dizi[5][0]; 
    for(int i=0; i < 51 ; i++){ 
      if((dizi[i][0]!=0)  &&  (dizi[i][0] > max)){ 
         max = dizi[i][0]; 
      } 
    } 
    return max; 
    }
    
    public void Sırala(double karlar[][]){
        double tmp [][] = new double [1][2] ;
        
        for (int j = 0; j < karlar.length-1; j++) {
            
       
            for (int i = 0; i < karlar.length-2; i++) {

                if((karlar[i][0] != 0) && (karlar[i][0]>karlar[i+1][0])){
                    tmp[0][0] = karlar[i][0];
                    tmp[0][1] = karlar[i][1];

                    karlar[i][0]= karlar[i+1][0];
                    karlar[i][1]= karlar[i+1][1];

                    karlar[i+1][0] = tmp[0][0];
                    karlar[i+1][1] = tmp[0][1];
                }

            }
        }
    }
    

    public static void main(String[] args) throws URISyntaxException {
       
        
        double s1 = 0; 
        double s2 = 0; 
        double s3=0; 
        
        
       
        

        int bas, bit;
        System.out.println("Kalkış yerinin plakası: ");
        Scanner al = new Scanner(System.in);
        bas = al.nextInt();
        System.out.println("İniş yerinin plakası: ");
        bit = al.nextInt();
        
        long startTime = System.nanoTime(); 
        System.out.println("bas: " + bas + " bit: " + bit);
        
        Zeplin z = new Zeplin(s1,s2); 
       double karlar [][] = new double[51][2];     
        
        long starttimeenboy = System.nanoTime(); 
        for (int n = 5; n < 51; n++) {
             z.diji(bas, bit, n, karlar);
        }
        long endtimeenboy = System.nanoTime(); 
        
        String s = "";
        z.Sırala(karlar);
        for (int i = karlar.length-1; i >4 ; i--) {
            if (karlar[i][0]!=0) {
                 System.out.println(karlar[i][1]+" kadar yolcu için kar :"+karlar[i][0]);
                 s=s+"  ("+karlar[i][1]+" , "+karlar[i][0]+") "+"/";
            }       
        }
       
        z.Kaydet("maliyetler.txt",s);
        
       
       
        
        
        long endTime = System.nanoTime();
        double seconds = (double)(endTime-startTime)/1000000000;
        
        s3 = (double)(endtimeenboy-starttimeenboy)/1000000000;
        
        System.out.println("Graf yapısını oluşturmak ve yolu hesaplamak için geçen süre: "+s3);
        System.out.println("Programın toplam çalışma süresi: "+seconds);
        //z.grafik();
    }
        
        
        
        
}

