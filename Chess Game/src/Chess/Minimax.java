package Chess;

import java.io.IOException;
import java.util.ArrayList;

public class Minimax {

	ArrayList<Piece> playerPieces, botPieces;
	ArrayList<Node> lastGeneration;
	GameData.player player;
	int movesAhead;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		this.movesAhead = movesAhead;
		lastGeneration = new ArrayList<Node>();
	}
	
	public void move() {
		if(player == GameData.player.PLAYER_1) {
			playerPieces = (Game.player2Pieces);
			botPieces = (Game.player1Pieces);
		}else{
			playerPieces = (Game.player1Pieces);
			botPieces = (Game.player2Pieces);
		}
		lastGeneration.clear();
		ArrayList<Node> initialLayer = new ArrayList<Node>();
		//	botPieces.forEach(piece -> {
		//	piece.getPossibleMovesAI(playerPieces, botPieces).forEach(move -> {initialLayer.add(new Node(null, 1, piece, new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));});
	//	});
//		d = 0;
		
		System.out.print("\033[H\033[2J");  
		System.out.flush();   
		boolean bo = Piece.isInCheck(player, Game.player1Pieces, Game.player2Pieces);
		for(Piece piece : botPieces)
			if(piece.getPossibleMovesAI(Game.player1Pieces, Game.player2Pieces).size()>0)
				for(int[] move : piece.getPossibleMovesAI(Game.player1Pieces, Game.player2Pieces))
					initialLayer.add(new Node(null, 1, piece.clone(), new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));
		
		int d = 0;
		for(Piece piece : Game.player2Pieces)
			d+=piece.getPossibleMovesInCheck().size();
		Node bestNode = null;
		int lowestVal = 100000;
		System.out.println("Last generation size: " + lastGeneration.size());
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		for(Node node : lastGeneration) {
			System.out.println("-------------------------------------------\n"+node);
		//	System.out.println("WOW!!");
			if(node.getValue() < lowestVal) {
				bestNode = node;
				lowestVal = node.getValue();
			} 
		}
		System.out.println("\n--------------------------------------------------");
		System.out.println("-------------\nBEST NODE\n-------------------------------------------\n"+bestNode.getRoot()+"\n--------------------------------------------------");
		Game.prevTileClicked = bestNode.getRoot().getOriginalLocation();
//		System.out.println("Prev tile clicked: " + Game.prevTileClicked[0] + ", " + Game.prevTileClicked[1]);
		Game.tileClicked = bestNode.getRoot().getMove();
//		System.out.println("Tile clicked: " + Game.tileClicked[0] + ", " + Game.tileClicked[1]);

		Game.onValidMoveClick();
		System.out.println("Initial Layer Size: "  + initialLayer.size());
		System.out.println("Game Piece Size: " + d);
		System.out.println("KING WAS IN CHECK: " + bo);

	}

	//Returns board value for piece after moving to certain location
	public static int getBoardValue(ArrayList<Piece> playerPieces, ArrayList<Piece> botPieces) {
		int boardValue = 0;
		for(Piece own : botPieces) 
			boardValue -= own.getPieceValue();
		for(Piece opponent : playerPieces) 
			boardValue += opponent.getPieceValue();
		return boardValue;
	}
	
	public static Node getBestNode(Node[] children) {
		Node bestNode = null;
		int highestVal = -1000000, lowestVal = 1000000;
		for(Node node : children) {
			switch(node.getPiece().getPlayer()) {
			case PLAYER_1:
				if(node.getValue() > highestVal) {
					bestNode = node;
					highestVal = bestNode.getValue();
				}
				break;
			case PLAYER_2:
				if(node.getValue() < lowestVal) {
					bestNode = node;
					lowestVal = bestNode.getValue();
				}
				break;
			}
		}
		return bestNode;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Piece> clone(ArrayList<Piece> arrList){
		return (ArrayList<Piece>) arrList.clone();
	}
	
	public static ArrayList<Piece> deepClone(ArrayList<Piece> arrList){
		ArrayList<Piece> arrListCopy = new ArrayList<Piece>();
		for(Piece piece : arrList)
			arrListCopy.add(piece.clone());
		return arrListCopy;
	}
	
}
