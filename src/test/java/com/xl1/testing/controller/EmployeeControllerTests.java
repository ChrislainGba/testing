package com.xl1.testing.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import static org.hamcrest.CoreMatchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl1.testing.entity.Employee;
import com.xl1.testing.service.EmployeeService;

@WebMvcTest
public class EmployeeControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean //insted of @Mock because we need to register this bean in the context
	private EmployeeService employeeService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//JUnit Controller test for CreateEmployee method
	@DisplayName("JUnit Controller test for CreateEmployee method")
	@Test
	public void givenEmployeeObject_whenCreateEmplyee_thenReturnSavedEmployee() throws JsonProcessingException, Exception {
		//given - precondition or setup
		Employee employee = Employee.builder()
				.firstName("minpik")
				.lastName("minpiker")
				.email("minpik@xl1.com")
				.build();
		given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
			.willAnswer(invocation -> invocation.getArgument(0));
		//when - action or the behaviour we are going to test
		ResultActions response = mockMvc.perform(post("/api/employees")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(employee)));
		//then - verify the output
		//$= the root acces of the returned object or List
		response
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
			.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	//JUnit controller test for getAllEmployees method
	@DisplayName("Junt controller test for getAllEmployees method")
	@Test
	public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		//given - precondition or setup
		List<Employee> employees = new ArrayList<>();
		employees.addAll(List.of(Employee.builder()
				.firstName("minpik")
				.lastName("minpiker")
				.email("minpik@xl1.com")
				.build(),
				Employee.builder()
				.firstName("trach")		
				.lastName("tracher")
				.email("trach@xl1.com")
				.build()
				));
		
		given(employeeService.getAllEmployees()).willReturn(employees);
		//when - action or the behaviour we are going to test
		ResultActions response = mockMvc.perform(get("/api/employees"));
		//then - verify the output
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.size()", is(employees.size())));
	}
	
	//JUnit controller test for GetEmployeeById Rest API (positive scenario)
	@DisplayName("Junt ontroller test for GetEmployeeById Rest API (positive scenario)")
	@Test
	public void givenEMployeeId_whenGetEMployeeById_thenReturnEmployeeObject() throws Exception {
		//given - precondition or setup
		Long employeeId = 1L;
		Employee employee = Employee.builder()
				.firstName("minpik")
				.lastName("minpiker")
				.email("minpik@xl1.com")
				.build();
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
		//when - action or the behaviour we are going to test
		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
		//then - verify the output
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
			.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	//JUnit controller test for GetEmployeeById Rest API (negative scenario)
		@DisplayName("Junt ontroller test for GetEmployeeById Rest API (negative scenario)")
		@Test
		public void givenEMployeeId_whenGetEMployeeById_thenReturnEmpty() throws Exception {
			//given - precondition or setup
			Long employeeId = 1L;
			Employee employee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
			//then - verify the output
			response
				.andExpect(status().isNotFound())
				.andDo(print());
		}
		
		//JUnit controller test for UpdateEmployee (positive scenario)
		@DisplayName("JUnit controller test for UpdateEmployee (positive scenario)")
		@Test
		public void givenUpdateEmplyee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
			//given - precondition or setup
			Long employeeId = 1L;
			Employee savedEmployee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			
			Employee updatedEmployee = Employee.builder()
					.firstName("Trach")
					.lastName("tracher")
					.email("trach@xl1.io")
					.build();
			given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
			given(employeeService.updateEmployee(any(Employee.class)))
			.willAnswer(invocation -> invocation.getArgument(0));
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updatedEmployee)));
			//then - verify the output
			response
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
		}
		
		//JUnit controller test for UpdateEmployee (negative scenario)
		@DisplayName("JUnit controller test for UpdateEmployee (negative  scenario)")
		@Test
		public void givenUpdateEmplyee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
			//given - precondition or setup
			Long employeeId = 1L;
			Employee savedEmployee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			
			Employee updatedEmployee = Employee.builder()
					.firstName("Trach")
					.lastName("tracher")
					.email("trach@xl1.io")
					.build();
			given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
			given(employeeService.updateEmployee(any(Employee.class)))
			.willAnswer(invocation -> invocation.getArgument(0));
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updatedEmployee)));
			//then - verify the output
			response
				.andExpect(status().isNotFound())
				.andDo(print());
		}
		
		//JUnit controller test for deleteEmployee
		@DisplayName("Junt controller test for deleteEmployee")
		@Test
		public void givenEMployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
			//given - precondition or setup
			Long employeeId = 1L;
			willDoNothing().given(employeeService).deleteEmployee(employeeId);
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));
			//then - verify the output
			response
				.andExpect(status().isOk())
				.andDo(print());
		}
		
		
}
