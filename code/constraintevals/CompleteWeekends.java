package constraintevals;
import nurses.*;

public class CompleteWeekends extends ConstraintEvaluator {
	WeekendDef w;

	public CompleteWeekends(WeekendDef w) {
		this.w = w;
	}
	
	int evaluateAt(int[] roster, int d) {
		if(w.isStartOfWorkingWeekend(roster, d)) {
			for (int i = 0; d+i < Dim.D && w.isWeekend(d+i); i++) {
				if (roster[d+i] == 0) {
					return 1;
				}
			}
		}
		return 0;
	}

	public int Evaluate(int[] roster) {
		int violation = 0;
		for (int d = 0; d < Dim.D; d++) {
			violation += evaluateAt(roster, d);			
		}
		return violation;
	}

	public int Contribution(int[] roster, int pos) {
		if (w.isWeekend(pos)) {
			int start = w.findStart(pos);
			return evaluateAt(roster, start);
		}
		return 0;
	}
	
	public void Enforce(int[] roster) {
		int nseq = 0;
		for (int d = 0; d < Dim.D; d++) {
			if (w.isStartOfWorkingWeekend(roster, d)) {
				for (int i = 0; d + i < Dim.D && w.isWeekend(d+i); i++) {
					if (roster[d+i] == 0) {
						roster[d+i] = ChooseShiftType();
					}
				}
			}
		}
	}
}

