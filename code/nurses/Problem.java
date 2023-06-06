package nurses;

public class Problem {

	class NurseEvaluator { // super-evaluator for the constraints of one nurse
		ConstraintEvaluator[] constraints;
		int[] weights;

		public NurseEvaluator(ConstraintEvaluator[] constraints, int[] weights) {
			this.constraints = constraints;
			this.weights = weights;
		}

		public int[] Enforce(int[] roster, int constraint) {

		}
		
		public int Contribution(int[] roster, int position) {

		}

		public int Evaluate(int[] roster) {

		}

	}

	class ProblemInstance {
		public int N; // number of nurses
		public int D; // number of days
		public int S; // number of shift types

		public int[][] demands;
		
		public NurseEvaluator[] nurses;
		public int nUsedConstraints;
		public int[] constraintIDs;

		public ProblemInstance(int[][] demands, NurseEvaluator[] nurses, int nused, int[] usedConstraints, int N, int D, int S) {
			this.N = N;
			this.D = D;
			this.S = S;

			this.demands = demands;

			this.nurses = nurses;
			this.nUsedConstraints = nused;
			this.constraintIDs = usedConstraints;
		}

		public int Evaluate(int[] roster, int nurse) {

		}

		public int[] Enforce(int constraint, int nurse) {

		}
		
		public int Contribution(int[] roster, int position, int nurse) {

		}
	}

	class WeekendDef {
		int start;
		int end;
		
		public WeekendDef(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean isWeekend(int day) {
			int weekday = ( day + dayoffset ) %7;
			
			if(weekday >= start) {
				return true;
			}else if(weekday == end) {
				return true;
			}
			return false;
			
		}
	}

}
