package com.diplabs.securecodeverifier;

public class Code {
    public Code(String client_id, String data, String token) {
        this.client_id = client_id;
        this.data = data;
        this.token = token;
    }

    private String client_id;
    private String data;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

}
