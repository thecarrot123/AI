import java.util.*;
import java.lang.*;
import java.io.*;
//System.out.println();
//[0,0] [4,2] [2,7] [7,4] [0,8] [1,4]

public class MurhafAlawir{
    private static void controller(){
        Env env = new Env();
        BacktrackActor act = new BacktrackActor(env);
        long starttime = System.nanoTime();
        act.play();
        long endtime = System.nanoTime();
        System.out.println("back track excution time: " + (endtime - starttime) / 1e9);
    }

    public static void main(String[] args){
        controller();
    }
}

class QueueObj implements Comparable<QueueObj> {
    public int f;
    public int g;
    public int x;
    public int y;

    public QueueObj(){
        f = (int)1e9;
        g = (int)1e9;
        x = -1;
        y = -1;
    }

    public QueueObj(int f, int g, int x, int y){
        this.f = f;
        this.g = g;
        this.x = x;
        this.y = y;
    }


    @Override
    public int compareTo(QueueObj obj) {
        return Integer.compare(this.val, obj.val);
    }
}

class Env{
    
    private int[][] ind = new int[6][2];
    private char[][] map = new char[10][10]; 
    private int variant;
    private int harryX;
    private int harryY;
    //todo

    private int[] mx;
    private int[] my;

    private boolean checkMap(){
        return true;
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
        harryX = ind[0][0];
        harryY = ind[0][1];
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

    public void move(int x, int y){
        harryX = x;
        harryY = y;
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

    public abstract void play();
}

class BacktrackActor extends Actor{

    public BacktrackActor(Env env){
        super(env);
    }
    private boolean backtrack(int x, int y, int haveBook, int haveCloak, int counter){
        char c = env.check(ins);
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
        System.out.println(x);
        System.out.println(y);
        System.out.println(counter);
        System.out.println();
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
        mem[x][y][haveCloak][haveBook] = false;
        return ret;
    }

    public void play(){
        int x = env.getHarryX();
        int y = env.getHarryY();
        boolean ans = backtrack(x,y,0,0,0);
        System.out.println(ans);
    }
}

