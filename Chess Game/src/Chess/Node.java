package Chess;

import java.util.ArrayList;

public class Node {

	private Node parent;
	private Node[] children = null;
	private Piece piece;	
	private int[] originalLocation, move;
	private int layer, value;
	
	public Node(Node parent, int layer, Piece piece, int[] originalLocation, int[] move, ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		this.parent = parent;
		this.layer = layer;
		this.piece = piece;
		this.move = move;
		this.originalLocation = originalLocation;
		Game.ultraBot.totalNodes++;
		if(Tile.isOccupiedByOpponent(move[0], move[1], piece.getPlayer(), playerPieces, botPieces))
			if(piece.getPlayer() == GameData.player.PLAYER_1) 
				botPieces.remove(Tile.getPiece(move[0], move[1], playerPieces, botPieces));
			else 
				playerPieces.remove(Tile.getPiece(move[0], move[1], playerPieces, botPieces));
		Tile.getPiece(piece.getRow(), piece.getColumn(), playerPieces, botPieces).setLocation(move);
		piece.setLocation(move);
		value = Minimax.getBoardValue(playerPieces, botPieces);
		if (layer < Game.ultraBot.movesAhead+1) {
			int totalChildren = 0, index = 0;
			for(Piece pieces : (piece.getPlayer() == GameData.player.PLAYER_1 ? botPieces : playerPieces)) 
				totalChildren += pieces.getPossibleMovesAI(playerPieces, botPieces).size();
			if(totalChildren == 0) {
				Game.ultraBot.lastGeneration.add(this);
				return;
			}
			children = new Node[totalChildren];
			for(Piece p : (piece.getPlayer() == GameData.player.PLAYER_1 ? botPieces : playerPieces))
				for(int[] m : p.getPossibleMovesAI(playerPieces, botPieces)) 
					children[index++] = new Node(this, layer+1, p.clone(), new int[] {p.getRow(), p.getColumn()}, m, Minimax.deepClone(playerPieces), Minimax.deepClone(botPieces));	
		}else if(layer == Game.ultraBot.movesAhead+1) {
			Game.ultraBot.lastGeneration.add(this);
/*			Node lastNode = getRoot();
			Node[] childs = lastNode.getChildren();
			System.out.println("Child val: " + childs);
			System.out.println("Child last val: " + childs[childs.length-2]) ;
			if(childs[childs.length-1] != null) {
				while(childs != null && childs[childs.length-1] == null) {
					System.out.println("IN LOOP");
					if(childs[childs.length-1] != null) {
						lastNode = childs[childs.length-1];
						childs = lastNode.getChildren();
						System.out.println("REACHEDD!!!!");
					}else
						return;
				}
			}
			else
				System.out.println("didnt do it!");
			System.out.println("Loop finished!!!");
			if(this == lastNode || this.equals(lastNode)) {
				System.out.println("\n\n\n\n\n\n\n\ngot to this");
				Game.ultraBot.initialLayerFinished[Minimax.getIndex(getRoot(), Game.ultraBot.initialLayer)] = true;
			}else {
				System.out.println("-------------------------------------------------------");
			//	System.out.println("This node: " + this + " with layer " + layer);
			//	System.out.println(parent);
			//	System.out.println("\nCalculated node: " + lastNode + " with layer " + lastNode.layer);
			//	if(lastNode.getParent() != null)
				//	System.out.println("Length: " + lastNode.getParent().getChildren().length);
			//	else
				//	System.out.println("parent is null!!");
			//	System.out.println("Index: " + Minimax.getIndex(lastNode, lastNode.getParent().getChildren()));
			//	System.out.println("Length of parent children: " + lastNode.getParent().getChildren().length);
				System.out.println("not equal!!!");
			}*/
 		}
		
	}
	
	public Node getParent() {return parent;}
	
	public Piece getPiece() {return piece;}
	
	public int[] getOriginalLocation(){return originalLocation;}
	
	public int[] getMove() {return move;}
	
	public int getValue() {return value;}
	
	public int getLayer() {return layer;}
	
	public Node getBestChild() {return Minimax.getBestNode(children);}
	
	public Node getLastChild() {return children[children.length-1];}

	public Node[] getChildren() {return children;}

	public Node getRoot() {
		if(layer == 1)
			return this;
		Node highest = parent;
		while(highest.getParent() != null)
			highest = highest.getParent();
		return highest;
	}

/*	public String toString() {
		return "\nPiece: " + getPiece().getType() + "\nPlayer: " + piece.getPlayer() + "\nOriginal Location: [" + originalLocation[0] + ", " + originalLocation[1] + "]"
				+ "\nMove Location: [" + move[0] + ", " + move[1] + "]\nValue: " + value;
	}*/
	
}
