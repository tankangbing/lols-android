package com.example.spt.jaxb.course;


public class Learningbehavior {
	
	String identifier;
	String name;
	String type;
	Behaviorsstandard behaviorsstandard;
	Behaviorsconstraint behaviorsconstraint;
	Prebehavior prebehavior;
	
	
	
	
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Behaviorsstandard getBehaviorsstandard() {
		return behaviorsstandard;
	}
	public void setBehaviorsstandard(Behaviorsstandard behaviorsstandard) {
		this.behaviorsstandard = behaviorsstandard;
	}
	public Behaviorsconstraint getBehaviorsconstraint() {
		return behaviorsconstraint;
	}
	public void setBehaviorsconstraint(Behaviorsconstraint behaviorsconstraint) {
		this.behaviorsconstraint = behaviorsconstraint;
	}
	public Prebehavior getPrebehavior() {
		return prebehavior;
	}
	public void setPrebehavior(Prebehavior prebehavior) {
		this.prebehavior = prebehavior;
	}
	
	

}
