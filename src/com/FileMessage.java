package com;

import java.io.File;
import java.io.Serializable;

public class FileMessage implements Serializable{
    private String client;
    private File file;

    public FileMessage(String client, File file) {
        this.client = client;
        this.file = file;
    }

    public FileMessage(String cliente) {
        this.client = client;
    }

    public FileMessage() {
    }

    public String getCliente() {
        return client;
    }

    public void setCliente(String cliente) {
        this.client = client;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
