package edu.handong.csee.pp1.dp;

public class RawInstance {
	
	String javaFilePath;
	
	static public final int NUMMETRICS = 8;
	// 인스턴스의 메트릭 값을 저장하는 field들.
	int[] values = new int[NUMMETRICS];
	
	int linesOfCode;
	int numOfVariables;
	int numOfInstanaceVariables;
	int numOfMethods;
	int numOfPublicMethods;
	int numOfMethodInvocation;
	int numOfForLoops;
	int numOfIfStatements;
	
	public RawInstance(String path) {
		setJavaFilePath(path);
	}
	
	public int[] getValues() {
		return values;
	}

	public String getJavaFilePath() {
		return javaFilePath;
	}
	
	public void setJavaFilePath(String javaFilePath) {
		this.javaFilePath = javaFilePath;
	}
	
	public int getLinesOfCode() {
		return values[0];
	}
	
	public void setLinesOfCode(int linesOfCode) {
		values[0] = linesOfCode;
	}
	
	public int getNumOfVariables() {
		return values[1];
	}
	
	public void setNumOfVariables(int numOfVariables) {
		values[1] = numOfVariables;
	}
	
	public int getNumOfInstanceVariables() {
		return values[2];
	}
	
	public void setNumOfInstanceVariables(int numOfInstanceVariables) {
		values[2] = numOfInstanceVariables;
	}
	
	public int getNumOfMethods() {
		return values[3];
	}

	public void setNumOfMethods(int numOfMethods) {
		values[3] = numOfMethods;
	}
	
	public int getNumOfPublicMethods() {
		return values[4];
	}
	
	public void setNumOfPublicMethods(int numOfPublicMethods) {
		values[4] = numOfPublicMethods;
	}
	
	public int getNumOfMethodInvocation() {
		return values[5];
	}
	
	public void setNumOfMethodInvocations(int numOfMethodInvocation) {
		values[5] = numOfMethodInvocation;
	}
	public int getNumOfForLoops() {
		return values[6];
	}
	
	public void setNumOfForLoops(int numOfForLoops) {
		values[6] = numOfForLoops;
	}
	
	public int getNumOfIfStatements() {
		return values[7];
	}
	
	public void setNumOfIfStatements(int numOfIfStatements) {
		values[7] = numOfIfStatements;
	}
}