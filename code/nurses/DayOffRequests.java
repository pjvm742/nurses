package nurses;


	public class DayOffRequests extends ConstraintEvaluator {
		int[] req;

		public DayOffRequests(int[] days_off) {
			this.req = days_off;
		}

		public int Evaluate(int[] roster) {
			int violations;
			for (int d = 0; d < Dim.D; d++) {
				violations += Contribution(roster, d);
			}
			return violations;
		}

		public int Contribution(int[] roster, int pos) {
			if (roster[pos] > 0) {
				return req[pos];
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
