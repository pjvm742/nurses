package nurses;

import java.util.List;
import java.lang.Integer;
import java.util.Calendar;
import Attributes.*;
import Helper.Helper;

public class Convert {

	public static int[][] convertSolution(List<int[][]> orig) {
		int D = orig.Length();
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

	public static ProblemInstance convertProblem(SchedulingPeriod s) {
		Helper h = new Helper(s, new List<[][]int>);

		List<Skill> skills = s.getSkills();
		nskills = skills.Length();
		Skill[] skillIDs = new Skill[nskills];
		for (int i = 0; i < nskills; i++) {
			skillIDs[i] = skills.get(i);
		}

		List<Shift> shiftTypes = h.getShiftList();
		S = shiftTypes.Length() + 1;
		String[] shiftIDs = new String[S];
		shiftIDs[0] = "None";
		int[] skillForShift = new int[S];
		boolean[] nightShift = new boolean[S];
		for (int i = 0; i < S-1, i++) {
			Shift st = shiftTypes.get(i);
			shiftIDs[i+1] = st.getID();
			skillForShift[i+1] = findSkill(skillIDs, st.getSkills.get(0));
			nightShift[i+1] = isNightShift(st.startTime, st.endTime)
		}

		D = h.getDaysInPeriod();
		dayoffset = h.getWeekdayOfPeriode(0); // 0: Monday, ..., 6: Sunday; weekday of day i is: (i + offset) % 7

		int[][] requirements = new int[D][S];
		for (int d = 0; d < D; d++) {
			for (int s = 1; s < S; s++) {
				sID = shiftIDs[s];
				requirements[d][s] = h.getRequirement(sID, d);
			}
		}

		List<Employee> nurses = h.getEmployeeList();
		int N = nurses.Length();
		List<Contract> contracts = h.getContractList();
		List<Pattern> patterns = h.getPatternList();
		List<DayOffRequest> off_days = h.getDayOffRequestList();
		List<ShiftOffRequest> off_shifts = h.getShiftOffRequestList();

		npatterns = patterns.Length();
		ConvertedPattern[] pats = new ConvertedPattern[npatterns];
		for (int p = 0; p < npatterns; p++) {
			pats[p] = new ConvertedPattern(patterns.get(p), shiftIDs);
		}

		int[][] dayoff = new int[N][D];
		int[][][] shiftoff = new int[N][D][S];
		for (int r = 0; r < off_days.Length(); r++) {
			req = off_days.get(r);
			n = req.getEmployeeId();
			d = h.getDaysFromStart(req.getDate());
			weight = req.getWeight();
			dayoff[n][d] = weight;
		}
		for (int r = 0; r < off_shifts.Length(); r++) {
			req = off_shifts.get(r);
			n = req.getEmployeeId();
			d = h.getDaysFromStart(req.getDate());
			s = findShiftType(shiftIDs, req.getShiftTypeId());
			weight = req.getWeight();
			dayoff[n][d][s] = weight;
		}

		NurseEvaluator[] evals = new NurseEvaluator[N];
		for (int i = 0; i < N; i++) {
			Employee nurse = nurses.get(i);
			Contract contract = contracts.get(nurse.getContractId());
			evals[i] = convertNurseInfo(contract, pats, dayoff[i], shiftoff[i], nurse.getSkills(),
					dayoffset, skillForShift, nightShift);
		}

		int nconstr = evals[0].weights.length;
		boolean[] used = new boolean[nconstr];
		int nused = 0;
		for (int c = 0; c < nconstr; c++) {
			for (int i = 0; i < N; i++) {
				if (evals[i].weights[c] > 0) {
					used[c] = true;
					nused++
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

		return new ProblemInstance(requirements, evals, nused, constraintIDs, N, D, S);
	}

	public static NurseEvaluator convertNurseInfo(Contract contract, ConvertedPattern[] patterns, double[] days_off,
			double[][] shifts_off, boolean[] skillset, int dayoffset, int[] skillForShift, boolean[] nightShift) {

		int nconstr = 15 + patterns.length;
		ConstraintEvaluator[] constraints = new ConstraintEvaluator[nconstr];
		int weights = new int[nconstr];

		WeekendDef wknddef = convertWeekend(contract.getWeekendDefinition(), dayoffset);

		if (contract.getMaxNumAssignments_on() == 1) {
			int target = contract.getMaxNumAssignments();
			constraints[0] = new MaxAssignments(target);
			weights[0] = contract.getMaxNumAssignments_weight();
		}
		if (contract.getMinNumAssignments_on() == 1) {
			int target = contract.getMinNumAssignments();
			constraints[1] = new MinAssignments(target);
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
			constraints[4] = new MaxConsecutiveFree(target);
			weights[4] = contract.getMaxConsecutiveFreeDays_weight();
		}
		if (contract.getMinConsecutiveFreeDays_on() == 1) {
			int target = contract.getMinConsecutiveFreeDays();
			constraints[5] = new MinConsecutiveFree(target);
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
		if (contract.getMaxWorkingWeekendsInFourWeeks_on() == 1) {
			int target = contract.getMaxWorkingWeekendsInFourWeeks();
			constraints[8] = new MaxWorkingWeekendsInFourWeeks(target, wknddef);
			weights[8] = contract.getMaxWorkingWeekendsInFourWeeks_weight();
		}
		if (contract.getCompleteWeekends()) {
			constraints[9] = new CompleteWeekends(wknddef);
			weights[9] = contract.getCompleteWeekends_weight();
		}
		if (contract.getIdenticalShiftTypesDuringWeekend()) {
			constraints[10] = new SameShiftDuringWeekend(wknddef);
			weights[10] = contract.getIdenticalShiftTypesDuringWeekend_weight();
		}
		if (contract.getNoNightShiftBeforeFreeWeekend()) {
			constraints[11] = new NoNightShiftBeforeFreeWeekend(wknddef, nightShift);
			weights[11] = contract.getNoNightShiftBeforeFreeWeekend_weight();
		}
		if (contract.getAlternativeSkillCategory()) {
			constraints[12] = new AlternativeSkill(skillForShift);
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
		for (int uwp = 0; uwp < unwanted.Length(); uwp++) {
			int pat = unwanted.get(uwp).intValue();
			constraints[15+pat] = new UnwantedPattern(patterns[pat].ShiftTypes, patterns[pat].Weekdays);
		}

		for (int c = 0; c < nconstr; c++) {
			if (constraints[c] == null) {
				constraints[c] = new EmptyConstraint();
			}
		}

		return new NurseEvaluator(constraints, weights);
	}

	public static boolean isNightShift(Date start, Date end) {
		Calendar startc = Calendar(start);
		Calendar endc = Calendar(end);

		if (startc.get(Calendar.HOUR_OF_DAY) > endc.get(Calendar.HOUR_OF_DAY)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean[] convertSkillset(Skill[] skillIDs, List<Skill> skillset) {
		boolean skset = new boolean[skillIDs.length];
		for (int s = 0; s < skillset.Length()) {
			sk = findSkill(skillset.get(s));
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
			if (IDs[i].equals(id) {
				return i;
			}
		}
		return -1;
	}

	class ConvertedPattern {
		int[] shiftTypes;
		int[] weekdays;
		double weight;
		
		public ConvertedPattern(Pattern p, String[] shiftIDs) {
			this.weight = (double) p.getWeight();
			List<PatternEntry> entries = p.getPatternEntryList();
			int size = entries.Length();
			shifts = new int[size];
			days = new int[size];
			for (int e = 0; e < size; e++) {
				pe = entries.get(e);
				st = pe.getShiftType();
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
		if (weekday == Monday) {
			return 0;
		} else if (weekday == Tuesday) {
			return 1;
		} else if (weekday == Wednesday) {
			return 2;
		} else if (weekday == Thursday) {
			return 3;
		} else if (weekday == Friday) {
			return 4;
		} else if (weekday == Saturday) {
			return 5;
		} else if (weekday == Sunday) {
			return 6;
		} else {
			return 7; // Any
		}
	}
}
