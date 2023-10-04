package com.xl1.testing.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl1.testing.entity.Employee;
import com.xl1.testing.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void setup() {
		employeeRepository.deleteAll();
	}
	
	//JUnit integration test for CreateEmployee method
		@DisplayName("JUnit Integration test for CreateEmployee method")
		@Test
		public void givenEmployeeObject_whenCreateEmplyee_thenReturnSavedEmployee() throws JsonProcessingException, Exception {
			//given - precondition or setup
			Employee employee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			
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
		
		//JUnit integration test for getAllEmployees method
		@DisplayName("Junt integration test for getAllEmployees method")
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
			employeeRepository.saveAll(employees);
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(get("/api/employees"));
			//then - verify the output
			response
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(employees.size())));
		}
		
		//JUnit integration test for GetEmployeeById Rest API (positive scenario)
		@DisplayName("Junt integration test for GetEmployeeById Rest API (positive scenario)")
		@Test
		public void givenEMployeeId_whenGetEMployeeById_thenReturnEmployeeObject() throws Exception {
			//given - precondition or setup
			Employee employee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			employeeRepository.save(employee);
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));
			//then - verify the output
			response
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
		}
		
		//JUnit integration test for GetEmployeeById Rest API (negative scenario)
		@DisplayName("Junt integration test for GetEmployeeById Rest API (negative scenario)")
		@Test
		public void givenEMployeeId_whenGetEMployeeById_thenReturnEmpty() throws Exception {
			//given - precondition or setup
			Long employeeId = 1L;
			Employee employee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			employeeRepository.save(employee);
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
			//then - verify the output
			response
				.andExpect(status().isNotFound())
				.andDo(print());
		}
		
		//JUnit integration test for UpdateEmployee (positive scenario)
		@DisplayName("JUnit integration test for UpdateEmployee (positive scenario)")
		@Test
		public void givenUpdateEmplyee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
			//given - precondition or setup
			Employee savedEmployee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			
			employeeRepository.save(savedEmployee);
			
			Employee updatedEmployee = Employee.builder()
					.firstName("Trach")
					.lastName("tracher")
					.email("trach@xl1.io")
					.build();
			
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
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
		
		//JUnit integration test for UpdateEmployee (negative scenario)
		@DisplayName("JUnit integration test for UpdateEmployee (negative  scenario)")
		@Test
		public void givenUpdateEmplyee_whenUpdateEmployee_thenReturnEmpty() throws Exception {
			//given - precondition or setup
			Long employeeId = 1L;
			Employee savedEmployee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			
			employeeRepository.save(savedEmployee);
			
			Employee updatedEmployee = Employee.builder()
					.firstName("Trach")
					.lastName("tracher")
					.email("trach@xl1.io")
					.build();
			
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updatedEmployee)));
			//then - verify the output
			response
				.andExpect(status().isNotFound())
				.andDo(print());
		}
		
		//JUnit integration test for deleteEmployee
		@DisplayName("Junt integration test for deleteEmployee")
		@Test
		public void givenEMployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
			//given - precondition or setup
			//Long employeeId = 1L;
			Employee savedEmployee = Employee.builder()
					.firstName("minpik")
					.lastName("minpiker")
					.email("minpik@xl1.com")
					.build();
			
			employeeRepository.save(savedEmployee);
			
			//when - action or the behaviour we are going to test
			ResultActions response = mockMvc.perform(delete("/api/employees/{id}",savedEmployee.getId()));
			//then - verify the output
			response
				.andExpect(status().isOk())
				.andDo(print());
		}

}
