package game;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Phyberosis on 2/18/2017.
 * move gen thread
 */

class MoveGen implements Runnable
{

    private LinkedList<byte[][]> mHistory;
    private byte[][] mScenario;
    private ScenarioHandler mScenarioHandler;
    private final GameEngine geRef;

    int progress;
    final LinkedList<Location> mScoreData;
    private final String TAG = "@ TaskDistributor @";

    private final byte BLANK = 0;
    private final byte PAWN=2;
    private final byte BISHOP=7;
    private final byte KNIGHT=6;
    private final byte ROOK=10;
    private final byte QUEEN=18;
    private final byte KING=127;

    MoveGen(byte[][] s, LinkedList<byte[][]> hist, GameEngine ge)
    {
        mHistory = new LinkedList<>();
        mHistory = hist;
        mScenario = new byte[9][8];
        mScenarioHandler = new ScenarioHandler();
        mScenario = mScenarioHandler.getNew(s);
        geRef = ge;
        mScoreData = new LinkedList<>();
    }

    private int abs(int a)
    {
        if(a < 0)
        {
            return a*-1;
        }else{
            return a;
        }
    }

    //check if location (x, y) is out of the board
    private boolean isOutOfBounds(int x, int y)
    {
        if (x > 7 || x < 0)
        {
            return true;
        }else if(y > 7 || y < 0)
        {
            return true;
        }

        return false;
    }

    private LinkedList<byte[][]> getChildScenariosOfTile(int x, int y, byte[][] scenario, byte[][] hist)
    {
        LinkedList<byte[][]> children = new LinkedList<>();

        byte[][] s = mScenarioHandler.getNew(scenario);

        if(s[x][y] != 0)//not blank and is my piece
        {
            LinkedList<Location> moves = getMoves(s[x][y], new Location(x, y), s, hist);
            if(moves == null)
            {
                return children;
            }
            for (Location l : moves)
            {
                byte[][] child = mScenarioHandler.getNew(s);
                int pc = abs(child[x][y]);
                switch (pc)
                {
                    case PAWN://promotion
                        if ((child[x][y] - 2) * 7 / 4 == l.mY)//move puts pc to end
                        {
                            addPawnPromotions(children, x, l.mX, l.mY, child);
                            continue;
                        }

                    case KING://castle
                        if (abs(x - l.mX) == 2)
                        {
                            children.add(Castle(x, y, l.mX, l.mY, child));
                            continue;

                        } else {
                            int index = ((abs(pc) / pc) + 1) / 2;
                            child[8][index] = 1;
                        }
                        break;

                    case ROOK://castle
                        int index = ((abs(pc) / pc) + 1) / 2; //1 or 0
                        if (child[8][index] == 1)//can't castle
                        {
                            break;
                        }
                        if (child[8][index] % 3 != 0 && x == 0)//left Rook hasn't moved && L being moved
                        {
                            child[8][index] *= 3;//moved
                            //System.out.println(TAG + "getChildScenariosOfTile: AI L :" + x + "," + y + " " + child[x][y] + " " + l.mX + "," + l.mY);
                            //printScenariod(child);
                        } else if (s[8][index] % 5 != 0 && x == 7)//R hasn't moved && R being moved
                        {
                            child[8][index] *= 5;//moved
                            //System.out.println(TAG + "getChildScenariosOfTile: AI R :" + x + "," + y + " " + child[x][y] + " " + l.mX + "," + l.mY);
                            //printScenariod(child);
                        }
                        break;
                }
                /*if(l.mX == -1)
                {
                    printScenariod(s);
                }*/
                child = mScenarioHandler.movePiece(x, y, l.mX, l.mY, s);
                children.add(child);//normal move found
            }
        }

        return children;
    }

    private void addPawnPromotions(LinkedList<byte[][]> children, int x, int toX, int toY, byte[][] sen)
    {
        int fromY = toY*5/7 + 1;
        /***/
        byte[][] knight = new byte[9][8];//new sen
        for (int mx = 0; mx < 8; mx++)
        {
            System.arraycopy(sen[mx], 0, knight[mx], 0, 8);
        }
        //set new pc
        knight[toX][toY] = (byte)(KNIGHT*sen[x][fromY]/2);//knight * PLayer or AI (PAWN = 2 so +-2 / 2)
        knight[x][fromY] = BLANK;//erase pawn
        /***/
        byte[][] bishop = new byte[9][8];
        for (int mx = 0; mx < 8; mx++)
        {
            System.arraycopy(sen[mx], 0, bishop[mx], 0, 8);
        }
        bishop[toX][toY] = (byte)(BISHOP*sen[x][fromY]/2);
        bishop[x][fromY] = BLANK;//erase pawn
        /***/
        byte[][] rook = new byte[9][8];
        for (int mx = 0; mx < 8; mx++)
        {
            System.arraycopy(sen[mx], 0, rook[mx], 0, 8);
        }
        rook[toX][toY] = (byte)(ROOK*sen[x][fromY]/2);
        rook[x][fromY] = BLANK;//erase pawn
        /***/
        byte[][] queen = new byte[9][8];
        for (int mx = 0; mx < 8; mx++)
        {
            System.arraycopy(sen[mx], 0, queen[mx], 0, 8);
        }
        queen[toX][toY] = (byte)(QUEEN*sen[x][fromY]/2);
        queen[x][fromY] = BLANK;//erase pawn

        children.add(knight);
        children.add(bishop);
        children.add(rook);
        children.add(queen);
    }

    private byte[][] Castle(int fromX, int fromY, int toX, int toY, byte[][] sen)
    {
        byte[][] ret;
        //update board and current scenario
        //move King
        byte attacker = (byte)(sen[fromX][fromY]/abs(sen[fromX][fromY]));
        ret = mScenarioHandler.movePiece(fromX, fromY, toX, toY, sen);
        ret[8][(attacker+1)/2] = 1; //set castleInfo

        int rookX;
        if(toX < 4)
        {
            rookX = 0;
        }else{
            rookX = 7;
        }
        //move rook
        ret = mScenarioHandler.movePiece(rookX, fromY, (fromX-toX)/2+toX, toY, ret);
        return ret;
    }

    //returns a list of moves (x, y) that a piece of type "piece" can move to from location "l"
    private LinkedList<Location> getMoves(byte pc, Location l, byte[][] scenario, byte[][] hist)
    {
        ////System.out.println(TAG + "getMoves getting move for " + attacker.toString() + "'s " + piece.toString());

        //decide which piece it is and calls appropriate method to generate a list of possible moves
        switch (abs(pc))
        {
            case PAWN:
                return getPawnMoves(l, scenario, hist);
            case BISHOP:
                return getBishopMoves(l, scenario);
            case KNIGHT:
                return getKnightMoves(l, scenario);
            case ROOK:
                return getRookMoves(l, scenario);
            case QUEEN:
                return getQueenMoves(l, scenario);
            case KING:
                return getKingMoves(l, scenario);
            default:
                return null;
        }
    }

    private LinkedList<Location> getKingMoves(Location location, byte[][] scenario)
    {
        LinkedList<Location> moveList = new LinkedList<>();
        int x = location.mX;
        int y = location.mY;

        byte attacker = (byte)(scenario[x][y]/(abs(scenario[x][y])));

        int dx, dy;//tiles to increment from location of bishop
        //search direction in {x, y} -> amount to increment in x and y directions to reach each tile to scan
        final int[][] searchDirection = {//direction check list
                {-1, 0}, {1, 0}, {0, 1}, {0, -1},
                {1, -1}, {-1, -1}, {-1, 1}, {1, 1}
        };

        /** 1 **/
        byte castleInfo = scenario[8][(attacker+1)/2]; // see header for use

        for (int i = 0; i < 8; i++) //per direction loop
        {
            //sets new search direction
            dx = searchDirection[i][0];
            dy = searchDirection[i][1];

            if (isOutOfBounds(x + (dx), y + (dy)))
            {continue;}
            //check castle
            {/**
             * 1 the king hasn't moved
             * 2 the respective rook hasn't moved
             * 3 there are no pieces in the way
             * 4 king not in check
             * 5 king would not be in check
             *
             * note 4 and 5 should be handled by the min-max -> checking if king is in check is too hard
             **/}

            if ((i == 0 || i == 1) && castleInfo % 2 == 0 && scenario[i*7][y] != BLANK)/**1**///directions left and right, coded in direction check list && rook exists
            {

                /** 2 **/
                if (castleInfo % (i *2 +3) != 0) /**2**/ //mod 3 is left, mod 5 R
                {
                    /** 3 **/
                    int ii = i*7 - (i*2-1);//starting from one closer than rook position -> x is 1 or 6
                    while(ii != x)
                    {
                        if (scenario[ii][y] != 0)
                            break;

                        ii -= dx;
                    }
                    //early break means found blocking piece -> i != x
                    if (ii == x)
                    {
                        moveList.add(new Location(dx*2 + x, y)); //king moves two spaces
                        //debug
                        //if(Arrays.equals(scenario, mCurrentScenario))
                        //mGameBoard.swapColours();

                    }
                }
            }

            ////System.out.println(TAG + "getKingMoves: scanning tile " + (x + (dx)) + ", " + (y + (dy)));
            if (scenario[x + dx][y + dy] * attacker <= 0)//if scanned tile is blank or occupied by enemy
            {
                moveList.add(new Location(x + (dx), y + (dy)));
            }
        }

        ////System.out.println("Phyberosis", "getKingMoves returned moveList of size " + moveList.size());
        return moveList;
    }

    private LinkedList<Location>  getQueenMoves(Location location, byte[][] scenario)
    {

        LinkedList<Location> moveList = new LinkedList<>();
        int x = location.mX;
        int y = location.mY;

        byte attacker = (byte)(scenario[x][y]/(abs(scenario[x][y])));

        int dx, dy;//tiles to increment from location of bishop
        //search direction in {x, y} -> amount to increment in x and y directions to reach each tile to scan
        final int[][] searchDirection = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, -1}, {-1, -1}, {-1, 1}, {1, 1}
        };

        for (int i = 0; i < 8; i++) //per direction loop
        {
            //sets new search direction
            dx = searchDirection[i][0];
            dy = searchDirection[i][1];

            int ii = 0; //per tile loop
            while (true) //add moves until no more moves in current direction
            {

                ii++;//increases distance from current position, in current direction
                if (isOutOfBounds(x + (dx * ii), y + (dy * ii)))
                {
                    break;
                }else if (scenario[x + (dx * ii)][y + (dy * ii)] * attacker <= 0)//if scanned tile is blank or enemy
                {
                    moveList.add(new Location(x + (dx * ii), y + (dy * ii)));
                    if(scenario[x + (dx * ii)][y + (dy * ii)]*attacker < 0) //enemy
                        break;
                }else //not blank and not enemy -> found piece of same allegiance as current bishop
                {
                    break;
                }
            }
        }

        ////System.out.println("Phyberosis", "getQueenMoves returned moveList of size " + moveList.size());
        return moveList;
    }

    //generates a list of moves for a knight at location "location"
    //attacker is an integer, -1 or 1 -> determines if it is a player's knight or ai's knight
    /**see "getBishopMoves" for detailed explanation of method**/
    private LinkedList<Location>  getKnightMoves(Location location, byte[][] scenario)
    {
        LinkedList<Location> moveList = new LinkedList<>();
        int x = location.mX;
        int y = location.mY;

        byte attacker = (byte)(scenario[x][y]/(abs(scenario[x][y])));

        int dx, dy;
        final int[][] searchDirection = {
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2},
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}
        };

        for (int i = 0; i < 8; i++) //per direction loop
        {
            //sets new search direction
            dx = searchDirection[i][0];
            dy = searchDirection[i][1];

            byte test;
            if (isOutOfBounds(x + (dx), y + (dy)))
            {
                continue;
            }else{
                test = scenario[x + (dx)][y + (dy)];
            }
            if (test * attacker <= 0)//if scanned tile is blank or enemy
            {
                moveList.add(new Location(x + (dx), y + (dy)));
            }
        }

        return moveList;
    }

    //generates a list of moves for a bishop at location "location"
    //attacker is an integer, -1 or 1 -> determines if it is a player's bishop or ai's bishop
    private LinkedList<Location> getBishopMoves(Location location, byte[][] scenario)
    {
        LinkedList<Location> moveList = new LinkedList<>();
        int x = location.mX;
        int y = location.mY;

        byte attacker = (byte)(scenario[x][y]/(abs(scenario[x][y])));

        int dx, dy;//tiles to increment from location of bishop
        //search direction in {x, y} -> amount to increment in x and y directions to reach each tile to scan
        final int[][] searchDirection = {
                {1, -1}, {-1, -1}, {-1, 1}, {1, 1}
        };

        for (int i = 0; i < 4; i++) //per direction loop
        {
            //sets new search direction
            dx = searchDirection[i][0];
            dy = searchDirection[i][1];

            int ii = 0; //per tile loop
            while (true) //add moves until no more moves in current direction
            {

                ii++;//increases distance from current position, in current direction
                byte test;
                if (isOutOfBounds(x + (dx * ii), y + (dy * ii)))
                {
                    break;
                }else{
                    test = scenario[x + (ii*dx)][y + (ii*dy)];
                }
                if (test * attacker <= 0)//if scanned tile is blank or enemy
                {
                    moveList.add(new Location(x + (dx * ii), y + (dy * ii)));
                    if(test*attacker < 0)//enemy
                        break;
                }else //not blank and not enemy -> found piece of same allegiance as current bishop
                {
                    break;
                }
            }
        }

        return moveList;
    }

    //follows same idea and format as other get_move methods
    private LinkedList<Location>  getRookMoves(Location location, byte[][] scenario)
    {
        //////System.out.println(TAG + "getRookMoves: test " + mGBoard.getAllegiance(6, 7).toString());
        LinkedList<Location> moveList = new LinkedList<>();
        int x = location.mX;
        int y = location.mY;

        byte attacker = (byte)(scenario[x][y]/(abs(scenario[x][y])));

        int dx, dy;//tiles to increment from location of bishop
        //search direction in {x, y} -> amount to increment in x and y directions to reach each tile to scan
        final int[][] searchDirection = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int i = 0; i < 4; i++) //per direction loop
        {
            //sets new search direction
            dx = searchDirection[i][0];
            dy = searchDirection[i][1];

            int ii = 0; //per tile loop
            while (true) //add moves until no more moves in current direction
            {

                ii++;//increases distance from current position, in current direction
                byte test;
                if (isOutOfBounds(x + (dx * ii), y + (dy * ii)))
                {
                    break;
                }else{
                    test = scenario[x + (ii*dx)][y + (ii*dy)];
                }

                if (test * attacker <= 0)//if scanned tile is blank or enemy
                {
                    moveList.add(new Location(x + (dx * ii), y + (dy * ii)));
                    if(test*attacker < 0) //enemy
                        break;
                }else //not blank and not enemy -> found piece of same allegiance as current bishop
                {
                    break;
                }
            }
        }

        ////System.out.println("Phyberosis", "getRookMoves returned moveList of size " + moveList.size());
        return moveList;
    }

    //follows same idea and format as other get_move methods
    private LinkedList<Location>  getPawnMoves(Location location, byte[][] scenario, byte[][] hist)
    {
        byte PLAYER = 1;
        byte AI = -1;

        LinkedList<Location> moveList = new LinkedList<>();
        int x = location.mX;
        int y = location.mY;

        byte attacker = (byte)(scenario[x][y]/(abs(scenario[x][y])));

        //one forward
        if (y - attacker < 8 && y - attacker >= 0) //in bounds
        {
            if(scenario[x][y - attacker] == 0)  //tile empty?
            {
                moveList.add(new Location(x, y - attacker));

                //if Pawn is in starting position -> double space
                if ((location.mY == 6 && attacker == PLAYER) || (location.mY == 1 && attacker == AI))
                {
                    if (scenario[x][y - 2 * attacker] == 0)      //and tile empty
                    {
                        moveList.add(new Location(x, y - (2 * attacker)));
                    }
                }
            }
            boolean leftInBounds = false, rightInBounds=false;
            //capture left
            if (x - 1 >= 0 ) //in bounds
            {
                leftInBounds = true;
                if (scenario[x-1][y-attacker]*attacker < 0) //tile has enemy
                {
                    moveList.add(new Location(x - 1, y-attacker));
                }
            }

            //capture right
            if (x + 1 >= 0) //in bounds
            {
                rightInBounds = true;
                if (scenario[x+1][y-attacker]*attacker < 0) //tile has enemy
                {
                    moveList.add(new Location(x + 1, y-attacker));
                }
            }


            //if pawn is in location for enPassant -> check enPassant
            /**
             * 1 there is pawn on side
             * 2 there is not respective pawn in starting position on that side
             * 3 there was pawn on starting position
             * 4 there was not pawn on side
             * **/
            if ((location.mY == 3 && attacker == PLAYER) || (location.mY == 3 && attacker == AI))
            {

                for (int i = -1; i < 2; i+=2)
                {
                    //check left then right using the for loop above
                    if (((i == -1 && leftInBounds) || (i == 1 && rightInBounds)) && //left/right side in bounds
                            scenario[x+i][y]*attacker < 0 &&                /**1**/
                            scenario[x+i][y-(2*attacker)]*attacker < 0 &&   /**2**/
                            hist[x+i][y]*attacker == 0 &&                   /**3**/
                            hist[x+i][y-(2*attacker)]*attacker == 0)        /**4**/
                    {
                        moveList.add(new Location(x + i, y+attacker));
                    }
                }
            }
        }

        //////System.out.println("Phyberosis", "getPawnMoves() first returned x value: " + ((Location)moveList.getFirst()).mX);
        return moveList;
    }

    void begin()
    {
        Thread me = new Thread(this);
        me.start();
    }

    public void run()
    {
        int depth = 2; /**@@ (0 is one move only)set depth (one depth = one turn of EITHER player or ai, NOT both) @@**/

        byte[][] myHist = mHistory.get(mHistory.size() - 2);

        LinkedList<byte[][]> outcomes = new LinkedList<>();

        int id = 0;

        byte[][] histP = mHistory.getLast();
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {

                if (mScenario[x][y] > 0)//not Ai piece
                    continue;

                for (byte[][] child : getChildScenariosOfTile(x, y, mScenario, myHist)) //if tile -> no children
                {//printScenariod(child);

                    //id should match child location
                    outcomes.add(child);

                    MinMax mm = new MinMax(this, id, child, histP, myHist, depth);
                    mm.begin();
                    id++;
                }
            }
        }

        while (progress < id)//report progress as mm threads run
        {
            try
            {
                synchronized (this)
                {
                    this.wait(250);
                }
            } catch (InterruptedException ignored)
            {}
        }

        int bestScore = Integer.MAX_VALUE;
        LinkedList<Location> bestMoves = new LinkedList<>();
        for(Location data : mScoreData)//location x is id, y is score
        {
            if(data.mY < bestScore)
            {
                bestScore = data.mY;
                bestMoves.clear();
                bestMoves.add(data);
            }else if (data.mY == bestScore)
            {
                bestMoves.add(data);
            }
        }

        if(bestMoves.isEmpty()) //game over
        {
            synchronized (geRef)
            {
                geRef.myMove = null;
            }
        }

        for (Location data : bestMoves)
        {
            System.out.println(TAG + "run: best score(s) -> " + data.mY);
        }

        //no best score found
        Random r = new Random(System.currentTimeMillis());
        int rand = r.nextInt(bestMoves.size());

        Location best = bestMoves.get(rand);
        System.out.println(TAG + "run: picked -> " + best.mY);

        synchronized (geRef)
        {
            geRef.myMove = outcomes.get(best.mX);
            geRef.done = true;
        }
    }
}
