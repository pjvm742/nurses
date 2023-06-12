package nurses;

import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.Date;
import java.util.Calendar;
import Attributes.*;
import Helper.Helper;
import constraintevals.*;

public class Convert {

	public static int[][] convertSolution(List<int[][]> orig) {
		int D = orig.size();
		int[][] day = orig.get(0);
		int S = day.length + 1;
		int N = day[0].length;

		int[][] sol = new int[N][D];

		for (int d = 0; d < D; d++) {
			day = orig.get(d);
			for (int i = 0; i < N; i++) {
				for (int s = 0; s < S - 1; s++) {
					if (day[s][i] == 1) {
						sol[i][d] = s + 1;
					}
					// if not working any shift, sol[i][d] stays at 0
				}
			}
		}
		
		return sol;
	}

	public static List<int[][]> convertSolutionBack(int [][] orig) {
		int N = orig.length;
		int D = orig[0].length;
		int S = 0;
		for(int i = 0; i < orig.length; i++) {
			int current = orig[i][0];
			if(current > S) {
				S = current;
			}
		}

		List<int[][]> sol = new ArrayList<int[][]>();
		for(int d = 0; d < D; d++) {
			int[][] day = new int[S][N];
			for(int j = 0; j < orig.length; j++) {
				if(orig[j][d] != 0) {
					day[orig[j][d] - 1][j] = 1;
					}
				}
			sol.add(day);
		}
		return sol;
	}


	public static ProblemInstance convertProblem(SchedulingPeriod s) {
		List<int[][]> dummy = new ArrayList<int[][]>();
		Helper h = new Helper(s, dummy);

		List<Skill> skills = s.getSkills();
		int nskills = skills.size();
		Skill[] skillIDs = new Skill[nskills];
		for (int i = 0; i < nskills; i++) {
			skillIDs[i] = skills.get(i);
		}

		List<Shift> shiftTypes = h.getShiftList();
		int S = shiftTypes.size() + 1;
		String[] shiftIDs = new String[S];
		shiftIDs[0] = "None";
		int[] skillForShift = new int[S];
		boolean[] nightShift = new boolean[S];
		for (int i = 0; i < S-1; i++) {
			Shift st = shiftTypes.get(i);
			shiftIDs[i+1] = st.getId();
			skillForShift[i+1] = findSkill(skillIDs, st.getSkills().get(0));
			nightShift[i+1] = isNightShift(st.getStartTime(), st.getEndTime());
		}

		int D = h.getDaysInPeriod();
		int dayoffset = convertWeekday(h.getWeekDayOfPeriode(0)); // 0: Monday, ..., 6: Sunday; weekday of day i is: (i + offset) % 7

		int[][] requirements = new int[D][S];
		for (int d = 0; d < D; d++) {
			for (int sh = 1; sh < S; sh++) {
				String sID = shiftIDs[sh];
				requirements[d][sh] = h.getRequirement(sID, d);
			}
		}

		List<Employee> nurses = h.getEmployeeList();
		int N = nurses.size();
		List<Contract> contracts = h.getContractList();
		List<Pattern> patterns = h.getPatternList();
		List<DayOff> off_days = h.getDayOffRequestList();
		List<ShiftOff> off_shifts = h.getShiftOffRequestList();

		int npatterns = patterns.size();
		ConvertedPattern[] pats = new ConvertedPattern[npatterns];
		for (int p = 0; p < npatterns; p++) {
			pats[p] = new ConvertedPattern(patterns.get(p), shiftIDs);
		}

		int[][] dayoff = new int[N][D];
		int[][][] shiftoff = new int[N][D][S];
		for (int r = 0; r < off_days.size(); r++) {
			DayOff req = off_days.get(r);
			int n = req.getEmployeeId();
			int d = h.getDaysFromStart(req.getDate()) -1;
			int weight = req.getWeight();
			dayoff[n][d] = weight;
		}
		for (int r = 0; r < off_shifts.size(); r++) {
			ShiftOff req = off_shifts.get(r);
			int n = req.getEmployeeId();
			int d = h.getDaysFromStart(req.getDate()) -1;
			int sh = findShiftType(shiftIDs, req.getShiftTypeId());
			int weight = req.getWeight();
			shiftoff[n][d][sh] = weight;
		}

		NurseEvaluator[] evals = new NurseEvaluator[N];
		for (int i = 0; i < N; i++) {
			Employee nurse = nurses.get(i);
			boolean[] skillset = convertSkillset(skillIDs, nurse.getSkills());
			Contract contract = contracts.get(nurse.getContractId());
			evals[i] = convertNurseInfo(contract, pats, dayoff[i], shiftoff[i], skillset,
					dayoffset, skillForShift, nightShift);
		}

		int nconstr = evals[0].weights.length;
		boolean[] used = new boolean[nconstr];
		int nused = 0;
		for (int c = 0; c < nconstr; c++) {
			for (int i = 0; i < N; i++) {
				if (evals[i].weights[c] > 0) {
					used[c] = true;
					nused++;
					break;
				}
			}
		}
		int[] constraintIDs = new int[nused];
		int uc = 0;
		for (int c = 0; c < nconstr; c++) {
			if (used[c]) {
				constraintIDs[uc] = c;
				uc++;
			}
		}

		Dim.N = N;
		Dim.D = D;
		Dim.S = S;

		return new ProblemInstance(requirements, evals, nused, constraintIDs);
	}

	public static NurseEvaluator convertNurseInfo(Contract contract, ConvertedPattern[] patterns, int[] days_off,
			int[][] shifts_off, boolean[] skillset, int dayoffset, int[] skillForShift, boolean[] nightShift) {

		int nconstr = 15 + patterns.length;
		ConstraintEvaluator[] constraints = new ConstraintEvaluator[nconstr];
		int[] weights = new int[nconstr];

		WeekendDef wknddef = convertWeekend(contract.getWeekendDefinition(), dayoffset);

		if (contract.getMaxNumAssignments_on() == 1) {
			int target = contract.getMaxNumAssignments();
			constraints[0] = new MaxTotalAssign(target);
			weights[0] = contract.getMaxNumAssignments_weight();
		}
		if (contract.getMinNumAssignments_on() == 1) {
			int target = contract.getMinNumAssignments();
			constraints[1] = new MinTotalAssign(target);
			weights[1] = contract.getMinNumAssignments_weight();
		}
		if (contract.getMaxConsecutiveWorkingDays_on() == 1) {
			int target = contract.getMaxConsecutiveWorkingDays();
			constraints[2] = new MaxConsecutiveWorking(target);
			weights[2] = contract.getMaxConsecutiveWorkingDays_weight();
		}
		if (contract.getMinConsecutiveWorkingDays_on() == 1) {
			int target = contract.getMinConsecutiveWorkingDays();
			constraints[3] = new MinConsecutiveWorking(target);
			weights[3] = contract.getMinConsecutiveWorkingDays_weight();
		}
		if (contract.getMaxConsecutiveFreeDays_on() == 1) {
			int target = contract.getMaxConsecutiveFreeDays();
			constraints[4] = new MaxConsecutiveFreeDays(target);
			weights[4] = contract.getMaxConsecutiveFreeDays_weight();
		}
		if (contract.getMinConsecutiveFreeDays_on() == 1) {
			int target = contract.getMinConsecutiveFreeDays();
			constraints[5] = new MinConsecutiveFreeDays(target);
			weights[5] = contract.getMinConsecutiveFreeDays_weight();
		}
		if (contract.getMaxConsecutiveWorkingWeekends_on() == 1) {
			int target = contract.getMaxConsecutiveWorkingWeekends();
			constraints[6] = new MaxConsecutiveWorkingWeekends(target, wknddef);
			weights[6] = contract.getMaxConsecutiveWorkingWeekends_weight();
		}
		if (contract.getMinConsecutiveWorkingWeekends_on() == 1) {
			int target = contract.getMinConsecutiveWorkingWeekends();
			constraints[7] = new MinConsecutiveWorkingWeekends(target, wknddef);
			weights[7] = contract.getMinConsecutiveWorkingWeekends_weight();
		}
		/* not implemented, because not used in any instance
		if (contract.getMaxWorkingWeekendsInFourWeeks_on() == 1) {
			int target = contract.getMaxWorkingWeekendsInFourWeeks();
			constraints[8] = new MaxWorkingWeekendsInFourWeeks(target, wknddef);
			weights[8] = contract.getMaxWorkingWeekendsInFourWeeks_weight();
		} */
		if (contract.isCompleteWeekends()) {
			constraints[9] = new CompleteWeekends(wknddef);
			weights[9] = contract.getCompleteWeekends_weight();
		}
		if (contract.isIdenticalShiftTypesDuringWeekend()) {
			constraints[10] = new IdenticalShiftsInWeekend(wknddef);
			weights[10] = contract.getIdenticalShiftTypesDuringWeekend_weight();
		}
		if (contract.isNoNightShiftBeforeFreeWeekend()) {
			constraints[11] = new NoNightShiftBeforeFreeWeekend(wknddef, nightShift);
			weights[11] = contract.getNoNightShiftBeforeFreeWeekend_weight();
		}
		if (contract.isAlternativeSkillCategory()) {
			constraints[12] = new AlternativeSkill(skillForShift, skillset);
			weights[12] = contract.getAlternativeSkillCategory_weight();
		}

		int D = days_off.length;
		int S = shifts_off[0].length;
		boolean DaysNonempty = false;
		boolean ShiftsNonempty = false;
		for (int d = 0; d < D; d++) {
			if (days_off[d] != 0) {
				DaysNonempty = true;
			}
			for(int s = 0; s < S; s++) {
				if (shifts_off[d][s] != 0) {
					ShiftsNonempty = true;
				}
			}
		}

		if (DaysNonempty) {
			constraints[13] = new DayOffRequests(days_off);
			weights[13] = 1;
		}
		if (ShiftsNonempty) {
			constraints[14] = new ShiftOffRequests(shifts_off);
			weights[14] = 1;
		}

		
		List<Integer> unwanted = contract.getUnwantedPatterns();
		for (int uwp = 0; uwp < unwanted.size(); uwp++) {
			int pat = unwanted.get(uwp).intValue();
			constraints[15+pat] = new UnwantedPattern(patterns[pat].shiftTypes, patterns[pat].weekdays, patterns[pat].weight);
		}

		for (int c = 0; c < nconstr; c++) {
			if (constraints[c] == null) {
				constraints[c] = new EmptyConstraint();
			}
		}

		return new NurseEvaluator(constraints, weights);
	}

	public static boolean isNightShift(Date start, Date end) {
		Calendar startc = Calendar.getInstance();
		startc.setTime(start);
		Calendar endc = Calendar.getInstance();
		endc.setTime(end);

		if (startc.get(Calendar.HOUR_OF_DAY) > endc.get(Calendar.HOUR_OF_DAY)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean[] convertSkillset(Skill[] skillIDs, List<Skill> skillset) {
		boolean[] skset = new boolean[skillIDs.length];
		for (int s = 0; s < skillset.size(); s++) {
			int sk = findSkill(skillIDs, skillset.get(s));
			skset[sk] = true;
		}
		return skset;
	}

	public static int findSkill(Skill[] IDs, Skill id) {
		for (int i = 0; i < IDs.length; i++) {
			if (IDs[i] == id) {
				return i;
			}
		}
		return -1;
	}

	public static int findShiftType(String[] IDs, String id) {
		for (int i = 0; i < IDs.length; i++) {
			if (IDs[i].equals(id)) {
				return i;
			}
		}
		return -1;
	}

	public static class ConvertedPattern {
		int[] shiftTypes;
		int[] weekdays;
		int weight;
		
		public ConvertedPattern(Pattern p, String[] shiftIDs) {
			this.weight = p.getWeight();
			List<PatternEntry> entries = p.getPatternEntryList();
			int size = entries.size();
			int[] shifts = new int[size];
			int[] days = new int[size];
			for (int e = 0; e < size; e++) {
				PatternEntry pe = entries.get(e);
				String st = pe.getShiftType();
				if (st.equals("None")) {
					shifts[e] = 0;
				} else if (st.equals("Any")) {
					shifts[e] = shiftIDs.length;
				} else {
					shifts[e] = findShiftType(shiftIDs, st);
				}
				days[e] = convertWeekday(pe.getDay());
			}
			this.shiftTypes = shifts;
			this.weekdays = days;
		}
	}

	public static int convertWeekday(Day weekday) {
		if (weekday == Day.Monday) {
			return 0;
		} else if (weekday == Day.Tuesday) {
			return 1;
		} else if (weekday == Day.Wednesday) {
			return 2;
		} else if (weekday == Day.Thursday) {
			return 3;
		} else if (weekday == Day.Friday) {
			return 4;
		} else if (weekday == Day.Saturday) {
			return 5;
		} else if (weekday == Day.Sunday) {
			return 6;
		} else {
			return 7; // Any
		}
	}

	public static WeekendDef convertWeekend(List<Day> dayList, int dayoffset) {
		int ndays = dayList.size();
		boolean[] weekend = new boolean[7];
		for (int d = 0; d < ndays; d++) {
			int day = convertWeekday(dayList.get(d));
			weekend[day] = true;
		}
		int start = -1;
		int end = -1;
		boolean started = false;
		for (int i = 0; i < 7; i++) {
			int d = (i + 2) % 7;
			if (!started && weekend[d]) {
				start = d;
				started = true;
			}
			int d1 = (i + 3) % 7;
			if (started && !weekend[d1]) {
				end = d;
				started = false;
				break;
			}
		}
		return new WeekendDef(start, end, dayoffset);
	}
}
