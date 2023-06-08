package nurses;

	public class MaxTotalAssign extends ConstraintEvaluator {
		int max;

		public MaxTotalAssign(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int total = 0;
			int violation = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (roster[d] > 0) {
					total++;
				}
			}
			if (total > max) {
				violation = total - max;
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int violationValue = Evaluate(roster);
			if(violationValue > 0 && roster[pos] > 0) {
				return 1;
			}
			return 0;
			
		}

		public void Enforce(int[] roster) {
			int violationValue = Evaluate(roster);
			int totalWorkingDays = 0;
			for (int d = 0; d < Dim.D; d++) {
				if(roster[d] > 0) {
					totalWorkingDays++;
				}
			}
			int stepValue = (int) ((double) totalWorkingDays / (double) violationValue);
			int counter = 0;
			for (int d = 0; d < Dim.D && violationValue > 0; d++) {
				if(roster[d] > 0) {
					counter++;
					if(counter == stepValue) {
						roster[d] = 0;
						violationValue--;
						counter = 0;
					}
				}
			}
		}
	}
