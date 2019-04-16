package com.example.arif.barangkonsumen.customer;

class CustomerResponseData {
    String customer_id, customer_name, alamat;

    CustomerResponseData(String customer_id, String customer_name, String alamat) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.alamat = alamat;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
