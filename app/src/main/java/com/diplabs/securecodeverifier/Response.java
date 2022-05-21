package com.diplabs.securecodeverifier;

public class Response {
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Response(boolean valid) {
        this.valid = valid;
    }

    boolean valid;



}
