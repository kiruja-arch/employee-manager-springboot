
package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display all employees
    @GetMapping("/")
    public String listEmployees(Model model) {
        model.addAttribute("employees",
                employeeService.getAllEmployees());
        return "employees";
    }

    // Show create employee form
    @GetMapping("/employees/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    // Save new employee
    @PostMapping("/employees")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {

        if (result.hasErrors()) {
            return "employee-form";
        }

        employeeService.saveEmployee(employee);
        return "redirect:/";
    }

    // Show edit employee form
    @GetMapping("/employees/edit/{id}")
    public String showEditForm(
            @PathVariable Long id, Model model) {

        Employee employee = employeeService
                .getEmployeeById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid employee Id: " + id));

        model.addAttribute("employee", employee);
        return "employee-form";
    }

    // Update employee
    @PostMapping("/employees/update/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result) {

        if (result.hasErrors()) {
            return "employee-form";
        }

        employee.setId(id);
        employeeService.saveEmployee(employee);

        return "redirect:/";
    }

    // Delete employee
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/";
    }
}
