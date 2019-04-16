package id.net.gmedia.stokaudit.detail_serial_barang;

class DetailSerialResponseData {
    String id_serial;
    String serial;
    private String id_barang;
    private String id_lokasi;
    private String nama_barang;

    DetailSerialResponseData(String id_serial, String id_barang, String id_lokasi, String nama_barang, String serial) {
        this.id_serial = id_serial;
        this.id_barang = id_barang;
        this.id_lokasi = id_lokasi;
        this.nama_barang = nama_barang;
        this.serial = serial;
    }

    public String getId_serial() {
        return id_serial;
    }

    public void setId_serial(String id_serial) {
        this.id_serial = id_serial;
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
