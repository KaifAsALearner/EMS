package five.ind.ems_mvc.service;

import five.ind.ems_mvc.dto.EmployeeDto;
import five.ind.ems_mvc.entity.Employee;

import java.util.List;

public interface EmployeeService {
    void saveEmployee(EmployeeDto employeeDto);
    Employee findByUsername(String username);
    List<EmployeeDto> findAllEmployees();
}
