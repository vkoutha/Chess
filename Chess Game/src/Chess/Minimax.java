package Chess;

import java.util.ArrayList;

public class Minimax {

	ArrayList<Piece> pieces, opponentPieces, botPieces, playerPieces;
	GameData.player player;
	
	Piece pieceToMove = null;
	int[] locationToMove = null;
	int movesAhead;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		if(player == GameData.player.PLAYER_1) {
			pieces = Game.player1Pieces;
			opponentPieces = Game.player2Pieces;
		}else{
			pieces = Game.player2Pieces;
			opponentPieces = Game.player1Pieces;
		}
		this.movesAhead = movesAhead;
	}
	
	@SuppressWarnings("unchecked")
	public void move() {
	//	ArrayList<int[]> ogPieceLocations = new ArrayList<int[]>(), ogOpponentPieceLocations = new ArrayList<int[]>();
	//	pieces.forEach(n -> {ogPieceLocations.add(new int[] {n.getRow(), n.getColumn()});});
	//	opponentPieces.forEach(n -> {ogOpponentPieceLocations.add(new int[] {n.getRow(), n.getColumn()});});
		
		botPieces = (ArrayList<Piece>) pieces.clone();
		playerPieces = (ArrayList<Piece>) opponentPieces.clone();
		Piece pieceToMove = null;
		int[] locationToMove = null;
		ArrayList<Node> initialLayer = new ArrayList<Node>();
		pieces.forEach(piece -> {
			piece.getPossibleMovesInCheck().forEach(move -> {initialLayer.add(new Node(null, 1, piece, move, getBoardValue(piece, move, botPieces, playerPieces), deepClone(botPieces), deepClone(playerPieces)));});
		});

	//	reset(ogPieceLocations, ogOpponentPieceLocations);
		Game.prevTileClicked = new int[] {pieceToMove.getRow(), pieceToMove.getColumn()};
		Game.tileClicked = locationToMove;
		Game.onValidMoveClick();
	}

	//Returns board value for piece after moving to certain location
	public static int getBoardValue(Piece piece, int[] moveLocation, ArrayList<Piece> botPieces, ArrayList<Piece> playerPieces) {
		int boardValue = 0;
		System.out.println("Before: " + botPieces.size());
		if (Tile.isOccupiedByOpponent(moveLocation[0], moveLocation[1], piece.getPlayer())) 
			if (piece.getPlayer() == GameData.player.PLAYER_1) 
				boardValue += Tile.getPiece(moveLocation[0], moveLocation[1]).getPieceValue();
			else 
				boardValue -= Tile.getPiece(moveLocation[0], moveLocation[1]).getPieceValue();

		System.out.println("After: " + botPieces.size());
	//	piece.setLocation(moveLocation);
		for(Piece own : botPieces) 
			boardValue -= own.getPieceValue();
		for(Piece opponent : playerPieces) 
			boardValue += opponent.getPieceValue();
		return boardValue;
	}
	
	public void reset(ArrayList<int[]> ogPieceLocations, ArrayList<int[]> ogOpponentPieceLocations) {
		pieces.forEach(n -> {n.setLocation(ogPieceLocations.get(pieces.indexOf(n)));});
		opponentPieces.forEach(n -> {n.setLocation(ogOpponentPieceLocations.get(opponentPieces.indexOf(n)));});
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
