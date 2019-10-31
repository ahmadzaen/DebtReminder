package entities;

import java.io.Serializable;

public class Pembayaran implements Serializable {
    private int idPembayaran;
    private int idPeminjaman;
    private String datePay;
    private int pay;
    private String description;

    public Pembayaran(int idPembayaran, int idPeminjaman, String datePay, int pay, String description) {
        this.idPembayaran = idPembayaran;
        this.idPeminjaman = idPeminjaman;
        this.datePay = datePay;
        this.pay = pay;
        this.description = description;
    }

    public Pembayaran() {
    }

    public int getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(int idPembayaran) {
        this.idPembayaran = idPembayaran;
    }

    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public String getDatePay() {
        return datePay;
    }

    public void setDatePay(String datePay) {
        this.datePay = datePay;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

