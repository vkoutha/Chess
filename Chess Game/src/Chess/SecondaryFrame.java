package Chess;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;

public class SecondaryFrame extends JPanel implements ActionListener{
	
	JFrame frame;
	JLabel timeLabel, p1MovesLabel, p2MovesLabel;
	JSeparator horizontalSeparator, verticalSeparator;
	DecimalFormat formatter = new DecimalFormat("#.##");
	double startTime, timePassed = 0;
	
	public SecondaryFrame() {
		
		Timer timer = new Timer(20, this);
		
		frame = new JFrame("Chess");
	    frame.setIconImage(GameData.frameIcon);
		frame.setSize(350, 250);
		frame.getContentPane().setBackground(Color.ORANGE);
	    frame.setLocation(Game.frame.getX()-frame.getWidth(), Game.frame.getY());
	    frame.setLayout(null);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		startTime = System.currentTimeMillis();
		
		timeLabel = new JLabel("Time passed: #####");
		timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
		
		frame.add(timeLabel);
		timeLabel.setBounds((int)((frame.getWidth()/2) - (timeLabel.getPreferredSize().getWidth()/2))+5, 55, (int)timeLabel.getPreferredSize().getWidth()+111, (int)timeLabel.getPreferredSize().getHeight());
		
		JButton reverseButton = new JButton();
		frame.add(reverseButton);	
		reverseButton.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("undoArrow.png")).getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
		reverseButton.setBounds((int)((frame.getWidth()/2) - (reverseButton.getPreferredSize().getWidth()/2))+50, 120, (int)reverseButton.getPreferredSize().getWidth(), (int)reverseButton.getPreferredSize().getHeight());
		removeBackground(reverseButton);
		reverseButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  reverseMove();
			  }});
			
		JButton saveButton = new JButton();
		frame.add(saveButton);	
		saveButton.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("save.png")).getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
		saveButton.setBounds((int)((frame.getWidth()/2) - (saveButton.getPreferredSize().getWidth()/2))-50, 120, (int)saveButton.getPreferredSize().getWidth(), (int)saveButton.getPreferredSize().getHeight());
		removeBackground(saveButton);
		saveButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  if(GameData.gameFile != null) 
					  Game.saveGameFile(GameData.gameFile);
			  }});
		
		timer.start();
			
	}
	
	public void reverseMove() {
		
		Tile.resetTiles(true);
		
		if(Game.moveCount > 0 && !Game.inPromotionMenu) {
			
			Game.player1Pieces.clear();
			Game.player2Pieces.clear();
			
			if(GameData.singlePlayer){
				
				Game.pastPieces.remove(Game.moveCount);
				Game.moveCount--;
				Game.pastPieces.remove(Game.moveCount);
				Game.moveCount--;
				
			}else{
				
				Game.pastPieces.remove(Game.moveCount);
				Game.moveCount--;
				
			}
			
			for(int z = 0; z < Game.pastPieces.get(Game.moveCount).size(); z++) 
				if(Game.pastPieces.get(Game.moveCount).get(z).getPlayer() == GameData.player.PLAYER_1) 
					Game.player1Pieces.add(Game.pastPieces.get(Game.moveCount).get(z));
				else
					Game.player2Pieces.add(Game.pastPieces.get(Game.moveCount).get(z));
					
			

			if(Game.moveCount%2 == 0)
				Game.playerTurn = GameData.player.PLAYER_1;
			else
				Game.playerTurn = GameData.player.PLAYER_2;				
			
			if(Game.winner != null || Game.stalemate) {
				Game.winner = null;
				Game.stalemate = false;
			}
			
			for(int z = 0; z < Game.player1Pieces.size(); z++) 
				if(Game.player1Pieces.get(z).getType() == Piece.type.PAWN && Game.player1Pieces.get(z).getRow() == 0)
					new Promotion(Game.player1Pieces.get(z));
			
			for(int z = 0; z < Game.player2Pieces.size(); z++) 
				if(Game.player2Pieces.get(z).getType() == Piece.type.PAWN && Game.player2Pieces.get(z).getRow() == 7)
					new Promotion(Game.player2Pieces.get(z));
							
			Tile.resetTiles(true);
			Game.storeBoardData();
			Game.showCheckMoves();
			
		}
		
	}
	
	public void removeBackground(JButton button) {
		
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		setBackground(Color.BLACK);
		render(g);
		
	}
	
	
	public void render(Graphics g) {
		
		g.setColor(Color.PINK);
		g.fillOval(20, 20, 20, 20);
		
	}
	
	public void update() {
		
		if(Game.winner == null) {
			
			timePassed = Double.parseDouble(formatter.format((System.currentTimeMillis() - startTime)/1000.0));
			
			timeLabel.setText("Time passed: " + timePassed);
			if((timePassed > 10 && timePassed < 10.1) || (timePassed > 100 && timePassed < 100.1) || (timePassed > 1000 && timePassed < 1000.1))
				timeLabel.setBounds((int)((frame.getWidth()/2) - (timeLabel.getPreferredSize().getWidth()/2))-5, 55, (int)timeLabel.getPreferredSize().getWidth()+100000, (int)timeLabel.getPreferredSize().getHeight());
				
		}
			
		this.repaint();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		update();
					
	}
	
}
