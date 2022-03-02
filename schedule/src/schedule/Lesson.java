package schedule;

import java.util.ArrayList;

public class Lesson {
	String name;
	ArrayList<String> requirements;
	boolean used = false;
	Teacher teacher;
	ArrayList<Auditor> right = new ArrayList<Auditor>();
	Auditor auditor;

	Lesson(String n) {
		name = n;
	}

	Lesson(String n, ArrayList<String> req) {
		name = n;
		requirements = (ArrayList<String>) req.clone();
	}

	Lesson(String n, String req) {
		name = n;
		requirements = new ArrayList<String>();
		for (String string : req.split(", ")) {
			requirements.add(string);
		}
	}

	Lesson(String n, String req, Teacher t) {
		name = n;
		requirements = new ArrayList<String>();
		for (String string : req.split(", ")) {
			requirements.add(string);
		}
		teacher = t;
	}

	Lesson(Lesson l) {
		name = l.name;
		requirements = l.requirements;
		teacher = l.teacher;
	}
}
