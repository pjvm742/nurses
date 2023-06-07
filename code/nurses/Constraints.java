package nurses;

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

		public static int isStartOfWorkingWeekend(int[] roster, int d, WeekendDef w) {
			if(w.isWeekendStart(d)) {
				for(int i = 0; i < 4 && d+i < D; i++) {
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
		public int[] Enforce(int[] roster) {
			return roster;
		}
	}

	public class MaxConsecutiveWorking extends ConstraintEvaluator {
		int max;

		public MaxConsecutiveWorking(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
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
			int D = roster.length;
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

		public int[] Enforce(int[] origRoster) {
			int D = origRoster.length;
			int[] roster = new int[D];
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
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
			return roster;
		}
	}
	
	public class MinConsecutiveWorking extends ConstraintEvaluator {
	int min;
	
	public MinConsecutiveWorking(int k) {
		this.min = k;
	}

	public int Evaluate(int[] roster) {
		int D = roster.length;
		int violation = 0;
		int nseq = 0;
		for (int d = 0; d < D; d++) {
			if (roster[d] > 0) {
				nseq++;
			} else {
				if(nseq < min) {
				violation += min - nseq;
			            nseq = 0;
				}	
			}
		}
		if(nseq < min) {
			violation += min - nseq;
		            nseq = 0;
		}
		
		return violation;
	}
	
	public int Contribution(int[] roster, int pos) {
		int D = roster.length;
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
		return 0;
	}
	
	public int[] Enforce(int[] origRoster) {
		return roster;
	}
	}
	
	public class maxTotalAssign extends ConstraintEvaluator {
		int max;

		public void maxTotal(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
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
			
				return 0;
			
		}

		public int[] Enforce(int[] origRoster) {
			
			return roster;
		}
	}
	
	public class MaxConsecutiveFreeDays extends ConstraintEvaluator {
		int max;

		public MaxConsecutiveFreeDays(int k) {
			this.max = k;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
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
			int D = roster.length;
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

		public int[] Enforce(int[] origRoster) {
			
			return roster;
		}
	}
		
	public class MinConsecutiveFreeDays extends ConstraintEvaluator {
		int min;
		
		public MinConsecutiveFreeDays(int k) {
			this.min = k;
		}

		public int Evaluate(int[] roster) {
			int D = roster.length;
			int violation = 0;
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				if (roster[d] == 0) {
					nseq++;
				}else if(nseq < min) {
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
			int D = roster.length;
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
		
		public int[] Enforce(int[] origRoster) {
			int D = origRoster.length;
			int[] roster = new int[D];
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
			for (int d = 0; d < D; d++) {
				if(roster[d] == 0) {
					nseq++;
				}
				else if (nseq < min) {
						roster[d] = 0;
						nseq++;
					}
				nseq = 0;
				} 
			
			return roster;
		}

		}
	
	public class CompleteWeekends extends ConstraintEvaluator {
		WeekendDef wknd;

		public CompleteWeekends(WeekendDef w) {
			this.wknd = w;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
					if(roster[d] > 0) {
						for(int i = 1; i < 4; i++) {                //max weekend length is equal to 4 days
							if(isWeekend(d+i) && roster[d+i] == 0) {
									violation++;
									break;
							}
						}
					}
					else {
						for(int i = 1; i < 4; i++) { 
							if(isWeekend(d+i) && roster[d+i] > 0) {
									violation++;
									break;
							}
						}
					}
				}
					
			}
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int D = roster.length;
			if (isWeekend(roster[pos]) && roster[pos] > 0) {
				for(int i = 1; i < 4; i++) {                             
					if(isWeekend(roster[pos-i]) && roster[pos-i] == 0) {
						return 1;
					}
				}
				for(int i = 1; i < 4; i++) {
					if(isWeekend(roster[pos+i]) && roster[pos+i] == 0) {
						return 1;
					}
			    }
			}
			else if(isWeekend(roster[pos]) && roster[pos] == 0) {
				for(int i = 1; i < 4; i++) {                             
					if (isWeekend(roster[pos-i]) && roster[pos-i] > 0) {
						return 1;
					}
				}
				for(int i = 1; i < 4; i++) {
					if(isWeekend(roster[pos+i]) && roster[pos+i] > 0) {
						return 1;
					}
			    }
			}
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			int D = origRoster.length;
			int[] roster = new int[D];
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
			
			return roster;
		}
	}
	
	public class MaxConsecutiveWorkingWeekends extends ConstraintEvaluator {
		int max;
		WeekendDef wknd;

		public MaxConsecutiveWorkingWeekends(int k, WeekendDef w) {
			this.max = k;
			this.wknd = w;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
				  boolean workWeekend = false;
				  for(int i = 0; i < 4; i++) {
					  if(isWeekend(d+i) && roster[d+i] > 0) {
						  nseq++;
						  workWeekend = true;
						  break;
					  }
					  else if(!workWeekend) {
						  if(nseq > max) {
							  violation++;
						  }
						  nseq = 0;
					  }
				  }
				  workWeekend = false;
				}
			}
				
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int D = roster.length;
			if (isWeekend(roster[pos]) && roster[pos] > 0) {

			}
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			
			return roster;
		}
	}
	
	public class MinConsecutiveWorkingWeekends extends ConstraintEvaluator {
		int min;
		WeekendDef wknd;

		public MinConsecutiveWorkingWeekends(int k, WeekendDef w) {
			this.min = k;
			this.wknd = w;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int nseq = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
					boolean workWeekend = false;
				  for(int i = 0; i < 4; i++) {
					  if(isWeekend(d+i) && roster[d+i] > 0) {
						  nseq++;
						  workWeekend = true;
					  }
					  else if(!workWeekend) {
						  if(nseq < min) {
							  violation++;
						  }
						  nseq = 0;
					  }
				  }
				  workWeekend = false;
				}
			}
				
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int D = roster.length;
			if (isWeekend(roster[pos]) && roster[pos] > 0)
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			
			return roster;
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
