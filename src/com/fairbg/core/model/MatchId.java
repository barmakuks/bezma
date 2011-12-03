package com.fairbg.core.model;

import java.util.UUID;

public class MatchId {

	public UUID value = null;
	public boolean isEmpty() {
		return value == null;
	}
	public static MatchId generateNew(){
		MatchId ret = new MatchId();
		ret.value = UUID.randomUUID();
		return ret;
	} 
	public static MatchId fromString(String str){
		MatchId ret = new MatchId();
		ret.value = UUID.fromString(str);
		return ret;
	}
	@Override
	public String toString() {
		return (value == null) ? "" : value.toString();
	}
	@Override
	public boolean equals(Object o) {		
		return (o instanceof MatchId) 
				&& (value != null) 
				&& (value.equals(((MatchId)o).value));
	}
}
