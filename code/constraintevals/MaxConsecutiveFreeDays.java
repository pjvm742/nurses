package constraintevals;
import nurses.*;

public class MaxConsecutiveFreeDays extends ConstraintEvaluator {
	int max;

	public MaxConsecutiveFreeDays(int k) {
		this.max = k;
	}
	
	public int Evaluate(int[] roster) {
		int nseq = 0;
		int violation = 0;
		for (int d = 0; d < Dim.D; d++) {
			if (roster[d] == 0) {
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
		if (roster[pos] == 0) {
			int start = pos;
			int end = pos;
			for (; start > 0; start--) {
				if (roster[start-1] != 0) {
					break;
				}
			}
			for (; end < Dim.D-1; end++) {
				if (roster[end+1] != 0) {
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
		return 0;
	}

	public void Enforce(int[] roster) {
		int nseq = 0;
		for (int d = 0; d < Dim.D; d++) {
			if(roster[d] == 0) {
				nseq++;
				if(nseq > max) {
					roster[d] = ChooseShiftType();
					nseq = 0;
				}
			} else {
				nseq = 0;
			}
		}
	}
}

