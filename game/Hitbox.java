public class Hitbox 
{
    Pipe pipe;
    public int scoreLine;
    public int front;
    public int behind;
    public int topLevel;
    public int botLevel;
    public Hitbox(Pipe p){
        this.pipe = p;
        scoreLine = pipe.x+pipe.getWidth()+2;

        front = pipe.x;
        behind = pipe.x+pipe.getWidth();
        topLevel = pipe.yTop+pipe.getHeight();
        botLevel = pipe.yBot;
    }
    public void update(){
        front = pipe.x;
        scoreLine = pipe.x+pipe.getWidth()+2;
        behind = pipe.x+pipe.getWidth();
    }
    
}
