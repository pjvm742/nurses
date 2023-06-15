package constraintevals;
import nurses.*;


	public class MinConsecutiveWorkingWeekends extends ConstraintEvaluator {
		int min;
		WeekendDef w;

		public MinConsecutiveWorkingWeekends(int k, WeekendDef w) {
			this.min = k;
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
						if (nseq < min) {
							violation += min - nseq;
						}
						nseq = 0;
					}
				}
			}
			if (nseq < min) {
				violation += min - nseq;
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			if (!w.isWeekend(pos)) {
				return 0;
			}
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
				if (w.isWeekendStart(d)) {
					if (w.isStartOfWorkingWeekend(roster, d)) {
						nseq++;
					} else {
						if (nseq < min) {
							roster[d] = ChooseShiftType();
							nseq++;
						} else {
							nseq = 0;
						}
					}
				}
			}
		}
	}
