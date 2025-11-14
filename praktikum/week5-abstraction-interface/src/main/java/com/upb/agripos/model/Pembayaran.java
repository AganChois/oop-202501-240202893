package main.java.com.upb.agripos.model;

public abstract class Pembayaran {
    protected String invoiceNo;
    protected double total;

    public Pembayaran(String invoiceNo, double total) {
        this.invoiceNo = invoiceNo;
        this.total = total;
    }

    public abstract double biaya();               // fee/biaya tambahan
    public abstract boolean prosesPembayaran();   // proses spesifik tiap metode

    public double totalBayar() {
        return total + biaya();
    }

    public String getInvoiceNo() { return invoiceNo; }
    public double getTotal() { return total; }

    public String cetakStruk() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cetakStruk'");
    }

    public boolean validasi() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validasi'");
    }
}