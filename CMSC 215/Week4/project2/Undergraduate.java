/* 
 * Daniel Smolsky
 * Programming Project 2: Student Honor Society
 * Feb 2, 2024
 * This class creates Undergraduate objects extending from the Student variables and methods. 
 * This class has 1 instance variable and 2 methods that override methods within the Student class.
*/

package project2;

public class Undergraduate extends Student {
    private String year; 
    
    public Undergraduate(String name, int creditHours, int qualityPoints, String schoolYear) {
        super(name, creditHours, qualityPoints);
        this.year = schoolYear;
    }

    @Override
    public boolean eligibleForHonorSociety() {
        if (this.year.equals("Freshman") || this.year.equals("Sophomore")) {
            return false;
        }
        else {
            return super.eligibleForHonorSociety();
        }
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.year;
    }
}