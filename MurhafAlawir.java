import java.util.*;
import java.lang.*;
import java.io.*;
//System.out.println();
//[0,0] [4,2] [2,7] [7,4] [0,8] [1,4]

public class MurhafAlawir{
    private static void controller(){
        Env env = new Env();
        Actor act = new Actor(env);
        act.play();
    }

    public static void main(String[] args){
        controller();
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
        if(Math.abs(x-ind[1][0])+Math.abs(y-ind[1][1]) <= 2)
            return true;
        if(Math.abs(x-ind[2][0])+Math.abs(y-ind[2][1]) <= 1)
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
                else
                    ins[new_x][new_y] = '2';
            }
        }
        return map[harryX][harryY];
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

class Actor{
    // 0 not explored, 1 not observed and not explored, 2 observed and not explored, 3 explored
    private char[][] map = new char[10][10];
    private char[][] ins = new char[10][10];
    private boolean[][] mem = new int[10][10][2][2];
    private int variant;
    private Env env;

    private int[] mx = new int[]{ 0, 0,+1,-1,-1,+1,+1,-1};
    private int[] my = new int[]{+1,-1, 0, 0,+1,-1,+1,-1};

    private boolean ok(int x, int y){
        return (x >= 0 && y >= 0 && x < 9 && y < 9);
    }

    private boolean backtrack(int x, int y, int state, boolean haveBook, boolean haveCloak){
        env.move(x,y);
        char c = env.check(ins);
        if(c != '.')
            map[x][y] = c;
        if(map[x][y] == 'B'){
            state = state | 2;
            haveBook = true;
        }
        if(map[x][y] == 'I'){
            state = state | 4;
            haveCloak = true;
        }
        if(map[x][y] == 'E'){
            if(haveBook)
                return true;
        }
        System.out.println(x);
        System.out.println(y);
        System.out.println(state);
        System.out.println();
        boolean ret = false;
        mem[x][y] = state;
        for(int k=0;k < 8 && ret == false;k++){
            int new_x = x + mx[k];
            int new_y = y + my[k];
            if( ok(new_x,new_y) == false)
                continue;
            if(ins[new_x][new_y] == '1' && mem[new_x][new_y] != state){
                ret = ret | backtrack(new_x,new_y,state,haveBook,haveCloak);
            }
            else if(ins[new_x][new_y] == '2' && state > 4 && mem[new_x][new_y] != state){
                ret = ret | backtrack(new_x,new_y,state,haveBook,haveCloak);
            }
        }
        return ret;
    }

    public Actor(Env env){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                map[i][j] = '.';
                mem[i][j] = 0;
                ins[i][j] = '0';
            }
        }
        this.env = env;
        variant = env.getVariant();
    }

    public void play(){
        int x = env.getHarryX();
        int y = env.getHarryY();
        boolean ans = backtrack(x,y,1,false,false);
        System.out.println(ans);
    }
}

