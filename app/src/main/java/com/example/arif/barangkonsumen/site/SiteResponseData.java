package com.example.arif.barangkonsumen.site;

class SiteResponseData {
    String id_site;
    private String service_id;
    String nama_site;
    private String alamat_site;
    private String kota_site;
    private String telp_site;

    SiteResponseData(String id_site, String service_id, String nama_site, String alamat_site, String kota_site, String telp_site) {
        this.id_site = id_site;
        this.service_id = service_id;
        this.nama_site = nama_site;
        this.alamat_site = alamat_site;
        this.kota_site = kota_site;
        this.telp_site = telp_site;
    }

    public String getId_site() {
        return id_site;
    }

    public void setId_site(String id_site) {
        this.id_site = id_site;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getNama_site() {
        return nama_site;
    }

    public void setNama_site(String nama_site) {
        this.nama_site = nama_site;
    }

    public String getAlamat_site() {
        return alamat_site;
    }

    public void setAlamat_site(String alamat_site) {
        this.alamat_site = alamat_site;
    }

    public String getKota_site() {
        return kota_site;
    }

    public void setKota_site(String kota_site) {
        this.kota_site = kota_site;
    }

    public String getTelp_site() {
        return telp_site;
    }

    public void setTelp_site(String telp_site) {
        this.telp_site = telp_site;
    }
}
