package Chess;

import java.util.ArrayList;

public class Minimax {

	private ArrayList<Piece> playerPieces, botPieces;
	public ArrayList<Node> lastGeneration;
	public Node[] initialLayer;
	private Node[][] bestMoves;
	public GameData.player player;
	public int movesAhead, totalNodes = 0;
	private int count = 0;
	public boolean[] initialLayerFinished;
	public boolean inMove = false;
	
	public Minimax(GameData.player player, int movesAhead) {
		this.player = player;
		this.movesAhead = movesAhead;
	}
	
	public void move() {
		inMove = true;
		count = 0;
		lastGeneration = new ArrayList<Node>();
		if(player == GameData.player.PLAYER_1) {
			playerPieces = deepClone(Game.player2Pieces);
			botPieces = deepClone(Game.player1Pieces);
		}else{
			playerPieces = deepClone(Game.player1Pieces);
			botPieces = deepClone(Game.player2Pieces);
		}
		for(Piece piece : botPieces)
			for(int[] move : piece.getPossibleMovesAI(playerPieces, botPieces))
				count++;
		initialLayer = new Node[count-1];
		initialLayerFinished = new boolean[count-1];
		count = 0;
		for(Piece piece : botPieces)
				for(int[] move : piece.getPossibleMovesAI(playerPieces, botPieces)) {	
					new Thread(new Runnable() {
						public void run() {
							initialLayer[count++] = (new Node(null, 1, piece.clone(), new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));
						}
					}).start();
				}
		
		boolean complete = false;
		outerLoop:
		while(complete == false) {
			System.out.println("Initial layer not added???? Size: " + initialLayer.length);
			for(Node node : initialLayer) {
				System.out.println(node);
				if(node == null)
					continue outerLoop;
			}
			complete = true;
		}
		
	/*	for(int i = 0; i < initialLayer.length; i++) {
			Node lastChild = initialLayer[i];
			while(lastChild.getLayer() < movesAhead+1) {
				while(lastChild.getLastChild() == null)
					;
				lastChild = lastChild.getLastChild();
			}
			initialLayerFinished[i] = true;
		}*/
		
		System.out.println("Initial Layer has been initializedd!!!");
		
		bestMoves = new Node[initialLayer.length][movesAhead];
		for(int i = 0; i < initialLayer.length; i++) {
			bestMoves[i][0] = initialLayer[i].getBestChild();
			for(int i2 = 1; i2 < movesAhead; i2++)
				bestMoves[i][i2] = bestMoves[i][i2-1].getBestChild();
		}
		ArrayList<Node> finalNodes = new ArrayList<Node>();
		for(int i = 0; i < initialLayer.length; i++)
			finalNodes.add(bestMoves[i][movesAhead-1]);
		for(Node n : finalNodes){
			System.out.println(n);
		}
		System.out.println("-------------------------------------------------");
		System.out.println(finalNodes);
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
	
	public static int getIndex(Node node, Node[] arr) {
		for(int i = 0; i < arr.length; i++) 
			if(arr[i].equals(node))
				return i;
			return -1;		
	}
	
	public static int getIndex(Node node, ArrayList<Node> list) {
		for(int i = 0; i < list.size(); i++) 
			if(list.get(i).equals(node))
				return i;
			return -1;
	}
	
}
