package cn.ommiao.wechatmoments.entity;

public abstract class AbstractContent extends JavaBean{

    private String content;

    private User sender;

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
