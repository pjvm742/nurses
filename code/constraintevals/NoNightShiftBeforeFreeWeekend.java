package constraintevals;
import nurses.*;


	public class NoNightShiftBeforeFreeWeekend extends ConstraintEvaluator {
		WeekendDef w;
		boolean[] night;

		public NoNightShiftBeforeFreeWeekend(WeekendDef w, boolean[] night) {
			this.w = w;
			this.night = night;
		}
		
		public int Evaluate(int[] roster) {
			int violation = 0;
			for (int d = 1; d < Dim.D; d++) {
				if(w.isWeekendStart(d) && !w.isStartOfWorkingWeekend(roster, d)) {
					if(night[ roster[d-1] ]) {
						violation++;
					}
				}

			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int s = roster[pos];
			if (night[s] && pos < Dim.D-1 && !w.isStartOfWorkingWeekend(roster, pos+1)) {
				return 1;
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < Dim.D; d++) {
				if (this.Contribution(roster, d) == 1) {
					roster[d] = 0;
				}
			}
		}
	}
