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
	
}
