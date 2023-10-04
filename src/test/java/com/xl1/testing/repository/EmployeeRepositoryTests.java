package com.xl1.testing.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.xl1.testing.entity.Employee;


//Will automatically configure inmemory DB for test purpose
@DataJpaTest
public class EmployeeRepositoryTests {
	
	@Autowired 
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	
	//factoriser le code
	@BeforeEach
	public void setup() {
		employee = Employee.builder()
				.firstName("minpikk")
				.lastName("minpiker")
				.email("minpik@xl1.com")
				.build();
	}
	
	//JUnit test for save employee operation
	@DisplayName("JUnit Repository test for save employee operation")
	@Test
	public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
		
		//given - precondition or setup
		
		//when - action or the behaviour we are going to test
		Employee savedEmployee = employeeRepository.save(employee);
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
		assertThat(savedEmployee.getId()).isGreaterThan(0);
	}
	
	@DisplayName("JUnit Repository test for findAll employees operation")
	@Test
	public void givenEmployeesList_whenFindAll_thenReturnEmplyeesList() {
		//given
		Employee employee2 = Employee.builder()
				.firstName("trach")
				.lastName("tracher")
				.email("trach@xl1.com")
				.build();
		employeeRepository.save(employee);
		employeeRepository.save(employee2);
		//when
		List<Employee> employees = employeeRepository.findAll();
		//then
		assertThat(employees).isNotNull();
		assertThat(employees.size()).isEqualTo(2);
	}
	
	//JUnit Repository test for getEmployeeById
	@DisplayName("Junt Repository test for getEmployeeById")
	@Test
	public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
		//given
		
		employeeRepository.save(employee);
		//when
		Employee employeeDB = employeeRepository.findById(employee.getId()).get();
		//then
		assertThat(employeeDB).isNotNull();
	}
	
	//JUnit Repository test for getEmployeeByEmail
	@DisplayName("JUnit Repository test for getEmployeeByEmail")
	@Test
	public void givenEmplyeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
		//given - precondition or setup

		employeeRepository.save(employee);
		//when - action or the behaviour we are going to test
		Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
		//then - verify the output
		assertThat(employeeDB).isNotNull();
		assertThat(employeeDB.getEmail()).isEqualTo(employee.getEmail());
	}
	
	//JUnit Repository test for update employee operation
	@DisplayName("JUnit Repository test for update employee operation")
	@Test
	public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {
		//given - precondition or setup
		
		employeeRepository.save(employee);
		//when - action or the behaviour we are going to test
		Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
		savedEmployee.setFirstName("Geek");
		Employee updatedEmployee = employeeRepository.save(savedEmployee);
		//then - verify the output
		assertThat(updatedEmployee).isNotNull();
		assertThat(updatedEmployee.getFirstName()).isEqualTo("Geek");
	}
	
	//JUnit Repository test for delete employee operation
	@DisplayName("JUnit Repository test for delete employee operation")
	@Test
	public void givenEmployeeObject_whenDelete_thenRemovedEmployee() {
		//given - precondition or setup

		employeeRepository.save(employee);
		//when - action or the behaviour we are going to test
		employeeRepository.delete(employee);
		Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());
		//then - verify the output
		assertThat(deletedEmployee.isPresent()).isFalse();
		assertThat(deletedEmployee).isEmpty();
	}
	
	//JUnit Repository test for custom query using JPQL with index
	@DisplayName("JUnit Repository test for custom query using JPQL with index params")
	@Test
	public void givenFirstnameAndLastname_whenFindByJPQLIndexParams_thenReturnEmployee() {
		//given - precondition or setup

		employeeRepository.save(employee);
		//when - action or the behaviour we are going to test
		Employee savedEmployee = employeeRepository.findByJPQLIndexParams(employee.getFirstName(), employee.getLastName());
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
	//JUnit Repository test for custom query using JPQL with named params
		@DisplayName("JUnit Repository test for custom query using JPQL with named params")
		@Test
		public void givenFirstnameAndEmail_whenFindByJPQLNamedParams_thenReturnEmployee() {
			//given - precondition or setup

			employeeRepository.save(employee);
			//when - action or the behaviour we are going to test
			Employee savedEmployee = employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getEmail());
			//then - verify the output
			assertThat(savedEmployee).isNotNull();
		}
		
		//JUnit Repository test for custom query using native SQL with index params
		@DisplayName("JUnit Repository test for custom query using native SQL with index params")
		@Test
		public void givenFirstnameAndLastname_whenFindByNativeSQLIndex_thenReturnEmployee() {
			//given - precondition or setup
			
			employeeRepository.save(employee);
			//when - action or the behaviour we are going to test
			Employee savedEmployee = employeeRepository.findByNativeSQLIndex(employee.getFirstName(), employee.getLastName());
			//then - verify the output
			assertThat(savedEmployee).isNotNull();
		}
		
		//JUnit Repository test for custom query using native SQL with named params
		@DisplayName("JUnit Repository test for custom query using native SQL with named params")
		@Test
		public void givenFirstnameAndLastname_whenFindByNativeSQLNamed_thenReturnEmployee() {
			//given - precondition or setup
			
			employeeRepository.save(employee);
			//when - action or the behaviour we are going to test
			Employee savedEmployee = employeeRepository.findByNativeSQLIndex(employee.getFirstName(), employee.getLastName());
			//then - verify the output
			assertThat(savedEmployee).isNotNull();
		}
}
