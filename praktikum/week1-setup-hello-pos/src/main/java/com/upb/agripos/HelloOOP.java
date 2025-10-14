package main.java.com.upb.agripos;

class Produk {
   String nama;
   int harga;
   Produk(String nama, int harga) {
      this.nama = nama;
      this.harga = harga;
   }
}

public class HelloOOP {
   public static void main(String[] args) {
      String nim = "240202893";
      String namaMhs = "Agan Chois";
      Produk[] daftar = {
         new Produk("Beras", 25000),
         new Produk("Pupuk", 50000),
         new Produk("Benih", 35000)
      };
      int total = 0;
      System.out.println("Hello POS World");
      System.out.println("NIM: " + nim + ", Nama: " + namaMhs);
      System.out.println("Daftar Produk:");
      for (Produk p : daftar) {
         System.out.println("- " + p.nama + ": " + p.harga);
         total += p.harga;
      }
      System.out.println("Total harga semua produk: " + total);
   }
}