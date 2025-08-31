package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String employeeId = compensation.getEmployeeId();
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        //We need to make sure the employee id belongs to a valid employee
        if (employee == null) {
            throw new RuntimeException("Invalid compensation: " + employeeId);
        }

        compensationRepository.insert(compensation);

        LOG.debug("Successfully created compensation [{}]", compensation); //added for debugging purposes
        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation with id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid compensation: " + id);
        }
        LOG.debug("Successfully read compensation [{}]", compensation); //added for debugging purposes
        return compensation;
    }

    @Override
    public Compensation update(Compensation compensation) {
        return null;
    }
}
