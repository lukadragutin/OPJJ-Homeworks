package ispit;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DateLabel extends JLabel implements ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateLabel() {
		super();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList<LocalDate> jList = (JList<LocalDate>) e.getSource();
		LocalDate localDate = jList.getSelectedValue();
		var dow = localDate.getDayOfWeek();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("Na datum " + localDate.toString() + " ");

		if (localDate.isBefore(LocalDate.now())) {
			switch (dow) {
			case MONDAY:
			case TUESDAY:
			case THURSDAY:
			case FRIDAY:
				sBuilder.append("bio je ");
				break;
			case WEDNESDAY:
			case SATURDAY:
			case SUNDAY:
				sBuilder.append("bila je ");
			}
		} else {
			sBuilder.append("biti će ");
		}

		switch (dow) {
		case MONDAY:
			sBuilder.append("ponedjeljak.");
			break;
		case TUESDAY:
			sBuilder.append("utorak.");
			break;
		case WEDNESDAY:
			sBuilder.append("srijeda.");
			break;
		case THURSDAY:
			sBuilder.append("četvrtak.");
			break;
		case FRIDAY:
			sBuilder.append("petak.");
			break;
		case SATURDAY:
			sBuilder.append("subota.");
			break;
		case SUNDAY:
			sBuilder.append("nedjelja.");
			break;
		}
		this.setText(sBuilder.toString());
	}
}
