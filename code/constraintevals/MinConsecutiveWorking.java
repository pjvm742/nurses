package constraintevals;
import nurses.*;


	public class MinConsecutiveWorking extends ConstraintEvaluator {
		int min;
	
		public MinConsecutiveWorking(int k) {
			this.min = k;
		}

		public int Evaluate(int[] roster) {
			int violation = 0;
			int nseq = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (roster[d] > 0) {
					nseq++;
				} else if(nseq < min && nseq > 0) {
					violation += min - nseq;
				        nseq = 0;
				}
			}
			if(nseq < min) {
				violation += min - nseq;
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

			if (curval > compval) {
				return curval - compval;
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			int nseq = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (roster[d] > 0) {
					nseq++;
				} else if(nseq < min){
					roster[d] = ChooseShiftType();
					nseq++;
				} else {
					nseq = 0;
				}
			}
		}
	}
