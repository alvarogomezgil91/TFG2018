package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 25/07/2018.
 */

public class Feed {

    private int id;
    private String title;
    private String hyperlink;
    private String imageLink;

    //Constructor

    public Feed(int id, String title, String hyperlink, String imageLink) {
        this.id = id;
        this.title = title;
        this.hyperlink = hyperlink;
        this.imageLink = imageLink;
    }

    //Setter, getter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }


}
