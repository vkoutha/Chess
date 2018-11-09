package Chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import Chess.Piece.type;

public class Knight implements Piece{

	private int column, row, tileCenterX, tileCenterY, moveCount = 0;
	private Color color;
	private GameData.player player;
	private type type;
	
	public Knight(int row, int column, GameData.player player) {
		
		this.row = row;
		this.column = column;
		
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		this.player = player;
		
		if(player == GameData.player.PLAYER_1)
			color = GameData.PLAYER_1_PIECE_COLOR;
		else if (player == GameData.player.PLAYER_2)
			color = GameData.PLAYER_2_PIECE_COLOR;
		
		type = Piece.type.KNIGHT;
	}

	@Override
	public int getRow() {return row;}
	
	@Override
	public int getColumn() {return column;}
	
	@Override
	public int getPieceValue() {return GameData.KNIGHT_VALUE;}
	
	@Override
	public int getMoveCount() {return moveCount;}
	
	@Override
	public int getMoveCountAtFirstMove() {return 0;}
	
	@Override
	public void increaseMoveCount() {moveCount++;}

	@Override
	public boolean isInCheck() {return false;}
	
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

	@Override
	public ArrayList<int[]> getPossibleMoves() {
		// TODO Auto-generated method stub

		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		
		if(!Tile.isOccupiedByOpponent(row, column, player)) {
			
			//ALLOWS TO MOVE 1 UP, 2 RIGHT
			if(row-1 >= 0 && column+2 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row-1, column+2, player) || !Tile.isOccupied(row-1, column+2)))
				possibleMoves.add(new int[] {row-1, column+2});
			
			//ALLOWS TO MOVE 1 UP, 2 LEFT
			if(row-1 >= 0 && column-2 >= 0 && (Tile.isOccupiedByOpponent(row-1, column-2, player) || !Tile.isOccupied(row-1, column-2))) 
				possibleMoves.add(new int[] {row-1, column-2});
			
			//ALLOWS TO MOVE 1 DOWN, 2 RIGHT
			if(row+1 < GameData.ROWS && column+2 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row+1, column+2, player) || !Tile.isOccupied(row+1, column+2)))
				possibleMoves.add(new int[] {row+1, column+2});
			
			//ALLOWS TO MOVE 1 DOWN, 2 LEFT
			if(row+1 < GameData.ROWS && column-2 >= 0 && (Tile.isOccupiedByOpponent(row+1, column-2, player) || !Tile.isOccupied(row+1, column-2)))
				possibleMoves.add(new int[] {row+1, column-2});
			
			//ALLOWS TO MOVE 2 UP, 1 RIGHT
			if(row-2 >= 0 && column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row-2, column+1, player) || !Tile.isOccupied(row-2, column+1)))
				possibleMoves.add(new int[] {row-2, column+1});
			
			//ALLOWS TO MOVE 2 UP, 1 LEFT
			if(row-2 >= 0 && column-1 >= 0 && (Tile.isOccupiedByOpponent(row-2, column-1, player) || !Tile.isOccupied(row-2, column-1)))
				possibleMoves.add(new int[] {row-2, column-1});
			
			//ALLOWS TO MOVE 2 DOWN, 1 RIGHT
			if(row+2 < GameData.ROWS && column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row+2, column+1, player) || !Tile.isOccupied(row+2, column+1)))
				possibleMoves.add(new int[] {row+2, column+1});
			
			//ALLOWS TO MOVE 2 DOWN, 1 LEFT
			if(row+2 < GameData.ROWS && column-1 >= 0 && (Tile.isOccupiedByOpponent(row+2, column-1, player) || !Tile.isOccupied(row+2, column-1)))
				possibleMoves.add(new int[] {row+2, column-1});
			
		}
		
		return possibleMoves;
	}

	public ArrayList<int[]> getPossibleMovesInCheck(){
		
		ArrayList<int[]> possibleMoves = getPossibleMoves();
					
		int ogRow = row, ogColumn = column;
		for(int z = 0; z < possibleMoves.size(); z++) {
			
			row = possibleMoves.get(z)[0];
			column = possibleMoves.get(z)[1];
			
			if(z >= 0 && Piece.getKing(player).isInCheck()) {
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
		
		this.row = row;
		this.column = column;
		
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
	}
	
	@Override
	public void setLocation(int[] location) {
		
		this.row = location[0];
		this.column = location[1];
		
	}
	
	public void delete() {
		
		for(int z = 0; z < Game.player1Pieces.size(); z++) {
			
			if(this == Game.player1Pieces.get(z)) {
				Game.player1Pieces.remove(z);
			}
			
		}
		
		
		for(int z = 0; z < Game.player2Pieces.size(); z++) {
			
			if(this == Game.player2Pieces.get(z))
				Game.player2Pieces.remove(z);
			
		}
		
		row = -1;
		column = -1;
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		System.gc();
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
		/*
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform old = g2.getTransform();
       
		g.setColor(color);
		
		//DRAWS THE KNIGHT HEAD
		g2.rotate(Math.toRadians(45), tileCenterX, tileCenterY);
		g.fillOval(GameData.getShapeStartingX(column, GameData.KNIGHT_HEAD_WIDTH)-5, GameData.getShapeStartingY(row, GameData.KNIGHT_HEAD_WIDTH)-10, GameData.KNIGHT_HEAD_WIDTH, GameData.KNIGHT_HEAD_HEIGHT);

		g2.setTransform(old);

		//DRAWS THE KNIGHT BODY
		g.fillArc(GameData.getShapeStartingX(column, 75)+10, GameData.getShapeStartingY(row, 120)+15, 75, 120, 180, -90);
		
		//DRAWS THE KNIGHT BASE
		g.fillRect(GameData.getShapeStartingX(column, GameData.KNIGHT_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.KNIGHT_BASE_HEIGHT)+30, GameData.KNIGHT_BASE_WIDTH, GameData.KNIGHT_BASE_HEIGHT);
		g.fillOval(GameData.getShapeStartingX(column, GameData.KNIGHT_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.KNIGHT_BASE_HEIGHT)+20, GameData.KNIGHT_BASE_WIDTH, GameData.KNIGHT_BASE_HEIGHT);
		*/
		
		if (GameData.singlePlayer || Game.playerTurn == GameData.player.PLAYER_1)
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.KNIGHT_PIECE_IMAGE_PLAYER_1 : GameData.KNIGHT_PIECE_IMAGE_PLAYER_2, (column * GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), null);
		else
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.KNIGHT_PIECE_IMAGE_PLAYER_1 : GameData.KNIGHT_PIECE_IMAGE_PLAYER_2, ((GameData.COLUMNS-1-column) * GameData.TILE_WIDTH), ((GameData.ROWS-1-row)*GameData.TILE_HEIGHT), null);
	
	}

	@Override
	public Piece clone() {
		// TODO Auto-generated method stub
			return new Knight(row, column, player);
	}


}
