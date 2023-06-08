package nurses;


	public class UnwantedPattern extends ConstraintEvaluator {
		int[] shifts;
		int[] days;
		int size;
		int offset;

		public UnwantedPattern(int[] shiftTypes, int[] weekdays, int dayoffset) {
			this.shifts = shiftTypes;
			this.days = weekdays;
			this.size = weekdays.length;
			this.offset = dayoffset;
		}

		int evaluateAt(int[] roster, int pos) {
			if (pos > Dim.D - size) {
				return 0;
			}
			for (int i = 0; i < size; i++) {
				int j = pos+i;
				if (days[i] != 7 && days[i] != (j + offset) % 7) {
					return 0;
				}
				if (shifts[i] < Dim.S && roster[j] != shifts[i]) {
					return 0;
				}
				if (shifts[i] == Dim.S && roster[j] == 0) {
					return 0;
				}
			}
			return 1;
		}

		public int Evaluate(int[] roster) {
			int violations = 0;
			for (int d = 0; d < Dim.D-size+1; d++) {
				violations += evaluateAt(roster, d);
			}
			return violations;
		}

		public int Contribution(int[] roster, int pos) {
			int violations = 0;
			for (int d = pos; d >= 0 && d > pos - size; d--) {
				violations += evaluateAt(roster, d);
			}
			return violations;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < Dim.D-size+1; d++) {
				if (evaluateAt(roster, d) > 0) {
					if (roster[d] == 0) {
						roster[d] = ChooseShiftType();
					} else {
						roster[d] = 0;
					}
				}
			}
		}
	}
