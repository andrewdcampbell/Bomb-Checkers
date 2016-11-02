public class Piece {
	public boolean isFire, isKing, hasCaptured;
	//private Board b;
	public int x, y;
	public String type;

	public Piece(boolean isFire, Board b, int x, int y, String type) {
		this.isFire = isFire;
		//this.b = b;
		this.x = x;
		this.y = y;
		this.type = type;
		this.isKing = false;
		this.hasCaptured = false;
	}

	public boolean isFire() {
		return this.isFire;
	}

	public int side() {
		if (this.isFire) return 0;
		else return 1;
	}

	public boolean isKing() {
		return this.isKing;
	}

	public boolean isBomb() {
		return this.type == "bomb";
	}

	public boolean isShield() {
		return this.type == "shield";
	}

/*	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean hasCaptured() {
		return this.hasCaptured;
	}

	public void doneCapturing() {
		this.hasCaptured = false;
	}*/
}



