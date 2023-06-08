package nurses;


	public class IdenticalShiftsInWeekend extends ConstraintEvaluator {
		WeekendDef w;

		public IdenticalShiftsInWeekend(WeekendDef w) {
			this.w = w;
		}
		
		public int Evaluate(int[] roster) {
			int violation = 0;
			for (int d = 0; d < Dim.D; d++) {
				if(w.isWeekendStart(d)) {
					for(int i = 0; i < 4 && d+i < Dim.D; i++) {
						if(w.isWeekend(d+i) && roster[d+i] != roster[d]) {
							violation++;
							break;
						}
					}
				}
			}
				
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int s = roster[pos];
			for (int d = pos -1; d >= 0 && w.isWeekend(d); d--) {
				if (roster[d] != s) {
					return 1;
				}
			}
			for (int d = pos +1; d < Dim.D && w.isWeekend(d); d++) {
				if (roster[d] != s) {
					return 1;
				}
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < Dim.D; d++) {
				if(w.isWeekendStart(d)) {
					for(int i = 0; i < 4 && d+i < Dim.D; i++) {
						if(w.isWeekend(d+i) ) {
							roster[d+i] = roster[d];
						}
					}
				}
			}
		}
	}
