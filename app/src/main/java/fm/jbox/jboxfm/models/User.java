package fm.jbox.jboxfm.models;

public class User {

    private String id;
    private String name;
    private String email;
    private String thumbnail;

    public User(String id,String name,String email,String thumbnail){
        this.id = id;
        this.name = name;
        this.email = email;
        this.thumbnail = thumbnail;
    }

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getThumbnail(){
        return this.thumbnail;
    }
    public void setThumbnail(String thumbnail){
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "{\"id\":"+ this.getId()+ ",\"name\":\"" +this.getName()+"\",\"email\":\""+this.getEmail()+"\",\"thumbnail\":\""+ this.getThumbnail()+"\"}";
    }
}
