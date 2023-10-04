package com.xl1.testing.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xl1.testing.entity.Employee;
import com.xl1.testing.exception.ResourceNotFoundException;
import com.xl1.testing.repository.EmployeeRepository;
import com.xl1.testing.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
	
	@Mock
	private EmployeeRepository employeeRepository;
	@InjectMocks
	private EmployeeServiceImpl employeeService;
	
	private Employee employee;
	
	@BeforeEach
	public void setup() {
		//static mock
		//employeeRepository = Mockito.mock(EmployeeRepository.class);
		//employeeService = new EmployeeServiceImpl(employeeRepository);
		employee = Employee.builder()
				.firstName("minpik")
				.lastName("minpiker")
				.email("minpik@xl1.com")
				.build();
	}
	
	//JUnit Service test for save employee method
	@DisplayName("JUnit Service test for save employee method")
	@Test
	public void givenEmployeeObject_whenSaveEmployeethenReturnEmployeeObject() {
		//given - precondition or setup
			//stub employeeRepository.findByEmail
			//Stubs are used to replace real objects or components that the code under test relies on.
		given(employeeRepository.findByEmail(employee.getEmail()))
		.willReturn(Optional.empty());
			//stub employeeRepository.save
		given(employeeRepository.save(employee)).willReturn(employee);
		
		System.out.println(employeeRepository);
		System.out.println(employeeService);
		//when - action or the behaviour we are going to test
		Employee savedEmployee = employeeService.saveEmployee(employee);
		System.out.println(savedEmployee);
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
	//JUnit Service test for save employee method with throws exception
		@DisplayName("JUnit Service test for save employee method with throws exception")
		@Test
		public void givenExisting_whenSaveEmployeethenThrowsException() {
			//given - precondition or setup
				//stub employeeRepository.findByEmail
				//Stubs are used to replace real objects or components that the code under test relies on.
			given(employeeRepository.findByEmail(employee.getEmail()))
			.willReturn(Optional.of(employee));
			
			System.out.println(employeeRepository);
			System.out.println(employeeService);
			//when - action or the behaviour we are going to test
			assertThrows(ResourceNotFoundException.class, () -> {
				employeeService.saveEmployee(employee);
			});
			//then - verify the output
			//after throw we never save because code done
			verify(employeeRepository, never()).save(any(Employee.class));
		}
		
		//JUnit Service test for getAllEmployees method
		@DisplayName("JUnit Service test for getAllEMployees method")
		@Test
		public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
			//given - precondition or setup
			Employee employee1 = Employee.builder()
					.firstName("trach")
					.lastName("tracher")
					.email("tracher@xl1.com")
					.build();
			//stub
			given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
			//when - action or the behaviour we are going to test
			List<Employee> employees = employeeService.getAllEmployees();
			//then - verify the output
			assertThat(employees).isNotNull();
			assertThat(employees.size()).isEqualTo(2);
		}
		
		//JUnit Service test for getAllEmployees method empty (negative scenario)
		@DisplayName("JUnit Service test for getAllEMployees method (negative scenario)")
		@Test
		public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
			//given - precondition or setup
			//stub
			given(employeeRepository.findAll()).willReturn(Collections.emptyList());
			//when - action or the behaviour we are going to test
			List<Employee> employees = employeeService.getAllEmployees();
			//then - verify the output
			assertThat(employees).isEmpty();
			assertThat(employees.size()).isEqualTo(0);
		}
		
		//JUnit Service test for getEmployeeById method
		@DisplayName("JUnit Service test for getEmployeeById method")
		@Test
		public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
			//given - precondition or setup
			given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
			//when - action or the behaviour we are going to test
			Optional<Employee> employeeDB = employeeService.getEmployeeById(1L);
			//then - verify the output
			assertThat(employeeDB.isPresent()).isTrue();
		}
		
		//JUnit service test for UpdateEmployee method
		@DisplayName("JUnit service test for UpdateEmployee method")
		@Test
		public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {
			//given - precondition or setup
			given(employeeRepository.save(employee)).willReturn(employee);
			employee.setEmail("minpik@xl1.io");
			//when - action or the behaviour we are going to test
			Employee savedEmployee = employeeService.saveEmployee(employee);
			System.out.println(savedEmployee.toString());
			//then - verify the output
			assertThat(savedEmployee).isNotNull();
			assertThat(savedEmployee.getEmail()).isEqualTo("minpik@xl1.io");
		}
		
		//JUnit Service test for deleteEmployee
		@DisplayName("JUnit Service test for deleteEmployee")
		@Test
		public void givenEmployeeId_whenDeleteEmployee_thhenNoThing() {
			//given - precondition or setup
			Long employeeId = 1L;
			BDDMockito.willDoNothing().given(employeeRepository).deleteById(employeeId);
			//when - action or the behaviour we are going to test
			employeeService.deleteEmployee(employeeId);
			//then - verify the output
			//Nothing to validate
			verify(employeeRepository, new Times(1)).deleteById(employeeId);
		}

}
