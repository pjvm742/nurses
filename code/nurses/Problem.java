package nurses;

public class Problem {

	class NurseEvaluator { // super-evaluator for the constraints of one nurse
		ConstraintEvaluator[] constraints;
		int[] weights;

		public NurseEvaluator(ConstraintEvaluator[] constraints, int[] weights) {
			this.constraints = constraints;
			this.weights = weights;
		}

		public void Enforce(int[] roster, int constraint) {

		}
		
		public int Contribution(int[] roster, int position) {

		}

		public int Evaluate(int[] roster) {

		}

	}

	class ProblemInstance {
		public int[][] demands;
		
		public NurseEvaluator[] nurses;
		public int nUsedConstraints;
		public int[] constraintIDs;

		public ProblemInstance(int[][] demands, NurseEvaluator[] nurses, int nused, int[] usedConstraints) {
			this.demands = demands;

			this.nurses = nurses;
			this.nUsedConstraints = nused;
			this.constraintIDs = usedConstraints;
		}

		public int EvaluateAll(int[][] solution) {
			
		}

		public int Evaluate(int[][] solution, int nurse) {

		}

		public void Enforce(int[][] solution, int constraint, int nurse) {

		}
		
		public int Contribution(int[][] solution, int position, int nurse) {

		}
	}

	class WeekendDef {
		int start;
		int end;
		int dayoffset;
		
		public WeekendDef(int start, int end, int dayoffset) {
			this.start = start;
			this.end = end;
			this.dayoffset = dayoffset;
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

		public boolean isWeekendStart(int day) {
			int weekday = (day + dayoffset ) % 7;
			return weekday == start || ( day == 0 && this.isWeekend(day) );
		}
	}

}
