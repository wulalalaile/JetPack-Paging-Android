package com.example.administrator.jetpacktodo.model;

import java.io.Serializable;

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

    private SourceBean source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

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

    public static class SourceBean {
        /**
         * id : null
         * name : Lifehacker.com
         */

        private Object id;
        private String name;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
