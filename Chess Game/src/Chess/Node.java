package Chess;

public class Node {

	Node[] children;
	Piece piece;	
	int[] move;
	int value;
	
	public Node(Piece piece, int[] move, int value) {
		this.piece = piece;
		this.move = move;
		children = new Node[piece.getPossibleMovesInCheck().size()];
	}
	
}
