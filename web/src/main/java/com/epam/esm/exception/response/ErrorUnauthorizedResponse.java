package com.epam.esm.exception.response;

import java.util.Objects;

public class ErrorUnauthorizedResponse {

    private String message;
    private String localAuthentication;
    private String serverAuthentication;
    private String registration;
    private int code;

    public ErrorUnauthorizedResponse() {
    }

    public ErrorUnauthorizedResponse(String message, String localAuthentication, String serverAuthentication, String registration, int code) {
        this.message = message;
        this.localAuthentication = localAuthentication;
        this.serverAuthentication = serverAuthentication;
        this.registration = registration;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalAuthentication() {
        return localAuthentication;
    }

    public void setLocalAuthentication(String localAuthentication) {
        this.localAuthentication = localAuthentication;
    }

    public String getServerAuthentication() {
        return serverAuthentication;
    }

    public void setServerAuthentication(String serverAuthentication) {
        this.serverAuthentication = serverAuthentication;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorUnauthorizedResponse that = (ErrorUnauthorizedResponse) o;
        return code == that.code && Objects.equals(message, that.message) && Objects.equals(localAuthentication, that.localAuthentication) && Objects.equals(serverAuthentication, that.serverAuthentication) && Objects.equals(registration, that.registration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, localAuthentication, serverAuthentication, registration, code);
    }

    @Override
    public String toString() {
        return "ErrorUnauthorizedResponse{" +
                "message='" + message + '\'' +
                ", localAuthentication='" + localAuthentication + '\'' +
                ", serverAuthentication='" + serverAuthentication + '\'' +
                ", registration='" + registration + '\'' +
                ", code=" + code +
                '}';
    }
}
