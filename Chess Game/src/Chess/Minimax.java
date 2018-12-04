package Chess;

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
			playerPieces = deepClone(Game.player2Pieces);
			botPieces = deepClone(Game.player1Pieces);
		}else{
			playerPieces = deepClone(Game.player1Pieces);
			botPieces = deepClone(Game.player2Pieces);
		}
		lastGeneration.clear();
		ArrayList<Node> initialLayer = new ArrayList<Node>();
		//	botPieces.forEach(piece -> {
		//	piece.getPossibleMovesAI(playerPieces, botPieces).forEach(move -> {initialLayer.add(new Node(null, 1, piece, new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));});
	//	});
//		d = 0;
		for(Piece piece : botPieces){
			for(int[] move : piece.getPossibleMovesAI(playerPieces, botPieces))
				initialLayer.add(new Node(null, 1, piece.clone(), new int[] {piece.getRow(), piece.getColumn()}, move, deepClone(playerPieces), deepClone(botPieces)));
		}
		
		System.out.println("Initial Layer size: "  + initialLayer.size());
		Node bestNode = null;
		int lowestVal = 100000;
		System.out.println("Last generation size: " + lastGeneration.size());
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		for(Node node : lastGeneration) {
			if(node != null)
			System.out.println("-------------------------------------------\n"+node+"\n--------------------------------------------------");
			System.out.println("WOW!!");
			if(node.getValue() < lowestVal) {
				bestNode = node;
				lowestVal = node.getValue();
			} 
		}
	//	System.out.println("-------------------------------------------\n"+bestNode.getRoot()+"\n--------------------------------------------------");
		Game.prevTileClicked = bestNode.getRoot().getOriginalLocation();
//		System.out.println("Prev tile clicked: " + Game.prevTileClicked[0] + ", " + Game.prevTileClicked[1]);
		Game.tileClicked = bestNode.getRoot().getMove();
//		System.out.println("Tile clicked: " + Game.tileClicked[0] + ", " + Game.tileClicked[1]);

		Game.onValidMoveClick();
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
