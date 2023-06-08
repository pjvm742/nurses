package nurses;


	public class MinTotalAssign extends ConstraintEvaluator {
		int min;

		public MinTotalAssign(int k) {
			this.min = k;
		}
		
		public int Evaluate(int[] roster) {
			int total = 0;
			int violation = 0;
			for (int d = 0; d < Dim.D; d++) {
				if (roster[d] > 0) {
					total++;
				}
			}
			if (total < min) {
				violation = min - total;
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int violationValue = Evaluate(roster);
			if(violationValue > 0 && roster[pos] == 0) {
				return 1;
			}
			return 0;
			
		}

		public void Enforce(int[] roster) {
			int violationValue = Evaluate(roster);
			int totalFreeDays = 0;
			for (int d = 0; d < Dim.D; d++) {
				if(roster[d] == 0) {
					totalFreeDays++;
				}
			}
			int stepValue = (int) ((double) totalFreeDays / (double) violationValue);
			for (int d = 0; d < Dim.D && violationValue > 0; d++) {
				int counter = 0;
				if(roster[d] == 0) {
					counter++;
					if(counter == stepValue) {
						roster[d] = ChooseShiftType();
						counter = 0;
					}
				}
			}
		}
	}
