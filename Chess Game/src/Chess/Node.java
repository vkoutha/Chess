package Chess;

public class Node {

	private Node[] children = null;
	private Piece piece;	
	private int[] move;
	private int layer, value;
	private boolean isRoot;
	
	public Node(boolean isRoot, int layer, Piece piece, int[] move, int value) {
		this.isRoot = isRoot;
		this.layer = layer;
		this.piece = piece;
		this.move = move;
		this.value = value;
		if (layer < Game.ultraBot.movesAhead) {
			int totalChildren = 0;
			if (piece.getPlayer() == GameData.player.PLAYER_1) {
				for(Piece pieces : Game.ultraBot.botPieces) 
					totalChildren += pieces.getPossibleMovesInCheck().size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : Game.ultraBot.botPieces)
					for(int[] m : p.getPossibleMovesInCheck()) 
						children[index++] = new Node(false, layer+1, p, m, Game.ultraBot.getBoardValue(p, m));
			}else{
				for(Piece pieces : Game.ultraBot.playerPieces) 
					totalChildren += pieces.getPossibleMovesInCheck().size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : Game.ultraBot.playerPieces)
					for(int[] m : p.getPossibleMovesInCheck()) 
						children[index++] = new Node(false, layer+1, p, m, Game.ultraBot.getBoardValue(p, m));
			}
		}
	}
	
	public boolean isRoot() {return isRoot;}
	
	public Piece getPiece() {return piece;}
	
	public int[] getMove() {return move;}
	
	public int getValue() {return value;}
	
	public Node getRoot() {
		if(layer == 1)
			return this;
		return null;
	}

	public Node[] getChildren() {return children;}
	
	public Node[] getPreviousGeneration() {
		if(layer == 1)
			return null;
		return null;
	}

	public Node[] getLastGeneration() {
		//Max layer always 1 less than movesAhead
		if(layer == Game.ultraBot.movesAhead)
			return children;
		
		return null;
	}
	
}
