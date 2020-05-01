package com.example.productive;

public class Image {
    private String name;
    private byte[] image;

    public Image(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    public String getName() { return name; }
    public byte[] getImage() { return image; }

}
