package five.ind.ems_mvc.security;

import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.Role;
import five.ind.ems_mvc.repository.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //find emp by username
        Employee employee = employeeRepository.findEmployeeByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Employee not found with username: " + username));

        String roleName = "ROLE_" + employee.getRole().getId().getRoleName().toUpperCase();
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(roleName));

        return new User(employee.getUsername(), employee.getPassword(), authorities);
    }
}
