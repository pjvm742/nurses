package nurses;
import nurses.*;

	public class WeekendDef {
		int start;
		int end;
		int dayoffset;
		
		public WeekendDef(int start, int end, int dayoffset) {
			this.start = start;
			this.end = end;
			this.dayoffset = dayoffset;
		}
		
		public boolean isWeekend(int day) {
			int weekday = ( day + dayoffset ) %7;
			
			if(weekday >= start) {
				return true;
			}else if(weekday == end) {
				return true;
			}
			return false;
			
		}

		public boolean isWeekendStart(int day) {
			int weekday = (day + dayoffset ) % 7;
			return weekday == start || ( day == 0 && this.isWeekend(day) );
		}

		public int findStart(int day) {
			for (int d = day; ; d--) {
				if (d == 0) {
					if (isWeekend(0)) {
						return 0;
					} else {
						return -1;
					}
				}
				if (isWeekendStart(d)) {
					return d;
				}
			}
		}

		public boolean isStartOfWorkingWeekend(int[] roster, int d) {
			if(isWeekendStart(d)) {
				for(int i = 0; i < 4 && d+i < Dim.D; i++) { // max weekend length is equal to 4 days
					if(isWeekend(d+i) && roster[d+i] != 0) {
						return true;
					}
				}
			}
			return false;
		}
	}
