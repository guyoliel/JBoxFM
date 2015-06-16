package fm.jbox.jboxfm.models;

public class Party {

    private String id;
    private String name;
    private String user_id;
    private String created_at;
    private String updated_at;

    public Party(String id,String name,String user_id,String created_at,String updated_at){
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getUser_id(){
        return this.user_id;
    }

    public String getCreated_at(){
        return this.created_at;
    }

    public String getUpdated_at(){
        return this.updated_at;
    }

    public void setId(String id){
        this.id = id;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at){
        this.id = updated_at;
    }
}
