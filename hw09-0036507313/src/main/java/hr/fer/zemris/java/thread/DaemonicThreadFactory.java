package hr.fer.zemris.java.thread;

import java.util.concurrent.ThreadFactory;

/**
 * Tvornica demonskih dretvi
 * @author Luka Dragutin
 *
 */
public class DaemonicThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return t;
	}

}
