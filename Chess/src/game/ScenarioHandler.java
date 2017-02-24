package game;

/**
 * Created by Phyberosis on 1/11/2017.
 */

public class ScenarioHandler
{

    private final byte BLANK = 0;

    private final byte PAWN=2;
    private final byte BISHOP=7;
    private final byte KNIGHT=6;
    private final byte ROOK=10;
    private final byte QUEEN=18;
    private final byte KING=127;

    //private final byte PLAYER=1;
    private final byte AI= -1;

    /*
    private final byte BLANK = 0;

    private final byte MYPAWN=1;
    private final byte MYBISHOP=2;
    private final byte MYKNIGHT=3;
    private final byte MYROOK=4;
    private final byte MYQUEEN=5;
    private final byte MYKING=6;

    private final byte PLAYERPAWN=-7;
    private final byte PLAYERBISHOP=8;
    private final byte PLAYERKNIGHT=9;
    private final byte PLAYERROOK=10;
    private final byte PLAYERQUEEN=11;
    private final byte PLAYERKING=12;*/

    private int abs(int x)
    {
        if (x < 0)
            return -x;

        return x;
    }

    /*
    public byte getBLANK(){return BLANK;}
    public byte getPAWN(){return PAWN;}
    public byte getBISHOP(){return BISHOP;}
    public byte getKNIGHT(){return KNIGHT;}
    public byte getROOK(){return ROOK;}
    public byte getQUEEN(){return QUEEN;}
    public byte getKING(){return KING;}*/


    public void setBlankTile(int x, int y, byte[][] s)
    {
        s[x][y] = BLANK;
    }
    boolean getIsMine(int x, int y, byte[][] s)
    {
        return s[x][y] < BLANK;
    }

    byte[][] movePiece(int Fx, int Fy, int Tx, int Ty, byte[][] from)
    {
        byte[][] s = new byte[9][8];

        for(int x = 0; x < 9; x++)
        {
            System.arraycopy(from[x], 0, s[x], 0, 8);
        }

        if (Tx == -1)
        {
            System.out.println("@@@@@@@@" + "movePiece: " + s[Fx][Fy]);
        }
        s[Tx][Ty] = s[Fx][Fy];
        s[Fx][Fy] = BLANK;
        return s;

    }

    byte[][] getNew(byte[][] old)
    {
        byte[][] b = new byte[9][8];

        for(int x = 0; x < 9; x++)
        {
            System.arraycopy(old[x], 0, b[x], 0, 8);
        }

        return b;
    }
    { /*
    void placePiece(byte pc, boolean isMine, int x, int y, byte[][] s)
    {

        switch (pc)
        {
            case PAWN:
                if(isMine)
                {
                    s[x][y] = PAWN*AI;
                }else{
                    s[x][y] = PAWN;
                }break;
            case BISHOP:
                if(isMine)
                {
                    s[x][y] = BISHOP*AI;
                }else{
                    s[x][y] = BISHOP;
                }break;
            case KNIGHT:
                if(isMine)
                {
                    s[x][y] = KNIGHT*AI;
                }else{
                    s[x][y] = KNIGHT;
                }break;
            case ROOK:

                if(isMine)
                {
                    s[x][y] = ROOK*AI;
                }else{
                    s[x][y] = ROOK;
                }break;
            case QUEEN:
                if(isMine)
                {
                    s[x][y] = QUEEN*AI;
                }else{
                    s[x][y] = QUEEN;
                }break;
            case KING:
                if(isMine)
                {
                    s[x][y] = KING*AI;
                }else{
                    s[x][y] = KING;
                }break;
        }
    }*/}

}
