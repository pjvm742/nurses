package nurses;

	public class ShiftOffRequests extends ConstraintEvaluator {
		int[][] req;

		public ShiftOffRequests(int[][] shifts_off) {
			this.req = shifts_off;
		}

		public int Evaluate(int[] roster) {
			int violations = 0;
			for (int d = 0; d < Dim.D; d++) {
				violations += Contribution(roster, d);
			}
			return violations;
		}

		public int Contribution(int[] roster, int pos) {
			int s = roster[pos];
			if (s > 0) {
				return req[pos][s];
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < Dim.D; d++) {
				if (Contribution(roster,d) > 0) {
					roster[d] = 0;
				}
			}
		}
	}
