package Chess;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Chess.Piece.type;

public class King implements Piece{

	private int column, row, tileCenterX, tileCenterY, moveCount = 0;
	private boolean isCastleMoveRight = false, isCastleMoveLeft = false;
	private Color color;
	private GameData.player player;
	private type type;
	
	public King(int row, int column, GameData.player player) {
		
		this.row = row;
		this.column = column;
		
		tileCenterX = GameData.getTileCenter(row, column)[0];
		tileCenterY = GameData.getTileCenter(row, column)[1];
		
		this.player = player;
		
		if(player == GameData.player.PLAYER_1)
			color = GameData.PLAYER_1_KING_COLOR;
		else if (player == GameData.player.PLAYER_2)
			color = GameData.PLAYER_2_KING_COLOR;
		
		type = Piece.type.KING;

	}
	
	@Override
	public int getRow() {return row;}
	
	@Override
	public int getColumn() {return column;}
	
	@Override
	public int getPieceValue() {return GameData.KING_VALUE;}
	
	@Override
	public int getMoveCount() {return moveCount;}
	
	@Override
	public int getMoveCountAtFirstMove() {return 0;}
	
	@Override
	public void increaseMoveCount() {moveCount++;}
	
	@Override
	public type getType() {return type;}
	
	@Override
	public GameData.player getPlayer() {return player;}
	
	@Override
	public boolean isPieceThatChecked() {return false;}

	@Override
	public ArrayList<int[]> getPossibleMoves() {
		// TODO Auto-generated method stub
		
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
		                           
		//ALLOWS TO MOVE LEFT
		if(column-1 >= 0 && (Tile.isOccupiedByOpponent(row, column-1, player) || !Tile.isOccupied(row, column-1)))
			possibleMoves.add(new int[] {row, column-1});
		
		//ALLOWS TO MOVE RIGHT
		if(column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row, column+1, player) || !Tile.isOccupied(row, column+1)))
			possibleMoves.add(new int[] {row, column+1});
		
		//ALLOWS TO MOVE UP
		if(row-1 >= 0 && (Tile.isOccupiedByOpponent(row-1, column, player) || !Tile.isOccupied(row-1, column)))
			possibleMoves.add(new int[] {row-1, column});
		
		//ALLOWS TO MOVE DOWN
		if(row+1 < GameData.ROWS && (Tile.isOccupiedByOpponent(row+1, column, player) || !Tile.isOccupied(row+1, column)))
			possibleMoves.add(new int[] {row+1, column});
		
		//ALLOWS TO MOVE DIAGONAL UP-LEFT
		if(row-1 >= 0 && column-1 >= 0 && (Tile.isOccupiedByOpponent(row-1, column-1, player) || !Tile.isOccupied(row-1, column-1)))
			possibleMoves.add(new int[] {row-1, column-1});
		
		//ALLOWS TO MOVE DIAGONAL UP-RIGHT
		if(row-1 >= 0 && column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row-1, column+1, player) || !Tile.isOccupied(row-1, column+1)))
			possibleMoves.add(new int[] {row-1, column+1});
		
		//ALLOWS TO MOVE DIAGONAL DOWN-LEFT
		if(row+1 < GameData.ROWS && column-1 >= 0 && (Tile.isOccupiedByOpponent(row+1, column-1, player) || !Tile.isOccupied(row+1, column-1)))
			possibleMoves.add(new int[] {row+1, column-1});
		
		//ALLOWS TO MOVE DIAGONAL DOWN-RIGHT
		if(row+1 < GameData.ROWS && column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row+1, column+1, player) || !Tile.isOccupied(row+1, column+1)))
			possibleMoves.add(new int[] {row+1, column+1});
		
		return possibleMoves;
		
	}
	
	public ArrayList<int[]> getPossibleMovesInCheck() {
		// TODO Auto-generated method stub
		
		ArrayList<int[]> possibleMoves = getPossibleMoves();
		
		if(moveCount == 0) {
			
			//ALLOWS CASTLE WITH RIGHT ROOK
			if(!Piece.isInCheck(player) && Tile.isOccupied(row, 7) && Tile.getPiece(row, 7).getType() == Piece.type.ROOK && Tile.getPiece(row, 7).getMoveCount() == 0  && moveCount == 0 && !Tile.isOccupied(row, 5) && !Tile.isOccupied(row, 6)) { 
				isCastleMoveRight = true;
				possibleMoves.add(new int[] {row, column+2});
			}
			
			//ALLOWS CASTLE WITH LEFT ROOK
			if(!Piece.isInCheck(player) && Tile.isOccupied(row, 0) && Tile.getPiece(row, 0).getType() == Piece.type.ROOK && Tile.getPiece(row, 0).getMoveCount() == 0  && moveCount == 0 && !Tile.isOccupied(row, 3) && !Tile.isOccupied(row, 2) && !Tile.isOccupied(row, 1)) { 
				isCastleMoveLeft = true;
				possibleMoves.add(new int[] {row, column-2});
			}
			
		}else{
			
			isCastleMoveRight = false;
			isCastleMoveLeft = false;
			
		}
		
		for(int z = 0; z < possibleMoves.size(); z++) {
			
			if(z >= 0 && Piece.isInCheck(player, possibleMoves.get(z)[0], possibleMoves.get(z)[1])) {
				possibleMoves.remove(z);
				z--;
			}
			
		}
		
		return possibleMoves;
		
	}
	
	public ArrayList<int[]> getAllMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces){
		
		ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        
		//ALLOWS TO MOVE LEFT
		if(column-1 >= 0 && (Tile.isOccupiedByOpponent(row, column-1, player, playerPieces, botPieces) || !Tile.isOccupied(row, column-1, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row, column-1});
		
		//ALLOWS TO MOVE RIGHT
		if(column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row, column+1, player, playerPieces, botPieces) || !Tile.isOccupied(row, column+1, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row, column+1});
		
		//ALLOWS TO MOVE UP
		if(row-1 >= 0 && (Tile.isOccupiedByOpponent(row-1, column, player, playerPieces, botPieces) || !Tile.isOccupied(row-1, column, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row-1, column});
		
		//ALLOWS TO MOVE DOWN
		if(row+1 < GameData.ROWS && (Tile.isOccupiedByOpponent(row+1, column, player, playerPieces, botPieces) || !Tile.isOccupied(row+1, column, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row+1, column});
		
		//ALLOWS TO MOVE DIAGONAL UP-LEFT
		if(row-1 >= 0 && column-1 >= 0 && (Tile.isOccupiedByOpponent(row-1, column-1, player, playerPieces, botPieces) || !Tile.isOccupied(row-1, column-1, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row-1, column-1});
		
		//ALLOWS TO MOVE DIAGONAL UP-RIGHT
		if(row-1 >= 0 && column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row-1, column+1, player, playerPieces, botPieces) || !Tile.isOccupied(row-1, column+1, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row-1, column+1});
		
		//ALLOWS TO MOVE DIAGONAL DOWN-LEFT
		if(row+1 < GameData.ROWS && column-1 >= 0 && (Tile.isOccupiedByOpponent(row+1, column-1, player, playerPieces, botPieces) || !Tile.isOccupied(row+1, column-1, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row+1, column-1});
		
		//ALLOWS TO MOVE DIAGONAL DOWN-RIGHT
		if(row+1 < GameData.ROWS && column+1 < GameData.COLUMNS && (Tile.isOccupiedByOpponent(row+1, column+1, player, playerPieces, botPieces) || !Tile.isOccupied(row+1, column+1, playerPieces, botPieces)))
			possibleMoves.add(new int[] {row+1, column+1});
			
		return possibleMoves;
		
	}
	
	public ArrayList<int[]> getPossibleMovesAI(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces){
		
		ArrayList<int[]> possibleMoves = getAllMovesAI(playerPieces, botPieces);
		
	/*	if(moveCount == 0) {
			
			//ALLOWS CASTLE WITH RIGHT ROOK
			if(Tile.isOccupied(row, 7, playerPieces, botPieces) && Tile.getPiece(row, 7, playerPieces, botPieces).getType() == Piece.type.ROOK && Tile.getPiece(row, 7, playerPieces, botPieces).getMoveCount() == 0  && moveCount == 0 && !Tile.isOccupied(row, 5, playerPieces, botPieces) && !Tile.isOccupied(row, 6, playerPieces, botPieces)) { 
				isCastleMoveRight = true;
				possibleMoves.add(new int[] {row, column+2});
			}
			
			//ALLOWS CASTLE WITH LEFT ROOK
			if(Tile.isOccupied(row, 0, playerPieces, botPieces) && Tile.getPiece(row, 0, playerPieces, botPieces).getType() == Piece.type.ROOK && Tile.getPiece(row, 0, playerPieces, botPieces).getMoveCount() == 0  && moveCount == 0 && !Tile.isOccupied(row, 3, playerPieces, botPieces) && !Tile.isOccupied(row, 2, playerPieces, botPieces) && !Tile.isOccupied(row, 1, playerPieces, botPieces)) { 
				isCastleMoveLeft = true;
				possibleMoves.add(new int[] {row, column-2});
			}
			
		}else{
			isCastleMoveRight = false;
			isCastleMoveLeft = false;
		}
		
		for(int z = 0; z < possibleMoves.size(); z++) {
			if(z >= 0 && Piece.isInCheck(player, possibleMoves.get(z)[0], possibleMoves.get(z)[1], playerPieces, botPieces)) {
				possibleMoves.remove(z);
				z--;	
			}
		}*/
		
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
	
	public void delete() {}
	
	public void move(int row, int column) {	
		
		if(isCastleMoveRight){
			
			Tile.getPiece(row, 7).move(row, 5);
			isCastleMoveRight = false;
			
		}else if(isCastleMoveLeft){
			
			Tile.getPiece(row, 0).move(row, 3);
			isCastleMoveLeft = false;
			
		}

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
	
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		/*
		g.setColor(color);
		
		//DRAWS THE KING HEAD
		g.fillRect(GameData.getShapeStartingX(column, GameData.KING_HORIZONTAL_CROSS_WIDTH), GameData.getShapeStartingY(row, GameData.KING_HORIZONTAL_CROSS_HEIGHT)-38, GameData.KING_HORIZONTAL_CROSS_WIDTH, GameData.KING_HORIZONTAL_CROSS_HEIGHT);
		g.fillRect(GameData.getShapeStartingX(column, GameData.KING_VERTICAL_CROSS_WIDTH), GameData.getShapeStartingY(row, GameData.KING_VERTICAL_CROSS_HEIGHT)-32, GameData.KING_VERTICAL_CROSS_WIDTH, GameData.KING_VERTICAL_CROSS_HEIGHT);

		//DRAWS THE KING NECK
		g.fillOval(GameData.getShapeStartingX(column, GameData.KING_NECK_WIDTH), GameData.getShapeStartingY(row, GameData.KING_NECK_HEIGHT)-15, GameData.KING_NECK_WIDTH, GameData.KING_NECK_HEIGHT);
		
		//DRAWS THE TOP HALF OF THE KING BODY
		g.fillPolygon(new int[] {tileCenterX+(GameData.KING_BODY_WIDTH/2), tileCenterX, tileCenterX-(GameData.KING_BODY_WIDTH/2)}, 
			new int[] {tileCenterY-(GameData.KING_BODY_HEIGHT/2)-15, tileCenterY+(GameData.KING_BODY_HEIGHT/2)-15, tileCenterY-(GameData.KING_BODY_HEIGHT/2)-15}, 
				3);
				
		//DRAWS THE BOTTOM HALF OF THE KING BODY
		g.fillPolygon(new int[] {tileCenterX+(GameData.KING_BODY_WIDTH/2), tileCenterX, tileCenterX-(GameData.KING_BODY_WIDTH/2)}, 
				new int[] {tileCenterY+(GameData.KING_BODY_HEIGHT/2)+15, tileCenterY-(GameData.KING_BODY_HEIGHT/2)+15, tileCenterY+(GameData.KING_BODY_HEIGHT/2)+15}, 
				3);
		
		//DRAWS THE CONNECTOR BETWEEN THE TWO BODY HALFS
		g.fillRect(GameData.getShapeStartingX(column, GameData.KING_BODY_CONNECTOR_WIDTH), GameData.getShapeStartingY(row, GameData.KING_BODY_CONNECTOR_HEIGHT), GameData.KING_BODY_CONNECTOR_WIDTH, GameData.KING_BODY_CONNECTOR_HEIGHT);
		
		//DRAWS THE KING BASE
		g.fillRect(GameData.getShapeStartingX(column, GameData.KING_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.KING_BASE_HEIGHT)+35, GameData.KING_BASE_WIDTH, GameData.KING_BASE_HEIGHT);
		g.fillOval(GameData.getShapeStartingX(column, GameData.KING_BASE_WIDTH), GameData.getShapeStartingY(row, GameData.KING_BASE_HEIGHT)+30, GameData.KING_BASE_WIDTH, GameData.KING_BASE_HEIGHT);

		*/
		
		if ((GameData.singlePlayer || Game.playerTurn == GameData.player.PLAYER_1) || GameData.switchViews == false)
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.KING_PIECE_IMAGE_PLAYER_1 : GameData.KING_PIECE_IMAGE_PLAYER_2, (column * (GameData.TILE_WIDTH-1)), (row*GameData.TILE_HEIGHT), null);
		else
			g.drawImage(player == GameData.player.PLAYER_1 ? GameData.KING_PIECE_IMAGE_PLAYER_1 : GameData.KING_PIECE_IMAGE_PLAYER_2, ((GameData.COLUMNS-1-column)*GameData.TILE_WIDTH), ((GameData.ROWS-1-row)*GameData.TILE_HEIGHT), null);
	
	}

	@Override
	public Piece clone() {
		// TODO Auto-generated method stub
			return new King(row, column, player);
	}

	
}
