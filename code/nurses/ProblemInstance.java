package nurses;


	public class ProblemInstance {
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

			this.N = Dim.N;
			this.D = Dim.D;
			this.S = Dim.S;
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
			int c = constraintIDs[constraint];
			nurses[nurse].Enforce(solution[nurse], c);
		}
			
		public int ImpactOfChange(int[][] solution, int nurse, int position, int shift) {
			return nurses[nurse].ImpactOfChange(solution[nurse], position, shift);
		}
	}
