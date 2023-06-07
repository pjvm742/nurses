package nurses;

public class Constraints {
	
	public abstract class ConstraintEvaluator {
		public abstract int Evaluate(int[] roster); // determines the number of violations of this constraint in the roster
		public abstract int Contribution(int[] roster, int position); // determines the number of violations of this constraint that one day is involved in
		public abstract int[] Enforce(int[] roster); // removes all violations of this constraint
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
		int D = origRoster.length;
		int[] roster = new int[D];
		int nseq = 0;
		for (int d = 0; d < D; d++) {
			roster[d] = origRoster[d];
		}
		for (int d = 0; d < D; d++) {
			if (roster[d] > 0) {
				nseq++;
			} else if(nseq < min){
				roster[d] = randomAssign(); // assign random shift method
				nseq++;
			  }
			nseq = 0;
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
			int violationValue = Evaluate(roster);
			if(violationValue > 0 && roster[pos] > 0) {
				return 1;
			}
				return 0;
			
		}

		public int[] Enforce(int[] origRoster) {
			int D = origRoster.length;
			int[] roster = new int[D];
			int violationValue = Evaluate(roster);
			double violationValueDouble = (double) violationValue;
			int totalFreeDays = 0;
			for (int d = 0; d < D; d++) {
				if(origRoster[d] > 0) {
					totalFreeDays++;
				}
			}
			int stepValue = (int) (totalFreeDays/violationValueDouble);
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
			for (int d = 0; d < D; d++) {
				int counter = 0;
				if(roster[d] > 0) {
					counter++;
					if(counter == stepValue) {
						if(violationValue > 0) {
						roster[d] = 0;
						violationValue--;
						counter = 0;
						}
					}
				}
			}
			return roster;
		}
	}
	
	public class minTotalAssign extends ConstraintEvaluator {
		int min;

		public minTotalAssign(int k) {
			this.min = k;
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

		public int[] Enforce(int[] origRoster) {
			int D = origRoster.length;
			int[] roster = new int[D];
			int violationValue = Evaluate(roster);
			double violationValueDouble = (double) violationValue;
			int totalAssignments = 0;
			for (int d = 0; d < D; d++) {
				if(origRoster[d] > 0) {
					totalAssignments++;
				}
			}
			int stepValue = (int) (totalAssignments/violationValueDouble);
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
			for (int d = 0; d < D; d++) {
				int counter = 0;
				if(roster[d] == 0) {
					counter++;
					if(counter == stepValue) {
						if(violationValue > 0) {
						roster[d] = randomAssign();
						counter = 0;
						}
					}
				}
			}
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
			int D = origRoster.length;
			int[] roster = new int[D];
			int nseq = 0;
			for (int d = 0; d < D; d++) {
				roster[d] = origRoster[d];
			}
			for (int d = 0; d < D; d++) {
				if(roster[d] == 0) {
					nseq++;
					if(nseq > max) {
						roster[d] = randomAssign();
						nseq = 0;
					}
				}
				nseq = 0;
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
			if (isWeekend(pos) && roster[pos] > 0) {
				for(int i = 1; i < 4; i++) {                             
					if(isWeekend(pos-i) && roster[pos-i] == 0) {
						return 1;
					}
				}
				for(int i = 1; i < 4; i++) {
					if(isWeekend(pos+i) && roster[pos+i] == 0) {
						return 1;
					}
			    }
			}
			else if(isWeekend(pos) && roster[pos] == 0) {
				for(int i = 1; i < 4; i++) {                             
					if (isWeekend(pos-i) && roster[pos-i] > 0) {
						return 1;
					}
				}
				for(int i = 1; i < 4; i++) {
					if(isWeekend(pos+i) && roster[pos+i] > 0) {
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
			for (int d = 0; d < D; d++) {
				if(isWeekend(d) && roster[d] > 0) {
					for(int i = 1; i < 4; i++) {
						if(isWeekend(d+i) && roster[d+i] == 0) {
							roster[d+i] = randomAssign();
						}
						if(isWeekend(d-i) && roster[d-i] == 0) {
							roster[d-i] = randomAssign();
						}
					}
				}
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
			int consecWorkingWeekends = 0;
			if(isWeekend(pos)){
				for(int j = pos; j > 0; j -= 7) {
					boolean isConsecutiveWeekendsWorking = false;
					for(int i = 0; i < 4; i++) {
						if(isWeekend(j+i) && roster[j+i] > 0) {
							consecWorkingWeekends++;
							isConsecutiveWeekendsWorking = true;
							break;
						}
						if(isWeekend(j-i) && roster[j-i] > 0) {
							consecWorkingWeekends++;
							isConsecutiveWeekendsWorking = true;
							break;
						}
					}
					if(!isConsecutiveWeekendsWorking) {
						if(consecWorkingWeekends > max) {
							return 1;
						}
					}
				}
				consecWorkingWeekends = 0;
					for(int j = pos; j < D-1; j += 7) {
						boolean isConsecutiveWeekendsWorking = false;
						for(int i = 0; i < 4; i++) {
							if(isWeekend(j+i) && roster[j+i] > 0) {
								consecWorkingWeekends++;
								isConsecutiveWeekendsWorking = true;
								break;
							}
							if(isWeekend(j-i) && roster[j-i] > 0) {
								consecWorkingWeekends++;
								isConsecutiveWeekendsWorking = true;
								break;
							}
						}
						if(!isConsecutiveWeekendsWorking) {
							if(consecWorkingWeekends > max) {
								return 1;
							}
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
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
					boolean workingWeekend = false;
					for(int i = 0; i < 4; i++) {
						if(isWeekend(d+i) && roster[d+i] > 0) {
							  workingWeekend = true;
							  break;
						}
					}
					if(workingWeekend) {
						  nseq++;
						  if(nseq > max) {
							  for(int i = 0; i < 4; i++) {
									if(isWeekend(d+i) && roster[d+i] > 0) {
										roster[d+i] = 0;
									}
							  }
						  }
					}
					nseq = 0;
				}
			}
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
			int consecWorkingWeekends = 0;
			if(isWeekend(pos)){
				for(int j = pos; j > 0; j -= 7) {
					boolean isConsecutiveWeekendsWorking = false;
					for(int i = 0; i < 4; i++) {
						if(isWeekend(j+i) && roster[j+i] > 0) {
							consecWorkingWeekends++;
							isConsecutiveWeekendsWorking = true;
							break;
						}
						if(isWeekend(j-i) && roster[j-i] > 0) {
							consecWorkingWeekends++;
							isConsecutiveWeekendsWorking = true;
							break;
						}
					}
					if(!isConsecutiveWeekendsWorking) {
						if(consecWorkingWeekends < min) {
							return 1;
						}
					}
				}
				consecWorkingWeekends = 0;
					for(int j = pos; j < D-1; j += 7) {
						boolean isConsecutiveWeekendsWorking = false;
						for(int i = 0; i < 4; i++) {
							if(isWeekend(j+i) && roster[j+i] > 0) {
								consecWorkingWeekends++;
								isConsecutiveWeekendsWorking = true;
								break;
							}
							if(isWeekend(j-i) && roster[j-i] > 0) {
								consecWorkingWeekends++;
								isConsecutiveWeekendsWorking = true;
								break;
							}
						}
						if(!isConsecutiveWeekendsWorking) {
							if(consecWorkingWeekends < min) {
								return 1;
							}
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
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
					boolean workingWeekend = false;
					for(int i = 0; i < 4; i++) {
						if(isWeekend(d+i) && roster[d+i] > 0) {
							  workingWeekend = true;
							  break;
						}
					}
					if(workingWeekend) {
						  nseq++;
					}
					if(!workingWeekend) {
						if(nseq > 0 && nseq < min) {
							for(int i = 0; i < 4; i++) {
								if(isWeekend(d+i) && roster[d+i] == 0) {
									roster[d+i] = randomAssign();
									nseq++;
									break;
								}
							}
						}
					}
					
				}
			}
			return roster;
		}
	}
	
	public class MaxWeekends4weeks extends ConstraintEvaluator {
		int max;
		WeekendDef wknd;

		public MaxWeekends4weeks(int k, WeekendDef w) {
			this.max = k;
			this.wknd = w;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int workWeekends = 0;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
				  boolean workWeekend = false;
				  for(int j = 0; j < 28; j += 7) {
					  for(int i = 0; i < 4; i++) {
						  if(isWeekend(d+j+i) && roster[d+j+i] > 0) {
						  workWeekends++;
						  workWeekend = true;
						  break;
						  }
					  }
				  workWeekend = false;
				  }
				  if(workWeekends > max) {
					  violation++;
				  }
				}
			}	
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int D = roster.length;
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			
			return roster;
		}
	}
	
	public class identicalShiftTypesDuringWeekend extends ConstraintEvaluator {
		WeekendDef wknd;

		public identicalShiftTypesDuringWeekend(int k, WeekendDef w) {
			this.wknd = w;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
					  for(int i = 0; i < 4; i++) {
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
			int D = roster.length;
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			
			return roster;
		}
	}
	
	public class noNightShiftBeforeWeekend extends ConstraintEvaluator {
		WeekendDef wknd;

		public noNightShiftBeforeWeekend(int k, WeekendDef w) {
			this.wknd = w;
		}
		
		public int Evaluate(int[] roster) {
			int D = roster.length;
			int violation = 0;
			for (int d = 0; d < D; d++) {
				int weekday = ( d + dayoffset ) %7;
				if(weekday = w.start) {
					  for(int i = 0; i < 4; i++) {
						  boolean workingWeekend = false;
						  if(isWeekend(d+i) && roster[d+i] != 0) {
						  workingWeekend true;
						  }
						  if(!workingWeekend) {
							  if(roster[d-1] == NIGHTSHIFTINDEX) {
								  violation++;
							  }
						  }
					  }
				}
			}
				
			return violation;
		}

		public int Contribution(int[] roster, int pos) {
			int D = roster.length;
			return 0;
		}

		public int[] Enforce(int[] origRoster) {
			
			return roster;
		}
	}
	
}
