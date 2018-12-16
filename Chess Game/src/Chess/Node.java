package Chess;

import java.util.ArrayList;

public class Node {

	private Node parent;
	private Node[] children = null;
	private Piece piece;	
	private int[] originalLocation, move;
	private int layer, value;
	
	public Node(Node parent, int layer, Piece piece, int[] originalLocation, int[] move, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		this.parent = parent;
		this.layer = layer;
		this.piece = piece;
		this.move = move;
		this.originalLocation = originalLocation;
		Game.ultraBot.totalNodes++;
		if(Math.abs(value) > Game.ultraBot.bestMoves[Minimax.getIndex(this, Game.ultraBot.initialLayer)][layer-1].getValue()) {
			if(Tile.isOccupiedByOpponent(move[0], move[1], piece.getPlayer(), playerPieces, botPieces))
				if(piece.getPlayer() == GameData.player.PLAYER_1) 
					botPieces.remove(Tile.getPiece(move[0], move[1], playerPieces, botPieces));
				else 
					playerPieces.remove(Tile.getPiece(move[0], move[1], playerPieces, botPieces));
			Tile.getPiece(piece.getRow(), piece.getColumn(), playerPieces, botPieces).setLocation(move);
			piece.setLocation(move);
			value = Minimax.getBoardValue(playerPieces, botPieces);
			if (layer < Game.ultraBot.movesAhead+1) {
				int totalChildren = 0, index = 0;
				for(Piece pieces : (piece.getPlayer() == GameData.player.PLAYER_1 ? botPieces : playerPieces)) 
					totalChildren += pieces.getPossibleMovesAI(playerPieces, botPieces).size();
				children = new Node[totalChildren];
				for(Piece p : (piece.getPlayer() == GameData.player.PLAYER_1 ? botPieces : playerPieces))
					for(int[] m : p.getPossibleMovesAI(playerPieces, botPieces)) 
						children[index++] = new Node(this, layer+1, p.clone(), new int[] {p.getRow(), p.getColumn()}, m, Minimax.deepClone(playerPieces), Minimax.deepClone(botPieces));	
			}else if(layer == Game.ultraBot.movesAhead+1) 
				Game.ultraBot.lastGeneration.add(this);
		}
	}
	
	public Node getParent() {return parent;}
	
	public Piece getPiece() {return piece;}
	
	public int[] getOriginalLocation(){return originalLocation;}
	
	public int[] getMove() {return move;}
	
	public int getValue() {return value;}
	
	public boolean isBestChild() {
		if(this == Minimax.getBestNode(parent.getChildren()))
			return true;
		return false;
	}

	public Node[] getChildren() {return children;}

	public Node getRoot() {
		if(layer == 1)
			return this;
		Node highest = parent;
		while(highest.getParent() != null)
			highest = highest.getParent();
		return highest;
	}

	public String toString() {
		return "\nPiece: " + getPiece().getType() + "\nPlayer: " + piece.getPlayer() + "\nOriginal Location: [" + originalLocation[0] + ", " + originalLocation[1] + "]"
				+ "\nMove Location: [" + move[0] + ", " + move[1] + "]\nValue: " + value;
	}
	
}
