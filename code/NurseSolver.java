import java.lang.Double;
import java.util.Random;
import nurses.*;
import Helper.XMLParser;
import Helper.Constraint;
import Attributes.SchedulingPeriod;

public class NurseSolver {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Wrong number of arguments. Need 2: instance name and time allowance.");
			System.exit(1);
		}
		String filename = args[0];
		double time = Double.parseDouble(args[1]);
		long millisecondsTime = (long) time*1000;

		XMLParser parser = new XMLParser(filename);
		SchedulingPeriod sp = parser.parseXML();
		ProblemInstance p = Convert.convertProblem(sp);
		int[][] finalsol = algorithm(p, millisecondsTime);
		System.out.println(p.EvaluateAll(finalsol));

/*		for (int i = 0; i < finalsol.length; i++) {
			for (int d = 0; d < finalsol[0].length; d++) {
				System.err.printf("%d\t", finalsol[i][d]);
			}
			System.err.println();
		}

		Constraint dks = new Constraint(sp, Convert.convertSolutionBack(finalsol));
		System.err.println(dks.calcRosterScore());

		System.err.println(p.Evaluate(finalsol, 0));
		for (int c = 0; c < p.constraintIDs.length; c++) {
			System.err.println(p.Report(finalsol, 0, c));
		} */
	}

	public static int[][] algorithm(ProblemInstance p, long timelimit) {
		long startT = System.currentTimeMillis();
		int N = p.N;
		int D = p.D;
		int[][] sol = new int[N][D];

		int[] nurses = new int[N];
		for(int i=0; i < N; i++){
			nurses[i] = i;
		}
		
		int kN = N - (int) ((double) N * 2/3);
		int kC = p.nUsedConstraints - (int) ((double) p.nUsedConstraints * 2/3);
		
		int[] constraints = new int[p.nUsedConstraints];
		for(int i=0; i < p.nUsedConstraints ; i++){
			constraints[i] = i;
		}
		
		int[] days = new int[D];
		for(int i=0; i < D; i++){
			days[i] = i;
		}
		
		repair(sol, p, days, nurses);
		
		//Random r = new Random();

		DynamicParams g = new DynamicParams(N, p.nUsedConstraints);
		
		int iters = 0;
		while (System.currentTimeMillis() - startT < timelimit) {
			int[][] cursol = copy(sol);
			//int kN = r.nextInt(N) + 1;
			//int kC = r.nextInt(p.nUsedConstraints) + 1;
			g.sample();
			int kN = g.kN;
			int kC = g.kC;
			destroy(sol, p, nurses, kN, constraints, kC);
			repair(sol, p, days, nurses);
			if (p.EvaluateAll(sol) > p.EvaluateAll(cursol)) {
				sol = cursol; // failure
				g.update(false);
			} else {
				g.update(true); // success
			}
			iters++;
		}
		//g.report();
		//System.err.println(iters);
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
		/*			int min = -1;
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
					sol[min][d] = 0; */
					for (int n = 0; n < p.N; n++) {
						int i = nurses[n];
						if (sol[i][d] == s) {
							sol[i][d] = 0;
							break;
						}
					}

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
		/*			int min = -1;
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
					sol[min][d] = s; */
					for (int n = 0; n < p.N; n++) {
						int i = nurses[n];
						if (sol[i][d] == 0) {
							sol[i][d] = s;
							break;
						}
					}
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
