package com.example.arif.barangkonsumen;

import java.util.HashMap;

public class ApiResponse {
    private Object response;
    private HashMap<String, String> metadata;

    public ApiResponse(Object response, HashMap<String, String> metadata) {
        this.response = response;
        this.metadata = metadata;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public HashMap<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap<String, String> metadata) {
        this.metadata = metadata;
    }
}
