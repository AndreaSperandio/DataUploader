package model;

import java.util.ArrayList;
import java.util.List;

import view.component.DUComboBoxItem;

public class Column {
	public enum Type {
		STRING(0), BOOL(1), INT(2), DOUBLE(3);

		private int value;

		private Type(final int value) {
			this.value = value;
		}

		public static Type toType(final int type) {
			if (type == STRING.value) {
				return STRING;
			}
			if (type == BOOL.value) {
				return BOOL;
			}
			if (type == INT.value) {
				return INT;
			}
			if (type == DOUBLE.value) {
				return DOUBLE;
			}
			return STRING;
		}

		public static List<DUComboBoxItem<Type>> getColumnComboItems() {
			final List<DUComboBoxItem<Type>> l = new ArrayList<>();
			l.add(new DUComboBoxItem<>(STRING, STRING.toString()));
			l.add(new DUComboBoxItem<>(BOOL, BOOL.toString()));
			l.add(new DUComboBoxItem<>(INT, INT.toString()));
			l.add(new DUComboBoxItem<>(DOUBLE, DOUBLE.toString()));
			return l;
		}
	}

	private Type type;
	private String name;

	public Column(final Type type, final String name) {
		super();
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Column [type=" + this.type + ", name=" + this.name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		result = prime * result + (this.type == null ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Column other = (Column) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.type != other.type) {
			return false;
		}
		return true;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
