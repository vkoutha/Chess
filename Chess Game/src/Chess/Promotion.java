package Chess;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Promotion{
	
	JFrame promotionWindow;
	Piece pieceToDelete;
	
	public Promotion(Piece pieceToDelete) {
				
			Game.inPromotionMenu = true;
			this.pieceToDelete = pieceToDelete;
			
			promotionWindow = new JFrame("Promotion");
			promotionWindow.setSize(GameData.PROMOTION_WINDOW_WIDTH, GameData.PROMOTION_WINDOW_HEIGHT);
			promotionWindow.setResizable(false);
			promotionWindow.setLayout(null);
			promotionWindow.setIconImage(GameData.frameIcon);
			promotionWindow.getContentPane().setBackground(GameData.TILE_COLOR_1_WHITE);
			promotionWindow.setVisible(true);
			promotionWindow.setLocationRelativeTo(null);
			promotionWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			JLabel heading = new JLabel("Choose Piece to Promote To!");
			heading.setFont(new Font(Font.SERIF, Font.BOLD, 30));
			heading.setBounds((GameData.PROMOTION_WINDOW_WIDTH/2)-((int)heading.getPreferredSize().getWidth()/2), 100, (int) heading.getPreferredSize().getWidth(), (int) heading.getPreferredSize().getHeight());
	
			promotionWindow.add(heading);
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			JLabel queen = new JLabel("Queen");
			queen.setFont(new Font(Font.SERIF, Font.BOLD, 25));
			queen.setBounds((int)queen.getPreferredSize().getWidth()+20, 190, (int)queen.getPreferredSize().getWidth(), (int)queen.getPreferredSize().getHeight());
			
			JButton queenB = new JButton();
			queenB.setIcon(GameData.queenIcon);
			queenB.setContentAreaFilled(false);
			queenB.setFocusPainted(false);
			queenB.setBorderPainted(false);
			queenB.setBounds(80, 230, 100, 110);
			queenB.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  promote(new Queen(pieceToDelete.getRow(), pieceToDelete.getColumn(), pieceToDelete.getPlayer()));
				  }
			});
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			JLabel bishop = new JLabel("Bishop");
			bishop.setFont(new Font(Font.SERIF, Font.BOLD, 25));
			bishop.setBounds((int)bishop.getPreferredSize().getWidth()*2 + (int) bishop.getPreferredSize().getWidth()/2 + 45, 190, (int)bishop.getPreferredSize().getWidth(), (int)bishop.getPreferredSize().getHeight());
			
			JButton bishopB = new JButton();
			bishopB.setContentAreaFilled(false);
			bishopB.setFocusPainted(false);
			bishopB.setBorderPainted(false);
			bishopB.setIcon(GameData.bishopIcon);
			bishopB.setBounds(220, 200, 100, 150);
			bishopB.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  promote(new Bishop(pieceToDelete.getRow(), pieceToDelete.getColumn(), pieceToDelete.getPlayer()));
				  } 
			});
			
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			JLabel rook = new JLabel("Rook");
			rook.setFont(new Font(Font.SERIF, Font.BOLD, 25));
			rook.setBounds((int)(GameData.PROMOTION_WINDOW_WIDTH *.75)/2 + (int)rook.getPreferredSize().getWidth()/2 + 80, 190 , (int)rook.getPreferredSize().getWidth(), (int)rook.getPreferredSize().getHeight());
			
			JButton rookB = new JButton();
			rookB.setIcon(GameData.rookIcon);
			rookB.setContentAreaFilled(false);
			rookB.setFocusPainted(false);
			rookB.setBorderPainted(false);
			rookB.setBounds(350, 230, 100, 110);
			rookB.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  promote(new Rook(pieceToDelete.getRow(), pieceToDelete.getColumn(), pieceToDelete.getPlayer()));
				  } 
			});
			
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			JLabel knight = new JLabel("Knight");
			knight.setFont(new Font(Font.SERIF, Font.BOLD, 25));
			knight.setBounds((int)(GameData.PROMOTION_WINDOW_WIDTH)/2 + 130, 190, (int)knight.getPreferredSize().getWidth(), (int)knight.getPreferredSize().getHeight());
		
			JButton knightB = new JButton();
			knightB.setIcon(GameData.knightIcon);
			knightB.setContentAreaFilled(false);
			knightB.setFocusPainted(false);
			knightB.setBorderPainted(false);
			knightB.setBounds(470, 230, 100, 110);
			knightB.addActionListener(new ActionListener() { 
				  public void actionPerformed(ActionEvent e) { 
					  promote(new Knight(pieceToDelete.getRow(), pieceToDelete.getColumn(), pieceToDelete.getPlayer()));
				  } 
			});
			
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			promotionWindow.add(queen);
			promotionWindow.add(queenB);
			
			promotionWindow.add(bishop);
			promotionWindow.add(bishopB);
			
			promotionWindow.add(rook);
			promotionWindow.add(rookB);
			
			promotionWindow.add(knight);
			promotionWindow.add(knightB);
			
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	}
	
	public void promote(Piece pieceToAdd) {
		
		switch(pieceToDelete.getPlayer()) {
		  
		  case PLAYER_1:

			  Game.player1Pieces.add(pieceToAdd);
			  Game.player1Pieces.remove(pieceToDelete);
			  
			  break;
			  
		  case PLAYER_2:
			  
			  Game.player2Pieces.add(pieceToAdd);
			  Game.player2Pieces.remove(pieceToDelete);
				  
			  break;
		  
		  }
		  
		  promotionWindow.dispose();
		  Game.inPromotionMenu = false;
		  
	  } 
		
	

}
