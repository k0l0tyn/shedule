package schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class main {
	static ArrayList<Integer[]> freedom = new ArrayList<Integer[]>();
	static String username;
	static String password;

	public static void put(Integer[] a) {
		int size = freedom.size();
		boolean trigger = true;
		for (int i = 0; i < size; i++) {
			if (freedom.get(i)[0] > a[0]) {
				freedom.add(i, a);
				trigger = false;
				break;
			}
		}
		if (freedom.size() == 0 || trigger) {
			freedom.add(a);
		}
	}

	public static void main(String[] args) {
		ArrayList<Auditor> auditors = new ArrayList<Auditor>();
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		ArrayList<Group> groups = new ArrayList<Group>();
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		boolean flag = true;
		try {
			String url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=schedule";
			if (args.length > 1) {
				username = args[0];
				password = args[1];
			} else {
				username = "sa";
				password = "123456";
			}
			String query = "";
			query = "select * from Auditors";
			Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				auditors.add(new Auditor(rs.getInt("Corpus"), rs.getInt("Number"),
						rs.getString("Requirements").toUpperCase().trim()));
			}

			query = "SELECT *\r\n" + "  FROM [Schedule].[dbo].[Teachers] t\r\n"
					+ "  inner join [Schedule].[dbo].[Lessons] l\r\n" + "  on nameTeacher = teacher \r\n"
					+ "  order by  nameTeacher";
			rs = stmt.executeQuery(query);
			String ID = null;
			Teacher _teacher = null;
			while (rs.next()) {
				if (!rs.getString("IDteacher").equals(ID)) {
					_teacher = new Teacher(rs.getString("IDteacher").toUpperCase().trim() + "_"
							+ rs.getString("nameTeacher").toUpperCase().trim());
					ID = rs.getString("IDteacher").toUpperCase().trim() + "_"
							+ rs.getString("nameTeacher").toUpperCase().trim();
				}

				lessons.add(
						new Lesson(/* rs.getInt("IDlesson") + "_" + */rs.getString("nameLesson").toUpperCase().trim(),
								rs.getString("Requirements").toUpperCase().trim(), _teacher));
				_teacher.lessons.add(lessons.get(lessons.size() - 1));
				teachers.add(_teacher);
			}

			query = "select * from Groups";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String[] _s = rs.getString("lessons").toUpperCase().trim().split(", ");
				ArrayList<Lesson> _lessons = new ArrayList<Lesson>();
				for (String string : _s) {
					for (Lesson lesson : lessons) {
						if (lesson.name.equals(string)) {
							_lessons.add(lesson);
							break;
						}
					}
				}
				groups.add(new Group(rs.getString("Number").toUpperCase().trim(), _lessons));
			}
			boolean[][] l = new boolean[6][7];
			int p[] = new int[teachers.size()];// ��������� �������������

			for (int i = 0; i < groups.size(); i++) {// ��� ������ ������
				for (int j = 0; j < groups.get(i).lessons.size(); j++) {// ��� ������� �����
					for (int k = 0; k < auditors.size(); k++) {// ��� ������ ���������
						for (String n : groups.get(i).lessons.get(j).requirements) {// ��� ������� ����������
							if (!auditors.get(k).requirements.contains(n) && !n.equals("")) {// ���� ������ ����������
																								// ����������� � �������
																								// ���������
								flag = false;
							}
						}
						if (flag) {
							groups.get(i).lessons.get(j).right.add(auditors.get(k));// ���������
																					// �����������
																					// ���������
						}
						flag = true;
					}
					Integer[] freedomL = new Integer[2];
					freedomL[1] = j;

					freedomL[0] = groups.get(i).lessons.get(j).right.size() / groups.get(i).lessons.size()
							/ lessons.get(j).teacher.lessons.size();
					put(freedomL);

				}

				System.out.println();
				for (int j = 0; j < freedom.size(); j++) {// � ��� ���� ���� � ����������� ��������
					boolean notok = true;
					q: for (int k = 0; k < 36; k++) {
						for (int n = 0; n < groups.get(i).lessons.get(freedom.get(j)[1]).right.size(); n++) {
							if (groups.get(i).lessons.get(freedom.get(j)[1]).teacher.shedule[k] == null
									& groups.get(i).lessons.get(freedom.get(j)[1]).right.get(n).shedule[k] == null
									& groups.get(i).shedule[k] == null) {
								groups.get(i).shedule[k] = groups.get(i).lessons.get(freedom.get(j)[1]);
								groups.get(i).lessons.get(freedom.get(j)[1]).teacher.shedule[k] = groups.get(i).lessons
										.get(freedom.get(j)[1]);
								groups.get(i).lessons.get(freedom.get(j)[1]).right.get(n).shedule[k] = groups
										.get(i).lessons.get(freedom.get(j)[1]);
								groups.get(i).lessons.get(
										freedom.get(j)[1]).auditor = groups.get(i).lessons.get(freedom.get(j)[1]).right
												.get(n);
								notok = false;
								break q;
							}
						}
					}
					if (notok)
						System.out.println("�������: " + groups.get(i).lessons.get(freedom.get(j)[1]).name
								+ ", ������: " + groups.get(i).number
								+ " �� ������� ���������� ������� ��� ���������� �������");

				}

				freedom.clear();
			}
			System.out.println("���������� �����:");
			boolean blankSpaces = false;
			for (Group group : groups) {
				System.out.println("");
				System.out.println("������ �" + group.number);
				for (int i = 0; i < 36; i++) {
					Lesson lesson = group.shedule[i];
					if (lesson != null) {
						System.out.print("���� ������: " + (i / 6 + 1) + ", ����� �������: " + (i % 6 + 1) + ": ");
						System.out.println(lesson.name + ", ���. " + lesson.auditor.number + ", ����. "
								+ lesson.auditor.pavilion + ", ����.: " + lesson.teacher.name.split("_")[1]);
					}

					else if (blankSpaces) {
						System.out.print("���� ������: " + (i / 6 + 1) + ", ����� �������: " + (i % 6 + 1) + ": ");
						System.out.println("-----------------------");
					}

				}
			}

			System.out.println("");
			System.out.println("");
			System.out.println("���������� ��������������:");

			for (Teacher teacher : teachers) {
				System.out.println("");
				System.out.println("�������������: " + teacher.name);
				for (int i = 0; i < 36; i++) {
					Lesson lesson = teacher.shedule[i];
					if (lesson != null) {
						System.out.print("���� ������: " + (i / 6 + 1) + ", ����� �������: " + (i % 6 + 1) + ": ");
						System.out.println(
								lesson.name + ", ���. " + lesson.auditor.number + ", ����. " + lesson.auditor.pavilion);
					}

					else if (blankSpaces) {
						System.out.print("���� ������: " + (i / 6 + 1) + ", ����� �������: " + (i % 6 + 1) + ": ");
						System.out.println("-----------------------");
					}

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
