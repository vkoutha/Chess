package Chess;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class GameData{
	
	static {
		
		try {
			PIECE_SPRITES = ImageIO.read(GameData.class.getResource("sprites.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
//-----------------------------------------------------------------

	public static final int WIDTH = 900;
	public static final int WIDTH_COMPENSATOR = 5;
	
	public static final int HEIGHT = 900;
	public static final int HEIGHT_COMPENSATOR = 35;
	
	public static final int PROMOTION_WINDOW_WIDTH = 700;
	public static final int PROMOTION_WINDOW_HEIGHT = 450;
	
	public static final int ROWS = 8;
	public static final int COLUMNS = 8;
	
	public static final int TILE_WIDTH = WIDTH/ROWS;
	public static final int TILE_HEIGHT = HEIGHT/COLUMNS;
	
	public static final int PLAYER_1_STARTING_PAWN_ROW = 6;
	public static final int PLAYER_2_STARTING_PAWN_ROW = 1;
	
//----------------------------------------------------------------
	
	public static final int PAWN_HEAD_DIAMETER = 30;
	
	public static final int PAWN_HEAD2_WIDTH = 40;
	public static final int PAWN_HEAD2_HEIGHT = 20;
	
	public static final int PAWN_BODY_WIDTH = 55;
	public static final int PAWN_BODY_HEIGHT = 60;
	
	public static final int PAWN_BASE_WIDTH = 70;
	public static final int PAWN_BASE_HEIGHT = 20;

//---------------------------------------------------------------
	
	public static final int ROOK_PILLAR_WIDTH = 15;
	public static final int ROOK_PILLAR_HEIGHT =13;
	
	public static final int ROOK_HEAD_WIDTH = 60;
	public static final int ROOK_HEAD_HEIGHT = 20;
	
	public static final int ROOK_BODY_WIDTH = 35;
	public static final int ROOK_BODY_HEIGHT = 50;
	
	public static final int ROOK_BASE_WIDTH = 70;
	public static final int ROOK_BASE_HEIGHT = 20;

//-----------------------------------------------------------------
	
	public static final int KNIGHT_HEAD_WIDTH = 50;
	public static final int KNIGHT_HEAD_HEIGHT = 30;
	
	public static final int KNIGHT_BASE_WIDTH = 70;
	public static final int KNIGHT_BASE_HEIGHT = 20;
	
//-----------------------------------------------------------------

	public static final int BISHOP_CROWN_RADIUS = 15;
	
	public static final int BISHOP_HEAD_DIAMETER = 30;
	
	public static final int BISHOP_HEAD2_WIDTH = 40;
	public static final int BISHOP_HEAD2_HEIGHT = 20;
	
	public static final int BISHOP_BODY_WIDTH = 55;
	public static final int BISHOP_BODY_HEIGHT = 60;
	
	public static final int BISHOP_BASE_WIDTH = 70;
	public static final int BISHOP_BASE_HEIGHT = 20;

//-----------------------------------------------------------------
	
	public static final int QUEEN_HEAD_DIAMETER = 15;
	
	public static final int QUEEN_NECK_WIDTH = 60;
	public static final int QUEEN_NECK_HEIGHT = 15;
	
	public static final int QUEEN_BODY_WIDTH = 50;
	public static final int QUEEN_BODY_HEIGHT = 30;
	
	public static final int QUEEN_BODY_CONNECTOR_WIDTH = 30;
	public static final int QUEEN_BODY_CONNECTOR_HEIGHT = 50;

	public static final int QUEEN_BASE_WIDTH = 70;
	public static final int QUEEN_BASE_HEIGHT = 10;
	
//-----------------------------------------------------------------
	
	public static final int KING_VERTICAL_CROSS_WIDTH = 10;
	public static final int KING_VERTICAL_CROSS_HEIGHT = 27;
	
	public static final int KING_HORIZONTAL_CROSS_WIDTH = 20;
	public static final int KING_HORIZONTAL_CROSS_HEIGHT = 5;

	public static final int KING_NECK_WIDTH = 60;
	public static final int KING_NECK_HEIGHT = 15;
	
	public static final int KING_BODY_WIDTH = 50;
	public static final int KING_BODY_HEIGHT = 30;
	
	public static final int KING_BODY_CONNECTOR_WIDTH = 30;
	public static final int KING_BODY_CONNECTOR_HEIGHT = 50;

	public static final int KING_BASE_WIDTH = 70;
	public static final int KING_BASE_HEIGHT = 10;

//-----------------------------------------------------------------
	
	public static final int QUEEN_VALUE = 500;
	public static final int ROOK_VALUE = 100;
	public static final int BISHOP_VALUE = 50;
	public static final int KNIGHT_VALUE = 50;
	public static final int PAWN_VALUE = 1;
	public static final int KING_VALUE = 1;
	
//-----------------------------------------------------------------
	
	public static final Color MENU_COLOR = new Color(244, 178, 46);
	
	public static final Color TILE_COLOR_1_WHITE = new Color(255, 178, 102); //WHITE
	public static final Color TILE_COLOR_2_MAROON = new Color(153, 76, 0); //MAROON
	public static final Color TILE_COLOR_WHEN_CLICKED = new Color(255, 255, 75);
	public static final Color TILE_COLOR_WHEN_CLICKED_OCCUPIED = Color.ORANGE;
	public static final Color TILE_COLOR_WHEN_MOVED_TO = new Color(183, 255, 74); //YELLOW GREEN	

	public static final Color TILE_COLOR_WHEN_CHECKED = new Color(220, 10, 30);
	public static final Color TILE_COLOR_WHEN_AVAILABLE_MOVE_IN_CHECK = new Color(100, 224, 189);
	
	public static final Color PLAYER_1_PIECE_COLOR = new Color(240, 240, 240); //WHITE
	public static final Color PLAYER_1_KING_COLOR = new Color(240, 240, 240);
	public static final Color PLAYER_2_PIECE_COLOR = Color.BLACK; //BLACK
	public static final Color PLAYER_2_KING_COLOR = Color.BLACK;
	
//-----------------------------------------------------------------
	
	static int AI_LEVEL = 1;
	static boolean singlePlayer = true;

//-----------------------------------------------------------------
	
	public static BufferedImage PIECE_SPRITES;
	
	public static final Image KING_PIECE_IMAGE_PLAYER_1 = PIECE_SPRITES.getSubimage(0, 0, 305, 336).getScaledInstance(100, 115, Image.SCALE_SMOOTH);
	public static final Image KING_PIECE_IMAGE_PLAYER_2 = PIECE_SPRITES.getSubimage(0, 331, 305, 336).getScaledInstance(100, 115, Image.SCALE_SMOOTH);
	
	public static final Image QUEEN_PIECE_IMAGE_PLAYER_1 = PIECE_SPRITES.getSubimage(336, 0, 307, 336).getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	public static final Image QUEEN_PIECE_IMAGE_PLAYER_2 = PIECE_SPRITES.getSubimage(336, 331, 307, 336).getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	
	public static final Image BISHOP_PIECE_IMAGE_PLAYER_1 = PIECE_SPRITES.getSubimage(672, 0, 300, 336).getScaledInstance(100, 110, Image.SCALE_SMOOTH);
	public static final Image BISHOP_PIECE_IMAGE_PLAYER_2 = PIECE_SPRITES.getSubimage(672, 331, 300, 336).getScaledInstance(100, 110, Image.SCALE_SMOOTH);
	
	public static final Image KNIGHT_PIECE_IMAGE_PLAYER_1 = PIECE_SPRITES.getSubimage(1008, 0, 300, 336).getScaledInstance(100, 110, Image.SCALE_SMOOTH);
	public static final Image KNIGHT_PIECE_IMAGE_PLAYER_2 = PIECE_SPRITES.getSubimage(1008, 331, 300, 336).getScaledInstance(100, 110, Image.SCALE_SMOOTH);
	
	public static final Image ROOK_PIECE_IMAGE_PLAYER_1 = PIECE_SPRITES.getSubimage(1300, 0, 305, 336).getScaledInstance(110, 120, Image.SCALE_SMOOTH);
	public static final Image ROOK_PIECE_IMAGE_PLAYER_2 = PIECE_SPRITES.getSubimage(1300, 331, 300, 336).getScaledInstance(105, 120, Image.SCALE_SMOOTH);

	public static final Image PAWN_PIECE_IMAGE_PLAYER_1 = PIECE_SPRITES.getSubimage(1650, 0, 300, 336).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
	public static final Image PAWN_PIECE_IMAGE_PLAYER_2 = PIECE_SPRITES.getSubimage(1650, 331, 300, 336).getScaledInstance(120, 120, Image.SCALE_SMOOTH);
	
//-----------------------------------------------------------------

	public static final String fileLocation = "H:\\animal stuff\\Chess";
	
	public static final File gameFileFolder = new File(fileLocation);
	public static final File[] gameFiles = gameFileFolder.listFiles(); 
	public static File gameFile;
		
//-----------------------------------------------------------------
	
	public static final Icon queenIcon = new ImageIcon(new ImageIcon(GameData.class.getResource("queen.png")).getImage().getScaledInstance(200, 140, Image.SCALE_DEFAULT));
	public static final Icon bishopIcon = new ImageIcon(new ImageIcon(GameData.class.getResource("bishop.png")).getImage().getScaledInstance(170, 135, Image.SCALE_DEFAULT));
	public static final Icon rookIcon = new ImageIcon(new ImageIcon(GameData.class.getResource("rook.png")).getImage().getScaledInstance(180, 130, Image.SCALE_DEFAULT));
	public static final Icon knightIcon = new ImageIcon(new ImageIcon(GameData.class.getResource("knight.png")).getImage().getScaledInstance(180, 190, Image.SCALE_DEFAULT));
	
	public static final Image frameIcon = new ImageIcon(new ImageIcon(GameData.class.getResource("gameIcon.png")).getImage().getScaledInstance(180, 190, Image.SCALE_DEFAULT)).getImage();

//-----------------------------------------------------------------
	public enum player{
		
		PLAYER_1, 
		PLAYER_2;
		
	}
	
	public enum gameState{
		
		STARTING_MENU,
		IN_GAME,
		PAUSED;
		
	}
	
//-----------------------------------------------------------------

	
	public static int[] getTileCenter(int row, int column) {
		
		//RETURNS coordinate [x, y] 
		return new int[] {(column * TILE_WIDTH) + (GameData.TILE_WIDTH/2), (row * TILE_HEIGHT) + (GameData.TILE_HEIGHT/2)};
		
	}
	
	public static int getShapeStartingX(int column, int width){
		
		return column*GameData.TILE_WIDTH+(GameData.TILE_WIDTH-width)/2;
		
	}
	
	public static int getShapeStartingY(int row, int height){
		
		return row*GameData.TILE_WIDTH + (GameData.TILE_WIDTH-height)/2;
		
	}
	
}

