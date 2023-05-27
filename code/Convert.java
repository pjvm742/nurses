import java.util.List;

public class Convert {

	public static int[][] convertSolution(List<int[][]> orig) {
		int D = orig.Length();
		int[][] day = orig.get(0);
		int S = day.length + 1;
		int N = day[0].length;

		int[][] sol = new int[N][D];

		for (int d = 0; d < D; d++) {
			day = orig.get(d);
			for (int i = 0; i < N; i++) {
				for (int s = 0; s < S - 1; s++) {
					if (day[s][i] == 1) {
						sol[i][d] = s + 1;
					}
					// if not working any shift, sol[i][d] stays at 0
				}
			}
		}
		
		return sol;
	}

}
