package schedule;

import java.util.ArrayList;

public class Group {
	String number;
	Lesson[] shedule = new Lesson[36];
	ArrayList<Lesson> lessons;

	Group(String num) {
		number = num;
	}

	Group(String num, ArrayList<Lesson> les) {
		number = num;
		lessons = (ArrayList<Lesson>) les.clone();
	}

}