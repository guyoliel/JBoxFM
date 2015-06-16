package fm.jbox.jboxfm.models;

public class Like {
    String id;
    User user;

    public Like(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString(){
        return "{\"id\":"+ this.getId()+ ",\"user\":"+this.user.toString()+"}";
    }
}
