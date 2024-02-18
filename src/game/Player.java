import java.util.ArrayList;
public class Player
{
    public Bird bird;
    public Pipe pipeList[];
    public ArrayList<Pipe> temporaryPipe;
    public Hitbox hitbox;
    public Scoreboard score;
    public Heartbar heartBar;
    public boolean firstStage = true;
    public int id;
    public int flappyV = 0;
    public int flappyA = 7;
    public int flappyI = 1;
    public int jump;
    
    public Player(int id){
        this.id = id;
        this.bird = new Bird(2); 
        this.pipeList = new Pipe[10];
        this.score = new Scoreboard(); 
        this.heartBar = new Heartbar();
        this.temporaryPipe = new ArrayList<Pipe>();
    } 

    public void addTempPipe(Pipe p){
        // append pipe to the end of ArrayList
        temporaryPipe.add(p);
    }
    private int hitboxposition=0;
    public void buildHitbox(){
        if (hitbox == null && pipeList[hitboxposition] != null){
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
            System.out.println("form player "+id+" "+temporaryPipe);
            for (int i=0; i<pipeList.length; ++i){
                if ( pipeList[i] == null ){
                    pipeList[i] = temporaryPipe.remove(0); 
                    System.out.println("form player "+id+" "+temporaryPipe);
                    if ( i == 0 ){
                        pipeList[i].x = (pipeList[pipeList.length-1].x+pipeList[i].getWidth())+((pipeList[i].getWidth()*2)); 
                    }else{
                        pipeList[i].x = (pipeList[i-1].x+pipeList[i].getWidth())+((pipeList[i].getWidth()*2)); 
                    }
                    if (temporaryPipe.isEmpty()) return;
                }
            }
        }
    }
    // for Pipebulider create pipe for itself and another
    // make all player have same pipe
    public void buildPipe(Player a[]){
        if (firstStage){
            for (int i=0; i<pipeList.length; ++i){
                if (pipeList[i] == null){
                    pipeList[i] = new Pipe();
                    for (int j=0; j<a.length; ++j){
                        if ( a[j] == this ){
                        }else{
                            a[j].pipeList[i] = new Pipe(pipeList[i]);
                            a[j].pipeList[i].x += ((a[j].pipeList[i].getWidth()*2)+200)*i;
                            a[j].firstStage = false;
                        }
                    }
                    // create space between each pipe
                    pipeList[i].x += ((pipeList[i].getWidth()*2)+200)*i;
                }
            }
            firstStage = false; // end the initialize stage 
        }else{
            for (int i=0; i<pipeList.length; ++i){
                if (pipeList[i] == null){
                    Pipe newPipe = new Pipe();
                    pipeList[i] = newPipe;
                    for (int j=0; j<a.length; ++j){
                        if ( a[j] == this ){
                            continue;
                        }
                        if ( a[j].pipeList[i] != null ){
                            a[j].temporaryPipe.add(new Pipe(newPipe));
                            System.out.println("Tmp pipe added to player " + j);
                        }else{
                            a[j].pipeList[i] = new Pipe(newPipe);
                            if ( i == 0 ){
                                a[j].pipeList[i].x = (a[j].pipeList[a[j].pipeList.length-1].x+a[j].pipeList[i].getWidth())+((a[j].pipeList[i].getWidth()*2)); 
                            }else{
                                a[j].pipeList[i].x = (a[j].pipeList[i-1].x+a[j].pipeList[i].getWidth())+((a[j].pipeList[i].getWidth()*2)); 
                            }
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
