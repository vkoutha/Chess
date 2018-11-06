package Chess;

import java.awt.Color;
import java.awt.Graphics;

public class Tile {

	int row, column;
	boolean isSelected, isValidMove, isTileThatChecked, isAvailableMoveInCheck;
	Color color;
	
	public Tile(int row, int column, Color color) {
		
		this.row = row;
		this.column = column;
			
		this.color = color;
		
	}
	
	public static Piece getPiece(int row, int column) {
		
		for(int z = 0; z < Game.player1Pieces.size(); z++) 
			if(Game.player1Pieces.get(z).getRow() == row && Game.player1Pieces.get(z).getColumn() == column)
				return Game.player1Pieces.get(z);
			
		for(int z = 0; z < Game.player2Pieces.size(); z++) 
			if(Game.player2Pieces.get(z).getRow() == row && Game.player2Pieces.get(z).getColumn() == column)
				return Game.player2Pieces.get(z);
		
		return null;
		
	}
		
	public static Color getTileColor(int row, int column) {
		
		if(row % 2 == 0)
			if(column % 2 == 0)
				return GameData.TILE_COLOR_1_WHITE;
			else
				return GameData.TILE_COLOR_2_MAROON;	
		else
			if(column % 2 == 0)
				return GameData.TILE_COLOR_2_MAROON;
			else
				return GameData.TILE_COLOR_1_WHITE;
				
	}
	
	public static boolean isOccupied(int row, int column) {
		
		for(int z = 0; z < Game.player1Pieces.size(); z++) 
			if(Game.player1Pieces.get(z).getRow() == row && Game.player1Pieces.get(z).getColumn() == column) 
				return true;
		
		for(int z = 0; z < Game.player2Pieces.size(); z++) 
			if(Game.player2Pieces.get(z).getRow() == row && Game.player2Pieces.get(z).getColumn() == column) 
				return true;
			
		return false;
		
	}
	
	public static boolean isOccupiedByPlayer1(int row, int column) {
		
		for(int z = 0; z < Game.player1Pieces.size(); z++) 
				if(Game.player1Pieces.get(z).getRow() == row && Game.player1Pieces.get(z).getColumn() == column) 
					return true;
			
		return false;
		
	}
	
	public static boolean isOccupiedByPlayer2(int row, int column) {
		
		for(int z = 0; z < Game.player2Pieces.size(); z++) 
			if(Game.player2Pieces.get(z).getRow() == row && Game.player2Pieces.get(z).getColumn() == column) 
				return true;
			
		return false;
		
	}
	
	public static boolean isOccupiedByOwn(int row, int column, GameData.player player) {
		
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++)
				if(Game.player1Pieces.get(z).getRow() == row && Game.player1Pieces.get(z).getColumn() == column) 
					return true;
			break;

		case PLAYER_2:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 				
				if(Game.player2Pieces.get(z).getRow() == row && Game.player2Pieces.get(z).getColumn() == column) 
					return true;
			break;
			
		}
		
		return false;	
			
	}
	
	public static boolean isOccupiedByOpponent(int row, int column, GameData.player player) {
		
		switch(player) {
		
		case PLAYER_1:
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 
				if(Game.player2Pieces.get(z).getRow() == row && Game.player2Pieces.get(z).getColumn() == column) 
					return true;
			break;
			
		case PLAYER_2:
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
				if(Game.player1Pieces.get(z).getRow() == row && Game.player1Pieces.get(z).getColumn() == column) 
					return true;
			break;
			
		}
		
		return false;
		
	}
	
	public static void resetTiles(boolean resetTileThatChecked) {
		
		for(int rowNumber = 0; rowNumber < GameData.ROWS; rowNumber++) {
			
			for(int columnNumber = 0; columnNumber < GameData.COLUMNS; columnNumber++) {
				
				if(Game.board[rowNumber][columnNumber].isSelected)
					Game.board[rowNumber][columnNumber].setAsSelected(false);
				
				if(Game.board[rowNumber][columnNumber].isValidMove)
					Game.board[rowNumber][columnNumber].setAsValidMove(false);
				
				if(Game.board[rowNumber][columnNumber].isAvailableMoveInCheck)
					Game.board[rowNumber][columnNumber].setAsAvailableMoveInCheck(false);
				
				if(resetTileThatChecked == true && Game.board[rowNumber][columnNumber].isTileThatChecked)
					Game.board[rowNumber][columnNumber].setAsTileThatChecked(false);
				
			}
			
		}
		
	}

	public void setAsSelected(boolean setAsSelected) {
		
		if(setAsSelected == true) {
			
			color = GameData.TILE_COLOR_WHEN_CLICKED;
			isSelected = true;
			
		}else if(!isTileThatChecked){
			
			color = getTileColor(row, column);
			isSelected = false;
			
		}
		
		
	}
	
	public void setAsValidMove(boolean setAsValidMove) {
		
		if(setAsValidMove == true) {
			
			if(isOccupied(row, column))
				color = GameData.TILE_COLOR_WHEN_CLICKED_OCCUPIED;
			else
				color = GameData.TILE_COLOR_WHEN_CLICKED;
			
			isValidMove = true;
			
		}else{
			
			if(!isTileThatChecked)
				color = getTileColor(row, column);
			else
				color = GameData.TILE_COLOR_WHEN_CHECKED;
			
			isValidMove = false;
			
		}
			
	}
	
	public void setAsTileThatChecked(boolean setAsTileThatChecked) {
		
		if(setAsTileThatChecked == true) {
			color = GameData.TILE_COLOR_WHEN_CHECKED;
			isTileThatChecked = true;
		}else{
			color = getTileColor(row, column);
			isTileThatChecked = false;
		}
		
	}
	
	public void setAsAvailableMoveInCheck(boolean setAsAvailableMoveInCheck) {
		
		if(setAsAvailableMoveInCheck == true){ 
			color = GameData.TILE_COLOR_WHEN_AVAILABLE_MOVE_IN_CHECK;
			isAvailableMoveInCheck = true;
		}else{
			color = getTileColor(row, column);
			isAvailableMoveInCheck = false;
		}
		
	}
	
	public void render(Graphics g) {
		
		g.setColor(color);
		g.fillRect(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
		
		if(color == GameData.TILE_COLOR_WHEN_CLICKED || color == GameData.TILE_COLOR_WHEN_CLICKED_OCCUPIED || color == GameData.TILE_COLOR_WHEN_CHECKED || color == GameData.TILE_COLOR_WHEN_AVAILABLE_MOVE_IN_CHECK) {
			
			g.setColor(Color.white);
			g.drawRect(column * GameData.TILE_WIDTH-1, row * GameData.TILE_HEIGHT-1, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
			
		}
		
	}

}
