package marvin.le;

public class RenderComponent {
    public static final int LETTER = 0;
    public static final int CURVE = 1;
    public static final int POINT = 2;

    public int id;
    public String content;

    public RenderComponent(int id, String content) {
        this.id = id;
        this.content = content;
    }
    
    public boolean equals(RenderComponent renderComponent) {
        return id == renderComponent.id;
    }
    
}
