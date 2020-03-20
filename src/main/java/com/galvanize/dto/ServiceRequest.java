package com.galvanize.dto;

public class ServiceRequest {
    private Long customerNumber;
    private String firstName;
    private String lastName;
    private String telephone;
    private String problem;
    private String problemDetails;

    public ServiceRequest() { }

    public ServiceRequest(Long customerNumber, String firstName, String lastName, String telephone, String problem, String problemDetails) {
        this.customerNumber = customerNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.problem = problem;
        this.problemDetails = problemDetails;
    }

    public ServiceRequest(Long customerNumber, String problem, String problemDetails) {
        this.customerNumber = customerNumber;
        this.problem = problem;
        this.problemDetails = problemDetails;
    }
    public ServiceRequest(String telephone, String problem, String problemDetails) {
        this.telephone = telephone;
        this.problem = problem;
        this.problemDetails = problemDetails;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemDetails() {
        return problemDetails;
    }

    public void setProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
    }
}
