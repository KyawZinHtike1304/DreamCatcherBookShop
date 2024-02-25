package com.example.bookshop.service;

import com.example.bookshop.dao.CustomerDao;
import com.example.bookshop.dao.OrderDao;
import com.example.bookshop.dao.RoleDao;
import com.example.bookshop.dto.CustomerOrder;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Order;
import com.example.bookshop.entity.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerDao customerDao;
    private final RoleDao roleDao;
    private final OrderDao orderDao;

    public CustomerOrder findCustomerInfoByCustomerName(String customerName){
        return customerDao.customerOrderInfo(customerName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void register(Customer customer , Order order){
        Role role = roleDao.findRoleByRoleName("ROLE_USER")
                .orElseThrow(EntityNotFoundException::new);

       customer.setPassword(passwordEncoder.encode(customer.getPassword()));

       customer.addRole(role);
       Order managedOrder = orderDao.save(order);
       customer.addOrder(order);
       customerDao.save(customer);
    }

    public boolean isLoggedIn() {
        // Access SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();

        // Check for non-null authentication object and authentication status
        return context.getAuthentication() != null && context.getAuthentication().isAuthenticated();
    }


}
