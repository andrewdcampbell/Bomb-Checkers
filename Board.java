public class Board {
    /** 8x8 array of Piece instances */
    private static Piece[][] pieces = new Piece[8][8];
    private Piece selected;
    private int player = 0;
    private boolean moved = false;
    private boolean captured = false;
    private int fireleft = 12, waterleft = 12; 

    public Board(boolean shouldBeEmpty) {
        /* Sets initial configuration in pieces */
        if (!shouldBeEmpty) {
            for (int i=0; i<7; i+=2) {
                this.pieces[i][0] = new Piece(true, this, i, 0, "pawn");
            }
            for (int i=0; i<7; i+=2) {
                this.pieces[i][2] = new Piece(true, this, i, 2, "bomb");
            }
            for (int i=1; i<=7; i+=2) {
                this.pieces[i][1] = new Piece(true, this, i, 1, "shield");
            }
            for (int i=1; i<=7; i+=2) {
                this.pieces[i][5] = new Piece(false, this, i, 5, "bomb");
            }
            for (int i=1; i<=7; i+=2) {
                this.pieces[i][7] = new Piece(false, this, i, 7, "pawn");
            }
            for (int i=0; i<7; i+=2) {
                this.pieces[i][6] = new Piece(false, this, i, 6, "shield");
            }
        }
    }
    /* Draws an NxN Board with pictures as given by the pieces array*/
    private static void drawBoard(int N, Board b) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (b.selected != null && b.pieces[i][j] == b.selected) StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
                else if ((i + j) % 2 == 0) StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
                else                       StdDrawPlus.setPenColor(StdDrawPlus.BLUE);
                StdDrawPlus.filledSquare(i + .5, j + .5, .5);
                // Selects correct image to draw on top of square
                if (b.pieces[i][j] != null) { 
                    String img = "img/" + b.pieces[i][j].type;
                    if (b.pieces[i][j].isFire()) {
                        if (b.pieces[i][j].isKing()) {
                            img += "-fire-crowned.png"; 
                        }
                        else {
                            img += "-fire.png";
                        }
                    }
                    else {
                        if (b.pieces[i][j].isKing()) {
                            img += "-water-crowned.png"; 
                        }
                        else {
                            img += "-water.png";
                        }
                    }
                    StdDrawPlus.picture(i + .5, j + .5, img, 1, 1);
                }   
            }
        }
    }

    public static void main(String[] args) {
        StdDrawPlus.setXscale(0, 8);
        StdDrawPlus.setYscale(0, 8);

        Board b = new Board(false);

        /** Monitors for mouse presses and spacebar presses */
        while(true) {
            drawBoard(8, b);
            if (StdDrawPlus.mousePressed()) {
                double x = StdDrawPlus.mouseX();
                double y = StdDrawPlus.mouseY();
                if (b.canSelect((int) x, (int) y)) {
                    b.select((int) x, (int) y);
                }
            }
            else if (StdDrawPlus.isSpacePressed()) {
                if (b.canEndTurn()) {
                    b.endTurn();
                }
            }
            if (b.fireleft == 0 || b.waterleft == 0) {
                System.out.println(b.winner() + " wins!");
                drawBoard(8, b);
                return;
            }            
            StdDrawPlus.show(30);
        } 
    }

    public Piece pieceAt(int x, int y) {
        if (x > 7 || y > 7 || x < 0 || y < 0 || pieces[x][y] == null) {
            return null;
        }
        return pieces[x][y];
    }

    public boolean canSelect(int x, int y) {
        if (pieceAt(x,y) != null) {
            if (pieceAt(x,y).side() == player && moved == false)
                return true;
        }
        else {
            if (selected != null && moved == false && validMove(selected.x, selected.y, x, y)) {
                return true;
            }
            else if (selected != null && captured == true && validMove(selected.x, selected.y, x, y)) {
                return true;
            }
        } 
        return false;
    }

    private boolean validMove(int xi, int yi, int xf, int yf) { 
        if (pieceAt(xi, yi).isKing()) {
            if (pieceAt(xf, yf) == null && Math.abs(xf-xi) == 1 && Math.abs(yf-yi) == 1 && !captured)
                return true;
            else if (pieceAt(xf, yf) == null && Math.abs(xf-xi) == 2 && Math.abs(yf-yi) == 2 && pieceAt((xi+xf)/2, (yi+yf)/2) != null && pieceAt((xi+xf)/2, (yi+yf)/2).side() != pieceAt(xi, yi).side())
                return true;
            else return false; 
        }
        else {
            if (pieceAt(xf, yf) == null && ((Math.abs(xf-xi) == 1 && yf-yi == 1 && pieceAt(xi, yi).side() == 0) || (Math.abs(xf-xi) == 1 && yf-yi == -1 && pieceAt(xi, yi).side() == 1)) && !captured)
                return true;
            else if (pieceAt(xf, yf) == null && Math.abs(xf-xi) == 2 && ((yf-yi == 2 && pieceAt(xi, yi).side() == 0) || (yf-yi == -2 && pieceAt(xi, yi).side() == 1)) && pieceAt((xi+xf)/2, (yi+yf)/2) != null && pieceAt((xi+xf)/2, (yi+yf)/2).side() != pieceAt(xi, yi).side())
                return true;
            else return false;  
        }
    } 

    public void select(int x, int y) {
        if (pieceAt(x, y) != null) {
            selected = pieceAt(x, y);
        }
        else {
            place(selected, x, y);
        }
    }

    public void place(Piece p, int x, int y) { // Need to implement: If (x, y) is out of bounds or if p is null, does nothing
        if (Math.abs(p.x - x) == 2) { // Indicates a capture
            if (p.isBomb()) {
                if (pieceAt((p.x+x)/2, (p.y+y)/2).side() == 0) fireleft -= 1;
                else waterleft -= 1;
                remove((p.x+x)/2, (p.y+y)/2);
                pieces[x][y] = p;
                moved = true;
                for (int yp=y-1; yp<y+2; yp++) {
                    for (int xp=x-1; xp<x+2; xp++) {
                        if (pieceAt(xp, yp) != null && !pieceAt(xp, yp).isShield()) {
                            if (pieceAt(xp, yp).side() == 0) fireleft -= 1;
                            else if (pieceAt(xp, yp).side() == 1) waterleft -= 1;
                            remove(xp,yp);
                            captured = true;
                        }
                    }
                }
                remove(p.x, p.y);
                selected = null;
                return;
            }
            else {
                if (pieceAt((p.x+x)/2, (p.y+y)/2).side() == 0) fireleft -= 1;
                else waterleft -= 1;
                remove((p.x+x)/2, (p.y+y)/2);
                captured = true;
            }
        }
        if (p.side() == 0 && y == 7) {
            p.isKing = true;
        }
        else if (p.side() == 1 && y == 0) {
            p.isKing = true;
        }
        remove(p.x, p.y);
        p.x = x;
        p.y = y;
        pieces[x][y] = p;
        moved = true;
    }

    public Piece remove(int x, int y) {
        Piece removed = pieceAt(x,y);
        pieces[x][y] = null;
        return removed;
    }

    public boolean canEndTurn() {
        if (moved) return true;
        else return false;
    }

    public void endTurn() {
        player = 1 - player;
        moved = false;
        captured = false;
        selected = null;
    }

    public String winner() {
        if (waterleft != 0 && fireleft != 0) return null;
        else if (waterleft == 0 && fireleft == 0) return "No one";
        else if (fireleft == 0) return "Water";
        else return "Fire";
    }
}