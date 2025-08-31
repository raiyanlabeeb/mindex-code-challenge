package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure read(String id) {
        //get the associated employee
        Employee employee = employeeService.read(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        LOG.debug("Successfully created reporting structure with id [{}]", id);
        return new ReportingStructure(employee, getNumReports(employee.getDirectReports()));
    }

    public int getNumReports(List<Employee> list){
        int count = 0;
        HashSet<Employee> visited = new HashSet<>();

        for (Employee employee : list) {
            if (!visited.contains(employee)){
                count++;
                visited.add(employee);
            }
        }

        return count;
    }
}
