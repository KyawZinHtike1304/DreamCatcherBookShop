package com.example.bookshop.controller;

import com.example.bookshop.dao.BookBoughtDao;
import com.example.bookshop.entity.BooksBought;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Order;
import com.example.bookshop.entity.PaymentMethod;
import com.example.bookshop.service.AuthService;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CartService cartService;
    private final CustomerService customerService;

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("customer",new Customer());
        return "register";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        return "login";
    }

    @PostMapping("/save-customer")
    public String saveCustomer(@RequestParam("billingAddress") String billingAddress,
                               @RequestParam("shippingAddress") String shippingAddress,
                               @RequestParam("payment") PaymentMethod method,
                               @ModelAttribute("totalPrice") double totalPrice,
                               Customer customer, BindingResult result){

        Order order = new Order(
                LocalDate.now(),
                billingAddress,
                shippingAddress,
                method,
                totalPrice
        );
        if (result.hasErrors()){
            return "register";
        }

        authService.register(customer,order);
        customerService.saveCustomerOrderItems(customer);
        return "redirect:/home";
    }

    private final BookBoughtDao bookBoughtDao;

    @GetMapping("/info")
    public ModelAndView checkInfo(ModelMap map, @ModelAttribute("totalPrice") double totalPrice, Principal principal){
        map.put("cartItems",cartService.getCartItems());
        map.put("totalPrice",totalPrice);
        String userName = principal.getName();
        BooksBought booksBought = bookBoughtDao.findBooksBoughtByCustomerName(userName)
                .orElseThrow(EntityNotFoundException::new);


        ModelAndView mv = new ModelAndView();
        mv.addObject("cartItems",cartService.getCartItems());
        mv.addObject("totalPrice",totalPrice);
        mv.addObject("customerInfo",
                authService.findCustomerInfoByCustomerName(booksBought.getCustomerName()));
        mv.setViewName("info");

        return mv;
    }


    @GetMapping("/login")
    public String login(){
            return "login";
    }

    @ModelAttribute("totalPrice")
    public double totalAmount(){
        Optional<Double> optionalDouble = cartService.getCartItems()
                .stream()
                .map(c -> c.getQuantity() * c.getPrice())
                .reduce(Double::sum);

        return optionalDouble.orElse(0.0);
    }
}
