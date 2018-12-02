package Chess;

import java.util.ArrayList;

public class Node {

	private Node parent;
	private Node[] children = null;
	private Piece piece;	
	private int[] move;
	private int layer, value;
	
	public Node(Node parent, int layer, Piece piece, int[] move, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		this.parent = parent;
		this.layer = layer;
		this.piece = piece;
		this.move = move;
		if(Tile.isOccupiedByOpponent(move[0], move[1], piece.getPlayer(), playerPieces, botPieces))
			(piece.getPlayer() == GameData.player.PLAYER_1 ? botPieces : playerPieces).remove(Tile.getPiece(move[0], move[1], playerPieces, botPieces));
		value = Minimax.getBoardValue(piece, move, playerPieces, botPieces);
		if (layer < Game.ultraBot.movesAhead-1) {
			int totalChildren = 0;
			if (piece.getPlayer() == GameData.player.PLAYER_1) {
				for(Piece pieces : botPieces) 
					totalChildren += pieces.getPossibleMovesAI(playerPieces, botPieces).size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : botPieces)
					for(int[] m : p.getPossibleMovesAI(playerPieces, botPieces)) 
						children[index++] = new Node(this, layer+1, p, m, Minimax.deepClone(playerPieces), Minimax.deepClone(botPieces));
			}else{
				for(Piece pieces : playerPieces) 
					totalChildren += pieces.getPossibleMovesAI(playerPieces, botPieces).size();
				children = new Node[totalChildren];
				int index = 0;
				for(Piece p : playerPieces)
					for(int[] m : p.getPossibleMovesAI(playerPieces, botPieces)) 
						children[index++] = new Node(this, layer+1, p, m, Minimax.deepClone(playerPieces), Minimax.deepClone(botPieces));
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
