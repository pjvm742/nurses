package constraintevals;
import nurses.*;


	public class MaxConsecutiveWorking extends ConstraintEvaluator {
		int max;

		public MaxConsecutiveWorking(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (roster[d] > 0) {
					nseq++;
				} else {
					if (nseq > max) {
						violation += nseq - max;
					}
					nseq = 0;
				}
			}
			if (nseq > max) {
				violation += nseq - max;
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int cur = roster[pos];
			int curval = Evaluate(roster);
			if (cur == 0) {
				roster[pos] = 1;
			} else {
				roster[pos] = 0;
			}
			int compval = Evaluate(roster);
			roster[pos] = cur;

			return curval - compval;
		}

		public void Enforce(int[] roster) {
			int nseq = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (roster[d] > 0) {
					nseq++;
					if (nseq > max) {
						roster[d] = 0;
						nseq = 0;
					}
				} else {
					nseq = 0;
				}
			}
		}
	}
