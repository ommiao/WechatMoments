package cn.ommiao.wechatmoments.entity;

import java.util.ArrayList;

/**
 * @author LiKai
 */
public class Tweet extends AbstractContent {

    private ArrayList<Image> images = new ArrayList<>();

    private ArrayList<Comment> comments = new ArrayList<>();

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public boolean isInvalid(){
        return getContent() != null || getImages().size() > 0;
    }

    public static class Image extends JavaBean {

        private String url;

        public String getUrl() {
            return url;
        }

    }


    public static class Comment extends AbstractContent {

    }

}
