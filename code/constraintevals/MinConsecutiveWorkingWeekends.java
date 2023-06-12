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
			int nConseq = 0;
			int start = w.findStart(pos);
			if (w.isStartOfWorkingWeekend(roster, start)) {
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
			if (nConseq < min) {
				return min - nConseq;
			}
			return 0;
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
