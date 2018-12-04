package Chess;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import Chess.Piece.type;

public class Pawn implements Piece{

	private int column, row, tileCenterX, tileCenterY, moveCount = 0, moveCountAtDoubleMove = 0;
	boolean isEnPassantable = true, enPassantLeft = false, enPassantRight = false;
	private Color color;
	private GameData.player player;
	private type type;
	
	public Pawn(int row, int column, GameData.player player){

		this.row = row;
		this.column = column;
		
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		this.player = player;
		
		if(player == GameData.player.PLAYER_1)
			color = GameData.PLAYER_1_PIECE_COLOR;
		else if (player == GameData.player.PLAYER_2)
			color = GameData.PLAYER_2_PIECE_COLOR;
		
		type = Piece.type.PAWN;
		
	}
	
	@Override
	public int getRow() {return row;}
	
	@Override
	public int getColumn() {return column;}
	
	@Override
	public int getPieceValue() {return GameData.PAWN_VALUE;}
	
	@Override
	public int getMoveCount() {return moveCount;}
	
	@Override
	public int getMoveCountAtFirstMove() {return moveCountAtDoubleMove;}
	
	@Override
	public void increaseMoveCount() {moveCount++;}

	@Override
	public type getType() {return type;}
	
	@Override
	public GameData.player getPlayer() {return player;}
	
	@Override
	public boolean isPieceThatChecked() {
		// TODO Auto-generated method stub
		
		ArrayList<int[]> possibleMoves = getPossibleMoves();
		
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 
				if(Game.player2Pieces.get(z).getType() == Piece.type.KING) 
					for(int z1 = 0; z1 < possibleMoves.size(); z1++) 
						if(possibleMoves.get(z1)[0] == Game.player2Pieces.get(z).getRow() && possibleMoves.get(z1)[1] == Game.player2Pieces.get(z).getColumn()) 
							return true;
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
				if(Game.player1Pieces.get(z).getType() == Piece.type.KING) 
					for(int z1 = 0; z1 < possibleMoves.size(); z1++) 
						if(possibleMoves.get(z1)[0] == Game.player1Pieces.get(z).getRow() && possibleMoves.get(z1)[1] == Game.player1Pieces.get(z).getColumn()) 
							return true;
			break;
		
		}
		
		return false;
		
	}

	private ArrayList<int[]> getPossibleMovesWithoutCapture(){
		
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		
		if(Tile.isOccupiedByOpponent(row, column, player))
			return possibleMoves;
			
			switch(player) {
			
			case PLAYER_1:
				
				//Allow Tile to move 1 up if it is not occupied
				if(row-1 >= 0 && !Tile.isOccupied(row-1, column))
					possibleMoves.add(new int[] {row-1, column});
				
				//Allow pawn to move up two spaces if its first move and is not occupied
				if(row == 6 && !Tile.isOccupied(row-1, column) && !Tile.isOccupied(row-2, column))
					possibleMoves.add(new int[] {row-2, column});
				
				//Allows En Passant diagonal left capture
				if(row == 3 && column > 0 && Tile.isOccupied(3, column-1) && Tile.getPiece(3, column-1).getType() == Piece.type.PAWN && Tile.getPiece(3, column-1).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column-1).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {2, column-1});
					enPassantLeft = true;
				}else
					enPassantLeft = false;
				
				//Allows En Passant diagonal right capture
				if(row == 3 && column < GameData.COLUMNS-1 && Tile.isOccupied(3, column+1) && Tile.getPiece(3, column+1).getType() == Piece.type.PAWN && Tile.getPiece(3, column+1).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column+1).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {2, column+1});
					enPassantRight = true;
				}else
					enPassantRight = false;
				
				break;
				
			case PLAYER_2:
				
				//Allow Tile to move 1 up if it is not occupied
				if(row+1 <= GameData.ROWS && !Tile.isOccupied(row+1, column))
					possibleMoves.add(new int[] {row+1, column});
				
				//Allow pawn to move up two spaces if its first move and is not occupied
				if(row == 1 && !Tile.isOccupied(row+1, column) && !Tile.isOccupied(row+2, column))
					possibleMoves.add(new int[] {row+2, column});
				
				//Allows En Passant diagonal left capture
				if(row == 4 && column > 0 && Tile.isOccupied(4, column-1) && Tile.getPiece(4, column-1).getType() == Piece.type.PAWN && Tile.getPiece(4, column-1).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column-1).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {5, column-1});
					enPassantLeft = true;
				}else
					enPassantLeft = false;
				
				
				//Allows En Passant diagonal right capture
				if(row == 4 && column < GameData.COLUMNS-1 && Tile.isOccupied(4, column+1) && Tile.getPiece(4, column+1).getType() == Piece.type.PAWN && Tile.getPiece(4, column+1).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column+1).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {5, column+1});
					enPassantRight = true;
				}else
					enPassantRight = false;

				break;
				
			}
			
			if(Piece.isInCheck(player)) {
				
				int ogRow = row, ogColumn = column;
				for(int z = 0; z < possibleMoves.size(); z++) {
					
					row = possibleMoves.get(z)[0];
					column = possibleMoves.get(z)[1];
					
					if(z >= 0 && Piece.isInCheck(player)) {
						possibleMoves.remove(z);
						z--;
					}
				}
				
				row = ogRow;
				column = ogColumn;
				
			}
			
		return possibleMoves;
		
	}
	
	@Override
	public ArrayList<int[]> getPossibleMoves(){
		// TODO Auto-generated method stub
		
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
			
		if(Tile.isOccupiedByOpponent(row, column, player)) 
			return possibleMoves;
			
			switch(player) {
			
			case PLAYER_1:
				
				//Allow pawn to capture/move to diagonal right if possessed by opponent
				if(row-1 >= 0 && column+1 < GameData.COLUMNS && Tile.isOccupiedByOpponent(row-1, column+1, player))
					possibleMoves.add(new int[] {row-1, column+1});
	
				//Allow pawn to capture/move to diagonal left if possessed by opponent	
				if(row-1 >= 0 && column-1 >= 0 && Tile.isOccupiedByOpponent(row-1, column-1, player)) 
					possibleMoves.add(new int[] {row-1, column-1});
					
				break;
				
			case PLAYER_2:
				
				//Allow pawn to capture/move to diagonal right if possessed by opponent
				if(row+1 < GameData.ROWS && column+1 < GameData.COLUMNS && Tile.isOccupiedByOpponent(row+1, column+1, player)) 
					possibleMoves.add(new int[] {row+1, column+1});
				
				//Allow pawn to capture/move to diagonal left if possessed by opponent
				if(row+1 < GameData.ROWS && column-1 >= 0 && Tile.isOccupiedByOpponent(row+1, column-1, player)) 
					possibleMoves.add(new int[] {row+1, column-1});
				
				break;
			
			}
		
		return possibleMoves;
		
	}
	
	public ArrayList<int[]> getPossibleMovesInCheck(){
		
		ArrayList<int[]> possibleMoves = getPossibleMoves();
		possibleMoves.removeAll(getPossibleMovesWithoutCapture());
		possibleMoves.addAll(getPossibleMovesWithoutCapture());
		
		int ogRow = row, ogColumn = column;
		
		for(int z = 0; z < possibleMoves.size(); z++) {
			row = possibleMoves.get(z)[0];
			column = possibleMoves.get(z)[1];
			if(z >= 0 && Piece.isInCheck(player)) {
				possibleMoves.remove(z);
				z--;
			}
		}
		
		row = ogRow;
		column = ogColumn;

		return possibleMoves;
		
	}
	
	public ArrayList<int[]> getNormalMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces){
		
	ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		
		if(Tile.isOccupiedByOpponent(row, column, player, playerPieces, botPieces))
			return possibleMoves;
			
			switch(player) {
			
			case PLAYER_1:
				
				//Allow Tile to move 1 up if it is not occupied
				if(row-1 >= 0 && !Tile.isOccupied(row-1, column, playerPieces, botPieces))
					possibleMoves.add(new int[] {row-1, column});
				
				//Allow pawn to move up two spaces if its first move and is not occupied
				if(row == 6 && !Tile.isOccupied(row-1, column, playerPieces, botPieces) && !Tile.isOccupied(row-2, column, playerPieces, botPieces))
					possibleMoves.add(new int[] {row-2, column});
				
				//Allows En Passant diagonal left capture
				if(row == 3 && column > 0 && Tile.isOccupied(3, column-1, playerPieces, botPieces) && Tile.getPiece(3, column-1, playerPieces, botPieces).getType() == Piece.type.PAWN && Tile.getPiece(3, column-1, playerPieces, botPieces).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column-1, playerPieces, botPieces).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {2, column-1});
					enPassantLeft = true;
				}else
					enPassantLeft = false;
				
				//Allows En Passant diagonal right capture
				if(row == 3 && column < GameData.COLUMNS-1 && Tile.isOccupied(3, column+1, playerPieces, botPieces) && Tile.getPiece(3, column+1, playerPieces, botPieces).getType() == Piece.type.PAWN && Tile.getPiece(3, column+1, playerPieces, botPieces).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column+1, playerPieces, botPieces).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {2, column+1});
					enPassantRight = true;
				}else
					enPassantRight = false;
				
				
				break;
			case PLAYER_2:
				
				//Allow Tile to move 1 up if it is not occupied
				if(row+1 <= GameData.ROWS && !Tile.isOccupied(row+1, column, playerPieces, botPieces))
					possibleMoves.add(new int[] {row+1, column});
				
				//Allow pawn to move up two spaces if its first move and is not occupied
				if(row == 1 && !Tile.isOccupied(row+1, column, playerPieces, botPieces) && !Tile.isOccupied(row+2, column, playerPieces, botPieces))
					possibleMoves.add(new int[] {row+2, column});
				
				//Allows En Passant diagonal left capture
				if(row == 4 && column > 0 && Tile.isOccupied(4, column-1, playerPieces, botPieces) && Tile.getPiece(4, column-1, playerPieces, botPieces).getType() == Piece.type.PAWN && Tile.getPiece(4, column-1, playerPieces, botPieces).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column-1, playerPieces, botPieces).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {5, column-1});
					enPassantLeft = true;
				}else
					enPassantLeft = false;
				
				
				//Allows En Passant diagonal right capture
				if(row == 4 && column < GameData.COLUMNS-1 && Tile.isOccupied(4, column+1, playerPieces, botPieces) && Tile.getPiece(4, column+1, playerPieces, botPieces).getType() == Piece.type.PAWN && Tile.getPiece(4, column+1, playerPieces, botPieces).getMoveCount() == 1 && Game.moveCount == Tile.getPiece(row, column+1, playerPieces, botPieces).getMoveCountAtFirstMove()+1) {
					possibleMoves.add(new int[] {5, column+1});
					enPassantRight = true;
				}else
					enPassantRight = false;
				
				break;
			}
			
			if(Piece.isInCheck(player, playerPieces, botPieces)) {
				int ogRow = row, ogColumn = column;
				for(int z = 0; z < possibleMoves.size(); z++) {
					setLocation(possibleMoves.get(z));
					if(z >= 0 && Piece.isInCheck(player, playerPieces, botPieces)) {
						possibleMoves.remove(z);
						z--;
					}
				}
				row = ogRow;
				column = ogColumn;
			}
			
			return possibleMoves;	
		
	}
	
	public ArrayList<int[]> getAllMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces){
		
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		
		if(Tile.isOccupiedByOpponent(row, column, player, playerPieces, botPieces))
			return possibleMoves;
			
			switch(player) {
			
			case PLAYER_1:
				
				//Allow pawn to capture/move to diagonal right if possessed by opponent
				if(row-1 >= 0 && column+1 < GameData.COLUMNS && Tile.isOccupiedByOpponent(row-1, column+1, player, playerPieces, botPieces))
					possibleMoves.add(new int[] {row-1, column+1});
	
				//Allow pawn to capture/move to diagonal left if possessed by opponent	
				if(row-1 >= 0 && column-1 >= 0 && Tile.isOccupiedByOpponent(row-1, column-1, player, playerPieces, botPieces)) 
					possibleMoves.add(new int[] {row-1, column-1});
				
				break;
				
			case PLAYER_2:
			
				//Allow pawn to capture/move to diagonal right if possessed by opponent
				if(row+1 < GameData.ROWS && column+1 < GameData.COLUMNS && Tile.isOccupiedByOpponent(row+1, column+1, player, playerPieces, botPieces)) 
					possibleMoves.add(new int[] {row+1, column+1});
				
				//Allow pawn to capture/move to diagonal left if possessed by opponent
				if(row+1 < GameData.ROWS && column-1 >= 0 && Tile.isOccupiedByOpponent(row+1, column-1, player, playerPieces, botPieces)) 
					possibleMoves.add(new int[] {row+1, column-1});
				
				break;
				
			}
			
			return possibleMoves;
	}
	
	public ArrayList<int[]> getPossibleMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces){
		
		ArrayList<int[]> possibleMoves = getAllMovesAI(playerPieces, botPieces);
		possibleMoves.addAll(getNormalMovesAI(playerPieces, botPieces));
		
		int ogRow = row, ogColumn = column;
		
		for(int z = 0; z < possibleMoves.size(); z++) {
			
			setLocation(possibleMoves.get(z));
			
			if(z >= 0 && Piece.isInCheck(player, playerPieces, botPieces)) {
				possibleMoves.remove(z);
				z--;
			}
		}
		
		row = ogRow;
		column = ogColumn;

		return possibleMoves;
		
	}
	
	
	@Override
	public void showValidMoves() {
		// TODO Auto-generated method stub
		
		ArrayList<int[]> possibleMoves = getPossibleMovesInCheck();

		for(int rowNumber = 0; rowNumber < GameData.ROWS; rowNumber++) 
			for(int columnNumber = 0; columnNumber < GameData.COLUMNS; columnNumber++) 
				for(int z = 0; z < possibleMoves.size(); z++) 
					if(possibleMoves.get(z)[0] == Game.board[rowNumber][columnNumber].row && possibleMoves.get(z)[1] == Game.board[rowNumber][columnNumber].column)
						Game.board[rowNumber][columnNumber].setAsValidMove(true);
		
		
	}

	public void move(int row, int column) {
		
		if(enPassantLeft) {	
			
			switch(player) {
			
			case PLAYER_1:
				
				if(Game.tileClicked[0] == 2 && Game.tileClicked[1] == this.column-1)
					Tile.getPiece(this.row, this.column-1).delete();
				break;
				
			case PLAYER_2:
				
				if(Game.tileClicked[0] == 5 && Game.tileClicked[1] == this.column-1)
					Tile.getPiece(this.row, this.column-1).delete();
				break;
				
			}
			
		}
		
		if(enPassantRight) {
			
			switch(player) {
			
			case PLAYER_1:
				
				if(Game.tileClicked[0] == 2 && Game.tileClicked[1] == this.column+1)
					Tile.getPiece(this.row, this.column+1).delete();
				break;
				
			case PLAYER_2:
				if(Game.tileClicked[0] == 5 && Game.tileClicked[1] == this.column+1)
					Tile.getPiece(this.row, this.column+1).delete();
				break;
				
			}
			
		}
		
		if(moveCount == 0) 
			moveCountAtDoubleMove = Game.moveCount;
		
		if (Tile.isOccupiedByOpponent(row, column, player)) {
			Tile.getPiece(row, column).delete();
		}
		
		this.row = row;
		this.column = column;
		
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		if(player == GameData.player.PLAYER_1 ? row == 0 : row == 7) {

			if(GameData.singlePlayer == false || player == GameData.player.PLAYER_1)
				new Promotion(this);
			else {
				Game.player2Pieces.add(new Queen(row, column, GameData.player.PLAYER_2));
				delete();
			}
		}
	
	}
	
	@Override
	public void setLocation(int[] location) {
		
		this.row = location[0];
		this.column = location[1];
		
	}
	
	public void delete() {
		
		for(int z = 0; z < Game.player1Pieces.size(); z++) 			
			if(this == Game.player1Pieces.get(z)) 
				Game.player1Pieces.remove(z);
		
		for(int z = 0; z < Game.player2Pieces.size(); z++)	
			if(this == Game.player2Pieces.get(z))
				Game.player2Pieces.remove(z);
			
		
		
		row = -1;
		column = -1;
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		System.gc();
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
/*		g.setColor(color);
		
		//MAKES THE PAWN HEAD
		g.fillOval(GameData.getShapeStartingX(column, GameData.PAWN_HEAD_DIAMETER), GameData.getShapeStartingY(row, GameData.PAWN_HEAD_DIAMETER)-20, GameData.PAWN_HEAD_DIAMETER, GameData.PAWN_HEAD_DIAMETER);
		
		//MAKE THE SECOND PAWN HEAD
		g.fillOval(GameData.getShapeStartingX(column, GameData.PAWN_HEAD2_WIDTH), GameData.getShapeStartingY(row, GameData.PAWN_HEAD2_HEIGHT)-10, GameData.PAWN_HEAD2_WIDTH, GameData.PAWN_HEAD2_HEIGHT);
		
		//MAKES THE PAWN BODY
		g.fillPolygon(new int[] {tileCenterX+(GameData.PAWN_BODY_WIDTH/2), tileCenterX, tileCenterX-(GameData.PAWN_BODY_WIDTH/2)}, 
				new int[] {tileCenterY+(GameData.PAWN_BODY_HEIGHT/2), tileCenterY-(GameData.PAWN_BODY_HEIGHT/2), tileCenterY+(GameData.PAWN_BODY_HEIGHT/2)}, 
				3);
		
		//MAKES THE PAWN BASE
		g.fillRoundRect(GameData.getShapeStartingX(column, GameData.PAWN_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.PAWN_BASE_HEIGHT)+30, GameData.PAWN_BASE_WIDTH, GameData.PAWN_BASE_HEIGHT, 20, 20);
		
		*/
		
		if ((GameData.singlePlayer || Game.playerTurn == GameData.player.PLAYER_1) || GameData.switchViews == false)
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.PAWN_PIECE_IMAGE_PLAYER_1 : GameData.PAWN_PIECE_IMAGE_PLAYER_2, (column * (GameData.TILE_WIDTH))-(GameData.TILE_WIDTH/10), (row*GameData.TILE_HEIGHT), null);
		else
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.PAWN_PIECE_IMAGE_PLAYER_1 : GameData.PAWN_PIECE_IMAGE_PLAYER_2, ((GameData.COLUMNS-1-column) * GameData.TILE_WIDTH)-16, ((GameData.ROWS-1-row)*GameData.TILE_HEIGHT)-5, null);

	}

	@Override
	public Piece clone() {
		// TODO Auto-generated method stub
			return new Pawn(row, column, player);
	}

}
