package usecase;

import api.GradeDataBase;
import entity.Grade;
import entity.Team;

/**
 * GetAverageGradeUseCase class.
 */
public final class GetAverageGradeUseCase {
    private final GradeDataBase gradeDataBase;

    public GetAverageGradeUseCase(GradeDataBase gradeDataBase) {
        this.gradeDataBase = gradeDataBase;
    }

    /**
     * Get the average grade for a course across your team.
     * @param course The course.
     * @return The average grade.
     */
    public float getAverageGrade(String course) {
        float sum = 0;
        int count = 0;

        // Get team from database
        final Team team = gradeDataBase.getMyTeam();

        // For each member in the team, get their grades
        for (String username : team.getMembers()) {
            Grade[] grades = gradeDataBase.getGrades(username);

            // Filter only grades for the specified course
            for (Grade grade : grades) {
                if (grade.getCourse().equals(course)) {
                    sum += grade.getGrade();
                    count++;
                }
            }
        }

        if (count == 0) {
            return 0;
        }

        return sum / count;
    }
}

