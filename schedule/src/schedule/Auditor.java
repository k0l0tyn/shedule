package schedule;

import java.util.ArrayList;
import java.util.Collection;

public class Auditor {
	int pavilion;
	int number;
	ArrayList<String> requirements;
	Lesson[] shedule = new Lesson[36];

	Auditor(int pav, int num) {
		pavilion = pav;
		number = num;
	}

	Auditor(int pav, int num, ArrayList<String> req) {
		pavilion = pav;
		number = num;
		requirements = (ArrayList<String>) req.clone();
	}

	Auditor(int pav, int num, String req) {
		pavilion = pav;
		number = num;
		requirements = new ArrayList<String>();
		for (String string : req.split(", ")) {
			requirements.add(string);
		}
	}
}
