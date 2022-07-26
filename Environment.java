import java.util.Scanner;

class OutofThisWorldCreatureException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	String exception_message;
	public OutofThisWorldCreatureException(String message) {
		super(message);
		exception_message = message;
	}
	public String getMessage() {
		return "Hata: " + exception_message;
	}
}
public class Environment {

	//Gerekli veri uyeleri, constructorlar vs.
	private Creature[][] grid;
	private int preys;
	private int hunters;
	private int m,n,mode;

	public Environment(int m, int n, int obstacles, int mode) {
		this.m = m;
		this.n = n;
		this.mode = mode;
		grid = new Creature[m][n];
		for(int i = 0; i < obstacles; i++) {
			Creature obstacle = new Creature(this);
			obstacle.setName("O");
			putCreature(obstacle);
		}
	}

	//(x,y) koordinatindaki organizmayi don.
	public Creature get(int x, int y) {
		return grid[x][y];
	}
	
	//Simulasyonu bir step ilerlet. Yukar�daki canl� (Creature)
	//ozelliklerine uymayan bir canl� gride kondugunda
	//OutofThisWorldCreatureException atmal�d�r.
	public void step() throws OutofThisWorldCreatureException {
		printGrid();
		
		//region: grid'deki canlilarin hareket ettigini temsil eden bool degerini setle
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if(grid[i][j]==null)
					continue;
				grid[i][j].setIsMoved(false);
			}
		}
		//endregion
		
		
		//move metodu icinde dolu kareye hareket etme durumu handle edildigi icin OutofThisWorldException yazsak da asla atmayacak.
		//region: Avcilari hareket ettir. Av varsa rastgele birini sec ye.
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if(grid[i][j]!=null && grid[i][j] instanceof Hunter) {
					int yon=0;
					if(!canHunterMove(i, j)) {
						/* Hareket edemeyen Avci hatasi cok yuksek ihtimalle olusacagidan 
						 * bu exception program akisini bozmamak icin comment'e alinmistir
						 * Test etmek icin uncomment edilebilir.
						 * Hata oncesi grid'in son hali ekrana basildigindan hata'nin dogrulugu gozlemlenebilir.
						 */
						//printGrid();
						//throw new OutofThisWorldCreatureException("Hareket edemeyen Avci(" + (j+1) + "," + (i+1) + ")");
						grid[i][j].setIsMoved(true); 
						continue;
					}
					if(grid[i][j].getIsMoved() == false) {
						grid[i][j].setIsMoved(true); 
						grid[i][j].setStarveCounter(grid[i][j].getStarveCounter()+1);
						yon = grid[i][j].move(grid);
						if(((Hunter) grid[i][j]).getIsEat()) {
							//Yemek yediyse starve counterini sifirla
							preys--;
							grid[i][j].setStarveCounter(0);
						}
						if(yon == 1) { //Yukari
							grid[i-1][j] = grid[i][j];   
							grid[i][j] = null;  		
							grid[i-1][j].setX(grid[i-1][j].getX()-1); 
						}
						else if(yon == 2) { //Asagi
							grid[i+1][j] = grid[i][j];   
							grid[i][j] = null;	  		
							grid[i+1][j].setX(grid[i+1][j].getX()+1); 
						}
						else if(yon == 3) { //Sag
							grid[i][j+1] = grid[i][j];   
							grid[i][j] = null;  		
							grid[i][j+1].setY(grid[i][j+1].getY()+1); 
						}
						else if(yon == 4) { //Sol
							grid[i][j-1] = grid[i][j];   
							grid[i][j] = null; 		
							grid[i][j-1].setY(grid[i][j-1].getY()-1); 
						}
						else throw new OutofThisWorldCreatureException("Beklenmeyen Yon");
					}
				}
			}
		}
		//endregion
		
		
		//region: Av'lari hareket ettir.
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if(grid[i][j] != null && grid[i][j] instanceof Prey) {
					if(!canPreyMove(i, j)) {
						/* Hareket edemeyen Av hatasi cok yuksek ihtimalle olusacagidan 
						 * bu exception program akisini bozmamak icin comment'e alinmistir
						 * Test etmek icin uncomment edilebilir.
						 * Hata oncesi grid'in son hali ekrana basildigindan hata'nin dogrulugu gozlemlenebilir.
						 */
						//printGrid();
						//throw new OutofThisWorldCreatureException("Hareket edemeyen Av(" + (j+1) + "," + (i+1) + ")");
						grid[i][j].setIsMoved(true); 
						continue;
					}
					if(grid[i][j].getIsMoved() == false) {
						grid[i][j].setIsMoved(true);
						int yon = grid[i][j].move(grid);
						if(yon == 1) {
							grid[i-1][j] = grid[i][j];   
							grid[i][j] = null;		
							grid[i-1][j].setX(grid[i-1][j].getX()-1);    
						}
						else if(yon == 2) {
							grid[i+1][j] = grid[i][j]; 
							grid[i][j] = null; 
							grid[i+1][j].setX(grid[i+1][j].getX()+1); 

						}
						else if(yon == 3) {
							grid[i][j+1] = grid[i][j];   
							grid[i][j] = null;		
							grid[i][j+1].setY(grid[i][j+1].getY()+1);  
						}
						else if(yon == 4) {
							grid[i][j-1] = grid[i][j];   
							grid[i][j] = null;		
							grid[i][j-1].setY(grid[i][j-1].getY()-1);  
						}
						else throw new OutofThisWorldCreatureException("Beklenmeyen Yon");
					}
				}
			}
		}
		//endregion
		
		
		//region: Olmesi gereken Avci'larin yasamina son ver. Avci sayisini azalt.
		//burda olmesi gereken avci'nin olmeme ihtimali bulunmamakta. Dolayisiyla OutofThisWorldException yazsak da asla atmayacak.
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if(grid[i][j] != null && grid[i][j] instanceof Hunter) {
					if (grid[i][j].starve()) {
						grid[i][j] = null;
						hunters--;
					}
				}
			}
		}
		//endregion
		
		
		//region: Hayatta kalan Avci'lardan ureme vakti gelenleri cogalt.
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if (grid[i][j] instanceof Hunter && grid[i][j].getIsMoved()) {
					int yon = grid[i][j].breed(grid);
					if(yon != -1) {
						if(yon == 1) {
							putCreature(new Hunter(this), i-1, j);   		   
						}
						else if(yon == 2) {
							putCreature(new Hunter(this), i+1, j);  
						}
						else if(yon == 3) {
							putCreature(new Hunter(this), i, j+1);  
						}
						else if(yon == 4) {
							putCreature(new Hunter(this), i, j-1);   
						}
						else throw new OutofThisWorldCreatureException("Beklenmeyen Yon");
					}
				}
			}
		}
		//endregion
		
	
		//region: Hayatta kalan Av'lardan ureme vakti gelenleri cogalt.
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if (grid[i][j] instanceof Prey && grid[i][j].getIsMoved()) {
					int yon = grid[i][j].breed(grid);
					if(yon != -1) {
						if(yon == 1) {
							putCreature(new Prey(this), i-1, j);   		   
						}
						if(yon == 2) {
							putCreature(new Prey(this), i+1, j);   
						}
						if(yon == 3) {
							putCreature(new Prey(this), i, j+1);    
						}
						if(yon == 4) {
							putCreature(new Prey(this), i, j-1);
						}
					}
				}
			}
		}
		//endregion 
		userInput();
	}
	//Gerekli diger metotlar...
	private void userInput() {
		if(mode == 0) {
			System.out.print("Devam etmek icin 'n' harfini giriniz: ");
			String input = getScanner().nextLine();
			if(!input.equalsIgnoreCase("n")) {
				System.out.println("\tSIMULASYON SONLANDI");
				System.exit(0);
			}
		}
		else if(mode == 1) {
			System.out.println();
			//Simulasyon modunda grid'i step step gozlemlemek icin 1 saniye delay.
			//region: delay
			try {
			    Thread.sleep(1000);
			} catch (InterruptedException ie) {
			    Thread.currentThread().interrupt();
			}
			//endregion
		}
		else throw new OutofThisWorldCreatureException("Calisma Modu(" + mode + ") tanimlanmamis.");
	}
	
	private Scanner getScanner() {
		return new Scanner(System.in);
	}

	public void putCreature(Creature creature) {
		int x = (int)(Math.random() * (m));
		int y = (int)(Math.random() * (n));

		while (grid[x][y] != null) {
			x = (int)(Math.random() * (m));
			y = (int)(Math.random() * (n));
		}
		creature.setX(x);
		creature.setY(y);
		grid[x][y] = creature;
		if(creature instanceof Hunter)
			hunters++;
		else if(creature instanceof Prey)
			preys++;
		else if(!creature.getName().equals("O"))
			throw new OutofThisWorldCreatureException("Creature ozelliklerine uymayan bir canli gride konuldu");
	}
	
	private void putCreature(Creature creature, int x, int y) {
		creature.setX(x);
		creature.setY(y);
		grid[x][y] = creature;
		if(creature instanceof Hunter)
			hunters++;
		if(creature instanceof Prey)
			preys++;
	}
	
	private boolean canHunterMove(int x, int y) { //returns true if Hunter(x,y) can move
		boolean yukari = (x>0 && grid[x-1][y] instanceof Prey) || (x>0 && grid[x-1][y] == null);
		boolean asagi = (x<m-1 && grid[x+1][y] instanceof Prey) || (x<m-1 && grid[x+1][y] == null);
		boolean sol = (y>0 && grid[x][y-1] instanceof Prey) || (y>0 && grid[x][y-1] == null);
		boolean sag = (y<n-1 && grid[x][y+1] instanceof Prey) || (y<n-1 && grid[x][y+1] == null);
		return yukari || asagi || sag || sol;
	}
	
	private boolean canPreyMove(int x, int y) { //returns true if Prey(x,y) can move 
		boolean yukari = (x>0  && grid[x-1][y] == null);
		boolean asagi = (x<m-1 && grid[x+1][y] == null);
		boolean sol = (y>0    && grid[x][y-1] == null);
		boolean sag =  (y<n-1 && grid[x][y+1] == null);
		return  yukari || asagi || sag || sol;
	}

	public int getM() {
		return m;
	}

	public int getN() {
		return n;
	}
	
	private void printGrid() {
		System.out.print("    ");
		for(int j=1; j <= n; j++) {
			if(j < 9) {
				System.out.print(j + "  ");
			}
			else {
				System.out.print(j + " ");
			}
		}
		System.out.println();
		for(int i=1; i<=m; i++) {
			if(i<10) 
				System.out.print(i+"   ");
			else
				System.out.print(i+"  ");
			for (int j=0; j<n; j++) {
				if (grid[i-1][j]==null)
					System.out.print(".  ");
				else
					System.out.print(grid[i-1][j].getName() + "  ");  
			}
			System.out.println();
		}
		int numPreys = 0;
		int numHunters = 0;
		for(int i = 0; i < m; i++) {
			for(Creature o : grid[i]) {
				if(o instanceof Prey) 
					numPreys++;
				if(o instanceof Hunter) 
					numHunters++;
			}
		}
		if(preys != numPreys) throw new OutofThisWorldCreatureException("Grid uzerindeki Av sayisi yanlis hesaplandi");
		if(hunters != numHunters) throw new OutofThisWorldCreatureException("Grid uzerindeki Avci sayisi yanlis hesaplandi");
	}
	
	public void info() { 
		System.out.println("Prey : " + preys + ", Hunter : " + hunters);
	}
}