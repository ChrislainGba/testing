package com.xl1.testing.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xl1.testing.entity.Employee;
import com.xl1.testing.exception.ResourceNotFoundException;
import com.xl1.testing.repository.EmployeeRepository;
import com.xl1.testing.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	//@Autowired
	EmployeeRepository employeeRepository;
	
	//@Autowired we don't ned it because there is only one constructor
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}	


	@Override
	public Employee saveEmployee(Employee employee) {
		Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
		if(savedEmployee.isPresent()) {
			throw new ResourceNotFoundException("Employee already exist with gien email: "+employee.getEmail());
		}
		return employeeRepository.save(employee);
	}


	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}


	@Override
	public Optional<Employee> getEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}


	@Override
	public Employee updateEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}


	@Override
	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
		
	}

}
