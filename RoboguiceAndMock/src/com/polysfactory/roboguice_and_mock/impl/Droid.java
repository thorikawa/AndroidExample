package com.polysfactory.roboguice_and_mock.impl;

import com.polysfactory.roboguice_and_mock.Person;

public class Droid implements Person {
	private String color;

	public String getName() {
		return "droid";
	}
	
	public void setColor(String color){
		this.color = color;
	}
}
