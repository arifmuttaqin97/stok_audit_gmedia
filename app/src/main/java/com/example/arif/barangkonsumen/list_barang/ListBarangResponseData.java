package com.example.arif.barangkonsumen.list_barang;

class ListBarangResponseData {
    String id_header,
            nama_barang,
            merk,
            type,
            kode,
            id_ukuran,
            qty,
            qty_small,
            min_stock,
            max_stock,
            harga_jual,
            id_aktif,
            gambar,
            keterangan,
            user_input,
            tanggal_input,
            user_update,
            tanggal_update,
            flag,
            id_kategori,
            id_tipe,
            id_satuan_kecil;

    ListBarangResponseData(String id_header, String nama_barang, String merk,
                           String type, String kode, String id_ukuran,
                           String qty, String qty_small, String min_stock,
                           String max_stock, String harga_jual, String id_aktif,
                           String gambar, String keterangan, String user_input,
                           String tanggal_input, String user_update, String tanggal_update,
                           String flag, String id_kategori, String id_tipe, String id_satuan_kecil) {
        this.id_header = id_header;
        this.nama_barang = nama_barang;
        this.merk = merk;
        this.type = type;
        this.kode = kode;
        this.id_ukuran = id_ukuran;
        this.qty = qty;
        this.qty_small = qty_small;
        this.min_stock = min_stock;
        this.max_stock = max_stock;
        this.harga_jual = harga_jual;
        this.id_aktif = id_aktif;
        this.gambar = gambar;
        this.keterangan = keterangan;
        this.user_input = user_input;
        this.tanggal_input = tanggal_input;
        this.user_update = user_update;
        this.tanggal_update = tanggal_update;
        this.flag = flag;
        this.id_kategori = id_kategori;
        this.id_tipe = id_tipe;
        this.id_satuan_kecil = id_satuan_kecil;
    }

    public String getId_header() {
        return id_header;
    }

    public void setId_header(String id_header) {
        this.id_header = id_header;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getId_ukuran() {
        return id_ukuran;
    }

    public void setId_ukuran(String id_ukuran) {
        this.id_ukuran = id_ukuran;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQty_small() {
        return qty_small;
    }

    public void setQty_small(String qty_small) {
        this.qty_small = qty_small;
    }

    public String getMin_stock() {
        return min_stock;
    }

    public void setMin_stock(String min_stock) {
        this.min_stock = min_stock;
    }

    public String getMax_stock() {
        return max_stock;
    }

    public void setMax_stock(String max_stock) {
        this.max_stock = max_stock;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getId_aktif() {
        return id_aktif;
    }

    public void setId_aktif(String id_aktif) {
        this.id_aktif = id_aktif;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getUser_input() {
        return user_input;
    }

    public void setUser_input(String user_input) {
        this.user_input = user_input;
    }

    public String getTanggal_input() {
        return tanggal_input;
    }

    public void setTanggal_input(String tanggal_input) {
        this.tanggal_input = tanggal_input;
    }

    public String getUser_update() {
        return user_update;
    }

    public void setUser_update(String user_update) {
        this.user_update = user_update;
    }

    public String getTanggal_update() {
        return tanggal_update;
    }

    public void setTanggal_update(String tanggal_update) {
        this.tanggal_update = tanggal_update;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getId_tipe() {
        return id_tipe;
    }

    public void setId_tipe(String id_tipe) {
        this.id_tipe = id_tipe;
    }

    public String getId_satuan_kecil() {
        return id_satuan_kecil;
    }

    public void setId_satuan_kecil(String id_satuan_kecil) {
        this.id_satuan_kecil = id_satuan_kecil;
    }
}
