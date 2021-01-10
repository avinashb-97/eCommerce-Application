package com.example.demo;


import com.example.demo.controller.CartControllerTest;
import com.example.demo.controller.ItemControllerTest;
import com.example.demo.controller.OrderControllerTest;
import com.example.demo.controller.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CartControllerTest.class, ItemControllerTest.class, OrderControllerTest.class, UserControllerTest.class})
public class ECommerceTestSuite {

}