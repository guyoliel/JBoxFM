package fm.jbox.jboxfm.models;

public class Request {

    private String id;
    private String author;
    private String party_id;
    private String thumbnail;
    private String url;
    private String created_at;
    private String title;
    private User user;
    private Like[] likes;

    public Request(String id,String author,String party_id,String thumbnail,String url,
                   String created_at,String title,User user,Like[] likes){

        this.id = id;
        this.author = author;
        this.party_id = party_id;
        this.thumbnail = thumbnail;
        this.url = url;
        this.created_at = created_at;
        this.title = title;
        this.user = user;
        this.likes = likes;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Like[] getLikes() {
        return likes;
    }

    public String getLikesString(){
        String str = "[";
        for (int i = 0; i < this.likes.length; i++) {
            str += this.likes[i].toString()+ ",";
        }
        str+= "]";
        return str.replace(",]","]");
    }
    public void setLikes(Like[] likes) {
        this.likes = likes;
    }
}
