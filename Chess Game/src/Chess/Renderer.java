package Chess;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel{

	protected void paintComponent(Graphics g) {
		
		try{
			super.paintComponent(g);
			Game.game.render(g);
		}catch(Exception e) {
			//e.printStackTrace();
		}
		
	}
	
}
