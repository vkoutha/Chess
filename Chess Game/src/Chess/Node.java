package Chess;

public class Node {

	private Node parent;
	private Node[] children = null;
	private Piece piece;	
	private int[] move;
	private int layer, value;
	
	public Node(Node parent, int layer, Piece piece, int[] move, int value) {
		this.parent = parent;
		this.layer = layer;
		this.piece = piece;
		this.move = move;
		this.value = value;
		if (layer < Game.ultraBot.movesAhead-1) {
			int totalChildren = 0;
			if (piece.getPlayer() == GameData.player.PLAYER_1) {
				for(Piece pieces : Game.ultraBot.botPieces) 
					totalChildren += pieces.getPossibleMovesInCheck().size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : Game.ultraBot.botPieces)
					for(int[] m : p.getPossibleMovesInCheck()) 
						children[index++] = new Node(this, layer+1, p, m, Game.ultraBot.getBoardValue(p, m));
			}else{
				for(Piece pieces : Game.ultraBot.playerPieces) 
					totalChildren += pieces.getPossibleMovesInCheck().size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : Game.ultraBot.playerPieces)
					for(int[] m : p.getPossibleMovesInCheck()) 
						children[index++] = new Node(this, layer+1, p, m, Game.ultraBot.getBoardValue(p, m));
			}
		}
	}
	
	public Node getParent() {return parent;}
	
	public Piece getPiece() {return piece;}
	
	public int[] getMove() {return move;}
	
	public int getValue() {return value;}
	
	public Node getRoot() {
		if(layer == 1)
			return this;
		Node highest = parent;
		while(highest.getParent() != null)
			highest = highest.getParent();
		return highest;
	}

	public Node[] getChildren() {return children;}

}
