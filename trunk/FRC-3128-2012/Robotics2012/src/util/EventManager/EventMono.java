package util.EventManager;

public interface EventMono {

    public void iterate();

    public boolean shouldIterate();
    
    public boolean shouldRemove();
}
