package nurses;

public class Problem {

	class NurseEvaluator { // super-evaluator for the constraints of one nurse
		ConstraintEvaluator[] constraints;
		int[] weights;

		public NurseEvaluator(ConstraintEvaluator[] constraints, int[] weights) {
			this.constraints = constraints;
			this.weights = weights;
		}

			
		public int Evaluate(int[] roster) {
			int sum = 0;
			for (int c = 0; c < constraints.length; c++) {
				sum += constraints[c].Evaluate(roster) * weights[c];
			}
			return sum;
		}

		public void Enforce(int[] roster, int c) {
			constraints[c].Enforce(roster);
		}
	
		public int ImpactOfChange(int[] roster, int position, int shift) {
			int sum = 0;
			for (int c = 0; c < constraints.length; c++) {
				sum += constraints[c].ImpactOfChange(roster, position, shift) * weights[c];
			}
			return sum;
		
		}
	}

	class ProblemInstance {
		public int N;
		public int D;
		public int S;

		public int[][] demands;
		
		public NurseEvaluator[] nurses;
		public int nUsedConstraints;
		public int[] constraintIDs;

		public ProblemInstance(int[][] demands, NurseEvaluator[] nurses, int nused, int[] usedConstraints) {
			this.demands = demands;

			this.nurses = nurses;
			this.nUsedConstraints = nused;
			this.constraintIDs = usedConstraints;

			this.N = Constraints.N;
			this.D = Constraints.D;
			this.S = Constraints.S;
		}

		public int EvaluateAll(int[][] solution) {
			int sum = 0;
			for (int i = 0; i < N; i++) {
				sum += nurses[i].Evaluate(solution[i]);
			}
			return sum;
		}

		public int Evaluate(int[][] solution, int nurse) {
			return nurses[nurse].Evaluate(solution[nurse]);
		}

		public void Enforce(int[][] solution, int nurse, int constraint) {
			c = constraintIDs[constraint];
			return nurses[nurse].Enforce(solution[nurse], c);
		}
			
		public int ImpactOfChange(int[][] solution, int nurse, int position, int shift) {
			return nurses[nurse].ImpactOfChange(solution[nurse], position, shift);
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

		public int findStart(int day) {
			for (int d = day; ; d--) {
				if (d == 0) {
					if (isWeekend(0)) {
						return 0;
					} else {
						return -1;
					}
				}
				if (isWeekendStart(d)) {
					return d;
				}
			}
		}
	}

}
