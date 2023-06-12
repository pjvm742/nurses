import java.util.Arrays;
import java.util.Random;
import nurses.*;
import Helper.XMLParser;
import Attributes.SchedulingPeriod;

public class NurseSolver {

	public static void main(String[] args) throws Exception {
		String filename = "long01";

		XMLParser parser = new XMLParser(filename);
		SchedulingPeriod sp = parser.parseXML();
		ProblemInstance p = Convert.convertProblem(sp);
		int[][] finalsol = algorithm(p, 10000, 2000);
		for(int i = 0; i < finalsol.length; i++) {
			//System.out.println(Arrays.toString(finalsol[i]));
		}
		System.out.println(p.EvaluateAll(finalsol));
	}

	public static int[][] algorithm(ProblemInstance p, long timelimit, int stucklimit) {
		long startT = System.currentTimeMillis();
		int N = p.N;
		int D = p.D;
		int[][] sol = new int[N][D];

		int[] nurses = new int[N];
		for(int i=0; i < N; i++){
			nurses[i] = i;
		}

		int[] constraints = new int[p.nUsedConstraints];
		for(int i=0; i < p.nUsedConstraints ; i++){
			constraints[i] = i;
		}
		
		int[] days = new int[D];
		for(int i=0; i < D; i++){
			days[i] = i;
		}
		
		repair(sol, p, days, nurses);
		
		Random r = new Random();
		
		int nonImprovingCounter = 0;
		while (System.currentTimeMillis() - startT < timelimit) {
			int[][] cursol = copy(sol);
			int kN = r.nextInt(N) + 1;
			int kC = r.nextInt(p.nUsedConstraints) + 1;
			destroy(sol, p, nurses, kN, constraints, kC);
			repair(sol, p, days, nurses);
			if (p.EvaluateAll(sol) <= p.EvaluateAll(cursol)) {
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

	public static void repair(int[][] sol, ProblemInstance p, int[] days, int [] nurses) {
		permute(days);
		for (int day = 0; day < p.D; day++) {
			permute(nurses);
			int d = days[day];
			for (int s = 1; s < p.S; s++) {
				int count = 0;
				for (int i = 0; i < p.N; i++) {
					if (sol[i][d] == s) {
						count++;
					}
				}
				int demand = p.demands[d][s];
				while (count > demand) {
					boolean first = true;
					int min = -1;
					int minval = 0;
					for (int n = 0; n < p.N; n++) {
						int i = nurses[n];
						if (sol[i][d] == s) {
							int impact = p.ImpactOfChange(sol, i, d, 0);
							if (first || impact <= minval) {
								min = i;
								minval = impact;
							}
						}
					}
					sol[min][d] = 0;
					count--;
				}
			}
			for (int s = 1; s < p.S; s++) {
				int count = 0;
				for (int i = 0; i < p.N; i++) {
					if (sol[i][d] == s) {
						count++;
					}
				}
				int demand = p.demands[d][s];
				while (count < demand) {
					boolean first = true;
					int min = -1;
					int minval = 0;
					for (int n = 0; n < p.N; n++) {
						int i = nurses[n];
						if (sol[i][d] == 0) {
							int impact = p.ImpactOfChange(sol, i, d, s);
							//System.out.println(impact);
							if (first || impact <= minval) {
								min = i;
								minval = impact;
							}
						}
					}
					sol[min][d] = s;
					count++;
				}
			}
		}
	}

	public static void destroy(int[][] sol, ProblemInstance p, int[] nurses, int kN, int[] constraints, int kC) {
		permute(nurses);
		permute(constraints);
		for(int i = 0; i < sol.length; i++) {
			//System.out.println(Arrays.toString(sol[i]));
		}
		for (int i = 0; i < kN; i++) {
			for (int c = 0; c < kC; c++) {
				int nurse = nurses[i];
				int constraint = constraints[c];
				p.Enforce(sol, nurse, constraint);
			}
		}
		for(int i = 0; i < sol.length; i++) {
			//System.out.println(Arrays.toString(sol[i]));
		}
		//System.out.println();
	}


	public static int[][] copy(int[][] sol) {
		int N = sol.length;
		int D = sol[0].length;

		int[][] solcopy = new int[N][D];
		for (int i = 0; i < N; i++) {
			for (int d = 0; d < D; d++) {
				solcopy[i][d] = sol[i][d];
			}
		}
		return solcopy;
	}

	public static void permute(int[] a) {
		int n = a.length;
		Random r = new Random();
		for (int k = n; k > 0; k--) {
			int j = r.nextInt(k);
			int temp = a[j];
			a[j] = a[k-1];
			a[k-1] = temp;
		}
	}
}
