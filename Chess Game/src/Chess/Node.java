package Chess;

import java.util.ArrayList;

public class Node {

	private Node parent;
	private Node[] children = null;
	private Piece piece;	
	private int[] move;
	private int layer, value;
	
	public Node(Node parent, int layer, Piece piece, int[] move, int value, ArrayList<Piece> botPieces, ArrayList<Piece> playerPieces) {
		this.parent = parent;
		this.layer = layer;
		this.piece = piece;
		this.move = move;
		this.value = value;
		if (layer < Game.ultraBot.movesAhead-1) {
			int totalChildren = 0;
			if (piece.getPlayer() == GameData.player.PLAYER_1) {
				for(Piece pieces : botPieces) 
					totalChildren += pieces.getPossibleMovesInCheck().size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : botPieces)
					for(int[] m : p.getPossibleMovesInCheck()) 
						children[index++] = new Node(this, layer+1, p, m, Minimax.getBoardValue(p, m, botPieces, playerPieces), Minimax.deepClone(botPieces), Minimax.deepClone(playerPieces));
			}else{
				for(Piece pieces : playerPieces) 
					totalChildren += pieces.getPossibleMovesInCheck().size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : playerPieces)
					for(int[] m : p.getPossibleMovesInCheck()) 
						children[index++] = new Node(this, layer+1, p, m, Minimax.getBoardValue(p, m, botPieces, playerPieces), Minimax.deepClone(botPieces), Minimax.deepClone(playerPieces));
			}
		}
	}
	
	public Node getParent() {return parent;}
	
	public Piece getPiece() {return piece;}
	
	public int[] getMove() {return move;}
	
	public int getValue() {return value;}

	public Node[] getChildren() {return children;}

	public Node getRoot() {
		if(layer == 1)
			return this;
		Node highest = parent;
		while(highest.getParent() != null)
			highest = highest.getParent();
		return highest;
	}

}
