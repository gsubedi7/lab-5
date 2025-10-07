package app;

import api.GradeDataBase;
import api.MongoGradeDataBase;
import usecase.FormTeamUseCase;
import usecase.GetAverageGradeUseCase;
import usecase.GetGradeUseCase;
import usecase.GetTopGradeUseCase;
import usecase.JoinTeamUseCase;
import usecase.LeaveTeamUseCase;
import usecase.LogGradeUseCase;

/**
 * Config class to provide use cases with the necessary dependencies.
 */

public class Config {
    private final GradeDataBase gradeDataBase = new MongoGradeDataBase();

    /**
     * Get the GetGradeUseCase object.
     * @return GetGradeUseCase object.
     */
    public GetGradeUseCase getGradeUseCase() {
        return new GetGradeUseCase(gradeDataBase);
    }

    /**
     * Get the LogGradeUseCase object.
     * @return LogGradeUseCase object.
     */
    public LogGradeUseCase logGradeUseCase() {
        return new LogGradeUseCase(gradeDataBase);
    }

    /**
     * Get the FormTeamUseCase object.
     * @return FormTeamUseCase object.
     */
    public FormTeamUseCase formTeamUseCase() {
        return new FormTeamUseCase(gradeDataBase);
    }

    /**
     * Get the JoinTeamUseCase object.
     * @return JoinTeamUseCase object.
     */
    public JoinTeamUseCase joinTeamUseCase() {
        return new JoinTeamUseCase(gradeDataBase);
    }

    /**
     * Get the LeaveTeamUseCase object.
     * @return LeaveTeamUseCase object.
     */
    public LeaveTeamUseCase leaveTeamUseCase() {
        return new LeaveTeamUseCase(gradeDataBase);
    }

    /**
     * Get the GetAverageGradeUseCase object.
     * @return GetAverageGradeUseCase object.
     */
    public GetAverageGradeUseCase getAverageGradeUseCase() {
        return new GetAverageGradeUseCase(gradeDataBase);
    }
    public GetTopGradeUseCase getTopGradeUseCase() {
        return new GetTopGradeUseCase(gradeDataBase);
    }
    // TODO Task 4: add code for the new GetTopGradeUseCase following the same pattern as the other use cases above.
}
