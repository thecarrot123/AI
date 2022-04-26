import java.util.*;
import java.lang.*;
import java.io.*;
//System.out.println();
//[0,0] [4,2] [2,7] [7,4] [0,8] [1,4]

public class MurhafAlawir{
    private static void controller(){
        Env env = new Env();
        BacktrackActor backtrackactor = new BacktrackActor(env);
        long starttime = System.nanoTime();
        System.out.println("Backtrack route:");
        backtrackactor.play();
        System.out.println("");
        long endtime = System.nanoTime();
        System.out.println("Backtrack moves: " + env.getMoveCounter() +", excution time: " + (endtime - starttime) / 1e9);
        env.reset();
        AStarActor astartactor = new AStarActor(env);
        starttime = System.nanoTime();
        System.out.println("A* route:");
        astartactor.play();
        System.out.println("");
        endtime = System.nanoTime();
        System.out.println("A* excution moves: " + env.getMoveCounter() +", excution time: " + (endtime - starttime) / 1e9);
        
    }

    public static void main(String[] args){
        controller();
    }
}

class QueueObj implements Comparable<QueueObj> {
    public int x;
    public int y;
    public int g;
    public int f;

    public QueueObj(){
        this.x = -1;
        this.y = -1;
        this.g = (int)1e9;
        this.f = (int)1e9;
    }

    public QueueObj(int x, int y, int g, int f){
        this.x = x;
        this.y = y;
        this.g = g;
        this.f = f;
    }
    
    public QueueObj(QueueObj obj){
		this.x = obj.x;
		this.y = obj.y;
		this.g = obj.g;
		this.f = obj.f;
	}
    
    public void print(){
		System.out.println(this.x + " " + this.y + " " + this.g + " " + this.f);
	}


    @Override
    public int compareTo(QueueObj obj) {
        return Integer.compare(this.f, obj.f);
    }
}

class Env{
    
    private int[][] ind = new int[6][2];
    private char[][] map = new char[10][10]; 
    private int variant;
    private int harryX;
    private int harryY;
    private int moveCounter;

    private int[] mx;
    private int[] my;
	
    private boolean checkMap(){
		if(observed(ind[3][0],ind[3][1]) == false && observed(ind[4][0],ind[4][1]) == false && 
			(ind[5][0] == ind[1][0] && ind[5][1] == ind[1][1]) == false && (ind[5][0] == ind[2][0] && ind[5][1] == ind[2][1]) == false)
			return true;
		return false;
    }


    private void generateRandomMap(){
        Random random = new Random();
        String temp = "HANBIE";
        while(checkMap() == false){
            for(int i=0;i<6;i++){
                int x = random.nextInt(8);
                int y = random.nextInt(8);
                ind[i][0] = x;
                ind[i][1] = y;
                map[x][y] = temp.charAt(i);
            }
        }
        for(int i=0;i<6;i++)
			System.out.print("[" + ind[i][0] + "," + ind[i][1] + "] ");
    }

    private void readInput(){
        Scanner sac = new Scanner(System.in);
        String s = sac.nextLine();
        int n = s.length();
        String temp = "HANBIE";
        if(n != 1){
            for(int i=0,k=0 ; i<n ; k++){
                i++;
                int x = s.charAt(i) - '0';
                i = i+2;
                int y = s.charAt(i) - '0';
                i = i+3;
                ind[k][0] = x;
                ind[k][1] = y;
                map[x][y] = temp.charAt(k);
            }
            s = sac.nextLine();
        }
        else{
            generateRandomMap();
        }
        variant = Integer.parseInt(s);

    }

    private boolean ok(int x, int y){
        return (x >= 0 && y >= 0 && x < 9 && y < 9);
    }

    private boolean observed(int x, int y){
        if(Math.abs(x-ind[1][0]) <= 2 && Math.abs(y-ind[1][1]) <= 2)
            return true;
        if(Math.abs(x-ind[2][0]) <= 1 && Math.abs(y-ind[2][1]) <= 1)
            return true;
        return false;
    }

    public char check(char ins[][]){
        for(int k=0;k<(variant==1?8:12);k++){
            int new_x = harryX + mx[k];
            int new_y = harryY + my[k];
            if( ok(new_x,new_y) == false)
                continue;
            if(ins[new_x][new_y] == '0'){
                if(observed(new_x,new_y) == false)
                    ins[new_x][new_y] = '1';
                else if(map[new_x][new_y] == 'N' || map[new_x][new_y] == 'A')
                    ins[new_x][new_y] = map[new_x][new_y];
                else
                    ins[new_x][new_y] = '2';
            }
        }
        return map[harryX][harryY];
    }
    
	public int getExitX(){
		return ind[5][0];
	}
	
	public int getExitY(){
		return ind[5][1];
	}
	
    public int getHarryX(){
        return harryX;
    }

    public int getHarryY(){
        return harryY;
    }

    public int getVariant(){
        return variant;
    }
    
    public int getMoveCounter(){
		return moveCounter;
	}


    public void move(int x, int y){
        harryX = x;
        harryY = y;
        moveCounter++;
        System.out.print(x + "," + y + " --> ");
    }
    
    public void reset(){
        harryX = ind[0][0];
        harryY = ind[0][1];
		moveCounter = 0;
	}

    public Env(){
        readInput();
        if(variant == 1){
            mx = new int[]{ 0, 0,+1,-1,-1,+1,+1,-1};
            my = new int[]{+1,-1, 0, 0,+1,-1,+1,-1};
        }
        else {
            mx = new int[]{-2,-2,-2,+2,+2,+2,-1, 0,+1,-1, 0,+1};
            my = new int[]{-1, 0,+1,-1, 0,+1,-2,-2,-2,+2,+2,+2};
        }
        moveCounter = 0;
        harryX = ind[0][0];
        harryY = ind[0][1];
    }

}

abstract class Actor{
    // 0 not explored, 1 not observed and not explored, 2 observed and not explored, 3 explored
    protected char[][] map = new char[10][10];
    protected char[][] ins = new char[10][10];
    protected boolean[][][][] mem = new boolean[10][10][2][2];
    protected int variant;
    protected Env env;

    protected int[] mx = new int[]{ 0, 0,+1,-1,-1,+1,+1,-1};
    protected int[] my = new int[]{+1,-1, 0, 0,+1,-1,+1,-1};

    protected boolean ok(int x, int y){
        return (x >= 0 && y >= 0 && x < 9 && y < 9);
    }

    public Actor(Env env){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                for(int k=0;k<2;k++){
                    for(int l=0;l<2;l++){
                        mem[i][j][k][l] = false;
                    }
                }
                ins[i][j] = '0';
            }
        }
        this.env = env;
        variant = env.getVariant();
    }
    
    public void enhancedCheck(char ins[][]){
		if(variant == 1) 
			return ;
		int[] checkMx = new int[]{-2,+2, 0, 0};
        int[] checkMy = new int[]{ 0, 0,-2,+2};
        int[] enhancedMx = new int[]{-1,+1, 0, 0};
        int[] enhancedMy = new int[]{ 0, 0,-1,+1};
        for(int k=0;k<4;k++){
			int x = env.getHarryX() + checkMx[k];
            int y = env.getHarryY() + checkMy[k];
			int new_x = env.getHarryX() + enhancedMx[k];
            int new_y = env.getHarryY() + enhancedMy[k];
            if( ok(x,y) == false)
                continue;
            if(ins[new_x][new_y] == '0' && ins[x][y] == '1')
				ins[new_x][new_y] = '1';
		}
		
	} 

    public abstract void play();
}

class BacktrackActor extends Actor{

    public BacktrackActor(Env env){
        super(env);
    }
    private boolean backtrack(int x, int y, int haveBook, int haveCloak, int counter){
        char c = env.check(ins);
        enhancedCheck(ins);
        if(c != '.')
            map[x][y] = c;
        if(map[x][y] == 'B'){
            haveBook = 1;
        }
        if(map[x][y] == 'I'){
            haveCloak = 1;
        }
        if(map[x][y] == 'E'){
            if(haveBook == 1){
                return true;
            }
        }
        boolean ret = false;
        mem[x][y][haveCloak][haveBook] = true;
        for(int k=0; k < 8 && ret == false; k++){
            int new_x = x + mx[k];
            int new_y = y + my[k];
            if( ok(new_x,new_y) == false)
                continue;
            if(ins[new_x][new_y] == '1' && mem[new_x][new_y][haveCloak][haveBook] != true){
                env.move(new_x,new_y);
                ret = ret | backtrack(new_x,new_y,haveBook,haveCloak,counter+1);
                env.move(x,y);
            }
            else if(ins[new_x][new_y] == '2' && haveCloak == 1 && mem[new_x][new_y][haveCloak][haveBook] != true){
                env.move(new_x,new_y);
                ret = ret | backtrack(new_x,new_y,haveBook,haveCloak,counter+1);
                env.move(x,y);
            }
        }
        return ret;
    }

    public void play(){
        int x = env.getHarryX();
        int y = env.getHarryY();
        boolean ans = backtrack(x,y,0,0,0);
        System.out.println(ans);
    }
}

class AStarActor extends Actor{

    private int[][] g = new int[10][10];
    private int[][][] parent = new int[10][10][2];
    private int exitX;
    private int exitY;

    public AStarActor(Env env){
        super(env);
        clear();
        exitX = env.getExitX();
        exitY = env.getExitY();
    }

    private int getH(int x, int y){
		return Math.max(Math.abs(exitX - x) , Math.abs(exitY - y));
    }

    private boolean AStart(int startX, int startY, int haveCloak){
        QueueObj temp,cur = new QueueObj(startX,startY,0,getH(startX,startY));
        PriorityQueue<QueueObj> pq = new PriorityQueue<QueueObj>();
        pq.add(cur);
        char c;
        while(pq.size() > 0){
            cur = new QueueObj(pq.poll());
            if(g[cur.x][cur.y] < cur.g)
                continue;
            if(cur.x == exitX && cur.y == exitY){
				//System.out.println(parent[cur.x][cur.y][0] + "///" + parent[cur.x][cur.y][1]);
				return true;
			}
            g[cur.x][cur.y] = cur.g;
			for(int k=0; k < 8; k++){
				int new_x = cur.x + mx[k];
				int new_y = cur.y + my[k];
				if( ok(new_x,new_y) == false)
					continue;
				if((ins[new_x][new_y] != '2' || (ins[new_x][new_y] == '2' && haveCloak == 1)) && g[new_x][new_y] > cur.g+1){
					temp = new QueueObj(new_x,new_y,cur.g+1,+cur.g+1 + getH(new_x,new_y));
					g[new_x][new_y] = cur.g+1;
					parent[new_x][new_y][0] = cur.x;
					parent[new_x][new_y][1] = cur.y;
					pq.add(temp);
				}
			}
        }
        return false;
    }
	
	private int backtrack(int x, int y, int haveBook, int haveCloak, int counter){
        char c = env.check(ins);
        enhancedCheck(ins);
        if(c != '.')
            map[x][y] = c;
        if(map[x][y] == 'B'){
            haveBook = 1;
        }
        if(map[x][y] == 'I'){
            haveCloak = 1;
        }
        if(haveCloak == 1 && haveBook == 1){
			return 3;
        }
        if(haveBook == 1 && x == exitX && y == exitY){
			return 4;
		}
        int ret = haveCloak + haveBook * 2;
        mem[x][y][haveCloak][haveBook] = true;
        for(int k=0; k < 8 && ret != 3; k++){
            int new_x = x + mx[k];
            int new_y = y + my[k];
            if( ok(new_x,new_y) == false)
                continue;
            if(ins[new_x][new_y] == '1' && mem[new_x][new_y][haveCloak][haveBook] != true){
                env.move(new_x,new_y);
                ret = ret | backtrack(new_x,new_y,haveBook,haveCloak,counter+1);
                if(ret != 3)
					env.move(x,y);
            }
            else if(ins[new_x][new_y] == '2' && haveCloak == 1 && mem[new_x][new_y][haveCloak][haveBook] != true){
                env.move(new_x,new_y);
                ret = ret | backtrack(new_x,new_y,haveBook,haveCloak,counter+1);
                if(ret != 3)
					env.move(x,y);
            }
        }
        return ret;
    }
    
	
	private char[][] copy(char[][] src) {
        if (src == null) {
            return null;
        }
        return Arrays.stream(src).map(char[]::clone).toArray(char[][]::new);
    }
    
    private void clear(){
		for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                g[i][j] = (int)1e9;
                parent[i][j][0] = parent[i][j][1] = -1;
            }
        }
	}
	
    private void getRoutFromExit(int x , int y, ArrayList[] list){
		int X=exitX;
		int Y=exitY;
		int temp;
		list[0] = new ArrayList<>();
		list[1] = new ArrayList<>();
		list[0].add(X);
		list[1].add(Y);
		while(((parent[X][Y][0] == x && parent[X][Y][1] == y) == false)){
			temp = X;
			X=parent[X][Y][0];
			Y=parent[temp][Y][1];
			list[0].add(X);
			list[1].add(Y);
			//System.out.println(X + "," + Y);
		}
	}
    
    //todo (check the excution)
	private boolean letsMove(int x, int y, int bt){		
		char[][] oldIns = new char[10][10];
        boolean ans = false;
		if(bt == 3)
			ans = AStart(x,y,1);
		if(bt == 2)
			ans = AStart(x,y,0);
		if(ans == false) {
			return false;
		}
		if(x == exitX && y == exitY)
			return true;
		ArrayList < Integer > [] list = new ArrayList[2];
		getRoutFromExit(x,y,list);
		Collections.reverse(list[0]);
		Collections.reverse(list[1]);
		for(int i = 0 ; i < list[0].size() ; i++){
			x = list[0].get(i);
			y = list[1].get(i);
			//System.out.println(x + " " + y);
			env.move(x,y);
			if(x == exitX && y == exitY)
				return true;
			oldIns = copy(ins);
			env.check(ins);
			enhancedCheck(ins);
			//if(Arrays.deepEquals(ins, oldIns) == false){
				//System.out.println(x + " " + y + " " + bt);
				//clear();
				//return letsMove(x,y,bt);
			//}
		}
		return ans;
	}
	
	//todo check if harry will lose at the start of the map
    public void play(){
		int x = env.getHarryX();
        int y = env.getHarryY();
        int bt = backtrack(x,y,0,0,0);
		if(bt == 4)
			return ;
        x = env.getHarryX();
        y = env.getHarryY();
        System.out.println(letsMove(x,y,bt));
			
    }
}

