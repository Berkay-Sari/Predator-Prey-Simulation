import java.util.ArrayList;

public class Hunter extends Creature {
	private boolean isEat;
	public Hunter(Environment grid) {
		super(grid);
		isEat = false;
		setStarveCounter(0);
		setName("H");
	}

	//yukarý : 1 asagý : 2, sag : 3, sol : 4.
	public int move(Creature[][] grid) {
		ArrayList<Integer> preyLocations = new ArrayList<Integer>();
		preyLocations.clear();
		isEat=false;
		//av olan yerleri belirle 
		if (getX()>0  && grid[getX()-1][getY()] != null && grid[getX()-1][getY()] instanceof Prey) {
			preyLocations.add(1); 
			isEat = true;
		}
		if (getX()<getM()-1 && grid[getX()+1][getY()] != null && grid[getX()+1][getY()] instanceof Prey) {
			preyLocations.add(2); 
			isEat = true;
		}
		if (getY()<getN()-1 && grid[getX()][getY()+1] != null && grid[getX()][getY()+1] instanceof Prey) {
			preyLocations.add(3);
			isEat = true;
		}
		if (getY()>0  && grid[getX()][getY()-1] != null && grid[getX()][getY()-1] instanceof Prey) {
			preyLocations.add(4);
			isEat = true;
		}
		//eger av varsa, av'in bulundugu konumlardan rastgele birini sec ve ye.
		if(isEat) {
			int index = (int)(Math.random() * preyLocations.size());
			return preyLocations.get(index);
		}
		//yoksa rastgele bir yon don
		else {
			return super.move(grid);
		}
	}
	
	public int breed(Creature[][] grid) {
		setBreedCounter(getBreedCounter()+1);
		if (getBreedCounter() >= 8) {
			setBreedCounter(0);
			ArrayList<Integer> emptyCells = new ArrayList<Integer>();
			//bos olan yerleri belirle 
			if (getX()>0  && grid[getX()-1][getY()] == null) 
				emptyCells.add(1); 
			if (getX()<getM()-1 && grid[getX()+1][getY()] == null) 
				emptyCells.add(2); 
			if (getY()<getN()-1 && grid[getX()][getY()+1] == null) 
				emptyCells.add(3);
			if (getY()>0  && grid[getX()][getY()-1] == null) 
				emptyCells.add(4);
			if(emptyCells.size() == 0) { 
				setBreedCounter(7);
				return -1;
			}
			int index = (int)(Math.random() * emptyCells.size());
			return emptyCells.get(index);
		}
		return -1;
	}
	public boolean starve() {
		return getStarveCounter() >= 3;
	}
	public boolean getIsEat() {
		return isEat;
	}

}
