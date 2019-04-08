package com.example.arif.barangkonsumen.detail_barang;

public class DetailBarangResponseData {
    String id_barang, id_lokasi, nama_barang, total_serial;

    DetailBarangResponseData(String id_barang, String id_lokasi, String nama_barang, String total_serial) {
        this.id_barang = id_barang;
        this.id_lokasi = id_lokasi;
        this.nama_barang = nama_barang;
        this.total_serial = total_serial;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getId_lokasi() {
        return id_lokasi;
    }

    public void setId_lokasi(String id_lokasi) {
        this.id_lokasi = id_lokasi;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getTotal_serial() {
        return total_serial;
    }

    public void setTotal_serial(String total_serial) {
        this.total_serial = total_serial;
    }
}
