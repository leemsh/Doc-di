package com.dd.server.dto;

import lombok.Data;


@Data
public class SuccessResponse<T> {
    private int status;
    private String message;
    private T data;

    public SuccessResponse(T data, int status) {
        this.status = status;
        this.data = data;

        if(status == 200) {
            this.message = "Success";
        }
        if(status == 201) {
            this.message = "Created";
        }
        if(status == 202) {
            this.message = "Accepted";
        }
        if(status == 203) {
            this.message = "Non-Authoritative Information";
        }
        if(status == 204) {
            this.message = "No Content";
        }
        if(status == 205) {
            this.message = "Reset Content";
        }
        if(status == 206) {
            this.message = "Partial Content";
        }
        if(status == 207) {
            this.message = "Multi-Status";
        }
        if(status == 208) {
            this.message = "Already Reported";
        }
        if(status == 210) {
            this.message = "Moved Permanently";
        }
        if(status == 211) {
            this.message = "Moved Temporarily";
        }
        if(status == 212) {
            this.message = "See Other";
        }
        if(status == 213) {
            this.message = "Not Modified";
        }
        if(status == 214) {
            this.message = "Use Proxy";
        }
        if(status == 215) {
            this.message = "Temporary Redirect";
        }
        if(status == 216) {
            this.message = "Permanent Redirect";
        }
        if(status == 400){
            this.message = "Bad Request";
        }
        if(status == 401) {
            this.message = "Unauthorized";
        }
        if(status == 402) {
            this.message = "Payment Required";
        }
        if(status == 403) {
            this.message = "Forbidden";
        }
        if(status == 404) {
            this.message = "Not Found";
        }
        if(status == 405) {
            this.message = "Method Not Allowed";
        }
        if(status == 406) {
            this.message = "Not Acceptable";
        }
        if(status == 407) {
            this.message = "Proxy Authentication Required";
        }
        if(status == 408) {
            this.message = "Request Timeout";
        }
        if(status == 409) {
            this.message = "Conflict";
        }
        if(status == 410) {
            this.message = "Gone";
        }
        if(status == 500) {
            this.message = "Internal Server Error";
        }
        if(status == 501) {
            this.message = "Not Implemented";
        }
        if(status == 502) {
            this.message = "Bad Gateway";
        }
        if(status == 503) {
            this.message = "Service Unavailable";
        }
        if(status == 504) {
            this.message = "Gateway Timeout";
        }
    }
}
