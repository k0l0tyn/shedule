package schedule;

import java.util.ArrayList;

public class Teacher {
	String name;
	ArrayList<Lesson> lessons;
	Lesson[] shedule = new Lesson[36];

	Teacher(String n, Lesson l) {
		name = n;
		lessons.add(l);
	}

	Teacher(String n, ArrayList<Lesson> l) {
		name = n;
		lessons = (ArrayList<Lesson>) l.clone();
	}

	Teacher(String n) {
		name = n;
		lessons = new ArrayList<Lesson>();
	}
}
