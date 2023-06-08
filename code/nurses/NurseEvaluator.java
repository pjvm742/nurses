package nurses;


	public class NurseEvaluator { // super-evaluator for the constraints of one nurse
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
