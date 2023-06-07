import java.util.Collections;
import java.util.Arrays;
import nurses.*;

public class NurseSolver {

	public int[][] algorithm(ProblemInstance p, long timelimit, int stucklimit) {
		long startT = System.currentTimeMillis();
		int N = p.N;
		int D = p.D;
		int[][] sol = new int[N][D];
		
		kN = N - (int) ((double) N * 2 / 3);
		kC = p.nUsedConstraints - (int) ((double) p.nUsedConstraints * 2 / 3);

		int[] nursesArray = new int[N];
		for (int i = 0; i < N; i++) {
			nursesArray[i] = i;
		}
		List<int> nurses = Arrays.asList(constraintsArray);

		int[] constraintsArray = new int[p.nUsedConstraints];
		for (int i = 0; i < p.nUsedConstraints; i++) {
			constraintsArray[i] = i;
		}
		List<int> constraints = Arrays.asList(constraintsArray);
		
		int[] daysArray = new int[D];
		for (int i = 0; i < D; i++) {
			daysArray[i] = i;
		}
		List<int> days = Arrays.asList(daysArray);

		repair(int[][] sol, ProblemInstance p, days);
		
		int nonImprovingCounter = 0;
		while (System.currentTimeMillis() - startT < timelimit) {
			cursol = copy(sol);
			destroy(int[][] sol, ProblemInstance p, nurses, kN, constraints, kC);
			repair(int[][] sol, ProblemInstance p, days);
			if (p.EvaluateAll(sol) > p.EvaluateAll(cursol)) {
				nonImprovingCounter = 0;
			} else {
				sol = cursol;
				nonImprovingCounter++;
			}
			if (nonImprovingCounter >= stucklimit) {
				break;
			}
		}
		return sol;
	}

	public void repair(int[][] sol, ProblemInstance p, List<int> days) {
		Collections.shuffle(days);
		for (int day = 0; day < p.D; day++) {
			d = days.get(day);
			for (int s = 1; s < p.S; s++) {
				int count;
				for (int i = 0; i < p.N; i++) {
					if (sol[i][d] == s) {
						count++;
					}
				}
				int demand = p.demands[d][s];
				while (count > demand) {
					boolean first = true;
					int min = 0;
					int minval = 0;
					for (int i = 0; i < p.N; i++) {
						if (sol[i][d] == s) {
							int impact = p.ImpactOfChange(sol, i, d, 0);
							if (first || impact <= minval) {
								min = i;
								minval = impact;
							}
						}
					}
					sol[min][d] = 0;
				}
				while (count < demand) {
					boolean first = true;
					int min = 0;
					int minval = 0;
					for (int i = 0; i < p.N; i++) {
						if (sol[i][d] == 0) {
							int impact = p.ImpactOfChange(sol, i, d, s);
							if (first || impact <= minval) {
								min = i;
								minval = impact;
							}
						}
					}
					sol[min][d] = s;
				}
			}
		}
	}

	public void destroy(int[][] sol, ProblemInstance p, List<int> nurses, int kN, List<int> constraints, int kC) {
		Collections.shuffle(nurses);
		Collections.shuffle(constraints);
		for (int i = 0; i < kN; i++) {
			for (int c = 0; c < kC; c++) {
				int nurse = nurses.get(i);
				int constraint = constraints.get(c);
				p.Enforce(sol, nurse, constraint);
			}
		}
	}


	public int[][] copy(int[][] sol) {
		N = sol.length;
		D = sol[0].length;

		int[][] solcopy = new int[N][D];
		for (int i = 0; i < N; i++) {
			for (int d = 0; d < D; d++) {
				solcopy[i][d] = sol[i][d];
			}
		}
		return solcopy;
	}
}
