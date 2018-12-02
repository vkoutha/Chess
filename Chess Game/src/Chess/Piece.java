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
	
	boolean isPieceThatChecked();
	
	type getType();
	GameData.player getPlayer();
	
	ArrayList<int[]> getPossibleMoves();
	ArrayList<int[]> getPossibleMovesInCheck();
	ArrayList<int[]> getAllMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces);
	ArrayList<int[]> getPossibleMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces);
	
	
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
	
	static Piece getKing(GameData.player player, ArrayList<Piece> botPieces, ArrayList<Piece> playerPieces) {
		
		switch (player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < playerPieces.size(); z++) 
				if(playerPieces.get(z).getType() == Piece.type.KING)
					return playerPieces.get(z);
			break;
		
		case PLAYER_2:
			
			for(int z = 0; z < botPieces.size(); z++) 
				if(botPieces.get(z).getType() == Piece.type.KING)
					return botPieces.get(z);
			break;
			
		}
		
		return null;
					
	}
	
	static boolean isInCheck(GameData.player player) {
		
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 
					for(int z1 = 0; z1 < Game.player2Pieces.get(z).getPossibleMoves().size(); z1++) 
						if(Game.player2Pieces.get(z).getPossibleMoves().get(z1)[0] == getKing(player).getRow() && Game.player2Pieces.get(z).getPossibleMoves().get(z1)[1] == getKing(player).getColumn()) 
							return true;
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
					for(int z1 = 0; z1 < Game.player1Pieces.get(z).getPossibleMoves().size(); z1++)                          
						if(Game.player1Pieces.get(z).getPossibleMoves().get(z1)[0] == getKing(player).getRow() && Game.player1Pieces.get(z).getPossibleMoves().get(z1)[1] == getKing(player).getColumn()) 
							return true;
			break;

		}
		
		return false;
	}
	
	static boolean isInCheck(GameData.player player, int row, int column) {
		
		Piece king = Piece.getKing(player);
		int[] ogLocation = {king.getRow(), king.getColumn()};
		Piece.getKing(player).setLocation(new int[] {row, column});
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 
				for(int z1 = 0; z1 < Game.player2Pieces.get(z).getPossibleMoves().size(); z1++) 
					if(Game.player2Pieces.get(z).getPossibleMoves().get(z1)[0] == row && Game.player2Pieces.get(z).getPossibleMoves().get(z1)[1] == column) { 
						Piece.getKing(player).setLocation(ogLocation);
						return true;
					}
						
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
				for(int z1 = 0; z1 < Game.player1Pieces.get(z).getPossibleMoves().size(); z1++)                          
						if(Game.player1Pieces.get(z).getPossibleMoves().get(z1)[0] == row && Game.player1Pieces.get(z).getPossibleMoves().get(z1)[1] == column) { 
							Piece.getKing(player).setLocation(ogLocation);
							return true;
						}
				
			break;

		}
		
		Piece.getKing(player).setLocation(ogLocation);
		return false;
	}
	
	static boolean isInCheck(GameData.player player, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < botPieces.size(); z++) 
					for(int z1 = 0; z1 < botPieces.get(z).getAllMovesAI(playerPieces, botPieces).size(); z1++) 
						if(botPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[0] == getKing(player, playerPieces, botPieces).getRow() && botPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[1] == getKing(player, playerPieces, botPieces).getColumn()) 
							return true;
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < playerPieces.size(); z++) 
					for(int z1 = 0; z1 < playerPieces.get(z).getAllMovesAI(playerPieces, botPieces).size(); z1++)                          
						if(playerPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[0] == getKing(player, playerPieces, botPieces).getRow() && playerPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[1] == getKing(player, playerPieces, botPieces).getColumn()) 
							return true;
			break;

		}
		
		return false;
	}
	
	static boolean isInCheck(GameData.player player, int row, int column, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		
		Piece king = Piece.getKing(player, playerPieces, botPieces);
		int[] ogLocation = {king.getRow(), king.getColumn()};
		king.setLocation(new int[] {row, column});
		
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < botPieces.size(); z++) 
					for(int z1 = 0; z1 < botPieces.get(z).getAllMovesAI(playerPieces, botPieces).size(); z1++) 
						if(botPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[0] == row && botPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[1] == column) { 
							king.setLocation(ogLocation);
							return true;
						}
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < playerPieces.size(); z++) 
					for(int z1 = 0; z1 < playerPieces.get(z).getAllMovesAI(playerPieces, botPieces).size(); z1++)                          
						if(playerPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[0] == row && playerPieces.get(z).getAllMovesAI(playerPieces, botPieces).get(z1)[1] == column) {
							king.setLocation(ogLocation);
							return true;
						}
		
			break;

		}
		
		king.setLocation(ogLocation);
		return false;
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
	
	static int getTotalPieceValue(GameData.player player, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		
		int totalPieceValue = 0;
		
		switch (player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < playerPieces.size(); z++)
				totalPieceValue += playerPieces.get(z).getPieceValue();
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < botPieces.size(); z++)
				totalPieceValue += botPieces.get(z).getPieceValue();
			break;

		}
	
		return totalPieceValue;
		
	}
	
}
