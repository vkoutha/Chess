package Chess;

import java.util.ArrayList;

public class Minimax {

	private ArrayList<Piece> playerPieces, botPieces;
	public ArrayList<Node> lastGeneration;
	public ArrayList<Node> initialLayer;
	public boolean[] initialLayerFinished;
	private Node[][] bestMoves;
	public GameData.player player;
	public int movesAhead;
	public int totalNodes = 0;
	public boolean inMove = false;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		this.movesAhead = movesAhead;
	}
	
	public void move() {
		inMove = true;
		lastGeneration = new ArrayList<Node>();
		if(player == GameData.player.PLAYER_1) {
			playerPieces = deepClone(Game.player2Pieces);
			botPieces = deepClone(Game.player1Pieces);
		}else{
			playerPieces = deepClone(Game.player1Pieces);
			botPieces = deepClone(Game.player2Pieces);
		}
		int count = 0;
		for(Piece piece : botPieces)
			for(int[] move : piece.getPossibleMovesAI(playerPieces, botPieces))
				count++;
		initialLayer = new ArrayList<Node>();
		initialLayerFinished = new boolean[count];
		for(Piece piece : botPieces)
			if(piece.getPossibleMovesAI(playerPieces, botPieces).size()>0)
				for(int[] move : piece.getPossibleMovesAI(playerPieces, botPieces)) {
					new Thread(new Runnable() {
						public void run() {
							initialLayer.add(new Node(null, 1, piece.clone(), new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));
						}
					}).start();
				}
		boolean complete = false;
		while(complete == false) {
			complete = true;
			for(boolean finished : initialLayerFinished) {
			//	System.out.println(finished);
				if(finished == false)
					complete = false;
			}
	//		System.out.println("in loop");
		}
		bestMoves = new Node[initialLayer.size()][movesAhead];
		for(int i = 0; i < initialLayer.size(); i++) {
			bestMoves[i][0] = initialLayer.get(i).getBestChild();
			for(int i2 = 1; i2 < movesAhead; i2++)
				bestMoves[i][i2] = bestMoves[i][i2-1].getBestChild();
		}
		ArrayList<Node> finalNodes = new ArrayList<Node>();
		for(int i = 0; i < initialLayer.size(); i++)
			finalNodes.add(bestMoves[i][movesAhead-1]);
		for(Node n : finalNodes){
			System.out.println(n);
		}
		System.out.println("-------------------------------------------------");
		Node bestNode = getBestNode(finalNodes);
		System.out.println("Last generation size: " + lastGeneration.size());
	//	bestNode = getBestNode(lastGeneration);
		Game.prevTileClicked = bestNode.getRoot().getOriginalLocation();
		Game.tileClicked = bestNode.getRoot().getMove();
		System.out.println("Prev tile Clicked is: " + Game.prevTileClicked[0] + "," + Game.prevTileClicked[1]);
		System.out.println("Tile Clicked is: " + Game.tileClicked[0] + "," + Game.tileClicked[1]);
		Game.onValidMoveClick();
		System.out.println("TOTAL NODES FOR MOVE: " + totalNodes);
		System.out.println(bestNode);
		totalNodes = 0;
		inMove = false;
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
	
		public static Node getBestNode(ArrayList<Node> children, GameData.player player) {
			Node bestNode = null;
			int highestVal = -1000000, lowestVal = 1000000;
			for(Node node : children) {
				switch(player) {
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
	
	public static <T> int getIndex(T var, T[] arr) {
		for(int i = 0; i < arr.length; i++)
			if(var == arr[i] || var.equals(arr[i]))
				return i;
		return -1;
	}
	
	public static <T> int getIndex(T var, ArrayList<T> arrList) {
		for(int i = 0; i < arrList.size(); i++)
			if(var == arrList.get(i) || var.equals(arrList.get(i)))
				return i;
		return -1;
	}
	
}
