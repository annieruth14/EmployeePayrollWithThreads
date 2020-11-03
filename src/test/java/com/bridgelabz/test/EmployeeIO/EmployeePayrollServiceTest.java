package com.bridgelabz.test.EmployeeIO;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bridgelabz.test.EmployeeIO.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {
	EmployeePayrollService employeePayrollService;
	
	@Before
	public void initialze() {
		EmployeePayroll[] arraysOfEmp = {
				new EmployeePayroll(1, "Jeffy", 500),
				new EmployeePayroll(2, "Bill", 600),
				new EmployeePayroll(3, "Mark", 800)
		};
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arraysOfEmp));
	}
	
	@Test
	public void givenEmployees_whenWrittenToFile_shouldMatchEmployee(){
		employeePayrollService.writeData(EmployeePayrollService.IOService.FILE_IO);
		employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}
	@Test
	public void givenEmployeesInFile_whenAddedToList_shouldMatchEntries(){
		long entries = employeePayrollService.fileToList(EmployeePayrollService.IOService.FILE_IO);
		System.out.println(entries);
		Assert.assertEquals(3, entries);
	}
	
	// size of entries in database
	@Test
	public void givenpayrollDB_whenRetrieve_shouldMatchCount() throws EmployeePayrollException {
		List<EmployeePayroll> list = employeePayrollService.readData(EmployeePayrollService.IOService.DB_IO);
		//Assert.assertEquals(3, list.size());
		System.out.println(list.size());
	}
	 
	@Test
	public void givenNewSalary_whenUpdated_shouldReturnSynchWithDB() throws EmployeePayrollException {
		 List<EmployeePayroll> list = employeePayrollService.readData(IOService.DB_IO);
		 employeePayrollService.updateSalary("Clare",5000);
		 boolean result = employeePayrollService.checkEmployeePayrollInSync("Clare");
		 Assert.assertTrue(result);
	}
	
	@Test 
	public void givenDateRange_shouldReturnEmployee() throws EmployeePayrollException {
		List<EmployeePayroll> list1 = employeePayrollService.getEmployeeInDateRange("2020-01-13", "2020-06-13");
		Assert.assertEquals(2, list1.size());
	}
	
	@Test  
	public void givenSalary_whenFindSum_shouldReturnSum() throws EmployeePayrollException {
		double salary = employeePayrollService.getSumByGender("F");
		Assert.assertEquals(12000, salary, 0);
	}
	
	@Test
	public void givenNewEmployee_whenAdded_shouldBeSyncWithDB() throws EmployeePayrollException {
		employeePayrollService.readData(IOService.DB_IO);
		ArrayList<String> departmentList = new ArrayList<>();
		departmentList.add("Sales");
		departmentList.add("Marketing");
		employeePayrollService.addEmployeeToPayroll("Ritu",7000, LocalDate.now(), "M", 3, departmentList, "Reliance");
		boolean result = employeePayrollService.checkEmployeePayrollInSync("Ritu");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenEmployee_whenDeleted_shouldBeRemovedFromEmployeeList() throws EmployeePayrollException {
		employeePayrollService.readData(IOService.DB_IO);
		List<EmployeePayroll> list = employeePayrollService.deleteEmployee("Kiran",false);
		//Assert.assertEquals(4, list.size());
	}
	
	@Test 
	public void givenEntries_whenAddedToDB_shouldMatchEntries() throws EmployeePayrollException {
		EmployeePayroll[] arraysOfEmp = {
				new EmployeePayroll(0, "Jeffy", 500,LocalDate.now(),"M"),
				new EmployeePayroll(0, "Bill", 600,LocalDate.now(), "M"),
				new EmployeePayroll(0, "Mark", 800,LocalDate.now(), "M")
		};
		employeePayrollService.readData(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeeToList(Arrays.asList(arraysOfEmp));
		Instant end =Instant.now();
		System.out.println("Duration without thread: "+ Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayrollService.addEmployeeToListWithThreads(Arrays.asList(arraysOfEmp));
		Instant threadEnd =Instant.now();
		System.out.println("Duration with thread: "+ Duration.between(threadStart, threadEnd));
		employeePayrollService.printData(IOService.DB_IO);
		Assert.assertEquals(12, employeePayrollService.countEntries(IOService.DB_IO));
	}
	
	@Test
	public void givenNewSalary_whenUpdated_shouldMatch() throws EmployeePayrollException {
		List<EmployeePayroll> list = employeePayrollService.readData(IOService.DB_IO);
		Map<Integer, Double> nameSalaryMap = new HashMap<>();
		nameSalaryMap.put(17, (double) 11000);
		nameSalaryMap.put(31, (double) 50000);
		nameSalaryMap.put(77, (double) 20000);
		Instant start = Instant.now();
		employeePayrollService.updateSalary(nameSalaryMap);
		Instant end =Instant.now();
		System.out.println("Duration with thread: "+ Duration.between(start, end));
		boolean result = employeePayrollService.checkEmployeePayrollInSync("Bill");
		Assert.assertTrue(result);
	}
}
