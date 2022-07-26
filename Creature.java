import java.util.ArrayList;

public class Creature {
	private String name; 
	//Gerekli veri uyeleri, constructorlar vs.
	private int x,y;		
	private boolean isMoved;	
	private int breedCounter;	
	private int starveCounter;
	private int m,n;
	public Creature(Environment grid) {
		m = grid.getM();
		n = grid.getN();
	}
	public boolean getIsMoved() {
		return isMoved;
	}

	public void setIsMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}
	//yukarý : 1 asagi : 2, sag : 3, sol : 4.
	public int move(Creature[][] grid) {
		ArrayList<Integer> emptyLocations = new ArrayList<Integer>();
		//bos olan yerleri belirle
		if (getX()>0  && grid[getX()-1][getY()] == null) 
			emptyLocations.add(1); 
		if (getX()<getM()-1 && grid[getX()+1][getY()] == null) 
			emptyLocations.add(2); 
		if (getY()<getN()-1 && grid[getX()][getY()+1] == null) 
			emptyLocations.add(3);
		if (getY()>0  && grid[getX()][getY()-1] == null) 
			emptyLocations.add(4);
		int index = (int)(Math.random() * emptyLocations.size());
		return emptyLocations.get(index);
	}
	//Cevresindeki bos bir hucrede ure. move() ile ayný sekilde hucreyi don. -1 : Ureme (No breeding).
	public int breed(Creature[][] grid) {
		return -1;
	}
	//Canlý aclýktan oldu mu? (Avlar hic bir zaman aclýktan olmuyor.)
	public boolean starve() {
		return false;
	}
	//Prey, Hunter, UltimateHunter
	public String getName() {
		return name;
	}
	//Diger gerekli metotlar...
	public void setName(String name) {
		this.name = name;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getBreedCounter() {
		return breedCounter;
	}
	public void setBreedCounter(int breedCounter) {
		this.breedCounter = breedCounter;
	}
	public int getStarveCounter() {
		return starveCounter;
	}
	public void setStarveCounter(int starveCounter) {
		this.starveCounter = starveCounter;
	}
	public int getM() {
		return m;
	}
	public int getN() {
		return n;
	}
}