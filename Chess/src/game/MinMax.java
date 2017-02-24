package game;

import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by Phyberosis on 2/18/2017.
 *
 * Move gen uses this to get moves per pc
 * must set with NEW byte arrays
 */

class MinMax implements Runnable
{

    private final MoveGen mgRef;
    private int ID;
    private ScenarioHandler mScenarioHandler;

    private final byte BLANK = 0;
    private final byte PAWN=2;
    private final byte BISHOP=7;
    private final byte KNIGHT=6;
    private final byte ROOK=10;
    private final byte QUEEN=18;
    private final byte KING=127;

    private final int STNDRD = 51;
    private final int ATT = 60;
    private final int DEF = 62;
    // mod 2 gets both att and def
    // mod 3 get STN and att
    
    private byte[][] mScenario;
    private byte[][] mPlayerH;
    private byte[][] mMyH;
    private int mDepth;
    private final int DEEP = 4;
    
    private final String TAG = "@ MinMax @";

    MinMax(MoveGen mg, int id, byte[][] s, byte[][] histP, byte[][] histA, int depth)
    {
        mgRef = mg;
        mScenario = s;
        mPlayerH = histP;
        mMyH = histA;
        mDepth = depth;
        this.ID = id;
        mScenarioHandler = new ScenarioHandler();

    }

    private int max(int i , int j)
    {
        if(i > j)
            return i;

        return j;
    }

    private int min(int i , int j)
    {
        if(i < j)
            return i;

        return j;
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

    //histP and histA are player last move and AI last move
    //a and b are best possible scores for this move branch
    private int alphaBeta(byte[][] s, byte[][] histP, byte[][] histA, int depth, int a, int b, boolean maxPlayer)
    {
        /*
        if (depth == 2)
        {
            System.out.println(TAG + "alphaBeta: testing scenario");
            printScenariod(s);
            System.out.println(TAG + "alphaBeta: depth 2, score:" + calculateScore(s));
        }*/

        //int i = 0;

    	boolean deep = false;
        if (depth > -1*DEEP-1) //arrived at specified depth
        {
        	deep = true;
            //System.out.println(TAG + "alphaBeta: depth reached, score:" + calculateScore(s));
        }else{
            return calculateScore(s); //the score of the scenario, pos is player advantage, neg is my advantage
        }

        int v;
        if(maxPlayer)//max player means its player's turn in move tree
        {
            v = Integer.MIN_VALUE;
            //the two for loops get the child of each tile -> total is all children of scenario
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    if(s[x][y] < 0)
                        continue; //not plr piece

                    LinkedList<byte[][]> children = getChildScenariosOfTile(x, y, s, histP, deep);
                    if(children.isEmpty())
                    {
                    	return calculateScore(s); 
                    }
                    for (byte[][] child : children)
                    {
                        v = max(v, alphaBeta(child, histP, s, depth - 1, a, b, false));
                        a = max(a, v);

                        //debug
                        //i++;
                        /*if(b < a)
                        {
                            System.out.println(TAG + "alphaBeta: alpha cut " + a);
                            return v;
                        }*/
                    }
                }
            }

        }else{
            v = Integer.MAX_VALUE;
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    if(s[x][y] > 0)
                        continue;//not AI's piece

                    LinkedList<byte[][]> children = getChildScenariosOfTile(x, y, s, histP, deep);
                    if(children.isEmpty())
                    {
                    	return calculateScore(s); 
                    }
                    for (byte[][] child :children)
                    {
                        v = min(v, alphaBeta(child, s, histA, depth-1, a, b, true));
                        b = min(b, v);

                        //debug
                       //i++;

                        /*if(b < a)
                        {
                            System.out.println(TAG + "alphaBeta: beta cut " + b + " " + a);
                            return v;
                        }*/
                    }
                }
            }
        }
        /*
        //debug
        if (depth == 3)
        {
            synchronized (mgRef)
            {
                System.out.println(TAG + "alphaBeta: ran through " + i + " moves, id:" + ID);
            }
        }*/
        return v;
    }

    //sums the all piece values of board
    private int calculateScore(byte[][] s)
    {
        int score;
        int matFactor = 100;
        int pawnFactor = 1;

        //materialistic calc
        int matScore = 0;
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                matScore += s[x][y];
                if(abs(s[x][y]) == KING)
                    matScore += s[x][y]*100;
            }
        }
        score = matScore * matFactor;

        //pawn advancement
        int pawnScore = 0;
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (abs(s[x][y]) == 2)
                {
                    pawnScore += (s[x][y] * 7/2) - (y - 7);
                }
            }
        }
        score += pawnScore * pawnFactor;

        return score;
    }

    private LinkedList<byte[][]> getChildScenariosOfTile(int x, int y, byte[][] scenario, byte[][] hist, boolean deep)
    {
        LinkedList<byte[][]> children = new LinkedList<>();

        byte[][] s = mScenarioHandler.getNew(scenario);

        if(s[x][y] != 0)//not blank and is my piece
        {
            LinkedList<Location> moves = getMoves(s[x][y], new Location(x, y), s, hist, deep);
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

    private LinkedList<Location> getDef(byte pc, Location l, byte[][] scenario, byte[][] hist)
    {
    	LinkedList<Location> moves;
        
        //decide which piece it is and calls appropriate method to generate a list of possible moves
        switch (abs(pc))
        {
            case PAWN:
                moves = getPawnMoves(l, scenario, hist, DEF);
                break;
            case BISHOP:
                moves = getBishopMoves(l, scenario, DEF);
                break;
            case KNIGHT:
                moves = getKnightMoves(l, scenario, DEF);
                break;
            case ROOK:
                moves = getRookMoves(l, scenario, DEF);
                break;
            case QUEEN:
                moves = getQueenMoves(l, scenario, DEF);
                break;
            case KING:
                moves = getKingMoves(l, scenario, DEF);
                break;
            default:
                return null;
        }

        return moves;
    }
    
    //returns a list of moves (x, y) that a piece of type "piece" can move to from location "l"
    private LinkedList<Location> getMoves(byte pc, Location l, byte[][] scenario, byte[][] hist, boolean deep)
    {
        ////System.out.println(TAG + "getMoves getting move for " + attacker.toString() + "'s " + piece.toString());

        LinkedList<Location> moves;
        int mode;
        if(deep)
        {
        	mode = ATT;
        }else{
        	mode = STNDRD;
        }
        //decide which piece it is and calls appropriate method to generate a list of possible moves
        switch (abs(pc))
        {
            case PAWN:
                moves = getPawnMoves(l, scenario, hist, mode);
                break;
            case BISHOP:
                moves = getBishopMoves(l, scenario, mode);
                break;
            case KNIGHT:
                moves = getKnightMoves(l, scenario, mode);
                break;
            case ROOK:
                moves = getRookMoves(l, scenario, mode);
                break;
            case QUEEN:
                moves = getQueenMoves(l, scenario, mode);
                break;
            case KING:
                moves = getKingMoves(l, scenario, mode);
                break;
            default:
                return null;
        }

        return moves;
    }

    private LinkedList<Location> getKingMoves(Location location, byte[][] scenario, int searchType)
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

        if(searchType == STNDRD)
        	
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

            if ((i == 0 || i == 1) && castleInfo % 2 == 0 && scenario[i*7][y]/(scenario[x][y]/127*ROOK) == 1 && searchType == STNDRD)/**1**///directions left and right, coded in direction check list && rook exists
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
            if (scenario[x + dx][y + dy] * attacker <= BLANK && searchType == STNDRD)//if scanned tile is blank or occupied by enemy
            {
                moveList.add(new Location(x + (dx), y + (dy)));
            }else if(searchType == ATT)
            {
            	if(scenario[x + dx][y + dy] * attacker < BLANK)
            	{
            		moveList.add(new Location(x + (dx), y + (dy)));
            	}
            }else if(searchType == DEF)
            {
            	if(scenario[x + dx][y + dy] * attacker > BLANK)
            	{
            		moveList.add(new Location(x + (dx), y + (dy)));
            	}
            }
        }

        ////System.out.println("Phyberosis", "getKingMoves returned moveList of size " + moveList.size());
        return moveList;
    }

    private LinkedList<Location>  getQueenMoves(Location location, byte[][] scenario, int searchType)
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
            int newX, newY;
            while (true) //add moves until no more moves in current direction
            {

                ii++;//increases distance from current position, in current direction
                newX = x + (dx * ii);
                newY = y + (dy * ii);
                if (isOutOfBounds(newX, newY))
                {
                    break;
                }else if (searchType == STNDRD)
                {
                	if(scenario[newX][newY] <= BLANK)//if scanned tile is blank or enemy
                	{
                        moveList.add(new Location(newX, newY));
                        if(scenario[newX][newY]*attacker < BLANK) //enemy
                        {
                        	break;
                        }
                	}else{
                		break;//not blank and not enemy -> found piece of same allegiance as current Queen
                	}
                }else if(searchType == ATT){
                	if(scenario[newX][newY] < BLANK)
                	{
                        moveList.add(new Location(newX, newY));
                        break;
                	}else if(scenario[newX][newY] > BLANK)
                	{
                		break;
                	}
                }else if(searchType == DEF){
                	if(scenario[newX][newY] > BLANK)
                	{
                        moveList.add(new Location(newX, newY));
                        break;
                	}else if(scenario[newX][newY] < BLANK)
                	{
                		break;
                	}
                }
            }

            if(!moveList.isEmpty())
            {
                moveList.addFirst(moveList.removeLast()); //assuming interesting moves at max move length
            }
        }

        ////System.out.println("Phyberosis", "getQueenMoves returned moveList of size " + moveList.size());
        return moveList;
    }

    //generates a list of moves for a knight at location "location"
    //attacker is an integer, -1 or 1 -> determines if it is a player's knight or ai's knight
    /**see "getBishopMoves" for detailed explanation of method**/
    private LinkedList<Location>  getKnightMoves(Location location, byte[][] scenario, int searchType)
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
            if (searchType == STNDRD)
            {
            	if(test*attacker <= BLANK)//if scanned tile is blank or enemy
            	{
                    moveList.add(new Location(x + (dx), y + (dy)));
            	}

            }else if(searchType == ATT){
            	
            	if(test < BLANK)
            	{
                    moveList.add(new Location(x + (dx), y + (dy)));
            	}
            }else if(searchType == DEF){
            	if(test > BLANK)
            	{
                    moveList.add(new Location(x + (dx), y + (dy)));
            	}
            }
        }

        return moveList;
    }

    //generates a list of moves for a bishop at location "location"
    //attacker is an integer, -1 or 1 -> determines if it is a player's bishop or ai's bishop
    private LinkedList<Location> getBishopMoves(Location location, byte[][] scenario, int searchType)
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
                if (searchType == STNDRD)
                {
                	if(test*attacker <= BLANK)//if scanned tile is blank or enemy
                	{
                        moveList.add(new Location(x + (dx), y + (dy)));
                        if(test*attacker < BLANK) //enemy
                        {
                        	break;
                        }
                	}else{
                		break; //same allegiance
                	}

                }else if(searchType == ATT){
                	
                	if(test < BLANK)
                	{
                        moveList.add(new Location(x + (dx), y + (dy)));
                        break;
                	}else if(test > BLANK)
                	{
                		break;
                	}
                }else if(searchType == DEF){
                	if(test > BLANK)
                	{
                        moveList.add(new Location(x + (dx), y + (dy)));
                        break;
                	}else if(test < BLANK)
                	{
                		break;
                	}
                }
            }

            if(!moveList.isEmpty())
            {
                moveList.addFirst(moveList.removeLast()); //assuming interesting moves at max move length
            }
        }

        return moveList;
    }

    //follows same idea and format as other get_move methods
    private LinkedList<Location>  getRookMoves(Location location, byte[][] scenario, int searchType)
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

                if (searchType == STNDRD)
                {
                	if(test*attacker <= BLANK)//if scanned tile is blank or enemy
                	{
                        moveList.add(new Location(x + (dx), y + (dy)));
                        if(test*attacker < BLANK) //enemy
                        {
                        	break;
                        }
                	}else{
                		break; //same allegiance
                	}

                }else if(searchType == ATT){
                	
                	if(test < BLANK)
                	{
                        moveList.add(new Location(x + (dx), y + (dy)));
                        break;
                	}else if(test > BLANK)
                	{
                		break;
                	}
                }else if(searchType == DEF){
                	if(test > BLANK)
                	{
                        moveList.add(new Location(x + (dx), y + (dy)));
                        break;
                	}else if(test < BLANK)
                	{
                		break;
                	}
                }
            }

            if(!moveList.isEmpty())
            {
                moveList.addFirst(moveList.removeLast()); //assuming interesting moves at max move length
            }
        }

        ////System.out.println("Phyberosis", "getRookMoves returned moveList of size " + moveList.size());
        return moveList;
    }

    //follows same idea and format as other get_move methods
    private LinkedList<Location>  getPawnMoves(Location location, byte[][] scenario, byte[][] hist, int searchType)
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
                if (scenario[x-1][y-attacker]*attacker < BLANK && (searchType < 61)) //tile has enemy && search att or stnd
                {
                    moveList.add(new Location(x - 1, y-attacker, true));
                }else if(scenario[x-1][y-attacker]*attacker > BLANK && searchType == DEF)
                {
                	moveList.add(new Location(x - 1, y-attacker, true));
                }
            }

            //capture right
            if (x + 1 >= 0) //in bounds
            {
                rightInBounds = true;
                if (scenario[x+1][y-attacker]*attacker < 0 && (searchType < 61)) //tile has enemy && search att or stnd
                {
                    moveList.add(new Location(x + 1, y-attacker, true));
                }else if(scenario[x+1][y-attacker]*attacker > BLANK && searchType == DEF)
                {
                	moveList.add(new Location(x - 1, y-attacker, true));
                }
            }


            //if pawn is in location for enPassant -> check enPassant
            /**
             * 1 there is pawn on side
             * 2 there is not respective pawn in starting position on that side
             * 3 there was pawn on starting position
             * 4 there was not pawn on side
             * **/
            if (((location.mY == 3 && attacker == PLAYER) || (location.mY == 3 && attacker == AI))
            		&& searchType < 61)//can't use in def, since pawns can't 2 move capture
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
                        moveList.add(new Location(x + i, y+attacker, true));
                    }
                }
            }
        }

        //////System.out.println("Phyberosis", "getPawnMoves() first returned x value: " + ((Location)moveList.getFirst()).mX);
        return moveList;
    }

    private void printScenariod(byte[][] s)
    {
        System.out.println(TAG + "printScenario: " + ID);
        System.out.println(TAG + "printScenario: ------------------------------------------------");
        for (int asdf = 0; asdf < 8; asdf++)
        {
            String[] a = new String[8];
            for (int i = 0; i < 8; i++)
            {
                byte b = s[i][asdf];
                switch (abs(b))
                {
                    case 0:
                        a[i] = "[]";
                        break;
                    case 2:
                        a[i] = "P";
                        break;
                    case 6:
                        a[i] = "N";
                        break;
                    case 7:
                        a[i] = "B";
                        break;
                    case 10:
                        a[i] = "R";
                        break;
                    case 18:
                        a[i] = "Q";
                        break;
                    case 127:
                        a[i] = "K";
                        break;

                }
                if (b > 0)
                {
                    a[i] = a[i].concat("p");
                } else if (b < 0)
                {
                    a[i] = a[i].concat("a");
                }
            }
            System.out.println(TAG + "Scenario print was called: "
                    + a[0] + " "
                    + a[1] + " "
                    + a[2] + " "
                    + a[3] + " "
                    + a[4] + " "
                    + a[5] + " "
                    + a[6] + " "
                    + a[7] + " ");
        }
        System.out.println(TAG + "printScenario: ------------------------------------------------");

    }

    public void run()
    {

        //long start = System.currentTimeMillis();
        /*synchronized (mgRef)
        {
            //printScenariod(mScenario);
            System.out.println(TAG + "run: " + ID);
        }*/

        int score = alphaBeta(mScenario, mPlayerH, mMyH, mDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        /*synchronized (mgRef)
        {
            //printScenariod(mScenario);
            System.out.println(TAG + "run: " + ID + " has score: " + score);
        }*/
        synchronized (mgRef)
        {
            mgRef.progress += 1;
        }
        synchronized (mgRef)
        {
            mgRef.mScoreData.add(new Location(ID, score));
        }

        //debug
        /*synchronized (mgRef)
        {
            long time = System.currentTimeMillis() - start;

            System.out.println(TAG + "minMax Total-> " + time + "ms for " + ID);
        }*/
    }

    void begin()
    {
        Thread me = new Thread(this);
        me.start();
    }

}
