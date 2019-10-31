package entities;

import java.io.Serializable;

public class Peminjaman implements Serializable {
    private int idPeminjaman;
    private String name;
    private String telphon;
    private int amount;
    private String description;
    private String dateOfLoan;
    private String dateDue;

    public Peminjaman(){

    }

    public Peminjaman(int idPeminjaman, String name, String telphon, int amount, String description, String dateOfLoan, String dateDue) {
        this.idPeminjaman = idPeminjaman;
        this.name = name;
        this.telphon = telphon;
        this.amount = amount;
        this.description = description;
        this.dateOfLoan = dateOfLoan;
        this.dateDue = dateDue;
    }

    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphon() {
        return telphon;
    }

    public void setTelphon(String telphon) {
        this.telphon = telphon;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateOfLoan() {
        return dateOfLoan;
    }

    public void setDateOfLoan(String dateOfLoan) {
        this.dateOfLoan = dateOfLoan;
    }

    public String getDateDue() {
        return dateDue;
    }

    public void setDateDue(String dateDue) {
        this.dateDue = dateDue;
    }
}
