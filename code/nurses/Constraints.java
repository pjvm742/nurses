package nurses;

import java.util.Random;

public class Constraints {
	public static int D;
	public static int S;
	public static int N;
	
	public abstract class ConstraintEvaluator {
		public abstract int Evaluate(int[] roster); // determines the number of violations of this constraint in the roster
		public abstract int Contribution(int[] roster, int position); // determines the number of violations of this constraint that one day is involved in
		public abstract void Enforce(int[] roster); // removes all violations of this constraint (works through side effects)

		public int ImpactOfChange(int[] roster, int index, int newshift) {
			curshift = roster[index];
			curval = this.Contribution(roster, index);
			roster[index] = newshift;
			newval = this.Contribution(roster, index);
			roster[index] = curshift;

			return newval - curval;
		}

		public int ChooseShiftType() {
			Random r = new Random();
			return r.nextInt(S);
		}

		public static int isStartOfWorkingWeekend(int[] roster, int d, WeekendDef w) {
			if(w.isWeekendStart(d)) {
				for(int i = 0; i < 4 && d+i < D; i++) { // max weekend length is equal to 4 days
					boolean workingWeekend = false;
					if(isWeekend(d+i) && roster[d+i] != 0) {
						workingWeekend true;
					}
				}
			}

		}
	}
	
	public class EmptyConstraint extends ConstraintEvaluator {
		public int Evaluate(int[] roster) {
			return 0;
		}
		public int Contribution(int[] roster, int positition) {
			return 0;
		}
		public void Enforce(int[] roster) {
		}
	}

	public class MaxConsecutiveWorking extends ConstraintEvaluator {
		int max;

		public MaxConsecutiveWorking(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				if (roster[d] > 0) {
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
			if (roster[pos] > 0) {
				int start = pos;
				int end = pos;
				for (; start > 0; start--) {
					if (roster[start-1] == 0) {
						break;
					}
				}
				for (; end < D-1; end++) {
					if (roster[end+1] == 0) {
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
			for (int d = 0; d < D; d++) {
				if (roster[d] > 0) {
					nseq++;
					if (nseq > max) {
						roster[d] = 0;
						nseq = 0;
					}
				} else {
					nseq = 0;
				}
			}
		}
	}
	
	public class MinConsecutiveWorking extends ConstraintEvaluator {
		int min;
	
		public MinConsecutiveWorking(int k) {
			this.min = k;
		}

		public int Evaluate(int[] roster) {
			int violation = 0;
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				if (roster[d] > 0) {
					nseq++;
				} else if(nseq < min) {
					violation += min - nseq;
				        nseq = 0;
				}
			}
			if(nseq < min) {
				violation += min - nseq;
			        nseq = 0;
			}

			return violation;
		}
	
		public int Contribution(int[] roster, int pos) {
			if (roster[pos] > 0) {
				int start = pos;
				int end = pos;
				for (; start > 0; start--) {
					if (roster[start-1] == 0) {
						break;
					}
				}
				for (; end < D-1; end++) {
					if (roster[end+1] == 0) {
						break;
					}
				}
				int length = end - start + 1;
				if (length < min) {
					return 1;
				}
			} else {
				return 0;
			}
			} else {
				return 0;
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				if (roster[d] > 0) {
					nseq++;
				} else if(nseq < min){
					roster[d] = ChooseShiftType();
					nseq++;
				} else {
					nseq = 0;
				}
			}
			return roster;
		}
	}
	
	public class maxTotalAssign extends ConstraintEvaluator {
		int max;

		public maxTotalAssign(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int total = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
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
			for (int d = 0; d < D; d++) {
				if(roster[d] > 0) {
					totalWorkingDays++;
				}
			}
			int stepValue = (int) ((double) totalWorkingDays / (double) violationValue);
			int counter = 0;
			for (int d = 0; d < D && violationValue > 0; d++) {
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
	
	public class minTotalAssign extends ConstraintEvaluator {
		int min;

		public minTotalAssign(int k) {
			this.min = k;
		}
		
		public int Evaluate(int[] roster) {
			int total = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
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
			for (int d = 0; d < D; d++) {
				if(roster[d] == 0) {
					totalFreeDays++;
				}
			}
			int stepValue = (int) ((double) totalFreeDays / (double) violationValue);
			for (int d = 0; d < D && violationValue > 0; d++) {
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
	
	public class MaxConsecutiveFreeDays extends ConstraintEvaluator {
		int max;

		public MaxConsecutiveFreeDays(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
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
				for (; end < D-1; end++) {
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
			for (int d = 0; d < D; d++) {
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
			return roster;
		}
	}
		
	public class MinConsecutiveFreeDays extends ConstraintEvaluator {
		int min;
		
		public MinConsecutiveFreeDays(int k) {
			this.min = k;
		}

		public int Evaluate(int[] roster) {
			int violation = 0;
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				if (roster[d] == 0) {
					nseq++;
				} else if (nseq < min) {
					violation += min - nseq;
					nseq = 0;
				}
			}
			if(nseq < min) {
				violation += min - nseq;
			        nseq = 0;
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
				for (; end < D-1; end++) {
					if (roster[end+1] != 0) {
						break;
					}
				}
				int length = end - start + 1;
				if (length < min) {
					return 1;
				}
			} else {
				return 0;
			}
			return 0;
		}
		
		public void Enforce(int[] roster) {
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				if(roster[d] == 0) {
					nseq++;
				} else if (nseq < min) {
					roster[d] = 0;
					nseq++;
				} else {
					nseq = 0;
				}
			}
		}

	}
	
	public class CompleteWeekends extends ConstraintEvaluator {
		WeekendDef w;

		public CompleteWeekends(WeekendDef w) {
			this.w = w;
		}
		
		int evaluateAt(int[] roster, int pos) {
			if(isStartOfWorkingWeekend(roster, d, w)) {
				for (int i = 0; d+i < D && w.isWeekend(d+i); i++) {
					if (roster[d+i] == 0) {
						return 1;
					}
				}
			}
			return 0;
		}

		public int Evaluate(int[] roster) {
			int violation = 0;
			for (int d = 0; d < D; d++) {
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
			for (int d = 0; d < D; d++) {
				if (isStartOfWorkingWeekend(roster, d, w)) {
					for (int i = 0; d + i < D && w.isWeekend(d+i) {
						if (roster[d+i] == 0) {
							roster[d+i] = ChooseShiftType();
						}
					}
				}
			}
		}
	}
	
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
			for (int d = 0; d < D; d++) {
				if (w.isWeekendStart(d)) {
					if (isStartOfWorkingWeekend()) {
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
			if (isStartOfWorkingWeekend(roster, start, w)) {
				nConseq = 1;
			} else {
				return 0;
			}
			for (int j = start - 7; ; j -= 7) {
				if (j > 0) {
					if (isStartOfWorkingWeekend(roster, j, w)) {
						nConseq++;
					} else {
						break;
					}
				} else {
					j = 0;
					if (isStartOfWorkingWeekend(roster, j, w)) {
						nConseq++;
					}
					break;
				}
			}
			for (int j = start + 7; j < D && isStartOfWorkingWeekend(roster, j, w); j += 7) {
				nConseq++;
			}
			if (nConseq > max) {
				return nConseq - max;
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			int nseq
			for (int d = 0; d < D; d++) {
				if (w.isWeekendStart(d)) {
					if (isStartOfWorkingWeekend()) {
						nseq++;
						if (nseq > max) {
							for (int i = 0; d+i < D && w.isWeekend(d+i)) {
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
			for (int d = 0; d < D; d++) {
				if (w.isWeekendStart(d)) {
					if (isStartOfWorkingWeekend()) {
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
				violation += min - nConseq;
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			if (!w.isWeekend(pos)) {
				return 0;
			}
			int nConseq = 0;
			int start = w.findStart(pos);
			if (isStartOfWorkingWeekend(roster, start, w)) {
				nConseq = 1;
			} else {
				return 0;
			}
			for (int j = start - 7; ; j -= 7) {
				if (j > 0) {
					if (isStartOfWorkingWeekend(roster, j, w)) {
						nConseq++;
					} else {
						break;
					}
				} else {
					j = 0;
					if (isStartOfWorkingWeekend(roster, j, w)) {
						nConseq++;
					}
					break;
				}
			}
			for (int j = start + 7; j < D && isStartOfWorkingWeekend(roster, j, w); j += 7) {
				nConseq++;
			}
			if (nConseq < min) {
				return min - nConseq;
			}
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			int nseq
			for (int d = 0; d < D; d++) {
				if (w.isWeekendStart(d)) {
					if (isStartOfWorkingWeekend()) {
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
	
	// MaxWeekends4weeks: not implemented	

	public class IdenticalShiftsInWeekend extends ConstraintEvaluator {
		WeekendDef w;

		public IdenticalShiftsInWeekend(int k, WeekendDef w) {
			this.w = w;
		}
		
		public int Evaluate(int[] roster) {
			int violation = 0;
			for (int d = 0; d < D; d++) {
				if(w.isWeekendStart(d)) {
					for(int i = 0; i < 4 && d+i < D; i++) {
						if(isWeekend(d+i) && roster[d+i] != roster[d]) {
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
			for (int d = pos +1; d < D && w.isWeekend(d); d++) {
				if (roster[d] != s) {
					return 1;
				}
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < D; d++) {
				if(w.isWeekendStart(d)) {
					for(int i = 0; i < 4 && d+i < D; i++) {
						if(isWeekend(d+i) ) {
							roster[d+i] = roster[d];
			}
		}
	}
	
	public class NoNightShiftBeforeWeekend extends ConstraintEvaluator {
		WeekendDef w;
		boolean[] night;

		public NoNightShiftBeforeWeekend(WeekendDef w, boolean[] night) {
			this.w = w;
			this.night = night;
		}
		
		public int Evaluate(int[] roster) {
			int violation = 0;
			for (int d = 1; d < D; d++) {
				if(w.isWeekendStart(d) && !isStartOfWorkingWeekend(roster, d, w)) {
					if(night[ roster[d-1] ]) {
						violation++;
					}
				}

			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int s = roster[pos];
			if (night[s] && pos < D-1 && !isStartOfWorkingWeekend(roster, pos+1, this.w)) {
				return 1;
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < D; d++) {
				if (this.Contribution(roster, d) == 1) {
					roster[d] = 0;
				}
			}
		}
	}
	
	public class UnwantedPattern extends ConstraintEvaluator {
		int[] shifts;
		int[] days;
		int size;
		int offset;

		public UnwantedPattern(int[] shiftTypes, int[] weekdays, dayoffset) {
			this.shifts = shiftTypes;
			this.days = weekdays;
			this.size = weekdays.length;
			this.offset = dayoffset;
		}

		int evaluateAt(int[] roster, int pos) {
			if (pos > D - size) {
				return 0;
			}
			for (int i = 0; i < size; i++) {
				j = pos+i;
				if (days[i] != 7 && days[i] != (j + offset) % 7) {
					return 0;
				}
				if (shifts[i] < S && roster[j] != shifts[i]) {
					return 0;
				}
				if (shifts[i] == S && roster[j] == 0) {
					return 0;
				}
			}
			return 1;
		}

		public int Evaluate(int[] roster) {
			int violations = 0;
			for (int d = 0; d < D-size+1; d++) {
				violations += evaluateAt(d);
			}
			return violations;
		}

		public int Contribution(int[] roster, int pos) {
			int violations = 0;
			for (int d = pos; d >= 0 && d > pos - size; d--) {
				violations += evaluateAt(d);
			}
			return violations;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < D-size+1; d++) {
				if (evaluateAt(d) > 0) {
					if (roster[d] == 0) {
						roster[d] = ChooseShiftType();
					} else {
						roster[d] = 0;
					}
				}
			}
		}
	}

	public class DayOffRequests extends ConstraintEvaluator {
		int[] req;

		public DayOffRequests(int[] days_off) {
			this.req = days_off;
		}

		public int Evaluate(int[] roster) {
			int violations;
			for (int d = 0; d < D; d++) {
				violations += Contribution(roster, d);
			}
			return violations;
		}

		public int Contribution(int[] roster, int pos) {
			if (roster[pos] > 0) {
				return req[pos];
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < D; d++) {
				if (Contribution(roster,d) > 0) {
					roster[d] = 0;
				}
			}
		}
	}

	public class ShiftOffRequests extends ConstraintEvaluator {
		int[][] req;

		public ShiftOffRequests(int[][] shifts_off) {
			this.req = shifts_off;
		}

		public int Evaluate(int[] roster) {
			int violations;
			for (int d = 0; d < D; d++) {
				violations += Contribution(roster, d);
			}
			return violations;
		}

		public int Contribution(int[] roster, int pos) {
			int s = roster[pos];
			if (s > 0) {
				return req[pos][s];
			}
			return 0;
		}

		public void Enforce(int[] roster) {
			for (int d = 0; d < D; d++) {
				if (Contribution(roster,d) > 0) {
					roster[d] = 0;
				}
			}
		}
	}

}
