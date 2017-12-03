package com.bharavi.naukriemployer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bharavi on 03-12-2017.
 */

public class JobModel {
        int RequirementID;
        int Age;
        int Salary;
        int Duration;
        String District;
        int Vacancy;
        String TypeOfWork;
        String FullAddress;
        int EmployerID;



    public JobModel(JSONObject object) throws JSONException {
        RequirementID=object.getInt("RequirementID");
        Age=object.getInt("Age");
        Salary=object.getInt("Salary");
        Duration=object.getInt("Duration");
        Vacancy=object.getInt("Vacancy");
        Age=object.getInt("Age");
        District=object.getString("District");
        TypeOfWork=object.getString("TypeOfWork");
        FullAddress=object.getString("FullAddress");
        EmployerID=object.getInt("EmployerID");

    }
    public int getRequirementID() {
        return RequirementID;
    }
    public void setRequirementID(int requirementID) {
        RequirementID = requirementID;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public int getVacancy() {
        return Vacancy;
    }

    public void setVacancy(int vacancy) {
        Vacancy = vacancy;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        TypeOfWork = typeOfWork;
    }

    public String getFullAddress() {
        return FullAddress;
    }

    public void setFullAddress(String fullAddress) {
        FullAddress = fullAddress;
    }



    public int getEmployerID() {
        return EmployerID;
    }

    public void setEmployerID(int employerID) {
        EmployerID = employerID;
    }


}
