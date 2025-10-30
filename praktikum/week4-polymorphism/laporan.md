# Laporan Praktikum Minggu 4
Topik: Polymorphism (Info Produk)

## Identitas
- Nama  : Agan Chois
- NIM   : 240202893
- Kelas : 3IKRB

---

## Tujuan
- Mahasiswa mampu **menjelaskan konsep polymorphism** dalam OOP.  
- Mahasiswa mampu **membedakan method overloading dan overriding**.  
- Mahasiswa mampu **mengimplementasikan polymorphism (overriding, overloading, dynamic binding)** dalam program.  
- Mahasiswa mampu **menganalisis contoh kasus polymorphism** pada sistem nyata (Agri-POS).  


---

## Dasar Teori
Polymorphism berarti “banyak bentuk” dan memungkinkan objek yang berbeda merespons panggilan method yang sama dengan cara yang berbeda.  
1. **Overloading** → mendefinisikan method dengan nama sama tetapi parameter berbeda.  
2. **Overriding** → subclass mengganti implementasi method dari superclass.  
3. **Dynamic Binding** → pemanggilan method ditentukan saat runtime, bukan compile time.  

Dalam konteks Agri-POS, misalnya:  
- Method `getInfo()` pada `Produk` dioverride oleh `Benih`, `Pupuk`, `AlatPertanian` untuk menampilkan detail spesifik.  
- Method `tambahStok()` bisa dibuat overload dengan parameter berbeda (int, double). 

---

## Langkah Praktikum
1. **Overloading**  
   - Tambahkan method `tambahStok(int jumlah)` dan `tambahStok(double jumlah)` pada class `Produk`.  

2. **Overriding**  
   - Tambahkan method `getInfo()` pada superclass `Produk`.  
   - Override method `getInfo()` pada subclass `Benih`, `Pupuk`, dan `AlatPertanian`.  

3. **Dynamic Binding**  
   - Buat array `Produk[] daftarProduk` yang berisi objek `Benih`, `Pupuk`, dan `AlatPertanian`.  
   - Loop array tersebut dan panggil `getInfo()`. Perhatikan bagaimana Java memanggil method sesuai jenis objek aktual.  

4. **Main Class**  
   - Buat `MainPolymorphism.java` untuk mendemonstrasikan overloading, overriding, dan dynamic binding.  

5. **CreditBy**  
   - Tetap panggil `CreditBy.print("<NIM>", "<Nama>")`.  

6. **Commit dan Push**  
   - Commit dengan pesan: `week4-polymorphism`.  
---

## Kode Program
### Produk.java (Overloading & getInfo default)
```java
package main.java.com.upb.agripos.model;

public class Produk {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public void tambahStok(double jumlah) {
        this.stok += (int) jumlah;
    }

    public String getInfo() {
        return "Produk: " + nama + " (Kode: " + kode + ")";
    }
}
```

### Benih.java (Overriding)
```java
package main.java.com.upb.agripos.model;

public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    @Override
    public String getInfo() {
        return "Benih: " + super.getInfo() + ", Varietas: " + varietas;
    }
}
```

### MainPolymorphism.java
```java
package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.util.CreditBy;

public class MainPolymorphism {
    public static void main(String[] args) {
        Produk[] daftarProduk = {
            new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64"),
            new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea"),
            new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja")
        };

        for (Produk p : daftarProduk) {
            System.out.println(p.getInfo()); // Dynamic Binding
        }

        CreditBy.print("240202893", "Agan Chois");
    }
}
```
---

## Hasil Eksekusi
  ![Screenshot hasil](w4-pholymorpisem.png)

---

## Analisis
Kode di atas berjalan dengan konsep polimorfisme, di mana objek-objek dari subclass seperti Benih, Pupuk, dan AlatPertanian disimpan dalam array bertipe Produk, lalu masing-masing memanggil metode getInfo() yang dioverride sesuai kelasnya menggunakan dynamic binding saat runtime. Pada saat program dijalankan, setiap elemen array memanggil versi getInfo() miliknya sendiri sehingga hasil output berbeda sesuai tipe objeknya. Pendekatan minggu ini berbeda dari minggu sebelumnya karena kini fokus pada polimorfisme dan overriding, sedangkan minggu sebelumnya hanya menggunakan inheritance dan overloading tanpa pemanggilan dinamis. Kendala yang sering muncul adalah error pada struktur package dan pemanggilan class lintas folder. Hal ini dapat diatasi dengan memastikan path package sesuai dengan struktur direktori dan melakukan import dengan benar agar setiap class bisa dikenali oleh program utama.
---

## Kesimpulan
Kesimpulannya, program ini menunjukkan penerapan polimorfisme di mana metode yang sama (getInfo()) dapat memberikan hasil berbeda tergantung objek yang memanggilnya. Konsep ini membuat kode lebih fleksibel, efisien, dan mudah dikembangkan karena cukup menggunakan satu tipe referensi (Produk) untuk berbagai subclass. Dibanding minggu sebelumnya yang hanya menekankan inheritance dan overloading, minggu ini menambah pemahaman tentang overriding dan dynamic binding. Kendala utama biasanya terkait struktur package dan import antar folder. Dengan pengaturan package yang benar, program dapat berjalan lancar dan menunjukkan manfaat nyata dari konsep OOP tingkat lanjut.

---

## Quiz
1. Apa perbedaan overloading dan overriding?  
   **Jawaban:** Overloading terjadi ketika dua atau lebih method memiliki nama yang sama tetapi parameter berbeda dalam satu class, biasanya digunakan untuk memberikan fleksibilitas pemanggilan fungsi. Sedangkan overriding terjadi ketika subclass membuat ulang method yang sudah ada di superclass dengan isi (implementasi) yang berbeda, untuk menyesuaikan perilaku sesuai kebutuhan subclass. Overloading ditentukan saat compile time, sedangkan overriding ditentukan saat runtime.  

2. Bagaimana Java menentukan method mana yang dipanggil dalam dynamic binding?  
   **Jawaban:** Java menentukan method mana yang dipanggil dalam dynamic binding berdasarkan tipe objek aktual yang dibuat di memori, bukan tipe referensinya. Artinya, meskipun variabel bertipe Produk, jika objek sebenarnya adalah Benih, maka method getInfo() milik Benih yang akan dijalankan. Proses ini dilakukan secara otomatis oleh JVM pada saat program dijalankan.  

3. Berikan contoh kasus polymorphism dalam sistem POS selain produk pertanian.  
   **Jawaban:** Dalam sistem POS toko elektronik, terdapat class Barang sebagai superclass dengan subclass seperti Laptop, Smartphone, dan Televisi yang masing-masing meng-override method getInfo(). Saat semua objek disimpan dalam array bertipe Barang, setiap objek akan menampilkan informasi sesuai jenisnya saat getInfo() dipanggil. Ini menunjukkan penerapan polimorfisme yang memudahkan pengelolaan berbagai jenis produk dengan satu tipe referensi umum.  

