package Entity;

enum SchoolCategory {
	SCSE, NBS, SPMS, RSiS, WKWSCI, MAE, CEE, ASEAN;
}

public class School {

	private int AULimit;
	private SchoolCategory school;
	
	public School(int AULimit, SchoolCategory school) {
		this.AULimit = AULimit;
		this.school = school;
	}
	
	public int getAULimit() { return this.AULimit; }
}
