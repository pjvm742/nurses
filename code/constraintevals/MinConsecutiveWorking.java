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
				} else if(nseq < min) {
					violation += min - nseq;
				        nseq = 0;
				}
			}
			if(nseq < min) {
				violation += min - nseq;
			        nseq = 0;
			}

			return violation;
		}
	
		public int Contribution(int[] roster, int pos) {
			if (roster[pos] > 0) {
				int start = pos;
				int end = pos;
				for (; start > 0; start--) {
					if (roster[start-1] == 0) {
						break;
					}
				}
				for (; end < Dim.D-1; end++) {
					if (roster[end+1] == 0) {
						break;
					}
				}
				int length = end - start + 1;
				if (length < min) {
					return 1;
				}
			} else {
				return 0;
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
