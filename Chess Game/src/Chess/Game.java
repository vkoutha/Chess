package Chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Game implements ActionListener, MouseListener, KeyListener{
	
	static Game game;
	static SecondaryFrame secondaryFrame;
	static Renderer renderer;
	static Tile[][] board = new Tile[GameData.ROWS][GameData.COLUMNS];
	static ArrayList<ArrayList<Piece>> pastPieces = new ArrayList<ArrayList<Piece>>();
	static ArrayList<Piece> player1Pieces = new ArrayList<Piece>();
	static ArrayList<Piece> player2Pieces = new ArrayList<Piece>();
	static GameData.player playerTurn = GameData.player.PLAYER_1;
	static GameData.player winner = null;
	static GameData.gameState gameState = GameData.gameState.STARTING_MENU;
	static JFrame frame, timerFrame;

	static int[] tileClicked, prevTileClicked;
	static int moveCount = 0;
	static int p1MoveCount = 0;
	static int p2MoveCount = 0;
	int moveSum = 0, victoryMessage = 0;
	long startTime = System.currentTimeMillis(), timePassed = 0;
	
	static boolean stalemate, inPromotionMenu = false;
	boolean sizeInitialized = false, loadScreenCreated = false, secondaryScreenCreated = false;
	
	AI bot;
	static Minimax ultraBot;
	DecimalFormat formatter;
	Dimension gameDimension;
	
	Clip clip;
	
	/**
	 * Constructor for Game Class, creates inital game frame, renderer class, and creates game file folder if necessary
	 */
	public Game() {
	
		Timer timer = new Timer(20, this);
		formatter = new DecimalFormat("#####.##");
		renderer = new Renderer();
		frame = new JFrame("Chess");
		
		if (!GameData.gameFileFolder.exists()) {
			System.out.println("exists");
			try {
			GameData.gameFileFolder.mkdir();
			}catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("Created");
		}
						
	    frame.setIconImage(GameData.frameIcon);
		frame.setSize(0, 0);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(renderer);
		frame.addMouseListener(this);
		frame.addKeyListener(this);
		
		start();
		timer.start();
		
	}
	
	/**
	 * Initializes board, game pieces, and AI bot
	 */
	private void start() {
		
		for(int rowNumber = 0; rowNumber < GameData.ROWS; rowNumber++) 
			for(int columnNumber = 0; columnNumber < GameData.COLUMNS; columnNumber++) 
				if(rowNumber % 2 == 0) 
					if(columnNumber % 2 == 0)
						board[rowNumber][columnNumber] = new Tile(rowNumber, columnNumber, GameData.TILE_COLOR_1_WHITE);
					else
						board[rowNumber][columnNumber] = new Tile(rowNumber, columnNumber, GameData.TILE_COLOR_2_MAROON);	
				else
					if(columnNumber % 2 == 0)
						board[rowNumber][columnNumber] = new Tile(rowNumber, columnNumber, GameData.TILE_COLOR_2_MAROON);
					else
						board[rowNumber][columnNumber] = new Tile(rowNumber, columnNumber, GameData.TILE_COLOR_1_WHITE);
				
//-------------------------------------------------------------------------------------------------------------------------------------
		//RENDER PLAYER 1 PIECES
		for(int columnNumber = 0; columnNumber < GameData.COLUMNS; columnNumber++)
			player1Pieces.add(new Pawn(GameData.PLAYER_1_STARTING_PAWN_ROW, columnNumber, GameData.player.PLAYER_1));
				
		player1Pieces.add(new Rook(GameData.ROWS-1, 0, GameData.player.PLAYER_1));
		player1Pieces.add(new Rook(GameData.ROWS-1, GameData.COLUMNS-1, GameData.player.PLAYER_1));
		
		player1Pieces.add(new Knight(GameData.ROWS-1, 1, GameData.player.PLAYER_1));
		player1Pieces.add(new Knight(GameData.ROWS-1, GameData.COLUMNS-2, GameData.player.PLAYER_1));
		
		player1Pieces.add(new Bishop(GameData.ROWS-1, 2, GameData.player.PLAYER_1));
		player1Pieces.add(new Bishop(GameData.ROWS-1, GameData.COLUMNS-3, GameData.player.PLAYER_1));
		
		player1Pieces.add(new Queen(GameData.ROWS-1, 3, GameData.player.PLAYER_1));
		player1Pieces.add(new King(GameData.ROWS-1, GameData.COLUMNS-4, GameData.player.PLAYER_1));
		
//------------------------------------------------------------------------------------------------------------------------------------
		
		//RENDER PLAYER 2 PIECES
		for(int columnNumber = 0; columnNumber < GameData.COLUMNS; columnNumber++)
			player2Pieces.add(new Pawn(GameData.PLAYER_2_STARTING_PAWN_ROW, columnNumber, GameData.player.PLAYER_2));
			
		player2Pieces.add(new Rook(0, 0, GameData.player.PLAYER_2));
		player2Pieces.add(new Rook(0, GameData.COLUMNS-1, GameData.player.PLAYER_2));
		
		player2Pieces.add(new Knight(0, 1, GameData.player.PLAYER_2));
		player2Pieces.add(new Knight(0, GameData.COLUMNS-2, GameData.player.PLAYER_2));
		
		player2Pieces.add(new Bishop(0, 2, GameData.player.PLAYER_2));
		player2Pieces.add(new Bishop(0, GameData.COLUMNS-3, GameData.player.PLAYER_2));
		
		player2Pieces.add(new Queen(0, 3, GameData.player.PLAYER_2));
		player2Pieces.add(new King(0, GameData.COLUMNS-4, GameData.player.PLAYER_2));
		
//---------------------------------------------------------------------------------------------------------------------------------------
	
		bot = new AI();
		storeBoardData();
		
	}
	
	/**
	 * Renders all components seen on starting menu screen and in-game screen (Tiles, pieces, words, etc.)
	 * @param g Graphics object
	 */
	public void render(Graphics g) {
		
		switch(gameState) {
		
		case STARTING_MENU:
			
			frame.setSize(GameData.WIDTH + GameData.WIDTH_COMPENSATOR-40, 200);
			frame.setLocationRelativeTo(null);

			g.setColor(GameData.MENU_COLOR);
			g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Bell MT" , Font.BOLD, 40));
			g.drawString("Press C to Create a New Game", GameData.WIDTH/2-350, (frame.getHeight()/2)-35);
			g.drawString("Press L to Load a Previous Game",  GameData.WIDTH/2-375, (frame.getHeight()/2)+25);
			g.drawImage(new ImageIcon("queen.png").getImage(), GameData.WIDTH/2, 200, null);
			break;
						
		case IN_GAME:
					
			if(!sizeInitialized) {
			//	frame.setResizable(true);
				frame.setPreferredSize(new Dimension(GameData.WIDTH + GameData.WIDTH_COMPENSATOR-5, GameData.HEIGHT+GameData.HEIGHT_COMPENSATOR));
				frame.pack();
//				frame.setSize(GameData.WIDTH + GameData.WIDTH_COMPENSATOR-5, GameData.HEIGHT+GameData.HEIGHT_COMPENSATOR);
				frame.setResizable(true);
				Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocationRelativeTo(null);
				frame.setLocation(screenDimension.width/2-frame.getSize().width/2, screenDimension.height/2-frame.getSize().height/2);
				//frame.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/5+90, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4-220);
				sizeInitialized = true;
				gameDimension = frame.getSize();
			//	frame.pack();
				updateBoardSize();

			}
			

			if(secondaryScreenCreated == false) {
    			secondaryFrame = new SecondaryFrame();
				secondaryScreenCreated = true;
			}
			
			//BOARD RENDERING
			for(int rowNumber = 0; rowNumber < GameData.ROWS; rowNumber++) 					
				for(int columnNumber = 0; columnNumber < GameData.COLUMNS; columnNumber++) { 
						board[rowNumber][columnNumber].render(g);
						if (columnNumber == 0) {
							g.setColor(Color.BLACK);
							g.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
							g.drawString("" + (GameData.ROWS-rowNumber), 5, (rowNumber*GameData.TILE_HEIGHT)+18);
						}
						
						if (rowNumber == 7) {
							g.setColor(Color.BLACK);
							g.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
							g.drawString("" + (char)(97+columnNumber), ((columnNumber+1)*GameData.TILE_WIDTH)-17 , (rowNumber*GameData.TILE_HEIGHT)+18);
						}
				}
			
			for(int z = 0; z < player1Pieces.size(); z++)
				player1Pieces.get(z).render(g);
			
			for(int z = 0; z < player2Pieces.size(); z++)
				player2Pieces.get(z).render(g);
			
			break;
			
		case PAUSED:
			
			g.setColor(GameData.MENU_COLOR);
			g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
			
			g.setColor(Color.BLACK);
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 150));
			g.drawString("PAUSED", frame.getWidth()/2-275, frame.getHeight()/2);
			
			break;
			
		}
		
	}
	
	/**
	 * Called from actionPerformed. Checks for player moves 
	 */
	private void update() {
		
		if (winner == null) 
			checkForMoves();
		
		if(victoryMessage != 1)
			checkForWin();
		
		if (sizeInitialized && ((int)gameDimension.getWidth() != (int)frame.getWidth() || gameDimension.getHeight() != frame.getHeight())) {
			if (frame.getPreferredSize().getHeight() < 10) {
				frame.setPreferredSize(new Dimension(frame.getWidth(), 10));
				frame.pack();
			}else {
				try {
				gameDimension = frame.getSize();
				updateBoardSize();
				}catch(Exception e) {
					System.out.println("CRASHIN");
					frame.setPreferredSize(new Dimension(800, 800));
					frame.pack();
					Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setLocation(screenDimension.width/2-frame.getSize().width/2, screenDimension.height/2-frame.getSize().height/2);				}
			}
		}
	}
	
	/**
	 * Checks if any player has won the game
	 */
	private void checkForWin() {
			
		if(moveCount >= 4) {
			
			moveSum = 0;
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
				moveSum += Game.player1Pieces.get(z).getPossibleMovesInCheck().size();					
			
			if(Piece.isInCheck(GameData.player.PLAYER_1) && moveSum == 0) 
				setWinner(GameData.player.PLAYER_2);
			
			moveSum = 0;
			
			for(int z = 0; z < Game.player2Pieces.size(); z++)		
				moveSum += Game.player2Pieces.get(z).getPossibleMovesInCheck().size();					
					
			if(Piece.isInCheck(GameData.player.PLAYER_2) && moveSum == 0) 
				setWinner(GameData.player.PLAYER_1);
			
			if (Game.player1Pieces.size() == 1 && Game.player1Pieces.get(0).getPossibleMovesInCheck().size() == 0 && !Piece.isInCheck(GameData.player.PLAYER_1) && playerTurn == GameData.player.PLAYER_1) 
				setWinner(null);
			else if (Game.player2Pieces.size() == 1 && Game.player2Pieces.get(0).getPossibleMovesInCheck().size() == 0 && !Piece.isInCheck(GameData.player.PLAYER_2) && playerTurn == GameData.player.PLAYER_2)
				setWinner(null);
			
		}
			
	}
	
	/**
	 * Sets the winner of the game
	 * @param player Winner of the game
	 */
	private void setWinner(GameData.player player) {
		
		if(winner == null && stalemate == false) {
		
			winner = player;
			
			if(winner == GameData.player.PLAYER_1)
				JOptionPane.showMessageDialog(null, "Player 1 wins! Total move count: " + moveCount, "Victory!", JOptionPane.PLAIN_MESSAGE);
			else if(winner == GameData.player.PLAYER_2)
				JOptionPane.showMessageDialog(null, "Player 2 wins! Total move count: " + moveCount, "Victory!", JOptionPane.PLAIN_MESSAGE);

			if (player == null) {
				JOptionPane.showMessageDialog(null, "Stalemate! Game is a draw!");
				stalemate = true;
				return;
			}
			
			
		}
		
	}
	
	/**
	 * Checks for whether or not a player has selected a new tile, selected a tile to move a piece, or has selected an invalid tile/move. Makes random move for AI bot if in single player mode
	 */
	private void checkForMoves(){
		
		if (!GameData.singlePlayer || playerTurn == GameData.player.PLAYER_1 && ultraBot != null && ultraBot.inMove == false) 
			if(isNewPieceClick()) 
				onNewPieceSelection();
			else if(isValidMoveClick())
				onValidMoveClick();
			else if(isInvalidValidClick())  
				onInvalidMoveClick();
			else
				;
		else if(!inPromotionMenu && ultraBot != null && ultraBot.inMove == false) {
		//	bot.randomMove();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ultraBot.move();
					resetTileClick();
					//bot.randomMove();

				}
			}).start();
		}	
		
	}
	
	/**
	 * Returns whether or not a tile is occupied by its own player
	 * @return A boolean for whether or not tile is occupied by own player
	 */
	public boolean isNewPieceClick() {
		
		if(playerTurn == GameData.player.PLAYER_1)
			return tileClicked != null && Tile.isOccupiedByPlayer1(tileClicked[0], tileClicked[1]);
		else
			return tileClicked != null && Tile.isOccupiedByPlayer2(tileClicked[0], tileClicked[1]);
	}
	
	/**
	 * Returns whether or not a clicked tile is a valid move for a selected piece
	 * @return A boolean for whether or not a clicked tile is a valid move for a selected piece
	 */
	public boolean isValidMoveClick() {
		
		try {
		return tileClicked != null && board[tileClicked[0]][tileClicked[1]].isValidMove && board[prevTileClicked[0]][prevTileClicked[1]].isSelected;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	/**
	 * Returns whether or not a clicked tile is not a possible piece/move selection
	 * @return A boolean for whether or not a clicked tile is not a possible piece/move selection.
	 */
	public boolean isInvalidValidClick() {
		
		try {
			if(playerTurn == GameData.player.PLAYER_1)
				return tileClicked != null && !Tile.isOccupiedByPlayer1(tileClicked[0], tileClicked[1]) && !board[tileClicked[0]][tileClicked[1]].isValidMove;
			else
				return tileClicked != null && !Tile.isOccupiedByPlayer2(tileClicked[0], tileClicked[1]) && !board[tileClicked[0]][tileClicked[1]].isValidMove;
		}catch (Exception e) {
		//	e.printStackTrace();
			return true;
		}
	}
	
	/**
	 * Resets all previous tile selections, selects the clicked tile, and shows valid moves for the piece on the selected tile
	 */
	public static void onNewPieceSelection() {
		
		Tile.resetTiles(false);
		board[tileClicked[0]][tileClicked[1]].setAsSelected(true);
		Tile.getPiece(tileClicked[0], tileClicked[1]).showValidMoves();
		
	}
	
	/**
	 * Kills any opposition pieces that are where the player's piece is going to, moves the player's piece, increments its move count, resets all tile selections, 
	 * plays a moving piece sound effect, highlights piece if it has checked opponent, shows opponents possible moves if in check, stores board data in arraylist of pieces,
	 * and then switches players turn
	 */
	public static void onValidMoveClick() {
			
		//Deletes a piece if opponent piece moves to its tile
		 if(Tile.isOccupiedByOpponent(tileClicked[0], tileClicked[1], playerTurn)) 
				Tile.getPiece(tileClicked[0], tileClicked[1]).delete();
		 
		System.out.println("Previous Tile Clicked: [" + prevTileClicked[0] + "," + prevTileClicked[1] + "]");
		System.out.println("Tile Clicked: [" + tileClicked[0] + "," + tileClicked[1] + "]");

		Tile.getPiece(prevTileClicked[0], prevTileClicked[1]).move(tileClicked[0], tileClicked[1]);
		Tile.getPiece(tileClicked[0], tileClicked[1]).increaseMoveCount();

		moveCount++;
		p1MoveCount++;
					
		Tile.resetTiles(true);
		
		board[tileClicked[0]][tileClicked[1]].setAsTileMovedTo(true);
		board[prevTileClicked[0]][prevTileClicked[1]].setAsTileMovedTo(true);

		storeBoardData();	
		game.playPieceSoundEffect();	
		showCheckMoves();
		resetTileClick();
		switchPlayerTurn();

	}
	
	/**
	 * Resets all tile selections
	 */
	public static void onInvalidMoveClick() {
		Tile.resetTiles(false);
	}
	
	/**
	 * Highlights piece that checked opponent and shows possible moves of player when in check
	 */
	public static void showCheckMoves() {
		
		if (Piece.isInCheck(GameData.player.PLAYER_1)) {
			
			for(int z = 0; z < player2Pieces.size(); z++)
				if(player2Pieces.get(z).isPieceThatChecked())
					board[player2Pieces.get(z).getRow()][player2Pieces.get(z).getColumn()].setAsTileThatChecked(true);
							
			for(int z = 0; z < player1Pieces.size(); z++)	
				if(player1Pieces.get(z).getPossibleMovesInCheck().size() != 0)
					board[player1Pieces.get(z).getRow()][player1Pieces.get(z).getColumn()].setAsAvailableMoveInCheck(true);
				
		}else if(Piece.isInCheck(GameData.player.PLAYER_2)) {
			
			for(int z = 0; z < player1Pieces.size(); z++) 
				if(player1Pieces.get(z).isPieceThatChecked())
					board[player1Pieces.get(z).getRow()][player1Pieces.get(z).getColumn()].setAsTileThatChecked(true);						
			
			for(int z = 0; z < player2Pieces.size() && !GameData.singlePlayer ; z++) 
				if(player2Pieces.get(z).getPossibleMovesInCheck().size() != 0)
					board[player2Pieces.get(z).getRow()][player2Pieces.get(z).getColumn()].setAsAvailableMoveInCheck(true);
				
		}
		
	}
	
	private void updateBoardSize() {
		
		GameData.WIDTH = frame.getWidth();
		GameData.HEIGHT = frame.getHeight();
		GameData.TILE_WIDTH = ((GameData.WIDTH-GameData.WIDTH_COMPENSATOR-14)/GameData.COLUMNS);
		GameData.TILE_HEIGHT = (GameData.HEIGHT-GameData.HEIGHT_COMPENSATOR-15)/GameData.ROWS;
		
		GameData.KING_PIECE_IMAGE_PLAYER_1 = GameData.PIECE_SPRITES.getSubimage(0, 0, 305, 336).getScaledInstance(GameData.TILE_WIDTH-(GameData.TILE_WIDTH/100), GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		GameData.KING_PIECE_IMAGE_PLAYER_2 = GameData.PIECE_SPRITES.getSubimage(0, 331, 305, 336).getScaledInstance(GameData.TILE_WIDTH-(GameData.TILE_WIDTH/100), GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		
		GameData.QUEEN_PIECE_IMAGE_PLAYER_1 = GameData.PIECE_SPRITES.getSubimage(336, 0, 307, 336).getScaledInstance(GameData.TILE_WIDTH-8, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		GameData.QUEEN_PIECE_IMAGE_PLAYER_2 = GameData.PIECE_SPRITES.getSubimage(336, 331, 307, 336).getScaledInstance(GameData.TILE_WIDTH-8, GameData.TILE_HEIGHT-5, Image.SCALE_SMOOTH);
		
		GameData.BISHOP_PIECE_IMAGE_PLAYER_1 = GameData.PIECE_SPRITES.getSubimage(672, 0, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		GameData.BISHOP_PIECE_IMAGE_PLAYER_2 = GameData.PIECE_SPRITES.getSubimage(672, 331, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		
		GameData.KNIGHT_PIECE_IMAGE_PLAYER_1 = GameData.PIECE_SPRITES.getSubimage(1008, 0, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		GameData.KNIGHT_PIECE_IMAGE_PLAYER_2 = GameData.PIECE_SPRITES.getSubimage(1008, 331, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		
		GameData.ROOK_PIECE_IMAGE_PLAYER_1 = GameData.PIECE_SPRITES.getSubimage(1300, 0, 305, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		GameData.ROOK_PIECE_IMAGE_PLAYER_2 = GameData.PIECE_SPRITES.getSubimage(1300, 331, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);

		GameData.PAWN_PIECE_IMAGE_PLAYER_1 = GameData.PIECE_SPRITES.getSubimage(1650, 0, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
		GameData.PAWN_PIECE_IMAGE_PLAYER_2 = GameData.PIECE_SPRITES.getSubimage(1650, 331, 300, 336).getScaledInstance(GameData.TILE_WIDTH, GameData.TILE_HEIGHT, Image.SCALE_SMOOTH);
				
	}
			
	/**
	 * Combines player1Pieces and player2Pieces Arraylist into one and then stores it in an Arraylist of Arraylists of Pieces. In case of undoing a move,
	 * player1Pieces and player2Pieces have their corresponding pieces added to them from a previous index in the Arraylist of Arraylists of Pieces.
	 */
	public static void storeBoardData() {
						
		ArrayList<Piece> combinedPieces = new ArrayList<Piece>();
		System.gc();
		
		for(int z = 0; z < player1Pieces.size(); z++) 
			combinedPieces.add(player1Pieces.get(z).clone());
		
		for(int z = 0; z < player2Pieces.size(); z++) 
			combinedPieces.add(player2Pieces.get(z).clone());
		
		pastPieces.add(moveCount, combinedPieces);
		
	}
	
	/**
	 * Plays a moving-piece sound effect
	 */
	public void playPieceSoundEffect() {
		

		try {
			 
			//InputStream pieceAudioStream = this.getClass().getResourceAsStream("ChessPieceSoundEffect.wav");
			InputStream pieceAudioStream = this.getClass().getResourceAsStream("ChessPieceSoundEffect.wav");

		 	AudioInputStream audio = AudioSystem.getAudioInputStream(pieceAudioStream);
		 	
		 	clip = AudioSystem.getClip();  
	        clip.open(audio);
	        clip.start();
	        
		} catch(Exception e) {
	       
	       e.printStackTrace();
	    	
	    }		
		
	}
	
	/**
	 * Swaps playerTurn variable to either PLAYER_1 or PLAYER_2
	 */
	public static void switchPlayerTurn() {
		
		if(playerTurn == GameData.player.PLAYER_1)
			playerTurn = GameData.player.PLAYER_2;
		else
			playerTurn = GameData.player.PLAYER_1;
		
	}
	
	/*
	 * Sets current tile clicked coordinates and previous tile clicked coordinates to null
	 */
	public static void resetTileClick() {
		
		tileClicked = null;
		prevTileClicked = null;
		
	}
	
	/**
	 * Writes board and game data to a file
	 * @param gameFile File to be written to
	 */
	public static void saveGameFile(File gameFile){
		
		System.gc();
		ObjectOutputStream outputStream = null;
		BufferedWriter writer = null;
				
		try{
						
			outputStream = new ObjectOutputStream(new FileOutputStream(new File(GameData.fileLocation.concat("\\").concat(gameFile.getName())), false));
			outputStream.writeObject(pastPieces);
			outputStream.flush();
			outputStream.close();
			
			writer = new BufferedWriter(new FileWriter(new File(GameData.fileLocation.concat("\\").concat(gameFile.getName())), true));
			writer.append(System.lineSeparator() + "Move Count: " + Integer.toString(moveCount) + System.lineSeparator());
			writer.append("Single Player: " + GameData.singlePlayer + System.lineSeparator());
			writer.append("AI Level: " + GameData.AI_LEVEL);
			
			writer.flush();
			
			JOptionPane.showMessageDialog(null, "Game saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}finally {
			try {
				outputStream.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Reads object and game data information stored in file to create identical game with the same properties
	 * @param file File to have board and game data loaded from
	 */
	public static void loadGameFile(File file) {
	
		System.gc();
		
		ObjectInputStream inputStream = null;
		BufferedReader reader = null; 
		GameData.gameFile = file;
		
		try{
			
			pastPieces.clear();
			player1Pieces.clear();
			player2Pieces.clear();
			
			inputStream = new ObjectInputStream(new FileInputStream(file));
			pastPieces = (ArrayList<ArrayList<Piece>>) inputStream.readObject();
		
			for (int z = 0; z < pastPieces.get(pastPieces.size()-1).size(); z++)
				if (pastPieces.get(pastPieces.size()-1).get(z).getPlayer() == GameData.player.PLAYER_1) 
					player1Pieces.add(pastPieces.get(pastPieces.size()-1).get(z));
				else 
					player2Pieces.add(pastPieces.get(pastPieces.size()-1).get(z));
			
			reader = new BufferedReader(new FileReader(file));
			String line;
			
			while ((line = reader.readLine()) != null) 
				if (line.startsWith("Move Count: ")) 
					moveCount = Integer.parseInt(line.replaceFirst("Move Count: ", ""));
				else if (line.startsWith("Single Player: ")) 
					GameData.singlePlayer = Boolean.parseBoolean(line.replaceFirst("Single Player: ", ""));
				else if (line.startsWith("AI Level: ")) 
					GameData.AI_LEVEL = Integer.parseInt(line.replaceFirst("AI Level: ", ""));
			
			if(moveCount%2 == 0)
				playerTurn = GameData.player.PLAYER_1;
			else
				playerTurn = GameData.player.PLAYER_2;
			
			if(GameData.singlePlayer == true)
				playerTurn = GameData.player.PLAYER_1;
						
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}finally{
			try {
				inputStream.close();
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		renderer.repaint();
		
		if(ultraBot != null && ultraBot.inMove == false && gameState == GameData.gameState.IN_GAME) 
			update();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stubs           
		
		try {
			
			if(gameState == GameData.gameState.IN_GAME && ultraBot != null && ultraBot.inMove == false) {
				
				if(tileClicked != null)
					prevTileClicked = tileClicked;
				
				if(!inPromotionMenu)
					if (GameData.singlePlayer || playerTurn == GameData.player.PLAYER_1 || GameData.switchViews == false)
						tileClicked = new int[]{(int)(Math.floor((e.getY()-GameData.HEIGHT_COMPENSATOR-10)/GameData.TILE_HEIGHT)), (int)Math.floor((e.getX()-(GameData.WIDTH_COMPENSATOR+5))/GameData.TILE_WIDTH)};
					else if (!GameData.singlePlayer && playerTurn == GameData.player.PLAYER_2) 
						tileClicked = new int[]{(int) (GameData.ROWS-1-(Math.floor((e.getY()-GameData.HEIGHT_COMPENSATOR+5)/GameData.TILE_WIDTH))), (int)(GameData.ROWS-1-(Math.floor((e.getX()-(GameData.WIDTH_COMPENSATOR-2))/GameData.TILE_HEIGHT)))};
			}
			
		}catch(Exception ex) {
		//	ex.printStackTrace();
			System.out.println("Tile clicked: " + tileClicked[0] + "," + tileClicked[1]);
			tileClicked = null;
			prevTileClicked = null;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(e.getKeyCode()) {
		
		case KeyEvent.VK_C:
			if (gameState == GameData.gameState.STARTING_MENU) 
				new GameCreationMenu();
			break;
		case KeyEvent.VK_L:
			if (gameState == GameData.gameState.STARTING_MENU)
				new LoadGameMenu();
			break;
		case KeyEvent.VK_P:
			if(gameState == GameData.gameState.IN_GAME)
				gameState = GameData.gameState.PAUSED;
			else if(gameState == GameData.gameState.PAUSED)
				gameState = GameData.gameState.IN_GAME;
			break;	

		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	/**
	 * Main method; loads initial game frame
	 * @param args
	 */
	public static void main(String[] args) {
	
		game = new Game();
		
	}

	

}
