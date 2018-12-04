package Chess;

import java.io.IOException;
import java.util.ArrayList;

public class Minimax {

	ArrayList<Piece> playerPieces, botPieces;
	ArrayList<Node> lastGeneration;
	GameData.player player;
	int movesAhead;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		this.movesAhead = movesAhead;
		lastGeneration = new ArrayList<Node>();
		if(player == GameData.player.PLAYER_1) {
			playerPieces = (Game.player2Pieces);
			botPieces = (Game.player1Pieces);
		}else{
			playerPieces = (Game.player1Pieces);
			botPieces = (Game.player2Pieces);
		}
	}
	
	public void move() {
		
		lastGeneration.clear();
		ArrayList<Node> initialLayer = new ArrayList<Node>();	
		for(Piece piece : botPieces)
			if(piece.getPossibleMovesAI(Game.player1Pieces, Game.player2Pieces).size()>0)
				for(int[] move : piece.getPossibleMovesAI(Game.player1Pieces, Game.player2Pieces))
					initialLayer.add(new Node(null, 1, piece.clone(), new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));
		Node bestNode = null;
		bestNode = getBestNode(lastGeneration);
		Game.prevTileClicked = bestNode.getRoot().getOriginalLocation();
		Game.tileClicked = bestNode.getRoot().getMove();
		Game.onValidMoveClick();
	}

	//Returns board value for piece after moving to certain location
	public static int getBoardValue(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		int boardValue = 0;
		for(Piece own : botPieces) 
			boardValue -= own.getPieceValue();
		for(Piece opponent : playerPieces) 
			boardValue += opponent.getPieceValue();
		return boardValue;
	}
	
	public static Node getBestNode(Node[] children) {
		Node bestNode = null;
		int highestVal = -1000000, lowestVal = 1000000;
		for(Node node : children) {
			switch(node.getPiece().getPlayer()) {
			case PLAYER_1:
				if(node.getValue() > highestVal) {
					bestNode = node;
					highestVal = bestNode.getValue();
				}
				break;
			case PLAYER_2:
				if(node.getValue() < lowestVal) {
					bestNode = node;
					lowestVal = bestNode.getValue();
				}
				break;
			}
		}
		return bestNode;
	}
	
	public static Node getBestNode(ArrayList<Node> children) {
		Node bestNode = null;
		int highestVal = -1000000, lowestVal = 1000000;
		for(Node node : children) {
			switch(node.getPiece().getPlayer()) {
			case PLAYER_1:
				if(node.getValue() < lowestVal) {
					bestNode = node;
					lowestVal = bestNode.getValue();
				}
				break;
			case PLAYER_2:
				if(node.getValue() > highestVal) {
					bestNode = node;
					highestVal = bestNode.getValue();
				}
				break;
			}
		}
		return bestNode;
	}

	
	@SuppressWarnings("unchecked")
	public static ArrayList<Piece> clone(ArrayList<Piece> arrList){
		return (ArrayList<Piece>) arrList.clone();
	}
	
	public static ArrayList<Piece> deepClone(ArrayList<Piece> arrList){
		ArrayList<Piece> arrListCopy = new ArrayList<Piece>();
		for(Piece piece : arrList)
			arrListCopy.add(piece.clone());
		return arrListCopy;
	}
	
}
