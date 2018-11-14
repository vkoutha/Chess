package Chess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.text.StyledEditorKit.ForegroundAction;

public class AI implements Runnable{

	GameData.player player;
	ArrayList<Piece> pieces;
	Random random;
	
	Piece pieceToMove = null;
	int[] randomLocation = null;
	int randomIndexPiece, randomIndexMove;
	boolean inMove = false;
	
	public AI(){
				
		player = GameData.player.PLAYER_2;
		pieces = Game.player2Pieces;

		random = new Random();
	}
	
	public void randomMove() {
		
		inMove = true;
		long moveTime = System.currentTimeMillis();
		
		switch(GameData.AI_LEVEL) {
		
		case 1:
			level1Move();
			break;
		case 2:
			level2Move();
			break;
		case 3:
			level3Move();
			break;
		case 4:
			level4Move();
			break;
		case 5:
			level5Move();
			break;
		case 6:
			level6Move();
			break;
		case 7:
			level7Move();
			break;
		case 8:
			level8Move();
			break;

		}
		
		if (GameData.AI_LEVEL < 5) {
			try {
				if(Piece.getKing(player).isInCheck())
					Thread.sleep(random.nextInt(500)+1000);
				else
					Thread.sleep(random.nextInt(500)+500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (System.currentTimeMillis() - moveTime < 700) {
			try {
				Thread.sleep(random.nextInt(1000)+500);
				System.out.println("wait");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Game.prevTileClicked = new int[] {pieceToMove.getRow(), pieceToMove.getColumn()};
		Game.tileClicked = randomLocation;
		Game.onValidMoveClick();
		inMove = false;
		
	}
	
	/**
	 * Makes completely random moves
	 */
	private void level1Move() {
	
		//PICKS A RANDOM NUMBER IN ARRAYLIST OF PIECES 		
		randomIndexPiece = randomPieceIndex(pieces);
		pieceToMove = pieces.get(randomIndexPiece);

		//PICKS A RANDOM NUMBER IN ARRAYLIST OF MOVES
		randomIndexMove = randomMoveIndex(pieceToMove.getPossibleMovesInCheck());
		randomLocation = pieces.get(randomIndexPiece).getPossibleMovesInCheck().get(randomIndexMove);
		
	}
	
	/**
	 * Will kill any pieces if it can, makes random move otherwise
	 */
	private void level2Move() {
					
		//IF THERE IS A PIECE THAT CAN KILL
		if(piecesWithKill().size() > 0) {
			
			//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
			randomIndexPiece = randomPieceIndex(piecesWithKill());
			pieceToMove = piecesWithKill().get(randomIndexPiece);

			//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
			randomIndexMove = randomMoveIndex(killableMoves(pieceToMove));
			randomLocation = killableMoves(piecesWithKill().get(randomIndexPiece)).get(randomIndexMove);
		
		//IF NO KILLABLE PIECES
		}else{
			
			//PICKS A RANDOM NUMBER IN ARRAYLIST OF PIECES 		
			randomIndexPiece = randomPieceIndex(pieces);
			pieceToMove = pieces.get(randomIndexPiece);

			//PICKS A RANDOM NUMBER IN ARRAYLIST OF MOVES
			randomIndexMove = randomMoveIndex(pieceToMove.getPossibleMovesInCheck());
			randomLocation = pieces.get(randomIndexPiece).getPossibleMovesInCheck().get(randomIndexMove);
		
		}
		
	}
	
	/**
	 * Goes through all pieces and finds the optimal/best piece to kill; takes into account whether or not it will be killed by a better/worse piece after killing
	 */
	private void level3Move() {
		
		Piece pieceWithBestCapture = null;
		int[] moveLocation = null;
		int highestCaptureValue = 0, lowestPieceWithKillValue = 0;
		if(piecesWithKill().size() > 0) 
			for(Piece piece : piecesWithKill()) 
				for(int[] possibleMoveLocation : killableMoves(piece)) 
					if (Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= piece.getPieceValue() || !canBeKilled(piece, possibleMoveLocation) || piece.getType() == Piece.type.PAWN) 
							 if(Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= highestCaptureValue && (piece.getPieceValue() < lowestPieceWithKillValue || pieceWithBestCapture == null)){
								pieceWithBestCapture = piece;
								lowestPieceWithKillValue = piece.getPieceValue();
								moveLocation = possibleMoveLocation;
								highestCaptureValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							}
			

			pieceToMove = pieceWithBestCapture;
			randomLocation = moveLocation;
			
			if (pieceWithBestCapture == null && highestCaptureValue == 0) {
				
				if (piecesWithNoKill().size() > 0){
					
					//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
					randomIndexPiece = randomPieceIndex(piecesWithNoKill());
					pieceToMove = piecesWithNoKill().get(randomIndexPiece);

					//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
					randomIndexMove = randomMoveIndex(nonKillableMoves(pieceToMove));
					randomLocation = nonKillableMoves(piecesWithNoKill().get(randomIndexPiece)).get(randomIndexMove);
					
				}else
					level1Move();
				
			}
										
	}
	
	private void level4Move() {
		
		Piece pieceWithBestCapture = null;
		int[] moveLocationCapture = null;
		int highestCaptureValue = 0, lowestPieceWithKillValue = 0;
		if(piecesWithKill().size() > 0) 
			for(Piece piece : piecesWithKill()) 
				for(int[] possibleMoveLocation : killableMoves(piece)) 
					if (Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= piece.getPieceValue() || !canBeKilled(piece, possibleMoveLocation)  || piece.getType() == Piece.type.PAWN) 
							 if(Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= highestCaptureValue && (piece.getPieceValue() < lowestPieceWithKillValue || pieceWithBestCapture == null)){
								pieceWithBestCapture = piece;
								lowestPieceWithKillValue = piece.getPieceValue();
								moveLocationCapture = possibleMoveLocation;
								highestCaptureValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							}
		
		Piece pieceToSave = null;
		int[] moveLocationSave = null;
		int highestSaveValue = 0;
		
		for(Piece piece : pieces)
			if (canBeKilled(piece) && !isSafe(piece))
				for(int[] possibleMoveLocation : piece.getPossibleMovesInCheck())
					if (isSafe(piece, possibleMoveLocation)) {
						if(Tile.isOccupiedByOpponent(possibleMoveLocation[0], possibleMoveLocation[1], player) && isSafe(piece, possibleMoveLocation)) {
							pieceToSave = piece;
							moveLocationSave = possibleMoveLocation;
							highestSaveValue = piece.getPieceValue();
							break;
						}
						
						pieceToSave = piece;
						moveLocationSave = possibleMoveLocation;
						highestSaveValue = piece.getPieceValue();
						
					}
					
		if (highestCaptureValue >= highestSaveValue) {
			pieceToMove = pieceWithBestCapture;
			randomLocation = moveLocationCapture;
		}else{
			pieceToMove = pieceToSave;
			randomLocation = moveLocationSave;
		}
		
		if (pieceToMove == null && randomLocation == null) {
			
			if (piecesWithNoKill().size() > 0){
				
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithNoKill());
				pieceToMove = piecesWithNoKill().get(randomIndexPiece);

				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(nonKillableMoves(pieceToMove));
				randomLocation = nonKillableMoves(piecesWithNoKill().get(randomIndexPiece)).get(randomIndexMove);
				
			}else
				level1Move();
			
		}
		
	}
	
	private void level5Move() {
		
		Piece pieceWithBestCapture = null;
		int[] moveLocationCapture = null;
		int highestCaptureValue = 0, lowestPieceWithKillValue = 0;
		if(piecesWithKill().size() > 0) 
			for(Piece piece : piecesWithKill()) 
				for(int[] possibleMoveLocation : killableMoves(piece)) 
					if (Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= piece.getPieceValue() || !canBeKilled(piece, possibleMoveLocation)  || piece.getType() == Piece.type.PAWN) 
							 if(Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= highestCaptureValue && (piece.getPieceValue() < lowestPieceWithKillValue || pieceWithBestCapture == null)){
								pieceWithBestCapture = piece;
								lowestPieceWithKillValue = piece.getPieceValue();
								moveLocationCapture = possibleMoveLocation;
								highestCaptureValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							}
		
		Piece pieceToSave = null;
		int[] moveLocationSave = null;
		int highestSaveValue = 0;
		
		for(Piece piece : pieces)
			if (!isSafe(piece) && piece.getType() != Piece.type.PAWN && piece.getPieceValue() >= highestSaveValue)
				for(int[] possibleMoveLocation : piece.getPossibleMovesInCheck())
					if (isSafe(piece, possibleMoveLocation)) {
						if(Tile.isOccupiedByOpponent(possibleMoveLocation[0], possibleMoveLocation[1], player) && isSafe(piece, possibleMoveLocation)) {
							pieceToSave = piece;
							moveLocationSave = possibleMoveLocation;
							highestSaveValue = piece.getPieceValue();
							break;
						}
						
						pieceToSave = piece;
						moveLocationSave = possibleMoveLocation;
						highestSaveValue = piece.getPieceValue();
						
					}
					
		if (highestCaptureValue >= highestSaveValue) {
			pieceToMove = pieceWithBestCapture;
			randomLocation = moveLocationCapture;
		}else{
			pieceToMove = pieceToSave;
			randomLocation = moveLocationSave;
		}
		
		if (pieceToMove == null && randomLocation == null) {
			
			if(piecesWithSafeMoves().size() > 0) {
				
				System.out.println("SAFE MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeMoves());
				pieceToMove = piecesWithSafeMoves().get(randomIndexPiece);
				
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(safeMoves(pieceToMove));
				randomLocation = safeMoves(piecesWithSafeMoves().get(randomIndexPiece)).get(randomIndexMove);
				
			}else if (piecesWithNoKill().size() > 0){
				
				System.out.println("NO KILL MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithNoKill());
				pieceToMove = piecesWithNoKill().get(randomIndexPiece);

				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(nonKillableMoves(pieceToMove));
				randomLocation = nonKillableMoves(piecesWithNoKill().get(randomIndexPiece)).get(randomIndexMove);
				
			}else {
				System.out.println("LEVEL 1 MOVE");
				level1Move();
			}
		}
		
	}
	
	private void level6Move() {

		//[start] CAPTURE	

		Piece pieceWithBestCapture = null;
		int[] moveLocationCapture = null;
		int highestCaptureValue = 0, lowestPieceWithKillValue = 0;
		if(piecesWithKill().size() > 0)  
			for(Piece piece : piecesWithKill()) 
				for(int[] possibleMoveLocation : killableMoves(piece)) 
					if ((Piece.getTotalPieceValue(player) > Piece.getTotalPieceValue(GameData.player.PLAYER_1) ? Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= piece.getPieceValue() : Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() > piece.getPieceValue()) || !canBeKilled(piece, possibleMoveLocation)  || piece.getType() == Piece.type.PAWN) 
							 if(Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= highestCaptureValue && (piece.getPieceValue() < lowestPieceWithKillValue || pieceWithBestCapture == null)){
								pieceWithBestCapture = piece;
								lowestPieceWithKillValue = piece.getPieceValue();
								moveLocationCapture = possibleMoveLocation;
								highestCaptureValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							}
	//	[end] CAPTURE
	
		//[start] SAVE
		
		Piece pieceToMoveForSave = null;
		int[] moveLocationSave = null;
		int[] ogPieceLocation = null;
		int highestSaveValue = 0, lowestSacrificeValue = 1000;
		
		for(Piece piece : pieces) {
			
			if (!isSafe(piece) && piece.getType() != Piece.type.PAWN && piece.getPieceValue() >= highestSaveValue) {
				
				System.out.println("SAVINGGG a " + piece.getType());
//--------------------------------------------------------------------------------------------------------------------------
				//[start]SAVE BY MOVING PIECE

				int highestCaptureWithSaveValue = 0;
				System.out.println("save with 1 piece");
				for(int[] possibleMoveLocation : piece.getPossibleMovesInCheck())
					if (isSafe(piece, possibleMoveLocation)) {
						if(Tile.isOccupiedByOpponent(possibleMoveLocation[0], possibleMoveLocation[1], player) && Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() > highestCaptureWithSaveValue) {
							pieceToMoveForSave = piece;
							moveLocationSave = possibleMoveLocation;
							highestSaveValue = piece.getPieceValue();
							highestCaptureWithSaveValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							break;
							
						}else{
						
						pieceToMoveForSave = piece;
						moveLocationSave = possibleMoveLocation;
						highestSaveValue = piece.getPieceValue();
						
						}
					}
				//[end] SAVE BY MOVING PIECE
//------------------------------------------------------------------------------------------------------------------------------------
				//[start] SAVING WITH OTHER PIECES

				if (pieceToMoveForSave == null) {
					System.out.println("saving with other pieces");
					for(Piece ownPiece : pieces) {
						if (ownPiece.getPieceValue() < piece.getPieceValue() && ownPiece.getPieceValue() < lowestSacrificeValue) {
							ogPieceLocation = new int[] {ownPiece.getRow(), ownPiece.getColumn()};
							for(int[] location : ownPiece.getPossibleMovesInCheck()) {
								ownPiece.setLocation(location);
								if (isSafe(piece)){
									pieceToMoveForSave = ownPiece;
									moveLocationSave = location;
									highestSaveValue = piece.getPieceValue();
									lowestSacrificeValue = ownPiece.getPieceValue();
								}
								ownPiece.setLocation(ogPieceLocation);
							}
						}
					}	
				}	
				//[end] SAVING WITH OTHER PIECES
			}
		}
				
	//[end] SAVE

		if (highestCaptureValue >= highestSaveValue) {
			pieceToMove = pieceWithBestCapture;
			randomLocation = moveLocationCapture;
		}else{
			pieceToMove = pieceToMoveForSave;
			randomLocation = moveLocationSave;
		}
		
		//[start] NON-CAPTURE-SAVE MOVES
		if (pieceToMove == null && randomLocation == null) {

			
			ArrayList<Piece> piecesWithSafeCheckMoves = piecesWithSafeCheckMoves(), piecesWithSafeMoves = piecesWithSafeMoves(), piecesWithNoKill = piecesWithNoKill();
			ArrayList<int[]> safeCheckMoves, safeMoves, nonKillableMoves;
			int piecesWithSafeCheckMovesSize = piecesWithSafeCheckMoves.size(), piecesWithSafeMovesSize = piecesWithSafeMoves.size(), piecesWithNoKillSize = piecesWithNoKill.size();
			
			if (piecesWithSafeCheckMovesSize > 0) {
			
				System.out.println("CHECK MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeCheckMoves);
				pieceToMove = piecesWithSafeCheckMoves.get(randomIndexPiece);
				
				safeCheckMoves = safeCheckMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				System.out.println("Amount of pieces that can safe check: " + piecesWithSafeCheckMovesSize);
				System.out.println("Amount of moves piece can make to check: " + safeCheckMoves.size());
				randomIndexMove = randomMoveIndex(safeCheckMoves);
				randomLocation = safeCheckMoves.get(randomIndexMove);
				
			}else if(piecesWithSafeMovesSize > 0) {
				
				System.out.println("SAFE MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeMoves);
				pieceToMove = piecesWithSafeMoves.get(randomIndexPiece);
				
				safeMoves = safeMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(safeMoves);
				randomLocation = safeMoves.get(randomIndexMove);
				
			}else if (piecesWithNoKillSize > 0){
				
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithNoKill);
				pieceToMove = piecesWithNoKill.get(randomIndexPiece);
				
				nonKillableMoves = nonKillableMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(nonKillableMoves);
				randomLocation = nonKillableMoves.get(randomIndexMove);
				
			}else{
				System.out.println("RANDOM MOVE");
				level1Move();
			}
		}
	//[end] NON-CAPTURE-SAVE MOVES

	}
	
	private void level7Move() {
		
		int moveSum = 0;
		
		for(Piece piece : pieces) {
			int[] ogLocation = {piece.getRow(), piece.getColumn()};
			for(int[] move : piece.getPossibleMovesInCheck()) {
				if (willOpponentBeInCheck(piece, move)) {
					piece.setLocation(move);
					for(Piece opponent : Game.player1Pieces) 
						moveSum+=opponent.getPossibleMovesInCheck().size();
				
					if (moveSum == 0) {
						pieceToMove = piece;
						randomLocation = move;
						piece.setLocation(ogLocation);
						System.out.println("doing this one checkamteing");
						return;
					}
					piece.setLocation(ogLocation);
				}
			}
			moveSum = 0;
		}
		
	/*	System.out.println(canBeCheckmated());
		if (canBeCheckmated()) {
			System.out.println("can be checkmated");
			Piece pieceToSaveCheckmate = null;
			int[] placeToMoveForSaveCheckmate = null;
			int lowestPieceValue = 1000;
			for(Piece own : pieces) {
				int[] ogLocation = {own.getRow(), own.getColumn()};
				if(own.getPossibleMovesInCheck().size() > 0 && own.getPieceValue() < lowestPieceValue) {
					for(int[] ownMove : own.getPossibleMovesInCheck()) {
						own.setLocation(ownMove);
						if (!canBeCheckmated()) {
							lowestPieceValue = own.getPieceValue();
							pieceToSaveCheckmate = own;
							placeToMoveForSaveCheckmate = ownMove;
							System.out.println("SAVING FROM CHECKAMATEEEE");
							if (own.getType() == Piece.type.PAWN) {
								pieceToMove = pieceToSaveCheckmate;
								randomLocation = placeToMoveForSaveCheckmate;
								own.setLocation(ogLocation);
								return;
							}
						}
					}
				}
				own.setLocation(ogLocation);
			}
			if (pieceToSaveCheckmate != null && placeToMoveForSaveCheckmate != null) {
				pieceToMove= pieceToSaveCheckmate;
				randomLocation = placeToMoveForSaveCheckmate;
				return;
			}
		}
		*/
		Piece pieceWithBestCapture = null;
		int[] moveLocationCapture = null;
		int highestCaptureValue = 0, lowestPieceWithKillValue = 0;
		if(piecesWithKill().size() > 0)  
			for(Piece piece : piecesWithKill()) 
				for(int[] possibleMoveLocation : killableMoves(piece)) 
					if ((Piece.getTotalPieceValue(player) > Piece.getTotalPieceValue(GameData.player.PLAYER_1)+100 ? Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= piece.getPieceValue() : Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() > piece.getPieceValue()) || !canBeKilled(piece, possibleMoveLocation)  || piece.getType() == Piece.type.PAWN) 
							 if(Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= highestCaptureValue && (piece.getPieceValue() < lowestPieceWithKillValue || pieceWithBestCapture == null)){
								pieceWithBestCapture = piece;
								lowestPieceWithKillValue = piece.getPieceValue();
								moveLocationCapture = possibleMoveLocation;
								highestCaptureValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							}
							
		Piece pieceToMoveForSave = null;
		int[] moveLocationSave = null;
		int[] ogPieceLocation = null;
		int highestSaveValue = 0, lowestSacrificeValue = 1000;
		
		for(Piece piece : pieces) {
			
			if (!isSafe(piece) && piece.getType() != Piece.type.PAWN && piece.getPieceValue() >= highestSaveValue) {
				
				System.out.println("SAVINGGG a " + piece.getType());
//--------------------------------------------------------------------------------------------------------------------------
				//SAVE BY MOVING PIECE

				int highestCaptureWithSaveValue = 0;
				System.out.println("save with 1 piece");
				for(int[] possibleMoveLocation : piece.getPossibleMovesInCheck())
					if (isSafe(piece, possibleMoveLocation)) {
						if(Tile.isOccupiedByOpponent(possibleMoveLocation[0], possibleMoveLocation[1], player) && Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() > highestCaptureWithSaveValue) {
							pieceToMoveForSave = piece;
							moveLocationSave = possibleMoveLocation;
							highestSaveValue = piece.getPieceValue();
							highestCaptureWithSaveValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							break;
							
						}else{
						
						pieceToMoveForSave = piece;
						moveLocationSave = possibleMoveLocation;
						highestSaveValue = piece.getPieceValue();
						
						}
			}
//------------------------------------------------------------------------------------------------------------------------------------
				//SAVING WITH OTHER PIECES

				if (pieceToMoveForSave == null) {
					System.out.println("saving with other pieces");
					for(Piece ownPiece : pieces) {
						if (ownPiece.getPieceValue() < piece.getPieceValue() && ownPiece.getPieceValue() < lowestSacrificeValue) {
							ogPieceLocation = new int[] {ownPiece.getRow(), ownPiece.getColumn()};
							for(int[] location : ownPiece.getPossibleMovesInCheck()) {
								ownPiece.setLocation(location);
								if (isSafe(piece)){
									pieceToMoveForSave = ownPiece;
									moveLocationSave = location;
									highestSaveValue = piece.getPieceValue();
									lowestSacrificeValue = ownPiece.getPieceValue();
								}
								ownPiece.setLocation(ogPieceLocation);
							}
						}
					}	
				}	
			}
		}


		if (highestCaptureValue >= highestSaveValue) {
			pieceToMove = pieceWithBestCapture;
			randomLocation = moveLocationCapture;
		}else{
			pieceToMove = pieceToMoveForSave;
			randomLocation = moveLocationSave;
		}
		
		//[start] NON-CAPTURE-SAVE MOVES
		if (pieceToMove == null && randomLocation == null) {

			
			ArrayList<Piece> piecesWithSafeCheckMoves = piecesWithSafeCheckMoves(), piecesWithSafeMoves = piecesWithSafeMoves(), piecesWithNoKill = piecesWithNoKill();
			ArrayList<int[]> safeCheckMoves, safeMoves, nonKillableMoves;
			int piecesWithSafeCheckMovesSize = piecesWithSafeCheckMoves.size(), piecesWithSafeMovesSize = piecesWithSafeMoves.size(), piecesWithNoKillSize = piecesWithNoKill.size();
			
			if (piecesWithSafeCheckMovesSize > 0) {
			
				System.out.println("CHECK MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeCheckMoves);
				pieceToMove = piecesWithSafeCheckMoves.get(randomIndexPiece);
				
				safeCheckMoves = safeCheckMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				System.out.println("Amount of pieces that can safe check: " + piecesWithSafeCheckMovesSize);
				System.out.println("Amount of moves piece can make to check: " + safeCheckMoves.size());
				randomIndexMove = randomMoveIndex(safeCheckMoves);
				randomLocation = safeCheckMoves.get(randomIndexMove);
				
			}else if(piecesWithSafeMovesSize > 0) {
				
				System.out.println("SAFE MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeMoves);
				pieceToMove = piecesWithSafeMoves.get(randomIndexPiece);
				
				safeMoves = safeMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(safeMoves);
				randomLocation = safeMoves.get(randomIndexMove);
				
			}else if (piecesWithNoKillSize > 0){
				
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithNoKill);
				pieceToMove = piecesWithNoKill.get(randomIndexPiece);
				
				nonKillableMoves = nonKillableMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(nonKillableMoves);
				randomLocation = nonKillableMoves.get(randomIndexMove);
				
			}else{
				System.out.println("RANDOM MOVE");
				level1Move();
			}
		}
	//[end] NON-CAPTURE-SAVE MOVES
		
	}
	
	private void level8Move() {
		
	int moveSum = 0;
		
		for(Piece piece : pieces) {
			int[] ogLocation = {piece.getRow(), piece.getColumn()};
			for(int[] move : piece.getPossibleMovesInCheck()) {
				if (willOpponentBeInCheck(piece, move)) {
					piece.setLocation(move);
					for(Piece opponent : Game.player1Pieces) 
						moveSum+=opponent.getPossibleMovesInCheck().size();
				
					if (moveSum == 0) {
						pieceToMove = piece;
						randomLocation = move;
						piece.setLocation(ogLocation);
						System.out.println("doing this one checkamteing");
						return;
					}
					piece.setLocation(ogLocation);
				}
			}
			moveSum = 0;
		}
		
	/*	System.out.println(canBeCheckmated());
		if (canBeCheckmated()) {
			System.out.println("can be checkmated");
			Piece pieceToSaveCheckmate = null;
			int[] placeToMoveForSaveCheckmate = null;
			int lowestPieceValue = 1000;
			for(Piece own : pieces) {
				int[] ogLocation = {own.getRow(), own.getColumn()};
				if(own.getPossibleMovesInCheck().size() > 0 && own.getPieceValue() < lowestPieceValue) {
					for(int[] ownMove : own.getPossibleMovesInCheck()) {
						own.setLocation(ownMove);
						if (!canBeCheckmated()) {
							lowestPieceValue = own.getPieceValue();
							pieceToSaveCheckmate = own;
							placeToMoveForSaveCheckmate = ownMove;
							System.out.println("SAVING FROM CHECKAMATEEEE");
							if (own.getType() == Piece.type.PAWN) {
								pieceToMove = pieceToSaveCheckmate;
								randomLocation = placeToMoveForSaveCheckmate;
								own.setLocation(ogLocation);
								return;
							}
						}
					}
				}
				own.setLocation(ogLocation);
			}
			if (pieceToSaveCheckmate != null && placeToMoveForSaveCheckmate != null) {
				pieceToMove= pieceToSaveCheckmate;
				randomLocation = placeToMoveForSaveCheckmate;
				return;
			}
		}
		*/
		Piece pieceWithBestCapture = null;
		int[] moveLocationCapture = null;
		int highestCaptureValue = 0, lowestPieceWithKillValue = 0;
		if(piecesWithKill().size() > 0)  
			for(Piece piece : piecesWithKill()) 
				for(int[] possibleMoveLocation : killableMoves(piece)) 
					if ((Piece.getTotalPieceValue(player) > Piece.getTotalPieceValue(GameData.player.PLAYER_1)+100 ? Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= piece.getPieceValue() : Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() > piece.getPieceValue()) || !canBeKilled(piece, possibleMoveLocation)  || piece.getType() == Piece.type.PAWN) 
							 if(Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() >= highestCaptureValue && (piece.getPieceValue() < lowestPieceWithKillValue || pieceWithBestCapture == null)){
								pieceWithBestCapture = piece;
								lowestPieceWithKillValue = piece.getPieceValue();
								moveLocationCapture = possibleMoveLocation;
								highestCaptureValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							}
							
		Piece pieceToMoveForSave = null;
		int[] moveLocationSave = null;
		int[] ogPieceLocation = null;
		int highestSaveValue = 0, lowestSacrificeValue = 1000;
		
		for(Piece piece : pieces) {
			
			if (!isSafe(piece) && piece.getType() != Piece.type.PAWN && piece.getPieceValue() >= highestSaveValue) {
				
				System.out.println("SAVINGGG a " + piece.getType());
//--------------------------------------------------------------------------------------------------------------------------
				//SAVE BY MOVING PIECE

				int highestCaptureWithSaveValue = 0;
				System.out.println("save with 1 piece");
				for(int[] possibleMoveLocation : piece.getPossibleMovesInCheck())
					if (isSafe(piece, possibleMoveLocation)) {
						if(Tile.isOccupiedByOpponent(possibleMoveLocation[0], possibleMoveLocation[1], player) && Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue() > highestCaptureWithSaveValue) {
							pieceToMoveForSave = piece;
							moveLocationSave = possibleMoveLocation;
							highestSaveValue = piece.getPieceValue();
							highestCaptureWithSaveValue = Tile.getPiece(possibleMoveLocation[0], possibleMoveLocation[1]).getPieceValue();
							break;
							
						}else{
						
						pieceToMoveForSave = piece;
						moveLocationSave = possibleMoveLocation;
						highestSaveValue = piece.getPieceValue();
						
						}
			}
//------------------------------------------------------------------------------------------------------------------------------------
				//SAVING WITH OTHER PIECES

				if (pieceToMoveForSave == null) {
					System.out.println("saving with other pieces");
					for(Piece ownPiece : pieces) {
						if (ownPiece.getPieceValue() < piece.getPieceValue() && ownPiece.getPieceValue() < lowestSacrificeValue) {
							ogPieceLocation = new int[] {ownPiece.getRow(), ownPiece.getColumn()};
							for(int[] location : ownPiece.getPossibleMovesInCheck()) {
								ownPiece.setLocation(location);
								if (isSafe(piece)){
									pieceToMoveForSave = ownPiece;
									moveLocationSave = location;
									highestSaveValue = piece.getPieceValue();
									lowestSacrificeValue = ownPiece.getPieceValue();
								}
								ownPiece.setLocation(ogPieceLocation);
							}
						}
					}	
				}	
			}
		}


		if (highestCaptureValue >= highestSaveValue) {
			pieceToMove = pieceWithBestCapture;
			randomLocation = moveLocationCapture;
		}else{
			pieceToMove = pieceToMoveForSave;
			randomLocation = moveLocationSave;
		}
		
		//[start] NON-CAPTURE-SAVE MOVES
		if (pieceToMove == null && randomLocation == null) {

			ArrayList<Piece> piecesWithSafeCheckMoves = piecesWithSafeCheckMoves(), piecesWithAggressiveMoves = null,
					piecesWithSafeMoves = null, piecesWithNoKill = null;
			
			int piecesWithSafeCheckMovesSize = piecesWithSafeCheckMoves.size(), piecesWithAggressiveMovesSize = -1,
					piecesWithSafeMovesSize = -1, piecesWithNoKillSize = -1;
			
			if (piecesWithSafeCheckMovesSize == 0) {
				piecesWithAggressiveMoves = piecesWithAggressiveMoves();
				piecesWithAggressiveMovesSize = piecesWithAggressiveMoves.size();
			}
			
			if (piecesWithAggressiveMovesSize == 0) {
				piecesWithSafeMoves = piecesWithSafeMoves();
				piecesWithSafeMovesSize = piecesWithSafeMoves.size();
			}
			
			if (piecesWithSafeMovesSize == 0) {
				piecesWithNoKill = piecesWithNoKill();
				piecesWithNoKillSize = piecesWithNoKill.size();
			}
			
			ArrayList<int[]> safeCheckMoves, aggressiveMoves, safeMoves, nonKillableMoves;
			
		
			if (piecesWithSafeCheckMovesSize > 0) {
			
				System.out.println("CHECK MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeCheckMoves);
				pieceToMove = piecesWithSafeCheckMoves.get(randomIndexPiece);
				
				safeCheckMoves = safeCheckMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				System.out.println("Amount of pieces that can safe check: " + piecesWithSafeCheckMovesSize);
				System.out.println("Amount of moves piece can make to check: " + safeCheckMoves.size());
				randomIndexMove = randomMoveIndex(safeCheckMoves);
				randomLocation = safeCheckMoves.get(randomIndexMove);
				
			}else if(piecesWithAggressiveMovesSize > 0) {
				
				System.out.println("AGGRESSIVE MOVE");
				randomIndexPiece = randomPieceIndex(piecesWithAggressiveMoves);
				pieceToMove = piecesWithAggressiveMoves.get(randomIndexPiece);
				
				aggressiveMoves = aggressiveMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(aggressiveMoves);
				randomLocation = aggressiveMoves.get(randomIndexMove);
				
			}else if(piecesWithSafeMovesSize > 0) {
				
				System.out.println("SAFE MOVE");
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithSafeMoves);
				pieceToMove = piecesWithSafeMoves.get(randomIndexPiece);
				
				safeMoves = safeMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(safeMoves);
				randomLocation = safeMoves.get(randomIndexMove);
				
			}else if (piecesWithNoKillSize > 0){
				
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF PIECES
				randomIndexPiece = randomPieceIndex(piecesWithNoKill);
				pieceToMove = piecesWithNoKill.get(randomIndexPiece);
				
				nonKillableMoves = nonKillableMoves(pieceToMove);
				//PICKS A RANDOM NUMBER BASED ON ARRAYLIST SIZE OF MOVES
				randomIndexMove = randomMoveIndex(nonKillableMoves);
				randomLocation = nonKillableMoves.get(randomIndexMove);
				
			}else{
				System.out.println("RANDOM MOVE");
				level1Move();
			}
		}
	//[end] NON-CAPTURE-SAVE MOVES
		
		
	}
	
	private ArrayList<Piece> piecesWithKill() {
		
		ArrayList<Piece> piecesWithKill = new ArrayList<Piece>();
		
		//ADDS PIECES THAT CAN KILL TO ARRAYLIST
		for(int z = 0; z < pieces.size(); z++)
			if(killableMoves(pieces.get(z)).size() > 0)
				piecesWithKill.add(pieces.get(z));
		
		return piecesWithKill;
		
	}
	
	private ArrayList<Piece> piecesWithNoKill() {
		
		ArrayList<Piece> piecesWithNoKill = new ArrayList<Piece>();
		
		//ADDS PIECES THAT CAN KILL TO ARRAYLIST
		for(int z = 0; z < pieces.size(); z++)
			if(nonKillableMoves(pieces.get(z)).size() > 0)
				piecesWithNoKill.add(pieces.get(z));
		
		return piecesWithNoKill;
		
	}	
	
	private ArrayList<int[]> killableMoves(Piece piece) {
		
		ArrayList<int[]> killableMoves = new ArrayList<int[]>();
		
		for(int z = 0; z < piece.getPossibleMovesInCheck().size(); z++)
			if(Tile.isOccupiedByOpponent(piece.getPossibleMovesInCheck().get(z)[0], piece.getPossibleMovesInCheck().get(z)[1], player))
				killableMoves.add(piece.getPossibleMovesInCheck().get(z));
			
		return killableMoves;
		
	}
	
	private ArrayList<int[]> nonKillableMoves(Piece piece) {
		
		ArrayList<int[]> nonKillableMoves = new ArrayList<int[]>();
		
		for(int z = 0; z < piece.getPossibleMovesInCheck().size(); z++)
			if(!Tile.isOccupiedByOpponent(piece.getPossibleMovesInCheck().get(z)[0], piece.getPossibleMovesInCheck().get(z)[1], player))
				nonKillableMoves.add(piece.getPossibleMovesInCheck().get(z));
			
		return nonKillableMoves;
		
	}
	
	public ArrayList<int[]> safeMoves(Piece piece){
		
		ArrayList<int[]> safeMoves = new ArrayList<int[]>();
		int[] ogLocation = {piece.getRow(), piece.getColumn()};
		
		for (int[] move : piece.getPossibleMovesInCheck()) { 
			piece.setLocation(move);
			safeMoves.add(move);
			for(Piece own : pieces) {
				if (own.getType() != Piece.type.PAWN && !isSafe(own)) {
					safeMoves.remove(move);
					break;
				}
			}
		}

		piece.setLocation(ogLocation);
		return safeMoves;
				
	}
	
	public ArrayList<Piece> piecesWithSafeMoves(){
		
		ArrayList<Piece> piecesWithSafeMoves = new ArrayList<Piece>();
		
		for(Piece piece : pieces)
			if(safeMoves(piece).size() > 0)
				piecesWithSafeMoves.add(piece);
		
		return piecesWithSafeMoves;
	}
	
	public ArrayList<int[]> safeCheckMoves(Piece piece){
		
		ArrayList<int[]> safeCheckMoves = new ArrayList<int[]>();
		
		for(int[] moveLocation : piece.getPossibleMovesInCheck())
			if (willOpponentBeInCheck(piece, moveLocation) && isSafe(piece, moveLocation)) 
				safeCheckMoves.add(moveLocation);
		
		return safeCheckMoves;
	}
	
	public ArrayList<Piece> piecesWithSafeCheckMoves(){
		
		ArrayList<Piece> piecesWithSafeCheckMoves = new ArrayList<Piece>();
		
		for(Piece piece : pieces)
			if (safeCheckMoves(piece).size() > 0)
				piecesWithSafeCheckMoves.add(piece);
				
		return piecesWithSafeCheckMoves;
	}
	
	public ArrayList<int[]> aggressiveMoves(Piece piece){
		
		ArrayList<int[]> aggressiveMoves = new ArrayList<int[]>();
		
		for(int[] move : piece.getPossibleMovesInCheck()) {
			int[] ogLocation = {piece.getRow(), piece.getColumn()};
			piece.setLocation(move);
			opponentLoop:
			for(Piece opponent : Game.player1Pieces) {
				if(opponent.getType() != Piece.type.PAWN && canOpponentBeKilled(opponent)) {
					for(Piece own : pieces) {
						if(!isSafe(own)) {
							piece.setLocation(ogLocation);
							break opponentLoop;
						}
					}
					aggressiveMoves.add(move);
				}
			}
			piece.setLocation(ogLocation);
		}
		
		return aggressiveMoves;
		
	}
	
	public ArrayList<Piece> piecesWithAggressiveMoves(){

		ArrayList<Piece> piecesWithAggressiveMoves = new ArrayList<Piece>();
		
		for(Piece piece : pieces)
			if(aggressiveMoves(piece).size() > 0)
				piecesWithAggressiveMoves.add(piece);
			
		return piecesWithAggressiveMoves;
		
	}
	
	private boolean canBeKilled(Piece piece, int[] moveLocation) {
		
		int[] ogLocation = {piece.getRow(), piece.getColumn()};
		
		piece.setLocation(moveLocation);

		for(Piece opponentPiece : Game.player1Pieces)    
			for(int[] opponentMoveLocation : opponentPiece.getPossibleMovesInCheck()) 
				if (piece.getRow() == opponentMoveLocation[0] && piece.getColumn() == opponentMoveLocation[1]) {
					piece.setLocation(ogLocation);
					return true;
				}
		
		piece.setLocation(ogLocation);
		return false;
		
	}
	
	private boolean canBeKilled(Piece piece) {
		
		for(Piece opponentPiece : Game.player1Pieces)    
			for(int[] opponentMoveLocation : opponentPiece.getPossibleMovesInCheck()) 
				if (piece.getRow() == opponentMoveLocation[0] && piece.getColumn() == opponentMoveLocation[1]) 
					return true;	
	
		return false;
		
	}
	
	//Piece is piece to be killed
	private boolean canOpponentBeKilled(Piece piece) {
		
		for(Piece own : pieces)    
			for(int[] ownMove: own.getPossibleMovesInCheck()) 
				if (piece.getRow() == ownMove[0] && piece.getColumn() == ownMove[1]) 
					return true;	
	
		return false;
		
	}
	
	//Piece is piece that can be killed
	public boolean canOpponentBeKilled(Piece piece, int[] location) {
		
		int[] ogLocation = {piece.getRow(), piece.getColumn()};
		piece.setLocation(location);
		
		for(Piece own : pieces)    
			for(int[] ownMove : own.getPossibleMovesInCheck()) 
				if (piece.getRow() == ownMove[0] && piece.getColumn() == ownMove[1]) {
					piece.setLocation(ogLocation);
					return true;	
				}
	
		piece.setLocation(ogLocation);
		return false;
		
	}
	
	public boolean isSafe(Piece piece) {
		
		if (!canBeKilled(piece))
			return true;
					
		for (Piece opponent : Game.player1Pieces) 
			for(int[] opponentMove : opponent.getPossibleMovesInCheck()) 
				if (piece.getRow() == opponentMove[0] && piece.getColumn() == opponentMove[1]) 
					if (opponent.getPieceValue() < piece.getPieceValue())
						return false;
					
		int[] ogOpponentLocation, ogOwnLocation;
		
		for(Piece opponent : Game.player1Pieces) { 
			ogOpponentLocation = new int[] {opponent.getRow(), opponent.getColumn()};
			for(int[] opponentMove : opponent.getPossibleMovesInCheck()) {
				opponent.setLocation(opponentMove);
				if (piece.getRow() == opponentMove[0] && piece.getColumn() == opponentMove[1]  && (opponent.getPieceValue() > piece.getPieceValue() || !canBeKilled(opponent, opponentMove))) {
					for(Piece own : pieces) {
						ogOwnLocation = new int[] {own.getRow(), own.getColumn()};
						for(int[] ownMove : own.getPossibleMovesInCheck()) {
							own.setLocation(ownMove);
							if(ownMove[0] == opponentMove[0] && ownMove[1] == opponentMove[1] && (own.getType() == Piece.type.PAWN || (own.getPieceValue() < piece.getPieceValue() && own.getPieceValue() < opponent.getPieceValue()) || !canBeKilled(own, ownMove))) {
								opponent.setLocation(ogOpponentLocation);
								own.setLocation(ogOwnLocation);
								return true;
							}
						}
						own.setLocation(ogOwnLocation);
					}
				}
			}
			opponent.setLocation(ogOpponentLocation);
		}
		
		return false;
				
	}
	
	public boolean isSafe(Piece piece, int[] moveLocation) {
		
		if (!canBeKilled(piece, moveLocation))
			return true;
		
		int[] ogLocation = {piece.getRow(), piece.getColumn()};
		piece.setLocation(moveLocation);
		
		for (Piece opponent : Game.player1Pieces) 
			for(int[] opponentMove : opponent.getPossibleMovesInCheck()) 
				if (piece.getRow() == opponentMove[0] && piece.getColumn() == opponentMove[1]) 
					if (opponent.getPieceValue() < piece.getPieceValue()) {
						piece.setLocation(ogLocation);
						return false;
					}
				
		int[] ogOpponentLocation, ogOwnLocation;
		
		for(Piece opponent : Game.player1Pieces) { 
			ogOpponentLocation = new int[] {opponent.getRow(), opponent.getColumn()};
			for(int[] opponentMove : opponent.getPossibleMovesInCheck()) {
				opponent.setLocation(opponentMove);
				if (piece.getRow() == opponentMove[0] && piece.getColumn() == opponentMove[1] && (opponent.getPieceValue() > piece.getPieceValue() || !canBeKilled(opponent, opponentMove))) {
					for(Piece own : pieces) {
						ogOwnLocation = new int[] {own.getRow(), own.getColumn()};
						for(int[] ownMove : own.getPossibleMovesInCheck()) {
							own.setLocation(ownMove);
							if(ownMove[0] == opponentMove[0] && ownMove[1] == opponentMove[1] && (own.getType() == Piece.type.PAWN || (own.getPieceValue() < piece.getPieceValue() && own.getPieceValue() < opponent.getPieceValue()) || !canBeKilled(own, ownMove))) {
								opponent.setLocation(ogOpponentLocation);
								own.setLocation(ogOwnLocation);
								piece.setLocation(ogLocation);
								return true;
							}
						}
						own.setLocation(ogOwnLocation);
					}
				}
			}
			opponent.setLocation(ogOpponentLocation);
		}
		
		piece.setLocation(ogLocation);
		return false;
				
	}
	
	public boolean willOpponentBeInCheck(Piece piece, int[] moveLocation) {
		
		int[] ogLocation = new int[] {piece.getRow(), piece.getColumn()};
		piece.setLocation(moveLocation);
		
		for(Piece opponentPiece : Game.player1Pieces)
			if(opponentPiece.getType() == Piece.type.KING)
				if(opponentPiece.isInCheck()) {
					piece.setLocation(ogLocation);
					return true;
				}
		
		piece.setLocation(ogLocation);
		return false;

	}
	
	private boolean canBeCheckmated() {
		
	/*	int[] ogLocation = null;
		int moveSum = 0;
		for(Piece opponent : Game.player1Pieces) { 
			ogLocation = new int[] {opponent.getRow(), opponent.getColumn()};
			for(int[] move : opponent.getPossibleMovesInCheck()) {
				opponent.setLocation(move);
				for(Piece own : pieces) 
					moveSum+=own.getPossibleMovesInCheck().size();
				if (moveSum == 0) { 
					opponent.setLocation(ogLocation);
					return true;
				}
				moveSum = 0;
			}
			opponent.setLocation(ogLocation);
		}
		
		return false;*/
		
		for (Piece opponent : Game.player1Pieces) {
			int[] ogLocation = {opponent.getRow(), opponent.getColumn()};
			for(int[] move : opponent.getPossibleMovesInCheck()) {
				opponent.setLocation(move); 
				if (Piece.getKing(player).isInCheck()) {
					System.out.println("got to this");
					int moveSum	= 0;
					for(Piece own : pieces)
						moveSum+=own.getPossibleMovesInCheck().size();
					if (moveSum == 0) {
						opponent.setLocation(ogLocation);
						return true;
					}
				}
			}
			opponent.setLocation(ogLocation);
		}
		return false;
	}
	
	private int randomPieceIndex(ArrayList<Piece> list) {
		
		randomIndexPiece = random.nextInt(list.size());
		
		while(list.get(randomIndexPiece).getPossibleMovesInCheck().size() <= 0) 
			randomIndexPiece = random.nextInt(pieces.size());
			
		return randomIndexPiece;
		
	}
	
	private int randomMoveIndex(ArrayList<int[]> list) {
		
		return random.nextInt(list.size());
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
 
}