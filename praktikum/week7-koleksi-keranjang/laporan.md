# Laporan Praktikum Minggu 7
Topik: Collections dan Implementasi Keranjang Belanja

## Identitas
- Nama  : Agan Chois
- NIM   : 240202893
- Kelas : 3IKRB

---

## Tujuan
 Mahasiswa mampu:
1. Menjelaskan konsep collection dalam Java (List, Map, Set).
2. Menggunakan ArrayList untuk menyimpan dan mengelola objek.
3. Mengimplementasikan Map atau Set sesuai kebutuhan pengelolaan data.
4. Melakukan operasi dasar pada collection: tambah, hapus, dan hitung total.
5. Menganalisis efisiensi penggunaan collection dalam konteks sistem Agri-POS.

## Dasar Teori

### 1. Collections Framework
Java Collections Framework menyediakan struktur data untuk mengelola objek secara dinamis dan efisien.
Struktur utama:
- List (implementasi: ArrayList) — Terurut, dapat menyimpan elemen duplikat.
- Map (implementasi: HashMap) — Menyimpan pasangan key–value, akses cepat berdasarkan key.
- Set (implementasi: HashSet) — Tidak menerima duplikat dan tidak mempertahankan urutan.
---
### 2. Studi Kasus: Keranjang Belanja Agri-POS
Keranjang belanja harus dapat:
- Menambahkan produk
- Menghapus produk
- Menampilkan isi keranjang
- Menghitung total nilai transaksi
- Menangani jumlah (quantity) menggunakan Map
Kasus ini mencerminkan penggunaan struktur data dalam aplikasi nyata seperti POS.
---

## Langkah Praktikum
1. Membuat Class Product
2. Implementasi Keranjang dengan ArrayList
3. Mengisi  Main Program 
4. Implementasi Alternatif Menggunakan Map (Dengan Quantity)

---

## Kode Program
## 1. Product.java
package com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
## 2. ShoppingCart.java
package com.upb.agripos;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p) { items.add(p); }
    public void removeProduct(Product p) { items.remove(p); }

    public double getTotal() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void printCart() {
        System.out.println("Isi Keranjang:");
        for (Product p : items) {
            System.out.println("- " + p.getCode() + " " + p.getName() + " = " + p.getPrice());
        }
        System.out.println("Total: " + getTotal());
    }
}
## 3. ShoppingCartMap.java
package com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartMap {
    private final Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p) { items.put(p, items.getOrDefault(p, 0) + 1); }

    public void removeProduct(Product p) {
        if (!items.containsKey(p)) return;
        int qty = items.get(p);
        if (qty > 1) items.put(p, qty - 1);
        else items.remove(p);
    }

    public double getTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void printCart() {
        System.out.println("Isi Keranjang (Map):");
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            System.out.println("- " + e.getKey().getCode() + " " + e.getKey().getName() + " x" + e.getValue());
        }
        System.out.println("Total: " + getTotal());
    }
}
## 4. MainCart.java
package com.upb.agripos;

public class MainCartMap {
    public static void main(String[] args) {
        System.out.println("Hello, I am Agan Chois-240202893 (Week7)");

        Product p1 = new Product("P01", "Beras", 50000);
        Product p2 = new Product("P02", "Pupuk", 30000);

        ShoppingCartMap cart = new ShoppingCartMap ();
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.printCart();

        cart.removeProduct(p1);
        cart.printCart();
    }
## 5. MainCartMap.java
package com.upb.agripos;

public class MainCartMap {
    public static void main(String[] args) {
        System.out.println("Hello, I am Agan Chois-240202893 (Week7)");

        Product p1 = new Product("P01", "Beras", 50000);
        Product p2 = new Product("P02", "Pupuk", 30000);

        ShoppingCartMap cart = new ShoppingCartMap ();
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.printCart();

        cart.removeProduct(p1);
        cart.printCart();
    }
---

## Hasil Eksekusi 
![Screenshot hasil](screenshots/Keranjang.png)

---

## Analisis
Kode program berjalan dengan diawali pembuatan objek Product yang menyimpan data dasar produk (kode, nama, dan harga), kemudian objek tersebut dimasukkan ke dalam keranjang belanja melalui kelas ShoppingCart atau ShoppingCartMap; pada pendekatan ArrayList, setiap produk disimpan sebagai elemen terpisah sehingga total harga dihitung dengan menjumlahkan harga seluruh objek, sedangkan pada pendekatan Map setiap produk menjadi key dengan value berupa jumlah (quantity) sehingga lebih efisien untuk menangani produk yang sama lebih dari satu. Perbedaan utama pendekatan minggu ini dibanding minggu sebelumnya adalah penggunaan Collection Framework yang lebih kompleks, khususnya Map untuk mengelola kuantitas barang, sementara sebelumnya umumnya hanya menggunakan struktur data sederhana atau satu objek per item tanpa perhitungan jumlah. Kendala yang dihadapi terutama pada pengelolaan data produk yang sama (duplikasi objek) dan logika pengurangan item di keranjang, yang diatasi dengan memanfaatkan metode getOrDefault() pada Map serta pengecekan jumlah sebelum menghapus produk dari keranjang.
---

## Kesimpulan
Penerapan konsep OOP dan Collection Framework pada program Agri-POS ini mempermudah pengelolaan data produk dan keranjang belanja secara terstruktur dan efisien, baik menggunakan ArrayList maupun Map. Pendekatan Map terbukti lebih efektif untuk menangani produk dengan jumlah lebih dari satu karena mendukung pencatatan kuantitas secara langsung. Melalui praktikum ini, pemahaman tentang perbedaan penggunaan struktur data serta penerapannya dalam studi kasus nyata menjadi lebih jelas. Selain itu, mahasiswa juga dilatih untuk mengatasi kendala logika program dengan memanfaatkan fitur bawaan Java secara tepat.

---

## Quiz
1. Jelaskan perbedaan mendasar antara List, Map, dan Set. 
   **Jawaban:** Perbedaan mendasar antara List, Map, dan Set adalah pada cara penyimpanan dan pengelolaan datanya, di mana List menyimpan elemen secara berurutan dan memperbolehkan data yang sama (duplikasi), Set menyimpan elemen unik tanpa urutan tertentu dan secara otomatis menolak duplikasi, sedangkan Map menyimpan pasangan key–value di mana setiap key harus unik dan digunakan untuk mengakses value tertentu. 

2. Mengapa ArrayList cocok digunakan untuk keranjang belanja sederhana?
   **Jawaban:** ArrayList cocok digunakan untuk keranjang belanja sederhana karena mudah diimplementasikan, mendukung penyimpanan data secara dinamis, dan memungkinkan penambahan produk dengan cepat tanpa perlu logika tambahan untuk mengelola jumlah barang.  

3. Bagaimana struktur Set mencegah duplikasi data?  
   **Jawaban:** Struktur Set mencegah duplikasi data dengan memeriksa kesamaan elemen menggunakan metode equals() dan hashCode(), sehingga jika ada data yang sama ditambahkan, Set akan mengabaikannya dan hanya menyimpan satu instance saja. 

4. Kapan sebaiknya menggunakan Map dibandingkan List? Jelaskan dengan contoh. 
   **Jawaban:** Map sebaiknya digunakan dibandingkan List ketika data memiliki hubungan pasangan key–value atau membutuhkan identifikasi unik, misalnya pada keranjang belanja yang menyimpan produk sebagai key dan jumlah (quantity) sebagai value, sehingga lebih efisien dibandingkan List yang harus menyimpan objek produk berulang kali untuk merepresentasikan jumlah yang sama.  
