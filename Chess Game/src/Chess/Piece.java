package Chess;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

public interface Piece extends Serializable{
	
	int getColumn();
	int getRow();
	int getPieceValue();
	int getMoveCount();
	int getMoveCountAtFirstMove();
	
	boolean isInCheck();
	boolean isPieceThatChecked();
	
	type getType();
	GameData.player getPlayer();
	
	ArrayList<int[]> getPossibleMoves();
	ArrayList<int[]> getPossibleMovesInCheck();
	
	void increaseMoveCount();
	void showValidMoves();
	void move(int row, int column);
	void setLocation(int[] location);
	void delete();
	void render(Graphics g);
	
	Piece clone();
	
	enum type{
		
		PAWN,
		ROOK,
		KNIGHT,
		BISHOP,
		QUEEN,
		KING;
		
	}
	
	static Piece getKing(GameData.player player) {
		
		switch (player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
				if(Game.player1Pieces.get(z).getType() == Piece.type.KING)
					return Game.player1Pieces.get(z);
			break;
		
		case PLAYER_2:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 
				if(Game.player2Pieces.get(z).getType() == Piece.type.KING)
					return Game.player2Pieces.get(z);
			break;
			
		}
		
		return null;
					
	}
	
	static int getTotalPieceValue(GameData.player player) {
		
		int totalPieceValue = 0;
		
		switch (player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++)
				totalPieceValue += Game.player1Pieces.get(z).getPieceValue();
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++)
				totalPieceValue += Game.player2Pieces.get(z).getPieceValue();
			break;

		}
	
		return totalPieceValue;
		
	}
	
}
