package com.xl1.testing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xl1.testing.entity.Employee;
//JpaRepository is have SimpleJpaRepository that is already anotated by @Repository
 
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	Optional<Employee> findByEmail(String email);
	//JPQL with index params
	@Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
	Employee findByJPQLIndexParams(String firstName,String lastName);
	
	//JPQL with name params
	@Query("select e from Employee e where e.firstName =:firstname and e.email =:email")
	Employee findByJPQLNamedParams(@Param("firstname") String firstName,@Param("email") String email);
	
	@Query(value = "select * from employee where first_name = ?1 and last_name = ?2 order by last_name ASC",nativeQuery = true)
	Employee findByNativeSQLIndex(String firstName,String lastName);
	
	@Query(value = "select * from employee where first_name =:firstname and last_name =:lastname order by last_name ASC",nativeQuery = true)
	Employee findByNativeSQLNamed(@Param("firstname")String firstName, @Param("lastname")String lastName);
	
	
}
