import Problem.ConstraintEvaluator;

public class Constraints {
	
	public class MaxConsecutiveWorking extends ConstraintEvaluator {
		int max;

		public MaxConsecutive(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
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
			int D = roster.length;
			if (roster[pos] > 0) {
				int start = pos;
				int end = pos;
				for (; start > 0; start--) {
					if (roster[start-1] == 0) {
						break;
					}
				}
				for (; end < D-1; end++) {
					if (roster[end+1] == 0) {
						break;
					}
				}
				int length = end - start + 1;
				if (length > max) {
					return 1;
				}
			} else {
				return 0;
			}
		}

		public int[] Enforce(int[] origRoster) {
			int D = origRoster.length;
			int[] roster = new int[D];
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
			for (int d = 0; d < D; d++) {
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
			return roster;
		}
	}
}
