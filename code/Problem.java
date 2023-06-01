public class Problem {
	
	abstract class ConstraintEvaluator {
		public int Evaluate(int[] roster); // determines the number of violations of this constraint in the roster
		public int Contribution(int[] roster, int position); // determines the number of violations of this constraint that one day is involved in
		public int[] enforce(int[] roster); // removes all violations of this constraint
	}

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


}
