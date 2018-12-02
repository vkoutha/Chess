package Chess;

import java.util.ArrayList;

public class Minimax {

	ArrayList<Piece> playerPieces, botPieces;
	GameData.player player;
	
	Piece pieceToMove = null;
	int[] locationToMove = null;
	int movesAhead;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		if(player == GameData.player.PLAYER_1) {
			playerPieces = deepClone(Game.player1Pieces);
			botPieces = deepClone(Game.player2Pieces);
		}else{
			playerPieces = deepClone(Game.player2Pieces);
			botPieces = deepClone(Game.player1Pieces);
		}
		this.movesAhead = movesAhead;
	}
	
	@SuppressWarnings("unchecked")
	public void move() {
		Piece pieceToMove = null;
		int[] locationToMove = null;
		ArrayList<Node> initialLayer = new ArrayList<Node>();
		botPieces.forEach(piece -> {
			piece.getPossibleMovesInCheck().forEach(move -> {initialLayer.add(new Node(null, 1, piece, move, deepClone(playerPieces), deepClone(botPieces)));});
		});
	}

	//Returns board value for piece after moving to certain location
	public static int getBoardValue(Piece piece, int[] moveLocation, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		int boardValue = 0;
		System.out.println("Before: " + botPieces.size());
		if (Tile.isOccupiedByOpponent(moveLocation[0], moveLocation[1], piece.getPlayer(), playerPieces, botPieces)) 
			if (piece.getPlayer() == GameData.player.PLAYER_1) 
				boardValue += Tile.getPiece(moveLocation[0], moveLocation[1], playerPieces, botPieces).getPieceValue();
			else 
				boardValue -= Tile.getPiece(moveLocation[0], moveLocation[1], playerPieces, botPieces).getPieceValue();

		System.out.println("After: " + botPieces.size());
	//	piece.setLocation(moveLocation);
		for(Piece own : botPieces) 
			boardValue -= own.getPieceValue();
		for(Piece opponent : playerPieces) 
			boardValue += opponent.getPieceValue();
		return boardValue;
	}
	
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
