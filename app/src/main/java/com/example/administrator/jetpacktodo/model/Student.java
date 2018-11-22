package com.example.administrator.jetpacktodo.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "student_table")
public class Student implements Serializable {

    /**
     * source : {"id":null,"name":"Lifehacker.com"}
     * author : Nick Douglas
     * title : The Scaredy-Cat Guide to Watching Scary Movies
     * description : You can hate getting scared and still want to watch scary movies. I’m a little crying baby, but I didn’t let that stop me from enjoying The Babadook, or enjoying and then hating 10 Cloverfield Lane. My strategy for both was simple: watch on the couch with a f…
     * url : https://lifehacker.com/the-scaredy-cat-guide-to-watching-scary-movies-1829876195
     * urlToImage : https://i.kinja-img.com/gawker-media/image/upload/s--I59asEoU--/c_fill,fl_progressive,g_center,h_900,q_80,w_1600/vzf2na8b1qef0khyaczb.png
     * publishedAt : 2018-10-22T17:00:00Z
     * content : You can hate getting scared and still want to watch scary movies. Im a little crying baby, but I didnt let that stop me from enjoying The Babadook, or enjoying and then hating 10 Cloverfield Lane. My strategy for both was simple: watch on the couch with a fri… [+2371 chars]
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public int getIndexInReponse() {
        return indexInReponse;
    }

    public void setIndexInReponse(int indexInReponse) {
        this.indexInReponse = indexInReponse;
    }

    private int indexInReponse = -1;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                '}';
    }
}
