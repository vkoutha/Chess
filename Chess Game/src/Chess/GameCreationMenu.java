package Chess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameCreationMenu {

	JFrame frame;
	JLabel lblSinglePlayer, lblAILevel, lblEnableGameSaves;
	JSlider slider;
	JButton btnSinglePlayer, btnTwoPlayer;
	ButtonGroup playerButtons;
	JRadioButton onePlayerRadio, twoPlayerRadio;
	JCheckBox saveGameCheckBox;
	JTextField gameNameField;
	JButton btnCreateGame;
	
	public GameCreationMenu() {
		
		Game.frame.setVisible(false);
		
		frame = new JFrame("Create Game");
	    frame.setIconImage(GameData.frameIcon);
		frame.setSize(600, 550);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.ORANGE);
		
		lblSinglePlayer = new JLabel("Players");
		lblSinglePlayer.setFont(new Font("Snap ITC", Font.BOLD, 30));
		lblSinglePlayer.setBounds(228, 130, 275, 51);
		frame.getContentPane().add(lblSinglePlayer);
		
		slider = new JSlider();
		slider.setBackground(Color.ORANGE);

		slider.setMinimum(1);
		slider.setMaximum(9);
		slider.setValue(1);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblAILevel.setText("AI Level: " + slider.getValue());
			}
		});
		slider.setBounds((int)((frame.getWidth()/2)-(slider.getPreferredSize().getWidth()/2)), 386, 200, 26);
		frame.getContentPane().add(slider);
		
		lblAILevel = new JLabel("AI Level: " + slider.getValue());
		lblAILevel.setBackground(new Color(0, 0, 0));
		lblAILevel.setFont(new Font("Snap ITC", Font.BOLD, 30));
		lblAILevel.setBounds(190, 330, 228, 59);
		frame.getContentPane().add(lblAILevel);
		
		btnSinglePlayer = new JButton();
		btnSinglePlayer.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("onePlayer.png")).getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT)));
		removeBackground(btnSinglePlayer);
		btnSinglePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onePlayerRadio.setSelected(true);
				slider.setEnabled(true);
				lblAILevel.setForeground(Color.DARK_GRAY);
			}
		});
		btnSinglePlayer.setBounds(170, 190, 90, 101);
		frame.getContentPane().add(btnSinglePlayer);
		
		btnTwoPlayer = new JButton();
		btnTwoPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				twoPlayerRadio.setSelected(true);
				slider.setEnabled(false);
				lblAILevel.setForeground(Color.GRAY);
			}
		});
		btnTwoPlayer.setForeground(Color.ORANGE);
		btnTwoPlayer.setBackground(Color.ORANGE);
		removeBackground(btnTwoPlayer);
		btnTwoPlayer.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("twoPlayer.png")).getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT)));
		btnTwoPlayer.setBounds(264, 194, 190, 98);
		frame.getContentPane().add(btnTwoPlayer);
		
		onePlayerRadio = new JRadioButton("", true);
		onePlayerRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slider.setEnabled(true);
				lblAILevel.setForeground(Color.DARK_GRAY);
			}
		});
		onePlayerRadio.setFont(new Font("Dialog", Font.BOLD, 81));
		onePlayerRadio.setBackground(Color.ORANGE);
		onePlayerRadio.setBounds(204, 300, 25, 25);
		frame.getContentPane().add(onePlayerRadio);
		
		twoPlayerRadio = new JRadioButton("");
		twoPlayerRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slider.setEnabled(false);
				lblAILevel.setForeground(Color.GRAY);
			}
		});
		
		twoPlayerRadio.setBackground(Color.ORANGE);
		twoPlayerRadio.setBounds(350, 301, 25, 25);
		frame.getContentPane().add(twoPlayerRadio);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playerButtons = new ButtonGroup();
		playerButtons.add(onePlayerRadio);
		playerButtons.add(twoPlayerRadio);
		
		lblEnableGameSaves = new JLabel("Enable Game Saves");
		lblEnableGameSaves.setFont(new Font("Snap ITC", Font.BOLD, 30));
		lblEnableGameSaves.setBounds((int)((frame.getWidth()/2)-(lblEnableGameSaves.getPreferredSize().getWidth()/2)), 31, 393, 40);
		frame.getContentPane().add(lblEnableGameSaves);
		
		saveGameCheckBox = new JCheckBox(" Game Name: ");
		saveGameCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(saveGameCheckBox.isSelected()) {
					gameNameField.setEditable(true);
					gameNameField.setFocusable(true);
				}else {
					gameNameField.setEditable(false);
					gameNameField.setFocusable(false);
					gameNameField.setText("");
				}
			}
		});
		saveGameCheckBox.setFont(new Font("Snap ITC", Font.ITALIC, 20));
		saveGameCheckBox.setBackground(Color.ORANGE);
		saveGameCheckBox.setBounds(133, 80, 184, 25);
		saveGameCheckBox.setFocusable(false);
		frame.getContentPane().add(saveGameCheckBox);
		
		gameNameField = new JTextField();
		gameNameField.setBounds(315, 84, 126, 22);
		gameNameField.setEditable(false);
		gameNameField.setFocusable(false);
		frame.getContentPane().add(gameNameField);
		gameNameField.setColumns(1);
		
		btnCreateGame = new JButton("Create Game");
		btnCreateGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (gameNameField.getText() == null || gameNameField.getText() == "") {
					JOptionPane.showMessageDialog(null, "Game name can not be blank!", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				createGame(new File(GameData.fileLocation.concat("\\").concat(gameNameField.getText()).concat(".txt")));
			}
		});
		btnCreateGame.setBounds(228, 425, 136, 40);
		frame.getContentPane().add(btnCreateGame);
		
		JButton backButton = new JButton();
		backButton.setBounds(0, -5, 100, 100);
		backButton.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("backArrow.png")).getImage().getScaledInstance(80, 50, Image.SCALE_DEFAULT)));
		removeBackground(backButton);
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Game.frame.setVisible(true);
				frame.dispose();
			}
		});
		frame.getContentPane().add(backButton);
		
	}
	
	public void removeBackground(JButton button) {
		
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		
	}
	
	public void createGame(File file) {
		
		if (onePlayerRadio.isSelected())
			GameData.singlePlayer = true;
		else
			GameData.singlePlayer = false;
			
		GameData.AI_LEVEL = slider.getValue();
		
		if(saveGameCheckBox.isSelected()) {
			
			if (gameNameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Game name can not be blank!", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}else if (file.exists()) {
				JOptionPane.showMessageDialog(null, "Game with the same name already exists!", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}else{
				try {
					file.createNewFile();
					GameData.gameFile = file;
					Game.saveGameFile(GameData.gameFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		Game.frame.setVisible(true);
		Game.gameState = GameData.gameState.IN_GAME;
		
		frame.dispose();
		
	}
	
}
