import java.util.ArrayList;
public class Player
{
    public Bird bird;
    public Pipe pipeList[];
    public ArrayList<Pipe> temporaryPipe;
    public Hitbox hitbox;
    public Scoreboard score;
    public boolean firstStage = true;
    public int id;
    public int flappyV = 0;
    public int flappyA = 7;
    public int flappyI = 1;
    
    public Player(int id){
        this.id = id;
        bird = new Bird(); 
        pipeList = new Pipe[10];
        score = new Scoreboard(); 
        temporaryPipe = new ArrayList<Pipe>();
    } 

    public void addTempPipe(Pipe p){
        // append pipe to the end of ArrayList
        temporaryPipe.add(p);
    }
    private int hitboxposition=0;
    public void buildHitbox(){
        if (hitbox == null){
            hitbox = new Hitbox(pipeList[hitboxposition++]);
            if (hitboxposition == pipeList.length){ // if reached to the last pipe
                hitboxposition = 0;
            }
        }
    }
    public void updateHitbox(){
        if (hitbox != null){
            hitbox.update();
        }
    }
    public int getBirdPosition(int flappyheight){
        return flappyheight+flappyV;
    }
    public void takeTmp(){
        if ( !temporaryPipe.isEmpty() ){
            for (int i=0; i<pipeList.length; ++i){
                if ( pipeList[i] == null ){
                    pipeList[i] = temporaryPipe.remove(0); 
                    if (temporaryPipe.isEmpty()) return;
                }
            }
        }
    }
    // for Pipebulider create pipe for itself and another
    // make all player have same pipe
    private Pipe newPipe;
    public void buildPipe(Player a[]){
        if (firstStage){
            for (int i=0; i<pipeList.length; ++i){
                if (pipeList[i] == null){
                    newPipe = new Pipe();
                    pipeList[i] = newPipe;
                    for (int j=0; j<a.length; ++j){
                        if ( a[j].id == this.id ){
                            continue;
                        }
                        a[j].pipeList[i] = newPipe;
                        a[j].firstStage = false;
                    }
                    // create space between each pipe
                    pipeList[i].x += ((pipeList[i].getWidth()*2)+200)*i;
                }
            }
            firstStage = false; // end the initialize stage 
        }else{
            for (int i=0; i<pipeList.length; ++i){
                if (pipeList[i] == null){
                    newPipe = new Pipe();
                    pipeList[i] = newPipe;
                    for (int j=0; j<a.length; ++j){
                        if ( a[j].id == this.id ){
                            continue;
                        }
                        if ( a[j].pipeList[i] != null ){
                            a[j].addTempPipe(new Pipe(newPipe));
                        }else{
                            a[j].pipeList[i] = newPipe;
                        }
                    }
                    // create space between each pipe
                    if ( i == 0 ){
                        pipeList[i].x = (pipeList[pipeList.length-1].x+pipeList[i].getWidth())+((pipeList[i].getWidth()*2)); 
                    }else{
                        pipeList[i].x = (pipeList[i-1].x+pipeList[i].getWidth())+((pipeList[i].getWidth()*2)); 
                    }
                }
            }
        }
    }
}
