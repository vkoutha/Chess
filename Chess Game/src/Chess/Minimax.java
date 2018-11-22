package Chess;

import java.util.ArrayList;

public class Minimax {

	ArrayList<Piece> pieces, opponentPieces, movePieces, moveOpponentPieces;
	GameData.player player;
	
	Piece pieceToMove = null;
	int[] locationToMove = null;
	
	public Minimax(GameData.player player) {
		this.player = player;
		if(player == GameData.player.PLAYER_1) {
			pieces = Game.player1Pieces;
			opponentPieces = Game.player2Pieces;
		}else{
			pieces = Game.player2Pieces;
			opponentPieces = Game.player1Pieces;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void move(int movesAhead) {
	//	ArrayList<int[]> ogPieceLocations = new ArrayList<int[]>(), ogOpponentPieceLocations = new ArrayList<int[]>();
	//	pieces.forEach(n -> {ogPieceLocations.add(new int[] {n.getRow(), n.getColumn()});});
	//	opponentPieces.forEach(n -> {ogOpponentPieceLocations.add(new int[] {n.getRow(), n.getColumn()});});
		
		movePieces = (ArrayList<Piece>) pieces.clone();
		moveOpponentPieces = (ArrayList<Piece>) opponentPieces.clone();
		Piece pieceToMove = null;
		int[] locationToMove = null;
		ArrayList<Node> initialLayer = new ArrayList<Node>();
		pieces.forEach(piece -> {
			piece.getPossibleMovesInCheck().forEach(move -> {initialLayer.add(new Node(piece, move, getBoardValue(piece, move)));});
		});
	//	System.out.println(initialLayer.size());
		for(Node n : initialLayer) {
		//	System.out.println(n.value);
			int minimizedScore = 5000;
			if(n.value < minimizedScore) {
				pieceToMove = n.piece;
				locationToMove = n.move;
				minimizedScore = n.value;
			}
		}
	//	reset(ogPieceLocations, ogOpponentPieceLocations);
		Game.prevTileClicked = new int[] {pieceToMove.getRow(), pieceToMove.getColumn()};
		Game.tileClicked = locationToMove;
		Game.onValidMoveClick();
	}

	//Returns board value for piece after moving to certain location
	public int getBoardValue(Piece piece, int[] moveLocation) {
		int boardValue = 0;
		System.out.println("Before: " + movePieces.size());
		if (Tile.isOccupiedByOpponent(moveLocation[0], moveLocation[1], piece.getPlayer())) 
			moveOpponentPieces.remove(Tile.getPiece(moveLocation[0], moveLocation[1]));
		else 
			movePieces.remove(Tile.getPiece(moveLocation[0], moveLocation[1]));
			
		System.out.println("After: " + movePieces.size());
	//	piece.setLocation(moveLocation);
		for(Piece own : movePieces) 
			boardValue -= own.getPieceValue();
		for(Piece opponent : moveOpponentPieces) 
			boardValue += opponent.getPieceValue();
		return boardValue;
	}
	
	public void reset(ArrayList<int[]> ogPieceLocations, ArrayList<int[]> ogOpponentPieceLocations) {
		pieces.forEach(n -> {n.setLocation(ogPieceLocations.get(pieces.indexOf(n)));});
		opponentPieces.forEach(n -> {n.setLocation(ogOpponentPieceLocations.get(opponentPieces.indexOf(n)));});

	}
	
}
