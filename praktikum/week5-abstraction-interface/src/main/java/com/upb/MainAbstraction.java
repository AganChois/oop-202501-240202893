package main.java.com.upb;


import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.model.kontrak.Receiptable;
import main.java.com.upb.agripos.util.CreditBy;

public class MainAbstraction {
    public static void main(String[] args) {
        Pembayaran cash = new Cash("INV-001", 100000, 120000);
        Pembayaran ew = new EWallet("INV-002", 150000, "user@ewallet", "123456");
        Pembayaran bank = new TransferBank("INV003", 300000, "BCA", false);

        System.out.println(((Receiptable) cash).cetakStruk());
        System.out.println(((Receiptable) ew).cetakStruk());
        System.out.println(((Receiptable) bank).cetakStruk());
    CreditBy.print("240202893", "Agan Chois");
    }

}