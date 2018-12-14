package Chess;

import java.util.ArrayList;

public class Minimax {

	ArrayList<Piece> playerPieces, botPieces;
	ArrayList<Node> lastGeneration;
	GameData.player player;
	int movesAhead;
	int totalNodes = 0;
	boolean inMove = false;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		this.movesAhead = movesAhead;
		lastGeneration = new ArrayList<Node>();
	}
	
	public void move() {
		inMove = true;
		Node[][] bestMoves = new Node[20][movesAhead];
		if(player == GameData.player.PLAYER_1) {
			playerPieces = deepClone(Game.player2Pieces);
			botPieces = deepClone(Game.player1Pieces);
		}else{
			playerPieces = deepClone(Game.player1Pieces);
			botPieces = deepClone(Game.player2Pieces);
		}
		lastGeneration.clear();
		Node[] initialLayer = new Node[20];	
		for(Piece piece : botPieces)
			if(piece.getPossibleMovesAI(playerPieces, botPieces).size()>0)
				for(int i = 0; i < piece.getPossibleMovesAI(playerPieces, botPieces).size(); i++) 
							initialLayer[i] = (new Node(null, 1, piece.clone(), new int[] {piece.getRow(), piece.getColumn()}, piece.getPossibleMovesAI(playerPieces, botPieces).get(i), deepClone(playerPieces), deepClone(botPieces)));
		Node bestNode = null;
		System.out.println("Last generation size: " + lastGeneration.size());
		bestNode = getBestNode(lastGeneration);
		Game.prevTileClicked = bestNode.getRoot().getOriginalLocation();
		Game.tileClicked = bestNode.getRoot().getMove();
		System.out.println("Prev tile Clicked is: " + Game.prevTileClicked[0] + "," + Game.prevTileClicked[1]);
		System.out.println("Tile Clicked is: " + Game.tileClicked[0] + "," + Game.tileClicked[1]);
		Game.onValidMoveClick();
		System.out.println("TOTAL NODES FOR MOVE: " + totalNodes);
		totalNodes = 0;
		inMove = false;
		lastGeneration.clear();
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
	
	public static Node getBestNode(ArrayList<Node> children) {
		Node bestNode = null;
		int highestVal = -1000000, lowestVal = 1000000;
		for(Node node : children) {
			switch(node.getPiece().getPlayer()) {
			case PLAYER_1:
				if(node.getValue() < lowestVal) {
					bestNode = node;
					lowestVal = bestNode.getValue();
				}
				break;
			case PLAYER_2:
				if(node.getValue() > highestVal) {
					bestNode = node;
					highestVal = bestNode.getValue();
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
