package Chess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadGameMenu {

	JFrame frame;
	File[] gameFiles;
	ArrayList<String> gameFileNames;
	
	public LoadGameMenu() {
		
		Game.frame.setVisible(false);

		gameFiles = GameData.gameFiles;
		gameFileNames = new ArrayList<String>();
		
		for (File file : gameFiles) {
			gameFileNames.add(file.getName().replace(".txt", ""));
		}
		
		frame = new JFrame("Load Game");
	    frame.setIconImage(GameData.frameIcon);
		frame.getContentPane().setBackground(Color.ORANGE);
		frame.setSize(500, 400);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JLabel lblLoadGame = new JLabel("Load Game");
		lblLoadGame.setFont(new Font("Snap ITC", Font.BOLD, 40));
		lblLoadGame.setBounds(124, 34, 289, 34);
		frame.getContentPane().add(lblLoadGame);
		
		JList list = new JList(gameFileNames.toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(378, 310, -261, -147);
		list.setBackground(Color.YELLOW);
		
		JScrollPane listScroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScroll.add(list);
		listScroll.setBounds(152, 150, 190, 190);
		listScroll.setViewportView(list);
		listScroll.setForeground(Color.LIGHT_GRAY);
		listScroll.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(listScroll);
		
		JButton btnLoad = new JButton();
		btnLoad.setFont(new Font("Snap ITC", Font.BOLD, 20));
		btnLoad.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("load.png")).getImage().getScaledInstance(80, 50, Image.SCALE_DEFAULT)));
		removeBackground(btnLoad);
		btnLoad.setBounds(184, 82, 155, 55);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				if(list.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select a game to load!", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				for(File file : gameFiles) {

					if((list.getSelectedValue() + ".txt").equals(file.getName())) {
						Game.frame.setVisible(true);
						Game.gameState = GameData.gameState.IN_GAME;
						Game.loadGameFile(file);
						frame.dispose();
						
					}
					
				}	
				
			}
		});
		
		frame.getContentPane().add(btnLoad);
		
		JButton backButton = new JButton();
		backButton.setBounds(0, -5, 100, 100);
		backButton.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("backArrow.png")).getImage().getScaledInstance(60, 50, Image.SCALE_DEFAULT)));
		removeBackground(backButton);
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Game.frame.setVisible(true);
				frame.dispose();
			}
		});
		frame.add(backButton);
		
	}
	
	public void deleteGame() {
		
		
		
	}
	
	public void removeBackground(JButton button) {
		
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		
	}
	
}
