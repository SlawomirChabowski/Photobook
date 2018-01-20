package beans;

public class CategoryObj {
    private int id;
    private String name;
    
    public int getId() { return id; }
    public String getName() { return name; }
    
    public CategoryObj(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
