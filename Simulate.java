

public class Simulate {
	public static void main(String[] args) {
		int m = 20, n = 20;
		//simulasyon mu (1), interaktif mi (0)?
		int mode = 0;
		int obstacles = 3;
		//obstacles kadar engeli, mxn boyutlu gride rastgele yerlestirir.
		Environment grid = new Environment(m,n,obstacles,mode);
		int numberOfHunters = 5;
		Creature org;
		for (int i = 0; i < numberOfHunters; i++) {
			org = new Hunter(grid);
			//canliyi bos olan rastgele konuma yerlestir.
			grid.putCreature(org);
		}
		int numberOfPreys = 100;
		for (int i = 0; i < numberOfPreys; i++) {
			org = new Prey(grid);
			grid.putCreature(org);
		}
		for (int i = 0; i < 10; i++) {
			//Ortama yukarıda bahsedilenden farklı (belki daha zeki)
			//davranabilen 10 adet yeni avcı yerlestir. Burada tek kısıt,
			//yeni tanımlanan avcının da bir Hunter olması.
			//org = new UltimateHunter(grid);
			//grid.putCreature(org);
		}
		int simulationSteps = 900;
		for (int i = 0; i < simulationSteps; i++) {
			//Simulasyonu bir zaman dilimi ilerlet.
			grid.step();
			if (i % 100 == 0)
				//kac av kac avcı kaldıgını asagıdaki formatta basar.
				//Prey : 34, Hunter : 55, UltimateHunter : 123
				grid.info();
		}
	}
}
