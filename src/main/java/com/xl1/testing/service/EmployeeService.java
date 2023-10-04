package com.xl1.testing.service;

import java.util.List;
import java.util.Optional;

import com.xl1.testing.entity.Employee;

public interface EmployeeService {
	Employee saveEmployee(Employee employee);
	List<Employee> getAllEmployees();
	Optional<Employee> getEmployeeById(Long id);
	Employee updateEmployee(Employee employee);
	void deleteEmployee(Long id);
}
