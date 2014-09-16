package de.devisnik.android.sliding;

public class PropertiesBuilder {

	private StringBuilder mBuilder;
	private String mSeparator = "";

	public PropertiesBuilder() {
		mBuilder = new StringBuilder();
	}
	
	public PropertiesBuilder append(String key, String value) {
		mBuilder.append(mSeparator).append(key).append("=").append(value);
		mSeparator = ", ";
		return this;
	}
	
	public PropertiesBuilder append(String key, Number value) {
		append(key, value.toString());
		return this;
	}
	
	@Override
	public int hashCode() {
		return mBuilder.hashCode();
	}
	
	@Override
	public String toString() {
		return mBuilder.toString();
	}
}
