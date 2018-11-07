package Chess;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Chess.Piece.type;

public class Queen implements Piece{

	private int column, row, tileCenterX, tileCenterY, moveCount = 0;
	private Color color;
	private GameData.player player;
	private type type;
	
	public Queen(int row, int column, GameData.player player) {
		
		this.row = row;
		this.column = column;
		
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		this.player = player;
		
		if(player == GameData.player.PLAYER_1)
			color = GameData.PLAYER_1_PIECE_COLOR;
		else if(player == GameData.player.PLAYER_2)
			color = GameData.PLAYER_2_PIECE_COLOR;
		
		type = Piece.type.QUEEN;
		
	}

	@Override
	public int getRow() {return row;}
	
	@Override
	public int getColumn() {return column;}
	
	@Override
	public int getPieceValue() {return GameData.QUEEN_VALUE;}
	
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

		if(Tile.isOccupiedByOpponent(row, column, player))
			return possibleMoves;
			
			//ALLOWS TO MOVE LEFT
			for(int z = column-1; z >= 0; z--) {
				
				if(Tile.isOccupiedByOwn(row, z, player) && !Tile.isOccupiedByOpponent(row, z, player))
					break;
				else if(Tile.isOccupiedByOpponent(row, z, player)) {
					possibleMoves.add(new int[] {row, z});
					break;
				}
				
				possibleMoves.add(new int[] {row, z});
				
			}
			
			//ALLOWS TO MOVE RIGHT
			for(int z = column+1; z < GameData.COLUMNS; z++) {
				
				if(Tile.isOccupiedByOwn(row, z, player) && !Tile.isOccupiedByOpponent(row, z, player))
					break;
				else if(Tile.isOccupiedByOpponent(row, z, player)) {
					possibleMoves.add(new int[] {row, z});
					break;
				}
				
				possibleMoves.add(new int[] {row, z});
				
			}
			
			//ALLOWS TO MOVE UP
			for(int z = row-1; z >= 0; z--) {
				
				if(Tile.isOccupiedByOwn(z, column, player) && !Tile.isOccupiedByOpponent(z, column, player))
					break;
				else if(Tile.isOccupiedByOpponent(z, column, player)) {
					possibleMoves.add(new int[] {z, column});
					break;
			}else
				possibleMoves.add(new int[] {z, column});
				
			}
			
			//ALOWS TO MOVE DOWN
			for(int z = row+1; z < GameData.ROWS; z++) {
				
				if(Tile.isOccupiedByOwn(z, column, player) && !Tile.isOccupiedByOpponent(z, column, player))
					break;
				else if(Tile.isOccupiedByOpponent(z, column, player)) {
					possibleMoves.add(new int[] {z, column});
					break;
				}else	
					possibleMoves.add(new int[] {z, column});
			
			}
			
			//ALLOWS TO MOVE DIAGONAL UP-LEFT
			for(int zR = row-1, zC = column-1; zR >= 0 && zC >= 0; zR--, zC--) {
				
				if(Tile.isOccupiedByOwn(zR, zC, player) && !Tile.isOccupiedByOpponent(zR, zC, player))
					break;
				else if(Tile.isOccupiedByOpponent(zR, zC, player)) {
					possibleMoves.add(new int[] {zR, zC});
					break;
				}
	
			possibleMoves.add(new int[] {zR, zC});
			
			}
			
			//ALLOWS TO MOVE DIAGONAL UP-RIGHT
			for(int zR = row-1, zC = column+1; zR >= 0 && zC < GameData.COLUMNS; zR--, zC++) {
				
				if(Tile.isOccupiedByOwn(zR, zC, player) && !Tile.isOccupiedByOpponent(zR, zC, player))
					break;
				else if(Tile.isOccupiedByOpponent(zR, zC, player)) {
					possibleMoves.add(new int[] {zR, zC});
					break;
				}
	
				possibleMoves.add(new int[] {zR, zC});
				
			}
			
			//ALLOWS TO MOVE DIAGONAL DOWN-LEFT
			for(int zR = row+1, zC = column-1; zR < GameData.ROWS && zC >= 0; zR++, zC--) {
				
				if(Tile.isOccupiedByOwn(zR, zC, player) && !Tile.isOccupiedByOpponent(zR, zC, player))
					break;
				else if(Tile.isOccupiedByOpponent(zR, zC, player)) {
					possibleMoves.add(new int[] {zR, zC});
					break;
				}
			
			possibleMoves.add(new int[] {zR, zC});
			
			}
			
			//ALLOWS TO MOVE DIAGONAL DOWN-RIGHT
			for(int zR = row+1, zC = column+1; zR < GameData.ROWS && zC < GameData.COLUMNS; zR++, zC++) {
				
				if(Tile.isOccupiedByOwn(zR, zC, player) && !Tile.isOccupiedByOpponent(zR, zC, player))
					break;
				else if(Tile.isOccupiedByOpponent(zR, zC, player)) {
					possibleMoves.add(new int[] {zR, zC});
					break;
				}
			
			possibleMoves.add(new int[] {zR, zC});
			
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
		g.setColor(color);
		
		//DRAWS THE QUEEN HEAD
		g.fillOval(GameData.getShapeStartingX(column, GameData.QUEEN_HEAD_DIAMETER), GameData.getShapeStartingY(row, GameData.QUEEN_HEAD_DIAMETER)-38, GameData.QUEEN_HEAD_DIAMETER, GameData.QUEEN_HEAD_DIAMETER);

		//DRAWS THE QUEEN NECK
		g.fillOval(GameData.getShapeStartingX(column, GameData.QUEEN_NECK_WIDTH), GameData.getShapeStartingY(row, GameData.QUEEN_NECK_HEIGHT)-15, GameData.QUEEN_NECK_WIDTH, GameData.QUEEN_NECK_HEIGHT);
		
		//DRAWS THE TOP HALF OF THE QUEEN BODY
		g.fillPolygon(new int[] {tileCenterX+(GameData.QUEEN_BODY_WIDTH/2), tileCenterX, tileCenterX-(GameData.QUEEN_BODY_WIDTH/2)}, 
			new int[] {tileCenterY-(GameData.QUEEN_BODY_HEIGHT/2)-15, tileCenterY+(GameData.QUEEN_BODY_HEIGHT/2)-15, tileCenterY-(GameData.QUEEN_BODY_HEIGHT/2)-15}, 
				3);
				
		//DRAWS THE BOTTOM HALF OF THE QUEEN BODY
		g.fillPolygon(new int[] {tileCenterX+(GameData.QUEEN_BODY_WIDTH/2), tileCenterX, tileCenterX-(GameData.QUEEN_BODY_WIDTH/2)}, 
				new int[] {tileCenterY+(GameData.QUEEN_BODY_HEIGHT/2)+15, tileCenterY-(GameData.QUEEN_BODY_HEIGHT/2)+15, tileCenterY+(GameData.QUEEN_BODY_HEIGHT/2)+15}, 
				3);
		
		//DRAWS THE CONNECTOR BETWEEN THE TWO BODY HALFS
		g.fillRect(GameData.getShapeStartingX(column, GameData.QUEEN_BODY_CONNECTOR_WIDTH), GameData.getShapeStartingY(row, GameData.QUEEN_BODY_CONNECTOR_HEIGHT), GameData.QUEEN_BODY_CONNECTOR_WIDTH, GameData.QUEEN_BODY_CONNECTOR_HEIGHT);
		
		//DRAWS THE QUEEN BASE
		g.fillRect(GameData.getShapeStartingX(column, GameData.QUEEN_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.QUEEN_BASE_HEIGHT)+35, GameData.QUEEN_BASE_WIDTH, GameData.QUEEN_BASE_HEIGHT);
		g.fillOval(GameData.getShapeStartingX(column, GameData.QUEEN_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.QUEEN_BASE_HEIGHT)+30, GameData.QUEEN_BASE_WIDTH, GameData.QUEEN_BASE_HEIGHT);
	 */

		try {
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.QUEEN_PIECE_IMAGE_PLAYER_1 : GameData.QUEEN_PIECE_IMAGE_PLAYER_2, (column * GameData.TILE_WIDTH)+3, (row*GameData.TILE_HEIGHT)+10, null);
			}catch(Exception e) {
				e.printStackTrace();
			}
		
	}

	@Override
	public Piece clone() {
		// TODO Auto-generated method stub
			return new Queen(row, column, player);
	}


}
