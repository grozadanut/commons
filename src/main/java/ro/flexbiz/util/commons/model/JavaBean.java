package ro.flexbiz.util.commons.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class JavaBean {
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	private boolean silenceListeners = false;
	
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		if (!silenceListeners)
			changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	/**
	 * @param silenceListeners if true, PropertyChangeListeners won't be called until set to false
	 */
	public void silenceListeners(final boolean silenceListeners) {
		this.silenceListeners = silenceListeners;
	}
}
