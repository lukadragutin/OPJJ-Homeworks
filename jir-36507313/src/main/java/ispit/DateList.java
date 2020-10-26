package ispit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataListener;

public class DateList extends AbstractListModel<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> dates;
	
	private List<ListDataListener> listeners;
	
	
	public DateList (List<LocalDate> dates) {
		this.dates = new ArrayList<>();
		dates.forEach(l -> this.dates.add(l.toString()));
		listeners = new ArrayList<>();
		fireIntervalAdded(this, 0, dates.size() - 1);
	}

	public void print() {
		System.out.print("Printing dates.\n");
		dates.forEach(l -> System.out.print(l.toString() + "\n"));
	}
	
	@Override
	public int getSize() {
		return dates.size();
	}

	@Override
	public String getElementAt(int index) {
		return dates.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
	public void updateDates(List<LocalDate> dates) {
		this.dates.clear();
		dates.forEach(l -> this.dates.add(l.toString()));
		fireIntervalAdded(this, 0, dates.size() - 1);
	}
}
