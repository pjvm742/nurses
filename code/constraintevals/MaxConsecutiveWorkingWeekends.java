package constraintevals;
import nurses.*;


	public class MaxConsecutiveWorkingWeekends extends ConstraintEvaluator {
		int max;
		WeekendDef w;

		public MaxConsecutiveWorkingWeekends(int k, WeekendDef w) {
			this.max = k;
			this.w = w;
		}
		
		public int Evaluate(int[] roster) {
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (w.isWeekendStart(d)) {
					if (w.isStartOfWorkingWeekend(roster, d)) {
						nseq++;
					} else {
						if (nseq > max) {
							violation += nseq - max;
						}
						nseq = 0;
					}
				}
			}
			if (nseq > max) {
				violation += nseq - max;
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			if (!w.isWeekend(pos)) {
				return 0;
			}
			int nConseq = 0;
			int start = w.findStart(pos);
			if (roster[pos] > 0) {
				nConseq = 1;
			} else {
				return 0;
			}
			for (int j = start - 7; ; j -= 7) {
				if (j > 0) {
					if (w.isStartOfWorkingWeekend(roster, j)) {
						nConseq++;
					} else {
						break;
					}
				} else {
					j = 0;
					if (w.isStartOfWorkingWeekend(roster, j)) {
						nConseq++;
					}
					break;
				}
			}
			for (int j = start + 7; j < Dim.D && w.isStartOfWorkingWeekend(roster, j); j += 7) {
				nConseq++;
			}
			if (nConseq > max) {
				return 1;
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			int nseq = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (w.isWeekendStart(d)) {
					if (w.isStartOfWorkingWeekend(roster, d)) {
						nseq++;
						if (nseq > max) {
							for (int i = 0; d+i < Dim.D && w.isWeekend(d+i); i++) {
								roster[d+i] = 0;
							}
							nseq = 0;
						}
					} else {
						nseq = 0;
					}
				}
			}
		}
	}
