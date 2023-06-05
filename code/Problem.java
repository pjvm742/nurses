public class Problem {

	class Contract { // super-evaluator for the constraints of one nurse
		ConstraintEvaluator[] constraints;
		double[] ConstraintWeights;

		public int[] enforce(int[] roster, int constraint) {

		}
		
		public double Contribution(int[] roster, int position) {

		}

		public double Evaluate(int[] roster) {

		}

	}

	class ProblemInstance {
		int dayoffset; // 0: Monday, ..., 6: Sunday; weekday of day i is: (i + offset) % 7
		int N; // number of nurses
		int D; // number of days
		int S; // number of shift types
		int nskills;
		int[] skillNeeded = new int[S];
		boolean[] nightShift = new boolean[S];

		Contract[] nurseTypes;

		public int nConstraints;

		double Evaluate(int[] roster, int nurse) {

		}

		public int[] enforce(int constraint, int nurse) {

		}
		
		public double Contribution(int[] roster, int position, int nurse) {

		}

		int[][] demands = new int[D][S];
	}

	class WeekendDef {
		int start;
		int end;
		
		public WeekendDef(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean isWeekend(int day) {
			
			
		}
	}

}
