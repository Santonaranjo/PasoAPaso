public class User {
    private int id;
    private String username;
    private String createdAt;
    
    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }
    
    // Getters y setters
    public int getId() { 
        return id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public String getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(String createdAt) { 
        this.createdAt = createdAt; 
    }
}