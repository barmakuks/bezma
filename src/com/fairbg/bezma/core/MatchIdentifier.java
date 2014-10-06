package com.fairbg.bezma.core;

import java.util.UUID;

/**Класс идентификатор матча*/
public class MatchIdentifier implements Cloneable{

	public UUID value = null;
	
	public boolean isEmpty() {
		return value == null;
	}
	
	public static MatchIdentifier generateNew(){
		MatchIdentifier ret = new MatchIdentifier();
		ret.value = UUID.randomUUID();
		return ret;
	} 
	
	public static MatchIdentifier fromString(String str){
		MatchIdentifier ret = new MatchIdentifier();
		ret.value = UUID.fromString(str);
		return ret;
	}
	
	@Override
	public String toString() {
		return (value == null) ? "" : value.toString();
	}
	
	@Override
	public boolean equals(Object o) {		
		return (o instanceof MatchIdentifier) 
				&& (value != null) 
				&& (value.equals(((MatchIdentifier)o).value));
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		MatchIdentifier newId = new MatchIdentifier();
		newId.value = this.value;
		return super.clone();
	}
}
