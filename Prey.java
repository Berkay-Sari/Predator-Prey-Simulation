import java.util.ArrayList;

public class Prey extends Creature {

	public Prey(Environment grid) {
		super(grid);
		setName("P");
	}
	
	public int move(Creature[][] grid) {
		return super.move(grid);
	}
	//Cevresindeki bos bir hucrede ure. move() ile ayný sekilde hucreyi don. -1 : Ureme (No breeding).
	public int breed(Creature[][] grid) {
		setBreedCounter(getBreedCounter()+1);
		if (getBreedCounter() >= 3) {
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
				setBreedCounter(2);
				return -1;
			}
			int index = (int)(Math.random() * emptyCells.size());
			return emptyCells.get(index);
		}
		return -1;
	}

}
